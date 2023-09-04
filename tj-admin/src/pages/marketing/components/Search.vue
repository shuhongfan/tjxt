<!--优惠券搜索-->
<template>
  <div class="bg-wt radius marg-tp-20">
    <div class="pad-30 searchForm">
      <el-form ref="ruleForm" :inline="true" :model="searchData">
        <el-row :gutter="30">
          <el-col :span="6">
            <el-form-item label="优惠券类型" prop="type">
              <div class="el-input">
                <el-select
                  v-model="searchData.type"
                  clearable
                  placeholder="请选择"
                  style="width: 100%"
                >
                  <el-option
                    v-for="item in baseTypeData"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="优惠券状态" prop="status">
              <div class="el-input">
                <el-select
                  v-model="searchData.status"
                  clearable
                  placeholder="请选择"
                  style="width: 100%"
                >
                  <el-option
                    v-for="item in baseStatusData"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="优惠券名称" prop="name">
              <el-input
                placeholder="请输入"
                v-model="searchData.name"
                clearable
                class="el-input"
              />
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
import { ref, reactive, onMounted } from "vue";
// 公用数据
// 公用数据
import { couponsTypeData,couponsStatusData } from "@/utils/commonData";
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
const baseTypeData = couponsTypeData; //优惠卷类型
const baseStatusData = couponsStatusData; //优惠券状态
// ------定义方法------
// 搜索
const handleSearch = () => {
  emit("handleSearch");
};
// 重置搜索表单
const handleReset = (ruleForm) => {
  ruleForm.resetFields();
  emit('getList')
};
</script>