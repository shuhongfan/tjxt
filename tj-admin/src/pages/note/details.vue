<!-- 笔记详情 -->
<template>
  <div class="contentBox">
    <div class="bg-wt radius marg-tp-20">
      <div class="detailBox">
        <!-- 基本信息 -->
        <BaseInfo
          ref="baseInfo"
          :fromData="fromData.value"
        ></BaseInfo>
        <!-- end -->
      </div>
    </div>
    <div class="bg-wt radius marg-tp-20">
      <div class="detailBox">
        <!-- 处理结果 -->
        <Result ref="result" @getOrderId="getOrderId" :baseData="fromData.value"></Result>
        <!-- end -->
        <!-- end -->
      </div>
      <div class="detailBox" style="padding-top:0">
        <div class="btn">
          <el-button class="button buttonSub" @click="handleGetback"
            >返回</el-button
          >
          <el-button
            class="button primary"
            v-preventReClick
            @click="handleSubmit('getback')"
            >保存并返回</el-button
          >
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted, watch, nextTick, onUnmounted } from "vue";
import { useRouter, useRoute } from "vue-router";
// 获取vuex存储数据
import { useUserStore } from "@/store";
// 删除弹出层
import Cancel from "@/components/Cancel/index.vue";
// 导入组件
// 接口api
import { getNoteDetails } from "@/api/question";
// 导入组件
// 基本数据
import BaseInfo from "./components/BaseInfo.vue";
// 处理结果
import Result from "./components/Result.vue";
// ------定义变量------
// ------vuex存储数据------
const store = useUserStore();
//初始化路由
const router = useRouter(); //获取全局
const route = useRoute(); //获取局部
const emit = defineEmits(); //子组件获取父组件事件传值
const baseInfo = ref(); // 基本信息ref
const result = ref(); // 处理结果ref
let noteId = route.params.id ? route.params.id : null; //保存基础信息时后端返回的课程目录
let dialogCancelVisible = ref(false); //控制删除弹层
let fromData = reactive({}); //编辑表单数据
// ------生命周期------
onMounted(() => {
  getDetailData(noteId);
  
});

// ------定义方法------
// 获取详情
const getDetailData = async (id) => {
  await getNoteDetails(id)
    .then((res) => {
      if (res.code === 200) {
        fromData.value = res.data;
        if(fromData.value.hidden){
          fromData.value.hidden=1
        }else{
          fromData.value.hidden=0
        }
        getCourseOrderData(fromData.value.orderDetailId);
      }
    })
    .catch((err) => {});
};

// 保存
const handleSubmit = async (str) => {
  // 保存基本信息
  result.value.handleSubmit(str);
};

// 获取保存基础信息时的课程id
const getOrderId = (val) => {
  router.push({
    path: "/order/refundDetails/"+val,
  });
  getDetailData(val);
};
// 返回
const handleGetback = (row) => {
  router.push({
    path: "/interactive/note",
  });
};
</script>
<style lang="scss" scoped>
.button{
  width: 130px;
}
.detailBox{
  border-top: 1px solid #F5EFEE;
  padding-right: 70px;
  padding-top: 20px;
  padding-bottom: 20px;
  .btn{
    margin-bottom: 10px;
  }
}
</style>
