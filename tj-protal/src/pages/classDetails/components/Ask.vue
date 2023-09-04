<!-- 常见问题模块 -->
<template>
  <div class="ask bg-wt marg-bt-20">
    <div class="title">常见问题</div>
    <div class="askCont">
      <li v-for="(item, index) in data" :key="index">{{item.ask}} <span>{{item.answer}}</span></li>
    </div>
  </div>
</template>
<script setup>

// 引入父级传参
defineProps({
  data:{
    type: Object,
    default:{}
  }
})
</script>
<style lang="scss" scoped>
.ask{
    padding: 30px;
    border-radius: 8px;
    .title{
        font-weight: 600;
        font-size: 20px;
        margin-bottom: 10px;
    }
    .askCont{
        li{
            position: relative;
            padding: 10px 0;
            list-style:disc;
            cursor: pointer;
            font-size: 14px;
            span{
                position: absolute;
                display: none;
                width: 298px;
                min-height: 52px;
                line-height: 25px;
                font-size: 14px;
                padding: 10px;
                border-radius: 8px;
                background: #FFFFFF;
                box-shadow: 0 0 6px 2px rgba(108,112,118,0.17);
                left: -320px;
                top:-10px;
                &::after{
                    position: absolute;
                    background: #FFFFFF;
                    transform: rotate(45deg);
                    right: -8px;
                    top: 25px;
                    content: '';
                    width: 15px;
                    height: 15px;
                    box-shadow: 0 0 6px 2px rgba(108,112,118,0.17);
                }
                &::before{
                    position: absolute;
                    background: #fff;
                    transform: rotate(45deg);
                    right: -6px;
                    top: 25px;
                    content: '';
                    width: 17px;
                    height: 17px;
                    z-index: 2;
                    box-shadow: 0 0 6px 2px #fff;
                }
            }
            &:hover{
                color: var(--color-main);
                span{
                    display: block;
                    color: var(--color-font1);
                }
            }
        }
    }
}
</style>
