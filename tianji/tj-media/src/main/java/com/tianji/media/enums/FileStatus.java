package com.tianji.media.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.tianji.common.exceptions.BadRequestException;

import static com.tianji.media.enums.FileErrorInfo.Msg.INVALID_FILE_STATUS;


public enum FileStatus {
    UPLOADING(1, "上传中"),
    UPLOADED(2, "已上传"),
    PROCESSED(3, "已处理"),
    ;
    @EnumValue
    private int value;
    private String desc;

    FileStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static FileStatus of(int value) {
        switch (value) {
            case 1:
                return UPLOADING;
            case 2:
                return UPLOADED;
            case 3:
                return PROCESSED;
            default:
                throw new BadRequestException(INVALID_FILE_STATUS);
        }
    }
}
