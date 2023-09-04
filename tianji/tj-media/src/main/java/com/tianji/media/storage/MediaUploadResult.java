package com.tianji.media.storage;

import lombok.Data;

@Data
public class MediaUploadResult {

    private String fileId;

    private String mediaUrl;

    private String coverUrl;

    private String requestId;

    private String filename;
}
