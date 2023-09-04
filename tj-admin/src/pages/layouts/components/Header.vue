<template>
  <header class="bg-wt">
    <div class="fx headerInfo">
      <div class="fx-1 marg-lt-20">
        <div class="fx" v-show="route.meta.title != '首页'">
          <!-- <span class="textDefault1" @click="() => $router.push('/')">
            首页
          </span> -->
          <!-- <span class="line"> / </span> -->
          <span
            class="textDefault1"
            @click="() => $router.push({ path: route.matched[0].path })"
            >{{ route.matched[0].meta.title }}</span
          >
          <span class="line" > / </span>
          <span
            v-if="route.meta && route.meta.fmeta"
            class="textDefault1"
            @click="() => $router.push({ path: route.meta.fmeta.path })"
            >{{ route.meta.fmeta.title }}</span
          >
          <span v-if="route.meta && route.meta.fmeta" class="line" > / </span>
          <span
            class="textDefault1 ft-cl-des"
            @click="() => $router.push({ path: route.matched[1].path })"
            >{{ route.matched[1].meta.title }}</span
          >
        </div>
        <div class="fx" v-show="route.meta.title == '首页'">
          <span class="textDefault1" @click="() => $router.push('/')">
            工作台
          </span>
        </div>
      </div>
      <div class="fx-al-ct">
        <div v-if="$route.path == '/main/index'" class="wecom">
          <img src="@/assets/wecom-temp.png" width="268" height="22" alt="">
        </div>
        <div class="fx-al-ct" v-if="isToken && userInfo">
          <router-link to="/my/index">
            <img
              class="headIcon"
              :src="userInfo.icon"
              :onerror="onerrorImg"
              alt=""
            />
            <div>{{ userInfo.name || "admin" }}</div>
          </router-link>
          <span class="vline"></span>
          <div class="back" @click="goLogin()">
            <img src="@/assets/out.png" alt="" style="width:19px;height: 15px;" class="out"/>
          </div>
        </div>
      </div>
    </div>
  </header>
</template>
<script setup>
import { onMounted, ref, nextTick, watchEffect } from "vue";
import defaultImage from "@/assets/icon.jpeg";
import { useUserStore } from "@/store";
import router from "@/router";
import { useRoute } from "vue-router";
import { log } from "debug/src/browser"
import {TOKEN_NAME} from "@/config/global"
const store = useUserStore();
const userInfo = ref({});
const isToken = ref(false);
const route = useRoute();

onMounted(() => {
  isToken.value = sessionStorage.getItem(TOKEN_NAME) ? true : false;
});

watchEffect(() => {
  userInfo.value = store.getUserInfo;
});

const goLogin = () => {
  router.push("/login");
};
// 默认头像
const onerrorImg = () => {
  userInfo.value.icon = defaultImage;
};
</script>
<style lang="scss" scoped>
header {
  position: fixed;
  top: 0;
  z-index: 998;
  padding-left: 226px;
  width: calc(100% - 226px);
  background-color: var(--color-white);
  text-align: left;
  padding: 18px 0;
  font-size: 14px;

  .wecom{
    margin: 4px 10px 0 0;
  }
  .headerInfo {
    padding-right: 40px;
    .line {
      padding: 0 5px;
    }
  }
  .headIcon {
    width: 30px;
    height: 30px;
    border-radius: 100%;
    margin-right: 13px;
    &:hover{
      // 透明度80%
      opacity: 0.8;
    }
  }
  .vline {
    margin: 0 16px;
    background: #b5abab;
    width: 1px;
    height: 17px;
  }
  .back {
    position: relative;
    cursor: pointer;
    img {
      position: relative;
      top: 2px;
      margin-left: 2px;
      width: 18px;
      height: 14px;
    }
  }
}
.fx-1{
  .marg-lt-20{
    display: flex;
    flex-direction: column-reverse;
    justify-content: space-around;
  }
}
.out{
  // 设置其hover状态的样式
  &:hover{
    // 设置其透明度为0.8
    opacity: 0.8;
  }
}
</style>
