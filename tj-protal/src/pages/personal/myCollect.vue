<!-- 我的收藏 -->
<template>
  <div class="myCollect bg-wt">
    <div class="content">功能研发中, 敬请期待！！</div>
  </div>
</template>
<script setup>

/** 数据导入 **/
import { computed, onMounted, ref } from "vue";
import { ElMessage } from "element-plus";
import { getClassDetails, getClassTeachers, getClassList } from "@/api/classDetails.js";
import { getCourseLearning } from "@/api/class.js";
import { useRoute } from "vue-router";
import { dataCacheStore } from "@/store"
import LeftNav from "./components/LeftNav.vue";

// 组件导入
// import LeftNav from "./components/LeftNav.jsx";

const route = useRoute()
const store = dataCacheStore()

// 课程目录
const classListData = ref([])
const baseClassTeacherData = ref([])

// mounted生命周期
onMounted(async () => {
 
});

/** 方法定义 **/

</script>
<style lang="scss" src="./index.scss"> </style>
