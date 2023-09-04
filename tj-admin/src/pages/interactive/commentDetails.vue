<!-- 评论问答详情 -->
<template>
  <div class="contentBox">
    <!-- 回答信息 -->
    <BaseInfoReplie
      ref="base"
      :baseData="baseData.value"
      :questionId="questionId"
      @setFolded="setFolded"
      @getList="getAnswersList"
      @getDetailData="getDetailData"
    ></BaseInfoReplie>
    <!-- end -->
    <!-- 评论列表 -->
    <div class="tableBox">
      <CommentItem
        :itemData="itemData"
        :questionId="questionId"
        :total="total"
        :loading="loading"
        :pageSize="searchData.pageSize"
        :baseData="baseData.value"
        @getList="getAnswersList"
        @handleSizeChange="handleSizeChange"
        @handleCurrentChange="handleCurrentChange"
      ></CommentItem>
    </div>
    <!-- end -->
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
// 接口api
import { getReplies,getAnswersDetails } from "@/api/question";
// 导入组件
// 问题信息
import BaseInfoReplie from "./components/BaseInfoReplie.vue";
// 评论列表
import CommentItem from "./components/CommentItem.vue";
// 获取vuex存储数据
import { useUserStore } from "@/store";

// ------定义变量------
// ------vuex存储数据------
const store = useUserStore();
//初始化路由
const router = useRouter(); //获取全局
const route = useRoute(); //获取局部
const emit = defineEmits(); //子组件获取父组件事件传值
const base = ref(); //定义回答信息ref
let total = ref(null); //数据总条数
const loading = ref(false); //加载数据
const repliesId = ref(route.params.id); //回答回答id
const questionId = ref(route.params.qId); //回答回答id
const baseData = reactive({}); //回答信息
let itemData = ref([]); //评论列表数据
let searchData = reactive({
  pageSize: 10,
  pageNo: 1,
  admin:true
}); //搜索对象

// ------生命周期------
onMounted(() => {
  // 问答id
  // baseData.value = JSON.parse(decodeURIComponent(route.query.row)); //获取问题列表页传递过来的回答详情
  repliesId.value = route.query.id;
  questionId.value = route.query.qId;
  getAnswersList(); //评论列表
  getAnswersDetail()
});
// ------定义方法------
// 获取回答详情
let getAnswersDetail = async () => {
  loading.value = true;
  await getAnswersDetails(repliesId.value).then((res) => {
    if (res.code === 200) {
      baseData.value = res.data
    }
  });
};
// 评论列表
let getAnswersList = async () => {
  loading.value = true;
  let params = {
    ...searchData,
    answerId: repliesId.value,
  };
  await getReplies(params).then((res) => {
    if (res.code === 200) {
      loading.value = false;

      itemData.value = res.data.list;
      total.value = res.data.total;
    }
  });
};
// 手动设置是显示还是隐藏
const setFolded = (val) => {
  baseData.value.folded = val;
};
// 设置每页条数
const handleSizeChange = (val) => {
  searchData.pageSize = val
  // 刷新列表
  getAnswersList()
}
// 当前页
const handleCurrentChange = (val) => {
  searchData.pageNo = val
  // 刷新列表
  getAnswersList()
}
</script>
<style src="./index.scss" lang="scss" scoped></style>
