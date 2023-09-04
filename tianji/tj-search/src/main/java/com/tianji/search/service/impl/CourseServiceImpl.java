package com.tianji.search.service.impl;

import com.tianji.api.client.course.CourseClient;
import com.tianji.api.dto.course.CourseSearchDTO;
import com.tianji.common.utils.BeanUtils;
import com.tianji.search.domain.po.Course;
import com.tianji.search.repository.CourseRepository;
import com.tianji.search.service.ICourseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CourseServiceImpl implements ICourseService {

    @Resource
    private CourseRepository courseRepository;
    @Resource
    private CourseClient courseClient;

    @Override
    public void handleCourseDelete(Long courseId) {
        // 1.直接删除
        courseRepository.deleteById(courseId);
    }

    @Override
    public void handleCourseUp(Long courseId) {
        // 1.根据id查询课程信息
        CourseSearchDTO courseSearchDTO = courseClient.getSearchInfo(courseId);
        if (courseSearchDTO == null) {
            return;
        }
        // 2.数据转换
        Course course = BeanUtils.toBean(courseSearchDTO, Course.class);
        course.setType(courseSearchDTO.getCourseType());
        // 3.写入索引库
        courseRepository.save(course);

    }

    @Override
    public void updateCourseSold(List<Long> courseIds, int amount) {
        courseRepository.incrementSold(courseIds, amount);
    }

    @Override
    public void handleCourseDeletes(List<Long> courseIds) {
        // 1.直接删除
        courseRepository.deleteByIds(courseIds);
    }
}
