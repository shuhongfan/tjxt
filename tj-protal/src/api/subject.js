import request from "@/utils/request.js"
// 联系、考试等考题相关接口
const EXAM_API_PREFIX = "/es"
 // 根据小节或测试id获取练习
export const getSubject = (data) =>
request({
	url: `${EXAM_API_PREFIX}/exams`,
	method: 'POST',
	data
})
// 提交考试答案，考试或测试提交时需要保存答案信息
export const postSubject = (data) =>
request({
	url: `${EXAM_API_PREFIX}/exams/details`,
	method: 'post',
	data
})
// 新增考试记录，考试或测试开始时需要保存基本信息，返回记录id
/*
export const startExamination = (data) =>
request({
	url: `${EXAM_API_PREFIX}/exam-records`,
	method: 'post',
	data
})
*/
