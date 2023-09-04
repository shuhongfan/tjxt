<template>
  <el-form
    :model="staffseditData"
    ref="ruleFormRef"
    :rules="rules"
    label-width="130px"
    class="demo-ruleForm"
  >
    <el-form-item label="头像：" prop="icon" class="el-form-icon">
      <div class="el-input-icon">
        <!-- <UploadImage
        @getFlag="getFlag"
        @getCoverUrl="getCoverUrl"
        @setUplad="setUplad"
        :upladImg="photoImg == '' ? photoImg : staffsData.icon"
        :isCourse="isCourse"
        ref="uploadImg"
      ></UploadImage> -->
        <!-- 当前版本暂不支持更换头像功能 -->
        <img
          :src="staffsData.icon"
          alt=""
          srcset=""
          class="icon"
        />
      </div>
    </el-form-item>
    <el-form-item label="用户名称：" prop="name">
      <div class="el-input">
        <span>{{ staffsData.name }}</span>
      </div>
    </el-form-item>
    <el-form-item label="用户手机号：" prop="cellPhone">
      <div class="el-input">
        <span>{{ staffsData.cellPhone }}</span>
      </div>
    </el-form-item>
    <el-form-item label="角色：">
      <div class="el-input">
        <span>{{ staffsData.roleName }}</span>
      </div>
    </el-form-item>
    <el-form-item label="密码：" class="passwordbody">
      <div class="password el-input">
        <el-input
          type="password"
          v-model="staffsData.cellPhone"
          placeholder="请输入"
          maxlength="20"
          disabled
        ></el-input>
      </div>
      <a
        href="###"
        onclick="return false;"
        style="color: #2080f7; margin-left: 20px"
        @click="editpassword"
        >修改密码</a
      >
    </el-form-item>
    <el-form-item label="旧密码：" v-show="isCourse" prop="oldPassword">
      <div class="el-input">
        <el-input
          type="password"
          v-model="staffseditData.oldPassword"
          placeholder="请输入原密码"
          @input="editStaffs"
          @blur="checkoldPassword"
        ></el-input>
      </div>
    </el-form-item>
    <el-form-item label="新密码：" v-show="isCourse" prop="password">
      <div class="el-input">
        <el-input
          type="password"
          v-model="staffseditData.password"
          placeholder="请输入新密码"
          @input="editStaffs"
        ></el-input>
      </div>
    </el-form-item>
    <el-form-item label="确认新密码：" v-show="isCourse" prop="checkpassword">
      <div class="el-input">
        <el-input
          type="password"
          v-model="staffseditData.checkpassword"
          placeholder="请再次输入新密码"
          @input="editStaffs"
        ></el-input>
      </div>
    </el-form-item>
  </el-form>
</template>

<script setup>
// 引用
import { ref, reactive, watch, computed } from "vue"
// 引入接口
import { CheckPassword } from "@/api/staffs.js" // 验证密码
import { useRouter } from "vue-router";
// 引用上传图片组件
const router = useRouter()
// 创建一个布尔值，控制密码修改的显示隐藏
const isCourse = ref(false)
let isPassword = ref() //用来检测密码是否重复
// 创建数组，其中内容为输入框中的内容
const staffseditData = reactive({
  oldPassword: "",
  password: "",
  checkpassword: "",
})
// 创建数组，其中内容为要上传修改的内容
const staffeditData = reactive({
  oldPassword: "",
  password: "",
})
// 表单校验ref
const ruleFormRef = ref() //表单校验ref
// 监听变化进行赋值
watch(staffseditData, () => {
  staffeditData.oldPassword = staffseditData.oldPassword
  staffeditData.password = staffseditData.password
})
// 定义emit方法，用于触发父组件中的方法
const emit = defineEmits()
// 引入父级传参
const props = defineProps({
  staffsData: {
    type: Object,
    default: {},
  },
})
// 将用户头像的值赋值给staffseditData用于传输修改内容
// watch(props,(news,olds) => {
//   staffseditData.photo = props.staffsData.icon;
//   console.log(props.staffseditData, "staffseditData");
// });
// 点击出现，再次点击隐藏
const editpassword = () => {
  isCourse.value = !isCourse.value
}
// 为密码框添加正则验证
const rules = computed(() => {
  return {
    oldPassword: [
      {
        required: true,
        message: "密码为空，请输入密码",
        trigger: "blur",
      },
      // 根据isPassword的值，验证是否与旧密码一致，若isPassword的值为true，则不一致，出错误文本提示：旧密码不正确，请重新输入；
      // 若isPassword的值为false，则一致，不出错误文本提示
      {
        validator: (rule, value, callback) => {
          setTimeout(() => {
            // 延迟执行0.3s，防止重复校验和校验不准确
            if (isPassword.value === false) {
              callback(new Error("旧密码不正确，请重新输入"))
            } else {
              callback()
            }
          }, 300)
        },
        trigger: "blur",
      },
    ],
    password: [{
      required: true,
      message: '密码为空，请输入密码',
      trigger: 'blur'
    },
    { min: 6, max: 16, message: '密码格式错误，请重新输入', trigger: "blur" },
    {
      validator: (rule, value, callback) => {
        if (value == staffseditData.oldPassword) {
          callback(new Error("新密码不能与原密码一致，请重新输入"))
        } else {
          callback()
        }
      },
      trigger: "blur",
    },
    ],
    checkpassword: [
      {
        required: true,
        message: "密码为空，请输入密码",
        trigger: "blur",
      },
      {
        validator: (rule, value, callback) => {
          if (value !== staffseditData.password) {
            callback(new Error("新密码与确认新密码不一致"))
          } else {
            callback()
          }
        },
        trigger: "blur",
      },
    ],
  }
})
// 定义事件，用于传递staffseditData的内容给父组件
const editStaffs = () => {
  // 将staffseditData中的内容传递给父组件
  emit("editStaffs", staffeditData)
}
// 定义事件，用于校验当前所有的密码框格式是否正确
const rulespassWord = () => {
  if (!isCourse.value){
    router.go(-1)
  }
  // 校验当前所有的密码框格式是否正确
  ruleFormRef.value.validate((valid) => {
    if (valid) {
      emit("handleSaveUser", staffeditData)
    } else {
      // 清除密码框中的内容
      staffseditData.oldPassword = ""
      staffseditData.password = ""
      staffseditData.checkpassword = ""
    }
  })
}
// 校验密码是否重复
const checkoldPassword = async () => {
  await CheckPassword(staffseditData.oldPassword)
    .then((res) => {
      if (res.code === 200) {
        isPassword.value = res.data.right
      }
    })
    .catch((err) => { })
}
// 向父组件宏显式暴露方法
defineExpose({
  rulespassWord
})
</script>
<style lang="scss" scoped>
:deep(.el-input__count) {
  height: 36%;
  font-weight: 400;
  font-size: 12px;
  color: #b5abab;
}
.uploadBox {
  font-weight: 400;
  font-size: 12px;
  color: #000;
  :deep(.avatar-uploader) {
    width: 89px !important;
    height: 89px !important;
    border-radius: 50%;
    overflow: hidden;

    .el-upload.el-upload {
      width: 89px !important;
      height: 89px !important;
      // 将图片的边框设置为圆形
      border-radius: 50%;
      overflow: hidden;
    }
  }
  :deep(.Prompttext) {
    display: none;
  }
}
.el-form-icon {
  position: absolute;
  right: 235px;
  top: 178px;
  .el-input-icon {
    font-weight: 400;
    font-size: 12px;
    color: #000;
    .icon {
      width: 89px !important;
      height: 89px !important;
      border-radius: 50%;
      overflow: hidden;
    }
    :deep(.avatar-uploader) {
      width: 89px !important;
      height: 89px !important;
      border-radius: 50%;
      overflow: hidden;

      .el-upload.el-upload {
        width: 89px !important;
        height: 89px !important;
        // 将图片的边框设置为圆形
        border-radius: 50%;
        overflow: hidden;
      }
    }
    :deep(.Prompttext) {
      display: none;
    }
  }
  :deep(.el-input__wrapper){
      padding-right: 0;
    }
}
</style>
<style lang="scss" scoped>
.el-input{
  width: 369px;
}
.el-form-item{
  margin-bottom: 10px;
}
.password{
  margin-top: 10px;
  }
</style>
<style lang="scss">
.passwordbody{
  label{
    margin-top: 10px;
  }
}
</style>