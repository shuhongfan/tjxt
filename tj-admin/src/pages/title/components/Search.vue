<!--题目搜索-->
<template>
  <div class="bg-wt radius marg-tp-20">
    <div class="pad-30 searchForm">
      <el-form ref="ruleForm" :inline="true" :model="searchData">
        <el-row :gutter="30">
          <el-col :span="6">
            <el-form-item label="题目分类" prop="categoryIdLv3">
              <div class="el-input">
                <el-cascader
                  v-model="categoryIdLv3"
                  placeholder="请选择"
                  :options="typeData.value"
                  popper-class="cascader"
                  :props="{
                    label: 'name',
                    value: 'id',
                    children: 'children',
                    multiple: true,
                  }"
                  collapse-tags
                  clearable
                  @change="handleChange"
                >
                  <template #default="{ node, data }">
                    <span>{{ data.name }}</span>
                    <span v-if="!node.isLeaf">
                      <!-- ({{ data.children.length }}) -->
                    </span>
                  </template>
                </el-cascader>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="难易程度" prop="difficulty">
              <div class="el-input">
                <el-select
                  v-model="searchData.difficulty"
                  clearable
                  placeholder="请选择"
                  style="width: 100%"
                >
                  <el-option
                    v-for="item in difficultyData"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="题目名称" prop="keyword">
              <el-input
                placeholder="请输入"
                clearable
                v-model="searchData.keyword"
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
import { ref, reactive, onMounted } from "vue"
// 公用数据
import { difficultyData } from "@/utils/commonData"
// 接口
import { getTypeAll } from "@/api/api"
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
let typeData = reactive([]) //类型数据
let thirdCateId = reactive([]) //三级分类ids
let categoryIdLv3=ref([])
onMounted(() => {
  // 获取分类
  getTypeList()
})
// ------定义方法------
// 获取分类
const getTypeList = async () => {
  await getTypeAll({ admin: true })
    .then((res) => {
      if (res.code === 200) {
        typeData.value = res.data
      }
    })
    .catch((err) => { })
}
// 搜索
const handleSearch = () => {
  emit("handleSearch")
}
// 重置搜索表单
const handleReset = (ruleForm) => {
  ruleForm.resetFields()//重置表单
  categoryIdLv3.value=null //清空选中的分类
  emit("handleReset") //重置表单
  emit("getList")//刷新列表
}
//择题目分类
const handleChange = (value) => {
  emit("getTypeData", value)
}
// 向父组件暴露方法
defineExpose({
  typeData,
});
</script>