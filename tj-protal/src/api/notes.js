import request from "@/utils/request.js"
// 笔记相关接口
const LEARNING_API_PREFIX = "/ls"
// 新增笔记
export const addNotes = params =>
	request({
		url: `${LEARNING_API_PREFIX}/notes`,
		method: 'post',
		data:params,
	})
// 全部笔记
export const getAllNotes = params =>
	request({
		url: `${LEARNING_API_PREFIX}/notes/page`,
		method: 'get',
		params,
	})
// 采集笔记
export const collectionNotes = id =>
	request({
		url: `${LEARNING_API_PREFIX}/notes/${id}`,
		method: 'post'
	})    
// 更新笔记
export const updateNotes = data =>
	request({
		url: `${LEARNING_API_PREFIX}/notes/${data.id}`,
		method: 'put',
		data
	})      
	
// 删除笔记
export const delNote = (id) =>
request({
	url: `${LEARNING_API_PREFIX}/notes/${id}`,
	method: 'delete',
})	
// 采集笔记
export const notesGathers = (id) =>
request({
	url: `${LEARNING_API_PREFIX}/notes/gathers/${id}`,
	method: 'post',
})	
// 取消采集笔记
export const unNotesGathers = (id) =>
request({
	url: `${LEARNING_API_PREFIX}/notes/gathers/${id}`,
	method: 'delete',
})	
// 笔记点赞
export const likeed = (id) =>
request({
	url: `${LEARNING_API_PREFIX}/note/${id}`,
	method: 'post',
})	
// 取消笔记点赞
export const unLikeed = (id) =>
request({
	url: `${LEARNING_API_PREFIX}/note/${id}`,
	method: 'delete',
})	