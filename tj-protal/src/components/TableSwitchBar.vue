<!-- table切换头部 -->
<template>
  <div class="tableSwitchBar">
    <span :class="{title:true,'font-bt4':actId != item.id, act:actId == item.id}"  @click="checkHandle(item.id)" v-for="(item, index) in data" :key="index">
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
      margin-right: 20px;
      font-weight: 400;
      cursor: pointer;
    }
    .act{
      font-weight: 600;
      position: relative;
      &::before{
        position: absolute;
        left: 50%;
        transform: translate(-50%);
        content: '';
        width: 33px;
        height: 4px;
        border-radius: 4px;
        bottom: -8px;
        background: var(--color-main);
      }
    }
  }
</style>
