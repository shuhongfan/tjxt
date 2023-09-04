<!-- 首页头部课程分类 -->
<template>
    <div class="classCategory ft-14" :class="{ classCategoryHeader: type == 'float'}">
      <div @mouseout="mouseoutHandle()">
        <div class="items">
          <div class="item" v-for="item in data" :key="item.id" @mouseover="mouseoverHandle(item.children)" >
            <!-- 一级分类 -->
            <div class="fx-sb">
              <div @click="() => $router.push({path:'/search', query:{type:'categoryIdLv1',id:item.id}})" class="font-bt2" >{{item.name}}</div>
              <img src="@/assets/icon_more.png" alt="">
            </div>
            <!-- 二级分类前两个 -->
            <div class="desc ft-12 ft-cl-des">
              <span @click="() => $router.push({path:'/search', query:{type:'categoryIdLv2',id:item.children[0].id}})" class="font-bt2 ft-cl-des" v-if="item.children.length > 0">{{ item.children[0].name}}</span>
              <span v-if="item.children.length > 1"> / </span>
              <span @click="() => $router.push({path:'/search', query:{type:'categoryIdLv2',id:item.children[1].id}})" v-if="item.children.length > 1" class="font-bt2 ft-cl-des">{{  item.children[1].name}}</span>
            </div>
          </div>
        </div>
        
        <!-- 展示详情 -->
        <div class="allCategory" v-show="isDetails" @mouseover="mouseoverHandle()">
          <div class="cont">
            <div class="fx ft-wt-600 pd-bt-10" v-for="item in categorys" :key="item.id">
              <span class="tit font-bt2" @click="() => $router.push({path:'/search', query:{type:'categoryIdLv2',id:item.id}})">{{item.name}} :</span> 
              <div class="name fx-1">
                <span class="ft-wt-400 cur-pt font-bt2" @click="() => $router.push({path:'/search', query:{type:'categoryIdLv3',id:it.id}})" v-for="it in item.children" :key="it.id">{{it.name}}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
</template>
<script setup>
import {ref} from 'vue';
  const isDetails = ref(false);
  // 接收的全部分类
  const props = defineProps({
    data: {
        type: Array,
        default: []
    },
    type: {
      type: String,
      default: ''
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
.classCategory, .classCategoryHeader{
  position: relative;
  .items{
    height: 388px;
    overflow: hidden;
  }
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
    overflow: hidden;
    height: 388px;
    padding: 20px 30px 20px 20px;
    background: #FFFFFF;
    box-shadow: 0 4px 6px 2px rgba(108,112,118,0.17);
    border-radius: 8px;
    .cont{
      height: 360px;
      // overflow-y: scroll;
    }
    .tit{
      display: inline-block;
      width: 120px;
      overflow: hidden;
      height: 20px;
      border-right:none;
    }
    span{
      display: inline-block;
      padding: 0 10px 0 10px;
      border-right: solid 1px var(--color-font5);
      line-height: 16px;
      margin-bottom: 10px;
      &:last-child{
        border-right:none;
      }
    }
  }
}
.classCategoryHeader{
  .items{
    min-height: 388px;
    width: 236px;
    overflow: inherit;
    box-shadow: 0 4px 6px 2px rgba(108,112,118,0.17);
    border-radius: 8px;
    .item:first-child{
      position: relative;
      &::before{
        content: '';
        display: inline-block;
        width: 15px;
        height: 15px;
        position: absolute;
        z-index: -1;
        background-color: #fff;
        top: -6px;
        left: 44%;
        transform: rotate(45deg);
        box-shadow: 4px 4px 6px 2px rgba(108,112,118,0.3);
        // border:solid 1px #E3E5E9;
      }
      &:hover{
        background-color: var(--color-background2);
        &::before{
          background-color: var(--color-background2);
        }
      }
    }
  }
}
</style>
