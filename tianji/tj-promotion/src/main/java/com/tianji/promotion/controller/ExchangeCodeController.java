package com.tianji.promotion.controller;

import com.tianji.common.domain.dto.PageDTO;
import com.tianji.promotion.domain.query.CodeQuery;
import com.tianji.promotion.domain.vo.ExchangeCodeVO;
import com.tianji.promotion.service.IExchangeCodeService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 * 兑换码 控制器
 * </p>
 *
 * @author 虎哥
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/codes")
public class ExchangeCodeController {

    private final IExchangeCodeService codeService;

    @ApiOperation("分页查询兑换码")
    @GetMapping("page")
    public PageDTO<ExchangeCodeVO> queryCodePage(@Valid CodeQuery query){
        return codeService.queryCodePage(query);
    }
}
