<!--订单信息-->
<template>
  <div class="orderInfo">
    <!-- 标题 -->
    <div class="tit"><span>订单信息</span></div>
    <!-- end -->
    <div class="title"><i></i>申请原因：{{ fromData.refundReason }}</div>
    <div class="con">退款说明：{{ fromData.refundMessage }}</div>
    <div class="infoItem">
      <ul>
        <li>
          <div class="tit">ID编号</div>
          <p>
            总订单<span>{{
              fromData.orderId === "" ? "无" : fromData.orderId
            }}</span>
          </p>
          <p>
            子订单ID<span>{{
              fromData.orderDetailId === "" ? "无" : fromData.orderDetailId
            }}</span>
          </p>
          <p class="pb">
            <label>支付流水号</label><span>{{
              fromData.payOrderNo === "" ? "无" : fromData.payOrderNo
            }}</span>
          </p>

          <p class="pb">
            <label>退款ID</label><span>{{ fromData.id === "" ? "无" : fromData.id }}</span>
          </p>
          <p>
            <label>退款流水号</label><span>{{
              fromData.refundOrderNo === "" ? "无" : fromData.refundOrderNo
            }}</span>
          </p>
        </li>
        <li>
          <div class="tit">申请人信息</div>
          <p>
            学员名称<span>{{
              fromData.studentName === undefined ? "无" : fromData.studentName
            }}</span>
          </p>
          <p>
            手机号<span>{{
              fromData.mobile === undefined
                ? "无"
                : fromData.mobile
            }}</span>
          </p>
          <p>
            退款申请人<span
              >{{
                fromData.refundProposerName === undefined ? "无" : fromData.refundProposerName
              }}
            </span>
          </p>
        </li>
        <li>
          <div class="tit">支付/退款方式</div>
          <p>
            支付方式<span>{{
              fromData.payChannel === undefined ? "无" : fromData.payChannel
            }}</span>
          </p>
          <p>
            退款方式<span>{{
              fromData.refundChannel === undefined
                ? "无"
                : fromData.refundChannel
            }}</span>
          </p>
        </li>
        <li>
          <div class="tit">操作时间</div>
          <p>
            下单时间<span>{{
              fromData.orderTime === undefined ? "无" : formatTimeOrdinary(fromData.orderTime)
            }}</span>
          </p>
          <p>
            支付时间<span>{{
              fromData.paySuccessTime === undefined
                ? "无"
                : formatTimeOrdinary(fromData.paySuccessTime)
            }}</span>
          </p>
          <p>
            退款申请时间<span>{{
              fromData.createTime === undefined ? "无" : formatTimeOrdinary(fromData.createTime)
            }}</span>
          </p>
          <p>
            退款审批时间<span>{{
              fromData.approveTime === undefined ? "无" : formatTimeOrdinary(fromData.approveTime)
            }}</span>
          </p>
        </li>
      </ul>
    </div>
    <div >
      <!-- 标题 -->
      <div class="tit"><span>课程信息</span></div>
      <!-- end -->
      <!-- 课程信息 -->
      <div class="coureseInfo">
        <div class="infoList">
          <div class="headInfo">
            <p>课程名称</p>
            <p>课程价格(元)</p>
            <p>课程有效期</p>
          </div>
          <div class="con">
            <div>{{ fromData.name }}</div>
            <div>{{ decimalsReplenish(Number(fromData.price / 100 )) }}</div>
            <div>
              <p>{{ fromData.studyValidDuration || "--"}}</p>
              <p class="warnTip" >退款课程无法学习</p>
            </div>
          </div>
        </div>
        <div class="priceInfo">
          <p>
            子订单总价<label>{{ decimalsReplenish(Number(fromData.price / 100 ))}}元</label>
          </p>
          <p>
            优惠券<label>{{
              fromData.couponRule
                ? fromData.couponRule
                : "未使用优惠券"
            }}</label>
          </p>
          <p>
            优惠金额<label>{{
              fromData.discountAmount === 0
                ? "0.00元"
                : decimalsReplenish(Number(fromData.discountAmount / 100 ))+'元'
            }}</label>
          </p>
          <p class="amountPaid">
            实付金额<label>{{
              fromData.realPayAmount === 0
                ? "0.00元"
                : decimalsReplenish(Number(fromData.realPayAmount / 100 ))+'元'
            }}</label>
          </p>
        </div>
      </div>
      <!-- end -->
    </div>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted, watch } from "vue"
import { ElMessage } from "element-plus"
import { useRouter, useRoute } from "vue-router"
// 控制字节数
import { decimalsReplenish,formatTimeOrdinary } from "@/utils/index.js";
// ------定义变量------
// 获取父组件值、方法
const props = defineProps({
  // 订单信息
  fromData: {
    type: Object,
    default: () => ({}),
  },
  // 课程信息
  fromCourseData: {
    type: Object,
    default: () => ({}),
  },
})
//初始化路由
const router = useRouter() //获取全局
const route = useRoute() //获取局部
const emit = defineEmits() //子组件获取父组件事件传值
// ------生命周期------
onMounted(() => { })

// ------定义方法------

// 向父组件暴露方法
defineExpose({});
</script>
<style lang="scss" scoped>
.priceInfo {
  p {
    // margin-bottom: 4px;
  }
}

.amountPaid {
  font-weight: 600 !important;
  font-size: 14px;
  color: #ff734f;
}
</style>
