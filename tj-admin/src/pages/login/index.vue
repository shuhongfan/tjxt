<!-- 登录页面 -->
<template>
  <div class="loginWrapper fx-fd-col fx-sb">
    <video autoplay loop muted="" preload class="loginVideo">
      <source src="@/assets/login.mp4" type="video/mp4">
     </video>
    <div class="fx-1 fx-al-ct">
      <div class="login">
        <div class="title">
          <img src="@/assets/logo.png" alt="" width="198" height="60" srcset="">
        </div>
        <LoginPass v-if="act == 'pass'"></LoginPass>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref } from "vue";
// 组件
// 用户登录组件
import LoginPass from "./components/LoginPass.vue";
// ------定义方法------
// 选中方法
const act = ref("pass");
// 切换登录方式
const changeLoginType = (type) => {
  act.value = type;
};
</script>
<style lang="scss" src="./index.scss"></style>
