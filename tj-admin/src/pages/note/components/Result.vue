<!--审批处理结果-->
<template>
  <div class="orderInfo">
    <!-- 标题 -->
    <div class="tit"><span>审批操作</span></div>
    <!-- end -->
    <!-- <div v-if="baseData.refundStatus===1"> -->
    <div>
      <el-form
        :model="baseData"
        ref="ruleFormRef"
        :rules="rules"
        label-width="150px"
        class="demo-ruleForm"
      >
        <el-form-item label="用户端是否展示：" prop="approvalType">
          <el-radio-group v-model="baseData.hidden">
            <el-radio
              v-for="item in showStautsData"
              :key="item.value"
              :label="item.value"
              >{{ item.label }}</el-radio
            >
          </el-radio-group>
        </el-form-item>
        <!-- <el-form-item label="备注：" prop="remark" class="textInput">
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
        </el-form-item> -->
      </el-form>
    </div>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted, watch, nextTick, watchEffect } from "vue";
import { ElMessage } from "element-plus";
import { useRouter, useRoute } from "vue-router";
//公用数据
import { showStautsData } from "@/utils/commonData.js";
// 控制字节数
import { validateTextLength } from "@/utils/index.js";
// 接口
import { setNotesShow, setNotesFolded } from "@/api/question";
// ------定义变量------
// 获取父组件值、方法
const props = defineProps({
  // 订单信息
  baseData: {
    type: Object,
    default: () => ({}),
  },
});
//初始化路由
const router = useRouter(); //获取全局
const route = useRoute(); //获取局部
const emit = defineEmits(); //子组件获取父组件事件传值
// let fromData = ref({
//   approvalType: 1,
// }); //表单数据
const ruleFormRef = ref(); //表单校验ref
let remarkNumVal = ref(0); //备注字数显示
let detailNumVal = ref(0); //审批意见字数显示
let refundId = ref(null); //当前退款id
// 表单校验
const rules = reactive({
  folded: [
    {
      required: true,
      message: "审批结果为空，请选择审批结果",
      trigger: "change",
    },
  ],
});
// ------生命周期------
onMounted(() => {
});
// ------定义方法------
const handleSubmit = async (str) => {
  // 表单校验
  const valid = await ruleFormRef.value.validate();
  if (valid) {
    if (props.baseData.hidden === 1) {
      handleShow(route.params.id);
    } else {
      handleHide(route.params.id);
    }
    // 返回笔记列表页
    router.push({
      path: `/interactive/note`,
    });
  }
};
// 显示
const handleShow = async (id) => {
  await setNotesShow(id)
    .then((res) => {
      if (res.code === 200) {
        ElMessage({

          message: msg.value,
          type: "success",
          showClose:false,
        });
      }
    })
    .catch((err) => {});
};
// 隐藏
const handleHide = async (id) => {
  await setNotesFolded(id)
    .then((res) => {
      if (res.code === 200) {
        ElMessage({

          message: msg.value,
          type: "success",
          showClose:false,
        });
      }
    })
    .catch((err) => {});
};
// // 备注字数限制
// const useRemarkInput = () => {
//   nextTick(() => {
//     let data = fromData.value;
//     const value = validateTextLength(data.remark, 100);
//     data.remark = value.val;
//     remarkNumVal.value = value.numVal;
//   });
// };
// 向父组件暴露方法
defineExpose({
  handleSubmit,
});
</script>
<style lang="scss" scoped>
.el-form-item{
  margin-bottom: 0;
}
</style>

