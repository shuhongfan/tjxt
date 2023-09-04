<!-- 课程分类列表-->
<template>
  <div class="contentBox">
    <!-- 搜索 -->
    <Search
      :searchData="searchData"
      @handleSearch="handleSearch"
      @getList="getList"
      @handleReset="handleReset"
    ></Search>
    <!-- end -->
    <div class="bg-wt radius marg-tp-20">
      <div class="tableBox">
        <!-- 新增 -->
        <div class="subHead pad-30">
          <AddButton @handleAdd="handleAdd" :text="text"></AddButton>
        </div>
        <!-- end -->
        <!-- 表格数据 -->
        <TableList
          ref="table"
          :baseData="baseData.value"
          :loading="loading"
          :isSearch="isSearch"
          @getList="getList"
          @handleAdd="handleAdd"
          @handleEdit="handleEdit"
        ></TableList>
        <!-- end -->
      </div>
    </div>
    <!-- 新增弹层 -->
    <Add
      ref="add"
      :title="title"
      :fromData="fromData"
      :parentId="parentId"
      :dialogFormVisible="dialogFormVisible"
      :firstLevelData="firstLevelData.value"
      :twoLevelData="twoLevelData.value"
      @getSort="getSort"
      @getList="getList"
      @handleClose="handleClose"
      @handleSuccee="handleSuccee"
      @resetFrom="resetFrom"
    ></Add>
    <!-- end -->
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from "vue"
// 公用数据
import { statusData } from "@/utils/commonData"
// 导入接口
import { getCurriculumType, getDetails } from "@/api/curriculum"
// 导入组件
// 新增按钮
import AddButton from "@/components/AddButton/index.vue"
// 搜索
import Search from "./components/Search.vue"
// 表格
import TableList from "./components/TableList.vue"
// 新增编辑
import Add from "./components/add.vue"
// ------定义变量------
const statusBase = statusData
const text = ref("新增一级分类")
const loading = ref(false)
const table = ref() //table ref
let title = ref("")
let dialogFormVisible = ref(false) //弹层隐藏显示
let searchData = reactive({
  pageSize: 10,
  pageNo: 1,
}) //搜索对象
let parentId = ref("0") //type级别id
let baseData = reactive([]) //表格数据
let fromData = ref({
  index: 1,
}) //新增编辑表单数据
let firstLevelData = reactive({}) //一级分类内容
let twoLevelData = reactive({}) //二级分类内容
let isSearch = ref(false) //是否触发了搜索按钮,用来控制没有搜索出数据和正常列表无数据的区分，显示的图片和提示语不一样
// ------生命周期------
onMounted(() => {
  init()
})
// ------定义方法------
// 获取初始值
const init = () => {
  getList()
}
// 获取列表值
const getList = async () => {
  loading.value = true
  await getCurriculumType(searchData)
    .then((res) => {
      if (res.code === 200) {
        loading.value = false
        baseData.value = res.data
      }
    })
    .catch((err) => { })
}
// 获取详情
const getDetailData = async (id) => {
  await getDetails(id)
    .then((res) => {
      if (res.code === 200) {
        fromData.value = res.data
      }
    })
    .catch((err) => { })
}
// 搜索
const handleSearch = () => {
  isSearch.value = true //是否触发了搜索按钮
  table.value.isExpandAll = true
  getList() //刷新列表
}
// //重置后table 闭合二级和三级
const handleReset = () => {
  table.value.isExpandAll = false
  getList() //刷新列表
}
// 打开新增弹层
const handleAdd = (val) => {

  title.value = "新增课程分类"
  dialogFormVisible.value = true
  if (val) {
    parentId.value = val.id
    if (val.level === 1) {
      firstLevelData.value = val
      twoLevelData.value = {}
    } else if (val.level === 2) {
      twoLevelData.value = val
    }
  } else {
    parentId.value = "0" //如果是新增一级分类，先把parentId清空
    firstLevelData.value = {}
    twoLevelData.value = {}
  }
  // fromData.value=val
}
// 打开编辑弹层
const handleEdit = (val) => {
  // parentId.value = id
  title.value = "编辑课程分类"
  dialogFormVisible.value = true
  // fromData.value = val
  getDetailData(val.id)
}
// 获取排序数字
const getSort = (val) => {
  fromData.value.index = val
}
// 关闭弹层
const handleClose = () => {
  dialogFormVisible.value = false
  fromData.value.index = 1
  fromData.value.name = null
}
const resetFrom = () => {
  firstLevelData.value = {}
  twoLevelData.value = {}
  parentId.value = '0'
  fromData.value.id = undefined
}
</script>
<style src="./../index.scss" lang="scss" scoped></style>
<style lang="scss" scoped>
.tableBox {
  margin-bottom: 20px;
}
:deep(.el-dialog) {
  .el-dialog__body {
    // padding-top: 20px;
    // padding-bottom: 12px;
  }
  // .el-dialog__footer {
  //   padding-bottom: 40px;
  // }
}
:deep(.typeBox .el-table .el-table__body-wrapper .el-table__cell) {
  padding: 20px 0;
  height: 20px;
  line-height: 20px;
}
:deep(.tableBox .el-table td.el-table__cell) {
  padding: 20px 0;
  line-height: 20px;
}
</style>
