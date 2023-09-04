<!-- 公用面包屑-所有详情页面使用 -->
<template>
  <div class="breadcrumb ">
    <span class="font-bt2" @click="() => {$router.push('/')}" >首页</span>
    <span style="padding:0 6px"> / </span>
    <span>{{data}}</span>
  </div>
</template>
<script setup>

const props = defineProps({
  data: {
    type: String,
    default: "一级分类",
  }
});

</script>
<style lang="scss" scoped>
.breadcrumb{
    position: relative;
    z-index: 9;
    font-size: 14px;
    padding: 36px 0 20px 0;
    a{
      color: var(--color-font1);
    }
  }
</style>
