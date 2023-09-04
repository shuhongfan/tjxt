<!-- 登录页面 -->
<template>
  <div class="loginWrapper fx-fd-col fx-sb">
    <video autoplay loop muted="" preload class="loginVideo">
      <source src="@/assets/login.mp4" type="video/mp4">
    </video>
    <div class="fx-1 fx-al-ct">
      <div class="login">
        <div class="title" v-show="act != 'register'">
          <span @click="changeLoginType('pass')" :class="act == 'pass' ? 'fx-1 active': 'fx-1'">账号登录</span>
          <span @click="changeLoginType('phone')" :class="act == 'phone' ? 'fx-1 active': 'fx-1'">短信登录</span>
        </div>
        <div class="title" v-show="act == 'register'">
          <span class="ft-wt-600 ft-cl-1" >注 册</span>
        </div>
        <!-- 用户名密码登录 - start -->
        <LoginPass v-if="act == 'pass'" @goHandle="goHandle"></LoginPass>
        <!-- 手机号登录 - start -->
        <LoginPhone v-if="act == 'phone'" @goHandle="goHandle"></LoginPhone>
        <!-- 注册 - start -->
        <Register v-if="act == 'register'" @goHandle="goHandle"></Register>
      </div>
    </div>
    <Footer></Footer>
  </div>
</template>
<script setup>
import { ref, watchEffect } from 'vue';
import Footer from '@/components/Footer.vue'
import Header from '@/components/Header.vue';
import LoginPass from './components/LoginPass.vue';
import LoginPhone from './components/LoginPhone.vue';
import Register from './components/Register.vue';
import { useRoute } from 'vue-router';
const route = useRoute()

// 选中方法
const act = ref('pass')

// 切换登录方式
const changeLoginType = (type) => {
act.value = type
}
// 去注册
const goHandle = val => {
  act.value = val
}
// 注册监听 - 路由
watchEffect(() => {
  // 头部的登录注册 通过url的方式触发
  if(route.query.md)
  goHandle(route.query.md)
})
</script>
<style lang="scss" src="./index.scss"></style>
