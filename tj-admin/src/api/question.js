import request from "@/utils/request.js"
// 获取问答列表
export const getQuestionPage = (params) =>
  request({
    url: `/ls/admin/questions/page`,
    method: "get",
    params,
  })
// 获取详情
export const getQuestionsDetails = (id) =>
  request({
    url: `/ls/admin/questions/${id}`,
    method: "get",
  })
// 获取回答详情
export const getAnswersDetails = (id) =>
  request({
    url: `/ls/admin/replies/${id}`,
    method: "get",
  })
// 问答回复
export const saveQuestionsReply = (params) =>
  request({
    url: `/ls/replies`,
    method: "post",
    data: params
  })
// 根据问题id分页查询答案列表
export const getReplies = (params) =>
  request({
    url: `/ls/admin/replies/page`,
    method: "get",
    params,
  })
// 设置隐藏或显示问题
export const setQuestionsFolded = (params) =>
  request({
    url: `/ls/admin/questions/${params.id}/hidden/${params.hidden}`,
    method: "put"
  })
// 设置隐藏或显示回答
export const setAnswersFolded = (params) =>
  request({
    url: `/ls/admin/replies/${params.id}/hidden/${params.hidden}`,
    method: "put"
  })
// 设置点赞或者取消点赞
export const setLiked = (data) =>
  request({
    url: `/ls/likes`,
    method: "post",
    data,
  })
// 获取笔记列表
export const getNotesPage = (params) =>
  request({
    url: `/ls/admin/notes/page`,
    method: "get",
    params,
  })
// 获取详情
export const getNoteDetails = (id) =>
  request({
    url: `/ls/admin/notes/${id}`,
    method: "get",
  })
// 显示指定笔记
export const setNotesShow = (id) =>
  request({
    url: `/ls/admin/notes/${id}/hidden/false`,
    method: "put"
  })
// 隐藏指定笔记
export const setNotesFolded = (id) =>
  request({
    url: `/ls/admin/notes/${id}/hidden/true`,
    method: "put"
  })