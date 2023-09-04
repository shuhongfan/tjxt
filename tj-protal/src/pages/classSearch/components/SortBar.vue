<!-- 排序 -->
<template>
  <div class="sortBar">
    <span :class="{active: activeKey == item.value, checkBox:true}" class="font-bt3" v-for="(item, index) in data" :key="index" @click="activeHandle(item.value)">{{item.key}}</span>
  </div>
</template>
<script setup>
import { ref } from 'vue'

// 引入父级传参
defineProps({
  data:{
    type: Object,
    default:{}
  }
})
// emit数据载入
const emit = defineEmits(['sortHandle'])
// 排序选中参数定义
const activeKey = ref('all')
// 点击选中
const activeHandle = (value) => {
  activeKey.value = value
  // 提交父级 
  emit('sortHandle', value)
}
</script>
<style lang="scss" scoped>
.sortBar{
.checkBox{
    cursor: pointer;
    font-weight: 400;
    padding: 4px 14px;
    font-size: 14px;
    margin-right: 20px;
    // color: var(--color-font1);
    border-radius: 20px;
  }
  .active{
    background: #2080F7;
    color: var(--color-main);
    color: #fff;
    font-weight: 600;
    border-radius: 30px;
  }
}
</style>
