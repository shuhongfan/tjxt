<!--课程管理列表-->
<template>
  <div>
    <!-- 表格 -->
    <el-table
      :data="baseData"
      border
      stripe
      v-loading="loading"
      row-key="id"
      ref="tableItem"
    >
      <el-table-column type="index" align="center" width="100" label="序号" />
      <el-table-column label="课程名称" min-width="250" class-name="textLeft">
        <template #default="scope">
          <div class="head">
            <div class="ellipsisHidden2">
              {{ scope.row.name }}
            </div>
          </div>
        </template>
      </el-table-column>
      <!-- 当数据中出现小数，导致排序错乱时用 sort来解决 -->
      <el-table-column
        label="课程价格"
        prop="price"
        sortable
        width="150"
        :sort-method="
          (a, b) => {
            return a.price - b.price;
          }
        "
      >
        <template #default="scope">
          <span class="fontTip ft-wt-600">{{
            scope.row.price === "0" ? "免费" : "￥" + scope.row.price
          }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="sections" label="课时" min-width="150">
      </el-table-column>
      <el-table-column
        prop="categories"
        label="所属分类"
        width="290"
        class-name="textLeft"
      >
      </el-table-column>
      <el-table-column
        prop="sold"
        label="报名人数"
        min-width="150"
        v-if="props.status !== 1"
      >
      </el-table-column>
      <el-table-column
        prop="score"
        label="课程评分"
        min-width="150"
        sortable
        v-if="props.status !== 1"
      >
        <template #default="scope"> {{ scope.row.score / 10 }}分 </template>
      </el-table-column>
      <el-table-column
        prop="updaterName"
        label="更新人"
        min-width="150"
      ></el-table-column>
      <el-table-column
        prop="updateTime"
        label="更新时间"
        sortable
        min-width="200"
        :formatter="formatTime"
      >
      </el-table-column>
      <el-table-column
        prop="publishTime"
        label="上架时间"
        sortable
        min-width="200"
        :formatter="formatTime"
        v-if="props.status !== 1"
      >
      </el-table-column>
      <el-table-column fixed="right" label="操作" align="center" width="170">
        <template #default="scope">
          <div class="operate">
            <span
              class="textDefault"
              @click="handleCheck(scope.row)"
              v-if="scope.row.status === 2 || scope.row.status === 4"
              >查看</span
            >
            <span
              :class="
                scope.row.status === 2 || scope.row.status === 4
                  ? 'textForbidden'
                  : 'textDefault'
              "
              v-if="scope.row.status === 1 || scope.row.status === 3"
              @click="handleEdit(scope.row)"
              >编辑</span
            >
            <!-- <span
                :class="
                  scope.row.status === 4 && scope.row.step !== 5
                    ? 'textForbidden'
                    : 'textWarning'
                "
                @click="handleOpenDescend(scope.row)"
                >{{
                  scope.row.status === 1 || scope.row.status === 3
                    ? "上架"
                    : "下架"
                }}</span
              > -->

            <span
              v-if="props.status === 1 || props.status === 3"
              :class="scope.row.step !== 5 ? 'textForbidden' : 'textDefault'"
              @click="handleOpenDescend(scope.row)"
              >上架</span
            >
            <span
              v-else
              @click="handleOpenDescend(scope.row)"
              :class="scope.row.status === 4 ? 'textForbidden' : 'textWarning'"
            >
              下架
            </span>
            <span
              :class="scope.row.status === 1 ? 'textWarning' : 'textForbidden'"
              @click="handleOpenDelete(scope.row)"
            >
              删除
            </span>
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
    <div v-if="total > 10">
      <el-pagination
        v-if="pageShow"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :page-sizes="[10, 20, 30, 40]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="Number(total)"
        class="paginationBox"
      >
      </el-pagination>
    </div>
    <!-- end -->
    <!-- 删除 -->
    <Delete
      :dialogDeleteVisible="dialogDeleteVisible"
      :deleteText="deleteText"
      @handleDelete="handleDelete"
      @handleClose="handleClose"
    ></Delete>
    <!-- end -->
    <!-- 上架、下架 -->
    <CourseStatus
      :dialogStatusVisible="dialogStatusVisible"
      :statusText="statusText"
      :title="title"
      :courseData="courseData"
      :coursesesId="coursesesId"
      @handleDescend="handleDescend"
      @handleCloseDescend="handleCloseDescend"
    ></CourseStatus>
    <!-- end -->
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from "vue";
import { ElMessage } from "element-plus";
import { useRouter, useRoute } from "vue-router";
import { formatTime } from "@/utils/index";
// 获取vuex存储数据
import { useUserStore } from "@/store";
// 接口api
import {
  deleteCourses,
  baseUpShelf,
  baseDownShelf,
  baseBeforeUpShelf,
} from "@/api/curriculum";
// 导入组件
// 删除弹出层
import Delete from "@/components/Delete/index.vue";
// 上架下架弹出层
import CourseStatus from "@/components/Coursestatus/index.vue";
// 空页面
import EmptyPage from "@/components/EmptyPage/index.vue";
// 获取父组件值、方法
const props = defineProps({
  // 搜索对象
  baseData: {
    type: Array,
    default: () => [],
  },
  // 状态
  total: {
    type: Number,
    default: 0,
  },

  // 总条数
  status: {
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
  // tab切换是分页重新加载，解决tab切换后分页的当前页不刷新问题
  pageShow: {
    type: Boolean,
    default: false,
  },
});
// ------定义变量------
// ------vuex存储数据------
const store = useUserStore();
const emit = defineEmits(); //子组件获取父组件事件传值
const router = useRouter(); //获取全局
const route = useRoute(); //获取局部
const deleteText = ref("此操作将永久删除该课程，是否继续？"); //删除提示
const statusText = ref(""); //上架下架提示
const status = ref(null); //上架下架类型
let title = ref(""); //弹层标题
let coursesesId = ref(""); //课程id
let courseData = ref({}); //获取课程详情（下架弹层用）
let dialogDeleteVisible = ref(false); //控制删除弹层
let dialogStatusVisible = ref(false); //控制上架下架弹层
const tableItem = ref();
// ------定义方法------
// 下架提交
const handleDescend = async () => {
  if (status.value === 1 || status.value === 3) {
    // 上架接口
    await baseUpShelf({ id: coursesesId.value }).then((res) => {
      if (res.code === 200) {
        ElMessage({

          message: "上架成功!",
          type: "success",
          showClose: false,
        });
      } else {
        ElMessage({

          message: res.data.msg,
          type: "error",
          showClose: false,
        });
      }
    });
  } else {
    // 下架接口
    await baseDownShelf({ id: coursesesId.value }).then((res) => {
      if (res.code === 200) {
        ElMessage({

          message: "下架成功!",
          type: "success",
          showClose: false,
        });
      } else {
        ElMessage({

          message: res.data.msg,
          type: "error",
          showClose: false,
        });
      }
    });
  }
  emit("getList"); //刷新列表
  handleCloseDescend(); //关闭上架下架弹层
};
// 打开上架、下架弹层
const handleOpenDescend = async (row) => {
  let time = new Date(row.purchaseEndTime).getTime();
  let newTime = new Date(new Date()).getTime();
  if (time < newTime) {
    ElMessage({

      message: "下架时间需要晚于当前时间",
      type: "error",
      showClose: false,
    });
  } else {
    coursesesId.value = row.id; //课程id
    status.value = row.status; //课程状态
    courseData.value = row;

    if (props.status === 3) {
      await baseBeforeUpShelf(row.id).then((res) => {
        if (res.code === 200) {
          dialogStatusVisible.value = true; //
          title.value = "确认上架";
        } else {
          ElMessage({
            showClose: true,
            message: res.data.msg,
            type: "error",
            showClose: false,
          });
        }
      });
    } else {
      if (row.purchaseEndTime) {
        dialogStatusVisible.value = true; //
        if (status.value === 1) {
          title.value = "确认上架";
        } else {
          title.value = "确认下架";
          statusText.value = "此操作将下架该课程，是否继续？";
        }
      }
    }
  }
};
// 隐藏上架下架弹层
const handleCloseDescend = () => {
  dialogStatusVisible.value = false;
};
// 确定删除
const handleDelete = async () => {
  await deleteCourses(coursesesId.value)
    .then((res) => {
      if (res.code === 200) {
        handleClose();
        setTimeout(() => {
          emit("getList"); //刷新列表
        }, 1000);
        ElMessage({

          message: "删除成功!",
          type: "success",
          showClose: false,
        });
      }
    })
    .catch((err) => {});
};
// 查看
const handleEdit = (row) => {
  // 设置当前可以触发的步骤
  store.setStepNum(row.step);
  if (row.status === 1 || row.status === 3) {
    router.push({
      path: "/curriculum/add/" + row.id,
    });
  }
};
// 查看
const handleCheck = (row) => {
  router.push({
    path: "/curriculum/details/" + row.id,
  });
};
// 设置每页条数
const handleSizeChange = (val) => {
  emit("handleSizeChange", val);
};
// 当前页
const handleCurrentChange = (val) => {
  emit("handleCurrentChange", val);
};
// 打开删除弹层
const handleOpenDelete = (row) => {
  if (row.status === 1) {
    dialogDeleteVisible.value = true;
    coursesesId.value = row.id;
  }
};
// 关闭删除弹层
const handleClose = () => {
  dialogDeleteVisible.value = false;
};

// 向父组件暴露方法
defineExpose({
  tableItem,
});
</script>
<style lang="scss" scoped>
:deep(.el-table__body-wrapper tr td.el-table-fixed-column--right) {
  .cell {
    padding: 0 20px;
  }
}
// .paginationBox{
//   padding-top: 0;
// }
:deep(.el-table--scrollable-x .el-table__body-wrapper) {
  // padding-bottom: 37px;
}
</style>