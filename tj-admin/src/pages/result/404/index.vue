<template>
<div class="noPage container fx-ct bg-wt">
  <result title="404 Not Found" tip="抱歉，您访问的页面不存在" type="404">
    <span class="bt" @click="() => $router.push('/')">返回首页</span>
  </result>
</div>
  
</template>

<script>
export default {
  name: 'Result404',
};
</script>

<script setup >
import Result from '@/components/Result/index.vue';
</script>
<style>
.noPage{
  position: relative;
  top: 10vh;
  border-radius: 8px;
  height: 80vh;
  width: 80%;
  margin: 30px auto 40px auto;
  border-radius: 8px;
  line-height: 40px;
  text-align: center;
}
</style>
