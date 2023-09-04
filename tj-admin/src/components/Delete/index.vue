<template>
  <div class="deleteDialog">
    <el-dialog
      v-model="dialogDeleteVisible"
      title="确认删除"
      :show-close="false"
    >
      
      {{deleteText}}
      <template #footer>
        <span class="dialog-footer">
          <el-button class="button buttonPrimary" @click="handleClose"
            >再想想</el-button
          >
          <el-button class="button primary" @click="handleSubmit"
            >确认删除</el-button
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
  dialogDeleteVisible: {
    type: Boolean,
    default: false,
  },
  deleteText:{
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
  emit("handleDelete");
};
</script>
