<!-- 分类 -->
<template>
    <div class="classCategory ft-14">
      <div @mouseout="mouseoutHandle()">
        <div class="item" v-for="item in data" :key="item.id" @mouseover="mouseoverHandle(item.children)" >
          <!-- 一级分类 -->
          <div class="fx-sb">
            <div>{{item.name}}</div>
            <img src="@/assets/icon_more.png" alt="">
          </div>
          <!-- 二级分类前两个 -->
          <div class="desc ft-12 ft-cl-des">
            <span v-if="item.children.length > 0">{{ item.children[0].name}}</span>
            <span v-if="item.children.length > 1"> / </span>
            <span v-if="item.children.length > 1">{{  item.children[1].name}}</span>
          </div>
        </div>
        <!-- 展示详情 -->
        <div class="allCategory" v-show="isDetails" @mouseover="mouseoverHandle()">
          <div class="ft-wt-600 pd-bt-20" v-for="item in categorys" :key="item.id">
            <span class="tit">{{item.name}} :</span> <span class="ft-wt-400" v-for="it in item.children" :key="it.id">{{it.name}}</span>
          </div>
        </div>
      </div>
    </div>
</template>
<script setup>
import { ref } from 'vue';
  const isDetails = ref(false);
  // 接收的全部分类
  const props = defineProps({
    data: {
        type: Array,
        default: []
    }
  })
  // 详情的二级分类展示数据
  const categorys = ref();

  // 鼠标滑过修改当前二级分类数据
  const mouseoverHandle = (item) => {
    item ? categorys.value = item : null
    isDetails.value = true
  }

  const mouseoutHandle = () => {
    isDetails.value = false
  }
  
</script>
<style lang="scss" scoped>
.classCategory{
  position: relative;
  .item{
    padding:15px;
    height: 78px;
    border-bottom: solid 1px #eeeeee;
    img{
      width: 20px;
      height: 20px;
    }
    .desc{
      padding-top: 8px;
    }
    &:hover{
      background-color: var(--color-background2);
    }
    &:first-child{
      border-radius: 8px 8px 0 0;
    }
    &:nth-child(5){
      border-radius: 0 0 8px 8px;
    }
  }
  .allCategory{
    position: absolute;
    top: 0;
    left: 236px;
    width: 537px;
    height: 388px;
    padding: 20px 30px;
    background: #FFFFFF;
    box-shadow: 0 4px 6px 2px rgba(108,112,118,0.17);
    border-radius: 8px;
    .tit{
      display: inline-block;
      width: 110px;
    }
    span{
      display: inline-block;
      padding: 0 10px;
      border-right: solid 1px var(--color-font5);
      line-height: 20px;
      &:first-child, &:last-child{
        border-right:none;
      }
    }
  }
}
</style>
