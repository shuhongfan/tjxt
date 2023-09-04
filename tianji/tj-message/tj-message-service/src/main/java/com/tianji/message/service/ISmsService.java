package com.tianji.message.service;

import com.tianji.api.dto.sms.SmsInfoDTO;
import com.tianji.api.dto.user.UserDTO;
import com.tianji.message.domain.po.NoticeTemplate;

import java.util.List;

public interface ISmsService {
    void sendMessageByTemplate(NoticeTemplate noticeTemplate, List<UserDTO> users);

    void sendMessage(SmsInfoDTO smsInfoDTO);

    void sendMessageAsync(SmsInfoDTO smsInfoDTO);
}
