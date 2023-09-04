<!-- 问答模块 -->
<template>
  <div class="questionWrapper marg-bt-20">
    <div class="askCont" >
      <div class="askLists" v-for="item in askListsDataes">
        <div class="userInfo fx ">
          <img :src="item.userIcon" :onerror="onerrorImg(item)" alt="" srcset="">
          {{item.userName  || "匿名"}}
        </div>
        <div class="ask">
          <div class="tit ft-14">{{item.title}}</div>
          <div class="font-bt2" @click="goDetails(item)" v-if="item.latestAnswer && item.latestAnswer.content">最新【{{item.latestAnswer.replier.name}}】的回答</div>
        </div>
        <div class="time fx-sb">
          <div>{{item.createTime}}</div>
          <div class="actBut">
            <span class="marg-rt-10" @click="() => $router.push({path:'/askDetails/index', query:{id:item.id}})">回答 {{item.answerTimes}}</span>
          </div>
        </div>
      </div>
      <div class="noData" v-if="askListsDataes && askListsDataes.length <= 0">
        <Empty :type="true"></Empty>
      </div>
    </div>
    <div class="questCont">
      <el-input class="title" v-model.number="quest.title" maxlength="64" @input="ruleshandle" show-word-limit placeholder="请输入"/>
      <el-input v-model="quest.description" rows="4" resize="none" type="textarea" @input="ruleshandle" maxlength="500" show-word-limit placeholder="请输入" />
      <div class="fx-sb fx-al-ct" style="margin-top: 4px;">
        <div><el-checkbox v-model="quest.anonymity" label="匿名提问" size="large" /></div>
        <div class="subCont">
          <span class="bt ft-14" :class="{'bt-dis':!isSend}" @click="submitForm()">提问</span>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import defaultImage from '@/assets/icon.jpeg'
import { ref, onMounted, reactive } from "vue"
import { postQuestions, getAskList, putLiked } from "@/api/classDetails.js"
import { useUserStore, dataCacheStore, isLogin } from '@/store'
import { useRoute, useRouter } from "vue-router";
import {ElMessage} from "element-plus"
import Empty from '@/components/Empty.vue'

const route = useRoute()
const router = useRouter()
const store = useUserStore();
const dataCache = dataCacheStore();

const currentPlayData = dataCacheStore().getCurrentPlayData

// 默认头像
const onerrorImg = (tag) => {
  tag.userIcon = defaultImage;
}

// 用户信息
const userInfo = ref();
onMounted(() => {
  if(isLogin()){
    // 获取登录信息中的我的信息
    userInfo.value = store.getUserInfo
    // 获取问答列表
    getAskListsDataes()
  }
})

// 问答列表参数
const params = ref({
  isAsc:true,
  pageNo: 1,
  pageSize: 1000,
  sectionId: currentPlayData.sectionId,
  sortBy: '',
  onlyMine: false
});
//
const isSend = ref(false)
// 提问数据
const quest = reactive({
  sectionId: currentPlayData.sectionId, // 小节Id
  courseId: currentPlayData.courseId, // 课程id
  chapterId: currentPlayData.chapterId,  // 章Id
  title: '', 
  anonymity: false, // 是否匿名
  description: '',
})
// 列表数据
const askListsDataes =  ref([])
const total = ref(0)
// 切换全部问答及我的问答
const askCheck = type => {
  params.value.pageNo = 1
  params.value.pageSize = 10
  params.value.onlyMine = type
  getAskListsDataes()
}
const checkCahpter = (id) => {
  params.value.pageNo = 1
  params.value.pageSize = 10
  params.value.sectionId = id
  params.value.onlyMine = false
  getAskListsDataes()
}
// 获取问答列表
const getAskListsDataes = async () => {
  await getAskList(params.value)
    .then((res) => {
      if (res.code == 200) {
        askListsDataes.value = res.data.list
        total.value = Number(res.data.total)
      } else {
        console.log(res.msg)
      }
    })
    .catch(() => {
      ElMessage({
        message: "问答列表数据请求出错！",
        type: 'error'
      });
    });
}
// 点赞或者取消
const putLikedHandle = async (item) => {
const liked = item.liked == undefined ? false : item.liked
await putLiked({bizType: "QA", bizId:item.id, liked:!item.liked})
    .then((res) => {
      if (res.code == 200) {
        item.liked = !item.liked
        item.liked ? item.likedTimes++ : item.likedTimes--
      } else {
        console.log(res.msg)
      }
    })
    .catch(() => {
      ElMessage({
        message: "点赞请求出错！",
        type: 'error'
      });
    });
}

// 去往详情页面
const goDetails = (item) => {
  dataCache.setAskDetails(item)
  router.push({path: '/askDetails', query:{id:item.id}})
}
// 小节数据
const chapterData = ref([])

// 设置每页多少条
const handleSizeChange = (val) => {
  params.value.pageSize = val
  getAskListsDataes()
}
// 去往某一页
const handleCurrentChange = (val) => {
  params.value.pageNo = val
  getAskListsDataes()
}
// 提交问题
const submitForm = async () => {
if (!isSend.value){
  ElMessage({
    message:'请输入标题和描述',
    type:'error'
  })
  return 
}  
await postQuestions(quest)
    .then((res) => {
      if (res.code == 200) {
        quest.title = ''
        quest.description = ''
        getAskListsDataes()
      } else {
        ElMessage({
          message:res.data.msg,
          type: 'error'
        });
      }
    })
    .catch(() => {
      ElMessage({
        message: "提问请求出错！",
        type: 'error'
      });
    });
}
// 输入文字的input
const ruleshandle = (val) => {
  if (quest.title != '' &&  quest.description != ''){
    isSend.value = true
  } else {
    isSend.value = false
  }
}
</script>
<style lang="scss" scoped>
.questionWrapper{
  height: calc(100vh - 415px);
  margin-top: 15px;
  .askCont{
    .askLists{
      line-height: 40px;
      font-size: 14px;
      .userInfo{
        line-height: 20px;
        font-size: 12px;
        color: var(--color-font3);
        img {
          width: 20px;
          height: 20px;
          border-radius: 26px;
          margin-right: 10px;
        }
      }
      .ask{
        color: #A0A9B2;
        .tit{
          line-height: 24px;
          margin-top: 6px;
        }
      }
      .time{
        color: var(--color-font3);
        padding-bottom:10px;
        margin-bottom: 19px;
        border-bottom: 1px solid #000000;
        line-height: 20px;
        .actBut{
          color: #A0A9B2;
          cursor: pointer;
          i{
            display: inline-block;
            position: relative;
            width: 20px;
            height: 20px;
          }
          .iconfont{
            position: relative;
            color: #A0A9B2;
            font-size: 20px;
            top: 2px;
          }
          .zhy-a-btn_zan_sel2x{
            color: var(--color-main);
            font-size: 18px;
            top: 0;
          }
          .btnIcon{
            color: #A0A9B2;
            width: 21px;
            height: 21px;
            position: relative;
            top: 5px;
          }
        }
      }
    }
    .noData{
      height: calc(100vh - 488px);
    }
  }
  .questCont{
    position: absolute;
    width: 100%;
    background-color: #292F37;
    bottom: 0;
    left: 0;
    padding: 15px;
    
    .title{
      margin-bottom: 10px;
    }
    .subCont{
      .bt{
        width: 80px;
        height: 28px;
        line-height: 28px;
      }
    }
    input::-webkit-input-placeholder{
      color:#000000;
    }
    :deep(.el-input__inner){
      color: #fff;
    }
    :deep(.el-textarea__inner){
      color: #fff;
    }
    :deep(.el-input__count){
      color: #7A838A;
    }
  }
  
}
</style>
<style>
  input::-webkit-input-placeholder{
    color:#fff;
  }
</style>
