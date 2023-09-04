<!--订单搜索-->
<template>
  <div class="bg-wt radius marg-tp-20">
    <div class="pad-30 searchForm">
      <el-form ref="ruleForm" :inline="true" :model="searchData">
        <el-row :gutter="30">
          <el-col :span="6">
            <el-form-item label="子订单ID" prop="id">
              <div class="el-input">
                <el-input
                type="number"
                  placeholder="请输入"
                  clearable
                  v-model="searchData.id"
                />
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="订单状态" prop="status">
              <div class="el-input">
                <el-select
                  v-model="searchData.status"
                  clearable
                  placeholder="请选择"
                  style="width: 100%"
                >
                  <el-option
                    v-for="item in orderStatusData"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="退款状态" prop="refundStatus">
              <div class="el-input">
                <el-select
                  v-model="searchData.refundStatus"
                  clearable
                  placeholder="请选择"
                  style="width: 100%"
                >
                  <el-option
                    v-for="item in refundStatusData"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="支付方式" prop="payChannel">
              <div class="el-input">
                <el-select
                  v-model="searchData.payChannel"
                  clearable
                  placeholder="请选择"
                  style="width: 100%"
                >
                  <el-option
                    v-for="item in payChannelCodeData"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="30">
          <el-col :span="6">
            <el-form-item label="学员手机号" prop="mobile">
              <div class="el-input">
                <el-input
                  placeholder="请输入"
                  type="number"
                  clearable
                  v-model="searchData.mobile"
                />
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="下单时间" prop="datePicker">
              <div class="el-input">
                <el-date-picker
                  v-model="datePicker"
                  format="YYYY-MM-DD HH:mm:ss"
                  value-format="YYYY-MM-DD HH:mm:ss"
                  type="datetimerange"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  clearable
                  @change="handleDate($event)"
                >
                </el-date-picker>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
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
import { orderStatusData, refundStatusData,payChannelCodeData } from "@/utils/commonData";
// 接口
import { getTypeAll } from "@/api/api";
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
let datePicker = ref([]);//时间数据，获取默认时间的是数组
onMounted(() => {
});
// ------定义方法------

// 搜索
const handleSearch = () => {
  emit("handleSearch");
};
// 重置搜索表单
const handleReset = (ruleForm) => {
  ruleForm.resetFields();
  datePicker.value = [];//清空时间
  emit("handleReset"); //重置表单
  emit("getList"); //刷新列表
};
// 日期选择
const handleDate = (val) => {
  emit("getTime", val);
};
</script>