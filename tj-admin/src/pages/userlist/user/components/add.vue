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
      label-width="150px"
      class="demo-ruleForm"
      algin="right"
    >
      <el-form-item label="后台用户名称：" prop="name">
        <el-input
          v-model="fromData.name"
          placeholder="请输入"
          @input="nameTextInput"
          maxlength="20"
        />
        <span class="numText" :class="nameNumVal === 0 ? 'tip' : ''"
          >{{ nameNumVal }}/20</span
        >
      </el-form-item>
      <el-form-item label="后台用户手机号：" prop="cellPhone">
        <el-input
          v-model="fromData.cellPhone"
          placeholder="请输入"
          @blur="checkuserphone"
        />
      </el-form-item>
      <!-- 选择角色,默认展示的数据和绑定为fromData中的rolename数据 -->
      <el-form-item label="选择角色：" prop="roleId">
        <el-select
          v-model="fromData.roleId"
          clearable
          style="width: 371px"
          placeholder="请选择"
          @change="roleChange"
        >
          <el-option
            v-for="item in roleNames"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          ></el-option>
        </el-select>
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
import { ref, reactive, nextTick, watch, onMounted } from "vue"
import { ElMessage } from "element-plus"
// 控制字节数
import { validateLength } from "@/utils/index.js"
import {
  validateuserPhone,
  validateuser,
} from "@/utils/validate.js"
// 接口api
import { getRoleList, checkPhone } from "@/api/staffs.js"
import { editUser, saveUser} from "@/api/user.js"

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
})
watch(
  () => props.fromData,
  (newValue, oldValue) => {
    // 课程名称默认的字数
    const value = validateLength(newValue.name, 20);
    nameNumVal.value = value.numVal;
  }
);
// ------定义变量------
const emit = defineEmits() //子组件获取父组件事件传值
const ruleFormRef = ref() //表单校验ref
const roleNames = reactive([]) //角色列表
let isPhone = ref() //手机号是否重复
const checkPhoneData = reactive({
  data: {
    cellPhone: "",
    type: 2,
  },
}) //校验手机号
// 创建数据
const nameNumVal = ref(0) //后台用户名称字数
// 表单校验
const rules = reactive({
  name: [
    {
      required: true,
      validator: validateuser,
      trigger: "blur",
    },
    {
      min: 2,
      message: "请至少输入2个字符",
      trigger: "blur",
    },
  ],
  cellPhone: [
    { required: true, validator: validateuserPhone, trigger: "blur" },
    // 新增时校验手机号是否重复,当isisPhone.value
    {
      validator: (rule, value, callback) => {
        // 延迟执行0.3s，防止重复校验和校验不准确
        setTimeout(() => {
          if (isPhone.value === true) {
            callback(new Error("后台用户手机号已存在，请重新输入"))
          } else {
            callback()
          }
        }, 300)
      },
      trigger: "blur",
    },
  ],
  roleId: [{ required: true, message: "选择角色为空，请选择角色", trigger: "change" }],
})
// 生命周期
onMounted(() => {
  getRoleList().then((res) => {
    roleNames.push(...res.data)
  })
})
// ------定义方法------
// 搜索
const handleSubmit = async () => {
  // 表单校验
  const valid = await ruleFormRef.value.validate()
  if (valid) {
    // 新增接口
    let params = {
      ...props.fromData,
      type: 1
    }


    if (props.fromData.id === undefined) {
      await saveUser(params)
        .then((res) => {
          if (res.code === 200) {
            ElMessage({

              message: "新增成功!",
              type: "success",
              showClose:false,
            })
            ruleFormRef.value.resetFields() //清空表单数据
            handleClose()
            // 刷新列表
            emit("getList")
            // 打开创建成功弹窗
            emit("handleSuccee")
            //   // 将params的内容传给父组件
            emit("getParams", params)

          } else {
            ElMessage({

              message: res.data.msg,
              type: "error",
              showClose:false,
            })
          }
        })
        .catch((err) => { })
    } else {
      // 编辑接口
      await editUser(params)
        .then((res) => {
          if (res.code === 200) {
            ElMessage({

              message: "编辑成功!",
              type: "success",
              showClose:false,
            })
            ruleFormRef.value.resetFields() //清空表单数据
            handleClose()
            // 刷新列表
            emit("getList")
          }
        })
        .catch((err) => { })
    }
  }
}
// 关闭弹层
const handleClose = () => {
  ruleFormRef.value.resetFields() //清空表单数据
  nameNumVal.value = 0
  emit("handleClose")
}
// 名字控制20个字符
const nameTextInput = () => {
  nextTick(() => {
    const value = validateLength(props.fromData.name, 20)
    props.fromData.name = value.val
    nameNumVal.value = value.numVal
  })
}
// 校验手机号是否重复
const checkuserphone = async () => {
  checkPhoneData.data.cellPhone = props.fromData.cellPhone
  await checkPhone(checkPhoneData.data)
    .then((res) => {
      if (res.code === 200) {
        isPhone.value = res.data.exists
      }
    })
    .catch((err) => { })
};

</script>
<style lang="scss" scoped>
.numText{
  top: 5px;
}
:deep(.el-select .el-input.is-focus .el-input__wrapper){
  box-shadow: none !important;
}
</style>
