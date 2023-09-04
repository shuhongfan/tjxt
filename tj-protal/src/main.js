import { createApp } from 'vue'
// 组件库
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import 'swiper/scss';
import 'swiper/scss/pagination';
import App from './App.vue'
import '@/style/font_style/iconfont.css';
// 全局样式
import '@/style/index.scss';
// 权限-路由拦截
import './permission';
// 数据存储、路由
import { store } from './store';
import router from './router';

const app = createApp(App);

app.use(ElementPlus);
app.use(store);
app.use(router);

app.mount('#app')
