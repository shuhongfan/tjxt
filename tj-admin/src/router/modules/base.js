import Layout from "@/pages/layouts/index.vue";
import HomeIcon from "@/assets/icon_home.svg";

export default [
  {
    path: "/main",
    component: Layout,
    redirect: "/main/index",
    name: "main",
    meta: { title: "工作台", icon:'&#xe60c;', hidden: true},
    children: [
      {
        path: "index",
        name: "index",
        component: () => import("@/pages/main/index.vue"),
        meta: { title: "首页", hidden: false },
      },
    ],
  },
];
