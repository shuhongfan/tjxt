<template>
  <header class="bg-wt">
    <div class="container fx">
      <div class="logo">
        <img src="@/assets/logo.png" alt="" srcset="" />
      </div>
      <div class="fx-1 fx-ct">
        <el-input
          v-model="input"
          class="headerSearch "
          size="large"
          placeholder="请输入关键字"
          :suffix-icon="Search"
        />
      </div>
      
      <div class="fx-al-ct">
        <div class="car fx-al-ct">
          <img src="@/assets/icon-gwc-nor.png" alt="" /> 购物车
        </div>
        <router-link class="marg-lr-40" to="/">学习中心</router-link>
        <div></div>
        <div v-if="!isToken"><span>注册</span><span>/</span><span @click="">登录</span></div>
        <div class="fx-al-ct" v-if="isToken && userInfo">
          <img class="headIcon" :src="userInfo.icon" alt="">
          <div>{{userInfo.name}}</div>
        </div>
      </div>
    </div>
  </header>
</template>
<script setup>
import { onMounted, ref } from "vue";
import { Search } from "@element-plus/icons-vue";
import {TOKEN_NAME} from "@/config/global.js"
import { useUserStore } from '@/store'

const store = useUserStore();
const userInfo = ref()
const isToken = sessionStorage.getItem(TOKEN_NAME) ? true : false
const input = ref();

onMounted(() => {
  userInfo.value = store.getUserInfo
})


</script>
<style lang="scss" scoped>
header {
  width: 100%;
  background-color: var(--color-white);
  text-align: left;
  padding: 11px 0;
  font-size: 14px;
  .headerSearch {
    width: 427px;
    height: 40px;
    background: #edf0f4;
    border-radius: 8px;
    :deep(.el-input__wrapper){
      background-color: transparent;
    }
  }
  .car {
    img {
      width: 24px;
      height: 25px;
      margin-right: 6px;
    }
  }
  .headIcon{
    width: 30px;
    height: 30px;
    border-radius: 100%;
    margin-right: 10px;
    &:hover{
      // 透明度80%
      opacity: 0.5;
    }
  }
}
</style>
