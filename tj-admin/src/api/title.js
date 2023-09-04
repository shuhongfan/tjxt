import request from "@/utils/request.js";
// 获取课程问题列表
export const getSubjectPage = (params) =>
  request({
    url: `/es/questions/page`,
    method: "get",
    params,
  });
// 获取问题详情
export const getDetails = (id) =>
  request({
    url: `/es/questions/${id}`,
    method: "get",
  });
// 新增问题
export const addSubject = (params) =>
  request({
    url: `/es/questions`,
    method: "post",
    data:params,
  });
// 编辑问题
export const editSubject = (data) =>
  request({
    url: `/es/questions/${data.id}`,
    method: "put",
    data,
  });
// 删除问题
export const deleteTitle = (id) =>
  request({
    url: `/es/questions/${id}`,
    method: "delete",
  });
// 题目名称是否存在校验
export const checkName = (params) =>
  request({
    url: `/es/questions/checkName`,
    method: "get",
    params,
  });
