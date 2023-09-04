package com.tianji.trade.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tianji.api.dto.IdAndNumDTO;
import com.tianji.trade.domain.po.OrderDetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 订单明细 Mapper 接口
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-29
 */
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {

    @Select("SELECT course_id FROM order_detail WHERE order_id = #{orderId}")
    List<Long> queryCourseIdsByOrderId(Long orderId);

    List<IdAndNumDTO> countEnrollNumOfCourse(@Param("ew") QueryWrapper<OrderDetail> wrapper);

    List<IdAndNumDTO> countEnrollCourseOfStudent(@Param("ew") QueryWrapper<OrderDetail> wrapper);

    @Select("SELECT SUM(real_pay_amount) FROM order_detail WHERE course_id = #{courseId}")
    int countRealPayAmountByCourseId(Long courseId);
}
