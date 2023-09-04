<!-- 提问、回复详情 -->
<template>
  <div class="askDetailsWrapper">
    <div class="container" v-if="askInfo">
        <div class="fx-sb ">
          <div class="fx-1 marg-rt-20">
            <!-- 问题主体- start -->
            <div class="askCont bg-wt marg-bt-20">
              <div class="userInfo">
                <img v-if="askInfo.userIcon" :src="askInfo.userIcon" alt="">
                <img v-else src="/src/assets/anonymity.png" alt="">
                {{askInfo.userName || '匿名'}}
              </div>
              <div class="askInfo">
                <div class="ft-20 ft-wt-600">{{askInfo.title}}</div>
                <div class="ft-cl-des marg-bt-10">{{askInfo.createTime}}</div>
                <div>{{askInfo.description}}</div>
              </div>
            </div> 
            <!-- 问题主体- end -->
            <!-- 回答题主- start -->
            <div class="answerCont bg-wt marg-bt-20">
              <div class="ft-20 ft-wt-600 marg-bt-20">我要回答</div>
              <div class="answer fx">
                <img :src="store.getUserInfo.icon" alt="" srcset="">
                <div class="fx-1">
                  <el-input v-model="description" rows="11" type="textarea" @input="ruleshandle" maxlength="500" show-word-limit placeholder="请发表高见" />
                  <div class="fx-sb fx-al-ct">
                    <div><el-checkbox v-model="anonymity" label="匿名提问" size="large" /></div>
                    <div class="subCont">
                      <span class="bt ft-14" :class="{'bt-dis':!isSend}" @click="answerHandle('first')">回答</span>
                      </div>
                  </div> 
                </div>
              </div>
            </div>
            <!-- 回答题主- end -->
            <!-- 全部回答- start -->
            <div class="answerCont bg-wt marg-bt-20">
              <div class="ft-20 ft-wt-600 marg-bt-20">全部回答({{count}})</div>
              <div class="answerItems">
                <div class="items" v-for="item in questionData" :key="item.id">
                  <div class="fx-al-ct">
                    <img class="img" v-if="item.userIcon" :src="item.userIcon" alt="">
                    <img class="img" v-else src="/src/assets/anonymity.png" alt="">
                    <span class="ft-cl-des">{{item.userName || '匿名'}}</span>
                  </div>
                  <div class="cont">
                    <div class="marg-bt-10">{{item.content}}</div>
                    <div class="fx-sb">
                      <div class="ft-cl-des">{{item.createTime}}</div>
                      <div>
                        <span class="marg-rt-10 cur-pt" @click="openReply(item)"> <i class="iconfont zhy-a-btn_pinglun_nor2x"></i> 评论({{item.replyTimes}}) </span>
                        <span :class="{'cur-pt':true, activeLiked: item.liked}" @click="likedHandle(item)"> <i class="iconfont zhy-a-btn_zan_nor2x"></i> 点赞( {{item.likedTimes}})</span>
                      </div>
                    </div>
                  </div>
                  <!-- 插入回复框的位置 -->
                  <component :is="openReplyFormId == item.id ? ReplayForm : null" :key="item.id" :name="item.userName || '匿名'" :askInfoId="askInfo.id" @commentHandle="commentHandle"></component>
                  <!-- 回复列表 -->
                  <div class="replyCont" v-show="replyData && isReplay == item.id">
                    <div class="items" v-for="it in replyData" :key="it.id">
                      <div class="fx-al-ct">
                        <img class="img" v-if="it.userIcon" :src="it.userIcon" alt="">
                        <img class="img" v-else src="/src/assets/anonymity.png" alt="">
                        <span class="ft-cl-des"> {{it.userName || '匿名'}} 回复 {{it.targetUserName || "匿名用户"}} </span>
                      </div>
                      <div class="cont">
                        <div class="marg-bt-10">{{it.content}}</div>
                        <div class="fx-sb">
                          <div class="ft-cl-des">{{it.createTime}}</div>
                          <div>
                            <span class="marg-rt-10 cur-pt" @click="replayHandle(it, 'targe')" > <i class="iconfont zhy-a-btn_pinglun_nor2x"></i> 回复</span>
                            <span :class="{'cur-pt':true, activeLiked: it.liked}" @click="likedHandle(it)"> <i class="iconfont zhy-a-btn_zan_nor2x"></i> 点赞 ({{it.likedTimes}})</span>
                          </div>
                        </div>
                      </div>
                      <!-- 插入回复框的位置 -->
                      <component :is="openReplyFormId == it.id ? ReplayForm : null" :name="it.userName || '匿名'" :id = "it.userId" :askInfoId="askInfo.id"  @commentHandle="commentHandle"></component>
                    <!-- 回复列表 -->
                    </div>
                    <div @click="() => {dialogTableVisible = true}" class="fx-ct ft-14 ft-cl-des cur-pt" v-if="replyCont > 5">
                      点击查看全部{{replyCont}}条回复
                    </div>
                  </div>
                </div>
                <div></div>
                <p @click="clickLoad" v-if="!noMore" class="fx-ct ft-14 ft-cl-des">点击查看更多</p>
                <p class="fx-ct ft-14 ft-cl-des" v-if="noMore">没有更多了</p>
              </div>
            </div>
          </div>
          <!-- 相关问题 写死 -->
          <RelatedQuestions :id="route.query.detailsId" :title="route.query.name"></RelatedQuestions>
      </div>
    </div>
    <el-dialog v-model="dialogTableVisible" :title="`全部回复(${replyCont})`" width="80%" top="5vh" >
      <div class="dialogReplyCont" v-infinite-scroll="load" style="overflow: auto" :infinite-scroll-disabled="disabled">
        <div class="items" v-for="it in replyData" :key="`ss${it.id}`">
          <div class="fx-al-ct">
            <img class="img" v-if="it.userIcon" :src="item.userIcon" alt="">
            <img class="img" v-else src="/src/assets/anonymity.png" alt="">
            <span class="ft-cl-des"> {{it.userName || '匿名'}} 回复 {{it.targetUserName || "匿名用户"}} </span>
          </div>
          <div class="cont">
            <div class="marg-bt-10">{{it.content}}</div>
            <div class="fx-sb">
              <div class="ft-cl-des">{{it.createTime}}</div>
              <div>
                <span class="marg-rt-10 cur-pt" @click="replayHandle(it, 'target')" > <i class="iconfont zhy-a-btn_pinglun_nor2x"></i> 评论{{it.replyTimes}} </span> 
                <span :class="{'cur-pt':true, activeLiked: it.liked}" @click="likedHandle(item)"> <i class="iconfont zhy-a-btn_zan_nor2x"></i> 点赞 {{it.likedTimes}}</span>
              </div>
            </div>
          </div>
          <!-- 插入回复框的位置 -->
          <component :is="openReplyFormId == it.id ? ReplayForm : null" :key="it.id" :name="it.userName || '匿名'" :id = "it.userId" :askInfoId="askInfo.id" @commentHandle="commentHandle"></component>
        <!-- 回复列表 -->
        </div>
         <p class="fx-ct ft-14 ft-cl-des" v-if="replayloading">Loading...</p>
         <p class="fx-ct ft-14 ft-cl-des" v-if="replaynoMore">没有更多了</p>
      </div>
  </el-dialog>
  </div>
</template>
<script setup>
/** 数据导入 **/
import { onMounted, reactive, ref, computed } from "vue";
import { ElMessage } from "element-plus";
import { useRoute } from "vue-router";
import { getQuestionsDetails, postAnswers, getReply, putLiked } from "@/api/classDetails.js";
import RelatedQuestions from './components/RelatedQuestions.vue'
import ReplayForm from './components/ReplayForm.vue'
import { useUserStore } from '@/store'
const store = useUserStore();

const route = useRoute()
const askInfo = ref()

onMounted(() => {
  // 获取问题详情
  getQuestionsDetailsData()
  // 获取回答的列表
  getAllQuestionsData()
})

const dialogTableVisible = ref(false)
// 获取问题详情
const getQuestionsDetailsData = async () => {
  await getQuestionsDetails(route.query.id)
    .then((res) => {
      if (res.code == 200) {
        const { data } = res
        askInfo.value = data
      } else {
        ElMessage({
          message:res.data.msg,
          type: 'error'
        });
      }
    })
    .catch(() => {
      ElMessage({
        message: "课程问题数据请求出错！",
        type: 'error'
      });
    });
} 
// 获取全部回答
const questParams = reactive({
  questionId: route.query.id,
  pageNo: 1,
  pageSize: 10
})
// 数据加载相关变量
const count = ref(0)
const loading = ref(false)
const questionData = ref([])
const noMore = computed(() => questionData.value.length >= count.value)
const clickLoad = () => {
  loading.value = false
  questParams.pageNo++
  getAllQuestionsData('more')
}
// 获取回答列表
const getAllQuestionsData = async (val) => {
  await getReply(questParams)
    .then((res) => {
      if (res.code == 200) {
        if (val == 'more') {
          questionData.value = questionData.value.concat(res.data.list)
        } else if(questParams.pageNo == 1){
          questionData.value = res.data.list
        } else {
          questionData.value = questionData.value.splice(0, (questParams.pageNo - 1) * questParams.pageSize).concat(res.data.list)
        }
        count.value = Number(res.data.total)
        loading.value = false
      } else {
        ElMessage({
          message:res.data.msg,
          type: 'error'
        });
      }
    })
    .catch(() => {
      ElMessage({
        message: "数据请求出错！",
        type: 'error'
      });
    });
} 

// 我要回答 - 回答题主
const description = ref('')
// 是否匿名
const anonymity = ref(false)
// 是否可以提交回答
const isSend = ref(false)
const ruleshandle = () => {
  isSend.value = description.value != '' ?  true : false
}
// 打开当前的回答的全部回复
const isReplay = ref();
const openReply = (item) => {
  // 获取回答的答复的列表
  if (item.id != isReplay.value) {
    getReplyData(item.id, 'one')
    replayHandle(item, 'answer')
  }
}

// 展示回复窗口
const openReplyFormId = ref()
// 存储一级回复数据
const answerInfo = ref({id:''}) 
// 存储二级以后的回复数据
const targetInfo = ref({id:''}) 
const replayHandle = (item, type) => {
  openReplyFormId.value = item.id
  targetInfo.value = item ;
  if (type == 'answer' ) {
    answerInfo.value = item
  };
}

// 回复数据请求参数
const replyParams = reactive({
  pageNo:1,
  pageSize:5,
  answerId: ''
})
const replyData = ref([]);
const replyCont = ref();
const replyLoding = ref(true)
const replaynoMore = computed(() => replyData.value.length >= replyCont.value)
const disabled = computed(() => replyLoding.value || replaynoMore.value)

// 弹窗滚动加载- 回复数据
const load = () => {
  replyLoding.value = false
  replyParams.pageNo++
  getReplyData()
}
// 获取回复数据
const getReplyData = async (id, st) => {
    replyLoding.value = true
    replyParams.answerId = id
    await getReply(replyParams)
    .then((res) => {
      if (res.code == 200) {
       replyLoding.value = false
       replyData.value = st == 'one' ? res.data.list : replyData.value.concat(res.data.list)

       replyCont.value = Number(res.data.total)
       isReplay.value = id
      } else {
        ElMessage({
          message:res.data.msg,
          type: 'error'
        });
      }
    })
    .catch(() => {
      ElMessage({
        message: "回复数据请求出错！",
        type: 'error'
      });
    });
}

// 提交回复数据
const params = reactive({
  questionId: '', // askInfo.value.id, // 当前问题的ID
  targetReplyId:'',
  targetUserId: '',
  answerId:'',
  content:'',
  anonymity:''
})
// 子组件 emit 回调函数 提交评论数据
function commentHandle (val){
  params.content = val.content
  params.anonymity = val.anonymity
  answerHandle() // 评论
}
// 提交回复
const answerHandle = async (type) => {
  params.questionId = askInfo.value.id
  params.targetUserId = targetInfo.value.userId || askInfo.value.userId
  if(params.content == ''){
    params.content = description.value
    params.anonymity = anonymity.value
  }
  params.answerId = answerInfo.value.id
  params.targetReplyId = targetInfo.value.id
  if (params.content == '') {
    ElMessage({
          message:'请输入您的内容！',
          type: 'success'
        });
    return 
  }
 await postAnswers(params)
    .then((res) => {
      if (res.code == 200) {
        ElMessage({
          message:'回复成功！',
          type: 'success'
        });
        
        // 第一层的回答
        if (type == 'first'){
          getAllQuestionsData()
        } else if(dialogTableVisible) {
          getReplyData(isReplay.value, 'one')
        } else {
          getReplyData(isReplay.value, 'one')
        }
        params.content = ''
        description.value = ''
        params.anonymity = ''
        anonymity.value = ''
        isSend.value = false
      } else {
        ElMessage({
          message:res.data.msg,
          type: 'error'
        });
      }
    })
    .catch(() => {
      ElMessage({
        message: "课程问题数据请求出错！",
        type: 'error'
      });
    });
}
// 点赞
const likedHandle = async (item) => {
await putLiked({bizId:item.id, liked:!item.liked, bizType: "QA"})
    .then((res) => {
      if (res.code == 200) {
        item.liked = !item.liked
        item.liked ? item.likedTimes++ : item.likedTimes--
      } else {
        ElMessage({
          message:res.data.msg,
          type: 'error'
        });
      }
    })
    .catch(() => {
      ElMessage({
        message: "点赞请求出错！",
        type: 'error'
      });
    });
}
</script>
<style lang="scss" src="./index.scss"></style>
