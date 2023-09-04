<!-- table切换头部 -->
<template>
  <div class="tableSwitchBar">
    <span :class="{title:true, act:actId == item.id, 'font-bt2':actId != item.id}" @click="checkHandle(item.id)" v-for="(item, index) in data" :key="index">
      {{item.name}}
    </span>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue';
// 接收父组件数据
const props = defineProps({
  data:{
    type: Array,
    default: []
  }
})
// 定义emits
const emit = defineEmits(['changeTable'])

const actId = ref();

onMounted(() => {
  // 默认选项
  actId.value = props.data[0].id
})
// // 点击切换效果
const checkHandle = (id) => {
  actId.value = id
  emit('changeTable', id)
}

</script>
<style lang="scss" scoped>
.tableSwitchBar{
    .title{
      display: inline-block;
      font-size: 14px;
      width: 84px;
      margin-right: 20px;
      font-weight: 400;
      text-align: center;
      cursor: pointer;
    }
    .act{
      font-weight: 400;
      position: relative;
      background-color: var(--color-font1);
      color: #fff;
      padding: 5px 0px;
      border-radius: 28px;
    }
  }
</style>
