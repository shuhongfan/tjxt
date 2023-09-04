<!-- 个人中心 - 左侧导航  -->
<template>
  <div class="planTable fx-sb fx-1">
    <el-table :data="data" height="247" style="width: 100%">
      <el-table-column center label="课程">
        <template #default="scope">
          <div>{{scope.row.courseName}}</div>
        </template>
      </el-table-column>
      <el-table-column center label="本周进度" align="center" width="100" >
         <template #default="scope">
          <div>{{scope.row.weekLearnedSections}}/{{scope.row.weekFreq}}</div>
        </template>
      </el-table-column>
      <el-table-column prop="address" align="center" label="课程进度" width="100" >
        <template #default="scope">
          <div>{{scope.row.learnedSections}}/{{scope.row.sections}}</div>
        </template>
      </el-table-column>
      <el-table-column prop="address" align="center" label="学习时间" width="180" >
        <template #default="scope">
          <div>{{scope.row.latestLearnTime}}</div>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="120">
        <template #default="scope">
          <div class="font-bt1" @click="() => $router.push({path: '/learning/index', query: {id: scope.row.courseId}})">去学习</div>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
<script setup>
// 介绍父组件传来的标题
defineProps({
  data:{
    type: Array,
    default: []
  }
})  
</script>
<style lang="scss" scoped>
.planTable{
  margin-top: 30px;
  border: solid 1px #ebeef5;
  border-bottom: none;
  line-height: 30px;
  img{
    width: 236px;
    height: 132px;
    border-radius: 4px;
  }
  .info{
    line-height: 30px;
    font-size: 14px;
    .tit{
      font-size: 20px;
      font-weight: 500;
      line-height: 40px;
    }
  }
}
</style>
