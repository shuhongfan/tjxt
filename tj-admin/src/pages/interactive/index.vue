<!-- 问答管理列表-->
<template>
  <div class="contentBox">
    <!-- 搜索 -->
    <Search
      :searchData="searchData"
      ref="search"
      @getTime="getTime"
      @handleSearch="handleSearch"
      @getList="getList"
      @handleReset="handleReset"
    ></Search>
    <!-- end -->
    <div class="bg-wt radius marg-tp-20">
      <div class="tableBox">
        <!-- 表格数据 -->
        <TableList
          :baseData="baseData.value"
          :total="total"
          :loading="loading"
          :pageSize="searchData.pageSize"  
          :isSearch="isSearch"        
          @getList="getList"
          @handleSizeChange="handleSizeChange"
          @handleCurrentChange="handleCurrentChange"
        ></TableList>
        <!-- end -->
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from "vue";
import { useRouter } from "vue-router";

// 接口api
import { getQuestionPage } from "@/api/question";
// 导入组件
// 搜索
import Search from "./components/Search.vue";
// 表格
import TableList from "./components/TableList.vue";
// ------定义变量------
//初始化路由
const router = useRouter();
const search = ref(); //定义搜索ref
const loading = ref(false); //加载数据
let total = ref(null); //数据总条数
let searchData = reactive({
  pageSize: 10,
  pageNo: 1,
  // sortBy: "updateTime",
  // isAsc: false,
}); //搜索对象
let baseData = reactive([]); //表格数据
let isSearch = ref(false);//是否触发了搜索按钮,用来控制没有搜索出数据和正常列表无数据的区分，显示的图片和提示语不一样
// ------生命周期------
onMounted(() => {
  init();
});
// ------定义方法------
// 获取初始值
const init = () => {
  getList();
};
// 获取列表数据
const getList = async () => {
  loading.value = true;
  await getQuestionPage(searchData)
    .then((res) => {
      if (res.code === 200) {
        loading.value = false;
        baseData.value = res.data.list;
        total.value = res.data.total;       
      }
    })
    .catch((err) => {});
};
// 搜索
const handleSearch = () => {
  // if (searchData.difficulty) {
  //   searchData.difficulty = Number(searchData.difficulty);
  // }
  isSearch.value = true;//是否触发了搜索按钮
  getList();//刷新列表
};
// 设置每页条数
const handleSizeChange = (val) => {
  searchData.pageSize = val;
  // 刷新列表
  getList();
};
// 当前页
const handleCurrentChange = (val) => {
  searchData.pageNo = val;
  // 刷新列表
  getList();
};
// 重置表单
const handleReset = () => {
  isSearch.value = false
  searchData.thirdCateId = [];
  searchData.beginTime = null;
  searchData.endTime = null;
};
// 获取时间
const getTime = (val) => {
  searchData.beginTime = val[0];//提问开始时间
  searchData.endTime = val[1];//提问结束时间
};
</script>
<style src="./index.scss" lang="scss" scoped></style>
<style lang="scss" scoped>
.contentBox{
  margin-bottom: 20px;
}
</style>
