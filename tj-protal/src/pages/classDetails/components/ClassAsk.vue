<!-- 问答模块 -->
<template>
  <div class="classAsk bg-wt marg-bt-20">
    <div class="tabLab fx-sb">
      <div class="lable">
        <span @click="askCheck(false)" :class="{act:!params.onlyMine,'bt-grey2': params.onlyMine}" class="marg-rt-20 ">全部问答</span>
        <span @click="askCheck(true)" :class="{act:params.onlyMine,'bt-grey2': !params.onlyMine}">我的问答</span>
      </div> 
      <div class="ask"><span @click="() => $router.push({path: '/ask', query: {id: $props.id, title: $props.title}})" class="bt bt-round ft-14">提问</span></div>
    </div>
    <AskChapterItems :data="chapterData" @checkCahpter="checkCahpter"></AskChapterItems>
    <div class="askCont">
      <div class="askLists" v-for="item in askListsDataes">
        <div class="userInfo fx">
          <img v-if="item.userIcon" :src="item.userIcon" alt="" srcset="">
          <img v-else src="/src/assets/anonymity.png" alt="" srcset="">
          {{item.userName || "匿名用户"}}
        </div>
        <div class="ask">
          <div class="ft-16">{{item.title}}</div>
          <div class="font-bt2" @click="goDetails(item)" v-if="item.latestReplyContent">
            最新【{{item.latestReplyUser}}】的回答
          </div>
        </div>
        <div class="time fx-sb">
          <div>{{item.createTime}}</div>
          <div class="actBut">
            <span class="font-bt2 marg-rt-20" @click="() => $router.push({path:'/ask', query:{id:$props.id,queryId:item.id,type:'edit',title:item.title}})" v-if="userInfo.id == item.userId">
              <i class="iconfont zhy-a-icon_kaoshi2x"></i> 编辑</span>
            <span class="font-bt2 marg-rt-20" @click="delQuestionsHandle(item.id)" v-if="userInfo.id == item.userId">
              <i class="iconfont zhy-a-btn_delete_nor2x"></i> 删除 </span>
            <span class="font-bt2" @click="goDetails(item)"><i class="iconfont zhy-a-btn_pinglun_nor2x"></i> 回答 {{item.answerTimes}}</span>
          </div>
        </div>
      </div>
      <div class="pagination fx-ct" v-if="total > 10">
        <el-pagination
          v-model:currentPage="params.pageNo"
          v-model:page-size="params.pageSize"
          background
          :page-sizes="[10, 20, 50, 100]"
          layout="total, prev, pager, next, sizes, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted } from "vue"
import { getClassChapter, getAskList, delQuestions } from "@/api/classDetails.js"
import AskChapterItems from "../../../components/AskChapterItems.vue";
import { useUserStore, dataCacheStore, isLogin } from '@/store'
import { useRoute, useRouter } from "vue-router";
import {ElMessage} from "element-plus"
const route = useRoute()
const router = useRouter()
const store = useUserStore();
const dataCache = dataCacheStore();

// 引入父级传参
const props = defineProps({
  id:{
    type: String,
    default: ''
  }, 
  title:{
    type: String,
    default: ''
  }
})

// 用户信息
const userInfo = ref();
onMounted(() => {
  if(isLogin()){
    // 获取登录信息中的我的信息
    userInfo.value = store.getUserInfo
    // 获取小节数据
    getClassChapterData(route.query.id)
    // 获取问答列表
    getAskListsDataes()
  }
})
// 问答列表参数
const params = ref({
  courseId: route.query.id,
  isAsc:true,
  pageNo: 1,
  pageSize: 10,
  sectionId: '',
  sortBy: '',
  onlyMine: false
});
// 列表数据
const askListsDataes =  ref([])
const total = ref(0)
// 切换全部问答及我的问答
const askCheck = onlyMine => {
  params.value.pageNo = 1
  params.value.pageSize = 10
  params.value.onlyMine=onlyMine
  getAskListsDataes()
}
const checkCahpter = (id) => {
  params.value.pageNo = 1
  params.value.pageSize = 10
  params.value.sectionId = id
  getAskListsDataes()
}
// 获取问答列表
const getAskListsDataes = async () => {
  await getAskList(params.value)
    .then((res) => {
      if (res.code === 200) {
        askListsDataes.value = res.data.list
        total.value = Number(res.data.total)
      } else {
        ElMessage({
          message:res.data.msg,
          type: 'error'
        });
      }
    })
    .catch(() => {
      ElMessage({
        message: "问答列表数据请求出错！",
        type: 'error'
      });
    });
}
// 删除问题
const delQuestionsHandle = async id => {
await delQuestions(id)
    .then((res) => {
      if (res.code == 200) {
        // 删除成功
        ElMessage({
          message:'问题删除成功！',
          type: 'success'
        });
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
        message: "问题删除请求出错！",
        type: 'error'
      });
    });
}
// 去往详情页面
const goDetails = (item) => {
  dataCache.setAskDetails(item)
  router.push({path: '/askDetails', query:{id:item.id, detailsId: props.id, name: props.title}})
}
// 小节数据
const chapterData = ref([])

// 获取小节数据
const getClassChapterData = async (id) => {
  await getClassChapter(id)
    .then((res) => {
      if (res.code == 200) {
        chapterData.value = [{id:'', index: '全部'},...res.data]
      } else {
        ElMessage({
          message:res.data.msg,
          type: 'error'
        });
      }
    })
    .catch(() => {
      ElMessage({
        message: "课程章节数据请求出错！",
        type: 'error'
      });
    });
}

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

</script>
<style lang="scss" scoped>
.classAsk{
  padding-top: 30px;
  .tabLab{
    margin-bottom: 20px;
    .lable{
      display: flex;
      align-items: center;
       span{
        display: inline-block;
        padding: 5px 20px;
        border-radius: 20px;
        font-size: 16px;
        text-align: center;
        cursor: pointer;
      }
    }
    .ask{
      width: 103px;
      height: 32px;
      align-items: center;
      span{
        line-height: 30px;
      }
    }
    .act{
      background: #19232B;
      border-radius: 20px;
      color: #fff;
      font-size: 14px;
      font-weight: 600;
      padding: 5px 20px;
    }
  }
  .askCont{
    padding-top: 20px;
    .askLists{
      line-height: 40px;
      font-size: 14px;
      .userInfo{
        line-height: 26px;
        color: var(--color-font3);
        img {
          width: 26px;
          height: 26px;
          border-radius: 26px;
          margin-right: 10px;
        }
      }
      .ask{
        padding-left: 36px;
      }
      .time{
        color: var(--color-font3);
        width: calc(100% - 36px);
        margin-left: 36px;
        padding-bottom:20px;
        border-bottom: 1px solid #EEEEEE;
        margin-bottom: 20px;
        .actBut{
          color: #19232B;
          cursor: pointer;
          .iconfont{
            position: relative;
            font-size: 20px;
            top: 2px;
          }
          .btnIcon{
            width: 21px;
            height: 21px;
            position: relative;
            top: 5px;
          }
        }
      }
    }
  }
  .pagination{
    padding-top: 40px;
    text-align: center;
  }
}
</style>
