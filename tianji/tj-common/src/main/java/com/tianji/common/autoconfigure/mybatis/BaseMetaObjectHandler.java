package com.tianji.common.autoconfigure.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.tianji.common.utils.NumberUtils;
import com.tianji.common.utils.UserContext;
import org.apache.ibatis.reflection.MetaObject;

import static com.tianji.common.constants.Constant.DATA_FIELD_NAME_CREATER;
import static com.tianji.common.constants.Constant.DATA_FIELD_NAME_UPDATER;


/**
 * 操作数据库前自动填充需要更新的内容，只支持单个对象，不支持批量插入更新时的填充
 *
 **/
public class BaseMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        //创建人
        setCreater(metaObject);

        //更新人
        setUpdater(metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        //更新数据时，修改更新人
        setUpdater(metaObject);
    }

    private void setCreater(MetaObject metaObject) {
        Long userId = UserContext.getUser();
        //未找到用户id默认0
        this.strictInsertFill(metaObject, DATA_FIELD_NAME_CREATER, Long.class, NumberUtils.null2Zero(userId)); // 起始版本 3.3.0(推荐使用)
    }

    private void setUpdater(MetaObject metaObject) {
        Long userId = UserContext.getUser();
        //未找到用户id默认0
        this.strictUpdateFill(metaObject, DATA_FIELD_NAME_UPDATER, Long.class, NumberUtils.null2Zero(userId)); // 起始版本 3.3.0(推荐)
    }
}
