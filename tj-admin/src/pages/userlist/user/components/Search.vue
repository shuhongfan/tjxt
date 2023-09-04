<template>
  <div class="bg-wt radius marg-tp-20">
    <div class="pad-30 searchForm">
      <el-form ref="ruleForm" :inline="true" :model="searchData">
        <el-row :gutter="30">
          <el-col :span="6">
            <el-form-item label="后台用户状态" prop="status">
              <div class="el-input">
                <el-select
                  v-model="searchData.status"
                  clearable
                  placeholder="请选择"
                  style="width: 100%"
                >
                  <el-option
                    v-for="item in statusData"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="后台用户名称" prop="name">
              <el-input
                placeholder="请输入"
                v-model="searchData.name"
                clearable
                class="el-input"
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="后台用户手机号" prop="phone">
              <el-input placeholder="请输入" clearable v-model="searchData.phone" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <div class="btn">
              <el-button class="button primary" @click="handleSearch"
                >搜索</el-button
              >
              <el-button class="button buttonSub" @click="handleReset(ruleForm)"
                >重置</el-button
              >
            </div>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>
<script setup>
import { ref } from "vue";
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
  emit("handleSearch");//触发父组件的handleSearch方法，执行搜索
};
// 重置搜索表单
const handleReset = (ruleForm) => {
  ruleForm.resetFields(); //重置表单
  emit('getList') // 重置搜索表单后，重新获取列表，刷新列表
};
</script>