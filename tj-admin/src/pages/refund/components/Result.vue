<!--审批处理结果-->
<template>
  <div class="orderInfo">
    <!-- 标题 -->
    <div class="tit"><span>处理结果</span></div>
    <!-- end -->

    <!-- <div v-if="baseData.refundStatus===1"> -->
    <div v-if="baseData.status === 1">
      <el-form
        :model="fromData"
        ref="ruleFormRef"
        :rules="rules"
        label-width="130px"
        class="demo-ruleForm"
      >
        <el-form-item label="审批结果：" prop="approveType">
          <el-radio-group v-model="fromData.approveType">
            <el-radio
              v-for="item in resultData"
              :key="item.value"
              :label="item.value"
              >{{ item.label }}</el-radio
            >
          </el-radio-group>
        </el-form-item>
        <el-form-item
          label="审批意见："
          prop="approveOpinion"
          class="textInput"
          v-if="fromData.approveType === 2"
        >
          <el-input
            v-model="fromData.approveOpinion"
            type="textarea"
            placeholder="请输入"
            resize="none"
            @input="detailTextInput"
          />
          <span class="numText" :class="detailNumVal === 0 ? 'tip' : ''"
            >{{ detailNumVal }}/100</span
          >
        </el-form-item>
        <el-form-item label="备注：" prop="remark" class="textInput">
          <el-input
            v-model="fromData.remark"
            type="textarea"
            placeholder="请输入"
            resize="none"
            @input="useRemarkInput"
          />
          <span class="numText" :class="remarkNumVal === 0 ? 'tip' : ''"
            >{{ remarkNumVal }}/100</span
          >
        </el-form-item>
      </el-form>
    </div>
    <div v-else class="resultCon">
      <!-- <p><label>审批结果：</label>{{baseData.approveResult===1?'同意':'拒绝'}}</p> -->
      <p>
        <label>审批结果：</label>
        <span v-if="baseData.status === 2">无</span>
        <span
          v-if="
            baseData.status === 3 ||
            baseData.status === 5 ||
            baseData.status === 6
          "
          >同意退款</span
        >
        <span v-if="baseData.refundStatus === 4">拒绝退款</span>
      </p>
      <p v-if="baseData.approveResult !== 1">
        <label>审批意见：</label
        >{{ baseData.approveOpinion ? baseData.approveOpinion : "无" }}
      </p>
      <p>
        <label>备注：</label
        >{{
          baseData.remark === "" || baseData.remark === undefined
            ? "无"
            : baseData.remark
        }}
      </p>
    </div>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted, watch, nextTick, watchEffect } from "vue"
import { ElMessage } from "element-plus"
import { useRouter, useRoute } from "vue-router"
//公用数据
import { resultData } from "@/utils/commonData.js"
// 控制字节数
import { validateTextLength } from "@/utils/index.js"
// 接口
import { refundApproval, getNextNoApproval } from "@/api/refund"
// ------定义变量------
// 获取父组件值、方法
const props = defineProps({
  // 订单信息
  baseData: {
    type: Object,
    default: () => ({}),
  },
})
//初始化路由
const router = useRouter() //获取全局
const route = useRoute() //获取局部
const emit = defineEmits() //子组件获取父组件事件传值
let fromData = ref({}) //表单数据
const ruleFormRef = ref() //表单校验ref
let remarkNumVal = ref(0) //备注字数显示
let detailNumVal = ref(0) //审批意见字数显示
let refundId = ref(null) //当前退款id
// 表单校验
const rules = reactive({
  approveType: [
    {
      required: true,
      message: "审批结果为空，请选择审批结果",
      trigger: "change",
    },
  ],
  approveOpinion: [
    {
      required: true,
      message: "审批意见为空，请输入审批意见",
      trigger: "blur",
    },
  ],
})
// ------生命周期------
onMounted(() => { })
// ------定义方法------
const handleSubmit = async (str) => {
  // 表单校验
  const valid = await ruleFormRef.value.validate()
  if (valid) {
    //  获取购买周期
    let parent = {
      ...fromData.value,
      id: route.params.id,
    }

    await refundApproval(parent)
      .then((res) => {
        if (res.code === 200) {
          ElMessage({

            message: "恭喜你，操作成功！",
            type: "success",
            showClose:false,
          })
          ruleFormRef.value.resetFields()
          getNextOrder()
        } else {
          ElMessage({

            message: res.data.msg,
            type: "error",
            showClose:false,
          })
        }
      })
      .catch((err) => { })
    // 返回退款列表页
    if (str === "getback") {
      router.push({
        path: "/order/refund",
      })
    } else {
      // 返回并继续
      router.push({
        path: `/order/refundDetails/` + refundId.value,
      })
    }
  }
}
// 获取下一个待审批的退款订单
const getNextOrder = async () => {
  await getNextNoApproval()
    .then((res) => {
      if (res.code === 200) {
        let id=res.data.id
        if(id){
          emit("getOrderId", res.data.id)
        }else{
          emit('setDispose')
        }
        
      }
    })
    .catch((err) => { })
}
// 备注字数限制
const useRemarkInput = () => {
  nextTick(() => {
    let data = fromData.value
    const value = validateTextLength(data.remark, 100)
    data.remark = value.val
    remarkNumVal.value = value.numVal
  })
}
// 审批意见字数限制
const detailTextInput = () => {
  nextTick(() => {
    let data = fromData.value
    const value = validateTextLength(data.approveOpinion, 100)
    data.approveOpinion = value.val
    detailNumVal.value = value.numVal
  })
}
// 向父组件暴露方法
defineExpose({
  handleSubmit,
});
</script>
<style lang="scss" scoped>
.numText {
  color: #B5ABAB;
}
:deep(.el-form-item__label) {
  color: #332929;
}
:deep(.el-textarea textarea.el-textarea__inner){
  color: #332929;
  &::placeholder{
    color: #B5ABAB;
  }
}
</style>

