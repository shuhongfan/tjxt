<template>
  <div class="addBox">
    
    <el-button class="button primary" @click="handleAdd">{{ text }}</el-button>
  </div>
</template>
<script setup>
import { ref, reactive } from "vue";
// 获取父组件值、方法
const props = defineProps({
  // 搜索对象
  text: {
    type: String,
    default: "",
  },
});
// ------定义变量------
const emit = defineEmits(); //子组件获取父组件事件传值
// ------定义方法------
// 搜索
const handleAdd = () => {
  emit("handleAdd");
};
</script>