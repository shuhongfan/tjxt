<!-- 详情 -->
<template>
  <div class="contentBox">
    <div class="bg-wt radius marg-tp-20">
      <div class="detailBox refundBox">
        <!-- 基本信息 -->
        <BaseInfo
          ref="baseInfo"
          :fromData="fromData.value"
        ></BaseInfo>
        <!-- end -->
      </div>
    </div>
    <div class="bg-wt radius marg-tp-20">
      <div class="detailBox refundBox">
        <!-- 处理结果 -->
        <Result
          ref="result"
          @setDispose="setDispose"
          @getOrderId="getOrderId"
          :baseData="fromData.value"
        ></Result>
        <!-- end -->
        <!-- end -->
      </div>
      <!-- 按钮 -->
      <div class="BottomBox detailBox">
        <div class="btn">

            <el-button  class="button buttonSub" @click="handleGetback"
              >返回</el-button
            >

            <el-button
            v-if="fromData.value&&fromData.value.status===1"
              class="button buttonSub"
              v-preventReClick
              @click="handleSubmit('getback')"
              >保存并返回</el-button
            >
            <!-- v-if="fromData.refundStatus===1"  -->
            <el-button
            v-if="fromData.value&&fromData.value.status===1"
              v-preventReClick
              class="button primary"
              @click="handleNext"
              >保存并继续</el-button
            >
        </div>
      </div>
    </div>
    <!-- 处理退款后的弹层 -->
    <Dispose :dialogVisible="dialogVisible" @setDispose="setDispose"></Dispose>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted, watch, nextTick, onUnmounted } from "vue";
import { useRouter, useRoute } from "vue-router";
// 获取vuex存储数据
import { useUserStore } from "@/store";
// 删除弹出层
import Cancel from "@/components/Cancel/index.vue";
// 导入组件
// 接口api
import { getDetails } from "@/api/refund";
// 导入组件
// 基本数据
import BaseInfo from "./components/BaseInfo.vue";
// 处理结果
import Result from "./components/Result.vue";
// 处理完后的弹层
import Dispose from "./components/Dispose.vue";
// ------定义变量------
// ------vuex存储数据------
const store = useUserStore();
//初始化路由
const router = useRouter(); //获取全局
const route = useRoute(); //获取局部
const emit = defineEmits(); //子组件获取父组件事件传值
const deleteText = ref("此操作将不会保存您所做的更改，是否继续？"); //需要删除的提示内容
const baseInfo = ref(); // 基本信息ref
const result = ref(); // 处理结果ref
let orderId = route.params.id ? route.params.id : null; //保存基础信息时后端返回的课程目录
let dialogCancelVisible = ref(false); //控制删除弹层
let dialogVisible = ref(false) //处理完后的弹层显示隐藏
let fromData = reactive({}); //编辑表单数据
let fromCourseData = reactive({}); //课程信息数据
// ------生命周期------
onMounted(() => {
  getDetailData(orderId);
});
// watch("orderId", (newValue, oldValue) => {
// });
// ------定义方法------
// 获取详情
const getDetailData = async (id) => {
  await getDetails(id)
    .then((res) => {
      if (res.code === 200) {
        fromData.value = res.data;
        // getCourseOrderData(fromData.value.orderDetailId);
      }
    })
    .catch((err) => {});
};
// 获取课程订单明细信息
const getCourseOrderData = async (id) => {
  await getCourseOrderDetail(id)
    .then((res) => {
      if (res.code === 200) {
        fromCourseData.value = res.data;
      }
    })
    .catch((err) => {});
};
// 保存
const handleSubmit = async (str) => {
  // 保存基本信息
  result.value.handleSubmit(str);
};

// 下一步
const handleNext = () => {
  // 保存基本信息
  result.value.handleSubmit();
};

// 获取保存基础信息时的课程id
const getOrderId = (val) => {
  router.push({
    path: "/order/refundDetails/" + val,
  });
  getDetailData(val);
};
// 返回
const handleGetback = (row) => {
  router.push({
    path: "/order/refund",
  });
};
// 所有的退款处理完毕后的弹层
const setDispose=()=>{
  dialogVisible.value= true
}
</script>
<style lang="scss" scoped>
.bg-wt {
  margin-bottom: 20px;
  .BottomBox {
    padding: 30px 0;
    border-top: 1px solid #F5EFEE;
    text-align: center;
    .btn {
      padding-top: 0;
      .button {
        width: 114px;
      }
    }
  }
}
</style>

