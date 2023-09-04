package com.tianji.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tianji.course.domain.dto.CataSaveDTO;
import com.tianji.course.domain.dto.CataSubjectDTO;
import com.tianji.course.domain.dto.CourseMediaDTO;
import com.tianji.course.domain.po.CourseCatalogueDraft;
import com.tianji.course.domain.vo.CataSimpleSubjectVO;
import com.tianji.course.domain.vo.CataVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 目录草稿 服务类
 * </p>
 *
 * @author wusongsong
 * @since 2022-07-19
 */
public interface ICourseCatalogueDraftService extends IService<CourseCatalogueDraft> {

    /**
     * 保存课程对应的目录结构
     *
     * @param courseId 课程id
     * @param cataSaveDTOS 课程目录信息
     */
    void save(Long courseId, List<CataSaveDTO> cataSaveDTOS, Integer step);

    /**
     * 查询课程的目录
     *
     * @param courseId 课程id必填
     * @param see      是否用于查看数据
     * @return
     */
    List<CataVO> queryCourseCatalogues(Long courseId, Boolean see, Boolean withPractice);

    /**
     * 保存媒资信息
     */
    void saveMediaInfo(Long courseId, List<CourseMediaDTO> courseMediaDTOS);

    void saveSuject(Long courseId, List<CataSubjectDTO> cataSubjectDTOS);

    /**
     * 根据课程id，获取题目，用于草稿编辑
     *
     * @param courseId
     * @return
     */
    List<CataSimpleSubjectVO> getSuject(Long courseId);

    /**
     * 校验课程对应的目录数据完整，包含  视频，题目，
     *
     * @param courseId
     */
    void checkCataInfoImplated(Long courseId);

    /**
     * 复制试题到架上
     *
     * @param courseId
     * @param isFirstShelf
     */
    void copySubjectToShelf(Long courseId, Boolean isFirstShelf);

    /**
     * copy目录到架上
     *
     * @param courseId
     * @param isFirstShelf
     */
    void copyToShelf(Long courseId, Boolean isFirstShelf);

    /**
     * 计算当前课程中每个章媒资时长
     *
     * @param courseId
     * @return
     */
    Map<Long, Integer> calculateMediaDuration(Long courseId);

    /**
     * 课程总节和练习数，不包含章
     *
     * @param courseId
     * @return
     */
    Integer totalSectionNums(Long courseId);

    /**
     * 根据类型查询课程小节/章/测试id列表
     *
     * @param courseId
     * @param types
     * @return
     */
    List<Long> queryCataIdsOfCourse(Long courseId, List<Integer> types);

}
