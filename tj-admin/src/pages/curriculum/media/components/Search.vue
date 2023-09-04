<!--视频搜索-->
<template>
  <div class="bg-wt radius marg-tp-20">
    <div class="pad-30 searchForm">
      <el-form ref="ruleForm" :inline="true" :model="searchData">
        <p style="font-family: PingFangSC-Medium;font-weight: 500;font-size: 14px;color: #332929;margin-bottom: 8px;">视频名称</p>
        <el-row :gutter="12">
          <el-col :span="4">
            <el-form-item label="" prop="name" class="mediaSearch">
              <el-input
                placeholder="请输入"
                v-model="searchData.name"
                clearable
                :prefix-icon="Search"
                class="el-input w-50 m-2"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <div class="btn pd-tp-0">
              <el-button class="button primary" @click="handleSearch"
                >搜索</el-button
              >
            </div>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from "vue";
import { Calendar, Search } from '@element-plus/icons-vue'
// 公用数据
import { statusData } from "@/utils/commonData";
// 获取父组件值、方法
const props = defineProps({
  // 搜索对象
  searchData: {
    type: Object,
    default: () => ({}),
  },
});
// ------定义变量------
const emit = defineEmits(); //子组件获取父组件事件传值
const ruleForm = ref(); //定义搜索表单的ref
// ------定义方法------
// 搜索
const handleSearch = () => {
  emit("handleSearch");
};
</script>
<style lang="scss" scoped>
.btn{
  .button{
    display: flex;
  }
}
:deep(.el-input__prefix-inner){
  display: none;
}
:deep(.is-guttered ){
  min-width: 240px;
}
</style>