package com.tianji.media.storage;

import java.io.InputStream;
import java.util.List;

public interface IFileStorage {

    /**
     * 上传文件
     * @param key 文件唯一标识（a.jpg)
     * @param inputStream 文件流
     * @return requestId
     */
    String uploadFile(String key, InputStream inputStream, long contentLength);

    /**
     * 下载文件
     * @param key 文件唯一标识（a.jpg)
     * @return 文件流
     */
    InputStream downloadFile(String key);

    /**
     * 删除指定文件
     * @param key 文件唯一标识（a.jpg)
     */
    void deleteFile(String key);

    /**
     * 删除指定文件
     * @param keys 文件唯一标识（a.jpg)的集合
     */
    void deleteFiles(List<String> keys);
}
