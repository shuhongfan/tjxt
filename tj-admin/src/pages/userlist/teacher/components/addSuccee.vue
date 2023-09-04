<template>
  <div class="succeeBox">
    <el-dialog v-model="dialogSucceeVisible" :title="title">
      <div class="icon"><component :is="Result403Icon"></component></div>
      <div class="succeeCon">
        <p class="tit">恭喜您，<span>{{fromData.name}}</span> 的账号创建成功！</p>
        <p class="phone">教师手机号：{{fromData.cellPhone}}</p>
        <p class="tip">
          注：请通知教师登录账号后，尽快修改密码，并完善个人信息
        </p>
      </div>
      <div class="btn">
        <el-button class="button primary" @click="handleClose"
          >我知道了</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from "vue";
import Result403Icon from "@/assets/Check-Circle-Fill@2x.svg?component";
// 获取父组件值、方法
const props = defineProps({
  // 表单数据
  fromData: {
    type: Object,
    default: () => ({}),
  },
  // 弹层隐藏显示
  dialogSucceeVisible: {
    type: Boolean,
    default: false,
  },
});
// ------定义变量------
const emit = defineEmits(); //子组件获取父组件事件传值
// ------定义方法------
const handleClose = () => {
  emit("handleSucceeClose");
};
</script>
