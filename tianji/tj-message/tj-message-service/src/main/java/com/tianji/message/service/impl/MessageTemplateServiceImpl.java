package com.tianji.message.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianji.message.domain.dto.MessageTemplateDTO;
import com.tianji.message.domain.dto.MessageTemplateFormDTO;
import com.tianji.message.domain.query.MessageTemplatePageQuery;
import com.tianji.common.domain.dto.PageDTO;
import com.tianji.common.utils.BeanUtils;
import com.tianji.common.utils.StringUtils;
import com.tianji.message.domain.po.MessageTemplate;
import com.tianji.message.mapper.MessageTemplateMapper;
import com.tianji.message.service.IMessageTemplateService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 第三方短信平台签名和模板信息 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-19
 */
@Service
public class MessageTemplateServiceImpl extends ServiceImpl<MessageTemplateMapper, MessageTemplate> implements IMessageTemplateService {

    @Override
    public List<MessageTemplate> queryByNoticeTemplateId(Long templateId) {
        return lambdaQuery()
                .eq(MessageTemplate::getTemplateId, templateId)
                .list();
    }

    @Override
    public Long saveMessageTemplate(MessageTemplateFormDTO messageTemplateDTO) {
        // 1.数据转换
        MessageTemplate messageTemplate = BeanUtils.copyBean(messageTemplateDTO, MessageTemplate.class);
        // 2.新增
        save(messageTemplate);
        return messageTemplate.getId();
    }

    @Override
    public void updateMessageTemplate(MessageTemplateFormDTO messageTemplateDTO) {
        // 1.数据转换
        MessageTemplate messageTemplate = BeanUtils.copyBean(messageTemplateDTO, MessageTemplate.class);
        // 2.新增
        updateById(messageTemplate);
    }

    @Override
    public PageDTO<MessageTemplateDTO> queryMessageTemplates(MessageTemplatePageQuery query) {
        // 1.分页条件
        Page<MessageTemplate> page = query.toMpPage();
        // 2.过滤条件
        page = lambdaQuery()
                .eq(query.getStatus() != null, MessageTemplate::getStatus, query.getStatus())
                .eq(query.getThirdPlatformId() != null, MessageTemplate::getPlatformCode, query.getThirdPlatformId())
                .like(StringUtils.isNotBlank(query.getKeyword()), MessageTemplate::getName, query.getKeyword())
                .page(page);
        // 3.数据转换
        return PageDTO.of(page, MessageTemplateDTO.class);
    }

    @Override
    public MessageTemplateDTO queryMessageTemplate(Long id) {
        return BeanUtils.copyBean( getById(id), MessageTemplateDTO.class);
    }
}
