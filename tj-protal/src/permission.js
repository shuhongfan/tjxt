// 权限配置页面
// import { MessagePlugin } from 'tdesign-vue-next';
import NProgress from 'nprogress'; // progress bar
import 'nprogress/nprogress.css'; // progress bar style

import { getPermissionStore, getToken } from '@/store';
import router from '@/router';

const permissionStore = getPermissionStore();
const userStore = getToken();

// 页面加载进度
NProgress.configure({ showSpinner: false });

const { whiteListRouters } = permissionStore;

// 登录状态效验
router.beforeEach(async (to, from, next) => {
  NProgress.start();
  const { token } = userStore;
  
  if (token) {
    if (to.path === '/login') {
      userStore.logout();
      permissionStore.restore();
      next();
      return;
    }
    if (to.path !== '/login'){
      next();
    }
    // token 存在 进入下一页
    // const { roles } = userStore;

    // if (roles && roles.length > 0) {
    //   next();
    // } else {
    //   try {
    //     await userStore.getUserInfo();

    //     const { roles } = userStore;

    //     await permissionStore.initRoutes(roles);

    //     if (router.hasRoute(to.name)) {
    //       next();
    //     } else {
    //       next(`/`);
    //     }
    //   } catch (error) {
    //     // MessagePlugin.error(error);
    //     next(`/login?redirect=${to.path}`);
    //     NProgress.done();
    //   }
    // }
  } else {
    // '无登录信息，跳转到登录页面'
    // console.log('无登录信息，跳转到登录页面');
    // if (whiteListRouters.indexOf(to.path) !== -1) {
    //   next();
    // } else {
    //   next(`/login?redirect=${to.path}`);
    // }
    // NProgress.done();
    // 学成项目 不登录也可以流量 
    next()
  }
});

router.afterEach(() => {
  NProgress.done();
});
