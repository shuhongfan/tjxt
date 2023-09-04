<!-- 主页面 -->
<template>
  <el-config-provider :locale="locale">
    <router-view></router-view>
  </el-config-provider>
</template>

<script setup>
import { defineComponent } from "vue";
import zhCn from "element-plus/lib/locale/lang/zh-cn";
let locale = zhCn;
import { computed, onMounted } from 'vue';
// import config from '@/config/style';
// import { useSettingStore } from '@/store';

// const store = useSettingStore();

// const mode = computed(() => {
//   console.log(33)
//   return store.displayMode;
// });


import { getTypeAll } from "@/api/api"
// 缓存
import { catchDataesStore } from '@/store';
let store = catchDataesStore();
onMounted(() => {
  if(!store.getCategoryTree){
     getTypeAll({admin:true})
        .then((res) => {
          if (res.code === 200) {
            store.setCategoryTree(res.data);
            let map = {};
            res.data.forEach(f => {
              map[f.id] = {id:f.id, name:f.name}
              f.children.forEach(s => {
                map[s.id] = {id:s.id, name:s.name, parentId: s.parentId}
                s.children.forEach(t => map[t.id] = {id:t.id, name:t.name, parentId:t.parentId})
              })
            })
            store.setCategoryMap(map);
          }
        })
        .catch((err) => console.log(err))
  }
});
</script>
