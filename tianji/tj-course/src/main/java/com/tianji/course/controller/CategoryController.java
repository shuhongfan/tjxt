package com.tianji.course.controller;

import com.tianji.course.domain.dto.CategoryAddDTO;
import com.tianji.course.domain.dto.CategoryDisableOrEnableDTO;
import com.tianji.course.domain.dto.CategoryListDTO;
import com.tianji.course.domain.dto.CategoryUpdateDTO;
import com.tianji.course.domain.vo.CategoryInfoVO;
import com.tianji.course.domain.vo.CategoryVO;
import com.tianji.course.domain.vo.SimpleCategoryVO;
import com.tianji.course.service.ICategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 课程分类
 *
 * @ClassName CategoryController
 * @Author wusongsong
 * @Date 2022/7/10 11:16
 * @Version
 **/
@RestController
@Api(tags = "课程分类相关接口")
@RequestMapping("categorys")
@Slf4j
@Validated
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @GetMapping("list")
    @ApiOperation("查询课程分类信息")
    public List<CategoryVO> list(CategoryListDTO categoryListDTO) {
        log.info("list categoryListDTO : {}", categoryListDTO);
        return categoryService.list(categoryListDTO);
    }

    @GetMapping("{id}")
    @ApiOperation("获取课程分类信息")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "id", value = "分类id")
    )
    public CategoryInfoVO get(@PathVariable("id") Long id) {
        return categoryService.get(id);
    }

    @PostMapping("add")
    @ApiOperation("新增课程分类")
    public void add(@Valid @RequestBody CategoryAddDTO categoryAddDTO) {

        categoryService.add(categoryAddDTO);
    }

    @DeleteMapping("{id}")
    @ApiOperation("删除分类信息")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "id", value = "分类id")
    )
    public void delete(@PathVariable("id") Long id) {
        categoryService.delete(id);
    }

    @PutMapping("disableOrEnable")
    @ApiOperation("课程分类停用或启用")
    public void disableOrEnable(@Validated @RequestBody CategoryDisableOrEnableDTO categoryDisableOrEnableDTO) {
        categoryService.disableOrEnable(categoryDisableOrEnableDTO);
    }

    @PutMapping("update")
    @ApiOperation("更新课程分类")
    public void updateCategory(@Validated @RequestBody CategoryUpdateDTO categoryUpdateDTO) {
        categoryService.update(categoryUpdateDTO);
    }

    @GetMapping("all")
    @ApiOperation("获取所有的课程分类信息，只包含id,名称，课程分类关系")
    public List<SimpleCategoryVO> all(@RequestParam(value = "admin",required = false, defaultValue = "0") Boolean admin) {
        return categoryService.all(admin);
    }

    @GetMapping("getAllOfOneLevel")
    @ApiOperation("获取所有的课程分类，不分层")
    public List<CategoryVO> allOfOneLevel() {
        return categoryService.allOfOneLevel();
    }
}
