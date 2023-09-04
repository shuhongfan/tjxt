<!-- 首页-兴趣模块组件 -->
<template>
<div class="Interest" >
  <div class="head fx-sb">
    <TableSwitchBar :data="[...data]" @changeTable="checkHandle"></TableSwitchBar>
    <div class="ft-14 font-bt">
      <span @click="changeHandle">修改兴趣</span>
    </div>
  </div>
  <div class="classInfo fx" v-if="teacherInfo">
    <div class="teacherInfo fx-1" :style="{'background-image': `url(${teacherInfo.coverUrl})`}">
      <div class="info fx-sb">
        <div class="fx-al-ct fx-1 ">
          <img :src="teacherInfo.icon || '/img-tx/1.jpg'" alt="" srcset="">
          讲师：<span>
          {{teacherInfo.teacher}}
        </span>
        </div>
        <div class="" @click="$router.push({path:'/details',query:{id:teacherInfo.id}})">
          <span class="bt">立即报名</span>
        </div>
      </div>
    </div>
    <div class="classList fx-1 bg-wt">
      <div class="box">
        <div class="fx-sb item" v-for="(item, index) in classList" :key="index"
             @mouseover="teacherInfo = item"
             @click="$router.push({path:'/details',query:({id: item.id})})">
          <span class="title">{{item.name}}</span>
          <span class="desc">共{{item.sections}}节 <i>.</i> {{item.sold}}人正在学习 </span>
        </div>
      </div>
    </div>
  </div>
  <div class="classInfo " v-else>
    <div class="nodata bg-wt fx-ct">
      <span>该兴趣分类下无课程</span>
    </div>
  </div>
</div>
</template>
<script setup>
import { onMounted, ref } from "vue";
import { getClassList } from "@/api/class.js";
import { ElMessage } from "element-plus";
import TableSwitchBar from "@/components/TableSwitchBar.vue";
// 引入父级传参
const props = defineProps({
  data: {
    type: Set,
    default: [],
  },
});
const emit = defineEmits(["setInterest"]);

// 当前兴趣课程 - 默认展示选中
const actId = ref();
// 课程讲师信息
const teacherInfo = ref({});
const classList = ref({});

// 初始化首选项并获取其课程数据
onMounted(() => {
  const data = props.data[Symbol.iterator]();
  actId.value = data.next().value.id;
  // 通过二级分类id 获取对应课程列表
  getClassListData(actId.value);
});

// 点击切换效果
const checkHandle = (id) => {
  actId.value = id;
  getClassListData(id);
};

// 打开修改兴趣弹窗
const changeHandle = () => {
  emit("setInterest", true);
};
// 通过二级分类id 获取对应课程列表
const getClassListData = async (id) => {
  await getClassList(id)
    .then((res) => {
      if (res.code == 200) {
        classList.value = res.data;
        teacherInfo.value = res.data[0];
      } else {
        ElMessage({
          message: res.data.msg,
          type: "error",
        });
      }
    })
    .catch(() => {
      ElMessage({
        message: "课程列表数据获取失败",
        type: "error",
      });
    });
};
</script>
<style lang="scss" scoped>
.Interest {
  .head {
    .title {
      display: inline-block;
      margin-right: 20px;
      font-weight: 400;
      cursor: pointer;
    }

    .act {
      font-weight: 600;
      position: relative;

      &::before {
        position: absolute;
        content: "";
        width: 33px;
        height: 4px;
        border-radius: 4px;
        bottom: -8px;
        background: var(--color-main);
      }
    }
  }

  .classInfo {
    height: 331px;
    padding-top: 30px;
    margin-bottom: 40px;

    .teacherInfo {
      position: relative;
      background-size: cover;
      background-repeat: no-repeat;
      border-radius: 8px 0 0 8px;

      .info {
        background: rgba(0, 0, 0, 0.45);
        border-radius: 0 0 0 8px;
        padding: 20px;
        position: absolute;
        line-height: 30px;
        width: 100%;
        bottom: 0;
        color: #fff;

        img {
          width: 30px;
          height: 30px;
          border-radius: 100%;
          margin-right: 10px;
        }

        .bt {
          line-height: 32px;
          padding: 0 20px;
          font-size: 14px;
        }
      }
    }

    .classList {
      padding: 20px 21px 20px 18px;

      .box {
        height: 266px;
        overflow: hidden;
      }

      .item {
        padding: 8px 12px;
        border-radius: 4px;
        cursor: pointer;

        .desc {
          display: none;
          font-size: 12px;

          i {
            position: relative;
            font-size: 16px;
            top: -3px;
          }
        }

        &:hover {
          background-color: #f5f6f9;
          color: var(--color-main);
          font-weight: 600;

          .desc {
            display: block;
            color: var(--color-font3);
            font-weight: 400;
          }
        }
      }
    }

    .nodata {
      width: 100%;
      height: calc(100% - 0px);
      margin-top: 0px;
    }
  }
}
</style>
