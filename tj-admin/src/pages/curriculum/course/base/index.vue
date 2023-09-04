<!--课程基本信息-->
<template>
  <el-form
    :model="fromData"
    ref="ruleFormRef"
    :rules="rules"
    label-width="130px"
    class="demo-ruleForm"
  >
    <el-form-item label="课程分类：" prop="thirdCateId">
      <div class="el-input inputRight">
        <el-cascader
          v-model="fromData.thirdCateId"
          placeholder="请选择"
          :options="typeData.value"
          v-if="fromData.canUpdate || fromData.canUpdate === undefined"
          :props="{
            label: 'name',
            value: 'id',
            children: 'children',
          }"
          clearable
          filterable
          collapse-tags
          @change="handleChange"
        >
          <template #default="{ data }">
            <span>{{ data.name }}</span>
          </template>
        </el-cascader>
        <el-input
          v-else
          v-model="fromData.cateNames"
          placeholder="请输入"
          :disabled="!fromData.canUpdate"
          @input="nameTextInput"
        />
      </div>
    </el-form-item>
    <el-form-item label="课程名称：" prop="name">
      <div class="el-input">
        <el-input
          v-model="fromData.name"
          placeholder="请输入"
          :disabled="!fromData.canUpdate"
          @input="nameTextInput"
        />
        <span
          v-if="fromData.canUpdate"
          class="numText"
          :class="nameNumVal === 0 ? 'tip' : ''"
          >{{ nameNumVal }}/30</span
        >
      </div>
    </el-form-item>
    <el-form-item label="课程封面：" prop="coverUrl">
      <UploadImage
        @getFlag="getFlag"
        @getCoverUrl="getCoverUrl"
        @setUplad="setUplad"
        :upladImg="fromData.coverUrl"
        :isCourse="isCourse"
      ></UploadImage>
    </el-form-item>
    <el-form-item label="售卖模式：" prop="free">
      <el-radio-group v-model="fromData.free">
        <el-radio
          v-for="(item, index) in sellingModelData"
          :key="index"
          :label="index"
          :disabled="!fromData.canUpdate"
          @change="handleFree"
          >{{ item.label }}</el-radio
        >
      </el-radio-group>
    </el-form-item>
    <el-form-item label="课程价格：" prop="price" v-if="fromData.free === 1">
      <div class="el-input inputRight inputMarginR">
        <el-input
          type="number"
          v-model="fromData.price"
          :disabled="!fromData.canUpdate"
          placeholder="请输入"
          clearable
          @input="textInputPrice"
          @blur="textBlurPrice"
          @onkeyup="handleOnkeyup"
          class="courseMoney"
        />
        <span class="text">元</span>
      </div>
    </el-form-item>
    <el-form-item label="下架时间：" prop="purchaseEndTime">
      <div class="el-input inputMarginR">
        <el-date-picker
          v-model="fromData.purchaseEndTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="datetime"
          :disabledDate="expireTimeOption"
          placeholder="请选择下架时间"
          clearable
          class="OffshelfTime"
          @change="handleDate($event)"
          :default-time="baseTime"
        >
        </el-date-picker>
      </div>
    </el-form-item>
    <el-form-item label="学习有效期：" prop="radio">
      <el-radio-group
        v-model="fromData.radio"
        :disabled="!fromData.canUpdate"
        @change="handleRadio"
      >
        <el-radio label="1">限时</el-radio>
        <el-radio label="2">永久</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="" prop="validDuration" v-if="isTimeLimit">
      <span class="inputRight timeRight">
        <el-input
          type="number"
          v-model="fromData.validDuration"
          minlength="1"
          maxlength="2"
          :disabled="!fromData.canUpdate"
          placeholder="请输入"
          @input="inputValidDuration"
        ></el-input>
        <span class="text">月</span>
      </span>
    </el-form-item>
    <el-form-item label="课程介绍：" prop="introduce" class="textInput">
      <div class="inputText">
        <el-input
          v-model="fromData.introduce"
          type="textarea"
          placeholder="请输入，最多可添加200个字"
          resize="none"
          @input="introTextInput"
        />
        <span class="numText" :class="introNumVal === 0 ? 'tip' : ''"
          >{{ introNumVal }}/200</span
        >
      </div>
    </el-form-item>
    <el-form-item label="适用人群：" prop="usePeople" class="textInput">
      <div class="inputText">
        <el-input
          v-model="fromData.usePeople"
          type="textarea"
          placeholder="请输入，最多可添加200个字"
          resize="none"
          @input="usePeopleTextInput"
        />
        <span class="numText" :class="usePeopleNumVal === 0 ? 'tip' : ''"
          >{{ usePeopleNumVal }}/200</span
        >
      </div>
    </el-form-item>
    <el-form-item
      label="教师介绍：&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"
      class="textInput"
    >
      <div class="tearch">
        <div class="head">
          <img src="../../../../assets/img_touxiang_small@2x.png" />
          <div class="text">
            <p>武大庆</p>
            <p>大厂高级前端开发工程师</p>
          </div>
        </div>
        <div class="info">
          多年互联网产品运营策划经验，掌握产品各环节运营方法。曾参加两家初创公司，擅长产品的冷启动，从0到1做用户精细化运营。曾以用户为中心，满足对产品的需求，使产品在三个月内用户增长23万。
        </div>
        <span class="tooltipIcon">
          <span class="hover">用户端将自动展示教师信息，无需手动设置</span>
        </span>
      </div>
    </el-form-item>
    <el-form-item label="课程详情：" prop="detail" class="textInput">
      <div class="inputText">
        <el-input
          v-model="fromData.detail"
          type="textarea"
          placeholder="请输入，最多可添加500个字"
          resize="none"
          @input="detailTextInput"
        />
        <span class="numText" :class="detailNumVal === 0 ? 'tip' : ''"
          >{{ detailNumVal }}/500</span
        >
      </div>
    </el-form-item>
  </el-form>
</template>
<script setup>
import { ref, reactive, onMounted, watch, nextTick, watchEffect } from "vue";
import { ElMessage } from "element-plus";
import { useRouter, useRoute } from "vue-router";
// 获取vuex存储数据
import { useUserStore } from "@/store";
// 控制字节数
import { validateTextLength, decimalsReplenish } from "@/utils/index.js";
// 公用数据
import { sellingModelData } from "@/utils/commonData";
// 表单校验
import { validatorPrice, validatorValidDuration } from "@/utils/validate";
// 接口
import { getTypeAll } from "@/api/api";
import {
  baseInfoSave,
  getCoursesDetail,
  setCoursesName,
} from "@/api/curriculum";
// 导入组件
// 上传图片
import UploadImage from "@/components/UploadImage/index.vue";
// ------定义变量------
// 获取父组件值、方法
const props = defineProps({});
//初始化路由
// ------vuex存储数据------
const store = useUserStore();
const router = useRouter(); //获取全局
const route = useRoute(); //获取局部
const emit = defineEmits(); //子组件获取父组件事件传值
let fromData = ref({
  thirdCateId: [],
  purchaseEndTime: "",
  radio: null,
  canUpdate: true,
}); //新增编辑表单数据
let typeData = reactive([]); //类型数据
const ruleFormRef = ref(); //表单校验ref
let nameNumVal = ref(0); //名字字数显示
let introNumVal = ref(0); //课程介绍字数显示
let usePeopleNumVal = ref(0); //适宜人群字数显示
let detailNumVal = ref(0); //课程详情字数显示
let flag = ref(false); //用来控制上传图片是否校验成功
let flagTime = ref(false); //用来日期是否校验成功
let isCourse = ref(true); //用来区别是课程封面上传还是教师头像选择，因为用的是通用图片上传组件
let isTimeLimit = ref(false); // 用来判断有效期是限时还是永久，限时的时候显示
let price = ref(0); //金额
let courseId = ref(null); //当前课程id
const baseTime = ref(new Date(2000, 2, 1, 23, 59, 59));
const expireTimeOption = (time) => {
  // 当天时间之前
  return time.getTime() < Date.now() - 24 * 60 * 60 * 1000;
};
// 校验上传图片
const validateUpload = (rule, value, callback) => {
  if (flag.value) {
    callback();
  } else {
    callback(new Error("课程封面为空，请上传课程封面"));
  }
};
// 课程名称校验
const validateName = async (rule, value, callback) => {
  if (value === "" || value === undefined) {
    callback(new Error("课程名称为空，请填写课程名称"));
  }
  let parent = {
    name: value,
    id: fromData.value.id,
  };
  await setCoursesName(parent).then((res) => {
    if (res.code === 200) {
      const isexisted = res.data.existed;
      if (isexisted) {
        callback(new Error("课程名称重复，请重新输入"));
      } else {
        callback();
      }
    }
  });
};
// 表单校验
const rules = reactive({
  thirdCateId: [
    {
      required: true,
      message: "课程分类为空，请选择课程分类",
      trigger: "change",
    },
  ],
  name: [
    {
      required: true,
      // message: "课程名称为空，请填写课程名称",
      validator: validateName,
      trigger: "blur",
    },
  ],
  coverUrl: [
    {
      required: true,
      validator: validateUpload,
      trigger: "change",
    },
  ],
  valid: [
    {
      required: true,
      message: "售卖模式为空，请选择售卖模式",
      trigger: "change",
    },
  ],
  price: [
    {
      required: true,
      validator: validatorPrice,
      trigger: "blur",
    },
  ],
  purchaseEndTime: [
    {
      trigger: "blur",
      required: true,
      message: "下架时间为空，请设置下架时间",
    },
  ],
  free: [
    {
      required: true,
      message: "售卖模式为空，请选择售卖模式",
      trigger: "change",
    },
  ],
  radio: [
    {
      required: true,
      message: "学习有效期为空，请设置学习有效期",
      trigger: "change",
    },
  ],
  validDuration: [
    {
      required: true,
      validator: validatorValidDuration,
      trigger: "blur",
    },
  ],
  introduce: [
    {
      required: true,
      message: "课程介绍为空，请输入课程介绍",
      trigger: "blur",
    },
  ],
  usePeople: [
    {
      required: true,
      message: "适用人群为空，请输入适用人群",
      trigger: "blur",
    },
  ],
  detail: [
    {
      required: true,
      message: "课程详情为空，请输入课程详情",
      trigger: "blur",
    },
  ],
});
// // 监听修改金额数值，小数点后保留2位
watch(fromData, (newValue, oldValue) => {
  const val = newValue.price;
  const validDuration = newValue.validDuration;
  // 课程名称默认的字数
  const value = validateTextLength(newValue.name, 30);
  nameNumVal.value = value.numVal;
  // 课程介绍默认的字数
  const introduceValue = validateTextLength(newValue.introduce, 200);
  introNumVal.value = introduceValue.numVal;
  // 适用人群默认的字数
  const usePeopleValue = validateTextLength(newValue.usePeople, 200);
  usePeopleNumVal.value = usePeopleValue.numVal;
  // 课程详情默认的字数
  const detailValue = validateTextLength(newValue.detail, 200);
  detailNumVal.value = detailValue.numVal;
  nextTick(() => {
    if (val < 100000 && val > 0) {
      fromData.value.price = parseInt(val * 100) / 100;
      fromData.value.price = decimalsReplenish(Number(fromData.value.price));
    }
    // 学习有效期选择
    if (isTimeLimit.value) {
      if (validDuration < 1) {
        fromData.validDuration = 1;
      } else if (validDuration > 99) {
        fromData.validDuration = 99;
      }
    }
  });
});
// watch(()=>fromData.value.price,(newValue,oldValue)=>{
//   nextTick(()=>{
//     if (newValue < 100000 && newValue > 0) {
//       fromData.value.price = parseInt(newValue * 100) / 100
//     }
//   })

// })
// ------生命周期------
watchEffect(() => {
  nextTick(() => {
    courseId.value = route.params.id;
    if (courseId.value !== "null") {
      getDetailData();
    }
  });
});
// ------定义方法------
// 监听课程价格
const textInputPrice = () => {
  // if (fromData.value.price > 0) {
  //   fromData.value.price = parseInt(fromData.value.price * 100) / 100
  // }
  // let data=fromData.value.price
  // console.log(data)
  // if (data.indexOf(".") ===1) {
  //   fromData.value.price = Math.floor(data * 100) / 100;
  // }
};
// 监听课程价格
const textBlurPrice = () => {
  let data = fromData.value.price;
  if (data !== undefined) {
    fromData.value.price = decimalsReplenish(Number(data));
  }
};
// const textBlurPrice = () => {
//   let data = fromData.value.price;
//   if (data.indexOf(".") ===1) {
//     fromData.value.price = Math.floor(data * 100) / 100;
//   }
//   if (data.indexOf(".") ===-1) {
//     fromData.value.price = decimalsReplenish(data.toString())
//   }
// };
// 触发键盘
const handleOnkeyup = (val) => {
  return val.key.replace(/[^\d^\.]+/g, "");

  // let data = val.key;
  // fromData.value.price = data
  //   .replace(/[^\d^\.]+/g, "")
  // .replace(".", "$#$")
  // .replace(/\./g, "")
  // .replace("$#$", ".");
};
onMounted(() => {
  getTypeList();
});
// 获取分类
const getTypeList = async () => {
  await getTypeAll({ admin: true })
    .then((res) => {
      if (res.code === 200) {
        typeData.value = res.data;
      }
    })
    .catch((err) => {});
};
// 获取详情
let getDetailData = async () => {
  await getCoursesDetail(courseId.value)
    .then((res) => {
      if (res.code === 200) {
        fromData.value = res.data;
        // 设置当前可以触发的步骤
        store.setStepNum(fromData.value.step);
        // // 处理购买周期时间
        // let times = [
        //   fromData.value.purchaseStartTime,
        //   fromData.value.purchaseEndTime,
        // ];
        // fromData.value.purchaseEndTime = times;
        //  处理售卖模式
        fromData.value.free = fromData.value.free ? 0 : 1;
        // 有效周期
        fromData.value.radio = fromData.value.validDuration > 99 ? "2" : "1";
        isTimeLimit.value = fromData.value.validDuration > 99 ? false : true;
        // 处理价格
        fromData.value.price = fromData.value.price / 100;
        fromData.value.price = decimalsReplenish(Number(fromData.value.price));
      }
    })
    .catch((err) => {});
};
// 保存
const handleSubmit = async (str) => {
  // 表单校验
  // 提交前先把获取详情的图片赋值给photoImg，要不然校验不到
  if (fromData.value.coverUrl !== undefined) {
    flag.value = true;
  }
  const valid = await ruleFormRef.value.validate();
  console.log(fromData.value.thirdCateId);
  if (valid) {
    //  获取购买周期
    let parent = {
      id: fromData.value.id,
      name: fromData.value.name,
      coverUrl: fromData.value.coverUrl,
      detail: fromData.value.detail,
      thirdCateId:
        fromData.value.thirdCateId.length === 3
          ? fromData.value.thirdCateId[2]
          : fromData.value.thirdCateId,
      introduce: fromData.value.introduce,
      price: fromData.value.free === 0 ? 0 : Number(fromData.value.price) * 100,
      purchaseEndTime: fromData.value.purchaseEndTime, //获取结束时间
      usePeople: fromData.value.usePeople,
      free: fromData.value.free === 0 ? true : false, //是否免费
      validDuration: isTimeLimit.value ? fromData.value.validDuration : "9999",
    };
    await baseInfoSave(parent)
      .then((res) => {
        if (res.code === 200) {
          ElMessage({

            message: "恭喜你，操作成功！",
            type: "success",
            showClose: false,
          });
          courseId.value = res.data.id;
          emit("getCourseId", res.data.id);
          emit("getActive", 1);
          // 保存并返回
          if (str === "getback") {
            router.push({
              path: "/curriculum/index",
            });
          } else {
            // 保存并下一步
            router.push({
              path: `/curriculum/add/` + courseId.value,
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
  }
};
// 选择课程分类
const handleChange = (val) => {
  fromData.thirdCateId = val;
};
// 名字控制30个字符
const nameTextInput = () => {
  nextTick(() => {
    let data = fromData.value;
    const value = validateTextLength(data.name, 30);
    data.name = value.val;
    nameNumVal.value = value.numVal;
  });
};
// 课程介绍控制200个字符
const introTextInput = () => {
  nextTick(() => {
    let data = fromData.value;
    const value = validateTextLength(data.introduce, 200);
    data.introduce = value.val;
    introNumVal.value = value.numVal;
  });
};
// 适宜人群
const usePeopleTextInput = () => {
  nextTick(() => {
    let data = fromData.value;
    const value = validateTextLength(data.usePeople, 200);
    data.usePeople = value.val;
    usePeopleNumVal.value = value.numVal;
  });
};
// 课程详情
const detailTextInput = () => {
  nextTick(() => {
    let data = fromData.value;
    const value = validateTextLength(data.detail, 500);
    data.detail = value.val;
    detailNumVal.value = value.numVal;
  });
};
//控制课程封面是否有图片
const getFlag = (val) => {
  flag.value = val;
};
// 获取时间
const handleDate = () => {
  flagTime.value = true;
};
// 获取封面图片
const getCoverUrl = (val) => {
  fromData.value.coverUrl = val;
};
// 学习有效期判断是限时还是永久，控制限时输入框显示隐藏
const handleRadio = (val) => {
  if (val === "1") {
    isTimeLimit.value = true;
  } else {
    isTimeLimit.value = false;
  }
  fromData.value.validDuration = null;
};
// 图片上传后的回调
const setUplad = (val) => {
  fromData.value.coverUrl = "";
};
// 去除有效期首位时0
const inputValidDuration = (val) => {
  if (val.toString().substr(0, 1) === "0") {
    fromData.value.validDuration = 1;
  }
};
// 售卖模式修改时清空课程价格
const handleFree = () => {
  fromData.value.price = "";
};
// 向父组件暴露方法
defineExpose({
  handleSubmit,
});
</script>
<style lang="scss">
.el-date-table td.disabled .el-date-table-cell {
  background-color: #fff;
}
.el-date-table td.today .el-date-table-cell__text {
  font-family: PingFangSC-S0pxibold;
  font-weight: 600;
  font-size: 12px;
  color: #fe734f;
}
.el-date-table td.available:hover {
  font-family: PingFangSC-S0pxibold;
  font-size: 12px;
  color: #fe734f;
}
.el-date-table td.current:not(.disabled) .el-date-table-cell__text {
  background: #fe734f;
}
.detailBox .el-form-item__label {
  color: #332929;
}
</style>
<style lang="scss" scoped>
:deep(.el-input__wrapper) {
  .el-input__inner {
    &::placeholder {
      color: #b5abab;
    }
  }
}
:deep(.el-textarea) {
  .el-textarea__inner {
    &::placeholder {
      color: #b5abab;
    }
  }
}
</style>
<style lang="scss">
.OffshelfTime {
  .el-input__wrapper {
    .el-input__suffix {
      .el-input__suffix-inner {
        margin-right: 20px;
      }
    }
  }
}
.courseMoney {
  .el-input__wrapper {
    .el-input__suffix {
      .el-input__suffix-inner {
        margin-right: 20px;
      }
    }
  }
}
.el-cascader-menu:last-child .el-cascader-node {
  &:hover {
    background-color: #fff;
  }
}
</style>
<style lang="scss">
.el-picker-panel__icon-btn:hover {
  color: #ff734f;
}
.el-date-picker__header-label:hover {
  color: #ffffff;
}
.el-date-table td.disabled div {
}
// 默认状态
.el-date-table td.today {
  .el-date-table-cell__text {
    color: #ff734f;
  }
}
// 鼠标停留状态
.el-picker-panel .el-date-table td.available:hover {
  color: #ffffff;
  .el-date-table-cell {
    .el-date-table-cell__text {
      background-color: #ff734f;
    }
  }
}
// 开始按钮选中状态
.el-date-table td.start-date {
  .el-date-table-cell__text {
    background-color: #ff734f;
  }
}
// 结束按钮选中状态
.el-date-table td.end-date {
  .el-date-table-cell__text {
    background-color: #ff734f;
  }
}
// 开始和结束之间的按钮的样式
.el-date-table td.in-range {
  .el-date-table-cell {
    background-color: #faf4ee;
    &:hover {
      background-color: #faf4ee;
    }
  }
}
.button {
  font-family: PingFangSC-Regular;
}
.el-button.is-plain {
  width: 64.5px;
  height: 30px;
  background: #ff734f;
  border-radius: 20px;
  font-family: PingFangSC-Medium;
  font-weight: 400;
  font-size: 14px;
  color: #ffffff;
  border: 1px solid transparent;
  &:hover {
    background: #f16440;
    border: 0;
  }
}
.el-button.is-text {
  width: 64.5px;
  height: 30px;
  // border: 1px solid #FF734F;
  box-sizing: border-box;
  border-radius: 20px;
  font-family: PingFangSC-Medium;
  font-weight: 400;
  font-size: 14px;
  background: #ffffff;
  &:hover {
    background: #f16440;
    border: 0;
  }
}
.el-picker-panel__footer {
  padding: 10px 30px;
}
.el-date-table td.current:not(.disabled) .el-date-table-cell__text {
  background: #f16440;
}
.el-date-table td.today {
  // hover时的样式
  &:hover {
    .el-date-table-cell__text {
      color: #fff;
      font-weight: normal;
    }
  }
}
.el-time-panel__btn.confirm {
  color: #ff734f;
  &:hover {
    opacity: 0.8;
  }
}
</style>
<style lang="scss" scoped>
.tooltipIcon {
  background: url(@/assets/btn-xiangqing.png) no-repeat;
  background-size: contain;
  display: inline-block;
  width: 17px;
  height: 17px;
  position: absolute;
  left: -38px;
  top: 13px;
  .hover {
    position: absolute;
    display: none;
    z-index: 9;
    left: -20px;
    top: 30px;
    width: 250px;
    border-radius: 6px;
    line-height: 40px;
    padding: 0 10px;
    background: #ffffff;
    box-shadow: 0 0 8px 1px rgba(0, 0, 0, 0.05);
    font-weight: 400;
    font-size: 12px;
    color: #332929;
    &::after {
      position: absolute;
      background: #ffffff;
      top: -5px;
      left: 23px;
      z-index: -1;
      content: "";
      width: 10px;
      height: 10px;
      transform: rotate(45deg);
      box-shadow: 0 0 8px 1px rgba(0, 0, 0, 0.05);
    }
    &::before {
      position: absolute;
      content: "";
      background: #fff;
      width: 50px;
      height: 10px;
      top: 0px;
      left: 10px;
    }
  }
  &:hover .hover {
    display: block;
  }
}
:deep(.el-input) {
  width: 293px;
}
</style>