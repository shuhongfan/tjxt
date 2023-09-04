<!-- 支付 - 结算页面 -->
<template>
  <div class="settlementWrapper container bg-wt">
    <div class="title">购买课程</div>
    <div class="tab" >
      <div class="tabHead fx-sb">
        <div>课程名称</div>
        <div class="cal">单价(元)</div>
      </div>
      <div class="tabItem fx-sb" v-for="item in orderInfo.courses" :key="item.id">
        <div class="fx">
          <img :src="item.coverUrl" alt=""> 
          <span>{{item.name || item.courseName}}</span>
        </div>
        <div class="cal ft-cl-err" >￥ {{((item.nowPrice || item.price) / 100).toFixed(2)}}</div>
      </div>
    </div>
    <div class="settiementInfo">
      <div class="coupon fx">
        <div v-if="orderInfo.discounts && orderInfo.discounts.length > 0" >
          优惠券： <el-select @change="changeHandle" v-model="couponIds" placeholder="请选择优惠券">
                    <el-option
                      v-for="item in orderInfo.discounts"
                      :key="item.ids"
                      :label="item.rule"
                      :value="item.ids"
                      :disabled="item.disabled"
                    />
                  </el-select>
        </div>
        <div v-else class="noData"> 优惠券： 暂无可用优惠券</div>
        <div class="price" style="width: 195px">
          <div class="fx-sb"><span>订单总价：</span> <span>￥ {{amountConversion(orderInfo.totalAmount)}}</span></div>
          <div class="fx-sb"><span>优惠金额：</span>  <span>￥ {{amountConversion(discountAmount) || 0}}</span></div>
        </div>
      </div>
      <div class="paid"><span>实付金额：</span><span class="ft-cl-err"> ￥ {{price}}</span></div>
      <div @click="orderHandle"><span class="bt bt-red">去结算</span></div>
    </div>
  </div>
</template>
<script setup>
/** 数据导入 **/
import { onMounted, ref, computed } from "vue";
import { ElMessage } from "element-plus";
import { useRoute, useRouter } from "vue-router";
import { confirmOrderInfo, setOrder} from "@/api/order.js";
import { dataCacheStore } from "@/store"
import {amountConversion} from "@/utils/tool.js"
const store = dataCacheStore()

const route = useRoute()
const router = useRouter()
// const orderClass = ref([])
onMounted(() => {
  // 获取章节列表 - 下拉选择
  // orderClass.value = store.getOrderClassInfo
  comfirePageInfoHandle()
})
// 优惠金额
const discountAmount = ref(0)
// 选中的优惠券
const coupon = ref({})
// 实付金额
const price= computed(() => {
  let p = ((orderInfo.value.totalAmount - discountAmount.value)/100).toFixed(2)
  return p < 0 ? 0 : p;
})
// 优惠券Id
const couponIds = ref("")
// 订单信息
const orderInfo = ref({})
// 下单确认页信息
const comfirePageInfoHandle = async () => {
  const courseIds = route.query.courseIds
  const params = {courseIds}
  await confirmOrderInfo(params)
    .then((res) => {
      if (res.code === 200) {
        if(res.data.discounts){
          res.data.discounts.forEach(d => {
            d.ids = d.ids.join();
            d.rule = d.rules.length > 1 ? `叠加${d.rules.length}券：【优惠${d.discountAmount/100}元】` : `单券：【${d.rules[0]}】`;
          })
        }
        orderInfo.value = res.data
      } else {
        ElMessage({
          message:res.data.msg,
          type: 'error'
        });
      }
    })
    .catch(() => {
      ElMessage({
        message: "获取优惠券信息出错！",
        type: 'error'
      });
    });
} 
const changeHandle = val => {
  const value = orderInfo.value.discounts.filter(n => n.ids == val)
  discountAmount.value = value[0].discountAmount
}
// 下单
const orderHandle = async () => {
  const courseIds = orderInfo.value.courses.map(n => n.courseId ? n.courseId : n.id)
  const params = {courseIds, orderId: orderInfo.value.orderId}
  if (couponIds.value != null) {
    params.couponIds = couponIds.value.split(",");
  }
  await setOrder(params)
    .then((res) => {
      if (res.code === 200) {
        router.push({path:'/pay/payment',query:{orderId: res.data.orderId}})
      } else {
        ElMessage({
          message:res.data.msg,
          type: 'error'
        });
      }
    })
    .catch(() => {
      ElMessage({
        message: "获取优惠券信息出错！",
        type: 'error'
      });
    });
} 
</script>
<style lang="scss" src="./index.scss"> </style>
