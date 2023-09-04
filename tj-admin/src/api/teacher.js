import request from "@/utils/request.js";
// 获取列表
export const getTeacherser = (params) =>
  request({
    url: `/us/teachers/page`,
    method: "get",
    params,
  });