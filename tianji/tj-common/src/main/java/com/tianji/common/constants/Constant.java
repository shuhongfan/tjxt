package com.tianji.common.constants;

public interface Constant {
    String REQUEST_ID_HEADER = "requestId";
    String REQUEST_FROM_HEADER = "x-request-from";

    String GATEWAY_ORIGIN_NAME = "gateway";
    String FEIGN_ORIGIN_NAME = "feign";

    // 数据字段 - id
    String DATA_FIELD_NAME_ID = "id";

    // 数据字段 - create_time
    String DATA_FIELD_NAME_CREATE_TIME = "create_time";
    String DATA_FIELD_NAME_CREATE_TIME_CAMEL = "createTime";

    // 数据字段 - update_time
    String DATA_FIELD_NAME_UPDATE_TIME = "update_time";
    String DATA_FIELD_NAME_UPDATE_TIME_CAMEL = "updateTime";

    // 数据字段 - liked_times
    String DATA_FIELD_NAME_LIKED_TIME = "liked_times";
    String DATA_FIELD_NAME_LIKED_TIME_CAMEL = "likedTimes";

    // 数据字段 - creater
    String DATA_FIELD_NAME_CREATER = "creater";

    // 数据字段 - updater
    String DATA_FIELD_NAME_UPDATER = "updater";

    // 数据已经删除标识值
    boolean DATA_DELETE = true;
    // 数据未删除标识值
    boolean DATA_NOT_DELETE = false;
    // 响应结果是否被R标记过
    String BODY_PROCESSED_MARK_HEADER = "IS_BODY_PROCESSED";





}
