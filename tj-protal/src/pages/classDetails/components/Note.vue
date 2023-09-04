<!-- 笔记模块 -->
<template>
  <div class="note bg-wt marg-bt-20">
    <div class="tabLab fx-sb">
      <div class="lable">
        <span @click="askCheck('all')" :class="{act:askType == 'all'}" class="marg-rt-20">全部笔记</span> 
        <span @click="askCheck('my')" :class="{act:askType == 'my'}">我的笔记</span>
      </div> 
    </div>
    <AskChapterItems :data="chapterData" @checkCahpter="checkCahpter"></AskChapterItems>
    <div class="askCont">
      <div class="askLists" v-for="item in askListsDataes">
        <div class="userInfo fx">
          <img :src="item.authorIcon" alt="" srcset="">
          {{item.authorName}}
        </div>
        <div class="ask">
          <div class="ft-16">{{item.content}}</div>
        </div>
        <div class="time fx-sb">
          <div>{{item.createTime}}</div>
          <div class="actBut">
            <!-- <span class="marg-rt-20" v-if="userInfo.id == item.author.id"><i class="iconfont zhy-a-icon_kaoshi2x"></i> 编辑</span> -->
            <span @click="delNoteHandle(item.id)" class="marg-rt-20" v-if="userInfo.id == item.authorId"><i class="iconfont zhy-a-btn_delete_nor2x" ></i> 删除 </span>
            <span @click="gathersHandle(item)" class="marg-rt-20" :class="{activeLiked:false}" v-if="userInfo.id != item.authorId "><i class="iconfont zhy-a-btn_caiji_nor2x" styel="font-size: 22px;" ></i> {{item.isGathered ? '已采集' : '采集'}}</span>
            <span @click="likedHandle(item)" :class="{activeLiked:item.liked}" ><i class="iconfont zhy-a-btn_zan_nor2x"></i> 点赞 {{item.likedTimes || 0}}</span>
          </div>
        </div>
      </div>
      <!-- 分页操作 -->
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
import { ElMessage } from "element-plus";
import { getClassChapter } from "@/api/classDetails.js"
import { getAllNotes, delNote, notesGathers, unNotesGathers } from "@/api/notes.js"
import AskChapterItems from "../../../components/AskChapterItems.vue";
import { useUserStore, isLogin } from '@/store'
import { useRoute } from "vue-router";

const route = useRoute()
// 引入父级传参
const props = defineProps({
  id:{
    type: String
  }
})
const store = useUserStore();
// 用户信息
const userInfo = ref();
onMounted(() => {
  // 获取登录信息中的我的信息
  userInfo.value = store.getUserInfo
  if(isLogin()){
    // 获取小节数据
    getClassChapterData(route.query.id)
    // 获取问答列表
    getAskListsDataes()
  }
})
// 问答列表参数
const params = ref({
  admin:false,
  isAsc:true,
  pageNo: 1,
  pageSize: 10,
  sectionId: '',
  courseId: route.query.id,
  sortBy: '',
  onlyMine: false
});
// 列表数据
const askListsDataes =  ref([])
const total = ref(0)
// 切换全部问答及我的问答
const askType = ref('all')
const askCheck = type => {
  params.value.pageNo = 1
  params.value.pageSize = 10
  params.value.onlyMine = type === 'my'
  askType.value = type
  getAskListsDataes()
}
const checkCahpter = (id) => {
  params.value.pageNo = 1
  params.value.pageSize = 100
  params.value.sectionId = id
  getAskListsDataes()
}
// 获取笔记列表
const getAskListsDataes = async () => {
  params.value.sectionId == 'all' ?  params.value.sectionId = undefined : null
  await getAllNotes(params.value)
    .then((res) => {
      if (res.code == 200) {
        askListsDataes.value = res.data.list
        total.value =  Number(res.data.total)
      } else {
        ElMessage({
          message:res.data.msg,
          type: 'error'
        });
      }
    })
    .catch(() => {
      ElMessage({
        message: "笔记列表数据请求出错！",
        type: 'error'
      });
    });
}
// 删除笔记
const delNoteHandle = async id => {
await delNote(id)
    .then((res) => {
      if (res.code == 200) {
        // 删除成功
        ElMessage({
          message:'笔记删除成功！',
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
        message: "课程章节数据请求出错！",
        type: 'error'
      });
    });
}
const gathersHandle = async item => {
  item.isGathered ? unNotesGathersData(item) : notesGathersData(item) 
}
// 采集笔记
const notesGathersData = async item => {
await notesGathers(item.id)
    .then((res) => {
      if (res.code == 200) {
        // 采集笔记成功
        getAskListsDataes()
        item.isGathered = !item.isGathered
      } else {
        ElMessage({
          message:res.data.msg,
          type: 'error'
        });
      }
    })
    .catch(() => {
      ElMessage({
        message: "采集笔记请求出错！",
        type: 'error'
      });
    });
} 
// 取消采集笔记
const unNotesGathersData = async item => {
await unNotesGathers(item.id)
    .then((res) => {
      if (res.code == 200) {
        // 取消采集笔记成功！
        // getAskListsDataes()
        item.isGathered = !item.isGathered
      } else {
        ElMessage({
          message:res.data.msg,
          type: 'error'
        });
      }
    })
    .catch(() => {
      ElMessage({
        message: "采集笔记请求出错！",
        type: 'error'
      });
    });
} 
// 小节数据
const chapterData = ref([])

// 获取小节数据
const getClassChapterData = async (id) => {
  await getClassChapter(id)
    .then((res) => {
      if (res.code == 200) {
        chapterData.value = [{id:'all', index: '全部'},...res.data]
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
// 点赞
const likedHandle = (item) => {
  item.liked = !item.liked
  // item.liked ? item.answerAmount-- : item.answerAmount++
}
</script>
<style lang="scss" scoped>
.note{
  padding-top: 30px;
  .tabLab{
    margin-bottom: 20px;
    .lable{
      display: flex;
      align-items: center;
      span{
        display: inline-block;
        width: 96px;
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
  .activeLiked{
        color: var(--color-main)
      }
  .pagination{
    padding-top: 40px;
    text-align: center;
  }
}
</style>
