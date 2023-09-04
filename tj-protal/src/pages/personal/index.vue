<!-- 个人中心 - 核心架构页面 -->
<template>
  <div class="personalWrapper container">
    <div class="personal">
      <LeftNav></LeftNav>
      <router-view class="fx-1"></router-view>
    </div>
  </div>
</template>
<script setup>
import LeftNav from "./components/LeftNav.vue";
</script>
<style lang="scss" src="./index.scss"></style>
