<!-- 详情 -->
<template>
  <div class="contentBox">
    <div class="bg-wt radius marg-tp-20">
      <div class="detailBox">
        <!-- 基本信息 -->
        <BaseInfo
          ref="baseInfo"
          :fromData="orderInfo.data"
        ></BaseInfo>
        <!-- end -->
      </div>
    </div>
    <div class="bg-wt radius marg-tp-20">
      <div class="detailBox">
        <!-- 处理结果 -->
        <Result ref="result" :baseData="orderInfo.data" @getList="getList"></Result>
        <!-- end -->
      </div>
      <div class="BottomBox detailBox">
        <!-- 按钮 -->
        <div class="btn">
          <el-button class="button primary" @click="handleGetback" style="width: 130px">
            返回</el-button
          >
        </div>
        <!-- end -->
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted, watch, provide } from "vue";
import { useRouter, useRoute } from "vue-router";
// 导入组件
// 接口api
import { getDetails } from "@/api/order";
// 导入组件
// 基本数据
import BaseInfo from "./components/detailBaseInfo.vue";
// 处理结果
import Result from "./components/Result.vue";
// ------定义变量------
//初始化路由
const router = useRouter(); //获取全局
const route = useRoute(); //获取局部
const baseInfo = ref(); // 基本信息ref
const result = ref(); // 处理结果ref
let orderId = route.params.id ? route.params.id : null; //保存基础信息时后端返回的课程目录
let fromData = reactive({
  data: {
    nodes:[
      {
        time:'',
        name:'',
        nodeCode:'',
      }
    ]
  },
}); //编辑表单数据
let fromCourseData = reactive({
  data: {},
});
//课程信息数据
let orderInfo = reactive({data:{nodes:[]}});
// ------生命周期------
onMounted(() => {
  getDetailData(orderId);
  // getCourseOrderData(orderId);
});
watch('orderId',()=>{
});
// 刷新页面，更新页面数据
const getList = () => {
  getDetailData(orderId);
  // getCourseOrderData(orderId);
};
// 将getList的方法传递给孙组件
provide('getList',getList)
// ------定义方法------
// 获取详情
const getDetailData = async (id) => {
  await getDetails(id)
    .then((res) => {
      if (res.code === 200) {
        orderInfo.data = res.data;
      }
    })
    .catch((err) => {console.log(err)});
};
// 获取课程订单明细信息
const getCourseOrderData = async (id) => {
  await getCourseOrderDetail(id)
    .then((res) => {
      if (res.code === 200) {
        fromCourseData.data = res.data;
      }
    })
    .catch((err) => {});
};

// 返回
const handleGetback = (row) => {
  router.push({
    path: "/order/index",
  });
};
</script>
<style lang="scss" scoped>
.bg-wt{
  margin-bottom: 20px;
  .BottomBox{
    padding: 20px 0;
    border-top: 1px solid #ebeef5;
    text-align: center;
    .btn{
      padding-top: 0;
      .button{
        width: 114px;
      }
    }
}
}
.detailBox{
  padding-top: 20px;
  padding-right: 30px;
}
:deep(.el-step.is-horizontal .el-step__line){
  height: 1px;
}
</style>
