package com.tianji.media.storage;

import com.tianji.media.domain.po.Media;

import java.io.InputStream;
import java.util.List;

public interface IMediaStorage {

    /**
     * 获取临时上传授权签名
     * @return 签名信息
     */
    String getUploadSignature();

    /**
     * 获取临时上传授权签名
     * @param fieldId 视频文件id
     * @param userId 查看视频的用户的id，用于生成水印
     * @param freeExpire  免费试看时长，null则表示不限制时长
     * @return 签名信息
     */
    String getPlaySignature(String fieldId,Long userId, Integer freeExpire);

    /**
     * 上传文件
     * @param filename 文件名称（a.mp4)
     * @param inputStream 文件流
     * @return requestId
     */
    MediaUploadResult uploadFile(String filename, InputStream inputStream, long contentLength);

    /**
     * 删除指定文件
     * @param fileId 文件唯一标识
     */
    void deleteFile(String fileId);

    /**
     * 删除指定文件
     * @param fileIds 文件唯一标识的集合
     */
    void deleteFiles(List<String> fileIds);

    /**
     * 根据fileId查询文件信息
     * @param fileIds 多个文件标示
     * @return 文件信息列表
     */
    List<Media> queryMediaInfos(String ... fileIds);
}
