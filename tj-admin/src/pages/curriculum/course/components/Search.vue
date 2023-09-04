<!--课程管理搜索-->
<template>
  <div class="bg-wt radius marg-tp-20">
    <div class="pad-30 searchForm">
      <el-form ref="ruleForm" :inline="true" :model="searchData">
        <el-row :gutter="30">
          <el-col :span="6">
            <el-form-item label="课程分类" prop="categoryIdLv3">
              <div class="el-input">
                <el-cascader
                  v-model="searchData.categoryIdLv3"
                  :options="typeData.value"
                  @change="handleChange"
                  :props="{
                    label: 'name',
                    value: 'id',
                    children: 'children',
                  }"
                  clearable
                  style="width: 100%"
                >
                </el-cascader>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="售卖模式" prop="free">
              <div class="el-input">
                <el-select
                  v-model="searchData.free"
                  clearable
                  placeholder="请选择"
                  style="width: 100%"
                  @change="handleFree"
                >
                  <el-option
                    v-for="item in sellingModelData"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </div>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="6">
            <el-form-item label="课程状态" prop="status">
              <div class="el-input">
                <el-select
                  v-model="searchData.status"
                  clearable
                  placeholder="请选择"
                  style="width: 100%"
                  @change="handleSelect"
                >
                  <el-option
                    v-for="item in courseStatusData"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </div>
            </el-form-item>
          </el-col> -->
          <el-col :span="6">
            <el-form-item label="课程名称" prop="keyword">
              <el-input
                placeholder="请输入"
                clearable
                v-model="searchData.keyword"
              />
            </el-form-item>
          </el-col>

          <el-col :span="6">
            <el-form-item label="更新时间" prop="datePicker">
              <div class="el-input">
                <el-date-picker
                  v-model="datePicker"
                  format="YYYY-MM-DD HH:mm:ss"
                  value-format="YYYY-MM-DD HH:mm:ss"
                  type="daterange"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  clearable
                  align="right"
                  :picker-options="dateButton"
                  @change="handleDate($event)"
                >
                </el-date-picker>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="30">
          <el-col :span="24">
            <div class="btn curriculumBtn">
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
import { ref, reactive, onMounted, watch } from "vue"
// 公用数据
import { sellingModelData } from "@/utils/commonData"
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
let typeData = reactive([]) //课程分类数据
let datePicker = ref([])//时间数据，获取默认时间的是数组
let categoryIdLv3 = ref([])
onMounted(() => {
  getTypeList()
})
// ------定义方法------
// 获取课程分类
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
  console.log(datePicker.value)
  emit("handleSearch")
}
// 重置搜索表单
const handleReset = (ruleForm) => {
  ruleForm.resetFields()//重置表单
  searchData.datePicker = null//清空时间数据
  searchData.categoryIdLv3 = null
  emit("handleReset") //重置表单
  emit("getList") //刷新列表
}
//处理分类
const handleChange = (value) => {
  emit("getTypeData", value)
}
// 日期选择
const handleDate = (val) => {
  emit("getTime", val)
}
// 售卖模式选择
const handleFree = (val) => {
  emit("getFree", val)
}
// 获取状态
const handleSelect = (val) => {
  emit("getStatus", val)
}

</script>
<style lang="scss">
.el-picker__popper.el-popper {
  // left: 274px !important;
}
.el-picker-panel__icon-btn:hover {
  color: #ff734f;
}
.el-date-picker__header-label:hover {
  color: #ffffff;
}
.el-date-table td.disabled div {
}
// 默认状态
.el-date-table td.today {
  .el-date-table-cell__text {
    color: #ff734f;
  }
}
// 鼠标停留状态
.el-picker-panel .el-date-table td.available:hover {
  color: #ffffff;
  .el-date-table-cell {
    .el-date-table-cell__text {
      background-color: #ff734f;
    }
  }
}
// 开始按钮选中状态
.el-date-table td.start-date {
  .el-date-table-cell__text {
    background-color: #ff734f;
  }
}
// 结束按钮选中状态
.el-date-table td.end-date {
  .el-date-table-cell__text {
    background-color: #ff734f;
  }
}
// 开始和结束之间的按钮的样式
.el-date-table td.in-range {
  .el-date-table-cell {
    background-color: #faf4ee;
    &:hover {
      background-color: #faf4ee;
    }
  }
}
.button {
  font-family: PingFangSC-Regular;
}
:deep(.el-input .el-input__wrapper .el-input__inner) {
  color: #332929;
}
</style>
<style lang="scss" scoped>
:deep(.el-input__wrapper) {
  // 修改.input输入框中的placeholder样式
  .el-input__inner {
    &::placeholder {
      color: #b5abab;
    }
  }
}
.curriculumBtn {
  padding: 10px 0 20px;
}
</style>
<style lang="scss" scoped>
// .el-cascader-node__prefix {
//   display: none;
// }
:deep(.el-input .el-input__icon) {
  color: #b5abab;
}
</style>
<style lang="scss">
// 默认状态
.el-date-table td.current:not(.disabled) .el-date-table-cell__text {
  background: #f16440;
}
.el-date-table td.today {
  // hover时的样式
  &:hover {
    .el-date-table-cell__text {
      color: #fff;
      font-weight: normal;
    }
  }
}
</style>