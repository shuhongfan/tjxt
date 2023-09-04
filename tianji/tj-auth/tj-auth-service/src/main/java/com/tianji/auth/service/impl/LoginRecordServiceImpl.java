package com.tianji.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianji.auth.domain.po.LoginRecord;
import com.tianji.auth.mapper.LoginRecordMapper;
import com.tianji.auth.service.ILoginRecordService;
import com.tianji.common.utils.MarkedRunnable;
import com.tianji.common.utils.WebUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p>
 * 登录信息记录表 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2022-07-12
 */
@Service
public class LoginRecordServiceImpl extends ServiceImpl<LoginRecordMapper, LoginRecord> implements ILoginRecordService {

    private static final Executor WRITE_RECORD_EXECUTOR;

    static {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(20);
        //配置最大线程数
        executor.setMaxPoolSize(40);
        //配置队列大小
        executor.setQueueCapacity(99999);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("login-record-write-worker-");
        // 设置拒绝策略：放弃任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        //执行初始化
        executor.initialize();
        WRITE_RECORD_EXECUTOR = executor;
    }

    @Override
    public void saveAsync(LoginRecord record) {
        WRITE_RECORD_EXECUTOR.execute(new MarkedRunnable(() -> save(record)));
    }

    @Override
    public void loginSuccess(String cellphone, Long userId) {
        LoginRecord record = new LoginRecord();
        LocalDateTime now = LocalDateTime.now();
        record.setLoginTime(now);
        record.setLoginDate(now.toLocalDate());
        record.setUserId(userId);
        record.setCellPhone(cellphone);
        record.setIpv4(WebUtils.getRemoteAddr());
        saveAsync(record);
    }
}
