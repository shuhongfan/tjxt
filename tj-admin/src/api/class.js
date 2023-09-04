import request from "@/utils/request.js"

// 课程分类
export const getClassCategorys = (params) =>
	request({
		url: `/cs/categorys/all`,
		method: 'get',
		params
	})
 