<!-- 详情 -->
<template>
  <div class="contentBox">
    <div class="bg-wt radius marg-tp-20">
      <div class="detailBox" style="padding-bottom: 10px ">
        <!-- 步骤条 -->
        <Steps :active="active" @getActive="getActive"></Steps>
        <!-- end -->
        <!-- 标题 -->
        <Title :active="active"></Title>
        <!-- end -->
        <!-- 基本信息 -->
        <BaseInfo
          v-if="active === 0"
          ref="baseInfo"
          @getActive="getActive"
          @getCourseId="getCourseId"
          @getStep="getStep"
        ></BaseInfo>
        <!-- end -->
        <!-- 课程目录 -->
        <Catalogue
          v-if="active === 1"
          ref="catalogues"
          :courseId="courseId"
          @getActive="getActive"
        ></Catalogue>
        <!-- end -->
        <!-- 课程目录 -->
        <Video
          v-if="active === 2"
          ref="video"
          :courseId="courseId"
          @getActive="getActive"
        ></Video>
        <!-- end -->
        <!-- 课程题目 -->
        <Topic
          v-if="active === 3"
          ref="topic"
          :courseId="courseId"
          @getActive="getActive"
        ></Topic>
        <!-- end -->
        <!-- 课程老师 -->
        <Tearch v-if="active === 4" ref="tearch" :courseId="courseId"></Tearch>
        <!-- end -->
      </div>
        <!-- 按钮 -->
      <div class="detailBox" style="border-top: 1px solid #F5EFEE;">
        <div class="btn">
          <el-button class="button buttonSub" @click="handleOpenCancel"
            >取消</el-button
          >
          <el-button class="button buttonSub" v-if="active !== 0" @click="handleBack"
            >上一步</el-button
          >
          <el-button
            class="button"
            v-preventReClick
            :class="active === 4 ? 'primary' : 'buttonSub'"
            @click="handleSubmit('getback')"
            >保存并返回</el-button
          >
          <el-button
            v-if="active !== 4"
            v-preventReClick
            class="button primary"
            @click="handleNext"
            >保存并下一步</el-button
          >
        </div>
      </div>

    </div>
    <!-- 删除 -->
    <Cancel
      :dialogVisible="dialogCancelVisible"
      :deleteText="deleteText"
      @handleCancel="handleCancel"
      @handleClose="handleClose"
    ></Cancel>
    <!-- end -->
  </div>
</template>
<script setup>
import {
  ref,
  reactive,
  onMounted,
  watch,
  watchEffect,
  nextTick,
  onUnmounted,
  onBeforeUpdate,
  onUpdated,
} from "vue";
import { useRouter, useRoute } from "vue-router";
// 获取vuex存储数据
import { useUserStore } from "@/store";
// 删除弹出层
import Cancel from "@/components/Cancel/index.vue";
// 导入组件
// 接口api
import { getDetails } from "@/api/curriculum";

// 导入组件
//步骤
import Steps from "./components/Steps.vue";
//标题
import Title from "./components/Title.vue";
// 基本数据

import BaseInfo from "./base/index.vue";
//课程目录
import Catalogue from "./catalogue/index.vue";
//  课程视频
import Video from "./video/index.vue";
//  课程题目
import Topic from "./topic/index.vue";
//  课程老师
import Tearch from "./tearch/index.vue";
// ------定义变量------
// ------vuex存储数据------
const store = useUserStore();
//初始化路由
const router = useRouter(); //获取全局
const route = useRoute(); //获取局部
const emit = defineEmits(); //子组件获取父组件事件传值
const deleteText = ref("此操作将不会保存您所做的更改，是否继续？"); //需要删除的提示内容
const baseInfo = ref(); // 基本信息ref
const catalogues = ref(); // 课程目录ref
const video = ref(); // 课程视频ref
const topic = ref(); // 课程题目ref
const tearch = ref(); // 课程老师ref
let active = ref(0); //当前step 步骤值
let courseId = route.params.id ? route.params.id : null; //保存基础信息时后端返回的课程目录
let dialogCancelVisible = ref(false); //控制删除弹层
let stepNumData = reactive([]); //当前能触发的step
// ------生命周期------
onMounted(() => {
  if (localStorage.getItem("active")) {
    active.value = Number(localStorage.getItem("active"));
  }
});
onUnmounted(() => {
  store.setStepActive(0);
  store.setStepNum(0);
});
// ------定义方法------
// 保存
const handleSubmit = async (str) => {
  if (active.value === 0) {
    // 保存基本信息
    baseInfo.value.handleSubmit(str);
  } else if (active.value === 1) {
    // 保存章节
    catalogues.value.handleSubmit(str);
  } else if (active.value === 2) {
    // 保存课程视频
    video.value.handleSubmit(str);
  } else if (active.value === 3) {
    // 保存课程题目
    topic.value.handleSubmit(str);
  } else {
    // 保存课程老师
    tearch.value.handleSubmit(str);
  }
};
// 上一步
const handleBack = () => {
  if (active.value > 0) {
    active.value--;
    store.setStepActive(active.value);
    router.push({
      path: `/curriculum/add/` + courseId,
    });
  }
};
// 下一步
const handleNext = () => {
  if (active.value === 0) {
    // 保存基本信息
    baseInfo.value.handleSubmit();
  } else if (active.value === 1) {
    // 保存章节
    catalogues.value.handleSubmit();
  } else if (active.value === 2) {
    // 保存课程视频
    video.value.handleSubmit();
  } else if (active.value === 3) {
    // 保存课程题目
    topic.value.handleSubmit();
  }
};
// 获取当前的步骤值
const getActive = (val) => {
  store.setStepActive(val);
  active.value = val;
};
// 获取保存基础信息时的课程id
const getCourseId = (val) => {
  courseId = val;
};
// 打开删除弹层
const handleOpenCancel = (row) => {
  dialogCancelVisible.value = true;
};
// 关闭删除弹层
const handleClose = () => {
  dialogCancelVisible.value = false;
};
// 确定删除
const handleCancel = async () => {
  handleClose();
  router.push({
    path: "/curriculum/index",
  });
};
// 从后台获取可以触发的step
const getStep = (val) => {
  let arr = [];
  for (let i = 0; i < val; i++) {
    arr.push(i);
  }
  stepNumData.value = arr;
};
</script>
<style src="./../index.scss" lang="scss" scoped></style>
<style lang="scss" scoped>
.btn {
  padding-top: 0px;
}
.detailBox{
  // margin-bottom: 15px;
}
:deep(.el-input .el-input__wrapper .el-input__inner){
  &::placeholder{
    color: #B5ABAB;
  }
}
:deep(.el-textarea textarea.el-textarea__inner){
  &::placeholder{
    color: #B5ABAB;
  }
}
// :deep(.el-input .el-input__wrapper .el-input__inner){
//   color: #332929;
// }
:deep(.el-textarea textarea.el-textarea__inner){
  color: #332929;
}
</style>
