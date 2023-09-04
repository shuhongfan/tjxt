<!--标题-->
<template>
  <div class="tit">
    <span v-if="active === 0">基本信息</span
    ><span v-if="active === 1">课程目录</span
    ><span v-if="active === 2">课程视频</span
    ><span v-if="active === 3">课程题目</span
    ><span v-if="active === 4">课程老师</span
    ><i></i>请您遵守国家相关规定，切勿上传低俗色情、暴力恐怖、谣言诈骗、侵权盗版等相关内容！
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from "vue";
// 获取父组件值、方法
const props = defineProps({
  // 搜索对象
  active: {
    type: Number,
    default: 0,
  },
});
</script>

<style scoped>
</style>