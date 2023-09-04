<!-- 架构页面 - 左侧导航 -->
<template>
  <div class="LeftSider fx-fd-col">
    <div  @click="() => $router.push('/')" class="logo cursor fx-ct"><img src="@/assets/newlogo.png" alt="" srcset="" /></div>
    <div class="nav">
      <el-menu
        :default-active="activeIndex"
        :default-openeds="defaultOpeneds"
        class="el-menu-vertical-demo"
        :unique-opened="true"
        @open="handleOpen"
        @close="handleClose"
        @select="handleSelect"
      > 
        <div class="first-menu">
          <el-menu-item index="99" :key="99" @click="goPath(`/`)">
            <i class="iconfont" v-html="basePath[0].meta.icon"></i>
            <span>工作台</span>
          </el-menu-item>
        </div> 
        <el-sub-menu v-for="(item, index) in basePath"  :key="index"  :index="index.toString()" >
          <template #title>
            <i class="iconfont" v-html="item.meta.icon"></i>
            <span>{{item.meta.title}}</span>
          </template>
          <el-menu-item v-for="(it, ind) in item.children" :key="ind" :index="`${index}-${ind}`" @click="goPath(`${it.path}`)">
            {{it.meta.title}}
          </el-menu-item>
        </el-sub-menu>
      </el-menu>
    </div>
    <span class="decorate"></span>
  </div>
</template>
<script setup>
import { ref, computed, watchEffect, } from 'vue';
import { useRoute } from 'vue-router';
import router, { asyncRouterList } from '@/router';
import { catchDataesStore,useUserStore } from '@/store';
// 全部路由信息
const routers = asyncRouterList

// 当前路由下的信息
const route = useRoute()

const store = catchDataesStore()
const useStore =useUserStore()

const activeIndex = ref('99')// ref(store.getDefaultIndex)
const defaultOpeneds = ref()// ref(store.getDefaultOpeneds)

// 处理侧边栏数据
const sideMenu = computed(() => {
  const newMenuRouters = [];
  routers.forEach((menu) => {
  })
  return newMenuRouters;
});

// 处理导航数据
const getMenuList = (list, basePath) => {
  if (!list) {
    return [];
  }
  return list
    .map((item) => {
      const path = basePath ? `${basePath}/${item.path}` : item.path;
      return {
        path,
        title: item.meta?.title,
        icon: item.meta?.icon || '',
        children: getMenuList(item.children, path),
        meta: item.meta,
        redirect: item.redirect,
      };
    })
    .filter((item) => item.meta && item.meta.hidden !== true);
};
// 展示基础路由
const basePath = getMenuList(routers)
// 进入导航
const goPath = (path) => {
  useStore.setTabNumber(0)
  router.push(path)
}
const handleOpen = (key) => {
  store.setDefaultOpeneds(key)
}
const handleClose = (key) => {
  store.setDefaultOpeneds(key)
}
const handleSelect = (key) => {
  store.setDefaultIndex(key)
}
// 处理  页面点击 菜单跟着动
watchEffect(()=>{
  if (basePath){
    const path = route.path.toString()
    // 如果是首页
    if(path == '/main/index' || path == '/'){
        activeIndex.value = '99'
        return 
      }
    // 如果是 三级子页  
    if(route.meta && route.meta.fmeta){
      const cpath = route.meta.fmeta.path
      basePath.forEach((item,index) => {
        const regA = new RegExp(item.path)
        // 非首页 在当前的路由下查找 
        if (cpath.search(regA) != -1){
          defaultOpeneds.value == [index.toString()] ? null : defaultOpeneds.value = [index.toString()]
          item.children.forEach((val, ind) => {
            if (val.path == cpath){
              activeIndex.value == `${index}-${ind}` ? null : activeIndex.value = `${index}-${ind}`
            } 
          })
        }
      })
      return ;
    }
    // 非首页的二级页面
    basePath.forEach((item,index) => {
      const regA = new RegExp(item.path)
      // 非首页 在当前的路由下查找 
      if (path.search(regA) != -1){
        defaultOpeneds.value == [index.toString()] ? null : defaultOpeneds.value = [index.toString()]
        item.children.forEach((val, ind) => {
          if (val.path == route.path){
            activeIndex.value == `${index}-${ind}` ? null : activeIndex.value = `${index}-${ind}`
          } 
        })
      }
    })
  }
})
</script>
<style lang="scss" scoped>
.LeftSider {
  position: relative;
  position: fixed;
  overflow: hidden;
  z-index: 999;
  width: 226px;
  height: 100vh;
  background-image: linear-gradient(130deg, #FAF8F4 0%, #F4EEE6 53%);
  &::before, &::after{
    position: absolute;
    content: '';
    // 设置背景图
    background-image: url(@/assets/leftbackground.png);
    // 设置背景图大小
    background-size: 183px 168px;
    z-index: 0;
    width: 183px;
    height: 168px;
  }
   &::after{
    // left: auto;
    // top: -140px;
    // right: -160px;
  }
  .decorate{
    top: 50vh;
    position: absolute;
    display: inline-block;
    width: 63px;
    height: 195px;
    opacity: 0.29;
    background: #FF6B3D;
    border-radius: 97px;
    filter: blur(50px);
  }
  .logo{
    position: relative;
    z-index: 9;
    margin-top: 30px;
    img{
      width: 98px;
      height: 88px;
    }
    margin-bottom: 47px;
  }
  .title{
    position: relative;
    z-index: 9;
    text-align: center;
    line-height: 60px;
    font-size: 18px;
    margin-bottom: 15px;
    color: #000000;
  }
  .nav{
    position: relative;
    z-index: 9;
    font-size: 14px;
    margin-right: 20px;
    .navIcon{
      margin-right: 16px;
    }
    .iconfont{
      font-size: 20px;
      margin-right: 10px;
    }
    .item{
      padding-left: 46px;
      padding-right: 43px;
      .navTopTit{
        display: flex;
        justify-content: space-between;
        padding: 10px 0;
        cursor: pointer;
        .vanIcon{
          width: 16px;
          height: 16px;
        }
      }
      .navListTit{
        // padding-left: 20px;
      }
    }
  }
  #svg{
    fill:red;
  }
  .first-menu{
    height: 46px;
    line-height: 46px;
    .el-menu-item{
      padding-left: 46px !important;
      height: 46px;
      line-height: 46px;
      border-radius: 0 100px 100px 0;
      &:hover{
        background-color: #765A58;
      }
    }
    .is-active{
      color:#fff;
      background: #765A58;
      border-radius: 0 100px 100px 0;
      height: 46px;
      &:hover{
        color: #E4DEDE;
      }
    }
  }
  :deep(.el-sub-menu__title){
    padding-left: 46px !important;
    border-radius: 0 100px 100px 0;
  }
  :deep(.el-sub-menu__icon-arrow){
    font-size: 15px;
    // color: #765A58;
  }
  :deep(.is-active .el-sub-menu__icon-arrow){
    font-size: 15px;
    // color: #fff;
    font-weight: 600;
  }
  :deep(.is-active > .el-sub-menu__title){
    color:#fff;
    background: #765A58;
    border-radius: 0 100px 100px 0;
    height: 46px;
  }
  :deep(.is-active > .el-sub-menu__title:hover){
    background: #765A58;
    border-radius: 0 100px 100px 0;
    color:#E4DEDE;
  }
  :deep(.el-menu-item:hover, .el-sub-menu__title:hover){
    background: transparent;
    color:#FF6B3D;
  }
  :deep(#svg){
    fill:red;
  }
  :deep(.el-sub-menu__title:hover){
    background: transparent;
    color:#FF6B3D;
  }
  :deep(.el-menu){
    background-color: transparent;
    border: none;
  }
  :deep(.el-menu-item){
    padding-left: 77px !important;
  }
}
</style>