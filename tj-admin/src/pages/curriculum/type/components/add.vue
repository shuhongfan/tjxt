<!--课程分类添加、编辑-->
<template>
  <el-dialog
    v-model="dialogFormVisible"
    :title="title"
    :before-close="handleClose"
  >
    <el-form
      :model="fromData"
      ref="ruleFormRef"
      :rules="rules"
      label-width="108px"
      class="demo-ruleForm"
    >
      <el-form-item
        label="所属一级分类："
        class="marg-b-0"
        v-if="firstLevelData && firstLevelData.level === 1"
      >
        {{ firstLevelData.name }}
      </el-form-item>
      <el-form-item
        label="所属二级分类："
        class="marg-b-10"
        v-if="twoLevelData && twoLevelData.level === 2"
      >
        {{ twoLevelData.name }}
      </el-form-item>
      <el-form-item label="分类名称：" prop="name">
        <div class="el-input">
          <el-input
            v-model="fromData.name"
            placeholder="请输入"
            @input="textInput"
          />
          <span class="numText" :class="numVal === 0 ? 'tip' : ''"
            >{{ numVal }}/15</span
          >
        </div>
      </el-form-item>
      <el-form-item label="排序：" prop="index">
        <el-input-number
          v-model="fromData.index"
          :min="1"
          :max="15"
          @change="handleIndex"
          label="请输入数字，数字越小排名越靠前"
        ></el-input-number>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button class="button buttonPrimary" @click="handleClose"
          >取消</el-button
        >
        <el-button class="button primary" @click="handleSubmit">保存</el-button>
      </span>
    </template>
  </el-dialog>
</template>
<script setup>
import { ref, reactive, nextTick, watch } from "vue";
import { ElMessage } from "element-plus";
// 控制字节数
import { validateTextLength } from "@/utils/index.js";
import { indexValidate } from "@/utils/validate.js";
// 接口api
import { addCurriculumType, editCurriculumType } from "@/api/curriculum";
// 获取父组件值、方法
const props = defineProps({
  // 弹层隐藏显示
  dialogFormVisible: {
    type: Boolean,
    default: false,
  },
  //标题
  title: {
    type: String,
    default: "",
  },
  // 分类父级id
  parentId: {
    type: String,
    default: "",
  },
  // // 表单数据
  fromData: {
    type: Object,
    default: () => ({}),
  },
  // 一级分类内容
  firstLevelData: {
    type: Object,
    default: () => ({}),
  },
  // 二级分类内容
  twoLevelData: {
    type: Object,
    default: () => ({}),
  },
});
// ------定义变量------
const emit = defineEmits(); //子组件获取父组件事件传值
const ruleFormRef = ref(); //表单校验ref
let numVal = ref(0); //分类名称字数显示
// let fromData = reactive({}); //新增编辑表单数据
// 表单校验
const rules = reactive({
  name: [
    {
      required: true,
      message: "分类名称不能为空！",
      trigger: "blur",
    },
    {
      min: 2,
      message: "请输入2-15个字符！",
      trigger: "blur",
    },
  ],
  index: [
    {
      required: true,
      validator: indexValidate,
      trigger: "change",
    },
  ],
});
// ------定义方法------
watch(
  () => props.fromData,
  (newValue, oldValue) => {
    // 分类名称默认的字数
    const value = validateTextLength(newValue.name, 15);
    numVal.value = value.numVal;
  }
);
// 提交表单
const handleSubmit = async () => {
  // 表单校验
  const valid = await ruleFormRef.value.validate();
  if (valid) {
    // 新增接口
    if (props.fromData.id === undefined) {
      let params = {
        name: props.fromData.name,
        index: props.fromData.index.toString(),
        parentId: props.parentId,
      };
      await addCurriculumType(params)
        .then((res) => {
          if (res.code === 200) {
            ElMessage({

              message: "新增成功!",
              type: "success",
              showClose: false,
            });
            ruleFormRef.value.resetFields(); //清空表单数据
            handleClose();
            // 刷新列表
            emit("getList");
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
      // 编辑接口
      let params = {
        id: props.fromData.id,
        name: props.fromData.name,
        index: props.fromData.index.toString(),
      };
      await editCurriculumType(params)
        .then((res) => {
          if (res.code === 200) {
            ElMessage({

              message: "编辑成功!",
              type: "success",
              showClose: false,
            });
            ruleFormRef.value.resetFields(); //清空表单数据
            emit("handleClose");
            // 刷新列表
            emit("getList");
          }
        })
        .catch((err) => {});
    }
  }
};
// 关闭弹层
const handleClose = () => {
  ruleFormRef.value.resetFields(); //清空表单数据
  numVal.value = 0;
  emit("handleClose");
  emit('resetFrom')
};
// 名字控制20个字符
const textInput = () => {
  nextTick(() => {
    const value = validateTextLength(props.fromData.name, 15);
    // emit('getSort',value.val)
    numVal.value = value.numVal;
  });
};
// 获取排序的序号
const handleIndex = (value) => {
  emit("getSort", value);
};

// const handleInput = () => {
//   // emit('getSort',value)
//   props.fromData.index = Math.floor(props.fromData.index);
// };
</script>
<style lang="scss" scoped>
:deep(.el-input) {
  .el-input__wrapper {
    .el-input__inner {
      &::placeholder {
        color: #b5abab;
      }
    }
  }
}
:deep(.el-input-number__increase) {
  .el-icon {
    background: #faf6f5;
    // border: 1px solid #E9E9EB;
    width: 40px;
    height: 40px;
    border-radius: 0 6px 6px 0;
    bottom: 0px;
    right: 0px;
    top: 0;
    color: #000;
    &:hover {
      color: var(--color-btnbackground);
    }
  }
}
:deep(.el-input-number__decrease.is-disabled) {
  .el-icon {
    background: #faf6f5;
    // border: 1px solid #E9E9EB;
    width: 40px;
    height: 40px;
    border-radius: 6px 0 0 6px;
    bottom: 1px;
    left: 1px;
    top: 0;
    // 不可点击
    color: #b5abab;
    &:hover {
      color: #b5abab;
    }
  }
}
:deep(.el-input-number__decrease) {
  .el-icon {
    background: #faf6f5;
    // border: 1px solid #E9E9EB;
    width: 40px;
    height: 40px;
    border-radius: 6px 0 0 6px;
    bottom: 0px;
    left: 0px;
    top: 0;
    &:hover {
      color: var(--color-btnbackground);
    }
  }
}
:deep(.el-input-number__increase) {
  border-left: 0;
}
:deep(.el-input-number__decrease) {
  border-right: 0;
  background: #faf6f5;
  width: 40px;
  height: 40px;
  border-radius: 6px 0 0 6px;
  bottom: 1px;
  left: 1px;
}
:deep(.el-input .el-input__wrapper) {
  height: 42px;
}
// .numText{
//   top: 43px;
// }
</style>
