<!--课程老师-->
<template>
  <div class="contentBox">
    <!-- 新增 -->
    <div class="subHead">
      <AddButton @handleAdd="handleAdd" :text="text"></AddButton>
    </div>
    <!-- end -->
    <div class="courseTeachList">
      <div class="itemCon">
        <div class="headTitle">
          <span style="min-width: 80px">序号</span>
          <span style="min-width: 155px; margin-right: 20px">教师名称</span>
          <span style="min-width: 300px">教师介绍</span>
          <span
            style="padding-left: 15px; padding-right: 30px; min-width: 170px"
            >排序</span
          >
          <span style="min-width: 155px; padding-right: 35px"
            >用户端是否显示</span
          >
          <span style="min-width: 140px">操作</span>
        </div>
        <div class="item" v-if="selectData.value && selectData.value.length">
          <ul>
            <li v-for="(val, i) in selectData.value" :key="i">
              <div class="con">
                <!-- 序号 -->
                <div>
                  <span>{{ i + 1 }}</span>
                </div>
                <div style="padding-right: 0">
                  <div class="head">
                    <span @click="handleMagnify(val.photo)">
                      <img :src="val.photo" />
                      <span class="shade"><i></i></span>
                    </span>

                    {{ val.name }}
                  </div>
                </div>
                <div>
                  <div class="ellipsisHidden3">
                    <el-popover
                      popper-class="tearchPopover"
                      placement="bottom-start"
                      title=""
                      trigger="hover"
                      :content="val.introduce"
                      v-if="val.introduce && val.introduce.length > 50"
                    >
                      <template #reference>{{ val.introduce }}</template>
                    </el-popover>
                    <p v-else>{{ val.introduce }}</p>
                  </div>
                </div>
                <div class="sortIcon">
                  <span
                    :class="i === 0 ? 'upforbid' : 'up'"
                    @click="handleUp(val, i, 'up')"
                  ></span>
                  <span
                    :class="
                      i === selectData.value.length - 1 ? 'downforbid' : 'down'
                    "
                    @click="handleDown(selectData.value.length, i, val)"
                  ></span>
                </div>
                <div>
                  <span
                    ><span
                      class="iconTip"
                      :class="!val.isShow ? 'forbidIcon' : 'normalIcon'"
                    ></span
                    >{{ val.isShow === true ? "显示" : "隐藏" }}</span
                  >
                </div>
                <div>
                  <span class="textDefault" @click="handleIsShow(val)">{{
                    val.isShow === true ? "隐藏" : "显示"
                  }}</span>
                  <span class="textWarning" @click="handleDeleteOpen(val)"
                    >删除</span
                  >
                </div>
              </div>
            </li>
          </ul>
        </div>
        <div v-else>
          <EmptyPage :baseData="baseData.value"></EmptyPage>
        </div>
      </div>
    </div>

    <!-- 选择老师弹出层 -->
    <SelectAdd
      ref="selectAdd"
      :dialogVisible="dialogVisible"
      :baseData="baseData.value"
      :total="total"
      :loading="loading"
      :searchData="searchData"
      :selectData="selectData.value"
      @getValue="getValue"
      @getList="getList"
      @handleEdit="handleEdit"
      @handleSizeChange="handleSizeChange"
      @handleCurrentChange="handleCurrentChange"
      @handleClose="handleSelectClose"
      @handleSearch="handleSearch"
    ></SelectAdd>
    <!-- end -->
    <!-- 删除弹层 -->
    <Delete
      :dialogDeleteVisible="dialogDeleteVisible"
      :deleteText="deleteText"
      @handleDelete="handleDelete"
      @handleClose="handleClose"
    ></Delete>
    <!-- end -->
    <!-- 放大图片弹层 -->
    <ImageMagnify
      :dialogPicVisible="dialogPicVisible"
      :pic="pic"
      @handleMagnifyClose="handleMagnifyClose"
    ></ImageMagnify>
    <!-- end -->
  </div>
</template>
<script setup>
import { ref, reactive, onMounted, nextTick, watch } from "vue";
import { ElMessage } from "element-plus";
import { useRouter, useRoute } from "vue-router";
// 导入接口
import { getTeacherser } from "@/api/teacher";
import { baseTeachersSave, getTeachers } from "@/api/curriculum";
// 导入组件
// 新增按钮
import AddButton from "@/components/AddButton/index.vue";
// 删除弹出层
import Delete from "@/components/Delete/index.vue";
// 选择教师弹出层
import SelectAdd from "./add.vue";
// 图片放大弹层
import ImageMagnify from "@/components/ImageMagnify/index.vue";
// 空页面
import EmptyPage from "@/components/EmptyPage/index.vue";
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
let dialogDeleteVisible = ref(false); //控制删除弹层
let dialogVisible = ref(false); //选择老师弹层
let pic = ref(""); //要放大的图片
let teacherId = ref(null);
const selectAdd = ref();
const text = ref("选择教师");
const deleteText = ref("此操作将该老师移除本课程，是否继续？"); //删除提示
const loading = ref(false);
let searchData = reactive({
  pageSize: 10,
  pageNo: 1,
  // status: 1,
}); //搜索对象
let total = ref(null); //数据总条数
let baseData = reactive([]); //表格数据
let selectData = reactive([]); //已经选择的老师
let dialogPicVisible = ref(false); //控制放大图片弹层显示隐藏
let courseId = route.params.id; //课程id
// ------生命周期------
onMounted(() => {
  getList();
  getCourseTearchList();
});
// ------定义方法------
// 获取老师列表值
const getList = async () => {
  loading.value = true;
  await getTeacherser(searchData)
    .then((res) => {
      if (res.code === 200) {
        loading.value = false;
        baseData.value = res.data.list;
        total.value = res.data.total;
      }
    })
    .catch((err) => {});
};
// 获取课程老师列表getTeachers
const getCourseTearchList = async () => {
  loading.value = true;
  let params = {
    id: courseId,
    see: false,
  };
  await getTeachers(params)
    .then((res) => {
      if (res.code === 200) {
        loading.value = false;
        selectData.value = res.data;
      }
    })
    .catch((err) => {});
};
// 提交
const handleSubmit = async (str) => {
  let obj = {};
  let arr = [];
  let isConceal = 0; //隐藏的个数
  let isShow = 0; //显示的各个
  if (selectData.value && selectData.value.length > 0) {
    selectData.value.map((val, i) => {
      // 用来判断已选老师是否被隐藏了
      if (val.isShow) {
        isShow++;
      }
      obj = {
        id: val.id,
        isShow: val.isShow,
      };
      arr.push(obj);
    });
    let params = {
      teachers: arr,
      id: courseId,
    };
    if (isShow > 0) {
      await baseTeachersSave(params)
        .then((res) => {
          if (res.code === 200) {
            ElMessage({

              message: "恭喜你，操作成功！",
              type: "success",
              showClose: false,
            });
            router.push({
              path: "/curriculum/index",
            });
          } else {
            ElMessage({

              message: res.data.msg,
              type: "error",
              showClose: false,
            });
          }
        })
        .catch((err) => {});
    } else {
      ElMessage({

        message: "请至少选择一名在用户端展示的教师",
        type: "error",
        showClose: false,
      });
    }
  } else {
    ElMessage({

      message: "请至少设置一名教师",
      type: "error",
      showClose: false,
    });
  }
};
// 打开选择老师弹层
const handleAdd = () => {
  dialogVisible.value = true;
};
// 关闭老师弹层
const handleSelectClose = () => {
  dialogVisible.value = false;
};
// 用户端是否显示隐藏
const handleIsShow = (val) => {
  if (val.isShow) {
    val.isShow = false;
  } else {
    val.isShow = true;
  }
};
// 删除
const handleDelete = async () => {
  selectData.value.map((val, i) => {
    if (val.id === teacherId.value) {
      selectData.value.splice(i, 1);
      handleClose();
    }
  });
};
// 打开删除弹层
const handleDeleteOpen = (item) => {
  teacherId.value = item.id;
  dialogDeleteVisible.value = true;
};
// 关闭删除弹层
const handleClose = (item) => {
  dialogDeleteVisible.value = false;
};
//打开放大图弹层
const handleMagnify = (val) => {
  dialogPicVisible.value = true;
  pic.value = val;
};
// 关闭放大图弹层
const handleMagnifyClose = () => {
  dialogPicVisible.value = false;
  pic.value = "";
};
// 获取已经选择的教师
const getValue = (val) => {
  val.map((obj) => {
    // if (obj.status === 0) {
    //   obj.isShow = false;
    // } else {

    // }
    obj.isShow = true;
    if (selectData.value.length > 0) {
      selectData.value.forEach((element) => {
        if (element.id === obj.id) {
          obj.isShow=element.isShow
        }
      });
    }
  });
  selectData.value = val;
};
// 搜索
const handleSearch = () => {
  getList();
};
// 设置每页条数
const handleSizeChange = (val) => {
  searchData.pageSize = val;
  // 刷新列表
  getList();
};
// 当前页
const handleCurrentChange = (val) => {
  searchData.pageNo = val;
  // 刷新列表
  getList();
};
// 排序向上
const handleUp = (val, index, str) => {
  if (index === 0) {
    return false;
  } else {
    // selectData.value.forEach((obj) => {
    // if (obj.id === val.id) {
    if (index != 0) {
      selectData.value[index] = selectData.value.splice(
        index - 1,
        1,
        selectData.value[index]
      )[0];
    } else {
      selectData.value.push(selectData.value.shift());
    }
    // }
    // });
  }
};
// 排序向下
const handleDown = (length, index, val) => {
  if (index === length - 1) {
    return false;
  } else {
    if (index !== length - 1) {
      selectData.value[index] = selectData.value.splice(
        index + 1,
        1,
        selectData.value[index]
      )[0];
    } else {
      selectData.value.unshift(selectData.value.splice(index, 1)[0]);
    }
  }
};
// 向父组件暴露方法
defineExpose({
  handleSubmit,
});
</script>
<style lang="scss" scoped>
.headTitle {
  // min-width: 1170px;
}
.item {
  // min-width: 1170px;
}
.sortIcon {
  padding-left: 25px !important;
  padding-right: 15px !important;
}
.head {
  min-width: 155px;
}
.courseTeachList .itemCon .item li .con > div:nth-child(6) {
  min-width: 150px;
}
.courseTeachList .itemCon .item li .con > div:nth-child(5) {
  min-width: 155px;
  padding-right: 30px;
}
.courseTeachList .itemCon .item li .con > div:nth-child(4) {
  min-width: 155px;
}
.courseTeachList .itemCon .item li .con > div:nth-child(3) {
  min-width: 300px;
}
.courseTeachList .itemCon .item li .con > div:nth-child(2) {
  min-width: 155px;
  margin-right: 20px;
}
.courseTeachList .itemCon .item li .con > div:nth-child(1) {
  min-width: 80px;
}
.courseTeachList .itemCon .headTitle > span:nth-child(3),
.courseTeachList .itemCon .headTitle > div:nth-child(3),
.courseTeachList .itemCon .item li .con > span:nth-child(3),
.courseTeachList .itemCon .item li .con > div:nth-child(3) {
  width: 300px;
}
.ellipsisHidden30 {
  min-width: 250px;
}
</style>
<style lang="scss" scoped>
:deep(.el-popper.is-light) {
  background: #ffffff;
  box-shadow: 0 0 8px 1px rgba(0, 0, 0, 0.05);
  font-family: PingFangSC-Regular;
  font-weight: 400;
  font-size: 12px;
  color: #332929;
}
</style>