<!--媒资列表-->
<template>
  <div>
    <!-- 表格 -->
    <el-table :data="baseData" border stripe v-loading="loading" :sort-change="handleSortChange">
      <el-table-column type="index" align="center" width="100" label="序号" />
      <el-table-column prop="" label="视频名称" min-width="250" class-name="textLeft">
        <template #default="scope">
          <div>
            {{ ellipsis(scope.row.filename,10) }} .mp4
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="size" label="大小（MB）" min-width="160">
        <template #default="scope">
          {{ (scope.row.size / 1024 / 1024).toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column prop="duration" label="视频时长" min-width="150">
        <template #default="scope">
          {{ formatSeconds(scope.row.duration) }}
        </template>
      </el-table-column>
      <el-table-column prop="creater" label="上传人" min-width="150">
      </el-table-column>
      <el-table-column
        prop="useTimes"
        sortable="custom"
        label="当前引用次数"
        min-width="170"
      >
      </el-table-column>
      <el-table-column
        prop="createTime"
        sortable="custom"
        min-width="220"
        label="上传时间"
        :formatter="formatTime"
      >
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        align="center"
        min-width="110"
        class-name="popperClass"
      >
        <template #default="scope">
          <div class="operate">
            <span class="textDefault" @click="handlePreview(scope.row.id)"
              >预览</span
            >
            <span
              @click="handleOpenDelete(scope.row)"
              :class="scope.row.useTimes > 0 ? 'textForbidden' : 'textWarning'"
              >删除</span
            >
          </div>
        </template>
      </el-table-column>
      <!-- 空页面 -->
      <template #empty>
        <EmptyPage :isSearch="isSearch" :baseData="baseData"></EmptyPage>
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
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="Number(total)"
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
    <!-- 预览弹层 -->
    <Preview
      ref="preview"
      :title="title"
      :mediaId="mediaId"
      :dialogFormVisible="dialogFormVisible"
      @handleClose="handlePreviewClose"
    ></Preview>
    <!-- end -->
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from "vue"
import { ElMessage } from "element-plus"
import { formatSeconds } from '@/utils/index'
import {formatTime,ellipsis} from "@/utils/index"
// 接口api
import { deleteMedia } from "@/api/media"
// 导入组件
// 删除弹出层
import Delete from "@/components/Delete/index.vue"
// 预览弹层
import Preview from "@/components/Preview/index.vue"
// 空页面
import EmptyPage from "@/components/EmptyPage/index.vue"
// 获取父组件值、方法
const props = defineProps({
  // 数据
  baseData: {
    type: Array,
    default: () => [],
  },
  // 总条数
  total: {
    type: Number,
    default: 0,
  },
  // 每页的数量
  pageSize: {
    type: Number,
    default: 0,
  },
  // loading
  loading: {
    type: Boolean,
    default: false,
  },
  // 是否触发了搜索按钮
  isSearch: {
    type: Boolean,
    default: false,
  },
})
// ------定义变量------
const emit = defineEmits() //子组件获取父组件事件传值
const deleteText = ref("此操作将删除该视频，是否继续？") //需要删除的提示内容
let dialogDeleteVisible = ref(false) //控制删除弹层
let dialogFormVisible = ref(false) //弹层隐藏显示
let mediaId = ref("")//视频id
let preview = ref(null)
// ------定义方法------
// 确定删除
const handleDelete = async () => {
  await deleteMedia(mediaId.value)
    .then((res) => {
      if (res.code === 200) {
        ElMessage({

          message: "删除成功!",
          type: "success",
          showClose:false,
        })
        emit("getList")
        handleClose()

      }
    })
    .catch((err) => { })
}
// 设置每页条数
const handleSizeChange = (val) => {
  emit("handleSizeChange", val)
}
// 当前页
const handleCurrentChange = (val) => {
  emit("handleCurrentChange", val)
}
// 排序方式
const handleSortChange = (column, prop, order) => {
  console.log(column, prop, order);
}
// 打开删除弹层
const handleOpenDelete = (row) => {
  dialogDeleteVisible.value = true
  mediaId.value = row.id
}
// 关闭删除弹层
const handleClose = () => {
  dialogDeleteVisible.value = false
}
// 打开预览弹层
const handlePreview = async (id) => {
  mediaId.value = id
  preview.value.getId(id)
  dialogFormVisible.value = true
}
// 关闭弹层
const handlePreviewClose = () => {
  dialogFormVisible.value = false
};
</script>
<style lang="scss" scoped>
:deep(.el-table th.el-table__cell>.cell) {
  width: 106%;
}

</style>
<style lang="scss">
.popperClass{
  .cell{
    padding: 0 !important;
  }
}
</style>