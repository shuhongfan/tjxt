package com.tianji.message.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianji.api.client.user.UserClient;
import com.tianji.api.dto.user.UserDTO;
import com.tianji.common.domain.dto.PageDTO;
import com.tianji.common.utils.BeanUtils;
import com.tianji.common.utils.CollUtils;
import com.tianji.common.utils.MarkedRunnable;
import com.tianji.common.utils.StringUtils;
import com.tianji.message.constants.MessageErrorInfo;
import com.tianji.message.domain.dto.NoticeTaskDTO;
import com.tianji.message.domain.dto.NoticeTaskFormDTO;
import com.tianji.message.domain.po.NoticeTask;
import com.tianji.message.domain.po.NoticeTemplate;
import com.tianji.message.domain.query.NoticeTaskPageQuery;
import com.tianji.message.enums.TemplateStatus;
import com.tianji.message.mapper.NoticeTaskMapper;
import com.tianji.message.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * <p>
 * 系统通告的任务表，可以延期或定期发送通告 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-19
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeTaskServiceImpl extends ServiceImpl<NoticeTaskMapper, NoticeTask> implements INoticeTaskService {

    private final Executor asyncNoticeExecutor;
    private final INoticeTemplateService noticeTemplateService;
    private final UserClient userClient;
    private final IPublicNoticeService publicNoticeService;
    private final IUserInboxService inboxService;
    private final ISmsService smsService;

    @Override
    public Long saveNoticeTask(NoticeTaskFormDTO noticeTaskFormDTO) {
        // 1.保存任务
        NoticeTask noticeTask = BeanUtils.copyBean(noticeTaskFormDTO, NoticeTask.class);
        save(noticeTask);
        Long taskId = noticeTask.getId();
        // 2.判断是否有执行时间
        LocalDateTime pushTime = noticeTask.getPushTime();
        if(pushTime == null || pushTime.isBefore(LocalDateTime.now())){
            // 没有执行时间，或者执行时间小于当前时间，立刻执行任务
            asyncNoticeExecutor.execute(new MarkedRunnable(() -> handleTask(noticeTask)));
        }
        return taskId;
    }

    @Override
    @Transactional
    public void handleTask(NoticeTask task) {
        // 1.获取任务要发送的通知模板
        Long templateId = task.getTemplateId();
        NoticeTemplate noticeTemplate = noticeTemplateService.getById(templateId);
        if(noticeTemplate == null){
            // 模板不存在或者无法使用
            log.error("通知任务无法执行，模板id【{}】，原因：{}", templateId, MessageErrorInfo.NOTICE_TEMPLATE_NOT_EXISTS);
            return;
        }
        if(noticeTemplate.getStatus() != TemplateStatus.IN_SERVICE.getValue()){
            // 模板不存在或者无法使用
            log.error("通知任务无法执行，模板id【{}】，原因：{}", templateId, MessageErrorInfo.NOTICE_TEMPLATE_CANNOT_USE);
            return;
        }
        // 2.获取通知对应的目标用户
        List<UserDTO> users = null;
        if (task.getPartial()) {
            // 针对部分用户，需要查询用户信息
            List<Long> userIds = getBaseMapper().queryTaskTargetByTaskId(task.getId());
            if(CollUtils.isNotEmpty(userIds)){
                users = userClient.queryUserByIds(userIds);
            }
        }

        // 3.判断是全部用户还是部分
        if (CollUtils.isEmpty(users)) {
            // 3.1.全部用户，直接存入公告箱，用户查看消息时才拉取(pull mode)
            publicNoticeService.saveNoticeOfTemplate(noticeTemplate);
        }else{
            // 3.2.部分用户，需要写入用户信箱
            inboxService.saveNoticeToInbox(noticeTemplate, users);
            // 3.3.判断是否需要发短信通知
            if(noticeTemplate.getIsSmsTemplate()){
                // 需要发送短信通知
                smsService.sendMessageByTemplate(noticeTemplate, users);
            }
        }
        // 4.到这里说明任务完成，更新任务状态
        boolean shouldRepeat = task.getMaxTimes() > 0;
        lambdaUpdate()
                .set(!shouldRepeat, NoticeTask::getFinished, true)
                .set(shouldRepeat, NoticeTask::getPushTime, task.getPushTime().plusMinutes(task.getInterval()))
                .setSql(shouldRepeat, "max_times = max_times - 1")
                .eq(NoticeTask::getId, task.getId())
                .update();
        task = null;
    }

    @Override
    public void updateNoticeTask(NoticeTaskFormDTO noticeTaskFormDTO) {
        NoticeTask noticeTask = BeanUtils.copyBean(noticeTaskFormDTO, NoticeTask.class);
        updateById(noticeTask);
    }

    @Override
    public PageDTO<NoticeTaskDTO> queryNoticeTasks(NoticeTaskPageQuery query) {
        // 1.分页条件
        Page<NoticeTask> page = query.toMpPage();
        // 2.过滤条件
        page = lambdaQuery()
                .eq(query.getFinished() != null, NoticeTask::getFinished, query.getFinished())
                .like(StringUtils.isNotBlank(query.getKeyword()), NoticeTask::getName, query.getKeyword())
                .ge(query.getMinPushTime() != null, NoticeTask::getPushTime, query.getMinPushTime())
                .le(query.getMaxPushTime() != null, NoticeTask::getPushTime, query.getMaxPushTime())
                .page(page);
        // 3.数据转换
        return PageDTO.of(page, NoticeTaskDTO.class);
    }

    @Override
    public NoticeTaskDTO queryNoticeTask(Long id) {
        return BeanUtils.copyBean(getById(id), NoticeTaskDTO.class);
    }

    @Override
    public PageDTO<NoticeTask> queryTodoNoticeTaskByPage(int pageNo, int size) {
        // 1.分页查询待发布任务：未完成，发布时间早于当前时间
        Page<NoticeTask> page = lambdaQuery()
                .eq(NoticeTask::getFinished, false)
                .le(NoticeTask::getPushTime, LocalDateTime.now())
                .page(new Page<>(pageNo, size));
        return PageDTO.of(page);
    }
}
