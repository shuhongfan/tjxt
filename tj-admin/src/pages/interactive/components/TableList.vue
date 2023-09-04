<!--回答列表-->
<template>
  <div>
    <div>
      <!-- 表格 -->
      <el-table :data="baseData" border stripe v-loading="loading">
        <el-table-column type="index" align="center" width="100" label="序号" />
        <el-table-column label="问题标题" min-width="300" class-name="textLeft">
          <template #default="scope">
            <div class="ellipsisHidden2">
              <el-popover
                placement="bottom-start"
                title=""
                width="300px"
                trigger="hover"
                :content="'' + '问题描述：' + scope.row.description"
                v-if="scope.row.description"
                  open-delay="200"
              >
                <template #reference>{{ scope.row.title }}</template>
              </el-popover>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop=""
          label="课程所属章节"
          width="350"
          class-name="textLeft"
        >
          <template #default="scope">
            {{ scope.row.chapterName }}-{{ scope.row.sectionName }}
          </template>
        </el-table-column>
        <el-table-column label="课程名称" prop="courseName" width="350">
        </el-table-column>
        <el-table-column prop="categoryName" label="课程分类" width="350">
        </el-table-column>
        <el-table-column prop="userName" label="提问学员昵称" min-width="150">
        </el-table-column>
        <el-table-column
          prop="answerAmount"
          label="回答数量"
          sortable
          min-width="150"
        >
        </el-table-column>
        <el-table-column
          prop="createTime"
          label="提问时间"
          min-width="220"
          :formatter="formatTime"
        >
        </el-table-column>
        <el-table-column prop="" label="用户端状态" min-width="140">
          <template #default="scope">
            {{ !scope.row.folded ? "显示" : "隐藏" }}
          </template>
        </el-table-column>
        <el-table-column prop="" label="问题状态" min-width="150">
          <template #default="scope">
            {{ scope.row.status === 0 ? "未查看" : "已查看" }}
          </template>
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
          align="center"
          min-width="115"
          class-name="textCenter"
        >
          <template #default="scope">
            <div class="operate">
              <span class="textDefault" @click="handleCheck(scope.row)"
                >查看</span
              >
              <span
                class="textWarning"
                @click="handleSetStatus(scope.row)"
                v-if="!scope.row.hidden"
              >
                隐藏
              </span>
              <span
                class="textDefault"
                @click="handleSetStatus(scope.row)"
                v-if="scope.row.hidden"
                >显示
              </span>
            </div>
          </template>
        </el-table-column>
        <!-- 空页面 -->
        <template #empty>
          <div class="emptyPageInter">
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
    </div>

    <!-- 显示隐藏 -->
    <ShowHidden
      :dialogVisible="dialogVisible"
      :statusText="statusText"
      @handleStatus="handleStatus"
      @handleClose="handleClose"
    ></ShowHidden>
    <!-- end -->
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from "vue";
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";
import { formatTime } from "@/utils/index";
// 接口api
import { setQuestionsFolded } from "@/api/question";
// 导入组件
// 显示隐藏弹出层
import ShowHidden from "@/components/Show/index.vue";
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
const statusText = ref(
  "此操作将隐藏该条提问及所属的回答和评价，是否继续隐藏？"
); //隐藏提示信息

let answerId = ref(""); //问答id
let Isfolded = ref(""); //是否隐藏：true隐藏、false显示
let dialogVisible = ref(false); //控制显示、隐藏弹层
let msg = ref(""); //提示信息
// ------定义方法------
// 显示隐藏
const handleStatus = async () => {
  let params = {
    id: answerId.value,
    hidden: Isfolded.value,
  };
  await setQuestionsFolded(params)
    .then((res) => {
      if (res.code === 200) {
        handleClose();
        ElMessage({

          message: msg.value,
          type: "success",
          showClose: false,
        });
        emit("getList");
      }
    })
    .catch((err) => {});
};
// 查看详情
const handleCheck = async (row) => {
  router.push({
    path: "/interactive/details/" + row.id,
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
const handleSetStatus = (row) => {
  answerId.value = row.id;
  Isfolded.value = row.hidden ? false : true;
  if (row.hidden) {
    msg.value = "该条问答已显示";
    handleStatus();
  } else {
    dialogVisible.value = true;
    msg.value = "该条问答已隐藏";
  }
};
// 关闭删除弹层
const handleClose = () => {
  dialogVisible.value = false;
};
</script>
<style lang="scss">
.textCenter{
  .cell{
    padding: 0 !important;
  }
}
</style>