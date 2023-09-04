<!--退款审批列表-->
<template>
  <div class="contentBox">
    <!-- 搜索 -->
    <Search
      :searchData="searchData"
      ref="serchInfo"
      @getTime="getTime"
      @getList="getList"
      @handleSearch="handleSearch"
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
          :searchData="searchData"
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
// 获取vuex存储数据
import { useUserStore } from "@/store";
// 接口api
import { getRefundPage } from "@/api/refund";
// 导入组件
// 搜索
import Search from "./components/Search.vue";
// 表格
import TableList from "./components/TableList.vue";
// ------定义变量------
// ------vuex存储数据------
const store = useUserStore();
//初始化路由
const router = useRouter();
const loading = ref(false); //加载数据
const serchInfo = ref(); //定义搜索ref
let total = ref(null); //数据总条数
let searchData = reactive({
  pageSize: 10,
  pageNo: 1,
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
// 获取列表值
const getList = async () => {
  loading.value = true;
  await getRefundPage(searchData)
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
// 获取退款申请时间
const getTime = (val) => {
  searchData.applyStartTime = val[0];//退款申请开始时间
  searchData.applyEndTime = val[1];//退款申请结束时间
};
// 重置表单
const handleReset = () => {
  isSearch.value = false;
  // 清空搜索表单内容
  searchData.applyStartTime = null;
  searchData.applyEndTime = null;
};
</script>
<style src="./index.scss" lang="scss" scoped></style>
<style lang="scss" scoped>
.contentBox{
  margin-bottom: 20px;
}
</style>