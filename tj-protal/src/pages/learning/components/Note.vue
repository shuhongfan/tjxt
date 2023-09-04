<!-- 学习笔记 -->
<template>
  <div class="learnNoteWrapper">
    <div class="tabCheck">
      <span class="fx-1 cur-pt" @click="activeHandle(1)" :class="{act: actIndex == 1}">我的笔记</span>
      <span class="line"></span>
      <span class="fx-1 cur-pt" @click="activeHandle(2)" :class="{act: actIndex == 2}">全部笔记</span>
    </div>
    <div class="noteCont" v-if="noteListsDataes.length > 0">
      <div class="noteLists" v-for="item in noteListsDataes">
      <div class="userInfo fx-sb">
        <div class="fx ft-cl-wt">
          <img :src="item.authorIcon" alt="" srcset="">
          {{item.authorName}}
        </div>
        <div>
          <i class="iconfont zhy-a-icon-sp22x"></i>
          {{item.noteMoment == 0 ? '00:00' :Math.trunc(item.noteMoment/60)+':'+ item.noteMoment % 60}}
        </div>
      </div>
      <div class="note">
        <div class="tit ft-14">{{item.content}}</div>
        <div class="font-bt2" @click="goDetails(item)" v-if="item.latestAnswer && item.latestAnswer.content">最新【{{item.latestAnswer.replier.name}}】的回答</div>
      </div>
      <div class="time fx-sb">
        <div class="tm">{{item.createTime}}</div>
        <div class="actBut">
          <span class="marg-rt-10" @click="editNoteHandle(item)" v-if="userInfo.id == item.authorId ">
            <i class="iconfont zhy-a-icon-xiugai22x"></i> 编辑
          </span>
          <span @click="gathersHandle(item)" v-if="userInfo.id != item.authorId " :class="{activeLiked:false && item.isGathered}">
            <i class="iconfont zhy-a-ico-caiji2x"></i> {{item.isGathered ? '已采集' : '采集'}}
          </span>
          <span class="" @click="delNoteHandle(item)" v-if="userInfo.id == item.authorId ">
            <i class="iconfont zhy-a-icon-delete22x" style="font-size: 23px;top: 3px;"></i> 删除
          </span>
          <!-- <span class="" @click="putLikedHandle(item)" >
            <i v-show="!item.liked" class="iconfont zhy-a-icon-zan2x"></i> 
            <i v-show="item.liked" class="iconfont zhy-a-btn_zan_sel2x"></i>
          </span> -->
        </div>
      </div>
      </div>
    </div>
    <div class="noData" v-else>
      <Empty :type="true"></Empty>
    </div>
    <div class="questCont">
      <el-input v-model="noteParams.content" rows="4" resize="none" type="textarea" @input="ruleshandle" maxlength="500" show-word-limit placeholder="请输入" />
      <div class="fx-sb fx-al-ct" style="margin-top: 12px;">
        <div><el-checkbox v-model="noteParams.isPrivate" label="私密" size="large" /></div>
        <div class="subCont">
          <span class="bt ft-14" :class="{'bt-dis':!isSend}" @click="submitForm()">保存</span>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { onMounted, ref, reactive, watchEffect } from 'vue'
import { getAllNotes, addNotes, likeed, delNote, updateNotes, notesGathers, unNotesGathers } from "@/api/notes.js"
import {ElMessage} from 'element-plus'
import { useUserStore, dataCacheStore } from '@/store'
import Empty from '@/components/Empty.vue'

const currentPlayData = dataCacheStore().getCurrentPlayData

const store = useUserStore();

const userInfo = ref(null)
// 引入父级传参
const props = defineProps({
  id:{
    type: String,
    default: ''
  },
  currentTime:{
    default: 0
  }
})

// 笔记数据
const noteParams = reactive({
  isPrivate:false, // 是否是隐私笔记，默认false  新增的全部都是正常的
  sectionId: currentPlayData.sectionId, // 小节Id
  courseId: currentPlayData.courseId, // 课程id
  chapterId: currentPlayData.chapterId,  // 章Id
  content: '',
  noteMoment: currentPlayData.currentTime
})

onMounted(() => {
  userInfo.value = store.getUserInfo
  // 获取笔记信息
  getAskListsDataes() 
})
const isSend = ref(false)
// emit数据载入
const emit = defineEmits(['sortHandle'])

const actIndex = ref(1);

// 点击选中
const activeHandle = (value) => {
  actIndex.value = value;
  params.value.onlyMine = value === 1;
  getAskListsDataes()
}

// 笔记数据
const noteListsDataes = ref({})
const total = ref(0)
// 问答列表参数
const params = ref({
  admin:false,
  isAsc:false,
  pageNo: 1,
  pageSize: 1000,
  sectionId: currentPlayData.sectionId,
  courseId: currentPlayData.courseId,
  sortBy: '',
  onlyMine: true
});
// 获取笔记列表
const getAskListsDataes = async () => {
  await getAllNotes(params.value)
    .then((res) => {
      if (res.code == 200) {
        if(res.data.list.length > 0){
          noteListsDataes.value = res.data.list
        } else {
          noteListsDataes.value = []
        }
        total.value =  Number(res.data.total)
      } else {
        ElMessage({
          message:res.data.msg,
          type: 'error'
        });
      }
    })
}
// 是否可以保存
const ruleshandle = (item) =>{
  if (noteParams.content != ''){
    isSend.value = true
  } else {
    isSend.value = false
  }
}
// 采集
const gathersHandle = async item => {
  item.isGathered ? unNotesGathersData(item) : notesGathersData(item) 
}
// 采集笔记
const notesGathersData = async item => {
await notesGathers(item.id)
    .then((res) => {
      if (res.code == 200) {
        ElMessage({
          message:'笔记采集成功！',
          type: 'success'
        });
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
} 
// 取消采集笔记
const unNotesGathersData = async item => {
  ElMessage({ message:'该笔记已采集过了'});
  return
await unNotesGathers(item.id)
    .then((res) => {
      if (res.code == 200) {
        // 取消采集笔记成功！
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
// 删除
const delNoteHandle = async (item) => {
await delNote(item.id)
    .then((res) => {
      if (res.code == 200) {
        ElMessage({
          message: '笔记删除成功',
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
        message: "笔记请求出错！",
        type: 'error'
      });
    });
}
const subType = ref('add')
const editParams = ref({})
// 点赞或者取消 
const putLikedHandle = async (item) => {
await likeed(item.id, !item.liked)
    .then((res) => {
      if (res.code == 200) {
        item.liked = !item.liked
      } else {
        ElMessage({
          message:res.data.msg,
          type: 'error'
        });
      }
    })
    .catch(() => {
      ElMessage({
        message: "笔记请求出错！",
        type: 'error'
      });
    });
}
watchEffect(() => {
  noteParams.noteMoment = props.currentTime
})

// 新增/编辑-笔记
const submitForm = async () => {
  const query = subType.value == 'add' ? addNotes : updateNotes
  let params = noteParams
  if(subType.value == 'edit'){
    editParams.value.content = noteParams.content
    params = editParams.value
  } else {
    params.noteMoment = props.currentTime
  }
  await query(params)
    .then((res) => {
      if (res.code == 200) {
        subType.value = 'add'
        noteParams.content = ''
        isSend.value = false
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
        message: "笔记请求出错！",
        type: 'error'
      });
    });
}
// 编辑笔记
const editNoteHandle = async (item) => {
  noteParams.content = item.content
  subType.value = 'edit'
  editParams.value = {
    id: item.id,
    content: noteParams.content,
    isPrivate: false
  }
}

</script>
<style lang="scss" scoped>
.learnNoteWrapper{
  .tabCheck{
    margin: 10px auto 20px auto;
    display:flex;
    justify-content: center;
    width: 204px;
    height: 32px;
    line-height: 32px;
    border: 1px solid #A0A9B2;
    color: #A0A9B2;
    border-radius: 16px;
    font-size: 14px;
    text-align: center;
    .line{
      position: relative;
      top: 10px;
      height: 12px;
      width: 1px;
      background-color: #A0A9B2;
    }
  }
  .act{
    color: #FFF;
  }
    .noteCont{
      height: calc(100vh - 378px);
      overflow-y: scroll;
    .noteLists{
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
        i{
          position: relative;
          top: 2px;
          font-size: 18px;
        }
      }
      .note{
        color: #A0A9B2;
        margin-top: 0px;
        .tit{
          line-height: 24px;
          margin-top: 0px;
        }
      }
      .time{
        color: var(--color-font3);
        padding-bottom:16px;
        margin-bottom: 19px;
        border-bottom: 1px solid #000000;
        line-height: 20px;
        .tm{
          color:var(--color-ft2)
        }
        .actBut{
          color: #A0A9B2;
          cursor: pointer;
          .iconfont{
            position: relative;
            color: #A0A9B2;
            font-size: 20px;
            top: 2px;
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
  }
  .noData{
      height: calc(100vh - 488px);
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
    :deep(.el-textarea__inner){
      color: #fff;
    }
  }
  .activeLiked, .activeLiked .iconfont{
        color: var(--color-main) !important;
      }
}
</style>
