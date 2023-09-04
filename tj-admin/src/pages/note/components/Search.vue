<!--笔记管理搜索-->
<template>
  <div class="bg-wt radius marg-tp-20">
    <div class="pad-30 searchForm">
      <el-form ref="ruleForm" :inline="true" :model="searchData">
        <el-row :gutter="30">
          <el-col :span="6">
            <el-form-item label="状态" prop="hidden">
              <div class="el-input">
                <el-select
                  v-model="searchData.hidden"
                  clearable
                  placeholder="请选择"
                  style="width: 100%"
                >
                  <el-option
                    v-for="item in showStautsData"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="课程名称" prop="noteName">
              <el-input
                placeholder="请输入"
                clearable
                v-model="searchData.name"
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="笔记发布时间" prop="datePicker">
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
import { ref, onMounted } from "vue"
// 公用数据
import { showStautsData } from "@/utils/commonData"
// 获取父组件值、方法
const props = defineProps({
  // 搜索对象
  searchData: {
    type: Object,
    default: () => ({}),
  },
})
// ------定义变量------
const emit = defineEmits() //子组件获取父组件事件传值
const ruleForm = ref() //定义搜索表单的ref
let datePicker = ref([]) //时间，数组形式
onMounted(() => { })
// ------定义方法------
// 搜索
const handleSearch = () => {
  emit("handleSearch")
}
// 重置搜索表单
const handleReset = (ruleForm) => {
  ruleForm.resetFields()//重置表单
  datePicker.value = []//清空时间
  emit("handleReset") //重置表单
  emit("getList")//刷新列表
}
//
// 日期选择
const handleDate = (val) => {
  emit("getTime", val)
}
// 向父组件暴露方法
defineExpose({});
</script>