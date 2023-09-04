import request from "@/utils/request.js"
/** 订单相关接口 **/ 
const TRADE_API_PREFIX = "/ts"
// 订单列表 - 分页列表
export const getOrderListes = (params) =>
request({
	url: `${TRADE_API_PREFIX}/orders/page`,
	method: 'get',
	params
})
// 查询订单明细
export const getOrderDetails = (id) =>
request({
	url: `${TRADE_API_PREFIX}/orders/${id}`,
	method: 'get'
})
// 当前学生的订单分页数据
export const getOrderPages = (params) =>
request({
	url: `${TRADE_API_PREFIX}/orders/page`,
	method: 'get',
	params
})
// 订单结算页，查询订单可用优惠券、订单id信息
export const confirmOrderInfo = (params) =>
	request({
		url: `${TRADE_API_PREFIX}/orders/prePlaceOrder`,
		method: 'get',
		params
	})

// 下单
export const setOrder = (params) =>
	request({
		url: `${TRADE_API_PREFIX}/orders/placeOrder`,
		method: 'post',
		data:params
	})

// 报名免费课程
export const enrolledFreeCourse = (id) =>
	request({
		url: `${TRADE_API_PREFIX}/orders/freeCourse/${id}`,
		method: 'post'
	})

// 订单支付取消
export const cancelOrder = (id) =>
	request({
		url: `${TRADE_API_PREFIX}/orders/${id}/cancel`,
		method: 'put'
	})
// 删除订单
export const delOrder = (id) =>
	request({
		url: `${TRADE_API_PREFIX}/orders/${id}`,
		method: 'delete'
	})


/** 购物车相关接口 **/ 	

// 将课程加入购物车
export const putCarts = (data) =>
	request({
		url: `${TRADE_API_PREFIX}/carts`,
		method: 'post',
		data
	})	

// 获取购物车中的课程
export const getCarts = (data) =>
	request({
		url: `${TRADE_API_PREFIX}/carts`,
		method: 'get',
		data
	})		

// 批量将课程从购物车中删除
export const delCarts = (data) =>
	request({
		url: `${TRADE_API_PREFIX}/carts?ids=${data.join(',')}`,
		method: 'delete'
	})		


/** 支付相关接口 **/ 	

// 支付渠道列表
export const getPayMethod = (params) =>
	request({
		url: `${TRADE_API_PREFIX}/pay/channels`,
		method: 'get',
		params
	})	
// 支付申请,返回支付二维码url
export const getPayUrl = (data) =>
	request({
		url: `${TRADE_API_PREFIX}/pay/order`,
		method: 'post',
		data
	})
// 获取支付信息,包含支付状态和支付超时时间
export const getPayState = (params) =>
	request({
		url: `${TRADE_API_PREFIX}/orders/${params.orderId}/status`,
		method: 'get',
		params
	})			

/** 退款相关接口 **/ 	

// 退款申请
export const refundsApply = (data) =>
	request({
		url: `${TRADE_API_PREFIX}/refund-apply`,
		method: 'post',
		data
	})		

// 退款详情
export const refundsDetails = (id) =>
	request({
		url: `${TRADE_API_PREFIX}/refund-apply/detail/${id}`,
		method: 'get'
	})		