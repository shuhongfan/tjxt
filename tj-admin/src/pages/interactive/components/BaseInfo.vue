<!-- 问题详情 -->
<template>
  <div class="bg-wt radius marg-tp-20">
    <div class="questionBox">
      <div class="head">
        <img :src="baseData.userIcon || ''" />
        {{baseData.userName || ''}}
      </div>
      <div class="con">
        <div class="title">
          <div class="info">{{ baseData.title }}</div>
          <div class="btn">
            <el-button
              @click="handleReply"
              class="button primary"
              v-preventReClick
              >我来回复</el-button
            >
            <el-button
              @click="handleSetStatus"
              v-preventReClick
              class="button buttonSub"
              >{{ baseData.hidden ? "显示" : "隐藏" }}</el-button
            >
          </div>
        </div>
        <div class="time"><span>提问时间：</span>{{ formatTimeOrdinary(baseData.createTime)}}</div>
        <div class="description">
          {{ baseData.description }}
        </div>
      </div>
      <div class="food">
        <ul>
          <li><span class="littletitle">课程分类：</span><span>{{ baseData.categoryName }}</span></li>
          <li>课程名称：{{ baseData.courseName }}</li>
          <li class="end">
            课程负责老师：{{
              baseData.teacherName || ""
            }}
          </li>
          <li>
            <p class="littletitle">所属章节：</p><p>{{ baseData.chapterName }}/{{ baseData.sectionName }}</p>
          </li>
          <li>
            回答数量：{{
              baseData.answerAmount === 0 ? "暂无" : baseData.answerAmount
            }}
          </li>
          <li class="end">用户端状态：{{ baseData.hidden ? "隐藏" : "显示" }}</li>
        </ul>
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
import { ref, reactive, onMounted } from "vue"
import { ElMessage } from "element-plus"
import { formatTimeOrdinary } from "@/utils/index";
// 获取vuex存储数据
import { useUserStore } from "@/store"
// 导入组件
// 接口api
import { saveQuestionsReply, setQuestionsFolded } from "@/api/question"

// 导入组件
// 我要回复弹层
import Reply from "./Reply.vue"
// 问题隐藏弹层提示
import ShowHidden from "@/components/Show/index.vue"
// 获取父组件值、方法
const props = defineProps({
  // 详情数据
  baseData: {
    type: Object,
    default: () => ({}),
  },
})
// ------定义变量------
const emit = defineEmits() //子组件获取父组件事件传值
let replyRef = ref() //回复弹层的ref
let dialogVisible = ref(false) //回复弹层显示/隐藏
let dialogShowVisible = ref(false) //问题弹层的显示/隐藏
let statusText = ref("此操作将隐藏该条提问及所属的回答和评价，是否继续隐藏？") //隐藏弹层提示的信息
let targetReplyId = ref(null) //问题id
// ------生命周期------
onMounted(() => { })
// ------定义方法------
// 确认隐藏问题
const handleStatus = async () => {
  let baseData = props.baseData
  let parent = {
    id: baseData.id,
  }
  // 判断当前的问题是显示的还是隐藏
  if (!baseData.hidden) {
    parent.hidden = true
  } else {
    parent.hidden = false
  }
  await setQuestionsFolded(parent)
    .then((res) => {
      if (res.code === 200) {
        ElMessage({

          message: "恭喜你，操作成功！",
          type: "success",
          showClose:false,
        })
        emit("getDetailData") //刷新详情
        // 刷新回复列表
        emit("getList")
        handleStatusClose() //隐藏弹层
      }
    })
    .catch((err) => { })
}
// 提交我的回复
const handleSubmit = async () => {
  let reply = replyRef.value.fromData
  let parent = {
    content: reply.content,
    anonymity: false,
    questionId: props.baseData.id,
    isStudent: false
  }
  if (reply.content === undefined) {
    ElMessage({

      message: "回复内容不能为空，请输入",
      type: "error",
      showClose:false,
    })
    return false
  }
  await saveQuestionsReply(parent)
    .then((res) => {
      if (res.code === 200) {
        ElMessage({

          message: "恭喜你，操作成功！",
          type: "success",
          showClose:false,
        })
        handleClose() //关闭弹层
        // 刷新回复列表
        emit("getList")
      }
    })
    .catch((err) => { })
}
// 触发显示隐藏按钮
const handleSetStatus = () => {
  //如果问题是隐藏的,直接调用显示/隐藏接口,无需弹层
  if (props.baseData.folded) {
    handleStatus()
  } else {
    // 如果要隐藏,选哟打开隐藏接口确认下是否要隐藏问题
    dialogShowVisible.value = true
  }
}
// 关闭隐藏弹层
const handleStatusClose = () => {
  dialogShowVisible.value = false
}
// 打开我来回复弹层
const handleReply = () => {
  targetReplyId.value = props.baseData.id
  dialogVisible.value = true
}
// 关闭我来回复弹层
const handleClose = () => {
  dialogVisible.value = false
};
</script>
<style lang="scss" scoped>
.questionBox .food ul li{
  // width: 40% !important;
}
.end{
  width: 25% !important;
}
.littletitle{
  // display: block !important;
  white-space: pre;//pre意为保留空格
}
</style>