// 路由配置 页面
import { useRoute, createRouter, createWebHashHistory } from 'vue-router';

import baseRouters from './modules/base';
import componentsRouters from './modules/components';
import othersRouters from './modules/others';

// 关于单层路由，meta 中设置 { single: true } 即可为单层路由，{ hidden: true } 即可在侧边栏隐藏该路由

// 存放动态路由
export const asyncRouterList = [...baseRouters, ...componentsRouters, ...othersRouters];

// 存放固定的路由
const defaultRouterList = [
  {
    path: '/login',
    name: 'login',
    component: () => import('@/pages/login/index.vue'),
  },
  {
    path: '/',
    redirect: '/main/index',
    component: () => import('@/pages/layouts/index.vue'),
  },
  {
    path: '/:w+',
    name: '404Page',
    redirect: '/result/404',
  },
];

export const allRoutes = [...defaultRouterList, ...asyncRouterList];

export const getActive = (maxLevel = 2) => {
  const route = useRoute();
  if (!route.path) {
    return '';
  }
  return route.path
    .split('/')
    .filter((_item, index) => index <= maxLevel && index > 0)
    .map((item) => `/${item}`)
    .join('');
};

const router = createRouter({
  history: createWebHashHistory(),
  routes: allRoutes,
  scrollBehavior() {
    return {
      el: '#app',
      top: 0,
      behavior: 'smooth',
    };
  },
});

export default router;
