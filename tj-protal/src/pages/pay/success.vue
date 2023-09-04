<!-- 支付 - 成功页面 -->
<template>
  <div class="successWrapper container bg-wt">
      <div class="successCont">
        <div><img src="@/assets/icon_success.png" width="72" height="72" alt=""></div>
        <div class="tit">支付成功</div>
        <div class="order">订单ID ：202203071921003</div>
        <div @click="() => $router.push('/personal/main/myOrder')"><span class="bt">查看订单详情</span></div>
      </div>
  </div>
</template>
<script setup>
/** 数据导入 **/
import { onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";

const route = useRoute()
const router = useRouter()

onMounted(() => {

})
</script>
<style lang="scss" src="./index.scss"> </style>
