<!-- 课程展示卡片 - 首页、搜索页 -->
<template>
  <div class="classCards" @click="goDetails(data.id)">
    <div class="image">
       <!-- 由于暂时不做直播 所以录播先去掉 -->
        <!-- <span class="label">录播</span> -->
        <img :src="data.coverUrl" alt="" />
    </div>
    <div class="pd-10">
        <div class="title marg-bt-10 ft-14" v-html="data.name"></div>
        <div class="ft-cl-des" v-if="type == 'default' || type == 'search'"><span>讲师：</span> {{data.teacher}}</div>
        <div class="ft-cl-des" v-if="type == 'default'"><span>共</span> {{data.sections}} <span>节</span></div>
        <div class="ft-cl-des fx-sb" v-if="type == 'default'">
          <span>有{{data.sold}}人在学习</span>
          <span v-if="Number(data.price) != 0" class="ft-16 ft-cl-err">￥ {{(data.price/100).toFixed(2)}}</span>
          <span v-else class="ft-16 ft-cl-err">免费</span> 
        </div>
        <div class="ft-cl-des fx-sb" v-if="type == 'search'">
          <span>共{{data.sections || 0}}节 <em>.</em> 有{{data.sold}}人在学习</span> 
          <span v-if="Number(data.price) != 0" class="ft-16 ft-cl-err">￥ {{(Number(data.price)/100).toFixed(2)}}</span>
          <span v-else class="ft-16 ft-cl-err">免费</span> 
        </div>
        <div class="ft-cl-des" v-if="type == 'like'"><span>评分：</span> 4.9</div>
        <div class="ft-cl-des fx-sb" v-if="type == 'like'">
          <span>{{data.sections}} 次播放 </span> 
          <span v-if="Number(data.price) != 0" class="ft-16 ft-cl-err">￥{{(Number(data.price)/100).toFixed(2)}}</span> 
          <span v-else class="ft-16 ft-cl-err">免费</span> 
        </div>
    </div>
    
  </div>
</template>
<script setup>
import router from '../router';
const props = defineProps({
  data:{
    type: Object,
    default:{}
  },
  type:{
    type: String,
    default:'default'
  }
})
const goDetails = id => {
  router.push({path: '/details', query:{id}})
}
</script>
<style lang="scss" scoped>
.classCards{
    position: relative;
    width: 23%;
    background: #FFFFFF;
    border: 1px solid #EEEEEE;
    border-radius: 8px;
    font-size: 12px;
    line-height: 24px;
    cursor: pointer;
    &:hover{
      box-shadow: 0 4px 6px 2px rgba(108,112,118,0.17);
      
      top: -3px;
    }
    .title{
        line-height: 22px;
        :deep(em){
          font-style: normal;
          color:var(--color-main);
        }
    }
    .image{
        width: 100%;
        height: 160px;
        overflow: hidden;
        position: relative;
        .label{
          position: absolute;
          top: 10px;
          left: 10px;
          font-size: 12px;
          background: rgba(0,0,0,0.49);
          padding:2px 5px;
          color: #fff;
          border-radius: 3px;
        }
        img{
            width: 100%;
            border-radius: 8px 8px 0 0;
        }
    }
    em{
      position: relative;
      top: -3px;
    }
}

</style>
