import request from "@/utils/request.js";
// 获取学生列表
export const getStudents = (params) =>
  request({
    url: `/us/students/page`,
    method: "get",
    params,
  });

  // 修改学生密码
export const editStudentsPassword = (params) =>
  request({
    url: `/us/students/password`,
    method: "put",
    data:params,
  });
