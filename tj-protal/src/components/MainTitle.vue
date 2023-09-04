<!-- 标题组件 -->
<template>
  <div class="mainTitle fx">
    <div class="marg-rt-10">{{ title }}</div>
    <div  @click="() => $router.push(moreUrl)" class="more ft-cl-des ft-14 font-bt1">
      更多 <i class="iconfont zhy-btn_more" style="font-size: 24px;"></i>
    </div>
  </div>
</template>
<script setup>

defineProps({
  title: {
    type: String,
    default: "课程标题",
  },
  more: {
    type: Boolean,
    default: true,
  },
  moreUrl:{
    type: String,
    default: '/search/index',
  },
});

</script>
<style lang="scss" scoped>
.mainTitle {
  font-size: 23px;
  color: #19232b;

  .more {
    display: flex;
    align-items: center;
    cursor: pointer;
    img{
      width: 20px;
      height: 20px;
      position: relative;
      top: -1px;
    }
  }
}
</style>
