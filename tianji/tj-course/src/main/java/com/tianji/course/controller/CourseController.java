package com.tianji.course.controller;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.tianji.api.dto.course.CourseSimpleInfoDTO;
import com.tianji.common.domain.dto.PageDTO;
import com.tianji.common.validate.annotations.ParamChecker;
import com.tianji.course.constants.CourseStatus;
import com.tianji.course.domain.dto.*;
import com.tianji.course.domain.vo.*;
import com.tianji.course.service.*;
import com.tianji.course.utils.CourseSaveBaseGroup;
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
 * 课程controller
 *
 * @ClassName CourseController
 * @Author wusongsong
 * @Date 2022/7/10 15:34
 * @Version
 **/
@Api(tags = "课程相关接口")
@RestController
@RequestMapping("courses")
@Slf4j
@Validated
public class CourseController {

    @Autowired
    private ICourseDraftService courseDraftService;

    @Autowired
    private ICourseCatalogueDraftService courseCatalogueDraftService;

    @Autowired
    private ICourseTeacherDraftService courseTeacherDraftService;

    @Autowired
    private ICourseService courseService;

    @Autowired
    private ICourseCatalogueService courseCatalogueService;

    //todo 二期做
//    @GetMapping("statistics")
//    @ApiOperation("课程数据统计")
    public CourseStatisticsVO statistics() {
        return null;
    }

    @GetMapping("baseInfo/{id}")
    @ApiOperation("获取课程基础信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "课程id"),
            @ApiImplicitParam(name = "see", value = "是否是用于查看页面查看数据，默认是查看,如果不是界面查看数据就是编辑页面使用")})
    public CourseBaseInfoVO baseInfo(@PathVariable("id") Long id,
                                     @RequestParam(value = "see", required = false, defaultValue = "1") Boolean see) {
        return courseDraftService.getCourseBaseInfo(id, see);
    }

    @PostMapping("baseInfo/save")
    @ApiOperation("保存课程基本信息")
    @ParamChecker
    //校验非业务限制的字段
    public CourseSaveVO save(@RequestBody @Validated(CourseSaveBaseGroup.class) CourseBaseInfoSaveDTO courseBaseInfoSaveDTO) {
        return courseDraftService.save(courseBaseInfoSaveDTO);
    }

    @GetMapping("catas/{id}")
    @ApiOperation("获取课程的章节")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "课程id"),
            @ApiImplicitParam(name = "see", value = "是否是用于查看页面查看数据，默认是查看,如果不是界面查看数据就是编辑页面使用")
    })
    public List<CataVO> catas(@PathVariable(value = "id", required = false) Long id,
                              @RequestParam(value = "see", required = false, defaultValue = "1") Boolean see,
                              @RequestParam(value = "withPractice", required = false, defaultValue = "1") Boolean withPractice) {
        return courseCatalogueDraftService.queryCourseCatalogues(id, see, withPractice);
    }

    @PostMapping("catas/save/{id}/{step}")
    @ApiOperation("保存章节")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "课程id"),
            @ApiImplicitParam(name = "step", value = "步骤")
    })
    @ParamChecker
    public void catasSave(@RequestBody @Validated List<CataSaveDTO> cataSaveDTOS,
                          @PathVariable("id") Long id, @PathVariable(value = "step",required = false) Integer step) {
        courseCatalogueDraftService.save(id, cataSaveDTOS, step);
    }

    @PostMapping("media/save/{id}")
    @ApiOperation("课程视频")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "课程id")
    })
    public void mediaSave(@PathVariable("id") Long id, @RequestBody @Valid List<CourseMediaDTO> courseMediaDTOS) {
        courseCatalogueDraftService.saveMediaInfo(id, courseMediaDTOS);
    }

    @PostMapping("subjects/save/{id}")
    @ApiOperation("保存小节或练习中的题目")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "课程id")
    })
    public void saveSuject(@PathVariable("id") Long id, @RequestBody @Validated List<CataSubjectDTO> cataSubjectDTO) {
        courseCatalogueDraftService.saveSuject(id, cataSubjectDTO);
    }

    @GetMapping("subjects/get/{id}")
    @ApiOperation("获取小节或练习中的题目（用于编辑）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "课程id")
    })
    public List<CataSimpleSubjectVO> getSuject(@PathVariable("id") Long id) {
        return courseCatalogueDraftService.getSuject(id);
    }

    @GetMapping("teachers/{id}")
    @ApiOperation("查询课程相关的老师信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "课程id"),
            @ApiImplicitParam(name = "see", value = "是否是用于查看页面查看数据，默认是查看,如果不是界面查看数据就是编辑页面使用")
    })
    public List<CourseTeacherVO> teacher(@PathVariable("id") Long id,
                                         @RequestParam(value = "see", required = false, defaultValue = "1") Boolean see) {
        return courseTeacherDraftService.queryTeacherOfCourse(id, see);
    }

    @PostMapping("teachers/save")
    @ApiOperation("保存老师信息")
    public void teachersSave(@RequestBody @Validated CourseTeacherSaveDTO courseTeacherSaveDTO) {
        courseTeacherDraftService.save(courseTeacherSaveDTO);
    }


    @PostMapping("upShelf")
    @ApiOperation("课程上架")
    public void upShelf(@RequestBody @Validated CourseIdDTO courseIdDTO) {
        courseDraftService.upShelf(courseIdDTO.getId());
    }

    @GetMapping("checkBeforeUpShelf/{id}")
    @ApiOperation("课程上架前校验")
    public void checkBeforeUpShelf(@PathVariable("id") Long id){
        courseDraftService.checkBeforeUpShelf(id);
    }

    @PostMapping("downShelf")
    @ApiOperation("课程下架")
    public void downShelf(@RequestBody @Validated CourseIdDTO courseIdDTO) {
        courseDraftService.downShelf(courseIdDTO.getId());
    }

    /**
     * 先去删除加上数据删除后，再去删除草稿
     *
     * @param id
     */
    @DeleteMapping("delete/{id}")
    @ApiOperation("课程删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id")
    })
    public void deleteById(@PathVariable("id") Long id) {
        courseService.delete(id);
    }

    @ApiOperation("根根条件列表获取课程信息")
    @GetMapping("/simpleInfo/list")
    public List<CourseSimpleInfoDTO> getSimpleInfoList(CourseSimpleInfoListDTO courseSimpleInfoListDTO) {
        return courseService.getSimpleInfoList(courseSimpleInfoListDTO);
    }

    @ApiOperation("根据课程id，查询所有章节的序号")
    @GetMapping("/catas/index/list/{id}")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "id", value = "课程id")
    )
    public List<CataSimpleInfoVO> catasIndexList(@PathVariable("id") Long id) {
        return courseCatalogueService.getCatasIndexList(id);
    }

    @ApiOperation("生成练习id")
    @GetMapping("generator")
    public CourseCataIdVO generator() {
        return new CourseCataIdVO(IdWorker.getId());
    }

    @ApiOperation("管理端课程搜索接口")
    @GetMapping("/page")
    public PageDTO<CoursePageVO> queryForPage(CoursePageQuery coursePageQuery) {
        if(CourseStatus.NO_UP_SHELF.equals(coursePageQuery.getStatus()) ||
        CourseStatus.DOWN_SHELF.equals(coursePageQuery.getStatus())){
            //待上架已下架查询草稿
            return courseDraftService.queryForPage(coursePageQuery);
        }else {
            //已上架已完结查询正式数据
            return courseService.queryForPage(coursePageQuery);
        }
    }

    @ApiOperation("校验课程名称是否已经存在")
    @GetMapping("/checkName")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id"),
            @ApiImplicitParam(name = "name", value = "课程名称")
    })
    public NameExistVO checkNameExist(@RequestParam(value = "id",required = false) Long id,
                                      @RequestParam(value = "name") String name){
        return courseService.checkName(name, id);
    }

    @ApiOperation("查询课程基本信息、目录、学习进度")
    @GetMapping("/{id}/catalogs")
    public CourseAndSectionVO queryCourseAndCatalogById(@PathVariable("id") Long courseId){
        return courseService.queryCourseAndCatalogById(courseId);
    }
}
