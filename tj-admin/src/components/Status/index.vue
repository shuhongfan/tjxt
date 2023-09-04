<template>
  <div class="deleteDialog">
    <el-dialog
      v-model="dialogVisible"
      title="确认禁用"
      :show-close="false"
    >
      
      此操作将禁用 {{statusText}}，是否继续？
      <template #footer>
        <span class="dialog-footer">
          <el-button class="button buttonPrimary" @click="handleClose"
            >再想想</el-button
          >
          <el-button class="button primary" @click="handleSubmit"
            >确认禁用</el-button
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
  statusText:{
    type: String,
    default: '',
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
  emit("handleStatus",1);
};
</script>
