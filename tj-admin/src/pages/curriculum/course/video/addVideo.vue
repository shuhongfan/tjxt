<template>
  <div class="dialogMain videoBox dialogHeignt">
    <el-dialog
      v-model="dialogVisible"
      title="选择媒资视频"
      :before-close="handleClose"
    >
      <div class="searchForm">
        <el-form ref="ruleForm" :inline="true" :model="searchData">
          <el-form-item label="" prop="name" class="mediaSearch">
            <el-input
              placeholder="请选择视频名称关键字"
              v-model="searchData.name"
              clearable
              :prefix-icon="Search"
              class="el-input w-50 m-2"
              @clear="handleClear"
            />
            <div class="btn pd-tp-0">
              <el-button class="button primary" @click="handleSearch"
                >搜索</el-button
              >
            </div>
          </el-form-item>
        </el-form>
      </div>
      <!-- 表格 -->
      <div class="tableBox">
        <el-table
          :data="baseData.value"
          ref="regTable"
          border
          stripe
          height="460"
          v-loading="loading"
          @row-click="handleTable"
          min-width="1094px"
        >
          <el-table-column label="选择" width="80">
            <template #default="scope">
              <el-radio v-model="sourceId" :label="scope.row.id"
                ><i></i
              ></el-radio>
            </template>
          </el-table-column>
          <el-table-column
            type="index"
            align="center"
            width="80"
            label="序号"
          />
          <el-table-column prop="filename" label="视频名称" min-width="150" />
          <el-table-column prop="size" label="大小（MB）" min-width="120">
            <template #default="scope">
              {{ (scope.row.size / 1024 / 1024).toFixed(2) }}
            </template>
          </el-table-column>
          <el-table-column prop="duration" label="视频时长" min-width="120">
            <template #default="scope">
              {{ formatSeconds(scope.row.duration) }}
            </template>
          </el-table-column>
          <el-table-column prop="creater" label="上传人" min-width="120">
          </el-table-column>
          <el-table-column prop="useTimes" label="当前引用次数" min-width="120">
          </el-table-column>
          <el-table-column
            prop="createTime"
            min-width="220"
            label="上传时间"
            width="180px"
          >
            <template #default="scope">
              {{ formatTime(scope.row.createTime) }}
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
        <!-- <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :page-sizes="[10, 20, 30, 40]"
          :page-size="10"
          layout="total, sizes, prev, pager, next, jumper"
          :total="Number(total)"
          class="paginationBox"
        >
        </el-pagination> -->
        <!-- end -->
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button class="button buttonPrimary" @click="handleClose"
            >取消</el-button
          >
          <el-button class="button primary" @click="handleSubmit"
            >提交</el-button
          >
        </span>
      </template>
    </el-dialog>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted, watch } from "vue"
import { ElMessage } from "element-plus"
import { formatSeconds } from "@/utils/index"
import moment from "moment"
// 接口api
// 媒资接口
import { getMedia } from "@/api/media"
// import { addCurriculumType, editCurriculumType } from "@/api/curriculum";
// 空页面
import EmptyPage from "@/components/EmptyPage/index.vue"
// 获取父组件值、方法
const props = defineProps({
  // 弹层隐藏显示
  dialogVisible: {
    type: Boolean,
    default: false,
  },
  itemData: {
    typeof: Object,
    default: () => ({}),
  }
})
// ------定义变量------
const emit = defineEmits() //子组件获取父组件事件传值
const loading = ref(false)
let total = ref(null) //数据总条数
let baseData = reactive([]) //表格数据
let searchData = reactive({
  pageSize: 10000,
  pageNo: 1,
  sortBy: "id",
  isAsc: false,
}) //搜索对象
let sourceId = ref(null)
let sourceData = reactive({}) //视频信息
let isSearch = ref(false) //是否触发了搜索按钮,用来控制没有搜索出数据和正常列表无数据的区分，显示的图片和提示语不一样
// ------生命周期------
onMounted(() => {
  init()
})
watch(() => props.itemData, (newValue, oldValue) => {
  //  console.log(newValue)
})
// ------定义方法------
// 获取初始值
const init = () => {
  getList()
}
// 获取列表值
const getList = async () => {

  loading.value = true
  await getMedia(searchData)
    .then((res) => {
      if (res.code === 200) {
        loading.value = false
        baseData.value = res.data.list
        total.value = res.data.total
        // 给弹层列表已经选择过的视频置灰
        baseData.value.forEach(value => {
          props.itemData.forEach((obj) => {
            obj.sections.forEach(val => {
              if (value.id === val.mediaId) {
                value.disabled = true
              }
            })
          })
        })
      }
    })
    .catch((err) => { })
}
// 搜索
const handleSubmit = async () => {
  if (sourceData.value === undefined) {
    ElMessage({

      message: "请选择一个视频文件",
      type: "error",
      showClose: false,
    })
  } else {
    emit("setVideoInfo", sourceData.value)
    clearSearch()
    handleClose()
  }
}
// 清除搜索名字
const clearSearch = () => {
  searchData.name = null
}
// 搜索
const handleSearch = () => {
  isSearch.value = true //是否触发了搜索按钮
  getList()
}
// 设置每页条数
const handleSizeChange = (val) => {
  searchData.pageSize = val
  // 刷新列表
  getList()
}
// 当前页
const handleCurrentChange = (val) => {
  searchData.pageNo = val
  // 刷新列表
  getList()
}
// 关闭弹层
const handleClose = () => {
  clearSearch()
  emit("handleClose")
  sourceId.value = null //关闭弹层清除选中的视频
  getList()
}
// 触发表格
const handleTable = (item) => {
  sourceId.value = item.id
  sourceData.value = item
}
// 清空搜索框
const handleClear = () => {
  isSearch.value = false
  // 刷新列表
  getList()
}
// 时间格式化
const formatTime = (time) => {
  return moment(time).format("YYYY.MM.DD HH:mm:ss")
};
</script>
<style lang="scss" scoped>
.videoBox {
  .searchBox {
    padding-bottom: 5px;
  }
  .tableBox {
    padding-top: 0;
    :deep(.el-table) {
      .el-table__header-wrapper {
        .el-table__cell {
          color: #332929;
          padding: 0 !important;
        }
      }
    }
    :deep(.el-table__row) {
      color: #332929;
    }
  }
  :deep(.el-radio) {
    margin-left: 45px;
  }
  // :deep(.el-dialog) {
  //   height: 580px;
  // }
}
:deep(.el-input) {
  .el-input__wrapper {
    .el-input__inner {
      &::placeholder {
        color: #b5abab;
      }
    }
  }
}
:deep(.el-dialog) {
  .el-dialog__body {
    padding: 20px 30px 30px 30px;
  }
}
:deep(.el-scrollbar) {
  overflow: initial;
  .el-scrollbar__bar {
    position: absolute;
    right: -30px;
  }
}
// :deep(.el-table__body-wrapper) {
//   overflow: initial;
// }
// :deep(.el-table) {
//   overflow: initial;
// }
//调整表头高度
:deep(.el-table__header tr),
:deep(.el-table__header th) {
  padding: 0;
  height: 40px;
}
:deep(.topicBox) {
  .tableBox {
    .el-table {
      .el-table__header-wrapper {
        .el-table__cell {
          padding: 0;
        }
      }
    }
  }
}
</style>
<style lang="scss" scoped>
:deep(.el-scrollbar__bar.is-vertical > div) {
  background-color: rgba(0, 0, 0, 0.45);
}
:deep(.tableBox .el-table .cell) {
  padding: 0;
}
:deep(.el-input) {
  width: 293px;
}
</style>
