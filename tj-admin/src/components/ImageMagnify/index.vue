<template>
  <div class="picBox">
    <el-dialog v-model="dialogPicVisible" :title="title">
      <div class="close" @click="handleClose"></div>
      <div class="pic"><img :src="pic" /></div>
    </el-dialog>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from "vue";
// 获取父组件值、方法
const props = defineProps({
  // 弹层隐藏显示
  dialogPicVisible: {
    type: Boolean,
    default: false,
  },
  // 要放大的图片
  pic: {
    type: String,
    default: "",
  },
});
// ------定义变量------
const emit = defineEmits(); //子组件获取父组件事件传值
// ------定义方法------
const handleClose = () => {
  emit("handleMagnifyClose");
};
</script>
