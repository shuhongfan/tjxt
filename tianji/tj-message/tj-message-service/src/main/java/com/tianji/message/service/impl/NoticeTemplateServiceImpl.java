package com.tianji.message.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianji.common.domain.dto.PageDTO;
import com.tianji.common.exceptions.BadRequestException;
import com.tianji.common.utils.BeanUtils;
import com.tianji.common.utils.CollUtils;
import com.tianji.common.utils.StringUtils;
import com.tianji.message.constants.MessageErrorInfo;
import com.tianji.message.domain.dto.MessageTemplateFormDTO;
import com.tianji.message.domain.dto.NoticeTemplateDTO;
import com.tianji.message.domain.dto.NoticeTemplateFormDTO;
import com.tianji.message.domain.po.MessageTemplate;
import com.tianji.message.domain.po.NoticeTemplate;
import com.tianji.message.domain.query.NoticeTemplatePageQuery;
import com.tianji.message.enums.TemplateStatus;
import com.tianji.message.mapper.NoticeTemplateMapper;
import com.tianji.message.service.IMessageTemplateService;
import com.tianji.message.service.INoticeTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 通知模板 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-19
 */
@Service
@RequiredArgsConstructor
public class NoticeTemplateServiceImpl extends ServiceImpl<NoticeTemplateMapper, NoticeTemplate> implements INoticeTemplateService {

    private final IMessageTemplateService messageTemplateService;

    @Override
    @Transactional
    public Long saveNoticeTemplate(NoticeTemplateFormDTO noticeTemplateFormDTO) {
        // 1.保存通知模板
        NoticeTemplate noticeTemplate = BeanUtils.copyBean(noticeTemplateFormDTO, NoticeTemplate.class);
        save(noticeTemplate);
        Long id = noticeTemplate.getId();
        // 2.判断是否有短信模板
        if(!noticeTemplate.getIsSmsTemplate()){
            return id;
        }
        // 3.保存短信模板
        List<MessageTemplateFormDTO> messageTemplateDTOs = noticeTemplateFormDTO.getMessageTemplates();
        List<MessageTemplate> messageTemplates = new ArrayList<>();
        for (MessageTemplateFormDTO dto : messageTemplateDTOs) {
            MessageTemplate template = BeanUtils.copyBean(dto, MessageTemplate.class);
            template.setTemplateId(id);
            template.setName(noticeTemplate.getName());
            template.setContent(noticeTemplate.getContent());
            messageTemplates.add(template);
        }
        messageTemplateService.saveBatch(messageTemplates);
        return id;
    }

    @Override
    @Transactional
    public void updateNoticeTemplate(NoticeTemplateFormDTO noticeTemplateDTO) {
        // 1.查询旧数据
        Long id = noticeTemplateDTO.getId();
        NoticeTemplate oldNT = getById(id);
        if (oldNT == null) {
            throw new BadRequestException(MessageErrorInfo.NOTICE_TEMPLATE_NOT_EXISTS);
        }
        // 2.首先更新通知模板数据
        NoticeTemplate noticeTemplate = BeanUtils.copyBean(noticeTemplateDTO, NoticeTemplate.class);
        updateById(noticeTemplate);
        // 3.是否需要删除短信模板
        List<Long> deleteMessageTemplates = noticeTemplateDTO.getDeleteMessageTemplates();
        if(CollUtils.isNotEmpty(deleteMessageTemplates)){
            messageTemplateService.removeByIds(deleteMessageTemplates);
        }
        // 4.是否有 新增或修改的短信模板
        List<MessageTemplateFormDTO> templateDTOS = noticeTemplateDTO.getMessageTemplates();
        if(CollUtils.isEmpty(templateDTOS)){
            return;
        }
        // 5.数据处理
        List<MessageTemplate> saveList = new ArrayList<>();
        List<MessageTemplate> updateList = new ArrayList<>();
        // 5.1.先查询当前通知模板有哪些平台的短信模板
        List<MessageTemplate> list = messageTemplateService.queryByNoticeTemplateId(id);
        Map<String, Long> mtMap = null;
        if(CollUtils.isNotEmpty(list)){
            mtMap = list.stream().collect(Collectors.toMap(MessageTemplate::getPlatformCode, MessageTemplate::getId));
        }
        // 5.2.判断是新增还是修改短信模板
        noticeTemplate = getById(id);
        for (MessageTemplateFormDTO dto : templateDTOS) {
            MessageTemplate template = BeanUtils.copyBean(dto, MessageTemplate.class);
            template.setTemplateId(id);
            template.setName(noticeTemplate.getName());
            template.setContent(noticeTemplate.getContent());
            if(template.getId() == null){
                // 判断当前短信模板是否已经存在
                if(mtMap != null && mtMap.containsKey(template.getPlatformCode())){
                    // 若已经存在，则改为更新
                    template.setId(mtMap.get(template.getPlatformCode()));
                    updateList.add(template);
                }else {
                    // 不存在则新增
                    saveList.add(template);
                }
            }else{
                updateList.add(template);
            }
        }
        // 5.3.新增短信模板
        if(CollUtils.isNotEmpty(saveList)) {
            messageTemplateService.saveBatch(saveList);
        }
        // 5.4.更新短信模板
        if(CollUtils.isNotEmpty(updateList)) {
            messageTemplateService.updateBatchById(updateList);
        }
    }

    @Override
    public PageDTO<NoticeTemplateDTO> queryNoticeTemplates(NoticeTemplatePageQuery query) {
        // 1.分页条件
        Page<NoticeTemplate> page = query.toMpPage();
        // 2.过滤条件
        page = lambdaQuery()
                .eq(query.getStatus() != null, NoticeTemplate::getStatus, query.getStatus())
                .like(StringUtils.isNotBlank(query.getKeyword()), NoticeTemplate::getName, query.getKeyword())
                .page(page);
        // 3.数据转换
        return PageDTO.of(page, NoticeTemplateDTO.class);
    }

    @Override
    public NoticeTemplateDTO queryNoticeTemplate(Long id) {
        return BeanUtils.copyBean( getById(id), NoticeTemplateDTO.class);
    }

    @Override
    public NoticeTemplate queryByCode(String code) {
        return lambdaQuery()
                .eq(NoticeTemplate::getCode, code)
                .eq(NoticeTemplate::getStatus, TemplateStatus.IN_SERVICE.getValue())
                .one();
    }
}
