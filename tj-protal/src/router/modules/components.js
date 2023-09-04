import Layout from '@/pages/layouts/index.vue';

export default [
  {
    path: '/ask',
    name: 'ask',
    component: Layout,
    redirect: '/ask/index',
    meta: { title: '发布、编辑问答'},
    children: [
      {
        path: 'index',
        name: 'ask',
        component: () => import('@/pages/ask/index.vue'),
        meta: { title: '发布问题' },
      }
    ],
  },
  {
    path: '/result',
    name: 'result',
    component: Layout,
    redirect: '/result/success',
    meta: { title: '结果页', icon: 'check-circle' },
    children: [
      {
        path: 'success',
        name: 'ResultSuccess',
        component: () => import('@/pages/result/success/index.vue'),
        meta: { title: '成功页' },
      },
      {
        path: '404',
        name: 'Result404',
        component: () => import('@/pages/result/404/index.vue'),
        meta: { title: '访问页面不存在页' },
      },
    ],
  },
];
