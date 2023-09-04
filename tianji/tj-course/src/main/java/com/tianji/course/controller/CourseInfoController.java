package com.tianji.course.controller;

import com.tianji.api.dto.course.*;
import com.tianji.common.utils.CollUtils;
import com.tianji.course.service.ICategoryService;
import com.tianji.course.service.ICourseCatalogueService;
import com.tianji.course.service.ICourseService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 内部服务接口调用
 *
 * @ClassName CourseInfoController
 * @Author wusongsong
 * @Date 2022/7/18 15:19
 * @Version
 **/
@RestController
@RequestMapping("course")
@Api(tags = "课程相关接口，内部调用")
public class CourseInfoController {

    @Autowired
    private ICourseCatalogueService courseCatalogueService;

    @Autowired
    private ICourseService courseService;

    @Autowired
    private ICategoryService categoryService;

    @GetMapping("infoByTeacherIds")
    @ApiOperation("通过老师id获取老师负责的课程和出的题目数量")
    public List<SubNumAndCourseNumDTO> infoByTeacherIds(@RequestParam("teacherIds") List<Long> teacherIds) {

        if (CollUtils.isEmpty(teacherIds)) {
            return new ArrayList<>();
        }
        return courseService.countSubjectNumAndCourseNumOfTeacher(teacherIds);
    }

    /**
     * 根据小节id获取小节对应的mediaId和课程id
     *
     * @param sectionId 小节id
     * @return 小节对应的mediaId和课程id
     */
    @GetMapping("/section/{id}")
    @ApiImplicitParam(name = "id", value = "小节id，不支持章id或者练习id查询")
    public SectionInfoDTO sectionInfo(@PathVariable("id") Long sectionId) {
        return courseCatalogueService.getSimpleSectionInfo(sectionId);
    }

    /**
     * 根据媒资Id列表查询媒资被引用的次数
     *
     * @param mediaIds 媒资id列表
     * @return 媒资id和媒资被引用的次数的列表
     */
    @GetMapping("/media/useInfo")
    public List<MediaQuoteDTO> mediaUserInfo(@RequestParam("mediaIds") List<Long> mediaIds) {
        return courseCatalogueService.countMediaUserInfo(mediaIds);
    }

    @GetMapping("/{id}/searchInfo")
    @ApiOperation("课程上架时，需要查询课程信息，加入索引库")
    public CourseDTO getSearchInfo(@ApiParam("课程id") @PathVariable("id") Long id) {
        return courseService.getCourseDTOById(id);
    }

    @GetMapping("/{id}")
    @ApiOperation("获取课程信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "获取课程信息"),
            @ApiImplicitParam(name = "withCatalogue", value = "是否要查询目录信息"),
            @ApiImplicitParam(name = "withTeachers", value = "是否查询课程老师信息")
    })
    public CourseFullInfoDTO getById(
            @PathVariable("id") Long id,
            @RequestParam(value = "withCatalogue", required = false) boolean withCatalogue,
            @RequestParam(value = "withTeachers", required = false) boolean withTeachers) {
        return courseService.getInfoById(id, withCatalogue, withTeachers);
    }


    @GetMapping("/getCateNameMap")
    @ApiIgnore
    public Map<Long, String> queryByThirdCateIds(@RequestParam("thirdCateIdList") List<Long> thirdCateIdList) {
        return categoryService.queryByThirdCateIds(thirdCateIdList);
    }

    @GetMapping("/name")
    public List<Long> queryCoursesIdByName(@RequestParam("name") String name){
        return courseService.queryCourseIdByName(name);
    }
}
