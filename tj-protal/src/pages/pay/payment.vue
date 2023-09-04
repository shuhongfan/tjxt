<!-- 支付 - 支付页面 -->
<template>
  <div class="paymenttWrapper container bg-wt">
    <div class="title">支付订单</div>
    <div class="successCont fx-sb">
      <div class="fx">
        <img src="@/assets/icon_success.png" width="72" height="72" alt="">
        <div class="">
          <p class="tit">订单提交成功！请尽快完成支付。</p>
          <p class="fx">支付还剩 <span class="ft-cl-err"><Countdown @timeOver="timeOver" :endTime="orderInfo && orderInfo.payOutTime"></Countdown></span> , 超时后将取消订单</p>
        </div>
      </div>
      <div class="price">
        <span>应付金额：</span>
        <span class="ft-cl-err">¥ {{orderInfo &&  amountConversion(orderInfo.payAmount)}}</span>
      </div>
    </div>
    <div class="pay">
      <div class="tit">选择一下支付方式付款</div>
      <div class="fx">

        <div v-for="item in payMethodList" :key="item.id" @click="payMethodCheck(item)" class="cont marg-rt-20" :class="{act: payMethod.id === item.id}">
          <img :src="item.channelIcon" width="44" height="44" alt=""> {{ item.name }}
        </div>
      </div>
    </div>
    <!-- 支付二维码弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      width="440px"
      :before-close="handleClose"
    >
      <template #header class="dialog-title">
        <span>{{payMethod.name}}支付</span>
      </template>
      <div style="padding: 0 40px" v-if="qrCodeUrl != ''">
        <qrcode-vue :key="qrCodeUrl" :value="qrCodeUrl" :size="320" level="H" />
      </div>
      <template #footer>
        <div class="dialog-footer">
          <p>请使用<em> {{payMethod.name}} </em>扫一扫</p> <p>二维码完成支付</p>
        </div>
      </template>
    </el-dialog>
  </div>
</template>
<script setup>
/** 数据导入 **/
import { onMounted,reactive, ref } from "vue";
import { ElMessage } from "element-plus";
import { useRoute, useRouter } from "vue-router";
import { getPayMethod, getPayUrl, getPayState } from "@/api/order.js";
import {amountConversion} from "@/utils/tool.js"
import QrcodeVue from 'qrcode.vue'
import Countdown from './components/Countdown.vue'

const route = useRoute()
const router = useRouter()

onMounted(() => {
  // 获取支付渠道列表
  getPayMethodList()
  // 获取订单的信息及时效
  getPayStateData()
})
// 支付二维码弹窗数据
const dialogVisible = ref(false)
const dialogCont = reactive({
  title: '',
  desc: ''
})

const title = ref('')
// 选择支付方式
const payMethod = ref({})
const payMethodCheck = (item) => {
  payMethod.value = item
  // 获取二维码 
  getPayUrlData(item)
} 

// 选择小节的数据
const value = ref([])

// 提问数据
const ruleForm = reactive({
  courseId: '', // 课程id,
  chapterId: '',  // 章Id
  sectionId: '', // 小节Id
  title: '', 
  anonymity: false, // 是否匿名
  description: '',
})

// 获取支付渠道列表
const payMethodList = ref([]) // 支付渠道信息
const getPayMethodList = async () => {
  await getPayMethod()
    .then((res) => {
      if (res.code == 200) {
        payMethodList.value = res.data
      } else {
        ElMessage({
          message:res.data.msg,
          type: 'error'
        });
      }
    })
    .catch(() => {
      ElMessage({
        message: "获取支付渠道列表出错！",
        type: 'error'
      });
    });
} 

// 获取支付二维码链接
const timer = ref(null) // 定时获取支付状态
const qrCodeUrl = ref('') 
const getPayUrlData = async val => {
  const payChannelCode = val.channelCode
  const params = {orderId: route.query.orderId, payChannelCode}
  await getPayUrl(params)
    .then((res) => {
      if (res.code == 200) {
        qrCodeUrl.value = res.data
        dialogVisible.value = true
        timer.value = setInterval(() => {
          getPayStateData()
        }, 5000)
      } else {
        ElMessage({
          message:res.data.msg,
          type: 'error'
        });
      }
    })
    .catch(() => {
      ElMessage({
        message: "获取支付二维码出错！",
        type: 'error'
      });
    });
} 
// 获取支付信息,包含支付状态和支付超时时间 payStaus 1:待支付，2：已支付，3：已关闭，4：已完成，5：已报名，6：已退款
const orderInfo = ref()
const isFirstGet = ref(true)
const getPayStateData = async () => {
  await getPayState({orderId: route.query.orderId})
    .then((res) => {
      if (res.code === 200) {
        if (res.data.status === 1 && isFirstGet.value){
          isFirstGet.value = false
          orderInfo.value = res.data
        } else if (res.data.status === 2 || res.data.status === 5){
          clearInterval(timer.value)
          router.push({path:'/pay/success', query:{order: res.data.id}})
        } else {
          // ElMessage('状态是非待支付或已支付成')
        }
      } else {
        ElMessage({
          message:res.data.msg,
          type: 'error'
        });
      }
    })
    .catch(() => {});
} 
const handleClose = (done) => {
  clearInterval(timer.value)
  done()
}
// 订单超时回首页去吧
const timeOver = () => {
  router.push('/main/index')
}
</script>
<style lang="scss" src="./index.scss"> </style>
