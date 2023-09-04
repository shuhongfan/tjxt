<!--题目列表-->
<template>
  <div>
    <!-- 表格 -->
    <el-table :data="baseData" border stripe v-loading="loading">
      <el-table-column type="index" align="center" width="100" label="序号" />
      <el-table-column label="题目名称" min-width="300" class-name="textLeft">
        <template #default="scope">
          <div
            class="ellipsisHidden2 titleDrawer"
            :class="titleId === scope.row.id && drawer ? 'fouse' : ''"
            @click="handleDrawer(scope.row.id)"
            v-html="scope.row.name"
          ></div>
        </template>
      </el-table-column>
      <el-table-column
        prop="categories"
        label="所属分类"
        width="290"
        class-name="textLeft"
      >
        <template #default="scope">
          <div class="ellipsisHidden">
            <div
              class="ellipsisHidden15"
              v-for="(item, index) in scope.row.categories"
              :key="index"
            >
              {{ item }}
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="类型" min-width="150">
        <template #default="scope">
          <span v-if="scope.row.type === 1">单选题</span>
          <span v-if="scope.row.type === 2">多选题</span>
          <span v-if="scope.row.type === 3">不定向选择题</span>
          <span v-if="scope.row.type === 4">判断题</span>
        </template>
      </el-table-column>
      <el-table-column label="难易程度" min-width="150">
        <template #default="scope">
          {{
            difficultDesc(scope.row.difficulty)
          }}
        </template>
      </el-table-column>
      <el-table-column prop="score" label="分值" min-width="120">
      </el-table-column>
      <el-table-column
        prop="useTimes"
        label="引用次数"
        sortable
        min-width="150"
      >
      </el-table-column>
      <el-table-column
        prop="answerTimes"
        label="作答次数"
        sortable
        min-width="150"
      >
      </el-table-column>
      <el-table-column
        prop="updater"
        label="更新人"
        min-width="150"
      ></el-table-column>
      <el-table-column
        prop="updateTime"
        label="更新时间"
        sortable
        min-width="220"
        :formatter="formatTime"
      >
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        align="center"
        min-width="160"
        class-name="textRight"
      >
        <template #default="scope">
          <div class="operate">
            <!-- 禁止的时候不能触发查看和重置密码,因此按钮置灰 -->
            <span class="textDefault" @click="handleCheck(scope.row)">查看</span>
            <span class="textDefault" @click="handleEdit(scope.row)">编辑</span>
            <span class="textWarning" @click="handleOpenDelete(scope.row)"
              >删除</span
            >
          </div>
        </template>
      </el-table-column>
      <!-- 空页面 -->
      <template #empty>
        <div class="emptyPage">
          <EmptyPage :isSearch="isSearch" :baseData="baseData"></EmptyPage>
        </div>
        
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
    <!-- 题目名称抽屉 -->
    <TitledRawer
      :drawer="drawer"
      :detailData="detailData.value"
      @handleClose="handleDrawerClose"
    ></TitledRawer>
    <!-- end -->
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from "vue";
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";
import { removeHTMLTag } from "@/utils/index";
import {formatTime} from "@/utils/index"
// 公用数据
import { statusData } from "@/utils/commonData";
// 接口api
import { deleteTitle, getDetails } from "@/api/title";
// 导入组件
// 删除弹出层
import Delete from "@/components/Delete/index.vue";
// 标题抽屉弹层
import TitledRawer from "./titledRawer.vue";
// 空页面
import EmptyPage from "@/components/EmptyPage/index.vue";
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
});
// ------定义变量------
//初始化路由
const router = useRouter();
const emit = defineEmits(); //子组件获取父组件事件传值
const deleteText = ref("此操作将永久删除该题目，是否继续？"); //删除提示
const drawer = ref(false); //标题抽屉弹层显示隐藏
let titleId = ref(""); //要重置的题目id
let dialogDeleteVisible = ref(false); //控制删除弹层
let detailData = reactive({}); //详情数据
// ------定义方法------
const difficultArr = ["简单","中等","困难"]
// 难易程度
const difficultDesc = (i) =>{
  return difficultArr[i - 1];
}
// 获取详情
const getDetail = async () => {
  await getDetails(titleId.value)
    .then((res) => {
      if (res.code === 200) {
        detailData.value = res.data;
        detailData.value.answers.sort();
      }
    })
    .catch((err) => {});
};
// 确定删除
const handleDelete = async () => {
  await deleteTitle(titleId.value)
    .then((res) => {
      if (res.code === 200) {
        handleClose();
        ElMessage({

          message: "删除成功!",
          type: "success",
          showClose:false,
        });
        emit("getList");
      }
    })
    .catch((err) => {});
};
// 查看
const handleChck = (row) => {
  router.push({
    path: "/title/details/" + row.id,
  });
};
// 编辑
const handleEdit = (row) => {
  router.push({
    path: "/title/add/" + row.id,
  });
};
// 跳转到详情页
const handleCheck = (row) => {
  router.push({
    path: "/title/detail/"+row.id ,
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
  if (row.useTimes > 0) {
    ElMessage({

      message: "当前题目已被引用，无法删除!",
      type: "error",
      showClose:false,
    });
  } else {
    dialogDeleteVisible.value = true;
    titleId.value = row.id;
  }
};
// 关闭删除弹层
const handleClose = () => {
  dialogDeleteVisible.value = false;
};
// 打开标题名称抽屉
const handleDrawer = (id) => {
  drawer.value = true;
  titleId.value = id;
  getDetail();
};
// 关闭标题名称抽屉
const handleDrawerClose = () => {
  drawer.value = false;
};
</script>
<style lang="scss" scoped>
.deleteDialog{
  .el-dialog{
    .el-dialog__body{
      color: #887E7E;
    }
  }
}
</style>
<style lang="scss">
.textRight{
  .cell{
    padding: 0 !important;
  }
}
</style>