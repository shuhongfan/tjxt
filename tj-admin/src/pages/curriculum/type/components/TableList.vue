<!-- 课程分类列表 -->
<template>
  <div class="typeBox">
    <!-- 表格 -->
    <el-table
      :data="baseData"
      border
      v-loading="loading"
      row-key="id"
      :default-expand-all="isExpandAll"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      
    >
      <el-table-column
        prop="name"
        label="分类名称"
        min-width="340"
        class-name="textLeft"
      >
        <template #default="scope">
          {{ scope.row.name
          }}<span class="textInfo">{{ scope.row.children.length>0?'('+scope.row.children.length+')':'' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="课程数量" prop="courseNum" min-width="160">
      </el-table-column>
      <el-table-column prop="index" label="排序" min-width="150">
      </el-table-column>
      <el-table-column prop="updateTime" min-width="220" label="更新时间" :formatter="formatTime">
      </el-table-column>
      <el-table-column label="状态" min-width="120">
        <template #default="scope">
          <span
            class="iconTip"
            :class="scope.row.status === 0 ? 'forbidIcon' : 'normalIcon'"
          ></span>
          {{ scope.row.status === 0 ? "禁用" : "正常" }}
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        align="center"
        min-width="265"
        class-name="typeAction"
      >
        <template #default="scope">
          <div class="operate">
            <span @click="handleAdd(scope.row)" class="textDefault"
              >{{
                scope.row.level === 1
                  ? "添加二级分类"
                  : scope.row.level === 2
                  ? "添加三级分类"
                  : ""
              }}
            </span>
            <span
              class="textForbidden"
              v-if="scope.row.level === 3"
              style="margin-left: 0"
              >添加三级分类</span
            >
            <span @click="handleEdit(scope.row)" class="textDefault">编辑</span>
            <span @click="handleOpenStatus(scope.row)" :class="scope.row.status === 1 ? 'textWarning' : 'textDefault'">
              {{ scope.row.status === 1 ? "禁用" : "启用" }}
            </span>
            <!-- 禁止的时候不能触发删除按钮，按钮置灰(textForbidden) -->
            <span
              @click="handleOpenDelete(scope.row)"
              :class="scope.row.status === 1 ? 'textForbidden' : 'textWarning'"
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
    <!-- 删除 -->
    <Delete
      :dialogDeleteVisible="dialogDeleteVisible"
      :deleteText="deleteText"
      @handleDelete="handleDelete"
      @handleClose="handleClose"
    ></Delete>
    <!-- end -->
    <!-- 启用、禁用 -->
    <Status
      :statusText="statusText"
      :dialogVisible="dialogStatusVisible"
      @handleStatus="handleStatus"
      @handleClose="handleCloseStatus"
    ></Status>
    <!-- end -->
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from "vue"
import { ElMessage } from "element-plus"
import { useRouter, useRoute } from "vue-router"
import {formatTime} from "@/utils/index"
// 公用数据
import { statusData } from "@/utils/commonData"
// 接口api
import { deleteType, editCurriculumStatus } from "@/api/curriculum"
// 导入组件
// 删除弹出层
import Delete from "@/components/Delete/index.vue"
// 启用禁用
import Status from "@/components/Status/index.vue"
// 空页面
import EmptyPage from "@/components/EmptyPage/index.vue"
// 获取父组件值、方法
const props = defineProps({
  // 搜索对象
  baseData: {
    type: Array,
    default: () => [],
  },
  // 总条数
  total: {
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
const router = useRouter() //获取全局
const route = useRoute() //获取局部
const emit = defineEmits() //子组件获取父组件事件传值
const deleteText = ref("此操作将永久删除该分类，是否继续？")//删除信息
const statusText = ref("")
let typeData = reactive({}) //类型内容
let dialogDeleteVisible = ref(false) //控制删除弹层
let dialogStatusVisible = ref(false) //禁用启用弹层显示隐藏
let typeId = ref("") //分类Id
let isExpandAll = ref(false)
// ------定义方法------
// 启用禁用接口
const handleStatus = async (status) => {
  let params = {
    id: typeData.value.id,
    status: typeData.value.status == 1 ? 0 : 1,
  }
  await editCurriculumStatus(params)
    .then((res) => {
      if (res.code === 200) {
        let mes = ""
        if (status === 1) {
          mes = "禁用成功!"
        } else {
          mes = "启用成功!"
        }
        ElMessage({

          message: mes,
          type: "success",
          showClose:false,
        })
        handleCloseStatus()
        emit("getList")
      } else {
        ElMessage({

          message: res.data.msg,
          type: "error",
          showClose:false,
        })
      }
    })
    .catch((err) => { })
}
// 确定删除
const handleDelete = async () => {
  await deleteType(typeId.value)
    .then((res) => {
      if (res.code === 200) {
        handleClose()
        emit("getList")
      } else {
        ElMessage({

          message: res.data.msg,
          type: "error",
          showClose:false,
        })
      }
    })
    .catch((err) => { })
}
// 添加
const handleAdd = (row) => {
  emit("handleAdd", row)
}
// 编辑
const handleEdit = (row) => {
  emit("handleEdit", row)
}

// 打开删除弹层
const handleOpenDelete = (row) => {
  if (row.courseNum === 0) {
    if (row.status === 0) {
      dialogDeleteVisible.value = true
      typeId.value = row.id
    }
  } else {
    ElMessage({

      message: "该分类下含有课程，无法删除!",
      type: "error",
      showClose:false,
    })
  }
}
// 关闭删除弹层
const handleClose = () => {
  dialogDeleteVisible.value = false
}
// 启用禁用状态
const handleOpenStatus = (row) => {
  typeData.value = row
  if (row.status === 1) {
    dialogStatusVisible.value = true
    statusText.value = row.name
  } else {
    handleStatus(row.status)
  }

  // emit("getList");
}
// 关闭启用禁用状态弹层
const handleCloseStatus = () => {
  dialogStatusVisible.value = false
}
// 向父组件暴露方法
defineExpose({
  isExpandAll,
});
</script>
<style lang="scss" scoped>
:deep(.el-table) {
  .textInfo {
    padding-left: 10px;
  }
}

</style>
<style lang="scss">
.typeAction{
  .cell{
    padding: 0 !important;
  }
}
</style>