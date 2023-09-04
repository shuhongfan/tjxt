<!-- 课程搜索页面 -->
<template>
  <div class="classSearchWrapper">
    <div class="container searchBar">
      <!-- 结果统计 -->
      <div class="result" v-if="isLogin() && isShow && searchParams.keyword">
        <span class="searchKey">关键词：<em>{{searchParams.keyword}}</em> <i class="close iconfont zhy-btn_qingchu1" @click="clearSearchKey"></i></span> 共找到 <em> {{count}} </em> 门“ <em> {{searchParams.keyword}} </em> ”相关课程
      </div>
      <!-- 筛选条件 -->
      <div class="title">全部课程</div>
      <SearchKey :data="searchType" @searchKey="searchKey" :active="activeId" :key="activeId"></SearchKey>
      <SearchKey :data="searchCost" @searchKey="searchKey"></SearchKey>
    </div>
    <div class="searchContain bg-wt">
      <div class="container">
        <!-- 排序及分页 -->
        <div class="fx-sb marg-bt-20">
          <SortBar :data="soleBar" @sortHandle="sortHandle"></SortBar>
          <div class="pageAction fx" v-if="count > 0" >
            <img src="@/assets/page_act.png" class="iconTurn" v-if="page > 1" @click="pagesHandle('reduce')" alt="" />
            <img src="@/assets/page_act_nor.png"  v-if="page == 1" alt="" />
            <span v-if="count > 0"><em>{{page}}</em> / {{Math.ceil(count/searchParams.pageSize) }}</span>
            <img src="@/assets/page_act.png" v-if="page < Math.ceil(count/searchParams.pageSize)" @click="pagesHandle('add')" alt="" />
            <img src="@/assets/page_act_nor.png" class="iconTurn" v-if="page == Math.ceil(count/searchParams.pageSize)" alt="" />
          </div>
        </div>
        <!-- 搜索课程列表 -->
        <div class="content fx-wp" v-if="count > 0">
           <ClassCards type="search" class="items marg-bt-20" v-for="(item, index) in searchResultData" :data="item" :key="index"></ClassCards>
        </div>
        <div class="content fx-ct noData" v-else>
          搜索结果为空！
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
/** 数据导入 **/

import { onBeforeMount, onUnmounted, onMounted, ref, watchEffect } from "vue";
import { ElMessage } from "element-plus";
import { getClassCategorys, classSeach } from "@/api/class.js";
// 组件导入
import SearchKey from './components/SearchKey.vue'
import SortBar from './components/SortBar.vue'
import ClassCards from '@/components/ClassCards.vue'
import { useRoute } from "vue-router";
import {isLogin, dataCacheStore} from '@/store'
const dataCache = dataCacheStore();
const searchType = ref({subKey: 'categoryIdLv1', title: "课程分类", searchKeys:[{id:'all', name: '全部'},  ...dataCache.getCourseClassDataes]});
const searchCost = ref({subKey: 'free', title: "收付费", searchKeys:[{id:'all', name: '全部'},{id: '1', name: '免费'},{id: '0', name: '付费'}]});    
const soleBar = ref([{key:'推荐', value:'all'},{key:'最新', value:'publishTime'}, {key:'最热', value:'sold'}])
const searchParams = ref({  // 搜索参数定义
  keyword: '',
  categoryIdLv1: '',
  categoryIdLv2: '',
  categoryIdLv3: '',
  pageNo: 1,
  pageSize: 12
})
// 结果 - 课程列表
const searchResultData = ref([])
const count = ref(0)
const page = ref(1);
const route = useRoute()
// 是否展示关键词及结果
const isShow = ref(true)
const fullPath = ref(route.fullPath)
// 课程分类默认状态
const activeId = ref('all')
// mounted生命周期
onMounted(() => {
  // 搜索
  searchParams.value.keyword = dataCache.getSearchKey
  if (route.query.type){
    searchParams.value[route.query.type] = route.query.id
  }
  getClassCategoryData();
  search()
});

// 组件卸载的时候触发 - 清空搜索结果
onUnmounted(() => {
  dataCache.setSearchKey('')
})

watchEffect(() => {
  if(dataCache.getSearchKey != ''){
    isShow.value = true
  }
  // 点击导航分类更新 url 触发这里的逻辑  点击全部课程下的分类不触发这里的逻辑
  if (route.fullPath != fullPath.value){
    fullPath.value = route.fullPath
    if (route.query.type && route.query.type == 'categoryIdLv1'){
      searchParams.value.categoryIdLv2 = route.query.id
    }
    searchParams.value.categoryIdLv2 = route.query.type && route.query.type == 'categoryIdLv2' ? route.query.id : undefined
    searchParams.value.categoryIdLv3 = route.query.type && route.query.type == 'categoryIdLv3' ? route.query.id : undefined
    activeId.value = route.query.id
    search()
  }
})

/** 方法定义 **/

// 获取一、二、三级分类信息
const getClassCategoryData = async () => {
  await getClassCategorys()
    .then((res) => {
      if (res.code === 200) {
        searchType.value.searchKeys = [{id:'all', name: '全部'}, ...res.data]
      } else {
        ElMessage({
          message:res.data.msg,
          type: 'error'
        });
      }
    })
    .catch(() => {
      ElMessage({
        message: "分类请求出错！",
        type: 'error'
      });
    });
};
// 清空搜索关键词
const clearSearchKey = () => {
  searchParams.value.keyword = ""
  isShow.value = false
  dataCache.setSearchKey('')
  initPage()
  search()
}

// 搜索组件对应的事件
async function searchKey(item){
  if(!isLogin()){
    isShow.value = false
    dataCache.setSearchKey('')
  } 
  searchParams.value.categoryIdLv1 = ""
  searchParams.value.categoryIdLv2 = ""
  searchParams.value.categoryIdLv3 = ""
  if (item.key == 'free') {
    searchParams.value[item.key] = item.value != 'all' ? Boolean(+item.value) : undefined;
  } 
  if (item.key == 'categoryIdLv1') {
    searchParams.value[item.key] = item.value != 'all' ? item.value : undefined;
  }
  initPage()
  search()
}
// 排序操作
const sortHandle = (item) => {
  searchParams.value.sortBy = item != 'all' ? item : undefined;
  initPage()
  search()
}
// 初始化分页页码
const initPage = () => {
  page.value = 1
  searchParams.value.pageNo = page.value
}
// 分页操作
const pagesHandle = (item) => {
  item == 'add' ? page.value ++ : page.value --;
  searchParams.value.pageNo = page.value
  search()
}
// 搜索
async function search (){
  // 将不存在的参数干掉
  const params  = JSON.stringify({...searchParams.value})
  await classSeach(JSON.parse(params))
  .then((res) => {
      if (res.code == 200) {
        searchResultData.value = res.data.list
        count.value =  Number(res.data.total)
      } else {
        ElMessage({
          message:res.data.msg,
          type: 'error'
        });
      }
    })
    .catch(() => {
      ElMessage({
        message: "分类请求出错！",
        type: 'error'
      });
    });
}
// 监听搜索关键词
watchEffect(() => {
  if (searchParams.value.keyword != dataCache.getSearchKey){
    searchParams.value.keyword = dataCache.getSearchKey
    initPage()
    search()
  }
})
</script>
<style lang="scss" src="./index.scss"> </style>
