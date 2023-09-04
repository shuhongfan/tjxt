package com.tianji.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianji.api.client.user.UserClient;
import com.tianji.api.dto.user.UserDTO;
import com.tianji.common.constants.ErrorInfo;
import com.tianji.common.exceptions.DbException;
import com.tianji.common.utils.BeanUtils;
import com.tianji.common.utils.CollUtils;
import com.tianji.course.constants.CourseConstants;
import com.tianji.course.domain.dto.CourseTeacherSaveDTO;
import com.tianji.course.domain.po.CourseTeacher;
import com.tianji.course.domain.po.CourseTeacherDraft;
import com.tianji.course.domain.vo.CourseTeacherVO;
import com.tianji.course.mapper.CourseTeacherDraftMapper;
import com.tianji.course.service.ICourseDraftService;
import com.tianji.course.service.ICourseTeacherDraftService;
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
public class CourseTeacherDraftServiceImpl extends ServiceImpl<CourseTeacherDraftMapper, CourseTeacherDraft> implements ICourseTeacherDraftService {

    @Autowired
    private ICourseDraftService courseDraftService;

    @Autowired
    private ICourseTeacherService courseTeacherService;

    @Autowired
    private UserClient userClient;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(CourseTeacherSaveDTO courseTeacherSaveDTO) {

        //1.数据删除条件
        LambdaUpdateWrapper<CourseTeacherDraft> updateWrapper =
                Wrappers.lambdaUpdate(CourseTeacherDraft.class)
                        .eq(CourseTeacherDraft::getCourseId, courseTeacherSaveDTO.getId());
        //1.1.数据删除
        baseMapper.delete(updateWrapper);

        //2.组装即将插入的数据
        List<CourseTeacherDraft> courseTeacherDrafts =
                BeanUtils.copyList(courseTeacherSaveDTO.getTeachers(),
                        CourseTeacherDraft.class, (teacherInfo, teacherDraft) -> {
                            //2.1.设置课程id
                            teacherDraft.setCourseId(courseTeacherSaveDTO.getId());
                            //2.2.设置老师id
                            teacherDraft.setTeacherId(teacherInfo.getId());
                            //2.3.设置课程中老师排序
                            teacherDraft.setCIndex(courseTeacherSaveDTO.getTeachers().indexOf(teacherInfo));
                        });
        //3.批量插入课程的老师信息
        saveBatch(courseTeacherDrafts);
        //4.更新课程填写进度
        courseDraftService.updateStep(courseTeacherSaveDTO.getId(), CourseConstants.CourseStep.TEACHER);
    }

    @Override
    public List<CourseTeacherVO> queryTeacherOfCourse(Long courseId, Boolean see) {
        if (see) {
            //1.查询课程老师关系
            List<CourseTeacherVO> courseTeacherVOS = courseTeacherService.queryTeachers(courseId);
            //1.1.课程老师关系判非空
            if (CollUtils.isNotEmpty(courseTeacherVOS)) {
                return courseTeacherVOS;
            }
            //2.查询草稿中的课程老师关系
            courseTeacherVOS = queryTeachers(courseId);
            //3.组装数据
            return CollUtils.isEmpty(courseTeacherVOS) ? new ArrayList<>() : courseTeacherVOS;
        } else {
            //4.查询草稿中的课程老师关系
            return queryTeachers(courseId);
        }
    }

    @Override
    @Transactional(rollbackFor = {DbException.class, Exception.class})
    public void copyToShelf(Long courseId, Boolean isFirstShelf) {
        //1.先将草稿中的数据查出来
        LambdaQueryWrapper<CourseTeacherDraft> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseTeacherDraft::getCourseId, courseId);
        List<CourseTeacherDraft> courseTeacherDrafts = baseMapper.selectList(queryWrapper);
        //2.删除架上的老师数据，
        if (!isFirstShelf) {
            courseTeacherService.deleteByCourseId(courseId);
        }
        //3.将草稿上架
        List<CourseTeacher> courseTeachers = BeanUtils.copyList(courseTeacherDrafts, CourseTeacher.class);
        courseTeacherService.saveOrUpdateBatch(courseTeachers);
        //4.删除草稿
        if (baseMapper.deleteByCourseId(courseId) <= 0) {
            throw new DbException(ErrorInfo.Msg.DB_DELETE_EXCEPTION);
        }
    }

    private List<CourseTeacherVO> queryTeachers(Long couserId) {

        //1.查询条件
        LambdaQueryWrapper<CourseTeacherDraft> queryWrapper =
                Wrappers.lambdaQuery(CourseTeacherDraft.class)
                        .eq(CourseTeacherDraft::getCourseId, couserId);
        //1.1.查询数据
        List<CourseTeacherDraft> courseTeacherDrafts = baseMapper.selectList(queryWrapper);
        //1.2.数据判空
        if (CollUtils.isEmpty(courseTeacherDrafts)) {
            return new ArrayList<>();
        }

        // 2.查询教师详细信息
        List<UserDTO> UserDTOS = userClient.queryUserByIds(
                courseTeacherDrafts.stream().map(CourseTeacherDraft::getTeacherId).collect(Collectors.toList()));
        // 3.组织为map
        Map<Long, UserDTO> UserDTOMap = UserDTOS.stream().collect(Collectors.toMap(UserDTO::getId, UserDTO -> UserDTO));

        //4.数据组装
        return BeanUtils.copyList(courseTeacherDrafts, CourseTeacherVO.class,
                (courseTeacher, courseTeacherVO) -> {
            //4.1.老师信息
                    UserDTO teacherDetailDTO = UserDTOMap.get(courseTeacher.getTeacherId());
            if (teacherDetailDTO != null) {
                //4.2.设置老师形象照
                courseTeacherVO.setIcon(teacherDetailDTO.getIcon());
                courseTeacherVO.setPhoto(teacherDetailDTO.getPhoto());
                //4.3.设置老师姓名
                courseTeacherVO.setName(teacherDetailDTO.getName());
                //4.4.设置老师介绍
                courseTeacherVO.setIntroduce(teacherDetailDTO.getIntro());
                //4.5.设置老师职业
                courseTeacherVO.setJob(teacherDetailDTO.getJob());
            }
            //4.6.设置老师id
            courseTeacherVO.setId(courseTeacher.getTeacherId());
        });
    }

}
