<!--  我的课程 - 卡片  -->
<template>
  <div class="orderCards">
    <img :src="data.coverUrl" alt=""  @click="() => $router.push({path: '/details/index', query:{id: data.courseId}})">
    <div class="info">
      <p class="tit">{{data.name}}</p>
      <p>{{data.price === 0 ?  '免费' : '¥' + (data.price / 100).toFixed(2)}}</p>
    </div>
  </div>
</template>
<script setup>

// 接收父组件传来的标题
defineProps({
  data:{
    type: Object,
    default: {}
  }
})  
</script>
<style lang="scss" scoped>
.orderCards{
  padding: 15px 0;
  display: flex;
  align-items: center;
  img{
    width: 120px;
    height: 68px;
    border-radius: 8px;
    margin-right: 20px;
  }
  .info{
    line-height: 32px;
    font-size: 14px;
    .tit{
      font-size: 16px;
      font-weight: 500;
    }
  }
}
</style>
