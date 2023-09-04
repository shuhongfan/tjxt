<!-- 标题组件 -->
<template>
  <div class="empty fx">
    <img class="imgcs" v-if="type" src="@/assets/img_empty1.png" alt="">
    <img v-else src="@/assets/img_empty.png"  alt="">
    <div :class="{wt: type}">{{desc}}</div>
  </div>
</template>
<script setup>

defineProps({
  type: {
    type: Boolean,
    default: false,
  },
  desc: {
    type: String,
    default: "一条数据也没有～",
  },
});

</script>
<style lang="scss" scoped>
.empty {
    width: 100%;
    height: 100%;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    font-weight: 400;
    font-size: 14px;
    line-height: 70px;
    color: #19232B;
    img{
        width:162px;
        height:144px;
    }
    .imgcs{
      width:104px;
      height:94px;
    }
    .wt{
      color: #fff;
    }
}
</style>
