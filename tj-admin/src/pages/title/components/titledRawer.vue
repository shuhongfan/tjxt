<!--抽屉，鼠标经过列表标题显示-->
<template>
  <div class="drawerBox">
    <el-drawer
      v-model="drawer"
      title="题目信息"
      :direction="direction"
      :before-close="handleClose"
    >
      <div class="drawerCon">
        <h6 class="tit" v-html="detailData.name"></h6>
        <ul class="item">
          <li v-for="(item, index) in detailData.options" :key="index">
            {{ statusWord(index + 1) }}、<span v-html="item"></span>
          </li>
        </ul>
        <div class="drawerInfo">
          <p>
            正确答案：<span v-if="detailData.type === 4">{{
              detailData.answers[0] === 1 ? '正确' : '错误'
            }}</span
            ><span v-else v-for="(val, i) in detailData.answers" :key="i">{{
              statusWord(val)
            }}</span>
          </p>
          <div>
            <p class="analysisText">
              <span>答案解析：</span>
              <span>{{detailData.analysis===''?'暂无':detailData.analysis}}</span>
            </p>
          </div>
        </div>
      </div>
      <div class="drawerFoot">
        <ul>
          <li v-for="(item, index) in detailData.cates" :key="index">
            <span
              >{{ item.firstCateName }}/{{ item.secondCateName }}/{{
                item.thirdCateName
              }}</span
            >
          </li>
        </ul>
      </div>
    </el-drawer>
  </div>
</template>
<script setup>
import { ref } from "vue"
const emit = defineEmits()
// 获取父组件值、方法
const props = defineProps({
  // 抽屉显示隐藏
  drawer: {
    type: Boolean,
    default: false,
  },
  //   详情数据
  detailData: {
    typeof: Object,
    default: {},
  },
})
// ------定义方法------
const handleClose = () => {
  emit("handleClose")
}
// 把数字转成英文大写
const statusWord = (val) => {
  switch (val) {
    case 1:
      return "A"
    case 2:
      return "B"
    case 3:
      return "C"
    case 4:
      return "D"
    case 5:
      return "E"
    case 6:
      return "F"
    case 7:
      return "G"
    case 8:
      return "H"
    case 9:
      return "I"
    case 10:
      return "J"
  }
};
</script>
<style lang="scss" scoped>
.drawerBox {
  .item li {
    display: flex;
  }
  .drawerInfo p {
    display: flex;
  }
}
</style>
