import { createPinia } from 'pinia';
import {usePersist} from "pinia-use-persist";

const store = createPinia();

// // 数据持久化
store.use(usePersist);
export { store };

export * from './modules/dataCache';
export * from './modules/permission';
export * from './modules/user';
export * from './modules/tabs-router';


export default store;