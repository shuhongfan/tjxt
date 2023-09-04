import { defineStore } from 'pinia';

export const catchDataesStore = defineStore('notification', {
  state: () => ({
    defaultOpeneds: ['0'], // 左侧导航默认打开
    defaultIndex: '99' // 左侧导航默认选中
  }),
  getters: {
    getDefaultOpeneds: state => {
      const data = localStorage.getItem('openeds');
      return  data ? JSON.parse(data) : state.defaultOpeneds
    },
    getDefaultIndex: state => {
      const data = localStorage.getItem('defaultIndex');
      return data ? data : state.defaultIndex
    },
    getCategoryTree: state => {
      const data = localStorage.getItem('categoryTree');
      return data ?  JSON.parse(data) : ""
    },
    getCategoryMap: state => {
      const data = localStorage.getItem('categoryMap');
      return data ?  JSON.parse(data) : ""
    },
  },
  actions: {
    setDefaultOpeneds(data) {
      this.defaultOpeneds = [data]
      localStorage.setItem('openeds', JSON.stringify(this.defaultOpeneds)) 
    },
    setDefaultIndex(data) {
      this.defaultIndex = data;
      localStorage.setItem('defaultIndex', this.defaultIndex)
    },
    setCategoryTree(data) {
      localStorage.setItem('categoryTree', JSON.stringify(data))
    },
    setCategoryMap(data) {
      localStorage.setItem('categoryMap', JSON.stringify(data))
    },
  },
});
