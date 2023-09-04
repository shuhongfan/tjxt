<!--回复弹层-->
<template>
  <div class="dialogBox">
    <el-dialog
      v-model="dialogVisible"
      title="我来回复"
      :before-close="handleClose"
    >
      <div>
        <el-form
          :model="fromData"
          ref="ruleFormRef"
          :rules="rules"
          class="demo-ruleForm"
        >
          <el-form-item label="" prop="content">
            <el-input
              v-model="fromData.content"
              type="textarea"
              placeholder="请输入您的回复，学员很期待您的回复哦！"
              resize="none"
              @change="handleChange"
              @input="textInput"
            />
            <span class="numText" :class="numVal === 0 ? 'tip' : ''"
              >{{ numVal }}/200</span
            >
          </el-form-item></el-form
        >
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button class="button buttonPrimary" @click="handleClose"
            >取消</el-button
          >
          <el-button class="button primary" @click="handleSubmit"
            >保存</el-button
          >
        </span>
      </template>
    </el-dialog>
  </div>
</template>
<script setup>
import { ref, reactive, nextTick, watch } from "vue"
import { ElMessage } from "element-plus"
// 接口api
import { getQuestionsDetails,getReplies,saveQuestionsReply,setAnswersFolded } from "@/api/question"
// 控制字节数
import { validateTextLength } from "@/utils/index.js"
// 获取父组件值、方法
const props = defineProps({
  // 弹层隐藏显示
  dialogVisible: {
    type: Boolean,
    default: false,
  },

})
// ------定义变量------
const emit = defineEmits() //子组件获取父组件事件传值
let fromData = reactive({})
const ruleFormRef = ref(); //表单校验ref
let numVal = ref(0) //内容字数显示
watch(
  () => fromData,
  (newValue, oldValue) => {
    // 分类名称默认的字数
    const value = validateTextLength(newValue.content, 200)
    numVal.value = value.numVal
  }
)
// ------定义方法------
//关闭弹层
const handleClose = () => {
  emit("handleClose")
  fromData.content = "" //清空数据
  numVal.value = 0
}
// 提交我的回复
const handleSubmit = () => {
  emit('handleSubmit')
  ruleFormRef.value.resetFields(); //清空表单数据
}
// 名字控制20个字符
const textInput = () => {
  nextTick(() => {
    const value = validateTextLength(fromData.content, 200)
    fromData.content = value.val
    numVal.value = value.numVal
  })
}
// 向父组件暴露方法
defineExpose({
  fromData,
});
</script>
