<!-- 课程列表 -->
<template>
  <div class="mainWrapper">
    <div class="container banner fx">
      <div class="categorys bg-wt">
        <!-- 头部分类 -->
        <ClassCategory :data="classCategorys"></ClassCategory>
      </div>
      <!-- 头部幻灯片 -->
      <Swiper :data="imags"></Swiper>
    </div>
    <!-- 直播公开课 -->
    <div class="bg-wt pd-tp-30">
      <OpenClass
        title="直播公开课"
        class="container bg-wt"
        :data="freeClassData"
      ></OpenClass>
    </div>
    <!-- 新课推荐 -->
    <div class="pd-tp-30">
      <OpenClass
        title="新课推荐"
        class="container"
        :data="freeClassData"
      ></OpenClass>
    </div>
    <!-- 广告位 -->
    <div class="globalTopBanner" style="display: block;">
      <img src="@/assets/adv.png" />
    </div>
    <!-- 精品好课 -->
    <div class="bg-wt pd-tp-30">
      <OpenClass
        title="精品好课"
        class="container bg-wt"
        :data="freeClassData"
      ></OpenClass>
    </div>
  </div>
</template>
<script setup>
/** 数据导入 **/

import { onMounted, ref } from "vue";
import { ElMessage } from "element-plus";
import { getClassCategorys, getFreeClassList } from "@/api/class.js";
import ClassCategory from "@/components/ClassCategory.vue";
import OpenClass from "./components/OpenClass.vue";
import Swiper from "./components/Swiper.vue";
import banner1 from "@/assets/banner1.jpg";
import banner2 from "@/assets/banner2.jpg";
import banner3 from "@/assets/banner3.jpg";

// 分类数据
const classCategorys = ref([]);
// banner幻灯片图片
const imags = [banner1, banner2, banner3];
// 直播公开课的数据
const freeClassData = ref([]);

// mounted生命周期
onMounted(() => {
  // 获取三级分类信息
  getClassCategoryData();
  // 获取精品公开课
  getFreeClassListData();
});

/** 方法定义 **/

// 获取三级分类信息
const getClassCategoryData = async () => {
  await getClassCategorys()
    .then((res) => {
      if (res.code == 200) {
        classCategorys.value = res.data;
      } else {
        ElMessage(res.meg);
      }
    })
    .catch(() => {
      ElMessage("分类请求出错！");
    });
};
// 精品公开课接口
const getFreeClassListData = async () => {
  await getFreeClassList()
    .then((res) => {
      if (res.code == 200) {
        freeClassData.value = res.data;
      } else {
        ElMessage(res.meg);
      }
    })
    .catch(() => {
      ElMessage("分类请求出错！");
    });
};
// 新课推荐
const getNewClassListData = async () => {
  await getClassCategorys()
    .then((res) => {
      if (res.code == 200) {
        classCategorys.value = res.data;
      } else {
        ElMessage(res.meg);
      }
    })
    .catch(() => {
      ElMessage("分类请求出错！");
    });
};
// 精品好课
const getGoodClassListData = async () => {
  await getClassCategorys()
    .then((res) => {
      if (res.code == 200) {
        classCategorys.value = res.data;
      } else {
        ElMessage(res.meg);
      }
    })
    .catch(() => {
      ElMessage("分类请求出错！");
    });
};
</script>
<style lang="scss" scoped>
.mainWrapper {
  .banner {
    padding: 20px 0;
    .categorys {
      position: relative;
      width: 236px;
      height: 388px;
      border-radius: 8px;
      z-index: 9;
    }
  }
  .globalTopBanner{
      width: 100%;
      min-width: 1152px;
      max-width: 2560px;
      height: 72px;
      overflow: hidden;
      cursor: pointer;
      position: relative;
      img{
        height: 100%;
        display: block;
        position: absolute;
        top: 0;
        left: 50%;
        transform: translateX(-50%);
      }
  }
}
</style>
