<!-- 顶部Banner区域左侧列表 -->
<template>
<div class="swiperWrapper fx-1">
   <swiper
      :modules="[ Pagination ]"
      :space-between="20"
      autoplay
      :pagination="{ clickable: true }"
    >
      <swiper-slide
        class="swiper-slide"
        v-for="(item, i) in data"
        :key="i"
      >
        <img :src="item" class="banImg" alt="" />
      </swiper-slide>
    </swiper>
</div>
</template>
<script setup>
import { Swiper, SwiperSlide } from "swiper/vue";
import {  Pagination } from 'swiper';

const props = defineProps({
  data:{
    type: Array,
    default:[]
  }
})

</script>
<style lang="scss" scoped>
.swiperWrapper {
      padding-left: 20px;
      border-radius: 8px;
      overflow: hidden;
      img {
        width: 100%;
        height: 388px;
        border-radius: 8px;
      }
      :deep(.swiper){
        border-radius: 8px;
      }
      :deep(.swiper-pagination-bullet){
        width: 28px;
        height: 5px;
        border-radius: 4px;
        background: rgba(255,255,255,0.5);
        opacity: 1
      }
      :deep(.swiper-pagination-bullets, .swiper-pagination-bullets.swiper-pagination-horizontal){
        bottom: 15px;
      }
      :deep(.swiper-pagination-bullet-active){
        background-color: #fff;
      }
    }
</style>
