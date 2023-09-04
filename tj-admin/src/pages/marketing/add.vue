<!-- 添加、编辑优惠券 -->
<template>
  <div class="contentBox">
    <div class="bg-wt radius marg-tp-20">
      <div class="detailBox" :key="keyTime">
        <!-- 标题 -->
        <div class="tit"><span>优惠券基本信息</span></div>
        <!-- end -->
        <!-- 基本信息 -->
        <BaseInfo
          ref="baseInfo"
          @getCourseId="getCourseId"
        ></BaseInfo>
        <!-- end -->
      </div>
      <div class="detailBox" :key="keyTime" style="border-top: 1px solid #e8e8e8; padding: 0;">
        <!-- 按钮 -->
        <div class="btn">
          <el-button class="button buttonSub" @click="handleCancel"
            >取消</el-button
          >
          <el-button
            class="button buttonSub"
            v-preventReClick
            @click="handleSubmit('getback')"
            >保存并返回</el-button
          >
          <el-button
            v-preventReClick
            class="button primary"
            @click="handleNext"
            >保存并继续</el-button
          >
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted, watchEffect, nextTick, onUnmounted } from "vue";
import { useRouter, useRoute } from "vue-router";
// 获取vuex存储数据
import { useUserStore } from "@/store";
// 删除弹出层
import Cancel from "@/components/Cancel/index.vue";
// 导入组件
// 接口api
import { getDetails } from "@/api/curriculum";
// 导入组件
// 基本数据
import BaseInfo from "./components/BaseInfo.vue";
// ------定义变量------
// ------vuex存储数据------
const store = useUserStore();
//初始化路由
const router = useRouter(); //获取全局
const route = useRoute(); //获取局部
const emit = defineEmits(); //子组件获取父组件事件传值
const deleteText = ref("此操作将不会保存您所做的更改，是否继续？"); //需要删除的提示内容
const baseInfo = ref(); // 基本信息ref
let courseId = route.params.id ? route.params.id : null; //保存基础信息时后端返回的课程目录
let dialogCancelVisible = ref(false); //控制删除弹层
let keyTime = ref(null);  //解决组件强制重新加载的问题
// ------生命周期------
onMounted(() => {
});
// 保存并继续时返回到重新添加页面，这时候会有残余的数据，所以要强制刷新加载页面
// 值发生改变时说明不是同一个组件，将会进行重新加载和渲染
watchEffect(() => {
  if (route.path === "/marketing/add/null") {
    keyTime.value = new Date().getTime();
  }
});
// ------定义方法------
// 保存
const handleSubmit = async (str) => {
  // 保存基本信息
    baseInfo.value.handleSubmit(str);
};

// 下一步
const handleNext = () => {
  // 保存基本信息
    baseInfo.value.handleSubmit();
};

// 获取保存基础信息时的课程id
const getCourseId = (val) => {
  courseId = val;
};
// 取消
const handleCancel = async () => {
  handleClose();
  router.push({
    path: "/marketing/index",
  });
};
// 关闭删除弹层
const handleClose = () => {
  dialogCancelVisible.value = false;
};

</script>
<style lang="scss" scoped>
.btn {
  padding: 30px 0;
  text-align: center;
  margin-bottom: 20px;
}
:deep(.el-form-item__label) {
  color: #332929;
}
:deep(.numText) {
  color: #B5ABAB;
}
.el-input__wrapper{
:deep(.el-input__inner){
  color: #332929;
  &::placeholder {
    color: #332929;
  }
}
}
:deep(.el-input .el-input__wrapper .el-input__inner){
  color: #332929;
}
.btn .button{
  width: 130px;
}
</style>

