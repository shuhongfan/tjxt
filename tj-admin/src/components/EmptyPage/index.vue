<!--空页面-->
<template>
  <div class="emptyPageBox">
      <!-- 没有搜索出数据要显示的图片和提示语 -->
    <div v-if="isSearch&&baseData.length===0">
        <div class="icon searchIcon"></div>
      <div class="text">要不换个字试试～</div>
    </div>
    <!-- 没有列表数据要显示的图片和提示语 -->
    <div v-else>
      <div class="icon"></div>
      <div class="text">一条数据也没有～</div>
    </div>
  </div>
</template>
<script setup>
import { ref, reactive, nextTick } from "vue";
// 获取父组件值、方法
const props = defineProps({
  // 列表数据
  baseData: {
    type: Array,
    default: () => [],
  },
  // 是否触发了搜索按钮
  isSearch: {
    type: Boolean,
    default: false,
  },
});
</script>
<style lang=scss>
.el-scrollbar__view .el-table__empty-block{
    width: 100% !important;
}
.emptyPageBox {
  text-align: center;
  padding: 86px 0;
  color: var(--color-font1);
  /* min-height: 65vh; */
  .icon {
    background: url(../../assets/emptyIcon.png) no-repeat;
    background-size: contain;
    width: 162px;
    height: 127px;
    margin: 0 auto;
  }
  .searchIcon{
    background: url(../../assets/emptySearchIcon.png) no-repeat;
    background-size: contain;
  }
  .text {
    padding: 15px;
    line-height: 22px;
    font-size: var(--font-size16);
  }
}
</style>
