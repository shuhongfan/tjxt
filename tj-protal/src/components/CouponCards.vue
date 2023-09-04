<!-- 优惠券卡片 -->
<template>
 <div class="couponCards fx" v-for="(item, index) in data" :key="index">
   <div class="price ft-cl-wt" v-if="item.discountType == 4 || item.discountType == 1 || item.discountType == 3">
     <div>￥ <em>{{item.discountValue / 100}}</em></div>
     <div class="desc">{{item.rule}}</div>
   </div>
   <div class="price ft-cl-wt" v-if="item.discountType == 2 || item.discountType == 5">
     <div><em>{{item.discountValue / 10}}</em> 折</div>
     <div class="desc">{{item.rule}}</div>
   </div>
    <div class="info">
        <div class="tit">{{item.name}}</div>
        <div><em>适用平台：</em>{{item.specific  ? '限定分类' : '全部课程'}}</div>
      <div><em>有效日期：</em> {{item.termDays ? `${item.termDays}天` : moment(item.termEndTime).format('YYYY-MM-DD hh:mm:ss')}}</div>
    </div>
    <div class="butCont fx-ct" v-if="type == 1"><span @click="() => {$router.push({path: '/search/index'})}" class="bt">去使用</span></div>
</div>
</template>
<script setup>
import moment from 'moment';
const props = defineProps({
  data:{
    type: Object,
    default:{}
  },
  type:{
    type: Number,
    default:1
  }
})
</script>
<style lang="scss" scoped>
.couponCards{
  width: calc(50% - 18px);
  background: #FFFFFF;
  border: 1px solid #EEEEEE;
  border-left:none;
  border-radius: 8px;
  margin-bottom: 30px;
  margin-right: 35px;
  &:nth-child(2n){
    margin-right: 0;
  }
  .price{
    width: 138px;
    height: 124px;
    background:url('@/assets/bg_yhq1.png') left center no-repeat;
    background-size: cover;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    font-weight: 400;
    font-size: 12px;
    em{
      font-style: normal;
      font-family: PingFangSC-Semibold;
      font-weight: 600;
      font-size: 42px;
    }
    .desc{
        padding: 0 15px;
        text-align: center;
    }
  }
  .info{
     display: flex;
     padding-left: 25px;
     text-align: left;
    flex-direction: column;
    justify-content: center;
    font-size: 12px;
    flex: 1;
    line-height: 28px;
    .tit{
      font-weight: 500;
      font-size: 18px;  
      margin-bottom: 15px;
    }
    em{
      font-style: normal;
      color: var(--color-font3);
    }
    .time{
        white-space:nowrap;
    }
  }
  .butCont{
    padding-right: 20px;
    span{
      font-size: 12px;
      min-width: 80px;
      text-align: center;
      padding: 0px 15px;
      height: 28px;
      line-height: 28px;
      border-radius: 28px;
    }
  }
}
</style>
