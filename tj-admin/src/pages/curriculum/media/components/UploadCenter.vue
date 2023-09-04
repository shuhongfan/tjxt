<!--上传中视频列表-->
<template>
  <div>
    <!-- 表格 -->
    <el-table
      :data="itemData.value"
      border
      stripe
      v-loading="loading"
      :row-class-name="tableRowClassName"
    >
      <el-table-column
        type="selection"
        width="55"
        @selection-change="handleSelectionChange"
      >
      </el-table-column>
      <el-table-column type="index" align="center" width="100" label="序号" />
      <el-table-column prop="name" label="视频名称" min-width="160" />
      <el-table-column prop="size" label="大小（MB）" min-width="160">
        <template #default="scope">
          {{ (scope.row.size / 1024 / 1024).toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column label="状态" min-width="150">
        <template #default="scope">
          <!-- {{scope.row.status}} -->
          <el-progress
            v-if="scope.row.videoFlag"
            :percentage="scope.row.videoUploadPercent"
          ></el-progress>
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        align="center"
        min-width="220"
      >
        <template #default="scope">
          <div class="operate">
            <span
              v-if="scope.row.isStop"
              @click.stop="continueUpload(scope.row)"
              >上传</span
            >
            <span v-else @click.stop="suspendUpload(scope.row)">暂停</span>
            <span
              @click="handleOpenDelete(scope.row)"
              :class="scope.row.status > 0 ? 'textForbidden' : 'textDefault'"
              >删除</span
            >
          </div>
        </template>
      </el-table-column>
      <!-- 空页面 -->
      <template #empty>
        <EmptyPage :isSearch="isSearch" :baseData="itemData"></EmptyPage>
      </template>
      <!-- end -->
    </el-table>
    <!-- end -->
    <!-- 分页 -->
    <el-pagination
      v-if="total > 10"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :page-sizes="[10, 20, 30, 40]"
      :page-size="searchData.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      class="paginationBox"
    >
    </el-pagination>
    <!-- end -->
    <!-- 删除 -->
    <Delete
      :dialogDeleteVisible="dialogDeleteVisible"
      :deleteText="deleteText"
      @handleDelete="handleDelete"
      @handleClose="handleClose"
    ></Delete>
    <!-- end -->
  </div>
</template>
<script setup>
import { ref, reactive, onUpdated, nextTick, watch, onBeforeMount } from "vue";
// 接口api
import { deleteType } from "@/api/curriculum";
// 导入组件
// 删除弹出层
import Delete from "@/components/Delete/index.vue";
// 空页面
import EmptyPage from "@/components/EmptyPage/index.vue";
// 获取父组件值、方法
const props = defineProps({
  // 上传中的视频
  baseData: {
    type: Array,
    default: () => [],
  },
  // // 总条数
  // total: {
  //   type: Number,
  //   default: 0,
  // },
  // loading
  loading: {
    type: Boolean,
    default: false,
  },
  //
  videoUploadPercent: {
    type: String,
    default: 0,
  },
  videoFlag: {
    type: Boolean,
    default: false,
  },
});
// ------定义变量------
const emit = defineEmits(); //子组件获取父组件事件传值
const deleteText = ref("此操作将取消上传视频，是否继续？"); //需要删除的提示内容
let dialogDeleteVisible = ref(false); //控制删除弹层
let videoObj = ref({});
let multipleSelection = ref([]);
let total = ref(null); //数据总条数
let itemData = reactive([]); //上传中数据
let searchData = reactive({
  pageSize: 10,
  pageNo: 1,
}); //搜索对象
// let baseData = ref([]);
// ------定义方法------

onBeforeMount(() => {
  total.value = props.baseData.length;
  itemData.value = props.baseData.slice(0, 10);
});
//当上传中删除正在上传的视频时
onUpdated(() => {
  total.value = props.baseData.length;
  itemData.value = props.baseData.slice(0, 10);
});
// 确定删除
const handleDelete = async () => {
  emit("deleteUpload", videoObj.value);
};
// 设置每页条数
const handleSizeChange = (val) => {
  itemData.value = baseData.value.slice(1, val);
  searchData.pageSize = val;
};
// 当前页
const handleCurrentChange = (val) => {
  // 前端处理分页
  let begin = (val - 1) * searchData.pageSize; //获取数据的开始位置
  let end = val * searchData.pageSize; //获取数据的结束位置
  itemData.value = baseData.value.slice(begin, end); //截取数据
};
// 打开删除弹层
const handleOpenDelete = (row) => {
  dialogDeleteVisible.value = true;
  videoObj.value = row;
};
// 关闭删除弹层
const handleClose = () => {
  dialogDeleteVisible.value = false;
};
// 全选
const handleSelectionChange = (val) => {
  multipleSelection.value = val;
};
const tableRowClassName = ({ row, rowIndex }) => {
  row.index = rowIndex;
};
// 暂停上传
const suspendUpload = (row) => {
  emit('suspendUpload',row)
};
// 恢复上传
const continueUpload = (row) => {
  emit("continueUpload", row);
};
// 向父组件暴露方法
defineExpose({
  itemData,
  dialogDeleteVisible,
});
</script>