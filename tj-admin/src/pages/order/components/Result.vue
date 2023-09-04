<!--基本信息-->
<template>
  <div class="orderInfo">
    <div class="tit">
      <span>订单信息</span>
      <el-button
        class="button primary"
        @click="handleGetback"
        v-if="baseData.canRefund"
        style="width: 130px"
      >
        发起退款</el-button
      >
      <el-button
        class="button primary"
        v-else
        @click="handleGetback"
        style="background: #ffb9ae"
        disabled
      >
        发起退款</el-button
      >
    </div>
    <!-- end -->
    <!-- 课程信息 -->
    <div class="coureseInfo">
      <div class="infoList">
        <div class="headInfo">
          <p>课程名称</p>
          <p>课程价格(元)</p>
          <p>学习有效期</p>
        </div>
        <div class="con">
          <div>{{ baseData.name }}</div>
          <div>{{ (baseData.price / 100).toFixed(2) }}</div>
          <div>
            <p>
              {{
                !baseData.studyValidTime
                  ? '无'
                  : baseData.studyValidTime
              }}
            </p>
            <p v-if="baseData.refundStatus == 5" class="disabledStudy">
              退款课程无法学习
            </p>
          </div>
        </div>
      </div>
      <div class="priceInfo">
        <p>
          子订单总价<label>{{ (baseData.price / 100).toFixed(2) }}元</label>
        </p>
        <p>
          优惠券<label>{{
            !baseData.couponRule ? '无' : baseData.couponRule
          }}</label>
        </p>
        <p>
          优惠金额<label>{{
            !baseData.discountAmount
              ? '无'
              : (baseData.discountAmount / 100).toFixed(2)
          }}</label>
        </p>
        <p class="amountPaid">
          实付金额<label class="payAmount">
            <!-- {{ baseData.realPayAmount}}的值保留两位小数 -->
            {{ (baseData.realPayAmount / 100).toFixed(2) }}
            元</label
          >
        </p>
      </div>
    </div>
  </div>
  <refund
    ref="add"
    :dialogFormVisible="dialogFormVisible"
    :baseData="baseData"
    @getorderList="getorderList"
    @handleClose="handleClose"
  ></refund>
</template>
<script setup>
import { ref, onMounted } from "vue"
import { formatTimeOrdinary } from "@/utils/index";
//公用数据
// 导入组件
import refund from "./refund.vue"
// ------定义变量------
// 获取父组件值、方法
// 获取父组件值、方法
const props = defineProps({
  // 订单信息
  baseData: {
    type: Object,
    default: () => ({}),
  },
})
//初始化路由
let dialogFormVisible = ref(false) //谈层隐藏显示
// 表单校验
// ------生命周期------
onMounted(() => {
})
// 打开退款弹层
const handleGetback = () => {
  dialogFormVisible.value = true
}
// 关闭弹层
const handleClose = () => {
  dialogFormVisible.value = false
}
// 获取刷新列表
const getorderList = () => {
  dialogFormVisible.value = false
  $emit("getList")
}
// 向父组件暴露方法
defineExpose({
});
</script>
<style lang="scss" scoped>
.tit {
  display: flex;
  justify-content: space-between;
  align-items: center;
  :deep(.button) {
    width: 90px !important;
    height: 40px;
    span {
      padding: 0;
      font-weight: 500;
      font-size: 14px;
      color: #ffffff;
    }
  }
}
.amountPaid {
  font-weight: 600 !important;
  font-family: PingFangSC-S0pxibold;
  font-size: 14px;
  color: #ff734f;
}
.priceInfo p label {
  padding: 0;
}
.disabledStudy {
  font-family: PingFangSC-Regular;
  font-weight: 400;
  font-size: 13px;
  color: #ffad33;
}
.payAmount {
  font-family: PingFangSC-S0pxibold;
  font-weight: 600;
  font-size: 20px;
  color: #ff734f;
}
</style>
