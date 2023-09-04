package com.tianji.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tianji.course.domain.dto.CategoryAddDTO;
import com.tianji.course.domain.dto.CategoryDisableOrEnableDTO;
import com.tianji.course.domain.dto.CategoryListDTO;
import com.tianji.course.domain.dto.CategoryUpdateDTO;
import com.tianji.course.domain.po.Category;
import com.tianji.course.domain.po.Course;
import com.tianji.course.domain.vo.CategoryInfoVO;
import com.tianji.course.domain.vo.CategoryVO;
import com.tianji.course.domain.vo.SimpleCategoryVO;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程分类 服务类
 * </p>
 *
 * @author wusongsong
 * @since 2022-07-14
 */
public interface ICategoryService extends IService<Category> {

    /**
     * 分页查询课程信息
     *
     * @param categoryPageDTO 分页参数
     * @return 课程分页信息
     */
    List<CategoryVO> list(CategoryListDTO categoryPageDTO);

    /**
     * 新增课程分页
     *
     * @param categoryAddDTO 分类信息
     */
    void add(CategoryAddDTO categoryAddDTO);

    /**
     * 获取课程分类信息
     * @param id 课程id
     * @return 课程分类信息
     */
    CategoryInfoVO get(Long id);

    /**
     * 删除课程分类
     * @param id 分类id
     */
    void delete(Long id);

    /**
     * 课程分类启用或禁用
     */
    void disableOrEnable(CategoryDisableOrEnableDTO categoryDisableOrEnableDTO);

    /**
     * 更新课程分类信息
     */
    void update(CategoryUpdateDTO categoryUpdateDTO);

    /**
     * 获取所有分类的数据及结构
     */
    List<SimpleCategoryVO> all(Boolean admin);

    /**
     * 获取课程分类id和名称
     * @return 课程分类id和名称
     */
    Map<Long, String> getCateIdAndName();

    List<CategoryVO> allOfOneLevel();

    /**
     * 根据课程分类id查询分类列表
     * @param ids  课程分类id
     * @return 分类列表
     */
    List<Category> queryByIds(List<Long> ids);

    /**
     * 根据三级课程分类查询课程分类信息
     * @param thirdCateIdList 三级课程分类
     * @return 课程分类信息
     */
    Map<Long, String> queryByThirdCateIds(@RequestParam("thirdCateIdList") List<Long> thirdCateIdList);

    /**
     * 获取课程分类信息
     *
     * @param course
     * @return
     */
    List<String> queryCourseCategorys(Course course);

    /**
     * 校验课程分类是否符合要求,并按顺序返回一二三级课程分类id列表
     *
     * @param thirdCateId 三级课程分类
     * @return 一二三级课程分类id列表
     */
    List<Long> checkCategory(Long thirdCateId);
}
