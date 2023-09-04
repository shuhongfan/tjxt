package com.tianji.course.service;

import com.tianji.course.domain.dto.CourseTeacherSaveDTO;
import com.tianji.course.domain.po.CourseTeacherDraft;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tianji.course.domain.vo.CourseTeacherVO;

import java.util.List;

/**
 * <p>
 * 课程老师关系表草稿 服务类
 * </p>
 *
 * @author wusongsong
 * @since 2022-07-20
 */
public interface ICourseTeacherDraftService extends IService<CourseTeacherDraft> {

    /**
     * 保存课程指定的老师
     * @param courseTeacherSaveDTO 教师数据
     */
    void save(CourseTeacherSaveDTO courseTeacherSaveDTO);

    /**
     * 查询指定课程对应的老师
     *
     * @param courseId 课程id
     * @param see 是否用于查看
     * @return 老师数据
     */
    List<CourseTeacherVO> queryTeacherOfCourse(Long courseId,Boolean see);

    /**
     * 课程老师上架
     * @param courseId 课程id
     */
    void copyToShelf(Long courseId, Boolean isFirstShelf);

}
