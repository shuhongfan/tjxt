package com.tianji.api.client.course;

import com.tianji.api.dto.course.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(contextId = "course", value = "course-service")
public interface CourseClient {

    /**
     * 根据老师id列表获取老师出题数据和讲课数据
     * @param teacherIds 老师id列表
     * @return 老师id和老师对应的出题数和教课数
     */
    @GetMapping("/course/infoByTeacherIds")
    List<SubNumAndCourseNumDTO> infoByTeacherIds(@RequestParam("teacherIds") Iterable<Long> teacherIds);

    /**
     * 根据小节id获取小节对应的mediaId和课程id
     *
     * @param sectionId 小节id
     * @return 小节对应的mediaId和课程id
     */
    @GetMapping("/course/section/{id}")
    SectionInfoDTO sectionInfo(@PathVariable("id") Long sectionId);

    /**
     * 根据媒资Id列表查询媒资被引用的次数
     *
     * @param mediaIds 媒资id列表
     * @return 媒资id和媒资被引用的次数的列表
     */
    @GetMapping("/course/media/useInfo")
    List<MediaQuoteDTO> mediaUserInfo(@RequestParam("mediaIds") Iterable<Long> mediaIds);

    /**
     * 根据课程id查询索引库需要的数据
     *
     * @param id 课程id
     * @return 索引库需要的数据
     */
    @GetMapping("/course/{id}/searchInfo")
    CourseSearchDTO getSearchInfo(@PathVariable("id") Long id);

    /**
     * 根据课程id集合查询课程简单信息
     * @param ids id集合
     * @return 课程简单信息的列表
     */
    @GetMapping("/courses/simpleInfo/list")
    List<CourseSimpleInfoDTO> getSimpleInfoList(@RequestParam("ids") Iterable<Long> ids);

    /**
     * 根据课程id，获取课程、目录、教师信息
     * @param id 课程id
     * @return 课程信息、目录信息、教师信息
     */
    @GetMapping("/course/{id}")
    CourseFullInfoDTO getCourseInfoById(
            @PathVariable("id") Long id,
            @RequestParam(value = "withCatalogue", required = false) boolean withCatalogue,
            @RequestParam(value = "withTeachers", required = false) boolean withTeachers
    );
}