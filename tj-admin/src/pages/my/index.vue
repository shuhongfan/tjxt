<template>
  <div class="contentBox">
    <div class="bg-wt radius marg-tp-20">
      <div class="detailBox">
        <!-- 标题 -->
        <div class="tit">
          <span>个人信息</span>
          <!-- <el-tooltip
          class="tooltipitem"
          effect="light"
          content="用户名称、手机号、角色请联系管理员进行修改"
          visible-arrow
          placement="right"
          popper-class="popperClass"
        >
          <span class="tooltipIcon"></span>
        </el-tooltip> -->
          <span class="tooltipIcon">
            <span class="hover"
              >用户名称、手机号、角色请联系管理员进行修改</span
            >
          </span>
        </div>
        <!-- 主体内容 -->
        <!-- 使用Teacher组件 -->
        <Teacher
          ref="teacher"
          :teacherData="teacherData.data"
          @editTeacher="editTeacher"
          @handleSaveTeacher="handleSaveTeacher"
          v-if="userInfo.data.type == 1"
        ></Teacher>
        <!-- 使用User组件 -->
        <Staffs
          ref="user"
          :staffsData="staffsData.data"
          @editStaffs="editStaffs"
          @handleSaveUser="handleSaveUser"
          rulesuserpassWord
          v-if="userInfo.data.type == 2"
        ></Staffs>
      </div>
    </div>
    <div class="BoxBottom">
      <div class="btn">
        <el-button class="button buttonSub" @click="handleGetback"
          >取消</el-button
        >
        <el-button
          class="button primary"
          @click="rulesteacherpassWord"
          v-if="userInfo.data.type == 1"
          >保存并返回</el-button
        >
        <el-button
          class="button primary"
          @click="rulesuserpassWord"
          v-if="userInfo.data.type == 2"
          >保存并返回</el-button
        >
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted, watch } from "vue" //引入vue中的方法
import { useRouter } from "vue-router"
// 获取vuex存储数据
// 导入组件
import Teacher from "./components/teacher.vue" // 教师个人详情页
import Staffs from "./components/user.vue" // 用户个人详情页
// 接口api
import { getUserInfo, editCurrentUser } from "@/api/user.js"//老师接口
import { USER_KEY } from "@/config/global.js"//用户接口
import { ElMessage } from "element-plus"
// ------定义变量------
//初始化路由
const router = useRouter() //获取全局
const user = ref() //staffs组件
const rulesuserpassWord = () => {
  // 使用staff组件中的rulespassWord方法
  user.value.rulespassWord()
}
const teacher = ref() //teacher组件
// 定义一个布尔值，判断子组件的回调
const isEdit = ref(false)
const rulesteacherpassWord = async () => {
  // 使用teacher组件中的rulespassWord方法
  teacher.value.rulespassWord()
}
// 老师基本信息
const teacherData = reactive({
  data: {}
})
// 用户基本信息
const staffsData = reactive({
  data: {}
})
// 当前登录用户存储信息
const userInfo = reactive({
  data: {}
})
// 接收子组件传过来的值teachereditData
// 定义数组
// 接收老师组件传过来的值
const teachereditData = reactive({
  data: {}
})
// 接收用户组件传过来的值
const staffseditData = reactive({
  data: {}
})
// 将子组件传过来的值赋值给teachereditData
const editTeacher = (val) => {
  teachereditData.data = val
  // 待赋值完成后，将isEdit改为true
  isEdit.value = true
}
// 将子组件传过来的值赋值给staffseditData
const editStaffs = (val) => {
  isChaseUser.value = true
  staffseditData.data = val
}
// ------生命周期------
onMounted(() => {
  userinfoDetail()
})
watch(userInfo, (val) => {
  if (userInfo.data.type == 1) {
    getTeacherDetail() //userInfo.data.type == 1时，用户身份为老师，调用老师接口
  } else if (userInfo.data.type == 2) {
    getStaffsDetail() //userInfo.data.type == 2时，用户身份为用户，调用用户接口
  }
})
// 获取教师详情信息
const getTeacherDetail = async () => {
  await getUserInfo()
    .then((res) => {
      if (res.code === 200) {
        teacherData.data = res.data
      }
    })
    .catch((err) => { })
}
// 获取当前登录用户详情信息
const userinfoDetail = async () => {
  // 获取当前登录用户存储在LOCALSTORAGE中的信息
  userInfo.data = JSON.parse(sessionStorage.getItem(USER_KEY))
}
//修改教师个人信息
const handleSaveTeacher = async () => {
  // 判断是否收到子组件传过来的值
  if (isEdit.value == true) {
    isEdit.value == false //将布尔值改为false,防止重复提交和第二次修改后不进行判断
    // 当teachereditData.data中的intro、job、photo中任意一向和teacherData.data中的intro、job、photo中任意一向不相等时，说明非密码区域内容有变化
    if (teachereditData.data.intro !== teacherData.data.intro || teachereditData.data.job !== teacherData.data.job || teachereditData.data.photo !== teacherData.data.photo) {
      editCurrentUser(teachereditData.data)
        .then((res) => {
          if (res.code === 200) {
            ElMessage({
              message: "修改成功",
              type: "success",
              showClose: false,
            })
            // 修改成功后重新获取教师详情信息
            getTeacherDetail()
            // 清空teachereditData.data
            teachereditData.data = {}
            router.go(-1)
          }
        })
        .catch((err) => { })
    } else {
      // 当teachereditData.data中的password和oldPassword都有值，说明密码区域内容有变化执行修改操作
      if (teachereditData.data.oldPassword && teachereditData.data.password) {
        editCurrentUser(teachereditData.data)
          .then((res) => {
            if (res.code === 200) {
              ElMessage({

                message: "恭喜你，操作成功！",
                type: "success",
                showClose: false,
              })
              router.go(-1)
            } else {
              ElMessage({

                message: "请填写正确信息!",
                type: "error",
                showClose: false,
              })
            }
          })
      } else {
        ElMessage({

          message: "请填写正确信息!",
          type: "error",
          showClose: false,
        })
      }
    }
  }
}

// 修改用户个人信息
const isChaseUser = ref(false) //判断是否修改用户信息，false为未修改，true为修改
const handleSaveUser = async () => {
  if (!isChaseUser.value) {
    return
  }
  // 调用Staffs组件中的rulespassWord方法，验证密码是否符合规则
  rulesuserpassWord()
  if (staffseditData.data.oldPassword && staffseditData.data.password) {
    await editCurrentUser(staffseditData.data)
      // 成功或失败时出现提示
      .then((res) => {
        if (res.code === 200) {
          ElMessage({

            message: "密码修改成功",
            type: "success",
            showClose: false,
          })
          router.go(-1)
        } else {
          ElMessage({

            message: res.data.msg,
            type: "error",
            showClose: false,
          })
        }
      })
      .catch((err) => { })
  } else {
    ElMessage({

      message: "请填写正确信息!",
      type: "error",
      showClose: false,
    })
  }
}

// 获取用户详情信息
const getStaffsDetail = async () => {
  await getUserInfo()
    .then((res) => {
      if (res.code === 200) {
        staffsData.data = res.data
      }
    })
    .catch((err) => { })
}
// 返回
const handleGetback = () => {
  router.push({ path: "/main" })
};
</script>
<style lang="scss" scoped>
.marg-tp-20 {
  border-bottom-left-radius: 0;
  border-bottom-right-radius: 0;
}
.detailBox {
  padding-left: 65px;
  padding-top: 41px;

  .tit {
    margin-bottom: 10px;
    margin-left: 45px;
  }
}
.BoxBottom {
  height: 100px;
  border-top: 1px solid #f5efee;
  margin-bottom: 20px;
  background: #ffffff;
  border-radius: 0 0 12px 12px;
  .btn {
    // 让按钮居中
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .button {
    width: 138px;
    height: 40px;
  }
}
.tooltipIcon {
  background: url(@/assets/btn-xiangqing.png) no-repeat;
  background-size: contain;
  display: inline-block;
  width: 19px;
  height: 19px;
  position: absolute;
  left: 432px;
  top: 129px;
  .hover {
    position: absolute;
    display: none;
    z-index: 9;
    left: -20px;
    top: 30px;
    width: 280px;
    border-radius: 6px;
    line-height: 40px;
    padding: 0 10px;
    background: #ffffff;
    box-shadow: 0 0 8px 1px rgba(0, 0, 0, 0.05);
    font-weight: 400;
    font-size: 12px;
    color: #332929;
    &::after {
      position: absolute;
      background: #ffffff;
      top: -5px;
      left: 23px;
      z-index: -1;
      content: '';
      width: 10px;
      height: 10px;
      transform: rotate(45deg);
      box-shadow: 0 0 8px 1px rgba(0, 0, 0, 0.05);
    }
    &::before {
      position: absolute;
      content: '';
      background: #fff;
      width: 50px;
      height: 10px;
      top: 0px;
      left: 10px;
    }
  }
  &:hover .hover {
    display: block;
  }
}
</style>
<style lang="scss">
.el-popper.is-light {
  box-shadow: 0 0 8px 1px rgba(0, 0, 0, 0.05);
  .el-popper__arrow {
    box-shadow: 0 0 8px 1px rgba(0, 0, 0, 0.05);
  }
}
</style>

