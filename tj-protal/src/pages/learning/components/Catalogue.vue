<!-- 学习目录 -->
<template>
  <div class="catalogueWrapper">
    <el-collapse accordion v-model="actIndex">
      <el-collapse-item :name="item.id" v-for="item in data" :key="item.id">
        <template #title>
          <div class="title"><span class="ft-wt-600">{{item.name}}</span></div>
        </template>
        <div class="subTitle fx-sb" :class="isPlay(item.id, it)"  v-for="it in item.sections" :key="it.id" >
          <i  @click="play(it, it.type == 2 ? 1 :it.type)" :class="startIcon(it)"></i>
          <div class="subTit fx-1">
            <span @click="play(it, it.type == 2 ? 1 : it.type == 2 ? 1 :it.type)" class="marg-rt-10">{{it.name}}</span>
            <span v-if="it.hasTest" @click="play(it, it.type == 2 ? 2 :it.type)" class="chapter">练习</span>
            <span v-if="it.trailer" class="trailer-font">试看</span>
          </div>
          <div> 
            <span @click="play(it, it.type == 2 ? 1 :it.type)" v-if="it.mediaDuration != 0">{{(it.mediaDuration/60).toFixed(0)}}:{{item.mediaDuration%60}}</span>
          </div>
        </div>
      </el-collapse-item>
    </el-collapse>
  </div>
</template>
<script setup>
import { onMounted, ref, watchEffect, inject } from 'vue'
import { dataCacheStore } from "@/store"
import { ElMessage, ElMessageBox } from "element-plus";
const store = dataCacheStore()
// 引入父级传参
const props = defineProps({
  data:{
    type: Array,
    default:[]
  },
  statusList:{
    type: Array,
    default:[]
  },
  playId:{
    type: String,
    default: ''
  },
  finished: {
    type: Boolean,
    default: false
  }
})


const currentPlayData = inject('currentPlayData')
// 默认打开项
const playId = ref(props.playId)
const finished = ref(props.finished)
const actIndex = ref("")
onMounted(async () => {
  for (let c in props.data) {
    c.sections.forEach(s => {
      if(s.id == props.playId){actIndex.value = c.id}
    })
  }
})
watchEffect(() => {
  playId.value = currentPlayData.sectionId || props.playId
  if(props.finished){
    next();
  }
})
// 展开章、节
const isPlay = (chapterId, section) => {
  let b = playId.value === section.id;
  if(b){
    openItem(chapterId);
  }
  return {playAct: b}
}

// 根据播放状态调整icon
const startIcon = (item) => {
  let data = 'iconfont zhy-a-ico-sp-sei2x'

  if(item.type == '2'){
    if(item.finished != undefined && item.finished == false){  // 未播放完成
      data = 'iconfont zhy-a-ico-502x1'
    } else if(item.finished && item.finished == true){ // 播放完成
      data = 'iconfont zhy-a-ico-wc2x'
    } {
      data = 'iconfont zhy-a-ico-sp-sei2x' // 未播放过
    }
  } else if(item.type == '3'){
    data = 'iconfont zhy-a-ico-jdks2x'
  }
  return data
}

// emit数据载入
const emit = defineEmits(['sortHandle', 'playHadle', 'openCatalogue'])
const openItem = val => {
  emit('openCatalogue', val)
}
// 排序选中参数定义
const activeKey = ref('all')

// 点击小节 type == 1 是点击视频 2 点击练习
const play = (item, tp) => {
  playId.value = item.id
  emit('playHadle', {item, tp})
}
const next = () => {
  let findNext = false;
  let item;
  let cs = props.data;
  a:for (let i = 0; i < cs.length; i++) {
    let ss = cs[i].sections;
    for (let j = 0; j < ss.length; i++) {
        if(findNext){
          item = ss[i];
          break a;
        }
        if(ss[i].id === playId.value){
          findNext = true
        }
    }
  }
  if(!item || item.type === 3){
    emit('playHadle', {item, tp: 0})
    return
  }
  playId.value = item.id
  emit('playHadle', {item, tp: 1})
}
</script>
<style lang="scss" scoped>
.catalogueWrapper {
  padding: 0 10px;

  ::deep(.el-collapse-item__header) {
    background: transparent;
  }

  .title {
    font-size: 16px;
    height: 40px;
    line-height: 40px;
    width: 280px;
    overflow: hidden;

    span {
      display: inline-block;
      height: 40px;
    }
  }

  .subTitle {
    position: relative;

    ::before {
      position: relative;
      z-index: 2;
    }

    ::after {
      content: '';
      position: absolute;
      left: 7px;
      top: 21px;
      border-left: 1px dashed #667280;
      height: calc(100% - 2px);
    }

    line-height: 20px;

    i {
      position: relative;
      top: 1px;
      margin-right: 4px;
    }

    cursor: pointer;
    margin: 5px 0 20px 0;
    color: #A0A9B2;

    .subTit {
      width: 230px;
      line-height: 20px;

      .chapter {
        display: inline-block;
        width: 32px;
        text-align: center;
        line-height: 15.5px;
        font-weight: 400;
        font-size: 12px;
        border-radius: 2px;
        background: #A0A9B2;
        color: #1B2127;
      }
    }

    &:hover {
      color: #fff;

      .chapter {
        background: #ffffff !important;
        color: #1B2127 !important;
      }
    }

  }

  :deep(.el-collapse-item__content) {
    padding-bottom: 5px;
  }

  .subTitle:last-child {
    margin-bottom: 0;

    ::after {
      display: none;
    }
  }

  .playAct {
    ::after {
      border-color: var(--color-main);
    }

    color: var(--color-main);

    .chapter {
      background: var(--color-main) !important;
      color: #FFFFFF !important;
    }
  }
  .trailer-font{
    padding: 3px 12px;
    font-size: 12px;
    //background: rgba(242,13,13,.1);
    border-radius: 12px;
    color: #f20d0d;
    font-weight: 700;
    line-height: 20px;
  }
}
</style>
