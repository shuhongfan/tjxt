<!-- 问题信息 -->
<template>
  <div class="contentBox">
    <!-- 问题信息 -->
    <BaseInfo
        ref="base"
        :baseData="baseData.value"
        @getList="getAnswersList"
        @getDetailData="getDetailData"
    ></BaseInfo>
    <!-- end -->
    <!-- 回答列表 -->
    <div class="tableBox">
      <AnswerItem
          :itemData="itemData"
          :total="total"
          :loading="loading"
          :pageSize="searchData.pageSize"
          :baseData="baseData.value"
          @getList="getAnswersList"
          @handleSizeChange="handleSizeChange"
          @handleCurrentChange="handleCurrentChange"
      ></AnswerItem>
    </div>
    <!-- end -->
  </div>
</template>
<script setup>
import {ref, reactive, onMounted} from "vue"
import {useRouter, useRoute} from "vue-router"
// 接口api
import {getQuestionsDetails, getReplies} from "@/api/question"
// 导入组件
// 问题信息
import BaseInfo from "./components/BaseInfo.vue"
// 回答列表
import AnswerItem from "./components/AnswerItem.vue"
// 获取vuex存储数据
import {useUserStore} from "@/store"

// ------定义变量------
// ------vuex存储数据------
const store = useUserStore()
//初始化路由
const router = useRouter() //获取全局
const route = useRoute() //获取局部
const emit = defineEmits() //子组件获取父组件事件传值
const base = ref() //定义问题信息ref
let total = ref(null) //数据总条数
const loading = ref(false) //加载数据
const questionId = ref(route.params.id) //回答问题id
const baseData = reactive({}) //问题信息
let itemData = ref([]) //回答列表数据
let searchData = reactive({
  pageSize: 10,
  pageNo: 1,
  isAsc: false,
  sortBy: "id"
}) //搜索对象

// ------生命周期------
onMounted(() => {
  // 问答id
  questionId.value = route.params.id
  getDetailData()//问答详情
  getAnswersList()//回答列表
})
// ------定义方法------
// 获取详情
let getDetailData = async () => {
  await getQuestionsDetails(questionId.value).then((res) => {
    if (res.code === 200) {
      baseData.value = res.data
    }
  })
}
// 问答列表
let getAnswersList = async () => {
  loading.value = true
  let params = {
    questionId: questionId.value
  }
  await getReplies(params)
      .then((res) => {
        if (res.code === 200) {
          loading.value = false

          itemData.value = res.data.list
          total.value = res.data.total
        }
      })
};

</script>
<style src="./index.scss" lang="scss" scoped></style>
<style lang="scss" scoped>
.contentBox {
  margin-bottom: 20px;
}
</style>
