<!-- 课程目录模块 -->
<template>
  <div class="classList">
    <el-collapse v-model="activeNames" accordion>
      <el-collapse-item v-for="(item, index) in data"  :name="index">
          <template #title>
            <div class="fx-sb">
              <span>{{item.name}}</span>
              <span class="time">{{(item.mediaDuration/60).toFixed(0)+'.'+item.mediaDuration % 60}}</span>
            </div>
          </template>
          <div  @click="playHandle($props.id, it)" class="item fx-sb" v-for="(it, ind) in item.sections">
            <div><iconVideo v-if="it.type === 2" class="icon" /> <iconJdks v-if="it.type === 3" class="icon" />
              {{it.index}}、{{it.name}}
            </div>
            <div class="time">
              <span v-if="it.trailer" class="trailer-font">试看</span>
              {{(it.mediaDuration/60).toFixed(0)+'.'+it.mediaDuration % 60}}
            </div>
          </div>
        </el-collapse-item>
    </el-collapse>
  </div>
</template>
<script setup>
import { ref } from "vue";
import iconVideo from '@/assets/ico-video.svg'
import iconJdks from '@/assets/ico-jdks.svg'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from "element-plus";

const router = useRouter()
// 引入父级传参
const props = defineProps({
  data:{
    type: Object,
    default:{}
  },
  id:{
    type: String
  },
  isSignUp:{
    type: Boolean,
    default: false
  }
})

const activeNames = ref([0])

const toPlayPage = (courseId,chapterId, sectionId) => {
  router.push({path:"/learning/index", query: {"id" : courseId, "sectionId": sectionId}})
}
const playHandle = (courseId, val) => {
  if(!val.trailer && !props.isSignUp){
    ElMessage('该课程章节不支持试看， 请购买后播放')
    return
  }
  router.push({path: '/learning/index', query: {id: courseId, sectionId: val.id}})
}
</script>
<style lang="scss" scoped>
.trailer-font{
  padding: 3px 12px;
  font-size: 12px;
  background: rgba(242,13,13,.1);
  border-radius: 12px;
  color: #f20d0d;
  font-weight: 700;
  line-height: 20px;
}
.trailer-font:hover{
  color: #fff;
  background: #f20d0d;
}
.classList{
  padding-top: 16px;
  .time{
      font-size: 14px;
      color: #80878C;
      font-weight: 400;
      padding-right: 20px;
      line-height: 50px;
    }
  .item{
    font-size: 16px;
    line-height: 50px;
    padding: 0 20px;
    cursor: pointer;
    .time{
      padding-right: 0px;
    }
    .icon{
      position: relative;
      top: 3px;
      width: 16px;
      height: 16px;
      margin-right: 10px;
    }
    .learn{
      display: inline-block;
      margin-left: 6px;
      width: 32px;
      line-height: 20px;
      text-align: center;
      background: #FFECEC;
      border-radius: 4px;
      font-weight: 400;
      font-size: 12px;
      color: #FF4C4C;
    }
  }
  :deep(.el-collapse-item__header){
    background: #F5F6F9;
    color: var(--color-font1);
    border:none;
    border-radius: 8px;
    margin: 20px 0;
    font-weight: 600;
    font-size: 18px;
    padding: 0 10px 0 25px;
  }
  :deep(.el-collapse){
    border: none;
  }
  :deep(.el-collapse-item__wrap){
    border: none;
  }
  :deep(.el-collapse-item__content){
    padding-bottom: 0;
    font-size: 14px;
  }

}
</style>
