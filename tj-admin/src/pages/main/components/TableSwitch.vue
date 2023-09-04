<!-- table切换头部 -->
<template>
  <div class="tableSwitchBar">
    <span
      :class="{ title: true, act: actId == item.id }"
      @click="checkHandle(item.id)"
      v-for="(item, index) in data"
      :key="index"
    >
      {{ item.name }}
    </span>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
// 接收父组件数据
const props = defineProps({
  data: {
    type: Array,
    default: []
  }
})
// 定义emits
const emit = defineEmits(['changeTable'])

const actId = ref()

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
.tableSwitchBar {
  margin-bottom: 15px;
  height: 40px;
  .title {
    display: inline-block;
    margin-right: 25px;
    text-align: center;
    cursor: pointer;
    width: 150px;
    height: 22px;
    font-family: PingFangSC-S0pxibold;
    font-weight: 600;
    font-size: 16px;
    color: #332929;
    letter-spacing: 0;
  }
  .act {
    width: 150px;
    // height: 22px;
    font-family: PingFangSC-S0pxibold;
    font-weight: 600;
    font-size: 16px;
    color: #332929;
    letter-spacing: 0;
    // text-align: right;
    position: relative;
    padding: 5px 0px;
    border-radius: 28px;
    &::before {
      position: absolute;
      left: 50%;
      transform: translate(-50%);
      content: '';
      width: 33px;
      height: 4px;
      border-radius: 4px;
      bottom: -15px;
      background: #ff734f;
    }
  }
}

</style>
