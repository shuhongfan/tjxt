<!--课程题目-->
<template>
  <div class="contentBox">
    <div class="courseList">
      <el-collapse accordion v-model="activeNames">
        <el-collapse-item v-for="(item, index) in itemData.value" :key="index">
          <template v-slot:title>
            <div class="titText">
              <span class="icon" v-if="item.sections.length > 0"></span>
              <div class="textL">
                <span
                  ><span v-if="index + 1 > 9">{{ index + 1 }}</span
                  ><span v-else>{{ "0" + (index + 1) }}</span></span
                >
                <span>{{ item.name }}</span>
              </div>
              <div class="textR">
                <span class="textForbidden">添加阶段考试</span>
              </div>
            </div>
          </template>
          <div class="itemCon" v-if="item.sections.length > 0">
            <div class="headTitle">
              <span>序号</span>
              <span>小节名称</span>
              <span>题目</span>
              <span>题目数目</span>
              <span>题目分数</span>
              <span>操作</span>
            </div>
            <div class="item">
              <ul>
                <li v-for="(val, i) in item.sections" :key="i">
                  <div class="leftLine"></div>
                  <div class="con">
                    <!-- 序号 -->
                    <div>
                      <div v-if="val.type !== 3">
                        <span v-if="i + 1 > 9">{{ i + 1 }}</span
                        ><span v-else>{{ "0" + (i + 1) }}</span>
                      </div>
                    </div>
                    <div>
                      <span>{{ val.name }}</span>
                    </div>
                    <div>
                      <span
                        @click="handleWatch(val)"
                        :class="
                          val.subjectNum > 0 ? 'textDefault' : 'textForbidden'
                        "
                      >
                        查看题目</span
                      >
                    </div>
                    <div>
                      {{ val.subjectNum }}
                    </div>
                    <div>
                      {{ val.totalScore }}
                    </div>
                    <div>
                      <span class="textForbidden" v-if="val.type === 3"
                        >删除阶段考试</span
                      >
                    </div>
                  </div>
                </li>
              </ul>
            </div>
            <div class="cover"></div>
          </div>
        </el-collapse-item>
      </el-collapse>
    </div>
    <!-- 查看题目弹层 -->
    <detailwatchTopic
      :dialogVisible="dialogVisible"
      :jectIds="jectIds.value"
      :score="score"
      @detailwatchTopicInfo="detailwatchTopicInfo"
      @handleClose="handleWatchClose"
    ></detailwatchTopic>
    <!-- end -->
    <!-- 删除弹层 -->
    <!-- end -->
  </div>
</template>
<script setup>
import { ref, reactive, onMounted, nextTick, watch } from "vue";
import { useRouter, useRoute } from "vue-router";
import { ElMessage } from "element-plus";
// 接口
import { baseCatalogueSave } from "@/api/curriculum";
// 导入组件
// 查看题目弹层
import detailwatchTopic from "./detailwatchTopic.vue";
// ------定义变量------
// 获取父组件值、方法
const props = defineProps({
  courseTopicData: {
    type: Object,
    default: {},
  },
});
const router = useRouter(); //获取全局
const route = useRoute(); //获取局部
let dialogVisible = ref(false); //弹层隐藏显示
let itemData = reactive([]); //目录数据
let activeNames = reactive(["1"]);
let chapterId = ref(null); //当前触发的章id
let courseId = route.params.id;
let jectIds = reactive([]); //  设置题目弹层的回显内容
let score = ref(null); //当前小节的分数

watch("itemData", (newValue, oldValue) => {
  itemData.value = newValue;
});
watch(props, () => {
  getCatalogue();
  // console.log(props.courseTopicData);
});
// ------定义方法------
// 获取目录数据
const getCatalogue = async () => {
  itemData.value = props.courseTopicData;
  // 遍历章节
  itemData.value.map((val, index) => {
    val.sections.map((obj, i) => {
      if (obj.type === 3) {
        val.isCheck = true;
      }
    });
  });
};

// 提交
const handleSubmit = async (str) => {
  let params = {
    datas: itemData.value,
    id: courseId,
    step: 4,
  };
  await baseCatalogueSave(params)
    .then((res) => {
      if (res.code === 200) {
        saveSubject(); //保存小节或者练习中的题目
        if (str === "getback") {
          router.push({
            path: "/curriculum/index",
          });
        } else {
          router.push({
            path: `/curriculum/add/` + courseId,
          });
        }
      } else {
        ElMessage({

          message: res.data.msg,
          type: "error",
          showClose: false,
        });
      }
    })
    .catch((err) => {});
};

// 获取设置题目的ids
const detailwatchTopicInfo = (data) => {
  itemData.value.map((val) => {
    val.sections.map((ele) => {
      if (ele.id === chapterId.value) {
        ele.subjectIds = data.value;
        ele.subjectNum = data.value.length;
        ele.totalScore = data.totalScore;
        // subjectData.push(JSON.parse(JSON.stringify(ele)))
        // arr.push(ele)
      }
    });
  });
  // subjectData.value = arr;
  // console.log(itemData.value);
};
// 打开查看题目弹层
const handleWatch = (obj) => {
  // console.log(obj, "obj")
  if (obj.subjectNum > 0) {
    jectIds.value = [];
    chapterId.value = obj.id;
    score.value = obj.totalScore; //分数
    dialogVisible.value = true;
    itemData.value.map((val) => {
      val.sections.map((ele) => {
        if (ele.id === chapterId.value) {
          jectIds.value = ele.id;
        }
      });
    });
  }
};
// 关闭设置题目弹层
const handleWatchClose = () => {
  // jectIds.value = []
  dialogVisible.value = false;
};

// 向父组件暴露方法
defineExpose({
  handleSubmit,
});
</script>
<style lang="scss" scoped>
.courseList .titText .textL span {
  font-size: 16px;
  color: #332929;
}
.headTitle {
  color: #332929;
}
.textR {
  font-size: 14px;
  font-family: PingFangSC-Regular;
  font-weight: 400;
}
</style>
