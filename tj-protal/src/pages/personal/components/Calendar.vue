<!-- table切换头部 -->
<template>
  <div class="calendar">
    <div class="label">
      <div class="fx marg-rt-20"><span></span> 未打卡 </div>
      <div class="fx"><span style="background: #ECF4FF;"></span>已打卡</div>
    </div>
    <div class="calendarHead">
      <span v-for="item in week" :key="item">{{item}}</span>
    </div>
    <div class="calendarCont">
      <div class="day" v-for="item in calendarData">
          <div class="taday" v-if="item.date==currentDay">
            <span v-if="item.isRecords == 1" style=" cursor: auto;">已打卡</span>
            <span v-else @click="pointsSignHandle(item)">打卡</span>
          </div>
          <div :class="{noMonth: item.date.split('-')[1] != currentDay.split('-')[1], records: item.isRecords == 1,noRecords: item.isRecords == 0}" v-else>
            {{item.date.split('-')[2]}}
          </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage } from "element-plus";
import { getSignRecords } from "@/api/class.js";
import moment from 'moment';
// 接收父组件数据
const props = defineProps({
  data:{
    type: Array,
    default: []
  }
})
// 定义emits
const emit = defineEmits(['pointsSign']);
const week = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'];
const calendarData = ref([])
const currentDay = moment().format('YYYY-MM-DD')

onMounted(() => {
  // 获取打卡数据
  getSignRecordsHandel()
})
// 日历数据处理
function getMounthDay(day){
  const arr = []
  // 计算本月一号 和最后一天 及一号是周几
  const startDay = moment().startOf('month').format("YYYY-MM-DD") // 本月第一天
  const startDayWeek = Number(moment(startDay).format('E')) // 第一天是周几
  const monthDay = moment().daysInMonth() // 
  // 计算当前月份日历的所有展示天总数
  const n = monthDay + (startDayWeek <= 6 ? startDayWeek+1 : 1);
  const calendarDay = n + (n % 7 == 0 ? 0 :7 - n % 7)
  for(let i = 0; i < calendarDay;i++){
    const date = moment(startDay).subtract(startDayWeek - i , 'd').format('YYYY-MM-DD')
    arr.push({
      date,
      week: week[7 - Number(moment(date).format('E'))],
      isRecords: signData.value[i-startDayWeek] // 0是未打卡 1 是已经打卡 99 是没到呢
    })
  }
  calendarData.value = arr
}
// 打卡
const pointsSignHandle = (val) => {
  emit("pointsSign", val)
  val.isRecords = 1
}
// 获取打卡数据
const signData = ref([])
const getSignRecordsHandel = async () => {
  await getSignRecords()
    .then((res) => {
      if (res.code == 200 ){
        signData.value = res.data
        // 日历数据处理
        getMounthDay(currentDay)
      } else {
        ElMessage({
          message: res.msg,
          type: 'error'
        });
      }
    })
    .catch(() => {
      ElMessage({
        message: "学霸榜请求失败！",
        type: 'error'
      });
    });
}
</script>
<style lang="scss" scoped>
.calendar{
  .label{
    margin-bottom: 20px;
    line-height: 18px;
    font-size: 12px;
    display: flex;
    span{
      display: inline-block;
      width: 18px;
      height: 18px;
      background-color:#E8E8E8;
      margin-right: 10px;
    }
  }
  .calendarHead{
    display: flex;
    border-top: solid 1px #eeeeee;
    border-left: solid 1px #eeeeee;
    span{
      flex: 1;
      text-align: center;
      line-height: 40px;
      border-right: solid 1px #eeeeee;
      border-bottom: solid 1px #eeeeee;
    }
  }
  .calendarCont{
      display:flex;
      flex-wrap: wrap;
      border-left: solid 1px #eeeeee;
      .day{
          width: calc(100% / 7);
          text-align: center;
          line-height: 40px;
          border-right: solid 1px #eeeeee;
          border-bottom: solid 1px #eeeeee;
      }
      .taday{
        background-color: var(--color-main);
        color: #fff;
        cursor: pointer;
      }
      .noMonth{
        color: #ccc;
      }
      .records{
        background: #ECF4FF;
        color: var(--color-main);
      }
      .noRecords{
        background: #E8E8E8;
      }
  }
}
</style>
