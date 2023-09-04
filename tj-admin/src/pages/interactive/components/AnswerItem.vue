<!-- 回答列表 -->
<template>
  <div class="bg-wt radius">
    <div class="titleInfo">回答列表</div>
    <!-- 表格 -->
    <el-table :data="itemData" border stripe v-loading="loading">
      <el-table-column type="index" align="center" width="100" label="序号" />
      <el-table-column label="学员昵称" min-width="180" class-name="textLeft">
        <template #default="scope">
          <div class="studentHead">
            <img :src="scope.row.userIcon" />
            {{ scope.row.userName }}
          </div>
        </template>
      </el-table-column>
      <el-table-column
        prop=""
        label="回答内容"
        width="350"
        class-name="textLeft"
      >
        <template #default="scope">
          <div class="ellipsisHidden2">
            <el-popover
              placement="top-start"
              title=""
              width="300px"
              trigger="hover"
              :content="scope.row.content"
              v-if="scope.row.content.length > 50"
            >
              <template #reference>{{ scope.row.content }}</template>
            </el-popover>
            <p v-else>{{ scope.row.content }}</p>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        prop="replyTimes"
        align="center"
        label="回答数量"
        width="150"
      >
      </el-table-column>
      <el-table-column align="center" label="点赞数量" width="150">
        <template #default="scope">
          <span class="praise">
            <i
              :class="!!scope.row.liked ? 'active' : ''"
              @click="handlePraise(scope.row)"
            ></i>
            {{ scope.row.likedTimes }}
          </span>
        </template>
      </el-table-column>
      <el-table-column
        prop="createTime"
        align="center"
        label="回答时间"
        min-width="200"
        :formatter="formatTime"
      >
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        align="center"
        min-width="220"
      >
        <template #default="scope">
          <div class="operate">
            <span class="textDefault" @click="handleReply(scope.row)"
              >回复</span
            >
            <span
              class="textDefault"
              :class="!scope.row.hidden ? 'textWarning' : ''"
              @click="handleSetStatus(scope.row)"
              >{{ !scope.row.hidden ? "隐藏" : "显示" }}</span
            >
            <span class="textDefault" @click="handleCheck(scope.row)"
              >查看更多</span
            >
          </div>
        </template>
      </el-table-column>
      <!-- 空页面 -->
      <template #empty>
        <EmptyPage :baseData="baseData"></EmptyPage>
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
    <!-- 回复弹层 -->
    <Reply
      :dialogVisible="dialogVisible"
      :questionId="questionId"
      ref="replyRef"
      @handleSubmit="handleSubmit"
      @handleClose="handleReplyClose"
    ></Reply>
    <!-- end -->
    <!-- 显示隐藏 -->
    <ShowHidden
      :dialogVisible="dialogShowVisible"
      :statusText="statusText"
      @handleStatus="handleStatus"
      @handleClose="handleClose"
    ></ShowHidden>
    <!-- end -->
    <!-- 返回按钮 -->
    <div class="bottomBtn">
      <div class="btn">
        <el-button class="button primary" @click="handleGetback"
          >返回</el-button
        >
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from "vue";
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";
import { formatTime } from "@/utils/index";
// 接口api
import { saveQuestionsReply, setAnswersFolded, setLiked } from "@/api/question";
// 导入组件
// 问题隐藏弹层提示
import ShowHidden from "@/components/Show/index.vue";
// 我要回复弹层
import Reply from "./Reply.vue";
// 空页面
import EmptyPage from "@/components/EmptyPage/index.vue";
// 获取父组件值、方法
const props = defineProps({
  // 搜索对象
  itemData: {
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
  // 问题Id
  baseData: {
    type: Object,
    default: () => ({}),
  },
});
// ------定义变量------
//初始化路由
const router = useRouter();
const emit = defineEmits(); //子组件获取父组件事件传值
const statusText = ref(
  "此操作将隐藏该条提问及所属的回答和评价，是否继续隐藏？"
); //删除提示
let replyRef = ref(); //回复弹层的ref
let Ishidden = ref(""); //是否隐藏：true隐藏、false显示
let dialogVisible = ref(false); //回复弹层显示/隐藏
let dialogShowVisible = ref(false); //控制删除弹层
let msg = ref(""); //提示信息
let targetReplyId = ref(null) //目标回复id
let targetUserId = ref(null) //目标用户id
// ------生命周期------
onMounted(() => {});
// ------定义方法------
// 提交我的回复
const handleSubmit = async () => {
  let reply = replyRef.value.fromData;
  let parent = {
    content: reply.content, //回复内容
    answerId: targetReplyId.value,
    // targetUserId: targetUserId.value,
    anonymity: false,
    questionId: props.baseData.id,
    isStudent: false
  };
  if (reply.content === undefined) {
    ElMessage({

      message: "回复内容不能为空，请输入",
      type: "error",
      showClose:false,
    });
    return false;
  }
  await saveQuestionsReply(parent)
    .then((res) => {
      if (res.code === 200) {
        ElMessage({

          message: "恭喜你，操作成功！",
          type: "success",
          showClose:false,
        });
        handleReplyClose(); //关闭回复弹层
        // 刷新回复列表
        emit("getList");
      }
    })
    .catch((err) => {});
};

// 确定隐藏
const handleStatus = async () => {
  let params = {
    id: targetReplyId.value,
    hidden: Ishidden.value,
  };
  await setAnswersFolded(params)
    .then((res) => {
      if (res.code === 200) {
        ElMessage({

          message: msg.value,
          type: "success",
          showClose:false,
        });
        emit("getList"); //刷新回答列表
        handleClose(); //关闭隐藏弹层
      }
    })
    .catch((err) => {});
};
// 点赞/取消点赞
const handlePraise = async (row) => {
  if (row.liked) {
    row.liked = false;
    row.likedTimes = row.likedTimes - 1;
  } else {
    row.liked = true;
    row.likedTimes = row.likedTimes + 1;
  }
  let parent = {
    id: row.id,
    liked: row.liked,
  };
  await setLiked(parent)
    .then((res) => {
      if (res.code === 200) {
        ElMessage({

          message: "恭喜你，操作成功！",
          type: "success",
          showClose:false,
        });
        // emit("getList"); //刷新回答列表
      }
    })
    .catch((err) => {});
};
// 触发隐藏显示按钮
const handleSetStatus = (row) => {
  const hidden = props.baseData.hidden;
  if (hidden) {
    ElMessage({

      message: "上一级回复为隐藏状态，无法显示",
      type: "error",
      showClose:false,
    });
  } else {
    targetReplyId.value = row.id;
    Ishidden.value = row.hidden ? false : true;
    if (row.hidden) {
      // 设置显示
      msg.value = "该条回答已显示";
      handleStatus();
    } else {
      // 打开隐藏弹层
      dialogShowVisible.value = true;
      msg.value = "该条回答已隐藏";
    }
  }
};
// 设置每页条数
const handleSizeChange = (val) => {
  emit("handleSizeChange", val);
};
// 当前页
const handleCurrentChange = (val) => {
  emit("handleCurrentChange", val);
};
// 关闭回答隐藏弹层
const handleClose = () => {
  dialogShowVisible.value = false;
};
// 打开我来回复弹层
const handleReply = (row) => {
  targetReplyId.value = row.id
  targetUserId.value = row.userId
  dialogVisible.value = true;
};
// 关闭我来回复弹层
const handleReplyClose = () => {
  dialogVisible.value = false;
};
// 查看更多,点击去回答详情页
const handleCheck = (row) => {
  router.push({
    path: "/interactive/replies",
    query: {
      // row: encodeURIComponent(JSON.stringify(row)),
      id: row.id,
      qId: props.baseData.id
    }, //路由只是对象传递,因为没有评论回答详情,所以需要把列表页的数据传递给回答页
  });
};
// 底部返回按钮
const handleGetback = (row) => {
  router.push({
    path: "/interactive/index",
  });
};
</script>
<style lang="scss" scoped>
:deep(.emptyPageBox) {
  min-height: 100%;
}
:deep(.el-table--scrollable-x .el-table__body-wrapper){
  margin-bottom: 0;
  padding-bottom: 0;
}
.bottomBtn{
  margin-top: 40px;
  border-top: 1px solid #F5EFEE;
  .btn{
    .button{
      width: 130px;
      height: 40px;
      background: #FF734F;
    }
  }
}
</style>