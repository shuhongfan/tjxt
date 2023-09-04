<!-- 架构页面 -->
<template>
  <div class="layoutsWrapper">
    <LeftSide></LeftSide>
    <div class="contain fx-1">
      <Header></Header>
      <router-view class="mainWrapper" />
    </div>
  </div>
</template>
<script setup>
import LeftSide from "./components/LeftSide.vue";
import Header from "./components/Header.vue";
</script>
<style lang="scss" scoped>
.layoutsWrapper {
  // min-height: 100vh;
  display: flex;
  .contain {
    padding-left: 226px;
    overflow: hidden;
    min-width: 1366px;
  }
  .mainWrapper {
    padding-top: 66px;
    padding-left: 20px;
    padding-right: 20px;
    // padding-bottom: 20px;
  }
}
</style>