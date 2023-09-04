package com.tianji.search.controller;


import com.tianji.search.domain.vo.CourseVO;
import com.tianji.search.service.IInterestsService;
import com.tianji.search.service.ISearchService;
import com.tianji.api.dto.course.CategoryBasicDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户兴趣表，保存感兴趣的二级分类id 前端控制器
 * </p>
 *
 * @author 虎哥
 * @since 2022-07-21
 */
@RestController
@RequestMapping("/interests")
@Api(tags = "课程推荐相关接口")
@RequiredArgsConstructor
public class InterestsController {

    private final IInterestsService interestsService;
    private final ISearchService searchService;

    @ApiOperation("新增兴趣爱好")
    @PostMapping
    public void saveMyInterests(@RequestParam("interestedIds") List<Long>interestedIds){
        interestsService.saveInterests(interestedIds);
    }

    @ApiOperation("查询我的兴趣爱好")
    @GetMapping
    public List<CategoryBasicDTO> queryMyInterests(){
        return interestsService.queryMyInterests();
    }


    @ApiOperation("根据二级分类id查询课程TOP10")
    @GetMapping("/{id}/courses")
    public List<CourseVO> queryCourseByCateId(@PathVariable("id") Long cateLv2Id){
        return searchService.queryCourseByCateId(cateLv2Id);
    }
}