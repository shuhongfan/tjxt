package com.tianji.course.constants;

/**
 * @author wusongsong
 * @since 2022/7/14 13:46
 * @version 1.0.0
 **/
public class CourseErrorInfo {

    //错误信息
    public class Msg {
        public static final String CATEGORY_PARENT_NOT_FOUND = "课程分类父分类没有找到";
        public static final String CATEGORY_CREATE_ON_THIRD = "三级分类下不能再创建子分类";
        public static final String CATEGORY_SAME_NAME = "分类名称不能重复！";
        public static final String CATEGORY_NOT_FOUND = "该分类未找到";
        public static final String CATEGORY_HAVE_CHILD = "该课程有子分类，不能删除";
        public static final String CATEGORY_ADD_NAME_NOT_NULL = "分类名称不能为空！";
        public static final String CATEGORY_ADD_NAME_SIZE = "名称不能超过15个字符";
        public static final String CATEGORY_ADD_INDEX_MAX_MIN = "分类序号格式错误，请重新输入！";
        public static final String CATEGORY_ADD_INDEX_NOT_NULL = "分类序号不能为空！";
        public static final String CATEGORY_ADD_OVER_THIRD_LEVEL = "课程分类不支持三级以上的分类";
        public static final String CATEGORY_ID_NOT_NULL = "未选中课程分类";
        public static final String CATEGORY_DISABLE_ENABLE_STATUS_ENUM = "只有禁用或启用两种状态";
        public static final String CATEGORY_UPDATE_NAME_NOT_NULL = "分类名称不能为空！";
        public static final String CATEGORY_UPDATE_NAME_SIZE = "名称不能超过15个字符";
        public static final String CATEGORY_UPDATE_INDEX_MAX_MIN = "分类序号格式错误，请重新输入！";
        public static final String CATEGORY_UPDATE_INDEX_NOT_NULL = "分类序号不能为空！";
        public static final String CATEGORY_DELETE_HAVE_SUBJECT = "该分类下含有题目，无法删除";
        public static final String CATEGORY_DELETE_HAVE_COURSE = "该分类下含有课程，无法删除";
        public static final String CATEGORY_QUERY_ID_NULL = "查询目录信息，id为空";
        public static final String CATEGORY_DELETE_FAILD = "课程分类删除失败";
        public static final String CATEGORY_ENABLE_CANNOT = "当前上级分类为禁用状态，无法启用";

        public static final String SUBJECT_NAME_EXEISTS = "该题目已存在";
        public static final String SUBJECT_NO_DELETE_BY_USED = "当前题目已被引用，无法删除";
        public static final String SUBJECT_SAVE_CATEGORY_INCOMPLETE = "课程分类不完整";

        public static final String COURSE_SAVE_FAILD = "课程基本信息保存失败";
        public static final String COURSE_SAVE_CATEGORY_NULL = "课程分类为空，请选择课程分类";
        public static final String COURSE_SAVE_CATEGORY_NOT_FOUND = "课程分类未找到";
        public static final String COURSE_CATEGORY_NOT_FOUND = "课程分类未找到";
        public static final String COURSE_SAVE_NAME_NULL = "课程名称为空，请填写课程名称";
        public static final String COURSE_SAVE_COVER_URL_NULL = "课程封面为空，请上传课程封面";
        public static final String COURSE_SAVE_FREE_NULL = "售卖模式为空，请选择售卖模式";
        public static final String COURSE_SAVE_PURCHASE_TIME_NULL = "课程周期为空，请设置课程周期";
        public static final String COURSE_SAVE_INTRODUCE_NULL = "课程介绍为空，请输入课程介绍";
        public static final String COURSE_SAVE_USE_PEOPLE_NULL = "适用人群为空，请输入适用人群";
        public static final String COURSE_SAVE_DETAIL_NULL = "课程详情为空，请输入课程详情";
        public static final String COURSE_SAVE_DURATION_NULL = "学习周期为空，请输入学习周期";
        public static final String COURSE_SAVE_PRICE_NULL = "课程价格为空，请输入课程价格";
        public static final String COURSE_SAVE_PRICE_NEGATIVE = "课程价格为正数，请输入合法的课程价格";
        public static final String COURSE_SAVE_PRICE_FREE = "免费课程没有价格，可以填0";
        public static final String COURSE_SAVE_PURCHASE_ILLEGAL = "课程下架时间不得早于当前时间";
        public static final String COURSE_SAVE_PURCHASE_ILLEGAL2 = "课程开始购买时间不得早于当前";
        public static final String COURSE_SAVE_NAME_EXISTS = "课程名称重复，请重新输入";
        public static final String COURSE_CATAS_SAVE_NAME_NULL = "章名称为空，请输入章名称";
        public static final String COURSE_CATAS_SAVE_NAME_SIZE = "章名称格格式错误，请重新输入";
        public static final String COURSE_CATAS_SAVE_NAME_SIZE2 = "小节名称格格式错误，请重新输入";
        public static final String COURSE_CATAS_SAVE_NAME_NULL2 = "小节名称为空，请输入小节名称";
        public static final String COURSE_CATAS_SAVE_CHAPTER_WITHOUT_SECTION = "章里必须有小节";
        public static final String COURSE_CATAS_SAVE_CHAPTER_NAME_DELETED = "已经上架的{}目录被删除了";
        public static final String COURSE_CATAS_SAVE_CHAPTER_NAME_MOVE = "已经上架的《%s》目录被移动了";
        public static final String COURSE_CATAS_SAVE_INEDX = "目录的章中有序号是重复的";
        public static final String COURSE_CATAS_SAVE_INEDX_JUMP = "目录的章中有序号不连续";
        public static final String COURSE_CATAS_SAVE_CHAPTER_INDEX_REPEAT = "章序号有重复的";
        public static final String COURSE_CATAS_SAVE_CHAPTER_INDEX_INTERRUPTED = "章序号填写有中断";
        public static final String COURSE_CATA_NOT_EXISTS = "目录结构不存在";
        public static final String COURSE_MEDIA_SAVE_ILLEGAL = "请求参数不合法";
        public static final String COURSE_MEDIA_SAVE_SIZE_WRONG = "请检查所有的小节是否关联媒资或题目";
        public static final String COURSE_MEDIA_SAVE_NO_EXECUTE = "媒资当前不能保存";
        public static final String COURSE_MEDIA_SAVE_MEDIA_NULL = "部分章节未选择视频，请选择/上传视频";
        public static final String COURSE_MEDIA_SAVE_TRAILER_NULL = "有课程还没有选择是否支持试看";
        public static final String COURSE_SUBJECT_SAVE_SUBJECT_IDS_NULL = "阶段测试为空，请设置阶段测试题目";
        public static final String COURSE_SUBJECT_SAVE_CATALOGUE_ID_NULL = "题目未指定练习id";
        public static final String COURSE_TEACHER_SAVE_COURSE_ID_NULL = "课程id不能为空";
        public static final String COURSE_TEACHER_SAVE_TEACHERS_NULL = "请至少设置一名教师";
//        public static final String COURSE_TEACHER_SAVE_TEACHERS_NUM_MAX = "最多可设置5名课程老师";
        public static final String COURSE_TEACHER_SAVE_TEACHERS_NUM_MAX = "必须设置老师1-到5人";
        public static final String COURSE_TEACHER_SAVE_TEACHER_SHOW = "老师用户端显示不能为空";
        public static final String COURSE_TEACHER_SAVE_TEACHER_ID_NULL = "老师id不能为空";

        public static final String COURSE_OPERATE_ID_NULL = "未选定课程";

        public static final String COURSE_UP_SHELF_INFO_INCOMPLETE = "课程信息未填写完，无法上架";
        public static final String COURSE_UP_SHELF_STATE_WRONG = "当前课程不能进行上架";
        public static final String COURSE_UP_SHELF_PURCHASE_ILLEGAL = "下架时间需晚于当前时间";
        public static final String COURSE_UP_SHELF_SECTION_WITHOUT_MEDIA = "小节《{}》未上传媒资";
        public static final String COURSE_UP_SHELF_PRACTICE_WITHOUT_SUBJECT = "练习《{}》未上传题目";
        public static final String COURSE_UP_SHELF_NOT_FOUND_COURSE = "未找到对应的课程";
        public static final String COURSE_UP_SHELF_AREADY = "课程已经上架，请勿重复操作";
        public static final String COURSE_DOWN_SHELF_FAILD = "当前课程不能下架";
        public static final String COURSE_CHECK_NOT_FOUND = "未查询到课程信息";
        public static final String COURSE_CHECK_NOT_EXISTS = "某些课程不存在或已经删除";
        public static final String COURSE_CHECK_DOWN_SHELF = "课程已经下架";
        public static final String COURSE_CHECK_FINISHED = "课程已经完结";
        public static final String COURSE_CHECK_NO_SALE = "课程还未开始销售";
    }
}
