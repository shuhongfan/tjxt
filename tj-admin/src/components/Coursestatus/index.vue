<template>
  <div class="deleteDialog">
    <el-dialog
      v-model="dialogStatusVisible"
      :title="title"
      :show-close="false"
    >
      <div v-if="title==='确认上架'">
        <p>请确认是否立即上架？</p>
        <p>上架后课程将在用户端进行展示，上架后若修改课程信息，需将课程先进行下架</p>
      </div>
      <p v-else>{{statusText}}</p>

      <div class="statusList" v-if="title==='确认下架'">
         <ul>
           <li><label>课程名称：</label>{{courseData.name}}</li>
           <li><label>报名人数：</label>{{courseData.sold}}</li>
           <li><label>下架时间：</label>{{courseData.purchaseEndTime}}</li>
         </ul>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button class="button buttonPrimary" @click="handleClose"
            >再想想</el-button
          >
          <el-button class="button primary" @click="handleSubmit"
            >确认</el-button
          >
        </span>
      </template>
    </el-dialog>
  </div>
</template>
<script setup>
import { ref, reactive, nextTick, watch,onMounted } from "vue";
// 引入接口
import {getCoursesDetail} from "@/api/curriculum"
// 获取父组件值、方法
const props = defineProps({
  // 弹层隐藏显示
  dialogStatusVisible: {
    type: Boolean,
    default: false,
  },
  statusText:{
    type: String,
    default: '',
  },
  title:{
    type: String,
    default: '',
  },
  courseData:{
    type: Object,
    default: () => ({}),
  },
  coursesesId:{
    type: String,
    default: '',
  }
});
// ------定义变量------
const emit = defineEmits(); //子组件获取父组件事件传值
// ------定义方法------
//关闭弹层
const handleClose = () => {
  emit("handleCloseDescend");
};
//提交确定删除
const handleSubmit = () => {
  emit("handleDescend");
};
</script>
