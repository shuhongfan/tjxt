<!-- 订单管理列表-->
<template>
  <div class="contentBox">
    <div class="bg-wt radius marg-tp-20">
      <div class="courseBox orderBox">
        <ul>
          <li>
            <span class="img"></span>
            <div class="text">
              <p>12.56</p>
              <p>累计订单金额（万元）</p>
            </div>
          </li>
          <li>
            <span class="img"></span>
            <div class="text">
              <p>1.56</p>
              <p>待支付金额（万元）</p>
            </div>
          </li>
          <li>
            <span class="img"></span>
            <div class="text">
              <p>0.56</p>
              <p>已关闭金额（万元）</p>
            </div>
          </li>
          <li>
            <span class="img"></span>
            <div class="text">
              <p>1.00</p>
              <p>已退款金额（万元）</p>
            </div>
          </li>
          <li>
            <span class="img"></span>
            <div class="text">
              <p>9.44</p>
              <p>实收金额（万元）</p>
            </div>
          </li>
        </ul>
      </div>
    </div>
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
          @handleEdit="handleEdit"
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
import { decimalsReplenish, formatYear } from "@/utils/index.js";
import moment from "moment";
// 获取vuex存储数据
import { useUserStore } from "@/store";
// 公用数据
import { statusData } from "@/utils/commonData";
// 接口api
import { getOrderPage, getDetails } from "@/api/order";
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
let datePicker = ref([formatYear(), formatYear()]); //时间数据，获取默认时间的是数组
const defaultTime = ref([
  new Date(new Date()),
  new Date(2000, 2, 1, 23, 59, 59),
]);
const dateReceiveTimeOption = (time) => {
  return moment(time).isBefore(Date.now() - 24 * 60 * 60 * 1000);
};
let searchData = reactive({
  pageSize: 10,
  pageNo: 1,
}); //搜索对象
let baseData = reactive([]); //表格数据
let fromData = reactive({}); //新增编辑表单数据
let isSearch = ref(false); //是否触发了搜索按钮,用来控制没有搜索出数据和正常列表无数据的区分，显示的图片和提示语不一样
// ------生命周期------
onMounted(() => {
  init();
});
// ------定义方法------
// 获取初始值
const init = () => {
  getList(); //获取订单列表数据
};
// 获取列表值
const getList = async () => {
  loading.value = true;
  await getOrderPage(searchData)
    .then((res) => {
      if (res.code === 200) {
        loading.value = false;
        baseData.value = res.data.list;
        total.value = res.data.total;
        baseData.value.map((val) => {
          let realPayAmount = Number(val.realPayAmount) / 100;
          let orderAmount = Number(val.orderAmount) / 100;
          if (realPayAmount > 0) {
            val.realPayAmount = decimalsReplenish(Number(realPayAmount));
          }
          if (orderAmount > 0) {
            val.orderAmount = decimalsReplenish(Number(orderAmount));
          }
        });
      }
    })
    .catch((err) => {});
};

// 搜索
const handleSearch = () => {
  isSearch.value = true; //是否触发了搜索按钮
  getList(); //刷新列表
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
// 获取时间
const getTime = (val) => {
  searchData.orderStartTime = val[0]; //开始时间
  searchData.orderEndTime = val[1]; //结束时间
};
// 重置表单
const handleReset = () => {
  // 清空搜索表单内容
  searchData.orderStartTime = null;
  searchData.orderEndTime = null;
  isSearch.value = false;
};
</script>
<style src="./index.scss" lang="scss" scoped></style>
<style lang="scss" scoped>
p {
  width: 140px;
}
.contentBox {
  margin-bottom: 20px;
}
</style>