<template>
  <div class="topicBox" v-if="dialogVisible">
    <el-dialog
      v-model="dialogVisible"
      title="查看题目"
      :before-close="handleClose"
    >
      <div class="transHearder">
        <span class="transHearder-title">已选题目：</span><span class="transHearder-item">{{ tableName.length }}</span><span class="transHearder-title">&nbsp;&nbsp;&nbsp;&nbsp;  总分：</span><span class="transHearder-item">{{ score }}</span>分
      </div>
      <!-- 题目列表 -->
      <el-table
        :data="tableName"
        border
        :stripe="true"
        v-loading="loading"
        row-key="id"
        height="475"
      >
        <el-table-column type="index" align="center" width="60px" label="序号" />
        <el-table-column label="题目名称" min-width="250" class-name="textLeft">
          <template #default="scope">
            <!-- 添加el-Popover弹出框 -->
            <el-popover
              placement="right"
              width="300"
              trigger="hover"
              content=""
              @show="handleShow(scope)"
            >
            <template #reference>
              <div class="head">
                <div class="ellipsisHidden2" v-html="scope.row.name"></div>
              </div>
            </template>
            <div class="popicCon">
                <div class="tit" v-html="scope.row.name"></div>
                <ul>
                  <li v-for="(item, index) in scope.row.options" :key="index">
                    {{numLetter(index+1)}}.<span v-html="item"></span>
                  </li>
                </ul>
                <div class="answer">
                  <div class="item">正确答案：
                    <span
                      v-for="(val, index) in scope.row.answers"
                      :key="index"
                    >
                    {{numLetter(val)}}
                    </span>
                  </div>
                  <div class="item">答案解析：<div v-html="scope.row.analysis===''?'无':scope.row.analysis"></div></div>
                </div>
                
              </div>
              <div class="topicFoot">
                <p>题目难度：{{scope.row.difficult===1?'简单':scope.row.difficult===2?'中等':'困难'}}</p>
                <p>题目分数：{{scope.row.score}}</p>
              </div>
            </el-popover>
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <span class="dialog-footer">
          <el-button class="button primary" @click="handleClose">返回</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>
<script setup>
import { ref,  watch } from "vue"
import {  numLetter } from "@/utils/index"
// 接口api
import { getSubjectsList } from "@/api/curriculum"
// 接口
import {  getDetails } from "@/api/title"
// 获取父组件值、方法
const props = defineProps({
  // 弹层隐藏显示
  dialogVisible: {
    type: Boolean,
    default: false,
  },
  //
  score: {
    type: Number,
    default: 0,
  },
  // 获取的题目ids
  jectIds: {
    type: Array,
    default: () => [],
  },
})
// ------定义变量------
const emit = defineEmits() //子组件获取父组件事件传值
const loading = ref(false) //加载
let value = ref([]) //右侧的选择内容
let score = ref(0) //分数
let tableName = ref([]) //数据表名
let tipicData = ref({}) //题目详情
// 监听题目回显数据
watch(
  () => props.jectIds,
  (count, prevCount) => {
    if (count) {
      value.value = count
    }
    /* ... */
  }
)
// 监听分数回显数据
watch(
  () => props.score,
  (count, prevCount) => {
    if (count) {
      score.value = count
    }
    /* ... */
  }
)
// 监听弹窗的显隐
watch(
  () => props.dialogVisible,
  (count, prevCount) => {
    if (count) {
      getCheckList()
    }
    /* ... */
  }
)

// 获取对应的题目
const getCheckList = async (id) => {
  await getSubjectsList(props.jectIds)
    .then((res) => {
      if (res.code === 200) {
        tableName.value = res.data
      }
    })
    .catch((err) => { })
}

// 关闭弹层
const handleClose = () => {
  value.value = props.jectIds
  emit("handleClose")
}

// 鼠标经过时显示内容
const handleShow = async (val) => {
  await getDetails(val.id)
    .then((res) => {
      tipicData.value = res.data
    })
    .catch((err) => { })
};
</script>
<style lang="scss" scoped>
.popicCon {
  color: #332929;
}
.topicFoot {
  color: #332929;
}
.topicBox {
  .btn {
    display: flex;
    padding-top: 0;
  }
  :deep(.el-transfer-panel) {
    width: 46% !important;
  }
}

:deep(.el-dialog) {
  width: 788px !important;
  height: 700px;
  .el-form-item {
    display: flex;
  }
  .el-dialog__body {
    padding-left: 30px;
    padding-right: 30px;
    padding-top: 20px;
    border-top: 1px solid #e5e4e4;
    padding-bottom: 32px;
  }
  // .searchForm {
  //   padding-top: 0 !important;
  //   height: 40px !important;
  //   margin-bottom: 20px !important;
  // }
}
.transHearder {
    right: 14px;
    font-family: PingFangSC-Medium;
    font-weight: 500;
    font-size: 14px;
    margin-bottom: 21px;
    .transHearder-title{
      font-family: PingFangSC-Medium;
      font-weight: 500;
      font-size: 14px;
      color: #332929;
    }
    .transHearder-item{
      color: #FF734F;
      font-family: PingFangSC-Medium;
      font-weight: 500;
      font-size: 14px;
    }
  }
  :deep(.el-table){
    thead{
      height: 40px !important;
    }
    tr.el-table__row{
      height: 60px
    }
    td.el-table__cell{
      font-family: PingFangSC-Regular;
      font-weight: 400;
      font-size: 14px;
      color: #332929;
  }
  th.el-table__cell.is-leaf{
    font-family: PingFangSC-Medium;
    font-weight: 500;
    font-size: 14px;
    color: #332929;
    background: #FDFCFA;
  }

}
  
</style>
<style lang="scss">
  .el-table--striped .el-table__body tr.el-table__row--striped td.el-table__cell{
    background: #FDFCFA;
  }
</style>
