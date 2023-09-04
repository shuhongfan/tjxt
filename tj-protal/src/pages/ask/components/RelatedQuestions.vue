<!-- 相关问题 - 回答页面 -->
<template>
  <div class="relatedQuestions">
    <div class="but"><span class="bt bt-round" @click="() => {$router.push({path:'/ask', query:{id, title}})}"> 发新问题 </span></div>
    <div class="tit">相关问题</div>
    <div>
    <p>自学 Java 怎么入门？</p> 
    <p>转行学Java，月薪5k到30k，给兄弟们一些个人建议</p>
    <p>大家学习 Java ，是看书学习快，还是看视频学习快呢 ？</p>
    <p>今年32岁女生，刚开始自学 java，给自己一年时间有出路吗？</p>
    </div>
  </div>
</template>
<script setup>
// 引入父级传参
defineProps({
  id:{
    type:String,
    default:''
  },
  title:{
    type:String,
    default:''
  }
})

</script>
<style lang="scss" scoped>
.relatedQuestions {
  width: 344px;
  height: fit-content;
  background: #FFFFFF;
  border-radius: 8px;
  padding:30px;
  line-height: 24px;
  margin-top: 30px;
  font-size: 14px;
  .but{
    border-bottom: solid 1px var(--color-background1);
    padding: 0 50px 30px 50px;
    margin-bottom: 30px;
  }
  p{
    margin: 20px 0;
  }
  .tit{
    font-weight: 600;
    font-size: 20px;
  }
}
</style>
