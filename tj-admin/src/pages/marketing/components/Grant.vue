<!--发放弹层-->
<template>
  <div class="dialogMain markDialog">
    <el-dialog
      v-model="dialogVisible"
      title="设置领用/使用期限"
      :before-close="handleClose"
    >
      <el-form
        :model="fromData"
        ref="ruleFormRef"
        :rules="rules"
        label-width="130px"
        class="demo-ruleForm"
      >
        <el-form-item label="发放方式：" prop="grantWay">
          <el-radio-group v-model="fromData.grantWay" @change="handleType">
            <el-radio
              v-for="item in grantTypeData"
              :key="item.value"
              :label="item.value"
              >{{ item.label }}</el-radio
            >
          </el-radio-group>
        </el-form-item>
        <!-- 立刻发放 -->
        <el-form-item
          label="领用期限："
          prop="endTime"
          v-if="fromData.grantWay === 1"
          style="margin-bottom: 10px; margin-top: 10px"
          class="Collecttime"
        >
          <div class="el-input startTime">
            <el-date-picker
              v-model="fromData.endTime"
              value-format="YYYY-MM-DD HH:mm:ss"
              type="datetime"
              placeholder="请设置结束时间"
              align="right"
              :disabledDate="expireTimeOption"
              :default-time="startTime"
              clearable
              class="ImmediateRelease"
            >
            </el-date-picker>
          </div>
        </el-form-item>
        <!-- end -->
        <!-- 定时发放 -->
        <el-form-item
          label="领用期限："
          v-if="fromData.grantWay === 2"
          prop="dateReceivePicker"
          class="timeFrom"
        >
          <div class="el-input" style="margin-top: 10px">
            <el-date-picker
              v-model="fromData.dateReceivePicker"
              format="YYYY-MM-DD HH:mm:ss"
              value-format="YYYY-MM-DD HH:mm:ss"
              type="datetimerange"
              range-separator="至"
              popper-class="gettime"
              start-placeholder="请设置开始日期"
              end-placeholder="请设置结束日期"
              :disabledDate="dateReceiveTimeOption"
              :default-time="defaultTime"
              clearable
            >
            </el-date-picker>
          </div>
        </el-form-item>
        <!-- end -->
        <el-form-item
          label="使用期限方式："
          prop="deadlineType"
          style="margin-top: 10px"
        >
          <el-radio-group
            v-model="fromData.deadlineType"
            @change="handleDeadlineType"
          >
            <el-radio
              v-for="item in grantDeadlineData"
              :key="item.value"
              :label="item.value"
              :disabled="fromData.granted"
              >{{ item.label }}</el-radio
            >
          </el-radio-group>
        </el-form-item>
        <el-form-item
          label="使用期限："
          prop="termValidity"
          v-if="fromData.deadlineType === 1"
          style="margin-bottom: 15px; margin-top: 10px"
        >
          <span class="inputRight timeRight">
            <el-input
              v-model="fromData.termValidity"
              type="number"
              min="1"
              max="99"
              placeholder="请输入"
              :disabled="fromData.granted"
            ></el-input>
            <span class="text">天</span>
          </span>
        </el-form-item>
        <el-form-item
          label="使用期限："
          prop="dateUsePicker"
          v-if="fromData.deadlineType === 2"
          style="margin-bottom: 15px; margin-top: 10px"
        >
          <div class="el-input">
            <el-date-picker
              v-model="fromData.dateUsePicker"
              format="YYYY-MM-DD HH:mm:ss"
              value-format="YYYY-MM-DD HH:mm:ss"
              type="datetimerange"
              range-separator="至"
              start-placeholder="请设置开始日期"
              end-placeholder="请设置结束日期"
              :disabledDate="dateUseTimeOption"
              clearable
              :disabled="
                (fromData.grantWay === 1 && !fromData.endTime) ||
                (fromData.grantWay === 2 &&
                  fromData.dateReceivePicker.length === 0)
              "
              :default-time="defaultTime"
            >
            </el-date-picker>
          </div>
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button class="button buttonPrimary" @click="handleClose"
            >取消</el-button
          >
          <el-button class="button primary" @click="handleSubmit"
            >保存</el-button
          >
        </span>
      </template>
    </el-dialog>
  </div>
</template>
<script setup>
import {
  ref,
  reactive,
  nextTick,
  computed,
  onMounted,
  watchEffect,
  watch,
} from "vue"
import { ElMessage } from "element-plus"
import moment from "moment"
// 校验
import { validatorTime } from "@/utils/validate"
// 公用数据
import { grantTypeData, grantDeadlineData } from "@/utils/commonData"
// 接口
import { configGrant } from "@/api/marketing"
// 获取父组件值、方法
const props = defineProps({
  // 弹层隐藏显示
  dialogVisible: {
    type: Boolean,
    default: false,
  },
  deleteText: {
    type: String,
    default: "",
  },
  // 优惠券id
  couponId: {
    type: String,
    default: "",
  },
  // 优惠券详情
  grantData: {
    type: Object,
    default: () => ({}),
  },
})
// ------定义变量------
const emit = defineEmits() //子组件获取父组件事件传值
let fromData = ref({}) //表单数据
const ruleFormRef = ref() //表单校验ref
let startTimeShow = ref(false) //是否显示开始时间错误提示
let startTimeText = ref("") //是否显示开始时间错误提示语
const startTime = ref(new Date(2000, 2, 1, 23, 59, 59))
const defaultTime = ref([
  new Date(new Date()),
  new Date(2000, 2, 1, 23, 59, 59),
])
const expireTimeOption = (time) => {
  // 立刻发放当天时间之前
  return moment(time).isBefore(Date.now() - 24 * 60 * 60 * 1000)
}
// // 领用期限定时发放的开始时间要早于结束时间
// const dateStartTimeOption = (time) => {
//   // return time.getTime() < Date.now() - 24 * 60 * 60 * 1000;
//   return moment(time).isBefore(Date.now() - 24 * 60 * 60 * 1000);
//   // return time.getTime() > new Date(fromData.value.endTime).getTime();
// };
// // 领用期限的结束时间要晚于开始时间
// const dateEndTimeOption = (time) => {
//   return moment(time).isBefore(fromData.value.startTime);
//   // return time.getTime() < new Date(fromData.value.startTime).getTime();
// };
// // 使用期限应该晚于领用开始时间
// const dateTermStartTimeOption = (time) => {
//   const forms = fromData.value;
//   // 根据立刻发放的结束时间计算
//   if (forms.grantWay === 1) {
//     const endTime = forms.endTime;
//     if (endTime) {
//       // return time.getTime() < new Date(endTime).getTime();
//       return moment(time).isBefore(endTime);
//     }
//   } else {
//     // 根据定时发放的开始、结束时间计算
//     const startTime = forms.startTime;
//     if (startTime) {
//       // return time.getTime() < new Date(startTime).getTime();
//       return moment(time).isBefore(startTime);
//     }
//   }
// };
// // 使用结束时间需要晚于开始时间
// const dateTermEndTimeOption = (time) => {
//   const forms = fromData.value;
//   const startTime = forms.termStartTime;
//   if (startTime) {
//     // return time.getTime() < new Date(startTime).getTime();
//     return moment(time).isBefore(startTime);
//   }
// };

// 领用期限当天时间之前(包含当天)禁止选择
const dateReceiveTimeOption = (time) => {
  return moment(time).isBefore(Date.now() - 24 * 60 * 60 * 1000)
}
// 使用期限应该晚于领用开始时间
const dateUseTimeOption = (time) => {
  let data=fromData.value
  const dateReceivePicker = data.dateReceivePicker
  const endTime = data.endTime
  if (data.grantWay===1) {
    return time.getTime() < new Date().getTime() - 24 * 60 * 60 * 1000
  }else{
    return moment(time).isBefore(
      new Date(dateReceivePicker[0]) - 24 * 60 * 60 * 1000
    )
    // return time.getTime() < new Date(dateReceivePicker[0]).getTime() - 24 * 60 * 60 * 1000;
    // 时间段选择
    // return time.getTime()< new Date(dateReceivePicker[0]).getTime() || time.getTime()> new Date(dateReceivePicker[1]).getTime();
  }
  
}

// // 发放方式：定时发放的结束时间应该晚于(大于)开始时间
// const validatorstartTime = (rule, value, callback) => {
//   // const text = '领用开始时间需早于领用结束时间'
//   // validatorTime(rule, value, callback, fromData.value.endTime,text);
//   const froms = fromData.value;
//   if (!value) {
//     callback(new Error("领用期限为空，请设置领用期限"));
//   } else {
//     let start = new Date(value).getTime(); //开始时间戳
//     let end = new Date(froms.dateReceivePicker[1]).getTime(); //结束时间戳
//     console.log(start, end);
//     if (start >= end) {
//       startTimeText.value = "领用开始时间需早于领用结束时间";
//     } else {
//       startTimeText.value = "";
//     }
//   }
// };
// 立刻发放
const validatorImmediatelyEndTime = (rule, value, callback) => {
  const froms = fromData.value
  let presentEnd = new Date(new Date()).getTime() //当前时间戳
  let end = new Date(value).getTime() //选择的领用结束时间
  let termStartTime = new Date(froms.dateUsePicker[0]).getTime() //使用开始时间戳
  let termEndTime = new Date(froms.dateUsePicker[1]).getTime() //使用结束时间戳
  if (!value) {
    callback(new Error("领用期限为空，请设置领用期限"))
  }
  if (end < presentEnd) {
    callback(new Error("领用期限需晚于当前时间"))
  } else if (end > termEndTime) {

    callback(new Error("领用结束时间需等于/早于使用结束时间"))
  } else {
    callback()
  }
}
// 
const validatorTermValidity = (rule, value, callback) => {
  if (!value) {
    callback(new Error("使用期限为空，请设置使用期限"))
  } else {
    if (Number(value) < 1 || Number(value) > 99) {
      callback(new Error("请输入1-99的整数"))
    } else {
      callback()
    }
  }
}
// 发放方式：定时发放的结束时间应该晚于(大于)开始时间
const validatorEndTime = (rule, value, callback) => {
  const froms = fromData.value
  let start = new Date(froms.dateReceivePicker[0]).getTime() //领用开始时间戳
  let dateEnd = new Date(froms.dateReceivePicker[1]).getTime() //领用结束时间戳
  let end = new Date(new Date()).getTime() //当前时间戳
  let termStartTime = new Date(froms.dateUsePicker[0]).getTime() //使用开始时间戳
  let termEndTime = new Date(froms.dateUsePicker[1]).getTime() //使用结束时间戳
  if (start < end) {
    callback(new Error("领用期限需晚于当前时间"))
  } else if (termStartTime < start) {
    callback(new Error("领用开始时间需等于/早于使用开始时间"))
  } else if (termEndTime < dateEnd) {
    callback(new Error("领用结束时间需等于/早于使用结束时间"))
  } else if (start === dateEnd) {
    callback(new Error("领用结束时间需晚于领用开始时间"))
  } else {
    callback()
  }

  // validatorTime(rule, value, callback, froms.dateReceivePicker[0], text);
}
// 使用期限：固定时间段的结束时间应该晚于(大于)开始时间
const validatorTermEndTime = (rule, value, callback) => {
  const froms = fromData.value
  // validatorTime(rule, value, callback, fromData.value.termStartTime,text);
  if (!value) {
    callback(new Error("领用期限为空，请设置领用期限"))
  } else {
    let presentEnd = new Date(new Date()).getTime() //当前时间戳
    let end = new Date(value).getTime() //结束时间戳
    let startTime = new Date(froms.dateReceivePicker[0]).getTime() //领用开始时间
    let TermStartTime = new Date(froms.dateUsePicker[0]).getTime() //使用开始时间戳
    let termStartTime = new Date(froms.dateUsePicker[0]).getTime() //使用开始时间戳

    if (froms.dateReceivePicker !== undefined) {
      if (froms.grantWay === 1) {
        let endTime = new Date(froms.endTime).getTime() //领用结束时间
        if (termStartTime < presentEnd) {
          callback(new Error("使用期限需晚于当前时间"))
        } else if (endTime > end) {
          callback(new Error("使用结束时间需等于/晚于领用结束时间"))
        } else {
          callback()
        }
      } else {
        let endTime = new Date(froms.dateReceivePicker[1]).getTime() //定时发放结束时间戳
        if (endTime > end) {
          callback(new Error("使用结束时间需等于/晚于领用结束时间"))
        } else if (TermStartTime < startTime) {
          callback(new Error("使用开始时间需等于/晚于领用开始时间"))
        } else {
          callback()
        }
      }
    }
    // if (startTime === end) {
    //   callback(new Error("使用结束时间需晚于使用开始时间"));
    // } else {
    //   callback();
    // }
  }
}
// // 使用期限：固定时间段的开始时间应该晚于(大于)定时发放的开始时间
// const validatorTermStartTime = (rule, value, callback) => {
//   // const text = "使用开始时间需等于/晚于领用开始时间";
//   // validatorTime(rule, value, callback, fromData.value.startTime, text);
//   if (!value) {
//     callback(new Error("领用期限为空，请设置领用期限"));
//   } else {
//     let start = new Date(value).getTime(); //开始时间戳
//     let end = new Date(dateReceivePicker.value[0]).getTime(); //结束时间戳
//     let termEndTime = new Date(dateUsePicker.value[1]).getTime(); //开始时间戳
//     if (start < end) {
//       callback(new Error("使用开始时间需等于/晚于领用开始时间"));
//     } else if (start >= termEndTime) {
//       callback(new Error("使用开始时间需早于使用结束时间"));
//     } else {
//       callback();
//     }
//   }
// };
// 领用期限
const endTime = [
  {
    required: true,
    // message: "领用期限为空，请设置领用期限",
    validator: validatorImmediatelyEndTime,
    trigger: "blur",
  },
]
// 固定天数
const termValidity = [
  {
    required: true,
    validator: validatorTermValidity,
    // message: "使用期限为空，请设置使用期限",
    trigger: "blur",
  }
]
// 领用期限
const dateReceivePicker = [
  {
    type: "array",
    required: true,
    message: "领用期限为空，请设置领用期限",
    fields: {
      0: { type: "string", required: true, message: "请选择开始日期" },
      1: {
        required: true,
        validator: validatorEndTime,
        trigger: "change",
      },
    },
    // trigger: "change",
  },
]
// 使用期限
const dateUsePicker = [
  {
    type: "array",
    required: true,
    message: "使用期限为空，请设置使用期限",
    fields: {
      0: { type: "string", required: true, message: "请选择开始日期" },
      1: {
        required: true,
        validator: validatorTermEndTime,
        trigger: "change",
      },
    },
    // trigger: "change",
  },
]
// 表单校验
const rules = reactive({
  grantWay: [
    {
      required: true,
      message: "发放方式为空，请选择发放方式",
      trigger: "change",
    },
  ],
  deadlineType: [
    {
      required: true,
      message: "使用期限为空，请设置使用期限",
      trigger: "change",
    },
  ],
  endTime,
  termValidity,
  dateReceivePicker,
  dateUsePicker,
})
// 由于此表单根据不同的类型校验不同的参数，所以这里用watch
// 表单校验
watch("formData", (newValue, oldValue) => {
  if (newValue.grantWay === 1) {
    // 使用期限方式
    if (newValue.deadlineType === 1) {
      Object.assign(rules, {
        endTime,
        termValidity,
        dateReceivePicker,
        dateUsePicker,
      })

      setTimeout(() => {
        // 添加事件队列很重要
        ruleFormRef.value.validateField(["endTime", "termValidity"])
        ruleFormRef.value.clearValidate(["dateReceivePicker", "dateUsePicker"])
      }, 0)
    } else {
      // 返回合并校验
      // Object.assign(baseRules, endTime, dateUsePicker);
      Object.assign(rules, {
        endTime,
        dateUsePicker,
        dateReceivePicker,
        termValidity,
      })

      setTimeout(() => {
        // 添加事件队列很重要
        ruleFormRef.value.validateField(["endTime", "dateUsePicker"])
        ruleFormRef.value.clearValidate(["dateReceivePicker", "termValidity"])
      }, 0)
    }
  } else {
    // 使用期限方式
    // 固定天数
    if (forms.deadlineType === 1) {
      // 返回合并校验
      Object.assign(rules, {
        dateReceivePicker,
        termValidity,
        endTime,
        dateUsePicker,
      })
      setTimeout(() => {
        // 添加事件队列很重要
        ruleFormRef.value.validateField(["dateReceivePicker", "termValidity"])
        ruleFormRef.value.clearValidate(["endTime", "dateUsePicker"])
      }, 0)
    } else {
      // 返回合并校验
      Object.assign(rules, {
        dateReceivePicker,
        dateUsePicker,
        endTime,
        termValidity,
      })
      setTimeout(() => {
        // 添加事件队列很重要
        ruleFormRef.value.validateField(["dateReceivePicker", "dateUsePicker"])
        ruleFormRef.value.clearValidate(["endTime", "termValidity"])
      }, 0)
    }
  }
})

/*watchEffect(() => {
  if (props.grantData.termDays) {
    const grantData = props.grantData
    fromData.value = grantData
    let froms = fromData.value
    // 通过是否有天数来判断是固定 天数还是固定时间段
    if (grantData.termValidity === undefined) {
      froms.deadlineType = 2
    } else {
      froms.deadlineType === 1
    }
  }
})*/
// ------生命周期------
onMounted(() => {
  handleType();
  handleDeadlineType();
})
// ------定义方法------

// 保存
const handleSubmit = async (str) => {
  // 校验数据
  const valid = await ruleFormRef.value.validate()

  if (valid) {
    let froms = fromData.value
    const grantData = props.grantData

    let parent = {
      id: grantData.id,
    }
    // 如果是立刻发放，就只有结束时间
    if (froms.grantWay === 1) {
      parent.issueEndTime = froms.endTime;
    } else {
      parent.issueBeginTime = froms.dateReceivePicker[0];
      parent.issueEndTime = froms.dateReceivePicker[1];
    }
    if (froms.deadlineType === 1) {
      parent.termDays = Number(froms.termValidity)
    } else {
      parent.termBeginTime = froms.dateUsePicker[0];
      parent.termEndTime = froms.dateUsePicker[1];
    }
    await configGrant(parent)
      .then((res) => {
        if (res.code === 200) {
          ElMessage({

            message: "恭喜你，操作成功！",
            type: "success",
            showClose: false,
          })
          handleClose()
        }
      })
      .catch((err) => { })
  }
  emit("getList")
}
// 选择发放方式清空所填的数据
const handleType = (val) => {
  let froms = fromData.value
  // froms.deadlineType = null
  froms.endTime = ""
  froms.dateReceivePicker = []
  froms.dateUsePicker = []
  // froms.termValidity = null
}
// 选择使用期限清空所填的数据
const handleDeadlineType = (val) => {
  let froms = fromData.value
  froms.dateUsePicker = []
  froms.termValidity = null
}
//关闭弹层
const handleClose = () => {
  ruleFormRef.value.resetFields() //清空表单数据
  fromData.value={
    grantWay:'',
    endTime:'',
    dateReceivePicker:[],
    deadlineType:'',
    termValidity:'',
    dateUsePicker:[]

  }
  emit("handleClose")
};
</script>
<style scoped>
.errorTip {
  color: var(--el-color-danger);
  font-size: 12px;
  line-height: 20px;
  padding-top: 6px;
}
</style>
<style lang="scss" scoped>
:deep(.el-dialog .el-form-item) {
  margin-bottom: 0;
}
:deep(.el-dialog__title) {
  font-family: MicrosoftYaHei-Bold;
  font-weight: Bold;
  font-size: 16px;
  color: #332929;
  letter-spacing: 0.69px;
}
:deep(#el-id-2071-6) {
  padding-top: 25px;
}
:deep(.el-dialog .el-dialog__header) {
  padding-bottom: 20px;
}
:deep(.el-dialog .el-dialog__header) {
  font-size: inherit;
  padding-top: 21px;
}
// :deep(.markDialog .el-dialog){
//   width: 600px;
// }
:deep(.el-dialog .el-dialog__footer) {
  padding-bottom: 40px;
}
:deep(.el-dialog__title) {
  line-height: 16px;
}
</style>
<style lang="scss">
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
.el-time-panel__btn.confirm {
  color: #ff734f;
  &:hover {
    opacity: 0.8;
  }
}
.timeFrom{
  .el-form-item__label{
    padding-top: 10px;
  }
}
</style>
<style lang="scss">
:deep(.el-input .el-input__icon) {
  color: #b5abab;
}
.ImmediateRelease{
  .el-input__wrapper{
    .el-input__suffix{
      .el-input__suffix-inner{
        margin-right: 25px;
      }
    }
  }
}
</style>

