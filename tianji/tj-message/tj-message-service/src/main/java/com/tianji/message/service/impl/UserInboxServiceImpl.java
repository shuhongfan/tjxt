package com.tianji.message.service.impl;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianji.api.dto.user.UserDTO;
import com.tianji.common.domain.dto.PageDTO;
import com.tianji.common.utils.CollUtils;
import com.tianji.common.utils.UserContext;
import com.tianji.message.config.MessageProperties;
import com.tianji.message.domain.dto.UserInboxDTO;
import com.tianji.message.domain.dto.UserInboxFormDTO;
import com.tianji.message.domain.po.NoticeTemplate;
import com.tianji.message.domain.po.PublicNotice;
import com.tianji.message.domain.po.UserInbox;
import com.tianji.message.domain.query.UserInboxQuery;
import com.tianji.message.enums.NoticeType;
import com.tianji.message.mapper.UserInboxMapper;
import com.tianji.message.service.IPublicNoticeService;
import com.tianji.message.service.IUserInboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户通知记录 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-19
 */
@Service
@RequiredArgsConstructor
public class UserInboxServiceImpl extends ServiceImpl<UserInboxMapper, UserInbox> implements IUserInboxService {

    private final MessageProperties properties;
    private final IPublicNoticeService publicNoticeService;

    @Override
    public void saveNoticeToInbox(NoticeTemplate notice, List<UserDTO> users) {
        LocalDateTime pushTime = LocalDateTime.now();
        LocalDateTime expireTime = pushTime.plusMonths(properties.getMessageTtlMonths());
        // 1.初始化信箱数据
        List<UserInbox> list = new ArrayList<>(users.size());
        // 2.组装
        for (UserDTO user : users) {
            UserInbox box = new UserInbox();
            box.setTitle(notice.getTitle());
            box.setContent(notice.getContent());
            box.setUserId(user.getId());
            box.setType(notice.getType());
            box.setPushTime(pushTime);
            box.setExpireTime(expireTime);
            list.add(box);
        }
        // 3.保存
        saveBatch(list);
    }

    @Override
    @Transactional
    public PageDTO<UserInboxDTO> queryUserInBoxesPage(UserInboxQuery query) {
        // 1.获取用户信息
        Long userId = UserContext.getUser();
        // 2.查询用户信箱中的最后一条公告，确认本次加载公告的最早时间点
        UserInbox latest = getBaseMapper().queryLatestPublicNotice(userId);
        // 2.1.默认时间点是当前时间减去公告的最大有效期时间（未过期的最早公告时间）
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime minTime = now.minusMonths(properties.getNoticeTtlMonths());
        // 2.2.如果有最后一条公告，判断公告时间是不是比最早时间要晚
        if(latest != null && latest.getPushTime().isAfter(minTime)){
            // 用户上次加载时间比最早时间晚，更新一下时间
            minTime = latest.getPushTime();
        }
        // 3.按照发布时间倒序，查看公告箱中的消息，最多加载200条
        Page<PublicNotice> page = new Page<PublicNotice>(1, 200)
                .addOrder(new OrderItem("push_time", false));
        page = publicNoticeService.lambdaQuery()
                .ge(PublicNotice::getPushTime, minTime)
                .page(page);
        // 4.将公告写入用户收件箱
        if (CollUtils.isNotEmpty(page.getRecords())) {
            saveNoticeListToInbox(page.getRecords(), userId);
        }
        // 5.分页查询收件箱信息并返回
        Page<UserInbox> userInboxPage = query.toMpPage("push_time", false);
        userInboxPage = lambdaQuery()
                .eq(UserInbox::getUserId, userId)
                .eq(query.getIsRead() != null, UserInbox::getIsRead, query.getIsRead())
                .eq(query.getType() != null, UserInbox::getType, query.getType())
                .page(userInboxPage);
        return PageDTO.of(userInboxPage, UserInboxDTO.class);
    }

    private void saveNoticeListToInbox(List<PublicNotice> notices, Long userId) {
        List<UserInbox> list = new ArrayList<>(notices.size());
        for (PublicNotice notice : notices) {
            UserInbox box = new UserInbox();
            box.setTitle(notice.getTitle());
            box.setContent(notice.getContent());
            box.setUserId(userId);
            box.setType(notice.getType());
            box.setPushTime(notice.getPushTime());
            box.setExpireTime(notice.getExpireTime());
            list.add(box);
        }
        saveBatch(list);
    }

    @Override
    public Long sentMessageToUser(UserInboxFormDTO userInboxFormDTO) {
        // 1.计算时间
        LocalDateTime pushTime = LocalDateTime.now();
        LocalDateTime expireTime = pushTime.plusMonths(properties.getMessageTtlMonths());
        // 2.获取当前用户
        Long userId = UserContext.getUser();
        // 3.组织数据
        UserInbox inbox = new UserInbox();
        inbox.setUserId(userInboxFormDTO.getUserId());
        inbox.setContent(userInboxFormDTO.getContent());
        inbox.setType(NoticeType.PRIVATE_MESSAGE.getValue());
        inbox.setPushTime(pushTime);
        inbox.setExpireTime(expireTime);
        inbox.setPublisher(userId);
        save(inbox);
        return inbox.getId();
    }
}
