import request from "@/utils/request.js";
// 获取看板数据
export const getGrantInfo = (params) =>
  request({
    url: `/ds/data/board`,
    method: "get",
    params
  });
// 获取top10数据
export const getTop10 = () =>
request({
  url: `/ds/data/top10`,
  method: "get",
});
// 获取今日数据
export const getToday = () =>
request({
  url: `/ds/data/today`,
  method: "get",
});