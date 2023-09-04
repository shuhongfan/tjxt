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
                <span
                  :class="!item.isCheck ? 'textDefault' : 'textForbidden'"
                  class=""
                  @click.stop="handleAdd(item)"
                  >添加阶段考试</span
                >
              </div>
            </div>
          </template>
          <div class="itemCon topicCon" v-if="item.sections.length > 0">
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
                    <!-- 触发名字编辑 -->

                    <div>
                      <span>{{ val.name }}</span>
                    </div>
                    <div>
                      <span class="textDefault" @click="handleSetOpen(val)">{{
                        val.subjectNum > 0 ? "查看题目" : "设置题目"
                      }}</span>
                    </div>
                    <div>
                      {{ val.subjectNum }}
                    </div>
                    <div>
                      {{ val.totalScore }}
                    </div>
                    <div>
                      <span
                        class="textWarning"
                        v-if="val.type === 3"
                        @click="handleDeleteOpen(val)"
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
    <!-- 设置题目弹层 -->
    <SetTopic
      :dialogVisible="dialogVisible"
      :jectIds="jectIds.value"
      :score="score"
      @setTopicInfo="setTopicInfo"
      @handleClose="handleSetClose"
    ></SetTopic>
    <!-- end -->
    <!-- 删除弹层 -->
    <Delete
      :dialogDeleteVisible="dialogDeleteVisible"
      :deleteText="deleteText"
      @handleDelete="handleDelete"
      @handleClose="handleClose"
    ></Delete>
    <!-- end -->
  </div>
</template>
<script setup>
import { ref, reactive, onMounted, nextTick, watch, onScopeDispose } from "vue";
import { useRouter, useRoute } from "vue-router";
import { ElMessage } from "element-plus";
// 接口
import {
  getCoursesCatalogue,
  baseCatalogueSave,
  addTopic,
  baseSubjectSave,
  getSubjects,
} from "@/api/curriculum";
// 导入组件
// 新增按钮
import AddButton from "@/components/AddButton/index.vue";
// 删除弹层
import Delete from "@/components/Delete/index.vue";
// 编辑弹层
import SetTopic from "./SetTopic.vue";
// ------定义变量------
// 获取父组件值、方法
const props = defineProps({
  // 课程id
  courseId: {
    type: Number,
    default: 0,
  },
});
const router = useRouter(); //获取全局
const route = useRoute(); //获取局部
const emit = defineEmits(); //子组件获取父组件事件传值
const deleteText = ref("此操作将永久删除阶段测试，是否继续？"); //删除提示
let dialogVisible = ref(false); //弹层隐藏显示
let itemData = reactive([]); //目录数据
let activeNames = reactive(["1"]);
let dialogDeleteVisible = ref(false); //控制删除弹层
let chapterItem = reactive({}); //章内容
let knobData = reactive({}); //节内容
let topicIndex = ref(0); //添加阶段测试当前序号
let chapterId = ref(null); //当前触发的章id
let courseId = route.params.id;
let subjectData = ref([]); //保存小节或练习中的题目
let jectData = reactive([]); //小节的 题目
let jectIds = reactive([]); //  设置题目弹层的回显内容
let score = ref(null); //当前小节的分数
// ------生命周期------
onMounted(() => {
  getCatalogue();
  getSubject();
});
watch("itemData", (newValue, oldValue) => {
  itemData.value = newValue;
});
// ------定义方法------
// 获取小节或练习中的题目
const getSubject = async () => {
  let params = {
    id: courseId,
  };
  await getSubjects(params)
    .then((res) => {
      if (res.code === 200) {
        jectData.value = res.data;
        let arr = [];
        let subIds = [];
        let obj = {};
        // 分解数据
        jectData.value.map((value) => {
          let subIds = [];
          value.subjects.map((val) => {
            subIds.push(val.id);
          });
          value.subjectIds = subIds;
          // obj = {
          //   cataId: value.cataId,
          //   subjects: value.subjects,
          // };
          // arr.push(obj);
        });
      }
    })
    .catch((err) => {});
};

// 获取目录数据
const getCatalogue = async () => {
  let params = {
    id: courseId,
    see: false,
    withPractice: 1, //是否带着练习题，1：带着练习题，0：不带练习题，默认1
  };
  await getCoursesCatalogue(params)
    .then((res) => {
      if (res.code === 200) {
        itemData.value = res.data;
        // 遍历章节
        itemData.value.map((val, index) => {
          val.sections.map((obj, i) => {
            if (obj.type === 3) {
              val.isCheck = true;
            }
          });
        });
      }
    })
    .catch((err) => {});
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
          showClose:false,
        });
      }
    })
    .catch((err) => {});
};
//保存小节或者练习中的题目
const saveSubject = async () => {
  let arr = [];
  let arr2 = [];
  let obj = {};

  // 分解数据
  // 新数据
  // itemData.value.map((val) => {
  //   val.sections.map((ele) => {
  //     if (ele.subjectIds && ele.subjectIds.length > 0) {
  //       subjectData.value.push(ele);
  //     }
  //   });
  // });
  // console.log(subjectData.value);
  let params = {};
  if (subjectData.value && subjectData.value.length > 0) {
    subjectData.value.map((value,index) => {
      // if (value.subjectIds) {
      //   // obj.cataId = value.id;
      //   obj = {
      //     cataId: value.id,
      //     subjectIds: value.subjectIds,
      //   };
      //   arr.push(obj);
      // }

      // 服务器获取的数据
      if (jectData.value.length > 0) {
        jectData.value.map((val) => {
          delete val.subjects;
          if (value.cataId === val.cataId && value.subjectIds) {
            val.subjectIds = value.subjectIds;
            subjectData.value.splice(index,1)
            // obj = {
            //   cataId: value.id,
            //   subjectIds: value.subjectIds,
            // };
            // val = obj;
          }
          // console.log(val.cataId)
          // arr2.push(val);
        });
      }
      
    });
    subjectData.value = subjectData.value.concat(jectData.value);
    params = {
      datas: subjectData.value,
      id: courseId,
    };
  } else {
    if (jectData.value.length > 0) {
      jectData.value.map((val) => {
        delete val.subjects;
      });
      arr = jectData.value;
    }
    params = {
      datas: arr,
      id: courseId,
    };
  }
  await baseSubjectSave(params)
    .then((res) => {
      if (res.code === 200) {
        emit("getActive", 4);
        ElMessage({

          message: "恭喜你，操作成功！",
          type: "success",
          showClose:false,
        });
      } else {
        ElMessage({

          message: res.data.msg,
          type: "error",
          showClose:false,
        });
      }
    })
    .catch((err) => {});
};
// 删除章内容
const handleDelete = async () => {
  let knob = chapterItem.value; //要删除的小节
  // 删除阶段测试逻辑
  // 从服务器获取的数据没有修改的情况下删除
  if (jectData.value.length > 0) {
    jectData.value.map((val, index) => {
      if (val.cataId === knob.id) {
        jectData.value.splice(index, 1);
      }
    });
  }
  // 设置了小节的题目或者添加了阶段考试情况下删除
  if (subjectData.value.length > 0) {
    subjectData.value.map((val, index) => {
      if (val.cataId === knob.id) {
        subjectData.value.splice(index, 1);
      }
    });
  }

  itemData.value.map((val, index) => {
    val.sections.map((obj, i) => {
      if (obj.id === knob.id) {
        val.sections.splice(i, 1);
        val.isCheck = false;
      }
    });
  });
  // 关闭弹层
  handleClose();
};
// 删除改章，判断改章节是否有小节，如果有，显示删除弹层，如果没有，直接删除改章
const handleDeleteOpen = (item) => {
  chapterItem.value = item;
  dialogDeleteVisible.value = true;
};

// 添加阶段测试
const handleAdd = async (item) => {
  if (!item.isCheck) {
    await addTopic()
      .then((res) => {
        // topicIndex.value++;
        // let value = topicIndex.value;
        // // 如果是序号小于10在序号前+0
        // value = value > 9 ? value : "0" + value;
        itemData.value.map((val) => {
          if (val.id === item.id) {
            val.isCheck = true;
            let obj = {};
            obj = {
              id: res.data.id,
              name: "阶段考试",
              type: 3,
            };
            val.sections.push(obj);
          }
        });
      })
      .catch((err) => {});
  }
};
// 获取设置题目的ids
const setTopicInfo = (data) => {
  let arr = [];
  let obj = {};
  itemData.value.map((val) => {
    val.sections.map((ele) => {
      if (ele.id === chapterId.value) {
        obj = {
          cataId: ele.id,
          subjectIds: data.value,
        };
        arr.push(obj);
        ele.subjectIds = data.value;
        ele.subjectNum = data.value.length;
        ele.totalScore = data.totalScore;
        subjectData.value.push(obj);
        // arr.push(ele)
      }
    });
  });
  // subjectData.value = arr;
};
// 打开设置题目弹层
const handleSetOpen = (obj) => {
  jectIds.value = [];
  chapterId.value = obj.id;
  score.value = obj.totalScore; //分数
  dialogVisible.value = true;
  itemData.value.map((val) => {
    val.sections.map((ele) => {
      // 服务器获取的数据

      jectData.value.map((sub) => {
        if (ele.id === sub.cataId && ele.id === obj.id) {
          jectIds.value = sub.subjectIds;
        }
      });
      if (ele.id === obj.id && ele.subjectIds && ele.subjectIds !== undefined) {
        jectIds.value = ele.subjectIds;
        // subjectData.push(JSON.parse(JSON.stringify(ele)))
        // arr.push(ele)
      }
    });
  });
};
// 关闭设置题目弹层
const handleSetClose = () => {
  // jectIds.value = []
  dialogVisible.value = false;
};
// 关闭删除弹层
const handleClose = () => {
  dialogDeleteVisible.value = false;
};

// 向父组件暴露方法
defineExpose({
  handleSubmit,
});
</script>
