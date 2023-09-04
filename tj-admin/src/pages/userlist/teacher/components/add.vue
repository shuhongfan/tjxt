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
      label-width="105px"
      class="demo-ruleForm"
      label-position="right"
    >
      <el-form-item label="教师名称：" prop="name">
        <div class="el-input">
          <el-input
            v-model="fromData.name"
            placeholder="请输入"
            @input="nameTextInput"
          />
          <span class="numText" :class="nameNumVal === 0 ? 'tip' : ''"
            >{{ nameNumVal }}/20</span
          >
        </div>
      </el-form-item>
      <el-form-item label="教师形象照：" prop="photo">
        <UploadImage
          @getFlag="getFlag"
          @getCoverUrl="getCoverUrl"
          @setUplad="setUplad"
          :upladImg="photoImg !== '' ? photoImg : fromData.photo"
          :isCourse="isCourse"
          ref="uploadImg"
        ></UploadImage>
      </el-form-item>
      <el-form-item label="教师手机号：" prop="cellPhone">
        <el-input
          v-model="fromData.cellPhone"
          placeholder="请输入"
          @blur="checkteacherphone"
        />
      </el-form-item>
      <el-form-item label="岗位：" prop="job">
        <div class="el-input">
          <el-input
            v-model="fromData.job"
            placeholder="请输入"
            minlength="2"
            @input="jobTextInput"
          />
          <span class="numText" :class="jobNumVal === 0 ? 'tip' : ''"
            >{{ jobNumVal }}/20</span
          >
        </div>
      </el-form-item>
      <el-form-item label="教师介绍：" prop="intro" class="textInput">
        <div class="inputText">
          <el-input
            v-model="fromData.intro"
            type="textarea"
            placeholder="请输入"
            resize="none"
            @input="introTextInput"
          />
          <span class="numText" :class="introNumVal === 0 ? 'tip' : ''"
            >{{ introNumVal }}/200</span
          >
        </div>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button class="button buttonPrimary" @click="handleClose"
          >取消</el-button
        >
        <el-button class="button primary" v-preventReClick @click="handleSubmit"
          >保存</el-button
        >
      </span>
    </template>
  </el-dialog>
</template>
<script setup>
import { ref, reactive, nextTick, watch } from "vue";
import { ElMessage } from "element-plus";
// 控制字节数
import { validateTextLength } from "@/utils/index.js";
import {
  validatePhone,
  validateContacts,
  validateContent,
} from "@/utils/validate.js";
// 接口api
import { editUser, saveUser } from "@/api/user";
import { checkPhone } from "@/api/staffs.js";
// 导入组件
// 上传图片
import UploadImage from "@/components/UploadImage/index.vue";
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
  // 表单数据
  fromData: {
    type: Object,
    default: () => ({}),
  },
});
// ------定义变量------
const emit = defineEmits(); //子组件获取父组件事件传值
const ruleFormRef = ref(); //表单校验ref
const uploadImg = ref(); //上传图片ref
let nameNumVal = ref(0); //教师字数显示
let jobNumVal = ref(0); //岗位字数显示
let introNumVal = ref(0); //教师介绍字数显示
let flag = ref(false); //用来控制上传图片是否校验成功
let isCourse = ref(false); //因为上传组件时公用的组件，所以用来控制一些样式
let photoImg = ref(""); //图片路径
let isPhone = ref(); //手机号是否重复
const checkPhoneData = reactive({
  data: {
    cellPhone: "",
    type: 1,
  },
}); //校验手机号
// 校验上传图片
const validateUpload = (rule, value, callback) => {
  if (flag.value) {
    callback();
  } else {
    callback(new Error("形象照为空，请上传形象照"));
  }
};
watch(
  () => props.fromData,
  (newValue, oldValue) => {
    // 课程名称默认的字数
    const value = validateTextLength(newValue.name, 20);
    nameNumVal.value = value.numVal;
    // 岗位默认的字数
    const jobValue = validateTextLength(newValue.job, 20);
    jobNumVal.value = jobValue.numVal;
    // 教师介绍默认的字数
    const introValue = validateTextLength(props.fromData.intro, 200);
    introNumVal.value = introValue.numVal;
  }
);
// 表单校验
const rules = reactive({
  name: [
    {
      required: true,
      validator: validateContacts,
      trigger: "blur",
    },
    {
      min: 2,
      message: "请至少输入2个字符",
      trigger: "blur",
    },
  ],
  photo: [
    {
      required: true,
      validator: validateUpload,
      trigger: "change",
    },
  ],
  cellPhone: [
    { required: true, validator: validatePhone, trigger: "blur" },
    {
      validator: (rule, value, callback) => {
        // 延迟执行0.3s，防止重复校验和校验不准确
        setTimeout(() => {
          if (isPhone.value === true) {
            callback(new Error("教师手机号已存在，请重新输入"));
          } else {
            callback();
          }
        }, 300);
      },
      trigger: "blur",
    },
  ],
  job: [
    {
      required: true,
      validator: validateContent,
      trigger: "blur",
    },
    {
      min: 2,
      message: "请至少输入2个字符",
      trigger: "blur",
    },
  ],
  intro: [
    {
      required: true,
      message: "教师介绍为空，请输入教师介绍",
      trigger: "blur",
    },
    {
      min: 10,
      message: "请至少输入10个字",
      trigger: "blur",
    },
  ],
});
// ------定义方法------
// 搜索
const handleSubmit = async () => {
  // 提交前先把获取详情的图片赋值给photoImg，要不然校验不到
  if (props.fromData.photo !== undefined && photoImg.value === "") {
    photoImg.value = props.fromData.photo;
    flag.value = true;
  }
  // 表单校验
  const valid = await ruleFormRef.value.validate();
  if (valid) {
    // 新增接口
    let params = {
      ...props.fromData,
      photo: photoImg.value,
      type: 3,
      roleId: 3
    };
    if (!props.fromData.id) {
      await addTeacher(params)
        .then((res) => {
          if (res.code === 200) {
            ElMessage({

              message: "新增成功!",
              type: "success",
              showClose: false,
            });
            ruleFormRef.value.resetFields(); //清空表单数据
            nameNumVal.value = 0; //清空教师字数
            jobNumVal.value = 0; //清空岗位字数
            introNumVal.value = 0; //清空教师介绍字数
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
      await editTeacher(params)
        .then((res) => {
          if (res.code === 200) {
            ElMessage({

              message: "教师账号信息修改成功!",
              type: "success",
              showClose: false,
            });
            ruleFormRef.value.resetFields(); //清空表单数据
            emit("setTearchId", null);
            handleClose();
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
  photoImg.value = "";
  uploadImg.value.imageUrl = "";
  nameNumVal.value = 0; //教师字数显示
  jobNumVal.value = 0; //岗位字数显示
  introNumVal.value = 0; //教师介绍字数显示
  flag.value = false;
  emit("handleClose");
};
// 校验图片
const getFlag = (val) => {
  flag.value = val;
};
// 获取封面图片
const getCoverUrl = (val) => {
  photoImg.value = val;
};
// 名字控制20个字符
const nameTextInput = () => {
  nextTick(() => {
    const value = validateTextLength(props.fromData.name, 20);
    props.fromData.name = value.val;
    nameNumVal.value = value.numVal;
  });
};

// 岗位控制20个字符
const jobTextInput = () => {
  nextTick(() => {
    const value = validateTextLength(props.fromData.job, 20);
    props.fromData.job = value.val;
    jobNumVal.value = value.numVal;
  });
};
// 教师介绍控制200个字符
const introTextInput = () => {
  nextTick(() => {
    const value = validateTextLength(props.fromData.intro, 200);
    props.fromData.intro = value.val;
    introNumVal.value = value.numVal;
  });
};
// 图片上传后的回调
const setUplad = (val) => {
  photoImg.value = "";
  props.fromData.photo = "";
};
// 校验手机号是否重复
const checkteacherphone = async () => {
  checkPhoneData.data.cellPhone = props.fromData.cellPhone;
  await checkPhone(checkPhoneData.data)
    .then((res) => {
      if (res.code === 200) {
        isPhone.value = res.data.exists;
      }
    })
    .catch((err) => {});
};
</script>
<style lang="scss" scoped>
:deep(.el-input__wrapper) {
  &:hover {
    border: 1px solid #887e7e;
  }
  // 修改.input输入框中的placeholder样式
  .el-input__inner {
    color: #332929;
    &::placeholder {
      color: #b5abab;
    }
  }
}
.uploadBox {
  color: #887e7e;
}
.numText {
  // top: 5px;
}
:deep(.el-select .el-input.is-focus .el-input__wrapper) {
  box-shadow: none;
}
:deep(.el-input__wrapper:hover) {
  box-shadow: none;
}
:deep(.el-textarea .el-textarea__inner) {
  color: #332929;
}
</style>
