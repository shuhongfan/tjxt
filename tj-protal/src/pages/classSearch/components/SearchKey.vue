<!-- 搜索按钮 -->
<template>
<div class="search fx">
  <div class="tit">{{data.title}}:</div>
  <div :class="{active: activeId === item.id, checkBox:true, 'font-bt1': true}"
      @click="activeHandle(item.id)"
      v-for="(item, index) in data.searchKeys"
      :key="index">
    {{item.name}}
  </div>
</div>
</template>
<script setup>
import {  onMounted, ref } from 'vue'

// 引入父级传参
const props = defineProps({
  data:{
    type: Object,
    default:{}
  },
  active:{
    type: String,
    default: 'all'
  }
})
// 选中的状态
const activeId = ref(props.active)
onMounted(() => {
  // 如果非一级分类 这改为全部
  const dt = props.data.searchKeys.filter(n => n.id === props.active)
  if (dt.length === 0){
    activeId.value = 'all'
  }
})
const emit = defineEmits(['searchKey'])

// 点击选中
const activeHandle = (id) => {
  activeId.value = id
  // 效验 
  emit('searchKey', {key:props.data.subKey, value:id})
}
</script>
<style lang="scss" scoped>
.search{
  font-weight: 600;
  margin: 20px 0;
  line-height: 32px;
  .tit{
    width: 70px;
  }
  .checkBox{
    cursor: pointer;
    font-weight: 400;
    padding: 0px 10px;
    margin: 0 20px;
  }
  .active{
    background: #D5E8FF;
    color: var(--color-main);
    font-weight: 600;
    border-radius: 4px;
  }
}
</style>
