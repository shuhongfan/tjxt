package com.tianji.media.storage.tencent;

import com.tianji.common.exceptions.BadRequestException;
import com.tianji.common.exceptions.CommonException;
import com.tianji.common.utils.AssertUtils;
import com.tianji.common.utils.CollUtils;
import com.tianji.media.config.TencentProperties;
import com.tianji.media.storage.IFileStorage;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.*;
import com.qcloud.cos.model.DeleteObjectsRequest.KeyVersion;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.Upload;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import static com.tianji.media.enums.FileErrorInfo.Msg.*;

@Slf4j
public class TencentFileStorage implements IFileStorage {

    private final COSClient cosClient;
    private final TransferManager transferManager;
    private final String bucketName;

    public TencentFileStorage(COSClient tencentCosClient, TransferManager transferManager, TencentProperties properties) {
        this.cosClient = tencentCosClient;
        this.transferManager = transferManager;
        this.bucketName = properties.getCos().getBucket() + "-" + properties.getAppId();
    }

    @Override
    public String uploadFile(String key, InputStream inputStream, long contentLength) {
        // 1.数据校验
        AssertUtils.isNotBlank(bucketName, BUCKET_NAME_IS_NULL);
        AssertUtils.isNotBlank(key, FILE_KEY_IS_NULL);
        AssertUtils.isNotNull(inputStream);

        // 2.元信息，主要是文件大小，这样才可以利用分片上传功能
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(contentLength);

        // 3.请求对象
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream, objectMetadata);

        try {
            // 4.异步发起上传，返回异步结果upload
            Upload upload = transferManager.upload(putObjectRequest);
            // 5.等待结果
            UploadResult result = upload.waitForUploadResult();
            // 6.返回信息
            return result.getRequestId();
        } catch (Exception e) {
            log.error("上传文件[{}]时发生异常：", key, e);
            throw new CommonException("文件上传异常。", e);
        }
    }

    @Override
    public InputStream downloadFile(String key) {
        // 1.数据校验
        AssertUtils.isNotBlank(bucketName, BUCKET_NAME_IS_NULL);
        AssertUtils.isNotBlank(key, FILE_KEY_IS_NULL);
        // 2.准备请求参数
        GetObjectRequest request = new GetObjectRequest(bucketName, key);
        try {
            // 3.下载
            COSObject cosObject = cosClient.getObject(request);
            return cosObject.getObjectContent();
        } catch (Exception e) {
            log.error("下载文件[{}]时发生异常：", key, e);
            throw new CommonException("文件下载异常。", e);
        }
    }

    @Override
    public void deleteFile(String key) {
        // 1.数据校验
        AssertUtils.isNotBlank(bucketName, BUCKET_NAME_IS_NULL);
        AssertUtils.isNotBlank(key, FILE_KEY_IS_NULL);
        try {
            // 2.删除
            cosClient.deleteObject(bucketName, key);
        } catch (Exception e) {
            log.error("删除文件[{}]时发生异常：", key, e);
            throw new CommonException("删除异常。", e);
        }

    }

    @Override
    public void deleteFiles(List<String> keys) {
        // 1.数据校验
        if(CollUtils.isEmpty(keys)){
            return;
        }
        AssertUtils.isNotBlank(bucketName, BUCKET_NAME_IS_NULL);
        if(keys.size() > 1000){
            throw new BadRequestException(FILE_KEY_TOO_MANY);
        }
        // 2.准备request
        DeleteObjectsRequest request = new DeleteObjectsRequest(bucketName);
        // 3.设置要删除的key列表, 最多一次删除1000个
        List<KeyVersion> keyList = keys.stream().map(KeyVersion::new).collect(Collectors.toList());
        request.setKeys(keyList);
        try {
            // 4.删除
            cosClient.deleteObjects(request);
        } catch (Exception e) {
            log.error("批量删除文件[{}]时发生异常：", keys, e);
            throw new CommonException("删除异常。", e);
        }
    }
}
