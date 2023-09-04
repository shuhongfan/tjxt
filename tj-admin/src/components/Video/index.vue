<template>
  <div class="videoDialog">
    <el-dialog
      v-model="dialogVisible"
      title="确认隐藏"
      :show-close="false"
    >
      
      <div class="uploadTitle"><i class="icon succeed"></i>共上传{{videoNumber}}个视频</div>
      <div class="uploadCon">{{videoNumber}}个视频上传成功，0个视频上传失败</div>
      <template #footer>
        <span class="dialog-footer">
          <el-button class="button primary" @click="handleClose"
            >我知道了</el-button
          >
        </span>
      </template>
    </el-dialog>
  </div>
</template>
<script setup>
import { ref, reactive, nextTick } from "vue";
// 获取父组件值、方法
const props = defineProps({
  // 弹层隐藏显示
  dialogVisible: {
    type: Boolean,
    default: false,
  },
  videoNumber:{
    type: Number,
    default: 0,
  }
});
// ------定义变量------
const emit = defineEmits(); //子组件获取父组件事件传值
// ------定义方法------
//关闭弹层
const handleClose = () => {
  emit("handleClose");
};
//提交确定删除
const handleSubmit = () => {
  emit("handleStatus");
};
</script>
