<!-- 倒计时处理 - 支付 -->
<template>
  <div class="table-right flex-a-center">
      <div class="time-text">
          <span class="timeTextSpan" v-for="item,index of timeData.m" >{{item}}</span>
          <span class="timeTextSpan1" >分</span>
          <span class="timeTextSpan" v-for="item,index of timeData.s" >{{item}}</span>
          <span class="timeTextSpan1" >秒</span>
      </div>
  </div>
</template>
<script setup>
// 数据引入
import { onMounted, onUnmounted, reactive, watchEffect } from 'vue';
import moment from 'moment'
// 接收父级数据
const props = defineProps({
  endTime:{   
    type: String,
    default: ''
  }
})
// emit 初始化
const emit = defineEmits(['timeOver'])
// 时间数据初始化
const timeData = reactive({
  h:'00',
  m:'00',
  s:'00',
  timer:null
})
// 监听timeData的数据变化
 watchEffect(() => {
  clearInterval(timeData.timer)
  timeData.timer = setInterval(()=>{init()},1000)
 })

onMounted(() => {
  clearInterval(timeData.timer)
  timeData.timer = setInterval(()=>{init()},1000)
})
// 时间数据处理
const init = () => {
   let time = moment(props.endTime).diff(moment())
    if(time <= 0){
      clearInterval(timeData.timer)
      onOver()
      return
    }

    let t = time / 1000;
    let d = Math.floor(t / (24 * 3600));  //剩余天数，如果需要可以自行补上
    let h = Math.floor((t - 24 * 3600 * d) / 3600) + d*24;  //不需要天数，把天数转换成小时
    let _h = Math.floor((t - 24 * 3600 * d) / 3600)  //保留天数后得小时
    let m = Math.floor((t - 24 * 3600 * d - _h * 3600) / 60);
    let s = Math.floor((t - 24 * 3600 * d - _h * 3600 - m * 60));
    
    timeData.h = String(h).length == 1? '0'+String(h):String(h)
    timeData.m = String(m).length == 1? '0'+String(m):String(m)
    timeData.s = String(s).length == 1? '0'+String(s):String(s)
} 
//倒计时结束得回调
const onOver = () => {
  emit('timeOver') 
}
//组件卸载清掉定时器
onUnmounted(()=>{
  clearInterval(timeData.timer)
})
</script>
<style lang="scss">
</style>
