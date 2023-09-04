import request from "@/utils/request.js";
// 获取员工列表
export const getStaffs = (params) =>
  request({
    url: `/us/staffs/page`,
    method: "get",
    params,
  });

 // 查询角色列表
 export const getRoleList = () =>
 request({
   url: `/as/roles`,
   method: "get",
 });
//  校验手机号是否存在
export const checkPhone = (params) =>
request({
  url: `/us/users/checkCellphone`,
  method: "get",
  params,
});
// 校验密码是否与原密码一致
export const CheckPassword = (id) =>
request({
  url: `/us/users/checkPasswd/${id}`,
  method: "get",
});