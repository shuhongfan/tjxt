package com.tianji.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianji.api.client.user.UserClient;
import com.tianji.api.dto.user.UserDTO;
import com.tianji.common.constants.ErrorInfo;
import com.tianji.common.exceptions.DbException;
import com.tianji.common.utils.BeanUtils;
import com.tianji.common.utils.CollUtils;
import com.tianji.course.domain.po.CourseTeacher;
import com.tianji.course.domain.vo.CourseTeacherVO;
import com.tianji.course.mapper.CourseTeacherMapper;
import com.tianji.course.service.ICourseTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程老师关系表草稿 服务实现类
 * </p>
 *
 * @author wusongsong
 * @since 2022-07-20
 */
@Service
public class CourseTeacherServiceImpl extends ServiceImpl<CourseTeacherMapper, CourseTeacher> implements ICourseTeacherService {

    @Autowired
    private UserClient userClient;

    @Override
    public List<CourseTeacherVO> queryTeachers(Long couserId) {
        //1.查询条件
        LambdaQueryWrapper<CourseTeacher> queryWrapper =
                Wrappers.lambdaQuery(CourseTeacher.class)
                        .eq(CourseTeacher::getCourseId, couserId);
        //2.查询数据
        List<CourseTeacher> courseTeachers = baseMapper.selectList(queryWrapper);
        //2.1.数据判空
        if (CollUtils.isEmpty(courseTeachers)) {
            return null;
        }
        //3.查询教师信息
        List<UserDTO> teacherDetailDTOS =
                userClient.queryUserByIds(
                        courseTeachers
                                .stream()
                                .map(CourseTeacher::getTeacherId)
                                .collect(Collectors.toList()));
        //3.1.教师id+教师信息map
        Map<Long, UserDTO> teacherDetailDTOMap =
                teacherDetailDTOS
                        .stream()
                        .collect(Collectors.toMap(UserDTO::getId,
                                TeacherDetailDTO -> TeacherDetailDTO));
        //4.组装数据
        return BeanUtils.copyList(courseTeachers, CourseTeacherVO.class,
                (courseTeacher, courseTeacherVO) -> {
                    //4.1.教师信息
                    UserDTO teacherDetailDTO = teacherDetailDTOMap.get(courseTeacher.getTeacherId());
                    if (teacherDetailDTO != null) {
                        //4.2设置教师形象照
                        courseTeacherVO.setIcon(teacherDetailDTO.getPhoto());
                        //4.3设置教师姓名
                        courseTeacherVO.setName(teacherDetailDTO.getName());
                        //4.4.设置教师简介
                        courseTeacherVO.setIntroduce(teacherDetailDTO.getIntro());
                        //4.5.设置教师职业
                        courseTeacherVO.setJob(teacherDetailDTO.getJob());
                    }
                    //4.6.设置教师id
                    courseTeacherVO.setId(courseTeacher.getTeacherId());
                });
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {DbException.class, Exception.class})
    public void deleteByCourseId(Long courserId) {
        //1.删除教师-课程关系
        if (baseMapper.deleteByCourseId(courserId) <= 0) {
            throw new DbException(ErrorInfo.Msg.DB_DELETE_EXCEPTION);
        }
    }

    @Override
    public List<Long> getTeacherIdOfCourse(Long courseId) {
        //1.查询条件
        LambdaQueryWrapper<CourseTeacher> queryWrapper =
                Wrappers.lambdaQuery(CourseTeacher.class)
                        .eq(CourseTeacher::getCourseId, courseId)
                        .orderByAsc(CourseTeacher::getCIndex);
        //2.查询数据
        List<CourseTeacher> courseTeachers = baseMapper.selectList(queryWrapper);
        //3.组装数据
        return CollUtils.isEmpty(courseTeachers) ?
                new ArrayList<>() : courseTeachers
                .stream()
                .map(CourseTeacher::getTeacherId)
                .collect(Collectors.toList());
    }

}
