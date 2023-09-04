<template>
  <el-dialog
    v-model="dialogFormVisible"
    :title="title"
    :before-close="handleClose"
  >
    <el-form
      :model="fromData"
      ref="ruleFormRef"
      :rules="rules"
      label-width="105px"
      class="demo-ruleForm"
    >
      <el-form-item label="学员昵称：" prop="name">
        <div class="el-input">
          <el-input
            v-model="fromData.name"
            placeholder="请输入"
            @input="nameTextInput"
          />
          <span class="numText" :class="nameNumVal === 0 ? 'tip' : ''"
            >{{ nameNumVal }}/20</span
          >
        </div>
      </el-form-item>
      <el-form-item label="学员手机号：" prop="cellPhone">
        <el-input v-model="fromData.cellPhone" placeholder="请输入" />
      </el-form-item>
      <!-- 性别 -->
      <el-form-item label="性别：" prop="gender" algin="center">
        <el-radio-group v-model="fromData.gender">
          <el-radio
            v-for="(item, index) in sexData"
            :key="index"
            :label="index"
            >{{ item.label }}</el-radio
          >
        </el-radio-group>
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
import { ref, reactive, nextTick, watch, watchEffect, onMounted } from "vue";
import { ElMessage } from "element-plus";
// 控制字节数
import { validatestudentsPhone, validatestudents } from "@/utils/validate.js";
// 引用公用数据中的性别
import { sexData } from "@/utils/commonData";
// 控制字节数
import { validateLength } from "@/utils/index.js";
// 接口api
import { editUser } from "@/api/user.js";
// 获取父组件值、方法
const props = defineProps({
  // 弹层隐藏显示
  dialogFormVisible: {
    type: Boolean,
    default: false,
  },
  //标题
  title: {
    type: String,
    default: "",
  },
  // 表单数据
  fromData: {
    type: Object,
    default: () => ({}),
  },
});
// 监听，获取默认字数
watch(
  () => props.fromData,
  (newValue, oldValue) => {
    // 课程名称默认的字数
    const value = validateLength(newValue.name, 20);
    nameNumVal.value = value.numVal;
  }
);
// ------定义变量------
const emit = defineEmits(); //子组件获取父组件事件传值
const ruleFormRef = ref(); //表单校验ref
let nameNumVal = ref(0); //学员字数显示
let flag = ref(false); //用来控制上传图片是否校验成功
// 表单校验
const rules = reactive({
  name: [
    {
      required: true,
      validator: validatestudents,
      trigger: "blur",
    },
    {
      min: 2,
      message: "请至少输入2个字符",
      trigger: "blur",
    },
  ],
  cellPhone: [
    { required: true, validator: validatestudentsPhone, trigger: "blur" },
  ],
  gender: [{ required: true, message: "请选择性别", trigger: "blur" }],
});
// ------定义方法------
// 生命周期
onMounted(() => {
  nameTextInput();
});
// 搜索
const handleSubmit = async () => {
  // 表单校验
  const valid = await ruleFormRef.value.validate();
  if (valid) {
    // 新增接口
    let params = {
      ...props.fromData,
      type: 2
    };
    // 编辑接口
    await editUser(params)
      .then((res) => {
        if (res.code === 200) {
          ElMessage({

            message: "编辑成功!",
            type: "success",
            showClose: false,
          });
          ruleFormRef.value.resetFields(); //清空表单数据
          handleClose();
          // 刷新列表
          emit("getList");
        }
      })
      .catch((err) => {});
  }
};
// 关闭弹层
const handleClose = () => {
  ruleFormRef.value.resetFields(); //清空表单数据
  flag.value = false;
  emit("handleClose");
};
// 名字控制20个字符
const nameTextInput = () => {
  nextTick(() => {
    const value = validateLength(props.fromData.name, 20);
    props.fromData.name = value.val;
    nameNumVal.value = value.numVal;
  });
};
</script>
<style lang="scss" scoped>
:deep(.el-dialog .el-form-item__label) {
  align-items: center;
}
</style>
