import { defineStore } from "pinia";
import { TOKEN_NAME, USER_KEY } from "@/config/global";
import store from "@/store";

const InitUserInfo = {};

export const useUserStore = defineStore("user", {
  id: "userInfo",
  state: () => ({
    token: sessionStorage.getItem(TOKEN_NAME), // 使用 || 'main_token' 默认token不走权限
    adminUserInfo: JSON.parse(sessionStorage.getItem(USER_KEY)),
    active: localStorage.getItem("active"),
    stepNumber: JSON.parse(localStorage.getItem("step")),
    tabNumber:localStorage.getItem("tabNumber"),
  }),
  persist: {
    enabled: true,
    strategies: [
      { storage: localStorage, paths: ["active", "step","tabNumber"] },
      { storage: sessionStorage, paths: [TOKEN_NAME, USER_KEY] },
    ],
  },
  getters: {
    // 获取token
    getToken: (state) => {
      return state.token;
    },
    // 获取用户信息
    getUserInfo: (state) => {
      return state.adminUserInfo;
    },
    // 获取课程管理步骤条的当前状态
    getStepActive: (state) => {
      return state.active;
    },
    // 获取课程管理步骤条的可以触发的状态
    getStepNum: (state) => {
      return state.stepNumber;
    },
    // 获取课程管理tab切换值
    getTabNumber: (state) => {
      return state.tabNumber;
    },
  },
  actions: {
    // 记录用户token
    async setToken(token) {
      this.token = token;
      sessionStorage.setItem(TOKEN_NAME, token);
    },
    // 记录登录时获取的用户信息
    async setUserInfo(userInfo) {
      this.adminUserInfo = userInfo;
      sessionStorage.setItem(USER_KEY, JSON.stringify(userInfo));
    },
    async logout() {
      sessionStorage.removeItem(TOKEN_NAME);
      sessionStorage.removeItem(USER_KEY);
      localStorage.removeItem("tabNumber");
      this.token = "";
      this.adminUserInfo = InitUserInfo;
    },
    async removeToken() {
      this.token = "";
    },
    //设置课程管理步骤条
    async setStepActive(active) {
      this.active = active;
      localStorage.setItem("active", active);
    },
    // 设置课程管理步骤条的可以触发的状态
    async setStepNum(step) {
      this.stepNumber = step;
      localStorage.setItem("step", step);
    },
    // 设置课程管理tab切换值
    async setTabNumber(tab) {
      this.tabNumber = tab;
      localStorage.setItem("tabNumber", tab);
    },
  },
});

export function getToken() {
  return useUserStore(store);
}
