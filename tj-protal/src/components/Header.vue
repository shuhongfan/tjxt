<!-- 页面头部组件 -->
<template>
  <header class="bg-wt">
    <div class="container fx">
      <div class="logo">
        <router-link to="/"><img src="@/assets/logo.png" alt="" srcset=""/></router-link>
      </div>
      <!-- 头部分类-start -->
      <div v-if="route.path != '/main/index' && route.path != '/login' " class="courseClass font-bt2"
           @mouseover="() => isShow = true" @mouseout="() => isShow = false">
        <i class="iconfont zhy-icon_fenlei_nor"></i> 分类
      </div>
      <div v-if="route.path != '/main/index' && route.path != '/login' " class="courseClassList" v-show="isShow"
           @mouseover="() => isShow = true" @mouseout="() => isShow = false">
        <div class="firstItems">
          <ClassCategory :data="courseClass" type="float"></ClassCategory>
        </div>
      </div>
      <!-- 头部分类-end -->
      <div class="fx-1 fx-ct">
        <el-input
            v-model="input"
            class="headerSearch "
            size="large"
            placeholder="请输入关键字"
            @keyup.enter="SearchHandle"

        >
          <template v-slot:prefix>
            <Search class="search" @click="SearchHandle"/>
          </template>
        </el-input>
      </div>
      <div class="fx-al-ct pt-rt">
        <div class="car fx-al-ct font-bt2" v-if="userInfo" @click="() => $router.push('/pay/carts')">
          <i class="iconfont">&#xe6f3;</i> 购物车
        </div>
        <!-- 学习中心 - start -->
        <div v-if="userInfo  && userInfo.name">
          <span class="marg-lr-40 font-bt2" style="padding:27px 0"
                @click="() => {$router.push('/personal/main/myClass')}" @mouseover="()=> learningShow = true"
                @mouseout="() => learningShow = false">学习中心</span>
          <div  class="learningCont" v-show="learningShow && learnClassInfo && learnClassInfo.courseAmount"
               @mouseover="()=> learningShow = true" @mouseout="() => learningShow = false">
            <div class="count"><em>{{ learnClassInfo && learnClassInfo.courseAmount }}</em> 门课程</div>
            <div class="info" v-if="learnClassInfo &&learnClassInfo.courseId">
              <div class="fx-sb">
                <span>正在学习：</span>
                <div class="fx">
                  <span class="bt"
                        @click="() => $router.push({path: '/learning/index', query: {id: learnClassInfo.courseId}})">继续学习</span>
                  <span class="bt bt-grey1" @click="() => $router.push('/personal/main/myClass')">全部课程</span>
                </div>
              </div>
              <div class="tit">{{ learnClassInfo && learnClassInfo.courseName }}</div>
              <div class="perc fx-sb"> {{ learnClassInfo && learnClassInfo.latestSectionName }}
                <i>{{ learnClassInfo && (Math.round(learnClassInfo.learnedSections * 100 / learnClassInfo.sections)) }}%</i>
              </div>
            </div>
          </div>
        </div>
        <!-- 登录注册 - start -->
        <div class="fx-al-ct" v-if="userInfo && userInfo.name">
          <img class="headIcon" :src="userInfo.icon" :onerror="onerrorImg" alt="">
          <div>{{ userInfo.name }}</div>
          <!-- <div class="font-bt2 pd-lf-10" @click="() => $router.push('/login')"> 退出 </div> -->
        </div>
        <div class="cur-pt" v-else>
          <span class="font-bt2" @click="() => $router.push({path: '/login', query: {md: 'register'}})">注册 </span><span>/</span>
          <span class="font-bt2" @click="() => $router.push('/login')"> 登录</span>
        </div>

      </div>
    </div>
  </header>
</template>
<script setup>
import defaultImage from '@/assets/icon.jpeg'
import {onBeforeMount, onMounted, ref, watchEffect} from "vue";
import {Search} from "@element-plus/icons-vue";
import {useUserStore, isLogin, getToken, dataCacheStore} from '@/store'
import {getUserInfo} from "@/api/user"
import router from "../router";
import {useRoute} from "vue-router";
import {ElMessage} from "element-plus";
import ClassCategory from "./ClassCategory.vue";
import {getMyLearning, getClassCategorys} from '@/api/class.js'
import {tryRefreshToken} from '../utils/refreshToken'

const store = useUserStore();
const userInfo = ref()
const isToken = sessionStorage.getItem('token') ? true : false
const input = ref('');
const route = useRoute()
const userStore = getToken();
const dataCache = dataCacheStore();
const courseClass = ref([]) // 分类数据
const isShow = ref(false)  // 分类展示
const learnClassInfo = ref(null) // 我真正学习的课程信息-学习中心展示
const learningShow = ref(false) // 学习中心hover模块展示

onBeforeMount(async () => {
  // 尝试获取用户信息
  const ui = store.getUserInfo;
  if (!ui) {
    if (await isLogin()) {
      let res = await getUserInfo();
      if (res.code === 200 && !!res.data) {
        userInfo.value = res.data
        // 记录到store 并调转到首页
        store.setUserInfo(res.data);
      }
    } else {
      userStore.logout();
    }
  } else {
    userInfo.value = ui
  }

  courseClass.value = dataCache.getCourseClassDataes
  // 先从store里拿如何没有就请求分类信息获取
  if (courseClass.value.length === 0) {
    getCourseClassHandle()
  }
  if (Object.keys(route.query).length > 0) {
    input.value = route.query.key
  }
  if (await isLogin()) {
    // 查询我正在学习的课程
    getLearnClassInfoHandle()
  }
})
// 监听路由 清空搜索框的值
watchEffect(() => {
  if (route.path !== '/search/index') {
    input.value = ''
  } else {
    input.value = dataCache.getSearchKey
  }
})
// 学习中心的信息

// 查询我正在学习的课程
const getLearnClassInfoHandle = async () => {
  await getMyLearning()
      .then((res) => {
        if (res.code === 200) {
          learnClassInfo.value = res.data;
        } else {
          ElMessage({
            message: res.data.msg,
            type: "error",
          });
        }
      })
      .catch(() => {
        ElMessage({
          message: "学习状态查询出错！",
          type: "error",
        });
      });
}


// 获取课程分类
const getCourseClassHandle = async () => {
  await getClassCategorys()
      .then((res) => {
        if (res.code == 200) {
          courseClass.value = res.data;
          dataCache.setCourseClassDataes(res.data)
        } else {
          ElMessage({
            message: res.data.msg,
            type: "error",
          });
        }
      })
      .catch(() => {
        ElMessage({
          message: "分类请求出错！",
          type: "error",
        });
      });
}
// 默认头像
const onerrorImg = () => {
  userInfo.value.icon = defaultImage;
}
// 搜索事件
const SearchHandle = () => {
  if (input.value == '') {
    ElMessage({
      type: 'error',
      message: '请输入搜索关键词！'
    })
    return false
  }
  dataCache.setSearchKey(input.value)
  router.push({path: '/search', query: {"key": input.value}})
}

</script>
<style lang="scss" scoped>
header {
  width: 100%;
  background-color: var(--color-white);
  text-align: left;
  padding: 11px 0;
  font-size: 14px;

  .courseClass {
    position: relative;
    line-height: 48px;
    margin-left: 26px;
    display: flex;
    font-size: 14px;

    .iconfont {
      font-size: 24px;
      margin-right: 5px;
    }
  }

  .courseClassList {
    position: absolute;
    z-index: 999;
    top: 50px;
    left: 102px;

    .firstItems {
      background-color: #fff;
    }
  }

  .headerSearch {
    width: 427px;
    height: 40px;
    background: #edf0f4;
    border-radius: 8px;

    :deep(.el-input__wrapper) {
      background-color: transparent;
    }

    .search {
      position: absolute;
      cursor: pointer;
      right: 0;
      width: 15px;
      height: 15px;

    }
  }

  .car {
    img {
      width: 24px;
      height: 25px;

    }

    .iconfont {
      font-size: 24px;
      margin-right: 6px;
    }
  }

  .learningCont {
    position: absolute;
    z-index: 999;
    width: 330px;
    height: 200px;
    border-radius: 8px;
    left: -40px;
    top: 45px;
    background-color: #fff;
    box-shadow: 0 4px 6px 2px rgba(108, 112, 118, 0.17);

    &::before {
      position: absolute;
      z-index: -1;
      content: '';
      top: -5px;
      left: 43%;
      display: inline-block;
      width: 15px;
      height: 15px;
      background-color: #fff;
      transform: rotate(45deg);
      box-shadow: 0 4px 6px 2px rgba(108, 112, 118, 0.17);
    }

    .count {
      background-color: #fff;
      padding: 20px 20px 20px 20px;
      line-height: 40px;
      border-bottom: 1px solid #EEEEEE;
      display: flex;

      em {
        color: var(--color-main);
        font-family: PingFangSC-S0pxibold;
        font-weight: 600;
        font-size: 28px;
        margin-right: 4px;
        font-style: normal;
      }
    }

    .info {
      padding: 13px 20px 20px 20px;
      line-height: 28px;
      color: #80878C;

      .bt {
        line-height: 28px;
        height: 28px;
        font-size: 12px;
        padding: 0 10px;
        margin-left: 10px;
      }

      .tit {
        font-weight: 600;
        font-size: 14px;
        line-height: 28px;
        color: #19232B;
      }

      .perc {
        i {
          font-style: normal;
          color: #80878C;
        }
      }
    }
  }

  .headIcon {
    width: 30px;
    height: 30px;
    border-radius: 100%;
    margin-right: 10px;
  }
}
</style>
