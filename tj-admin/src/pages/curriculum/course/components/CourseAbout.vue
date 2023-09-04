<!-- 课程介绍 -->
<template>
  <div class="classAbout">
    <div class="title">
      <span class="itemtitle">课程分类</span>
      <span class="item">{{ courseData.cateNames }}</span>
    </div>
    <div class="title">
      <span class="itemtitle">课程名称</span>
      <span class="item">{{ courseData.name }}</span>
    </div>
    <div class="title">
      <span class="itemtitle">售卖模式</span>
      <span class="item">{{ courseData.free ? "免费" : "收费" }}</span>
    </div>
    <div class="title">
      <span class="itemtitle">课程价格</span>
      <span class="item">{{ (courseData.price / 100).toFixed(2) }}元</span>
    </div>
    <div class="title">
      <span class="itemtitle">课程下架时间</span>
      <span class="item">{{ formatTimeOrdinary(courseData.purchaseEndTime) }}</span>
    </div>
    <div class="title">
      <span class="itemtitle">课程有效期</span>
      <span class="item"
        >{{ courseData.validDuration > 99 ? "永久" : courseData.validDuration }}
        <span v-if="courseData.validDuration <= 99">月</span>
      </span>
    </div>
    <div class="title">
      <span class="itemtitle">课程介绍</span>
      <span class="item">{{ courseData.introduce }}</span>
    </div>
    <div class="title">
      <span class="itemtitle">适学人群</span>
      <span class="item">{{ courseData.usePeople }}</span>
    </div>
    <div class="title">授课老师</div>
    <div class="classTeacher">
      <div class="swiper-button" v-show="courseTeacher.data.length > 2">
        <span class="prev"><span class="iconfont icon-a-shouqi2x"></span></span>
        <span class="next"><span class="iconfont icon-a-shouqi2x"></span></span>
      </div>
      <div class="swiperWrapper">
        <!-- 创建一个swiper翻页，且一个页面最多同时显示两项 -->
        <swiper
          :mode="horizontal"
          :modules="[Pagination, Navigation]"
          :options="swiperOption"
          :slides-per-view="2"
          :navigation="{ nextEl: '.next', prevEl: '.prev' }"
          ref="mySwiper"
          :space-between="50"
          :pagination="{ clickable: true }"
          :scrollbar="{ draggable: true }"
          @swiper="onSwiper"
          @slideChange="onSlideChange"
        >
          <swiper-slide
            class="swiper-slide"
            v-for="(item, i) in courseTeacher.data"
            :key="i"
          >
            <div class="teacherInfo">
              <div class="teach">
                <img :src="item.icon" alt="" />
                <div>
                  <div class="name">{{ item.name }}</div>
                  <div>{{ item.job }}</div>
                </div>
              </div>
              <p class="about">
                {{ item.introduce }}
              </p>
              <!-- <div class="teachertoolip">
                <div class="teach">
                  <img :src="item.icon" alt="" />
                  <div>
                    <div class="name">{{ item.name }}</div>
                    <div>{{ item.job }}</div>
                  </div>
                </div>
                <p class="about">
                  {{ item.introduce }}
                </p>
              </div> -->
            </div>
          </swiper-slide>
        </swiper>
      </div>
    </div>
    <div class="title">课程详情</div>
    <div class="item">{{ courseData.detail }}</div>
  </div>
</template>
<script setup>
import { formatTimeOrdinary } from "@/utils/index";
// 讲师信息轮播插件
import { Swiper, SwiperSlide } from "swiper/vue"
import { Pagination } from 'swiper'//swiper分页器
import { Navigation } from "swiper"//swiper左右切换
import 'swiper/css/navigation' //swiper导航
import 'swiper/css' //swiper css


// 引入父级传参
defineProps({
  courseData: {
    type: Object,
    default: {}
  },
  courseTeacher: {
    type: Object,
    default: {}
  }
})
</script>
<style lang="scss" scoped>
.classAbout {
  margin-top: 40px;
  font-weight: 400;
  font-size: 14px;
  line-height: 30px;
  .title {
    margin-top: 28px;
    font-family: PingFangSC-S0pxibold;
    font-weight: 600;
    font-size: 14px;
    color: #332929;
    letter-spacing: 0;
    display: flex;
    .itemtitle {
      width: 90px;
      display: inline-block;
      margin-right: 7px;
    }
    .item {
      font-family: PingFangSC-Regular;
      font-weight: 400;
      font-size: 14px;
      color: #332929;
      left: 96px;
      flex: 1;
      letter-spacing: 0;
      text-align: justify;
      line-height: 28px;
    }
  }
  .classTeacher {
    position: relative;
    display: flex;
    height: 210px;
    width: 100%;
    justify-content: space-between;
    // transform: translate(-50%);
    margin-top: 20px;
    // left: 50%;
    :deep(.swiper-button-next, .swiper-button-prev) {
      top: 0;
    }
    .teacherInfo {
      overflow: hidden;
      padding: 20px 30px;
      margin-right: 20px;
      height: 201.5px;
      // flex: 50%;
      background: #faf7f6;
      border-radius: 8px;
      .teachertoolip {
        overflow: hidden;
        padding: 20px 30px;
        margin-right: 20px;
        // flex: 50%;
        background: #faf7f6;
        border-radius: 8px;
        display: none;
      }
      .teach {
        display: flex;
        margin-bottom: 10px;
        img {
          width: 64px;
          height: 64px;
          border-radius: 64px;
          margin-right: 15px;
        }
        .name {
          font-weight: 600;
        }
      }
      .about {
        font-size: 14px;
        text-overflow: ellipsis;
        overflow: hidden;
        display: -webkit-box;
        -webkit-line-clamp: 3;
        -webkit-box-orient: vertical;
      }
      &:last-child {
        margin-right: 0;
      }
    }
  }
}
.swiper-button {
  position: absolute;
  right: 10px;
  top: -47px;
  span {
    cursor: pointer;
  }
  .next,
  .prev {
    position: relative;
    display: inline-block;
    width: 21px;
    height: 21px;
    border-radius: 21px;
    margin-left: 17px;
    box-shadow: 0px 0px 10px rgba($color: #d3d3d3, $alpha: 1);
    cursor: pointer;
    color: #ff734f;
    span {
      position: relative;
      display: inline-block;
      left: 1px;
      top: -5px;
      width: 20px;
      height: 20px;
      font-size: 12px;
      text-align: center;
    }
  }
  .prev {
    transform: rotate(180deg);
  }
  .swiper-button-disabled {
    color: #b5abab;
  }
}
.swiperWrapper {
  width: 100%;
  padding-left: 20px;
  border-radius: 8px;
  overflow: hidden;
  display: inline-block;
  .swiper-slide {
    // width: auto !important;
  }
  img {
    width: 64px;
    height: 64px;
    border-radius: 50%;
  }
  :deep(.swiper) {
    border-radius: 8px;
  }
  :deep(.swiper-pagination-bullet) {
    width: 28px;
    height: 5px;
    border-radius: 4px;
    background: rgba(255, 255, 255, 0.5);
    opacity: 1;
  }
  :deep(.swiper-pagination-bullets, .swiper-pagination-bullets.swiper-pagination-horizontal) {
    bottom: 15px;
  }
}
.itemtitle {
  // width: 57px;
}
.iconfont {
  &::before {
    content: "\e654";
  }
}
</style>
