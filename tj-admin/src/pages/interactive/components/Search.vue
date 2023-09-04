<!--回答搜索-->
<template>
  <div class="bg-wt radius marg-tp-20">
    <div class="pad-30 searchForm">
      <el-form ref="ruleForm" :inline="true" :model="searchData">
        <el-row :gutter="30">
          <el-col :span="6">
            <el-form-item label="问题状态" prop="status">
              <div class="el-input">
                <el-select
                  v-model="searchData.status"
                  clearable
                  placeholder="请选择"
                  style="width: 100%"
                >
                  <el-option
                    v-for="item in questionsData"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="课程名称" prop="courseName">
              <el-input
                placeholder="请输入"
                clearable
                v-model="searchData.courseName"
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="提问时间" prop="datePicker">
              <div class="el-input">
                <el-date-picker
                  v-model="datePicker"
                  format="YYYY-MM-DD HH:mm:ss"
                  value-format="YYYY-MM-DD HH:mm:ss"
                  type="datetimerange"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  clearablea
                  align="right"
                  :popper-class="questionTime"
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
import { ref, reactive, onMounted } from "vue"
// 公用数据
import { questionsData } from "@/utils/commonData"
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
let datePicker = ref([])//时间数据，获取默认时间的是数组
// 生命周期
onMounted(() => {
})
// ------定义方法------
// 搜索
const handleSearch = () => {
  emit("handleSearch")
}
// 重置搜索表单
const handleReset = (ruleForm) => {
  ruleForm.resetFields()//重置表单
  datePicker.value = [] //清空时间选择
  emit('handleReset') //重置表单
  emit('getList') //刷新列表
}
//
// 日期选择
const handleDate = (val) => {
  emit("getTime", val)
}
// 向父组件暴露方法
defineExpose({
});
</script>
<style lang="scss">
// .el-picker__popper.el-popper {
//   // left: 274px !important;
// }
// .el-picker-panel__icon-btn:hover{
//   color: #ff734f;
// }
// .el-date-picker__header-label:hover {
//   color: #ffffff;
// }
// .el-date-table td.disabled div {
// }
// // 默认状态
// .el-date-table td.today {
//   .el-date-table-cell__text {
//     color: #ff734f;
//   }
// }
// // 鼠标停留状态
// .el-picker-panel .el-date-table td.available:hover {
//   color: #ffffff;
//   .el-date-table-cell {
//     .el-date-table-cell__text {
//       background-color: #ff734f;
//     }
//   }
// }
// // 开始按钮选中状态
// .el-date-table td.start-date {
//   .el-date-table-cell__text {
//     background-color: #ff734f;
//   }
// }
// // 结束按钮选中状态
// .el-date-table td.end-date {
//   .el-date-table-cell__text {
//     background-color: #ff734f;
//   }
// }
// // 开始和结束之间的按钮的样式
// .el-date-table td.in-range {
//   .el-date-table-cell {
//     background-color: #faf4ee;
//     &:hover{
//       background-color: #faf4ee;
//     }
//   }
// }
.button {
  font-family: PingFangSC-Regular;
}
.el-button.is-plain{
  width: 64.5px;
  height: 30px;
  background: #FF734F;
  border-radius: 20px;
  font-family: PingFangSC-Medium;
  font-weight: 400;
  font-size: 14px;
  border: 1px solid transparent;
  color: #FFFFFF;
  &:hover{
    background: #F16440;
    border: 0;
  }
}
.el-button.is-text{
  width: 64.5px;
  height: 30px;
  // border: 1px solid #FF734F;
  box-sizing: border-box;
  border-radius: 20px;
  font-family: PingFangSC-Medium;
  font-weight: 400;
  font-size: 14px;
  background:#ffffff;
  &:hover{
    background: #F16440;
    border: 0;
  }
}
.el-picker-panel__footer{
  padding: 10px 30px;
}
.gettime{
  // left: 560px !important;
  position: absolute !important;
  .el-popper__arrow{
      &::before{
        position: absolute;
        // right: 314px !important;
      }
  }
}
// .el-date-table td.current:not(.disabled) .el-date-table-cell__text{
//   background: #F16440;
// }
// .el-date-table td.today{
//   // hover时的样式
//   &:hover{
//     .el-date-table-cell__text{
//       color: #fff;
//       font-weight: normal;
//     }
//   }
// }
.el-time-panel__btn.confirm{
  color: #ff734f;
  &:hover{
    opacity: 0.8;
  }
}
</style>
