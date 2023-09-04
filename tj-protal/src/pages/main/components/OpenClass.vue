<!-- 顶部Banner区域左侧列表 -->
<template>
<div class="openClass">
   <MainTitle class="marg-bt-20" :title="title"></MainTitle>
   <div class="classCont fx-wp">
     <ClassCards class="items marg-bt-20" v-for="(item, index) in data" :data="item" :key="index"></ClassCards>
   </div>
</div>
</template>
<script setup>
import MainTitle from '@/components/MainTitle.vue'
import ClassCards from '@/components/ClassCards.vue'
const props = defineProps({
  title:{
    type: String,
    default:'模块标题'
  },
  data:{
    type: Array,
    default:[]
  }
})

</script>
<style lang="scss" scoped>
.openClass {
  padding-bottom: 40px;
  .classCont{
    justify-content: flex-start;
    .items{
      margin-right: 20px;
      width: calc(25% - 15px);
    }
    .items:nth-child(4n){
      margin-right: 0;
    }
  }
}
</style>
