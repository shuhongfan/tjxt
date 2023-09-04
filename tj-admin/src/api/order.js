import request from "@/utils/request.js";
// 获取子订单列表
export const getOrderPage = (params) =>
  request({
    url: `/ts/order-details/page`,
    method: "get",
    params,
  });
// 获取详情
export const getDetails = (id) =>
  request({
    url: `/ts/order-details/${id}`,
    method: "get",
  });
// 新增优惠券
export const addCoupon = (params) =>
  request({
    url: `/ps/coupon`,
    method: "post",
    data: params,
  });
// 编辑优惠券
export const editCoupon = (params) =>
  request({
    url: `/ps/coupon/{id}`,
    method: "put",
    data: params,
  });