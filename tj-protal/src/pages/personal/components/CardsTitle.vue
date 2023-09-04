<!-- 我的课程 - 标题  -->
<template>
  <div class="cardsTitle fx-sb ft-20 ft-wt-600">
    <div>{{title}}</div>
    <slot />
  </div>
</template>
<script setup>
// 介绍父组件传来的标题
defineProps({
  title:{
    type: String,
    default: '最近学习'
  }
})  
</script>
<style lang="scss" scoped>
.cardsTitle{
  font-weight: 600;
  font-size: 20px;
}
</style>
