package com.tianji.media.storage.tencent;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.hutool.jwt.JWT;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.BasicSessionCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.Upload;
import com.qcloud.vod.common.FileUtil;
import com.qcloud.vod.common.StringUtil;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.*;
import com.tianji.common.exceptions.CommonException;
import com.tianji.common.utils.StringUtils;
import com.tianji.media.config.TencentProperties;
import com.tianji.media.domain.po.Media;
import com.tianji.media.enums.FileStatus;
import com.tianji.media.storage.IMediaStorage;
import com.tianji.media.storage.MediaUploadResult;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.tianji.media.enums.FileErrorInfo.Msg.*;

@Slf4j
public class TencentMediaStorage implements IMediaStorage {
    private final VodClient vodClient;
    private final TencentProperties tencentProperties;

    public TencentMediaStorage(VodClient vodClient, TencentProperties tencentProperties) {
        this.vodClient = vodClient;
        this.tencentProperties = tencentProperties;
    }

    private static final String CONTEXT_TEMPLATE =
            "secretId=%s&currentTimeStamp=%d&expireTime=%d&random=%d";
    private static final String CONTEXT_TEMPLATE_WITH_PROCEDURE =
            "secretId=%s&currentTimeStamp=%d&expireTime=%d&random=%d&procedure=%s";
    private static final String[] MEDIA_INFO_FILTERS = new String[]{"basicInfo", "metaData"};
    @Override
    public String getUploadSignature() {
        // 1.获取加密工具
        HMac mac = new HMac(HmacAlgorithm.HmacSHA1, tencentProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
        // 2.准备加密数据
        long now = System.currentTimeMillis() / 1000;
        long endTime = now + tencentProperties.getVod().getVodValidSeconds();
        String procedure = tencentProperties.getVod().getProcedure();
        String context;
        if (StringUtils.isBlank(procedure)) {
            context = String.format(CONTEXT_TEMPLATE,
                    URLEncoder.encode(tencentProperties.getSecretId(), StandardCharsets.UTF_8),
                    now,
                    endTime,
                    RandomUtil.randomInt(0, Integer.MAX_VALUE)
            );
        }else{
            context = String.format(CONTEXT_TEMPLATE_WITH_PROCEDURE,
                    URLEncoder.encode(tencentProperties.getSecretId(), StandardCharsets.UTF_8),
                    now,
                    endTime,
                    RandomUtil.randomInt(0, Integer.MAX_VALUE),
                    procedure
            );
        }

        // 3.加密返回
        byte[] bytes = ArrayUtil.addAll(mac.digest(context), context.getBytes(StandardCharsets.UTF_8));
        return Base64.encode(bytes);
    }

    @Override
    public String getPlaySignature(String fieldId, Long userId, Integer freeExpired) {
        long currentTime = System.currentTimeMillis() / 1000;

        HashMap<String, Object> urlAccessInfo = new HashMap<>(2);
       /* if (userId != null) {
            urlAccessInfo.put("uid", String.valueOf(userId));
        }*/
        if (freeExpired != null) {
            urlAccessInfo.put("exper", freeExpired * 60);
        }
        return JWT.create()
                .setKey(tencentProperties.getVod().getUrlKey().getBytes(StandardCharsets.UTF_8))
                .setPayload("appId", tencentProperties.getAppId())
                .setPayload("fileId", fieldId)
                .setPayload("currentTimeStamp", currentTime)
                .setPayload("pcfg", tencentProperties.getVod().getPfcg())
                .setPayload("urlAccessInfo", urlAccessInfo)
                .sign();
    }

    @Override
    public MediaUploadResult uploadFile(String filename, InputStream inputStream, long contentLength) {
        CommitUploadResponse response = null;
        try {
            // 1.申请上传
            ApplyUploadResponse applyUploadResponse = applyUpload(filename);
            // 2.开始上传
            handleUpload(inputStream, contentLength, applyUploadResponse);
            // 3.确认上传
            response = commitUpload(applyUploadResponse);
        } catch (Exception e) {
            log.error("上传视频文件【{}】失败。", filename, e);
            throw new CommonException(MEDIA_UPLOAD_ERROR, e);
        }
        // 3.解析结果
        MediaUploadResult result = new MediaUploadResult();
        result.setFileId(response.getFileId());
        result.setMediaUrl(response.getMediaUrl());
        result.setCoverUrl(response.getCoverUrl());
        result.setRequestId(response.getRequestId());
        return result;
    }

    private CommitUploadResponse commitUpload(ApplyUploadResponse applyUploadResponse) {
        CommitUploadRequest request = new CommitUploadRequest();
        request.setVodSessionKey(applyUploadResponse.getVodSessionKey());
        TencentCloudSDKException err = null;
        int i = 0;
        while (i < 4) {
            try {
                return vodClient.CommitUpload(request);
            } catch (TencentCloudSDKException e) {
                if (StringUtil.isEmpty(e.getRequestId())) {
                    err = e;
                    ++i;
                    continue;
                }

                throw new CommonException(MEDIA_COMMIT_UPLOAD_ERROR, e);
            }

        }
        throw new CommonException(MEDIA_COMMIT_UPLOAD_ERROR, err);
    }

    private void handleUpload(InputStream inputStream, long contentLength, ApplyUploadResponse applyUploadResponse) {
        // 1.整理授权上传的凭证
        COSCredentials credentials = null;
        if (applyUploadResponse.getTempCertificate() != null) {
            TempCertificate certificate = applyUploadResponse.getTempCertificate();
            credentials = new BasicSessionCredentials(certificate.getSecretId(), certificate.getSecretKey(), certificate.getToken());
        } else {
            credentials = new BasicCOSCredentials(tencentProperties.getSecretId(), tencentProperties.getSecretKey());
        }
        // 2.准备上传客户端
        ClientConfig clientConfig = new ClientConfig(new Region(applyUploadResponse.getStorageRegion()));
        clientConfig.setHttpProtocol(HttpProtocol.https);
        COSClient cosClient = new COSClient(credentials, clientConfig);
        TransferManager transferManager = new TransferManager(cosClient);
        try {
            // 3.开始上传
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(contentLength);
            Upload upload = transferManager.upload(applyUploadResponse.getStorageBucket(), applyUploadResponse.getMediaStoragePath(), inputStream, metadata);
            // 4.等待上传完成
            upload.waitForCompletion();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // 5.关闭客户端
            transferManager.shutdownNow();
        }
    }

    private ApplyUploadResponse applyUpload(String filename) {
        ApplyUploadRequest req = new ApplyUploadRequest();
        req.setMediaName(filename);
        req.setMediaType(FileUtil.getFileType(filename));
        req.setProcedure(tencentProperties.getVod().getProcedure());
        TencentCloudSDKException err = null;
        int i = 0;
        while (i < 4) {
            try {
                // 返回的resp是一个ApplyUploadResponse的实例，与请求对象对应
                return vodClient.ApplyUpload(req);
            } catch (TencentCloudSDKException e) {
                if (StringUtil.isEmpty(e.getRequestId())) {
                    err = e;
                    ++i;
                    continue;
                }
                throw new CommonException(MEDIA_APPLY_UPLOAD_ERROR, e);
            }
        }
        throw new RuntimeException(MEDIA_APPLY_UPLOAD_ERROR, err);
    }

    @Override
    public void deleteFile(String fileId) {
        try {
            DeleteMediaRequest request = new DeleteMediaRequest();
            request.setFileId(fileId);
            vodClient.DeleteMedia(request);
        } catch (TencentCloudSDKException e) {
            throw new CommonException(MEDIA_DELETE_ERROR, e);
        }
    }

    @Override
    public void deleteFiles(List<String> fileIds) {
        for (String fileId : fileIds) {
            deleteFile(fileId);
        }
    }

    @Override
    public List<Media> queryMediaInfos(String ... fileIds) {
        // 1.请求参数
        DescribeMediaInfosRequest req = new DescribeMediaInfosRequest();
        req.setFileIds(fileIds);
        req.setFilters(MEDIA_INFO_FILTERS);
        // 2.发送请求
        DescribeMediaInfosResponse resp = null;
        try {
            resp = vodClient.DescribeMediaInfos(req);
        } catch (TencentCloudSDKException e) {
            throw new RuntimeException("获取媒资信息异常", e);
        }

        // 3.解析结果
        MediaInfo[] mediaInfoSet = resp.getMediaInfoSet();
        if (mediaInfoSet == null || mediaInfoSet.length == 0) {
            return Collections.emptyList();
        }
        // 4.数据转换
        List<Media> list = new ArrayList<>(mediaInfoSet.length);
        for (MediaInfo info : mediaInfoSet) {
            Media media = new Media();
            media.setFileId(info.getFileId());
            MediaMetaData md = info.getMetaData();
            MediaBasicInfo bi = info.getBasicInfo();
            media.setMediaUrl(bi.getMediaUrl());
            media.setFilename(bi.getName());
            media.setSize(md.getSize());
            media.setDuration(md.getDuration());
            media.setStatus(FileStatus.UPLOADED);
            list.add(media);
        }
        return list;
    }
}
