<!-- 登录页面 -->
<template>
  <div class="loginPass">
    <el-form
      ref="formRef"
      :model="fromData"
      :rules="rules"
      rules
      label-width="0px"
      class="demo-dynamic"
    >
      <el-form-item prop="username" label="">
        <el-input v-model="fromData.cellPhone" placeholder="请输入手机号" >
          <template #prepend>
            <img src="@/assets/icon_user.png" alt="">
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="password" label="" class="pass">
        <el-input
          type="pass"
          :show-password="true"
          v-model="fromData.password"
          placeholder="请输入密码"
        >
        <template #prepend>
          <img src="@/assets/icon_pass.png" alt="">
        </template>
      </el-input>
      </el-form-item>
      <el-form-item >
        <div class="bt button primary" @click="submitForm(formRef)">登 录</div>
      </el-form-item>
    </el-form>
  </div>
</template>
<script setup>
import { reactive, ref } from "vue"
import { ElMessage } from "element-plus"
import { useRouter } from "vue-router"
// api接口
import { userLogins, getUserInfo } from "@/api/user"
// 获取vuex存储数据
import { useUserStore } from "@/store"
// ------vuex存储数据------
const store = useUserStore()
// ------页面路由------
const router = useRouter()
// ------定义页面元素ref------
const formRef = ref()
// ------定义变量------
const checked = ref(false)
const fromData = reactive({
  cellPhone: "13500010002", //用户名
  password: "admin", //密码
  type: 1, //登录方式
})
// 手机号效验
const verifyPone = (rull, value, callback) => {
  const reg = /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/
  if (value == '') {
    callback(new Error('手机号为空，请输入手机号！'));
  } else if(!reg.test(value)){
    callback(new Error('手机号格式错误，请重新输入'));
  }
  callback()
}
const verfyPass = (rull, value, callback) => {
  if (value == '') {
    callback(new Error('密码为空，请输入密码！'));
  } else if(value.length < 4 || value.length > 16){
    callback(new Error('密码格式错误，请重新输入！'));
  }
  callback()
}
// 表单校验
const rules = reactive({
  cellPhone: [
    { validator: verifyPone, trigger: "blur" },
  ],
  password: [
    { validator: verfyPass, trigger: "blur" },
  ],
})
// 登录
const submitForm = (formEl) => {
  if (!formEl) return
  formEl.validate(async (valid) => {
    if (valid) {
      // 提交登录
      await userLogins(fromData)
        .then(async (res) => {
          if (res.code == 200) {
            // 用户token写入 pinia
            store.setToken(res.data)
            router.push("/main/index")
            ElMessage({
              message: '登录成功!',
              type: "success",
              showClose:false,
            })
            // 获取用户信息
            const data = await getUserInfo()
            if (data.code == 200) {
              // 记录到store 并调转到首页
              store.setUserInfo(data.data)
            }
            // 跳转到首页
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
      return false
    }
  })
};
</script>
<style lang="scss" scoped>
.loginPass {
  margin-top: 40px;
  img{
    width: 21px;
    height: 21px;
  }
 :deep(.el-input-group__prepend) {
    position: absolute;
    top: 1px;
    left: 1px;
    height: 38px;
    min-height: 38px;
    padding: 0px 14px;
    background: #EAE7E7;
    border: solid 1px #E9E9EB;
    box-shadow:none;
    border-radius: 6px 0 0 6px;
  }
  :deep(.el-input__wrapper){
    border-radius: 6px;
    padding-left: 60px;
    &:hover{
      border: solid 1px #E9E9EB
      
    }
  }
  :deep(.is-focus){
    border: solid 1px #E9E9EB
  }
}
</style>
