package com.tianji.media.enums;

public interface FileErrorInfo {
    interface Msg {
        String FILE_KEY_TOO_MANY = "文件key不能超过1000";
        String BUCKET_NAME_IS_NULL = "桶名称不能为空";
        String FILE_KEY_IS_NULL = "文件key不能为空";
        String INVALID_FILE_STATUS = "无效的文件状态";

        String FILE_UPLOAD_ERROR = "上传文件失败";


        String MEDIA_APPLY_UPLOAD_ERROR = "申请上传视频失败";
        String MEDIA_UPLOAD_ERROR = "上传视频失败";
        String MEDIA_COMMIT_UPLOAD_ERROR = "确认上传视频失败";
        String MEDIA_DELETE_ERROR = "删除视频失败";

    }
}
