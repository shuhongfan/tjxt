package com.tianji.common.autoconfigure.mybatis;

import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.tianji.common.utils.ReflectUtils;
import com.tianji.common.utils.UserContext;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;

import java.sql.SQLException;

import static com.tianji.common.constants.Constant.DATA_FIELD_NAME_CREATER;
import static com.tianji.common.constants.Constant.DATA_FIELD_NAME_UPDATER;

public class MyBatisAutoFillInterceptor implements InnerInterceptor {

    @Override
    public void beforeUpdate(Executor executor, MappedStatement ms, Object parameter) throws SQLException {
        //1.更新操作
        updateExe(parameter);
        //2.插入操作
        insertExe(ms, parameter);
    }

    private void insertExe(MappedStatement ms, Object parameter){
        //1.判断当前操作是否是插入操作
        if(ms.getSqlCommandType().compareTo(SqlCommandType.INSERT) == 0) {
            //2.判断是否有updater字段，如果
            if(ReflectUtils.containField(DATA_FIELD_NAME_CREATER, parameter.getClass())){
                Long userId = UserContext.getUser();
                //3.有userId也存在并设置updater
                if(userId != null){
                    //4.当前操作人设置到创建人字段
                    ReflectUtils.setFieldValue(parameter, DATA_FIELD_NAME_CREATER, userId);
                }
            }
        }
    }

    private void updateExe(Object parameter){
        //1.判断是否有updater字段
        if(ReflectUtils.containField(DATA_FIELD_NAME_UPDATER, parameter.getClass())){
            Long userId = UserContext.getUser();
            //2.如果有userId也存在并设置updater
            if(userId != null){
                //3.当前用户设置到更新人字段
                ReflectUtils.setFieldValue(parameter, DATA_FIELD_NAME_UPDATER, userId);
            }
        }
    }
}
