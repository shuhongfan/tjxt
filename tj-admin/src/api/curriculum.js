import request from "@/utils/request.js";
// 获取课程分类列表
export const getCurriculumType = (params) =>
  request({
    url: `/cs/categorys/list`,
    method: "get",
    params,
  });
// 获取详情
export const getDetails = (id) =>
  request({
    url: `/cs/categorys/${id}`,
    method: "get",
  });
// 新增分类
export const addCurriculumType = (params) =>
  request({
    url: `/cs/categorys/add`,
    method: "post",
    data: params,
  });
// 编辑分类
export const editCurriculumType = (params) =>
  request({
    url: `/cs/categorys/update`,
    method: "put",
    data: params,
  });
// 课程分类停用或启用
export const editCurriculumStatus = (params) =>
  request({
    url: `/cs/categorys/disableOrEnable`,
    method: "put",
    data: params,
  });
// 删除分类信息
export const deleteType = (id) =>
  request({
    url: `/cs/categorys/${id}`,
    method: "delete",
  });
// 删除分类信息
export const deleteCourses = (id) =>
  request({
    url: `/cs/courses/delete/${id}`,
    method: "delete",
  });
// 获取课程管理列表
export const getCoursesPage = (params) =>
  request({
    url: `/cs/courses/page`,
    method: "get",
    params,
  });
// 获取课程管理列表详情
export const getCoursesDetail = (id) =>
  request({
    url: `/cs/courses/baseInfo/${id}?see=0`,
    method: "get",
  });
// 获取课程详情
export const getCourseDetail = (id) =>
  request({
    url: `/cs/courses/baseInfo/${id}?see=1`,
    method: "get",
  });
// 保存课程基本信息
export const baseInfoSave = (params) =>
  request({
    url: `/cs/courses/baseInfo/save`,
    method: "post",
    data: params,
  });
// 获取小节或练习中的题目
export const getCoursesCatalogue = (params) =>
  request({
    url: `/cs/courses/catas/${params.id}?see=0`,
    method: "get",
    params,
  });
// 详情页获取小节或练习中的题目
export const getCourseCatalogue = (params) =>
  request({
    url: `/cs/courses/catas/${params.id}?see=1`,
    method: "get",
    params,
  });
// 获取课程详情页课程目录
export const getcoursesListData = (id) =>
  request({
    url: `/cs/courses/catas/${id}?see=1&withPractice=0`,
    method: "get",
  });
// 获取课程小节内的题目列表
export const getSubjects = (params) =>
request({
  url: `/cs/courses/subjects/get/${params.id}?see=0`,
  method: "get",
  params,
});
// 获取课程目录小节中的题目
export const getSubjectsList = (Id) =>
request({
  url: `/es/questions/listOfBiz?bizId=${id}`,
  method: "get",
});
// 保存题目
export const baseCatalogueSave = (params) =>
  request({
    url: `/cs/courses/catas/save/${params.id}/${params.step}`,
    method: "post",
    data: params.datas,
  });
// 添加阶段测试
export const addTopic = () =>
  request({
    url: `/cs/courses/generator`,
    method: "get"
  });
// 保存课程视频
export const baseVideoSave = (params) =>
  request({
    url: `/cs/courses/media/save/${params.id}`,
    method: "post",
    data: params.datas,
  });
// 保存小节或练习中的题目
export const baseSubjectSave = (params) =>
  request({
    url: `/cs/courses/subjects/save/${params.id}`,
    method: "post",
    data: params.datas,
  });
// 获取老师列表
export const getTeachers = (params) =>
  request({
    url: `/cs/courses/teachers/${params.id}?see=0`,
    method: "get",
    params,
  });
// 获取详情页老师信息
export const getcourseTeachers = (id) =>
  request({
    url: `/cs/courses/teachers/${id}?see=0`,
    method: "get",
  });
    // 获取详情老师信息
    export const getcourseTeacher = (id) =>
    request({
      url: `/cs/courses/teachers/${id}?see=1`,
      method: "get",
    });
// 保存课程老师接口
export const baseTeachersSave = (params) =>
  request({
    url: `/cs/courses/teachers/save`,
    method: "post",
    data: params,
  });
// 课程上架
export const baseUpShelf = (params) =>
  request({
    url: `/cs/courses/upShelf`,
    method: "post",
    data: params,
  });
// 校验课程是否可以上架
export const baseBeforeUpShelf = (id) =>
  request({
    url: `/cs/courses/checkBeforeUpShelf/${id}`,
    method: "get",
  });
// 课程下架
export const baseDownShelf = (params) =>
  request({
    url: `/cs/courses/downShelf`,
    method: "post",
    data: params,
  });
// 课程名称是否存在校验
export const setCoursesName = (params) =>
request({
  url: `/cs/courses/checkName`,
  method: "get",
  params
});