<!-- table切换头部 -->
<template>
  <div class="integralRankTab">
    <div class="item head fx-sb">
      <span>排名</span> <span class="ct">姓名</span> <span>积分</span>
    </div>
    <div class="item myItem fx-sb">
      <span>{{ data.rank || '--' }}</span>
      <span class="ct">我</span>
      <span>{{ data.points || '--'  }}</span>
    </div>
    <div v-if="data.boardList">
      <div
        class="item fx-sb"
        v-for="(item, index) in data.boardList"
        :key="index"
      >
        <img v-if="index <= 2" :src="ranking(index + 1)" alt="" />
        <span class="lt" v-else>{{ ranking(index + 1) }}</span>
        <span class="ct">{{ item.name || "--" }}</span>
        <span>{{ item.points }}</span>
      </div>
    </div>
    <div class="onData" v-else>
      <Empty ></Empty>
    </div>
  </div>
</template>
<script setup>
import rank1 from "@/assets/icon_rank1.png";
import rank2 from "@/assets/icon_rank2.png";
import rank3 from "@/assets/icon_rank3.png";
import Empty from "@/components/Empty.vue";

// 前三名图片展示
const ranks = [rank1, rank2, rank3];
const ranking = (num) => {
  let val = num;
  if (num <= 3) {
    val = ranks[num - 1];
  }
  return val;
};
// 接收父组件数据
const props = defineProps({
  data: {
    type: Object,
    default: [],
  },
  type:{ // 控制表格最后一样是否有线条展示 固定高度的有的 非固定没有 默认 有
    type: Boolean,
    default: false
  }
});
</script>
<style lang="scss" scoped>
.integralRankTab {
  border-top: solid 1px #eeeeee;
  border-left: solid 1px #eeeeee;
  border-right: solid 1px #eeeeee;
  border-bottom: solid 1px #eeeeee;
  height: calc(100% - 86.7px);
  .item {
    padding: 0px 30px;
    line-height: 40px;
    height: 41px;
    font-weight: 400;
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-bottom: solid 1px #eeeeee;
    img {
      width: 40px;
      height: 40px;
    }
    span:first-child {
      display: inline-block;
      text-align: center;
      width: 40px;
    }
    span:last-child {
      display: inline-block;
      text-align: center;
      width: 40px;
    }
    .ct {
      flex: 1;
      display: inline-block;
      text-align: center;
      width: 120px;
    }
  }
  .item:last-child {
    border-bottom: solid 1px #eeeeee;
  }
  .item:nth-child(10) {
    border-bottom: none;
  }
  .head {
    height: 60px;
    line-height: 60px;
    font-weight: 600;
  }
  .myItem {
    line-height: 40px;
    background: #ecf4ff;
    color: var(--color-main);
  }
  .onData{
    display: flex;
    align-items: center;
    justify-content: center;
    color: #999;
    height: calc(100% - 100px);
  }
}
.fxTab{
  .item:last-child{
    border-bottom: none;
  }
}
</style>
