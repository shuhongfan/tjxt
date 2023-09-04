<template>
  <el-dialog
    v-model="dialogFormVisible"
    :before-close="handleClose"
    title="选择退款原因"
  >
    <el-form
      :model="refundData"
      ref="ruleFormRef"
      :rules="rules"
      label-width="105px"
      class="demo-ruleForm"
    >
      <el-form-item label="退款原因" prop="refundReason">
        <el-radio-group v-model="refundData.refundReason">
          <el-radio
            v-for="(item, index) in radioValue"
            :key="index"
            :label="item.label"
            >{{ item.label }}</el-radio
          >
        </el-radio-group>
      </el-form-item>
      <el-form-item label="退款说明：" prop="questionDesc">
        <div class="el-input">
          <el-input
            v-model="refundData.questionDesc"
            type="textarea"
            placeholder="请输入"
            resize="none"
            @input="useintroInput"
            maxlength="100"
            show-word-limit
          ></el-input>
        </div>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button class="button buttonPrimary" @click="handleClose"
          >取消</el-button
        >
        <el-button class="button primary" v-preventReClick @click="handleSubmit"
          >保存</el-button
        >
      </span>
    </template>
  </el-dialog>
</template>
<script setup>
import { ref, reactive, watch, inject } from "vue"
import { ElMessage } from "element-plus"

// 接口api
import { refund } from "@/api/refund.js"
// 导入组件

// ------定义变量------
const emit = defineEmits() //子组件获取父组件事件传值
const ruleFormRef = ref() //表单校验ref
const refundData = reactive({
  // 表单数据
  refundReason: "",
  questionDesc: "",
  applyerType: 2,
  orderDetailId: "",
})
// 获取父组件传值
const props = defineProps({
  // 弹层隐藏显示
  dialogFormVisible: {
    type: Boolean,
    default: false,
  },
  // 数据
  baseData: {
    type: Object,
    default: {},
  },
})
// 将id赋予要提交的数据
watch(props, () => {
  refundData.orderDetailId = props.baseData.id
})
// 表单校验
const rules = reactive({
  refundReason: [
    { required: true, message: "退款原因为空，请选择退款原因", trigger: "change" },
  ],
  questionDesc: [
    {
      required: true,
      message: "退款说明为空，请输入退款说明",
      trigger: "blur",
    },
  ],
})
// ------定义方法------
// 搜索
// 退款审批
const handleSubmit = async () => {
  // 表单校验
  ruleFormRef.value.validate((valid) => {
    if (valid) {
      // 退款接口
      refund(refundData)
        .then((res) => {
          if (res.code === 200) {
            ElMessage({

              message: "申请成功!",
              type: "success",
              showClose:false,
            })
            ruleFormRef.value.resetFields() //清空表单数据
            handleClose() //关闭弹层
            getlist() //刷新列表
          } else {
            ElMessage({

              message: res.msg,
              type: "error",
              showClose:false,
            })
            ruleFormRef.value.resetFields() //清空表单数据
            handleClose() //关闭弹层
          }
        })
        .catch((err) => { })
    } else {
    }
  })

}
// 定义radio框的值,退款说明
const radioValue = [
  { label: "由于技术故障引发课程无法学习", value: 1 },
  { label: "未使用优惠券", value: 2 },
  { label: "个人原因（个人网络原因、计划有变、账号异常等）", value: 3 },
  { label: "课程内容不感兴趣", value: 4 },
  { label: "其他", value: 5 },
]
const getlist = inject("getList") //刷新父组件
// 关闭弹层
const handleClose = () => {
  ruleFormRef.value.resetFields() //清空表单数据
  emit("handleClose")
  getlist()
};
</script>
<style lang="scss" scoped>
:deep(.el-radio-group) {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}
:deep(.el-input__count) {
  height: 36%;
  font-weight: 400;
  font-size: 12px;
  color: #b5abab;
}
:deep(.el-textarea) {
  .el-textarea__inner {
    height: 114px;
  }
}
</style>