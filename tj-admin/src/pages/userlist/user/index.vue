<!-- 教师列表-->
<template>
  <div class="contentBox">
    <!-- 搜索 -->
    <Search :searchData="searchData" @handleSearch="handleSearch" @getList="getList"></Search>
    <!-- end -->
    <div class="bg-wt radius marg-tp-20">
      <div class="tableBox">
        <div class="subHead pad-30">
        <!-- 新增 -->
        <AddButton @handleAdd="handleAdd" :text="text"></AddButton>
        <!-- end -->
        </div>
        <!-- 表格数据 -->
        <TableList
          :userData="userData.value"
          :total="total"
          :loading="loading"
          :isSearch="isSearch"
          :pageSize="searchData.pageSize"
          @getList="getList"
          @handleEdit="handleEdit"
          @handleSizeChange="handleSizeChange"
          @handleCurrentChange="handleCurrentChange"
        ></TableList>
        <!-- end -->
      </div>
    </div>
    <!-- 新增弹层 -->
    <Add
      ref="add"
      :title="title"
      :fromData="fromData"
      :dialogFormVisible="dialogFormVisible"
      @getList="getList"
      @handleClose="handleClose"
      @handleSuccee="handleSuccee"
      @getParams="getParams"
    ></Add>
    <!-- end -->
    <!-- 创建账号成功提示 -->
    <CreateSuccess
      :dialogSucceeVisible="dialogSucceeVisible"
      @handleSucceeClose="handleSucceeClose"
      :fromData="ParamsData"
    ></CreateSuccess>
    <!-- end -->
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from "vue";
// 导入接口
import { getStaffs } from "@/api/staffs";
import { queryUserById } from "@/api/user";
// 导入组件
// 新增按钮
import AddButton from "@/components/AddButton/index.vue";
// 搜索
import Search from "./components/Search.vue";
// 表格
import TableList from "./components/TableList.vue";
// 新增编辑
import Add from "./components/add.vue";
// 创建成功
import CreateSuccess from "./components/addSuccess.vue";
// ------定义变量------
const text = ref("新增后台用户");
const loading = ref(false);
const add = ref()
let title = ref("");
let total = ref(null); //数据总条数
let dialogFormVisible = ref(false); //谈层隐藏显示
let dialogSucceeVisible = ref(false); //创建成功弹层显示
let isSearch = ref(false)//是否触发了搜索按钮,用来控制没有搜索出数据和正常列表无数据的区分，显示的图片和提示语不一样
let searchData = reactive({
  pageSize: 10,
  pageNo: 1,
}); //搜索对象
let userData = reactive([]); //表格数据
let fromData = ref({}); //新增编辑表单数据
let ParamsData = ref({}); //创建成功返回的数据
// ------生命周期------
onMounted(() => {
  init();
});
// ------定义方法------
// 获取初始值
const init = () => {
  getList();
};
// 获取列表值
const getList = async () => {
  loading.value = true;
  await getStaffs(searchData)
    .then((res) => {
      if (res.code === 200) {
        loading.value = false;
        userData.value = res.data.list;
        total.value = res.data.total;
      }
    })
    .catch((err) => {});
};
// 获取详情
const getDetailData = async (id) => {
  await queryUserById(id)
    .then((res) => {
      if (res.code === 200) {
        fromData.value = res.data;

      }
    })
    .catch((err) => {});
};
// 搜索
const handleSearch = () => {
  isSearch.value = true//是否触发了搜索按钮
  getList();
};
// 设置每页条数
const handleSizeChange = (val) => {
  searchData.pageSize = val;
  // 刷新列表
  getList();
};
// 当前页
const handleCurrentChange = (val) => {
  searchData.pageNo = val;
  // 刷新列表
  getList();
};
// 打开新增弹层
const handleAdd = () => {
  title.value = "新增后台用户账号信息";
  dialogFormVisible.value = true;
};
// 打开编辑弹层
const handleEdit = (id) => {
  title.value = "编辑后台用户账号信息";
  dialogFormVisible.value = true;
  getDetailData(id);
};
// 关闭弹层
const handleClose = () => {
  dialogFormVisible.value = false;
};
// 账号创建成功弹层显示
const handleSuccee = () => {
  dialogSucceeVisible.value = true;
  handleClose();
};
// 关闭创建账号成功弹层
const handleSucceeClose = () => {
  dialogSucceeVisible.value = false;
};
// 接收子组件传值
const getParams = (val) => {
  ParamsData.value = {...val};
};
</script>
<style src="./../index.scss" lang="scss" scoped></style>
<style lang="scss" scoped>
:deep(.el-dialog .el-form-item__label) {
  justify-content: end;
}
:deep(.el-dialog .el-dialog__body) {
  padding-left: 50px;
}
.contentBox{
  margin-bottom: 20px;
}
</style>