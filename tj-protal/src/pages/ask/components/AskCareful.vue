<!-- 提问注意事项-问答详情 -->
<template>
  <div class="askCareful">
    <div class="tit">提问注意事项</div>
    <div>
      <p>问答禁止这些提问</p>
      <p>1、禁止发布求职、交易、推广、广告类与问答无关信息将一律清理；</p>
      <p>2、尽可能详细描述您的问题，如标题与内容不符，或与问答无关的信息将被删除扣分；</p>
      <p>3、问答刷屏用户一律冻结帐号。</p>
    </div>
  </div>
</template>
<script setup></script>
<style lang="scss" scoped>
.askCareful {
  width: 344px;
  height: min-content;
  background: #FFFFFF;
  border-radius: 8px;
  padding:30px;
  line-height: 22px;
  font-size: 14px;
  .but{
    border-bottom: solid 1px var(--color-background1);
    padding: 0 50px 30px 50px;
    margin-bottom: 30px;
  }
  p{
    margin: 28px 0;
  }
  .tit{
    font-weight: 600;
    font-size: 20px;
  }
}
</style>
