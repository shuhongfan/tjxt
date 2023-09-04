import request from "@/utils/request.js"
const SEARCH_API_PREFIX = "/ss"
const COURSE_API_PREFIX = "/cs"
const LEARNING_API_PREFIX = "/ls"
const MEDIA_API_PREFIX = "/ms"
const PROMOTION_API_PREFIX = "/prs"
const EXAM_API_PREFIX = "/es"
// 课程分类
export const getClassCategorys = (params) =>
	request({
		url: `${COURSE_API_PREFIX}/categorys/all`,
		method: 'get',
		params
	})

// 获取课程推荐接口
export const getRecommendClassList = (type) =>
	request({
		url: `${SEARCH_API_PREFIX}/recommend/${type}`,
		method: 'get'
	})

// 获取课程列表 - 分类id 查询对应的列表 （倒序 十条）
export const getClassList = (id) =>
	request({
		url: `${SEARCH_API_PREFIX}/interests/${id}/courses`,
		method: 'get'
	})

// 获取学习计划 getLearningPlan	
export const getLearningPlan = (id) =>
	request({
		url: `${LEARNING_API_PREFIX}/plans`,
		method: 'get'
	})

// 查询当前用户学习的指定课程信息，返回null则代表没有购买	
export const getCourseLearning = (courseId) =>
request({
	url: `${LEARNING_API_PREFIX}/lessons/${courseId}`,
	method: 'get'
})	
// 课程搜索
export const classSeach = (params) =>
	request({
		url: `${SEARCH_API_PREFIX}/courses/portal`,
		method: 'get',
		params
	})


// 学习相关接口
export const getLearningClassDetails = (id) =>
	request({
		url: `${COURSE_API_PREFIX}/courses/${id}/catalogs`,
		method: 'get'
	})



// 兴趣接口

// 新增兴趣爱好
export const setInterests = (params) =>
	request({
		url: `${SEARCH_API_PREFIX}/interests`,
		method: 'post',
		data:params,
		params
	})	
// 查询我的兴趣爱好
export const getInterests = (params) =>
	request({
		url: `${SEARCH_API_PREFIX}/interests`,
		method: 'get',
		params
	})		

// 获取播放视频的授权签名
export const getMediasSignature = (params) =>
	request({
		url: `${MEDIA_API_PREFIX}/medias/signature/play`,
		method: 'get',
		params
	})	
// 优惠券 相关接口
// 格式化规则
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
// 可领优惠券（超值优惠券）
export const getCollectableCoupon = (params) =>
request({
	url: `${PROMOTION_API_PREFIX}/coupons/list`,
	method: 'get',
	params
})		
// 我的优惠券（近一年）
export const getMyCoupon = (params) =>
request({
	url: `${PROMOTION_API_PREFIX}/user-coupons/page`,
	method: 'get',
	params
})	
// 优惠券领取
export const getCoupon = (params) =>
request({
	url: `${PROMOTION_API_PREFIX}/user-coupons/${params.id}/receive`,
	method: 'post'
})	
// 兑换码兑换优惠券
export const exchangeCoupon = (data) =>
request({
	url: `${PROMOTION_API_PREFIX}/user-coupons/${data.code}/exchange`,
	method: 'post'
})	

// 课程表管理接口

// 查询我的课程表
export const getMylessons = () =>
request({
	url: `${LEARNING_API_PREFIX}/lessons/page`,
	method: 'get',
})	
// 查询我正在学习的课程
export const getMyLearning = () =>
request({
	url: `${LEARNING_API_PREFIX}/lessons/now`,
	method: 'get',
})

// 查询我的学习计划
export const getMyPlan = () =>
request({
	url: `${LEARNING_API_PREFIX}/lessons/plans`,
	method: 'get',
})
/*
// 重新学习课程
export const restartMyLesson = (courseId) =>
	request({
		url: `${LEARNING_API_PREFIX}/lessons/${courseId}/restart`,
		method: 'PUT',
	})*/

// 报名免费课程
export const signUp = (courseId) =>
request({
	url: `/ts/orders/freeCourse/${courseId}`,
	method: 'psot',
})

// 将指定课程从课程表移除
export const delMyClass = (courseId) =>
request({
	url: `${LEARNING_API_PREFIX}/lessons/${courseId}`,
	method: 'delete',
})

// 创建学习计划
export const creatPlans = (params) =>
request({
	url: `${LEARNING_API_PREFIX}/lessons/plans`,
	method: 'post',
	data: params
})

// 考试相关
// 分页查询我的考试记录
export const getExamList = (params) =>
request({
	url: `${EXAM_API_PREFIX}/exams/page`,
	method: 'get',
	params
})
// 查询我的考试记录详情
export const getExamDetails = (id) =>
request({
	url: `${EXAM_API_PREFIX}/exams/${id}`,
	method: 'get',
})
// 提交考试答案，考试或测试提交时需要保存答案信息
export const submitExamRecords = (params) =>
request({
	url: `${EXAM_API_PREFIX}/exam-records/details`,
	method: 'post',
	data:params
})
// 新增考试记录，考试或测试开始时需要保存基本信息，返回记录id
export const addExamRecords = (params) =>
request({
	url: `${EXAM_API_PREFIX}/exam-records`,
	method: 'post',
	data:params
})

// 课程表的信息

// 查询我的某个课程表的学习记录
export const getLearningLog = (lessonId) =>
request({
	url: `${LEARNING_API_PREFIX}/learning-records/lessons/${lessonId}`,
	method: 'get'
})

// 新增学习记录，在学习某小节时提交
export const addPlayLog = (params) =>
request({
	url: `${LEARNING_API_PREFIX}/learning-records`,
	method: 'post',
	data:params
})



/**  积分相关的接口 **/

// 签到打卡功能，返回本次签到的积分值
export const pointsSign = (params) =>
request({
	url: `${LEARNING_API_PREFIX}/sign-records`,
	method: 'post',
	data:params
})
// 获取签到记录
export const getSignRecords = () =>
request({
	url: `${LEARNING_API_PREFIX}/sign-records`,
	method: 'get',
})
// 获取签到记录
export const getTodayPoints = () =>
request({
	url: `${LEARNING_API_PREFIX}/points/today`,
	method: 'get',
})
// 查询指定赛季信息
export const getSeasons = (params) =>
request({
	url: `${LEARNING_API_PREFIX}/boards`,
	method: 'get',
	params
})
// 查询赛季信息列表
export const getSelectOptions = (params) =>
request({
	url: `${LEARNING_API_PREFIX}/boards/seasons/list`,
	method: 'get',
	params
})

