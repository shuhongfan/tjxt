package com.tianji.message.service.impl;

import com.tianji.api.dto.sms.SmsInfoDTO;
import com.tianji.api.dto.user.UserDTO;
import com.tianji.common.exceptions.CommonException;
import com.tianji.common.utils.AssertUtils;
import com.tianji.common.utils.MarkedRunnable;
import com.tianji.message.constants.MessageErrorInfo;
import com.tianji.message.domain.po.MessageTemplate;
import com.tianji.message.domain.po.NoticeTemplate;
import com.tianji.message.domain.po.SmsThirdPlatform;
import com.tianji.message.service.IMessageTemplateService;
import com.tianji.message.service.INoticeTemplateService;
import com.tianji.message.service.ISmsService;
import com.tianji.message.service.ISmsThirdPlatformService;
import com.tianji.message.thirdparty.ISmsHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements ISmsService {

    @Resource
    private Map<String, ISmsHandler> smsHandlers;
    private final Executor asyncSmsExecutor;
    private final ISmsThirdPlatformService platformService;
    private final INoticeTemplateService noticeTemplateService;
    private final IMessageTemplateService messageTemplateService;

    @Override
    public void sendMessageByTemplate(NoticeTemplate noticeTemplate, List<UserDTO> users) {
        // 1.获取用户手机号
        Set<String> phones = users.stream().map(UserDTO::getCellPhone).collect(Collectors.toSet());
        // 2.组织数据，公告默认是无参的
        SmsInfoDTO info = new SmsInfoDTO();
        info.setPhones(phones);
        info.setTemplateCode(noticeTemplate.getCode());
        // 3.发送
        sendMessage(info);
    }

    @Override
    public void sendMessage(SmsInfoDTO smsInfoDTO) {
        // 1.获取通知模板信息
        String code = smsInfoDTO.getTemplateCode();
        NoticeTemplate noticeTemplate = noticeTemplateService.queryByCode(code);
        AssertUtils.isNotNull(noticeTemplate, MessageErrorInfo.NOTICE_TEMPLATE_NOT_EXISTS);
        AssertUtils.isTrue(noticeTemplate.getIsSmsTemplate(), MessageErrorInfo.NOTICE_NOT_MESSAGE_TEMPLATE);
        // 2.查询短信模板
        List<MessageTemplate> messageTemplates = messageTemplateService.queryByNoticeTemplateId(noticeTemplate.getId());
        AssertUtils.isNotEmpty(messageTemplates, MessageErrorInfo.NOTICE_NOT_MESSAGE_TEMPLATE);

        // 3.按照平台优先级来排序并筛选模板
        List<MessageTemplate> sortedTemplates =  sortMessageTemplate(messageTemplates);
        if (sortedTemplates.isEmpty()) {
            throw new CommonException(MessageErrorInfo.NO_SUITABLE_TEMPLATE);
        }
        // 4.基于模板发送短信
        for (MessageTemplate template : sortedTemplates) {
            try {
                ISmsHandler smsHandler = smsHandlers.get(template.getPlatformCode());
                smsHandler.send(smsInfoDTO, template);
                return;
            } catch (Exception e) {
                log.error("短信发送异常，平台{}, 原因{}, 稍后重试", template.getPlatformCode(), e.getMessage(), e);
            }
        }
        log.error("短信发送失败，所有平台都已尝试，放弃发送");
    }

    private List<MessageTemplate> sortMessageTemplate(List<MessageTemplate> messageTemplates) {
        // 1.查询平台集合，按照优先级排序
        List<SmsThirdPlatform> platforms = platformService.queryAllPlatform();
        AssertUtils.isNotEmpty(platforms, MessageErrorInfo.PLATFORM_IS_EMPTY);
        // 2.以平台code为key，以template为value，组织map
        Map<String, MessageTemplate> mtMap = messageTemplates.stream()
                .collect(Collectors.toMap(MessageTemplate::getPlatformCode, m -> m));
        // 3.排序数据
        List<MessageTemplate> list = new ArrayList<>(messageTemplates.size());
        for (SmsThirdPlatform platform : platforms) {
            MessageTemplate mt = mtMap.get(platform.getCode());
            if (mt != null) {
                list.add(mt);
            }
        }
        return list;
    }

    @Override
    public void sendMessageAsync(SmsInfoDTO smsInfoDTO) {
        asyncSmsExecutor.execute(
                new MarkedRunnable(() -> this.sendMessage(smsInfoDTO))
        );
    }
}
