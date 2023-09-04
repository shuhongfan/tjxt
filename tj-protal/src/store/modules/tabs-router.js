import { defineStore } from 'pinia';
import { store } from '@/store';

const homeRoute = [
  {
    path: '/dashboard/base',
    routeIdx: 0,
    title: '仪表盘',
    name: 'DashboardBase',
    isHome: true,
  },
];

const state = {
  tabRouterList: homeRoute,
  isRefreshing: false,
};

// 不需要做多标签tabs页缓存的列表 值为每个页面对应的name 如 DashboardDetail
// const ignoreCacheRoutes = ['DashboardDetail'];
const ignoreCacheRoutes = [];

export const useTabsRouterStore = defineStore('tabsRouter', {
  state: () => state,
  getters: {
    tabRouters: (state) => state.tabRouterList,
    refreshing: (state) => state.isRefreshing,
  },
  actions: {
    // 处理刷新
    toggleTabRouterAlive(routeIdx) {
      this.isRefreshing = !this.isRefreshing;
      this.tabRouters[routeIdx].isAlive = !this.tabRouters[routeIdx].isAlive;
    },
    // 处理新增
    appendTabRouterList(newRoute) {
      const needAlive = !ignoreCacheRoutes.includes(newRoute.name);
      if (!this.tabRouters.find((route) => route.path === newRoute.path)) {
        // eslint-disable-next-line no-param-reassign
        this.tabRouterList = this.tabRouterList.concat({ ...newRoute, isAlive: needAlive });
      }
    },
    // 处理关闭当前
    subtractCurrentTabRouter(newRoute) {
      const { routeIdx } = newRoute;
      this.tabRouterList = this.tabRouterList.slice(0, routeIdx).concat(this.tabRouterList.slice(routeIdx + 1));
    },
    // 处理关闭右侧
    subtractTabRouterBehind(newRoute) {
      const { routeIdx } = newRoute;
      this.tabRouterList = this.tabRouterList.slice(0, routeIdx + 1);
    },
    // 处理关闭左侧
    subtractTabRouterAhead(newRoute) {
      const { routeIdx } = newRoute;
      this.tabRouterList = homeRoute.concat(this.tabRouterList.slice(routeIdx));
    },
    // 处理关闭其他
    subtractTabRouterOther(newRoute) {
      const { routeIdx } = newRoute;
      this.tabRouterList = homeRoute.concat([this.tabRouterList?.[routeIdx]]);
    },
    removeTabRouterList() {
      this.tabRouterList = [];
    },
    initTabRouterList(newRoutes) {
      newRoutes?.forEach((route) => this.appendTabRouterList(route));
    },
  },
});

export function getTabsRouterStore() {
  return useTabsRouterStore(store);
}
