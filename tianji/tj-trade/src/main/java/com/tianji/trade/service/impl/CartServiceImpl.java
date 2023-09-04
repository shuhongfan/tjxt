package com.tianji.trade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianji.api.client.course.CourseClient;
import com.tianji.api.dto.course.CourseFullInfoDTO;
import com.tianji.api.dto.course.CourseSimpleInfoDTO;
import com.tianji.common.exceptions.BadRequestException;
import com.tianji.common.exceptions.BizIllegalException;
import com.tianji.common.utils.BeanUtils;
import com.tianji.common.utils.CollUtils;
import com.tianji.common.utils.StringUtils;
import com.tianji.common.utils.UserContext;
import com.tianji.trade.config.TradeProperties;
import com.tianji.trade.domain.po.Cart;
import com.tianji.trade.domain.vo.CartVO;
import com.tianji.trade.mapper.CartMapper;
import com.tianji.trade.service.ICartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.tianji.trade.constants.TradeErrorInfo.*;

/**
 * <p>
 * 购物车条目信息，也就是购物车中的课程 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2022-08-28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements ICartService {

    private final CourseClient courseClient;
    private final TradeProperties tradeProperties;

    @Override
    public void addCourse2Cart(Long courseId) {
        Long userId = UserContext.getUser();
        log.debug("加入购物车请求：用户：{}，课程：{}", userId, courseId);
        // 1.查询该课程是否已经在购物车
        if (checkCourseExists(courseId, userId)) {
            return;
        }
        // 2.查询购物车中课程是否超出上限
        checkCartsFull(userId);

        // 3.根据id查询课程信息
        CourseFullInfoDTO courseInfo = courseClient.getCourseInfoById(courseId, false, false);

        // 4.判断是否为空
        if (courseInfo == null) {
            throw new BadRequestException(COURSE_NOT_EXISTS);
        }

        // 5.判断是否过期
        if (courseInfo.getPurchaseEndTime().isBefore(LocalDateTime.now())) {
            // 已经过期，无法购买
            throw new BadRequestException(COURSE_EXPIRED);
        }
        // 5.写入购物车
        Cart cart = new Cart();
        cart.setId(IdWorker.getId()); //购物车中的id
        cart.setCourseId(courseId); //课程id
        cart.setCourseName(courseInfo.getName());
        cart.setUserId(UserContext.getUser());
        cart.setCoverUrl(courseInfo.getCoverUrl());
        cart.setPrice(courseInfo.getPrice());
        save(cart);
        log.debug("加入购物车成功！用户：{}，课程：{}", userId, courseId);
    }

    private void checkCartsFull(Long userId) {
        int count = lambdaQuery().eq(Cart::getUserId, userId).count();
        if (count >= tradeProperties.getMaxCourseAmount()) {
            throw new BizIllegalException(
                    StringUtils.format(CARTS_FULL, tradeProperties.getMaxCourseAmount()));
        }
    }

    private boolean checkCourseExists(Long courseId, Long userId) {
        int count = lambdaQuery()
                .eq(Cart::getUserId, userId)
                .eq(Cart::getCourseId, courseId)
                .count();
        return count > 0;
    }

    @Override
    public List<CartVO> getMyCarts() {
        // 1.获取用户
        Long userId = UserContext.getUser();
        // 2.查询我的购物车
        List<Cart> carts = lambdaQuery().eq(Cart::getUserId, userId).list();
        if (CollUtils.isEmpty(carts)) {
            return CollUtils.emptyList();
        }
        // 3.查询购物车中的课程
        List<Long> courseIds = carts.stream().map(Cart::getCourseId).collect(Collectors.toList());
        List<CourseSimpleInfoDTO> courseSimpleInfos = courseClient.getSimpleInfoList(courseIds);
        Map<Long, CourseSimpleInfoDTO> map = courseSimpleInfos.stream()
                .collect(Collectors.toMap(CourseSimpleInfoDTO::getId, c -> c));
        // 4.组织 vo
        List<CartVO> list = new ArrayList<>(carts.size());
        for (Cart cart : carts) {
            // 4.1.转换VO
            CartVO vo = BeanUtils.toBean(cart, CartVO.class);
            list.add(vo);
            // 4.2.获取新的课程信息
            CourseSimpleInfoDTO info = map.get(cart.getCourseId());
            vo.setNowPrice(info.getPrice());
            vo.setExpired(info.getPurchaseEndTime().isBefore(LocalDateTime.now()));
            vo.setCourseValidDate(info.getPurchaseEndTime());
        }
        // 5.排序
        return list.stream().sorted(
                Comparator.comparing(CartVO::getExpired).reversed() // 先看是否过期，未过期的在前，已过期的在后
                        .thenComparingLong(CartVO::getId).reversed() // 再看id，id大的是新加入的
        ).collect(Collectors.toList());
    }

    @Override
    public void deleteCartById(Long id) {
        // 1.获取用户
        Long userId = UserContext.getUser();
        // 2.删除
        remove(new LambdaQueryWrapper<Cart>()
                .eq(Cart::getId, id)
                .eq(Cart::getUserId, userId)
        );
    }

    @Override
    public void deleteCartByIds(List<Long> ids) {
        Long userId = UserContext.getUser();
        remove(new LambdaQueryWrapper<Cart>()
                .eq(Cart::getUserId, userId)
                .in(Cart::getId, ids)
        );
    }

    @Override
    public void deleteCartByUserAndCourseIds(Long userId, List<Long> courseIds) {
        log.debug("尝试从购物车删除用户已购买的课程，用户id：{}，课程id：{}", userId, courseIds);
        try {
            if(CollUtils.isEmpty(courseIds) || userId == null){
                return;
            }
            remove(new LambdaQueryWrapper<Cart>()
                    .eq(Cart::getUserId, userId)
                    .in(Cart::getCourseId, courseIds)
            );
        } catch (Exception e) {
            log.error("从购物车删除用户已购买的课程发生异常，用户id：{}，课程id：{}", userId, courseIds, e);
        }
    }
}
