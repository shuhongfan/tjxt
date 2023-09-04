import request from "@/utils/request.js";
// 获取优惠券列表
export const getMarketPage = (params) =>
  request({
    url: `/prs/coupons/page`,
    method: "get",
    params,
  });
// 获取详情
export const getDetails = (id) =>
  request({
    url: `/prs/coupons/${id}`,
    method: "get",
  });
// 新增优惠券
export const saveMarket = (params) =>
  request({
    url: `/prs/coupons`,
    method: "post",
    data: params,
  });
// 修改优惠券
export const updateCoupon = (params) =>
  request({
    url: `/prs/coupons/${params.id}`,
    method: "put",
    data: params,
  });
// 删除优惠券信息
export const deleteMarket = (id) =>
  request({
    url: `/prs/coupons/${id}`,
    method: "delete",
  });
// 优惠券配置暂停
export const configStopGrant = (id) =>
  request({
    url: `/prs/coupons/${id}/pause`,
    method: "put"
  });
// 优惠券配置发放
export const configGrant = (params) =>
  request({
    url: `/prs/coupons/${params.id}/issue`,
    method: "put",
    data: params,
  });

// 获取优惠券列表
export const getCodePage = (params) =>
  request({
    url: `/prs/codes/page`,
    method: "get",
    params,
  });
export const formatRule = (d) => {
  let rule = "";
  let PER_PRICE_DISCOUNT = 1, RATE_DISCOUNT = 2,NO_THRESHOLD = 3, PRICE_DISCOUNT = 4
  switch (d.discountType) {
    case PER_PRICE_DISCOUNT:
      rule = `每满${d.thresholdAmount / 100}元减${d.discountValue / 100}元，不超过${d.maxDiscountAmount / 100}元`;
      break;
    case PRICE_DISCOUNT:
      rule = `满${d.thresholdAmount / 100}元减${d.discountValue / 100}元`;
      break;
    case NO_THRESHOLD:
      rule = `无门槛抵扣${d.discountValue / 100}元`;
      break;
    case RATE_DISCOUNT:
      rule = `满${ d.thresholdAmount / 100}元打${d.discountValue / 10}折，不超过${d.maxDiscountAmount / 100}元`
      break;
  }
  return rule;
}