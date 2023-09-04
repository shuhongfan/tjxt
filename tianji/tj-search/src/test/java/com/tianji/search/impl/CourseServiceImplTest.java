package com.tianji.search.impl;

import com.tianji.api.client.course.CourseClient;
import com.tianji.api.dto.course.CourseSearchDTO;
import com.tianji.common.utils.BeanUtils;
import com.tianji.common.utils.RandomUtils;
import com.tianji.search.domain.po.Course;
import com.tianji.search.domain.vo.CourseVO;
import com.tianji.search.repository.CourseRepository;
import com.tianji.search.service.ICourseService;
import com.tianji.search.service.ISearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class CourseServiceImplTest {

    @Autowired
    private ISearchService searchService;

    @Autowired
    private CourseRepository repository;

    @Autowired
    private CourseClient courseClient;

    @Autowired
    private ICourseService courseService;

    @Test
    void testTop5Best() {
        List<CourseVO> courseVOS = searchService.queryBestTopN();
        for (CourseVO courseVO : courseVOS) {
            System.out.println("courseVO = " + courseVO);
        }
    }

    @Test
    void testTop5New() {
        List<CourseVO> courseVOS = searchService.queryNewTopN();
        for (CourseVO courseVO : courseVOS) {
            System.out.println("courseVO = " + courseVO);
        }
    }

    @Test
    void testTop5Free() {
        List<CourseVO> courseVOS = searchService.queryFreeTopN();
        for (CourseVO courseVO : courseVOS) {
            System.out.println("courseVO = " + courseVO);
        }
    }

    static List<Long> teachers = List.of(
            1548889371405492225L, 1548940676303970306L,
            1548940777449611265L, 1548940921662365698L,
            1548941239125041153L, 1548941336596471809L
    );

    @Test
    void testLoadCourse() {
        for (long i = 1549025085494521857L; i <= 1549025085494521857L; i++) {
            // 1.根据id查询课程信息
            CourseSearchDTO courseSearchDTO = courseClient.getSearchInfo(i);
            if (courseSearchDTO == null) {
                return;
            }
            // 2.数据转换
            Course course = BeanUtils.toBean(courseSearchDTO, Course.class);
            course.setType(courseSearchDTO.getCourseType());
            course.setScore(41 + RandomUtils.randomInt(10));
            course.setSold(0);
            // 3.写入索引库
            repository.save(course);
        }
    }

    @Test
    void testRepository() {
        List<Course> list = new ArrayList<>();

        for (int i = 0; i < 200; i++) {
            LocalDateTime time = LocalDateTime.now().minusDays(RandomUtils.randomInt(20));
            Course course = new Course();
            course.setId(1L + i);
            course.setCategoryIdLv1(1001L);
            course.setCategoryIdLv2(2000L + RandomUtils.randomInt(1,9));
            course.setCategoryIdLv3(3007L);
            course.setCoverUrl("default-cover-url.jpg");
            course.setFree(RandomUtils.randomBoolean());
            course.setName("Java实战课");
            course.setPublishTime(time);
            course.setScore(30 + RandomUtils.randomInt(20));
            course.setSections(10 + RandomUtils.randomInt(10));
            course.setSold(200 + RandomUtils.randomInt(1000));
            course.setTeacher(teachers.get(RandomUtils.randomInt(5)));
            course.setType(2);
            list.add(course);
        }

        repository.saveAll(list);
    }
}