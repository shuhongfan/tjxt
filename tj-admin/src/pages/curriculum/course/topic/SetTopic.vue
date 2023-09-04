<!--课程题目-->
<template>
  <div class="dialogMain topicBox dialogHeignt" v-if="dialogVisible">
    <el-dialog
      v-model="dialogVisible"
      title="选择题目"
      :before-close="handleClose"
    >
      <div class="searchForm">
        <el-form ref="ruleForm" :inline="true" :model="searchData">
          <el-row :gutter="30">
            <el-col :span="9">
              <el-form-item label="题目分类" prop="categoryIdLv3">
                <div class="el-input">
                  <el-cascader
                    v-model="searchData.categoryIdLv3"
                    :options="typeData.value"
                    popper-class="cascader"
                    :props="{
                      label: 'name',
                      value: 'id',
                      children: 'children',
                      multiple: true,
                    }"
                    clearable
                    collapse-tags
                    @change="handleChange"
                  >
                    <template #default="{ node, data }">
                      <span>{{ data.name }}</span>
                      <span v-if="!node.isLeaf">
                        ({{ data.children.length }})
                      </span>
                    </template>
                  </el-cascader>
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="9">
              <el-form-item label="题目名称" prop="name">
                <el-input
                  placeholder="请输入"
                  clearable
                  v-model="searchData.name"
                />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <div class="btn">
                <el-button class="button primary" @click="handleSearch"
                  >搜索</el-button
                >
                <el-button
                  class="button buttonSub"
                  @click="handleReset(ruleForm)"
                  >重置</el-button
                >
              </div>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <!-- 穿梭框 -->
      <div class="transferBox">
        <div class="transHearder">
          已选题目：{{ value.length }}/40 总分：{{ score }}分
        </div>
        <el-transfer
          v-model="value"
          :titles="['题目库', '已选题目']"
          :props="{
            key: 'id',
            label: 'name',
          }"
          :data="baseData.value"
          @change="handleTransfer"
          height="500"
        >
          <template #default="{ option }">
            <el-popover
              placement="right-start"
              title=""
              width="200"
              trigger="hover"
              content=""
              @show="handleShow(option)"
            >
              <template #reference>
                <span
                  class="titleText item"
                  @dragstart="drag($event, option)"
                  v-html="option.name"
                ></span>
              </template>
              <div class="popicCon">
                <div class="tit" v-html="tipicData.name"></div>
                <ul>
                  <li v-for="(item, index) in tipicData.options" :key="index">
                    {{ numLetter(index + 1) }}.<span v-html="item"></span>
                  </li>
                </ul>
                <div class="answer">
                  <div class="item">
                    正确答案：
                    <span
                      v-for="(val, index) in tipicData.answers"
                      :key="index"
                    >
                      {{ numLetter(val) }}
                    </span>
                  </div>
                  <div class="item">
                    答案解析：
                    <div
                      v-html="
                        tipicData.analysis === '' ? '无' : tipicData.analysis
                      "
                    ></div>
                  </div>
                </div>
              </div>
              <div class="topicFoot">
                <p>
                  题目难度：{{
                    tipicData.difficult === 1
                      ? "简单"
                      : tipicData.difficult === 2
                      ? "中等"
                      : "困难"
                  }}
                </p>
                <p>题目分数：{{ tipicData.score }}</p>
              </div>
            </el-popover>
          </template>
        </el-transfer>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button class="button buttonPrimary" @click="handleClose"
            >取消</el-button
          >
          <el-button class="button primary" @click="handleSubmit"
            >保存</el-button
          >
        </span>
      </template>
    </el-dialog>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted, watch } from "vue"
import { ElMessage } from "element-plus"
import { formatSeconds, numLetter } from "@/utils/index"
// 接口api
import { getTypeAll } from "@/api/api"
import { getSubjectPage, getDetails } from "@/api/title"
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
const loading = ref(false)
const ruleForm = ref() //定义搜索表单的ref
let total = ref(null) //数据总条数
let baseData = reactive([]) //表格数据
let searchData = reactive({
  pageSize: 1000,
  pageNo: 1,
  sortBy: "update_time",
  isAsc: false,
}) //搜索对象
let sourceData = reactive({}) //视频信息
let typeData = reactive([]) //类型数据
let value = ref([]) //右侧的选择内容
let score = ref(0) //分数
let tableName = reactive([]) //数据表名
let tipicData = ref({}) //题目详情
// ------生命周期------

onMounted(() => {
  init()
})
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
// ------定义方法------
// 获取初始值
const init = () => {
  getTypeList() //获取分类列表
  getTitleList() //获取题目列表
}
// 获取题目列表值
const getTitleList = async () => {
  let parent = {
    pageSize: 1000,
    pageNo: 1,
    sortBy: "update_time",
    isAsc: false,
    thirdCateIds: searchData.thirdCateIds,
    name: searchData.name
  }
  await getSubjectPage(parent)
    .then((res) => {
      if (res.code === 200) {
        baseData.value = res.data.list
        // let data = [];
        // let allData = res.data.list;
        // baseData.value.forEach((member, index) => {
        //   member.name = removeHTMLTag(member.name);
        //   //   data.push({
        //   //     label: member.name,
        //   //     id:member.id,
        //   //     //这里的key值一定要是index，否则目标列表无法显示
        //   //     key: index,
        //   //   });
        // });
        // baseData.value = data;
        // 接口总数据筛选出来的新数组赋值给transferData
        total.value = res.data.total
      }
    })
    .catch((err) => { })
}

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
// 保存选择的题目
const handleSubmit = async () => {
  if (value.value.length > 0) {
    let info = {
      value: value.value,
      totalScore: score.value,
    }
    emit("setTopicInfo", info)
    handleClose()
  } else {
    ElMessage({

      message: "已选题目为空，请设置测试题目！",
      type: "error",
      showClose: false,
    })
  }
}
// 搜索
const handleSearch = () => {
  getTitleList()
}
// 关闭弹层
const handleClose = () => {
  value.value = props.jectIds
  searchData.categoryIdLv3 = null //清空选中的分类
  searchData.thirdCateIds = null
  emit("handleClose")
  ruleForm.value.resetFields()
  getTitleList()

}
// 获取分类ids
const handleChange = (val) => {
  let thirdCateId = []
  if (val.length > 0) {
    val.map((val) => {
      if (val[2]) {
        thirdCateId.push(val[2])
      }
    })
  }
  searchData.thirdCateIds = thirdCateId.join(",")
}
// 重置表单
const handleReset = (ruleForm) => {
  ruleForm.resetFields()
  searchData.categoryIdLv3 = null //清空选中的分类
  searchData.thirdCateIds = null
  // 清空搜索表单内容
  getTitleList()
}
// 触发穿梭框左侧或者右侧数据
const handleTransfer = (val) => {
  // 数据最多添加40条
  if (val.length > 40) {
    ElMessage({

      message: "最多选择40条数据！",
      type: "error",
      showClose: false,
    })
  }
  let soliceVal = val.splice(0, 40)
  value.value = soliceVal //截取的值赋给右侧列表
  // 计算已选题目的分数
  let num = 0
  baseData.value.forEach((ele, index) => {
    soliceVal.map((id) => {
      if (ele.id === id) {
        num += ele.score
      }
    })
  })
  score.value = num
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
.transferBox {
  :deep(.el-transfer) {
    display: flex;
    justify-content: space-between;
  }
  :deep(.el-transfer__buttons) {
    display: flex;
    flex-direction: column;
    justify-content: center;
    .el-button.is-disabled {
      background-color: #f8f5f5;
      border-color: #f8f5f5;
      color: #b5abab;
    }
  }
  .transHearder {
    right: 14px;
  }
  :deep(.el-transfer-panel) {
    .el-transfer-panel__header {
      .el-checkbox__label {
        font-size: 14px;
      }
    }
    .el-transfer-panel__body {
      .el-transfer-panel__item {
        // 添加hover状态的背景
        &:hover {
          background-color: #faf4ee;
        }
        // 清空选中状态的背景色
        &.is-checked {
          background-color: #fff;
        }
      }
      // 空样式
      .el-transfer-panel__empty {
        color: transparent;
        background-image: url(@/assets/emptyIcon.png);
        background-size: 162px 126px;
        background-repeat: no-repeat;
        width: 162px;
        height: 126px;
        position: absolute;
        top: 140px;
        left: 156px;
        &::after {
          content: "一条数据也没有~";
          display: block;
          text-align: center;
          color: #332929;
          font-size: 14px;
          position: absolute;
          top: 130px;
          left: 27px;
        }
      }
    }
  }
}
:deep(.el-dialog) {
  width: 1094px !important;
  .el-form-item {
    display: flex;
  }
  .el-dialog__body {
    padding-left: 30px;
    padding-right: 30px;
    padding-top: 20px;
    padding-bottom: 32px;
  }
  // .searchForm {
  //   padding-top: 0 !important;
  //   height: 40px !important;
  //   margin-bottom: 20px !important
  //     ;
  // }
}
</style>
<style lang="scss" scoped>
.popicCon {
  .tit {
    display: flex;
    img {
      width: 50px;
      height: 50px;
    }
  }
}
.el-checkbox {
  font-weight: normal;
}
</style>