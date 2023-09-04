package com.tianji.course.controller;

import com.tianji.course.domain.vo.CataSimpleInfoVO;
import com.tianji.course.service.ICourseCatalogueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 目录课程相关接口
 *
 * @ClassName CatalogueController
 * @Author wusongsong
 * @Date 2022/7/27 13:59
 * @Version
 **/
@Api(tags = "章节目录相关接口")
@RestController
@RequestMapping("catalogues")
public class CatalogueController {

    @Autowired
    private ICourseCatalogueService courseCatalogueService;

    @GetMapping("batchQuery")
    @ApiOperation("根据章节目录批量查询基础信息")
    public List<CataSimpleInfoVO> batchQuery(@RequestParam("ids") List<Long> ids) {
        return courseCatalogueService.getManyCataSimpleInfo(ids);
    }

    @GetMapping("querySectionInfoById/{id}")
    @ApiOperation("获取小节信息")
    public CataSimpleInfoVO querySectionInfoById(@PathVariable("id") Long id) {
        return courseCatalogueService.querySectionInfoById(id);
    }
}
