import { defineStore } from 'pinia';

export const dataCacheStore = defineStore('notification', {
  state: () => ({
    courseClassDataes: [], // 存储课程分类
    searchKey: '', // 搜索关键词存储
    askDetails: '22',
    learingDataes:{ // 学习相关信息存储 
      classDetailsData:{}, //课程详情的信息 - 打开课程详情时写入 
      teacherData:{}, // 讲师信息 - 打开课程详情时写入
      classSectionMap:{},// 小节信息
      classCatalogs:{}, // 目录信息
      planData:{} // 课程计划信息 
    },
    currentPlayData:{}, // 视频的当前播放数据存储
    orderClassInfo:{}, //订单的课程信息
    currentCourseInfo:{}, // 当前浏览的课程信息
    myLearnClassInfo:{}, // 当前学习课程的信息
  }),
  getters: {
    // 获取对应的state的值
    getAskDetails: state => state.askDetails,
    getSearchKey: state => state.searchKey,
    getLearingDataes: state => state.learingDataes,
    getCurrentPlayData: state => state.currentPlayData,
    getOrderClassInfo: state => state.orderClassInfo,
    getCourseClassDataes: state => state.courseClassDataes,
    getCurrentCourseInfo: state => state.currentCourseInfo,
    getMyLearnClassInfo: state => state.myLearnClassInfo
  },
  actions: {
    setCurrentCourseInfo(data){
      this.currentCourseInfo = data;
    },
    // 设置state对应的值
    setAskDetails(data) {
      this.askDetails = data;
    },
    setSearchKey(data) {
      this.searchKey = data;
    },
    setLearingDataes(data) {
      this.learingDataes = data;
    },
    setCurrentPlayData(data) {
      this.currentPlayData = data;
    },
    setOrderClassInfo(data){
      this.orderClassInfo = data;
    },
    setCourseClassDataes(data){
      this.courseClassDataes = data;
    },
    setMyLearnClassInfo(data){
      this.myLearnClassInfo = data;
    },
  },
  persist: {
    enabled: true,
    encryptionKey: 'data-catch',
  }
});
