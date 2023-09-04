import { createPinia } from 'pinia';
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

const store = createPinia();

// // 数据持久化
store.use(piniaPluginPersistedstate);
export { store };

export * from './modules/catchDataes';
export * from './modules/permission';
export * from './modules/user';
export * from './modules/tabs-router';


export default store;