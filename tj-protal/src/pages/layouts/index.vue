<!-- 架构页面 -->
<template>
<div class="layoutsWrapper fx-fd-col">
  <Header></Header>
    <router-view class="fx-1"/>
  <Footer></Footer>
  </div>
</template>
<script setup>
import Footer from '@/components/Footer.vue';
import Header from '@/components/Header.vue';
</script>
<style lang="scss" scoped>
.layoutsWrapper {
  min-height: 100vh;
}
</style>