<!-- 我的考试 -->
<template>
  <div class="myExamWrapper">
    <div class="personalCards" v-if="myExamData != null">
      <CardsTitle class="marg-bt-20" title="我的考试" />
      <div v-if="count == 0" class="nodata">
        <Empty ></Empty>
      </div>
      <ExamTable v-if="count > 0" :data="myExamData"></ExamTable>
      <div class="pageination" v-if="count > 0">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="count"
          class="mt-4"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>
  </div>
</template>
<script setup>

/** 数据导入 **/
import { onMounted, ref, reactive } from "vue";
import { ElMessage } from "element-plus";
import { getExamList } from "@/api/class.js";

// 组件导入
import CardsTitle from './components/CardsTitle.vue'
import ExamTable from './components/ExamTable.vue'
import Empty from "@/components/Empty.vue";

// mounted生命周期
onMounted(async () => {
  // 查询我的考试记录
  getExamListData()
});

/** 方法定义 **/

// 查询我的考试记录
const myExamData = ref(null)
const count = ref(0)
const params = reactive({
  pageNo: 1,
  pageSize: 10,
})
// 查询我的考试记录
const getExamListData = async () => {
  await getExamList(params)
    .then((res) => {
      if (res.code == 200 && res.data != null){
        myExamData.value = res.data.list
        count.value = Number(res.data.total)
      }
    })
    .catch(() => {
      ElMessage({
        message: "最近学习数据请求出错！",
        type: 'error'
      });
    });
}

const handleSizeChange = (val) => {
  params.pageSize = val
  getExamListData()
}
const handleCurrentChange = (val) => {
  params.pageNo = val
  getExamListData()
}
</script>
<style lang="scss" src="./index.scss"> </style>
