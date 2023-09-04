import { createApp } from "vue";
// 组件库
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";
import '@/style/font-style/iconfont.css';

import App from "./App.vue";
// 全局样式
import "@/style/index.scss";
// 权限-路由拦截
import "./permission";
// 数据存储、路由
import { store } from "./store";
import router from "./router";
// 防止用户频繁点击按钮
import preventReClick from "@/utils/preventRepeatClick";
const app = createApp(App);
app.use(ElementPlus);
app.use(store);
app.use(router);
app.use(preventReClick);
app.mount("#app");
