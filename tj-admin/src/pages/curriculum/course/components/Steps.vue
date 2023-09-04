<!--步骤条-->
<template>
  <div class="stepsBox">
    <el-steps :active="active" simple finish-status="success">
      <el-step
        v-for="(item, index) in stepData"
        :key="index"
        :class="stepClassObj(index)"
        @click="handleClick(index)"
      >
        <template v-slot:icon>
          <span class="stepNum">
            {{ item.value }}
          </span>
        </template>
        <template v-slot:title>
          <span>
            {{ item.label }}
          </span>
        </template>
      </el-step>
    </el-steps>
  </div>
</template>
<script setup>
import { ref, onMounted, computed} from "vue";
import { stepData } from "@/utils/commonData";
// 获取vuex存储数据
import { useUserStore } from "@/store";
// 获取父组件值、方法
const props = defineProps({
  // 搜索对象
  title: {
    type: String,
    default: "",
  },
  // 当前step步骤
  active: {
    type: Number,
    default: null,
  },
  // 可以触发的的step步骤
  // stepNumData: {
  //   type: Array,
  //   default: () => [],
  // },
});
// ------vuex存储数据------
const store = useUserStore();
let stepNumData = ref(0);
let stepClassObj = computed((val) => {
  return (val) => {
    return {
      stepSuc: stepSuc.value.includes(val),
      stepErr: !stepSuc.value.includes(val),
    };
  };
});
// ------定义变量------
const emit = defineEmits(); //子组件获取父组件事件传值
let stepSuc = ref([]); //获取可以触发的step步骤
onMounted(() => {
  const stepNum = store.getStepNum;
  if (stepNum > 0) {
    stepNumData.value = stepNum;
    let arr = [];
    for (let i = 0; i < stepNum; i++) {
      arr.push(i);
    }
    stepSuc.value = arr;
    emit("getActive", Number(stepNum - 1));
  }
});
// ------定义方法------
onMounted(() => {});
// 触发step步骤
const handleClick = (val) => {
  if (stepSuc.value.includes(val) === true) {
    emit("getActive", val);
  }
};
</script>

<style lang="scss">
.el-step.is-simple .el-step__title{
  color: #B5ABAB;
}
.stepsBox .el-step.is-simple .is-wait .el-step__icon {
    border-color: #B5ABAB !important;
}
:deep(.stepsBox .el-step.is-simple .el-step__icon){
  color: #B5ABAB;
}
</style>