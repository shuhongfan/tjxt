import request from "@/utils/request.js";
// 获取退款列表
export const getRefundPage = (params) =>
  request({
    url: `/ts/refund-apply/page`,
    method: "get",
    params,
  });
// 获取详情
export const getDetails = (id) =>
  request({
    url: `/ts/refund-apply/${id}`,
    method: "get",
  });
// 退款申请
export const refund = (params) =>
  request({
    url: `/ts/refund-apply`,
    method: "post",
    data:params,
  });
// 退款审批
export const refundApproval = (params) =>
  request({
    url: `/ts/refund-apply/approval`,
    method: "put",
    data:params,
  });
  // 下一个待审批的退款订单
  export const getNextNoApproval = (id) =>
  request({
    url: `/ts/refund-apply/next`,
    method: "get",
  });
