package com.tianji.course.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianji.common.constants.Constant;
import com.tianji.common.constants.ErrorInfo;
import com.tianji.common.enums.CommonStatus;
import com.tianji.common.exceptions.BizIllegalException;
import com.tianji.common.exceptions.DbException;
import com.tianji.common.utils.*;
import com.tianji.course.constants.CourseConstants;
import com.tianji.course.constants.CourseErrorInfo;
import com.tianji.course.constants.CourseStatus;
import com.tianji.course.domain.dto.CategoryAddDTO;
import com.tianji.course.domain.dto.CategoryDisableOrEnableDTO;
import com.tianji.course.domain.dto.CategoryListDTO;
import com.tianji.course.domain.dto.CategoryUpdateDTO;
import com.tianji.course.domain.po.Category;
import com.tianji.course.domain.po.Course;
import com.tianji.course.domain.vo.CategoryInfoVO;
import com.tianji.course.domain.vo.CategoryVO;
import com.tianji.course.domain.vo.SimpleCategoryVO;
import com.tianji.course.mapper.CategoryMapper;
import com.tianji.course.mapper.SubjectCategoryMapper;
import com.tianji.course.service.ICategoryService;
import com.tianji.course.service.ICourseDraftService;
import com.tianji.course.service.ICourseService;
import com.tianji.course.utils.CategoryDataWrapper;
import com.tianji.course.utils.CategoryDataWrapper2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程分类 服务实现类
 * </p>
 *
 * @author wusongsong
 * @since 2022-07-14
 */
@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SubjectCategoryMapper subjectCategoryMapper;

    @Autowired
    private ICourseService courseService;

    @Autowired
    private ICourseDraftService courseDraftService;

    @Resource(name = "taskExecutor")
    private Executor taskExecutor;

    @Override
    public List<CategoryVO> list(CategoryListDTO categoryListDTO) {

        //1.搜索条件根据priority正序排序，id逆序排序
        LambdaQueryWrapper<Category> queryWrapper =
                Wrappers.lambdaQuery(Category.class)
                        .orderByAsc(Category::getPriority)
                        .orderByDesc(Category::getUpdateTime);
        //2.查询数据
        List<Category> list = super.list(queryWrapper);
        if (CollUtils.isEmpty(list)) {
            return new ArrayList<>();
        }

        //3.获取课程分类拥有的课程数量
        Map<Long, Long> thirdCategoryNumMap = this.statisticThirdCategory();

        Map<Long, Integer> cateIdAndNumMap = courseService
                .countCourseNumOfCategory();
        //4.通过TreeDataUtils组装数据
        List<CategoryVO> categoryVOS = TreeDataUtils.parseToTree(list, CategoryVO.class,
                //4.1设置转换
                (category, categoryVO) -> {
                    //4.2设置三级分类数量、课程数量、状态描述、排序
                    categoryVO.setThirdCategoryNum(NumberUtils.null2Zero(thirdCategoryNumMap.get(category.getId())).intValue());
                    categoryVO.setCourseNum(NumberUtils.null2Zero(cateIdAndNumMap.get(category.getId())));
                    categoryVO.setStatusDesc(CommonStatus.desc(category.getStatus()));
                    categoryVO.setIndex(category.getPriority());
                }, new CategoryDataWrapper2());
        //5.根据条件过滤
        if (CollUtils.isNotEmpty(categoryVOS)) {
            return fiter(categoryVOS, categoryListDTO);
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional(rollbackFor = {DbException.class, Exception.class})
    public void add(CategoryAddDTO categoryAddDTO) {

        //校验名称是否重复
        checkSameName(categoryAddDTO.getParentId(), categoryAddDTO.getName(), null);
        int level = 1; //默认一级分类
        if (CourseConstants.CATEGORY_ROOT != categoryAddDTO.getParentId()) {
            //校验父分类是否存在
            Category parentCategory = this.baseMapper.selectById(categoryAddDTO.getParentId());
            if (parentCategory == null) {
                throw new BizIllegalException(CourseErrorInfo.Msg.CATEGORY_PARENT_NOT_FOUND);
            }
            //三级课程分类下不能在创建子分类
            if (parentCategory.getLevel() == 3) {
                throw new BizIllegalException(CourseErrorInfo.Msg.CATEGORY_CREATE_ON_THIRD);
            }
            //分类级别，父分类+1
            level = parentCategory.getLevel() + 1;
        }
        if (level > 3) {
            throw new BizIllegalException(CourseErrorInfo.Msg.CATEGORY_ADD_OVER_THIRD_LEVEL);
        }

        //将请求参数转换成PO
        Category category = BeanUtils.copyBean(categoryAddDTO, Category.class, (dto, po) -> {
            po.setPriority(dto.getIndex());
            po.setStatus(CommonStatus.DISABLE.getValue());
        });
        //设置分类级别
        category.setLevel(level);
        if (this.baseMapper.insert(category) <= 0) {
            throw new DbException(null);
        }
    }

    @Override
    public CategoryInfoVO get(Long id) {
        //1.查询数据
        Category category = this.baseMapper.selectById(id);
        //1.1判空
        if (category == null) {
            return new CategoryInfoVO();
        }
        //2.数据组装
        CategoryInfoVO categoryInfoVO = BeanUtils.toBean(category, CategoryInfoVO.class);
        //2.1.课程分类级别
        categoryInfoVO.setCategoryLevel(category.getLevel());
        //2.2.课程分类状态描述
        categoryInfoVO.setStatusDesc(CommonStatus.desc(category.getStatus()));
        //2.3课程分类序号
        categoryInfoVO.setIndex(category.getPriority());
        Long firstCategoryId = null;
        if (category.getLevel() == 3) {
            //2.4.查询二级课程分类
            Category secondCategory = this.baseMapper.selectById(category.getParentId()); //所在二级目录
            //2.5.设置二级课程分类名称
            categoryInfoVO.setSecondCategoryName(secondCategory.getName());
            //2.6.设置一级课程分类id
            firstCategoryId = secondCategory.getParentId();
        } else if (category.getLevel() == 2) {
            //2.7.设置一级课程分类id
            firstCategoryId = category.getParentId();
        }

        if (firstCategoryId != null) {
            //2.8.查询一级课程分类信息
            Category firstCategory = this.baseMapper.selectById(firstCategoryId);
            //2.9设置一级课程分类名称
            categoryInfoVO.setFirstCategoryName(firstCategory.getName());
        }
        return categoryInfoVO;
    }

    @Override
    public void delete(Long id) {
        //1.子分类查询条件
        LambdaQueryWrapper<Category> queryWrapper =
                Wrappers.lambdaQuery(Category.class)
                        .eq(Category::getParentId, id);
        //1.1查询子分类信息
        List<Category> categories = this.baseMapper.selectList(queryWrapper);
        //1.2.子分类判空
        if (CollectionUtil.isNotEmpty(categories)) { //分类有子分类
            throw new BizIllegalException(CourseErrorInfo.Msg.CATEGORY_HAVE_CHILD);
        }
        //2.查询分类信息
        Category category = this.baseMapper.selectById(id);
        //2.1.判空
        if (category == null) {
            throw new DbException(ErrorInfo.Msg.DB_DELETE_EXCEPTION);
        }
        //3.统计分类拥有的课程数量
        Integer courseNum = courseService.countCourseNumOfCategory(id);
        //3.1.分类拥有课程数据量判空
        if (courseNum > 0) {
            throw new BizIllegalException(CourseErrorInfo.Msg.CATEGORY_DELETE_HAVE_COURSE);
        }
        //4.统计分类拥有的题目数量
        int subjectNum = subjectCategoryMapper.countSubjectNum(category.getId(), category.getLevel());
        //4.1.分类拥有的题目数量判空
        if (subjectNum > 0) { //课程含有题目
            throw new BizIllegalException(CourseErrorInfo.Msg.CATEGORY_DELETE_HAVE_SUBJECT);
        }
        //5.删除课程
        int result = this.baseMapper.deleteById(id);
        if (result <= 0) {
            throw new DbException(CourseErrorInfo.Msg.CATEGORY_DELETE_FAILD);
        }
    }

    /**
     * 功能点：
     * 1.启用或禁用课程，下一级或下一级的课程同时启用或禁用，
     * 联动启用或禁用
     *
     * @param categoryDisableOrEnableDTO
     */
    @Override
    @Transactional(rollbackFor = {DbException.class, Exception.class})
    public void disableOrEnable(CategoryDisableOrEnableDTO categoryDisableOrEnableDTO) {

        //1.获取禁用/启用的课程分类
        Category category = baseMapper.selectById(categoryDisableOrEnableDTO.getId());
        if (category == null) {
            throw new BizIllegalException(CourseErrorInfo.Msg.COURSE_CATEGORY_NOT_FOUND);
        }
        //2.校验
        if (category.getParentId() != 0) { //校验父级分类
            //2.1启用校验
            if (categoryDisableOrEnableDTO.getStatus() == CommonStatus.ENABLE.getValue()) {
                //2.2获取父分类
                Category parentCategory = baseMapper.selectById(category.getParentId());
                if (parentCategory == null) {
                    log.error("操作异常，根据父类id查询课程分类未查询到，parentId : {}", category.getParentId());
                    throw new BizIllegalException(ErrorInfo.Msg.OPERATE_FAILED);
                }
                //2.3父分类禁用下不能启用当前分类
                if (CommonStatus.DISABLE.getValue() == parentCategory.getStatus()) {
                    throw new BizIllegalException(CourseErrorInfo.Msg.CATEGORY_ENABLE_CANNOT);
                }
            }
        }

        //3.获取启用/禁用时联动启用的分类
        List<Long> childCategoryIds = new ArrayList<>();
        LambdaQueryWrapper<Category> directQueryWrapper = new LambdaQueryWrapper<>();
        directQueryWrapper.eq(Category::getParentId, categoryDisableOrEnableDTO.getId());
        //3.1获取启用分类的子分类列表
        List<Category> categories = baseMapper.selectList(directQueryWrapper);
        if (CollUtils.isNotEmpty(categories)) { //直接子分类
            //3.2将子类id写入到childCategoryIds中
            childCategoryIds.addAll(categories.stream().map(Category::getId).collect(Collectors.toList()));
        }
        //3.3获取启用分类子分类的子分类列表
        if (CollUtils.isNotEmpty(childCategoryIds)) {
            LambdaQueryWrapper<Category> inDirectQueryWrapper = new LambdaQueryWrapper<>();
            inDirectQueryWrapper.in(Category::getParentId, childCategoryIds);
            List<Category> inDirectCategorys = baseMapper.selectList(inDirectQueryWrapper);
            if (CollUtils.isNotEmpty(inDirectCategorys)) {
                //3.4将子分类的子分类id列表写入childCategoryIds中
                childCategoryIds.addAll(inDirectCategorys.stream()
                        .map(Category::getId).collect(Collectors.toList()));
            }
        }

        //4.更新当前分类，启用/禁用
        int result = this.baseMapper.updateById(BeanUtils.toBean(categoryDisableOrEnableDTO, Category.class));
        if (result <= 0) {
            throw new BizIllegalException(ErrorInfo.Msg.DB_UPDATE_EXCEPTION);
        }
        //5.启用或禁用关联课程分类
        if (CollUtils.isNotEmpty(childCategoryIds)) {
            //5.1更新条件
            LambdaUpdateWrapper<Category> updateWrapper = new LambdaUpdateWrapper();
            updateWrapper.in(Category::getId, childCategoryIds);
            Category updateCategory = new Category();
            updateCategory.setStatus(categoryDisableOrEnableDTO.getStatus());
            //5.2更新关联分类状态
            baseMapper.update(updateCategory, updateWrapper);
        }
        //6.课程分类禁用触发课程批量下架
        if (categoryDisableOrEnableDTO.getStatus() == CommonStatus.DISABLE.getValue()) {
            Long userId = UserContext.getUser();
            taskExecutor.execute(() -> {
                batchDownShelfCourse(category.getId(), category.getLevel(), userId);
            });

        }
    }

    @Override
    @Transactional(rollbackFor = {DbException.class, Exception.class})
    public void update(CategoryUpdateDTO categoryUpdateDTO) {
        //1.查询更新数据
        Category category = this.baseMapper.selectById(categoryUpdateDTO.getId());
        if (category == null) {
            throw new BizIllegalException(CourseErrorInfo.Msg.CATEGORY_NOT_FOUND);
        }
        //2.校验名称是否可以更新
        checkSameName(category.getParentId(), categoryUpdateDTO.getName(), categoryUpdateDTO.getId());
        //3.设置更新字段
        Category updateCategory = new Category();
        updateCategory.setId(categoryUpdateDTO.getId()); //修改课程分类id
        updateCategory.setPriority(categoryUpdateDTO.getIndex());
        updateCategory.setName(categoryUpdateDTO.getName());
        //4.更新
        int result = this.baseMapper.updateById(updateCategory);
        if (result <= 0) {
            throw new BizIllegalException(ErrorInfo.Msg.DB_UPDATE_EXCEPTION);
        }
    }

    @Override
    public List<SimpleCategoryVO> all(Boolean admin) {
        // 1.查询有课程的课程分类id列表
        List<Long> categoryIdList = admin ?
                null :courseService.getCategoryIdListWithCourse();
        // 1.1.判空
        if(!admin && CollUtils.isEmpty(categoryIdList)){
            return new ArrayList<>();
        }

        // 2.升序查询所有未禁用的课程分类
        LambdaQueryWrapper<Category> queryWrapper = Wrappers.lambdaQuery(Category.class)
                .eq(!admin, Category::getStatus, CommonStatus.ENABLE.getValue())
                .in(CollectionUtil.isNotEmpty(categoryIdList), Category::getId, categoryIdList)
                .orderByAsc(Category::getPriority)
                .orderByDesc(Category::getId);
        List<Category> categories = this.baseMapper.selectList(queryWrapper);

        // 3.将课程分类转换成树状结构
        List<SimpleCategoryVO> simpleCategoryVOS = TreeDataUtils.parseToTree(categories,
                SimpleCategoryVO.class, new CategoryDataWrapper());
        // 4.过滤掉没有三级子课程分类的课程分类
        filter(simpleCategoryVOS);
        return simpleCategoryVOS;

    }

    @Override
    public Map<Long, String> getCateIdAndName() {
        List<Category> categories = this.baseMapper.selectList(null);
        return categories.stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));
    }

    @Override
    public List<CategoryVO> allOfOneLevel() {
        //1.查询数据
        List<Category> list = super.list();
        if (CollUtils.isEmpty(list)) {
            return new ArrayList<>();
        }

        //2.统计一级二级目录对应的三级目录的数量，做一个三分钟的redis缓存
        Map<Long, Long> thirdCategoryNumMap = this.statisticThirdCategory();
        return BeanUtils.copyList(list, CategoryVO.class, (category, categoryVO) -> {
            categoryVO.setThirdCategoryNum(thirdCategoryNumMap.getOrDefault(category.getId(), 0L).intValue());
        });
    }

    @Override
    public List<Category> queryByIds(List<Long> ids) {
        if (CollUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<Category> queryWrapper =
                Wrappers.lambdaQuery(Category.class)
                        .in(Category::getId, ids);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public Map<Long, String> queryByThirdCateIds(List<Long> thirdCateIdList) {
        Map<Long, String> resultMap = new HashMap<>();
        //1.校验
        // 1.1判断参数是否为空
        if (CollUtils.isEmpty(thirdCateIdList)) {
            return resultMap;
        }
        // 1.2校验分类id都是三级分类id
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getLevel, 3)
                .in(Category::getId, thirdCateIdList);
        int thirdCateNum = baseMapper.selectCount(queryWrapper);
        if (!NumberUtils.equals(thirdCateNum, thirdCateIdList.size())) {
            throw new BizIllegalException(ErrorInfo.Msg.REQUEST_PARAM_ILLEGAL);
        }
        //2.查询所有分类，并将分类转化成map
        List<Category> categories = baseMapper.selectList(null);
        Map<Long, Category> categoryMap = categories.stream()
                .collect(Collectors.toMap(Category::getId, p -> p));
        //3.遍历三级分类id
        for (Long thirdCateId : thirdCateIdList) {
            //3.1三级分类
            Category thirdCategory = categoryMap.get(thirdCateId);

            //3.2二级分类
            Category secondCategory = categoryMap.get(thirdCategory.getParentId());
            //3.3一级分类
            Category firstCategory = categoryMap.get(thirdCateId);
            resultMap.put(thirdCateId, StringUtils.format("{}/{}/{}",
                    firstCategory.getName(), secondCategory.getName(), thirdCategory.getName()));
        }
        return resultMap;
    }

    @Override
    public List<String> queryCourseCategorys(Course course) {
        //1.查询课程分类
        List<Category> categories = baseMapper.selectBatchIds(
                Arrays.asList(course.getFirstCateId(),
                        course.getSecondCateId(),
                        course.getThirdCateId()));
        if (CollUtils.isNotEmpty(categories)) {
            return new ArrayList<>();
        }
        Map<Long, String> categoryIdAndNameMap = categories.stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));
        //2.按照分类层级关系组装成列表
        return Arrays.asList(categoryIdAndNameMap.get(course.getFirstCateId()),
                categoryIdAndNameMap.get(course.getSecondCateId()),
                categoryIdAndNameMap.get(course.getThirdCateId()));
    }

    @Override
    public List<Long> checkCategory(Long thirdCateId) {
        //1.查询三级课程分类
        Category thirdCategory = baseMapper.selectById(thirdCateId);
        //1.1判断三级课程分类状态
        if (thirdCategory.getStatus() != CommonStatus.ENABLE.getValue()) {
            throw new BizIllegalException(CourseErrorInfo.Msg.COURSE_CATEGORY_NOT_FOUND);
        }
        //2.查询二级课程分类
        Category secondCategory = baseMapper.selectById(thirdCategory.getParentId());
        //2.1判断三级课程分类状态
        if (secondCategory.getStatus() != CommonStatus.ENABLE.getValue()) {
            throw new BizIllegalException(CourseErrorInfo.Msg.COURSE_CATEGORY_NOT_FOUND);
        }
        //3.返回数据
        return Arrays.asList(secondCategory.getParentId(), secondCategory.getId(), thirdCateId);
    }

    /**
     * 获取一级二级没有下一级分类的分类id列表
     * @return
     */
    private List<Long> getCateIdsWithoutChildCateId(){
        // 1.查询数据
        List<Category> list = list();
        // 1.1.判空
        if(CollUtils.isEmpty(list)){
            return new ArrayList<>();
        }
        // 2.list转map
        Map<Long, List<Category>> idAndParentIdMap = list.stream()
                .collect(Collectors.groupingBy(Category::getParentId));
        // 3.遍历
        return list.stream()
                .filter(category -> category.getLevel() < 3 && !idAndParentIdMap.containsKey(category.getId()))
                .map(Category::getId)
                .collect(Collectors.toList());
    }


    /**
     * 新增或更新时校验是否有同名的分类
     *
     * @param parentId  父id
     * @param name
     * @param currentId
     */
    private void checkSameName(Long parentId, String name, Long currentId) {
        //1.统计同一个父分类的子分类列表中有同名分类，或和父分类同名查询条件
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.or().eq(true, Category::getParentId, parentId)
                .eq(Category::getName, name)
                .eq(Category::getDeleted, Constant.DATA_NOT_DELETE);
        queryWrapper.or().eq(Category::getId, parentId)
                .eq(Category::getName, name);
        //2.统计符合上述条件的分类列表
        List<Category> categories = this.baseMapper.selectList(queryWrapper);
        //3.新增情况下，有同名的分类
        if (currentId == null && CollectionUtil.isNotEmpty(categories)) {
            throw new BizIllegalException(CourseErrorInfo.Msg.CATEGORY_SAME_NAME);
        }
        //4.更新情况下出现同名，需要判断是否是当前分类的名称
        if (CollectionUtil.isNotEmpty(categories) && categories.get(0).getId() != currentId.longValue()) {
            throw new BizIllegalException(CourseErrorInfo.Msg.CATEGORY_SAME_NAME);
        }
    }

    private void setThirdCategoryNum(List<CategoryVO> categoryVOList){
        // 1.判空
        if(CollUtils.isEmpty(categoryVOList)){
            return;
        }
        // 2.
    }

    /**
     * 统计每个一级目录二级目录有多少个三级目录
     *
     * @return
     */
    private Map<Long, Long> statisticThirdCategory() {
        Map<Long, Long> result = new HashMap<>();
        // 1.查询所有数据
        List<Category> categories = baseMapper.selectList(null);
        // 1.1.判空
        if(CollUtils.isEmpty(categories)){
            return result;
        }

        // 2.二级分类的拥有的三级课程分类数量
        Map<Long, Long> collect = categories.stream()
                .filter(category -> category.getLevel() == 3)
                .collect(Collectors.groupingBy(Category::getParentId, Collectors.counting()));
        result.putAll(collect);
        // 3.一级分类拥有的三级课程分类数量
        Map<Long, List<Category>> category2Map = categories.stream()
                .filter(category -> category.getLevel() == 2)
                .collect(Collectors.groupingBy(Category::getParentId));
        // 3.1.遍历category2Map
        for (Map.Entry<Long, List<Category>> entry : category2Map.entrySet()){
            long sum = entry.getValue()
                    .stream()
                    .map(category -> NumberUtils.null2Zero(result.get(category.getId())))
                    .collect(Collectors.summarizingLong(num -> num))
                    .getSum();
            result.put(entry.getKey(), sum);
        }
        // 4.返回结果
        return result;
    }

    /**
     * 根据条件过滤课程分类
     *
     * @param categoryVOList
     * @param categoryListDTO
     * @return
     */
    private List<CategoryVO> fiter(List<CategoryVO> categoryVOList, CategoryListDTO categoryListDTO) {
        if (CollUtils.isEmpty(categoryVOList)) {
            return new ArrayList<>();
        }
        return categoryVOList.stream().
                filter(categoryVO -> filter(categoryVO, categoryListDTO))
                .collect(Collectors.toList());
    }

    /**
     * 递归过滤出查询的数据，当前分类是否符合条件 = 当前分类信息符合条件 OR 有符合条件的子分类
     * 1.校验信息状态和名称符合dto要求
     * 2.循环遍历子分类，子分类不符合条件将从子分类列表中删除
     * 3.步骤1中通过 + 是否还有子分类（步骤2删除了不符合条件的）
     *
     * @param categoryVO
     * @param categoryListDTO
     * @return 当前分类是否符合条件
     */
    private boolean filter(CategoryVO categoryVO, CategoryListDTO categoryListDTO) {

        //当前分类通过，或者子分类有一个通过则都通过
        //不需要过滤
        if (StringUtils.isEmpty(categoryListDTO.getName()) && categoryListDTO.getStatus() == null) {
            return true;
        }
        boolean pass = true;
        // 状态校验
        if (categoryListDTO.getStatus() != null) { //和查询状态一致pass
            pass = (categoryVO.getStatus() == categoryListDTO.getStatus());
        }
        //名称校验
        if (pass && StringUtils.isNotEmpty(categoryListDTO.getName())) {//状态pass通过后校验名称，包含名称关键字 通过
            pass = StringUtils.isNotEmpty(categoryVO.getName()) && categoryVO.getName().contains(categoryListDTO.getName());
        }
        //分类信息校验未通过，并且没有子分类，当前分类不符合条件
        if (!pass && CollUtils.isEmpty(categoryVO.getChildren())) { //告诉上一级没通过
            return false;
        }
        //遍历子分类是否符合条件
        for (int count = categoryVO.getChildren().size() - 1; count >= 0; count--) {
            CategoryVO child = categoryVO.getChildren().get(count);
            //子分类校验
            boolean childPass = filter(child, categoryListDTO);
            if (!childPass) { //子分类不符合条件，从子分类列表中删除
                categoryVO.getChildren().remove(count);
            }
        }
        return pass || CollUtils.isNotEmpty(categoryVO.getChildren());
    }

    private void batchDownShelfCourse(Long categoryId, Integer level, Long userId) {
        //1.多线程下设置操作用户id
        UserContext.setUser(userId);
        //2.查询需要下架的课程
        List<Course> courses = courseService.queryByCategoryIdAndLevel(categoryId, level);
        if (CollUtils.isEmpty(courses)) {
            return;
        }
        //3.遍历下架课程
        for (Course course : courses) {
            //4.判断状态是否可以下架
            if (!CourseStatus.SHELF.equals(course.getStatus())) {
                continue;
            }
            try {
                //5.课程下架
                courseDraftService.downShelf(course.getId());
            } catch (Exception e) {
                log.error("课程下架异常");
            }
        }
    }

    /**
     * 过滤掉没有三级课程分类的
     * @param simpleCategoryVOS
     */
    private void filter(List<SimpleCategoryVO> simpleCategoryVOS){
        // 1.判空
        if(CollUtils.isEmpty(simpleCategoryVOS)){
            return;
        }
        // 2.遍历分类列表
        for (int count = simpleCategoryVOS.size() -1; count >= 0; count--) {
            SimpleCategoryVO simpleCategoryVO = simpleCategoryVOS.get(count);
            if(simpleCategoryVO.getLevel() == 3){
                continue;
            }
            filter(simpleCategoryVO.getChildren());
            if(CollUtils.isEmpty(simpleCategoryVO.getChildren())){
                simpleCategoryVOS.remove(count);
            }
        }
    }
}
