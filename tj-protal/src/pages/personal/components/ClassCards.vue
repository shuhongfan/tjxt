<!--  我的课程 - 卡片  -->
<template>
  <div class="classCards fx-sb fx-ct">
    <div class="marg-rt-20">
      <img :src="data.courseCoverUrl" alt="" @click="$router.push({path: '/details/index', query: {id: data.courseId}})">
    </div>
    <div class="info fx-1">
      <div class="tit ">{{data.courseName}}</div>
      <div>
        <span>有效日期：</span>
        {{data.expireTime == null ? '永久有效' :
          new Date(data.createTime).toLocaleDateString() + ' - ' + new Date(data.expireTime).toLocaleDateString()}}
      </div>
      <div><span>已学习：</span><em>{{data.learnedSections}}</em> / {{data.sections}}</div>
      <div v-if="type == '1'"><span>正在学习：</span>第{{data.latestSectionIndex}}节 {{data.latestSectionName}}</div>
    </div>
    <div class="btnCont">
      <div class="btn" v-if="type == '1'" @click="() => $router.push({path: '/learning/index', query: {id: data.courseId}})">
        <span class="bt bt-round">继续学习</span>
      </div>
      <div class="btn" v-if="type == '2' && data.status != 3" @click="() => $router.push({path: '/learning/index', query: {id: data.courseId}})">
        <span class="bt bt-round" v-if="data.status == 0">马上学习</span>
        <span class="bt bt-round" v-if="data.status == 1">继续学习</span>
        <span class="bt bt-round" v-if="data.status == 2">重新学习</span>
      </div>
      <div class="btn" v-if="type == '2' && data.status != 3 && data.planStatus == 0" @click="planActive(data, 'add')">
        <span class="bt-grey bt-round">创建计划</span>
      </div>
      <div class="btn" v-if="type == '2' && data.status != 3 && data.planStatus == 1" @click="planActive(data, 'edit')">
        <span class="bt-grey bt-round">修改计划</span>
      </div>
      <div class="btn" v-if="type == '2' && data.status == 3" @click="planActive(data, 'del')">
        <span class="bt-grey bt-round">删除课程</span>
      </div>
    </div> 
  </div>
</template>
<script setup>

// 接收父组件传来的标题
defineProps({
  data:{
    type: Object,
    default: {}
  },
  type:{
    type: String,
    default: '1'
  }
})  
const emit = defineEmits(['planHandle'])

const planActive = (it, type) => {
  emit('planHandle', {data: it, type})
}

</script>
<style lang="scss" scoped>
.classCards{
  img{
    width: 236px;
    height: 132px;
    border-radius: 8px;
  }
  .info{
    line-height: 30px;
    font-size: 14px;
    em{
      font-style: normal;
      color: var(--color-main);
    }
    span{
      display: inline-block;
      color: var(--color-font3);
      min-width: 85px;
    }
    .tit{
      font-size: 20px;
      font-weight: 500;
      line-height: 40px;
    }
  }
  
  .btnCont{
    display: flex;
    flex-direction: column;
    flex-wrap: wrap;
     .btn{
        width: 114px;
        height: 40px;
        display: flex;
        align-items: center;
        margin: 10px 0;
      }
  }
  
}
</style>
