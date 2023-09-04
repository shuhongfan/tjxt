import request from "@/utils/request.js";
// 获取课程分类列表
export const getTypeAll = (params) =>
  request({
    url: `/cs/categorys/all`,
    method: "get",
    params,
  });
// 获取课程分类列表
export const getCoursesAll = (params) =>
  request({
    url: `/cs/courses/simpleInfo/list`,
    method: "get",
    params,
  });