<!-- 回答详情 -->
<template>
  <div class="bg-wt radius marg-tp-20">
    <div class="repliesBox">
      <div class="head">
        <div class="info">
          <img
            :src="baseData.userIcon || ''"
          />{{ baseData.userName || "" }}
        </div>

        <div class="btn">
          <el-button
            @click="handleReply"
            class="button primary"
            v-preventReClick
            >我来评论</el-button
          >
          <el-button
            @click="handleSetStatus"
            v-preventReClick
            class="button buttonSub"
            >{{ baseData.hidden ? "显示" : "隐藏" }}</el-button
          >
        </div>
      </div>
      <div class="time">回答时间：{{ formatTimeOrdinary(baseData.createTime) }}</div>
      <div class="con">
        <div class="description">
          {{ baseData.content }}
        </div>
      </div>
      <div class="repliesFood">
        <div class="praise">
          <i :class="baseData.liked ? 'active' : ''" @click="handlePraise"></i>
          {{ baseData.likedTimes }}
        </div>
        <div class="text">回答数量：{{ baseData.replyTimes }}</div>
      </div>
    </div>
  </div>
  <!-- 回复弹层 -->
  <Reply
    :dialogVisible="dialogVisible"
    :targetReplyId="targetReplyId"
    ref="replyRef"
    @handleSubmit="handleSubmit"
    @handleClose="handleClose"
  ></Reply>
  <!-- end -->
  <!-- 问题显示/隐藏弹层 -->
  <ShowHidden
    :dialogVisible="dialogShowVisible"
    :statusText="statusText"
    @handleStatus="handleStatus"
    @handleClose="handleStatusClose"
  ></ShowHidden>
  <!-- end -->
</template>
<script setup>
import { ref, reactive, onMounted } from "vue";
import { ElMessage } from "element-plus";
import { formatTimeOrdinary } from "@/utils/index";
// 获取vuex存储数据
import { useUserStore } from "@/store";
import { useRouter, useRoute } from "vue-router";
// 导入组件
// 接口api
import { saveQuestionsReply, setAnswersFolded, setLiked } from "@/api/question";

// 导入组件
// 我要回复弹层
import Reply from "./Reply.vue";
// 问题隐藏弹层提示
import ShowHidden from "@/components/Show/index.vue";
// 获取父组件值、方法
const props = defineProps({
  // 详情数据
  baseData: {
    type: Object,
    default: () => ({}),
  },
  questionId: {
    type: String
  },
});
// ------定义变量------
const emit = defineEmits(); //子组件获取父组件事件传值
const route = useRoute(); //获取局部
let replyRef = ref(); //回复弹层的ref
let dialogVisible = ref(false); //回复弹层显示/隐藏
let dialogShowVisible = ref(false); //问题弹层的显示/隐藏
let statusText = ref("此操作将隐藏该条提问及所属的回答和评价，是否继续隐藏？"); //隐藏弹层提示的信息
let targetReplyId = ref(null); //目标评论id
let targetUserId = ref(null); //目标用户id
// ------生命周期------
onMounted(() => {});
// ------定义方法------
// 确认隐藏回答
const handleStatus = async () => {
  let baseData = props.baseData;
  let parent = {
    id: baseData.id,
  };
  // 判断当前的回答是显示的还是隐藏
  if (!baseData.hidden) {
    parent.hidden = true;
  } else {
    parent.hidden = false;
  }
  await setAnswersFolded(parent)
    .then((res) => {
      if (res.code === 200) {
        ElMessage({

          message: "恭喜你，操作成功！",
          type: "success",
          showClose:false,
        });
        emit("setFolded", parent.hidden); //设置是显示还是隐藏
        emit("getList"); //刷新回答列表
        handleStatusClose(); //隐藏弹层
      }
    })
    .catch((err) => {});
};
// 提交我的回复
const handleSubmit = async () => {
  let reply = replyRef.value.fromData;
  let parent = {
    content: reply.content,
    // targetReplyId: targetReplyId.value,
    // targetUserId: targetUserId.value,
    answerId: props.baseData.id,
    anonymity: false,
    questionId: props.questionId,
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
  await getQuestionsReply(parent)
    .then((res) => {
      if (res.code === 200) {
        ElMessage({

          message: "恭喜你，操作成功！",
          type: "success",
          showClose:false,
        });
        handleClose(); //关闭弹层
        // 刷新评论列表
        emit("getList");
      }
    })
    .catch((err) => {});
};
// 触发显示隐藏按钮
const handleSetStatus = () => {
  console.log(props.baseData)
  const hidden = props.baseData.replyFolded;
  if (hidden) {
    ElMessage({

      message: "上一级回复为隐藏状态，无法显示",
      type: "error",
      showClose:false,
    });
  } else {
    // 如果回答是隐藏的, 直接调用显示 / 隐藏接口, 无需弹层;
    if (props.baseData.hidden) {
      handleStatus();
    } else {
      // 如果要隐藏,打开隐藏接口确认下是否要隐藏问题
      dialogShowVisible.value = true;
    }
  }
};
// 关闭隐藏弹层
const handleStatusClose = () => {
  dialogShowVisible.value = false;
};
// 打开我来回复弹层
const handleReply = () => {
  
  let row=props.baseData
  targetReplyId.value = row.id;
  targetUserId.value = row.userId;
  dialogVisible.value = true;
};
// 关闭我来回复弹层
const handleClose = () => {
  dialogVisible.value = false;
};
// 点赞
const handlePraise = async () => {
  let baseData = props.baseData;
  if (baseData.liked) {
    baseData.liked = false;
    baseData.likedTimes = baseData.likedTimes - 1;
  } else {
    baseData.liked = true;
    baseData.likedTimes = baseData.likedTimes + 1;
  }
  let parent = {
    id: baseData.id,
    liked: baseData.liked,
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
</script>
<style lang="scss" scoped>
.button{
  width: 90px;
}
</style>
