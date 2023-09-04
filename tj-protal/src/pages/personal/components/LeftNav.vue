<!-- 个人中心 - 左侧导航  -->
<template>
  <div class="LeftNav">
    <router-link class="fx-sb font-bt2" :class="{'active': activeClass(item)}" v-for="item in personalRoute" :to="item.path">
      {{item.meta.title}}<i class="iconfont" v-html="item.meta.icon"></i>
    </router-link>
    <a  class="fx-sb font-bt2" @click="logout">退出<i class="iconfont">&#xe60c;</i></a>
  </div>
</template>
<script setup>
import { onMounted, ref, watchEffect, computed } from 'vue'
import {useRoute, useRouter} from 'vue-router'
import {userLogout} from "@/api/user"
import { useUserStore } from '@/store'
const store = useUserStore();
const route = useRoute()
const router = useRouter()
// 使用路由
const personalRoute = ref([])
const currentPath = ref('')

const activeClass = computed(() => item => {
  const reg = new RegExp("^"+item.meta.active)
  return reg.test(currentPath.value)
})

onMounted(() => {
  const currentRoute = router.currentRoute.value
  // 获取个人中心的子路由作为导航
  personalRoute.value = currentRoute.matched[1].children.filter(n => n.meta && !n.meta.hidden)
  // 获取当前的路由作为默认项
  currentPath.value = route.path.split('/').at(-1);
})
// 通过监听实现 更新当前页面状态
watchEffect(() => {
  currentPath.value = route.path.split('/').at(-1);
})

// 退出登录
const logout = () => {
  userLogout().then(res => {
    if (res.code === 200) {
      store.logout();
      location.href = "/"
    }
  }).catch(err => console.log(err))
}

</script>
<style lang="scss" scoped>
.LeftNav{
  width: 190px;
  height:fit-content;
  background: #FFFFFF;
  border-radius: 8px;
  margin-right: 20px;
  a{
    width: 100%;
    padding: 13px 15px 13px 20px;
    border-bottom: 1px solid #EEEEEE;
    font-size: 14px;
    line-height: 32px;
    i{
      font-size: 30px;
    }
  }
  .active{
    color: var(--color-main);
  }
}
</style>
