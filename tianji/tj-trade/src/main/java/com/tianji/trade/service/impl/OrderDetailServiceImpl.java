package com.tianji.trade.service.impl;

import cn.hutool.db.DbRuntimeException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianji.api.cache.RoleCache;
import com.tianji.api.client.user.UserClient;
import com.tianji.api.dto.IdAndNumDTO;
import com.tianji.api.dto.course.CoursePurchaseInfoDTO;
import com.tianji.api.dto.user.UserDTO;
import com.tianji.common.constants.ErrorInfo;
import com.tianji.common.domain.dto.PageDTO;
import com.tianji.common.exceptions.BadRequestException;
import com.tianji.common.utils.*;
import com.tianji.pay.sdk.client.PayClient;
import com.tianji.pay.sdk.constants.PayChannel;
import com.tianji.pay.sdk.constants.RefundChannelEnum;
import com.tianji.trade.constants.OrderStatus;
import com.tianji.trade.constants.RefundStatus;
import com.tianji.trade.domain.po.Order;
import com.tianji.trade.domain.po.OrderDetail;
import com.tianji.trade.domain.po.RefundApply;
import com.tianji.trade.domain.query.OrderDetailPageQuery;
import com.tianji.trade.domain.vo.OrderDetailAdminVO;
import com.tianji.trade.domain.vo.OrderDetailPageVO;
import com.tianji.trade.domain.vo.OrderProgressNodeVO;
import com.tianji.trade.mapper.OrderDetailMapper;
import com.tianji.trade.mapper.OrderMapper;
import com.tianji.trade.mapper.RefundApplyMapper;
import com.tianji.trade.service.IOrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.tianji.trade.constants.OrderStatus.*;
import static com.tianji.trade.constants.TradeErrorInfo.ORDER_NOT_EXISTS;

/**
 * <p>
 * 订单明细 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-29
 */
@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements IOrderDetailService {

    private final UserClient userClient;

    private final OrderMapper orderMapper;

    private final PayClient payClient;

    private final RefundApplyMapper applyMapper;

    private final RoleCache roleCache;

    @Override
    @Transactional
    public void updateStatusByOrderId(Long orderId, Integer status) {
        boolean success = lambdaUpdate()
                .set(OrderDetail::getStatus, status)
                .eq(OrderDetail::getOrderId, orderId)
                .update();
        if (!success) {
            throw new DbRuntimeException(ErrorInfo.Msg.DB_UPDATE_EXCEPTION);
        }
    }

    @Override
    public List<OrderDetail> queryByOrderIds(List<Long> orderIds) {
        return lambdaQuery().in(OrderDetail::getOrderId, orderIds).list();
    }

    @Override
    public List<OrderDetail> queryByOrderId(Long orderId) {
        return lambdaQuery().eq(OrderDetail::getOrderId, orderId).list();
    }

    @Override
    public PageDTO<OrderDetailPageVO> queryDetailForPage(OrderDetailPageQuery query) {
        // 1.分页和排序条件
        Page<OrderDetail> p = query.toMpPageDefaultSortByCreateTimeDesc();
        // 2.可能有用户条件
        Long userId = null;
        if (StringUtils.isNotBlank(query.getMobile())) {
            userId = userClient.exchangeUserIdWithPhone(query.getMobile());
            if (userId == null) {
                // 学生不存在，则返回空数据
                return PageDTO.empty(0L, 0L);
            }
        }
        // 3.搜索
        Page<OrderDetail> page = lambdaQuery()
                .eq(query.getId() != null, OrderDetail::getId, query.getId())
                .eq(query.getStatus() != null, OrderDetail::getStatus, query.getStatus())
                .eq(query.getRefundStatus() != null, OrderDetail::getRefundStatus, query.getRefundStatus())
                .eq(StringUtils.isNotBlank(query.getPayChannel()), OrderDetail::getPayChannel, query.getPayChannel())
                .ge(query.getOrderStartTime() != null, OrderDetail::getCreateTime, query.getOrderStartTime())
                .le(query.getOrderEndTime() != null, OrderDetail::getCreateTime, query.getOrderEndTime())
                .eq(userId != null, OrderDetail::getUserId, userId)
                .page(p);
        // 4.判断是否为空
        List<OrderDetail> records = page.getRecords();
        if (CollUtils.isEmpty(records)) {
            return PageDTO.empty(page);
        }
        // 5.查询订单中的用户信息
        Set<Long> uIds = records.stream().map(OrderDetail::getUserId).collect(Collectors.toSet());
        List<UserDTO> users = userClient.queryUserByIds(uIds);
        AssertUtils.isNotEmpty(users, ErrorInfo.Msg.USER_NOT_EXISTS);
        Map<Long, UserDTO> userMap = users.stream().collect(Collectors.toMap(UserDTO::getId, u -> u));

        // 6.数据转换
        List<OrderDetailPageVO> list = new ArrayList<>(records.size());
        for (OrderDetail record : records) {
            // 6.1.转换vo
            OrderDetailPageVO v = BeanUtils.toBean(record, OrderDetailPageVO.class);
            list.add(v);
            // 6.2.用户信息
            UserDTO u = userMap.get(record.getUserId());
            v.setName(roleCache.exchangeRoleName(u));
            v.setMobile(u == null ? null : u.getCellPhone());
            // 6.3.其它
            v.setPayChannel(PayChannel.desc(record.getPayChannel()));
            v.setStatusDesc(OrderStatus.desc(record.getStatus()));
            v.setRefundStatusDesc(RefundStatus.desc(record.getRefundStatus()));
        }

        return PageDTO.of(page, list);
    }

    @Override
    public OrderDetailAdminVO queryOrdersDetailProgress(Long id) {
        // 1.查询订单明细
        OrderDetail detail = getById(id);
        if (detail == null) {
            throw new BadRequestException(ORDER_NOT_EXISTS);
        }
        // 2.查询对应订单
        Order order = orderMapper.getById(detail.getOrderId());
        if (order == null) {
            throw new BadRequestException(ORDER_NOT_EXISTS);
        }
        // 3.查询退款申请单
        List<RefundApply> refundApplyList = null;
        RefundApply refundApply = null;
        if (detail.getRefundStatus() != null && detail.getRefundStatus() != 0) {
            refundApplyList = applyMapper.queryByDetailId(detail.getId());
            refundApply = refundApplyList.get(0);
        }

        // 4.查询学生和申请人信息
        Set<Long> uIds = new HashSet<>(2);
        uIds.add(detail.getUserId());
        if (refundApply != null) {
            uIds.add(refundApply.getCreater());
        }
        List<UserDTO> userDTOS = userClient.queryUserByIds(uIds);
        AssertUtils.isNotEmpty(userDTOS, ErrorInfo.Msg.USER_NOT_EXISTS);
        Map<Long, UserDTO> userMap = userDTOS.stream().collect(Collectors.toMap(UserDTO::getId, u -> u));

        // 5.数据处理
        OrderDetailAdminVO vo = BeanUtils.toBean(detail, OrderDetailAdminVO.class);
        vo.setMessage(order.getMessage());
        vo.setPayChannel(PayChannel.desc(order.getPayChannel()));
        // 5.1.订单流水信息
        vo.setPayOrderNo(order.getPayOrderNo());
        if (refundApply != null) {
            vo.setRefundOrderNo(refundApply.getRefundOrderNo());
            vo.setRefundChannel(RefundChannelEnum.desc(refundApply.getRefundChannel()));
            vo.setFailedReason(refundApply.getFailedReason());
        }
        // 5.2.用户信息
        UserDTO student = userMap.get(detail.getUserId());
        vo.setStudentName(student.getName());
        vo.setMobile(student.getCellPhone());

        // 5.3.退款单信息
        if (refundApply != null) {
            vo.setRefundApplyId(refundApply.getId());
            vo.setRefundProposerName(userMap.get(refundApply.getCreater()).getName());
            vo.setRefundReason(refundApply.getRefundReason());
            vo.setRemark(refundApply.getRemark());
            vo.setRefundMessage(refundApply.getMessage());
        }

        // 5.4.课程有效期
        Integer validDuration = detail.getValidDuration();
        if (order.getPayTime() != null && validDuration != null && validDuration > 0) {
            // 已经支付成功，设置课程有效期
            vo.setStudyValidTime(order.getPayTime().plusMonths(validDuration));
        }

        // 5.5.设置订单进度
        List<OrderProgressNodeVO> progressNodes = packageProgressNodes(order, refundApply);
        vo.setNodes(progressNodes);

        // 5.6.是否允许退款:
        // - 已经支付并且当前没有退款
        // - 当前只且只有1次退款并且退款进程已完结
        vo.setCanRefund(
                (OrderStatus.canRefund(detail.getStatus()) && refundApply == null) ||
                        (refundApply != null && refundApplyList.size() == 1 &&
                                !RefundStatus.inProgress(refundApply.getStatus()))
        );
        return vo;
    }

    @Override
    public List<OrderProgressNodeVO> packageProgressNodes(Order order, RefundApply refundApply) {
        // 1.先填充订单交易的每个节点的时间值
        List<OrderProgressNodeVO> list = new ArrayList<>();
        // 1.1.下单时间
        list.add(new OrderProgressNodeVO(OrderStatus.NO_PAY.getProgressName(), order.getCreateTime()));
        // 1.2.支付成功时间
        list.add(new OrderProgressNodeVO(PAYED.getProgressName(), order.getPayTime()));
        // 1.3.交易关闭时间
        list.add(new OrderProgressNodeVO(OrderStatus.CLOSED.getProgressName(), order.getCloseTime()));
        // 1.4.交易完成时间
        list.add(new OrderProgressNodeVO(FINISHED.getProgressName(), order.getFinishTime()));
        if (refundApply == null) {
            // 1.5.没有退款参数情况下，默认是用户端查询，添加退款成功时间字段
            list.add(new OrderProgressNodeVO(OrderStatus.REFUNDED.getProgressName(), order.getRefundTime()));
        } else {
            // 2.再填充订单退款的每个节点的时间值
            // 2.1.订单申请退款
            list.add(new OrderProgressNodeVO(RefundStatus.UN_APPROVE.getProgressName(), refundApply.getCreateTime()));
            // 2.2.订单审批时间（审批成功或失败）
            list.add(new OrderProgressNodeVO(RefundStatus.AGREE.getProgressName(), refundApply.getApproveTime()));
            // 2.3.退款结束时间（学员取消、退款成功、退款关闭）
            RefundStatus status = RefundStatus.of(refundApply.getStatus());
            list.add(new OrderProgressNodeVO(status.getProgressName(), refundApply.getFinishTime()));
        }

        // 3.然后过滤掉没有时间的节点，再按照时间进行排序，升序
        return list.stream()
                .filter(nodeVO -> nodeVO.getTime() != null)
                .sorted(Comparator.comparing(OrderProgressNodeVO::getTime))
                .collect(Collectors.toList());
    }

    @Override
    public void markDetailSuccessByOrderId(Long id, String payChannel, LocalDateTime successTime) {
        List<OrderDetail> details = queryByOrderId(id);
        for (OrderDetail detail : details) {
            detail.setStatus(PAYED.getValue());
            detail.setPayChannel(payChannel);
            detail.setCourseExpireTime(successTime.plusMinutes(detail.getValidDuration()));
        }
        updateBatchById(details);
    }

    @Override
    public void updateRefundStatusById(Long orderDetailId, int status) {
        lambdaUpdate()
                .set(OrderDetail::getRefundStatus, status)
                .eq(OrderDetail::getId, orderDetailId)
                .update();
    }

    @Override
    public List<Long> queryCourseIdsByOrderId(Long orderId) {
        return baseMapper.queryCourseIdsByOrderId(orderId);
    }

    @Override
    public Boolean checkCourseOrderInfo(Long courseId) {
        // 1.获取用户
        Long userId = UserContext.getUser();

        // 2.查询订单
        List<OrderDetail> orders = lambdaQuery()
                .eq(OrderDetail::getUserId, userId)
                .eq(OrderDetail::getCourseId, courseId)
                .in(OrderDetail::getStatus, PAYED.getValue(), FINISHED.getValue(), ENROLLED.getValue())
                .list();

        // 3.判断是否存在订单
        if (CollUtils.isEmpty(orders)) {
            return false;
        }

        // 4.找到未过期的
        LocalDateTime now = LocalDateTime.now();
        return orders.stream().anyMatch(o -> o.getCourseExpireTime().isAfter(now));
    }

    @Override
    public Map<Long, Integer> countEnrollNumOfCourse(List<Long> courseIdList) {
        // 1.条件构造
        QueryWrapper<OrderDetail> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .in(OrderDetail::getCourseId, courseIdList)
                .in(OrderDetail::getStatus, PAYED.getValue(), FINISHED.getValue(), ENROLLED.getValue());

        // 2.统计
        List<IdAndNumDTO> list = baseMapper.countEnrollNumOfCourse(wrapper);

        // 3.转换返回
        return IdAndNumDTO.toMap(list);
    }

    @Override
    public Map<Long, Integer> countEnrollCourseOfStudent(List<Long> studentIds) {
        // 1.条件构造
        QueryWrapper<OrderDetail> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .in(OrderDetail::getUserId, studentIds)
                .in(OrderDetail::getStatus, PAYED.getValue(), FINISHED.getValue(), ENROLLED.getValue());
        // 2.统计
        List<IdAndNumDTO> list = baseMapper.countEnrollCourseOfStudent(wrapper);

        // 3.转换返回
        return IdAndNumDTO.toMap(list);
    }

    @Override
    public CoursePurchaseInfoDTO getPurchaseInfoOfCourse(Long courseId) {
        // 1.统计报名人数
        Integer enrollNum = lambdaQuery()
                .eq(OrderDetail::getCourseId, courseId)
                .in(OrderDetail::getStatus, PAYED.getValue(), FINISHED.getValue(), ENROLLED.getValue())
                .count();
        // 2.统计退款人数
        Integer refundNum = lambdaQuery()
                .eq(OrderDetail::getCourseId, courseId)
                .eq(OrderDetail::getStatus, REFUNDED.getValue())
                .count();
        // 3.统计销售额
        int realPayAmount = baseMapper.countRealPayAmountByCourseId(courseId);

        return new CoursePurchaseInfoDTO(enrollNum, refundNum, realPayAmount);
    }
}
