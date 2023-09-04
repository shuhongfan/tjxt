<!--新增、编辑优惠券-->
<template>
  <el-form
    :model="fromData"
    ref="ruleFormRef"
    :rules="rules"
    label-width="130px"
    class="demo-ruleForm"
  >
    <el-form-item label="优惠券名称：" prop="name">
      <div class="el-input">
        <el-input
          v-model="fromData.name"
          placeholder="请输入"
          @input="nameTextInput"
          @keydown="channelInputKeyCode"
        />
        <span class="numText" :class="nameNumVal === 0 ? 'tip' : ''"
          >{{ nameNumVal }}/20</span
        >
      </div>
    </el-form-item>
    <el-form-item label="使用范围：" prop="rangeType">
      <el-radio-group v-model="fromData.rangeType">
        <el-radio
          v-for="item in couponsScopeData"
          :key="item.value"
          :label="item.value"
          >{{ item.label }}</el-radio
        >
      </el-radio-group>
    </el-form-item>

    <el-form-item label="" prop="cateIds" v-if="fromData.rangeType === 1">
      <div class="el-input inputRight">
        <el-cascader
          v-model="fromData.cateIds"
          placeholder="请选择"
          :options="typeData.value"
          :props="{
            label: 'name',
            value: 'id',
            children: 'children',
            multiple: true,
          }"
          collapse-tags
          filterable
        >
          <template #default="{ data }">
            <span>{{ data.name }}</span>
          </template>
        </el-cascader>
      </div>
    </el-form-item>
    <el-form-item label="优惠券类型：" prop="type">
      <el-radio-group v-model="fromData.type" @change="handleType">
        <el-radio
          v-for="item in couponsTypeData"
          :key="item.value"
          :label="item.value"
          >{{ item.label }}</el-radio
        >
      </el-radio-group>
    </el-form-item>
    <el-form-item label="" v-if="fromData.type">
      <!-- 每满减 -->
      <div class="subtractInfo">
        <el-form-item prop="amountCondition" v-if="fromData.type !== 3">
          <div class="inputW inputRight">
            <span>{{ fromData.type === 1 ? "每满" : "满" }}</span>
            <el-input
              type="number"
              v-model="fromData.amountCondition"
              placeholder="请输入"
              @keydown="channelInputLimit"
            />
            <span class="text">元</span>
          </div>
        </el-form-item>
        <el-form-item
          prop="discountAmount"
          v-if="
            fromData.type === 1 || fromData.type === 3 || fromData.type === 4
          "
        >
          <div class="inputW inputRight">
            <span v-if="fromData.type !== 3">减</span
            ><el-input
              type="number"
              v-model="fromData.discountAmount"
              placeholder="请输入"
              @keydown="channelInputLimit"
            />
            <span class="text">元</span>
          </div>
        </el-form-item>
        <el-form-item prop="discountRate" v-if="fromData.type === 2">
          <div class="inputW inputRight">
            <span>打</span
            ><el-input
              type="number"
              v-model="fromData.discountRate"
              placeholder="请输入"
              @keydown="channelInputLimit"
            />
            <span class="text">折</span>
          </div>
        </el-form-item>
        <el-form-item
          prop="maxDiscountAmount"
          v-if="fromData.type !== 3 && fromData.type !== 4"
        >
          <div class="inputW inputRight">
            <span>最高减</span
            ><el-input
              v-model="fromData.maxDiscountAmount"
              placeholder="请输入"
              @keydown="channelInputLimit"
            />
            <span class="text">元</span>
          </div>
        </el-form-item>
      </div>
      <!-- end -->
    </el-form-item>
    <!-- 此功能放在了发放弹出层里 -->
    <!-- <el-form-item label="领用期限：" prop="dateReceivePicker">
      <div class="el-input">
        <el-date-picker
          v-model="fromData.dateReceivePicker"
          format="YYYY-MM-DD HH:mm:ss"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :disabledDate="dateReceiveTimeOption"
          clearable
        >
        </el-date-picker>
      </div>
    </el-form-item>
    <el-form-item label="使用期限：" prop="dateUsePicker">
      <div class="el-input">
        <el-date-picker
          v-model="fromData.dateUsePicker"
          format="YYYY-MM-DD HH:mm:ss"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :disabledDate="dateUseTimeOption"
          :disabled="!fromData.dateReceivePicker"
          clearable
        >
        </el-date-picker>
      </div>
    </el-form-item> -->
    <div class="tit"><span>优惠券推广方式</span></div>
    <el-form-item label="推广方式：" prop="obtainWay">
      <el-radio-group v-model="fromData.obtainWay" @change="handWay">
        <el-radio
          v-for="item in couponsWayData"
          :key="item.value"
          :label="item.value"
          >{{ item.label }}</el-radio
        >
      </el-radio-group>
    </el-form-item>
    <el-form-item label="发放数量：" prop="totalNum">
      <div class="subtractInfo">
        <div class="inputW inputRight">
          <el-input
            v-model="fromData.totalNum"
            placeholder="请输入"
            @input="inputTotalNum"
          />
          <span class="text">张</span>
        </div>
      </div>
    </el-form-item>
    <el-form-item label="每人限领：" prop="perNum">
      <div class="subtractInfo">
        <div class="inputW inputRight">
          <el-input
            v-model="fromData.perNum"
            placeholder="请输入"
            @input="inputPerNum"
          />
          <span class="text">张</span>
        </div>
      </div>
    </el-form-item>
  </el-form>
</template>
<script setup>
import {
  ref,
  reactive,
  onMounted,
  watch,
  nextTick,
  watchEffect,
  computed,
} from "vue"
import { ElMessage } from "element-plus"
import { useRouter, useRoute } from "vue-router"
//
// 控制字节数
import { validateTextLength, inputNum,channelInputLimit,channelInputKeyCode } from "@/utils/index.js"
// 公用数据
import {
  couponsScopeData,
  couponsTypeData,
  couponsWayData,
} from "@/utils/commonData"
// 表单校验
import {
  amountConditionValid, //每满
  // validatorAmount,
  discountAmountValid, //减
  validatorMaxAmountValid, //最高金额
  discountRateValid, //最高满减金额不能小于折扣后的价格
  thresholdValid, //无门槛
  validatorDiscountRate,
  first,
} from "@/utils/validate"
// 接口
import { getTypeAll } from "@/api/api"
import { getDetails, saveMarket ,updateCoupon} from "@/api/marketing"
// 缓存
import { catchDataesStore } from '@/store';
// 导入组件
// ------定义变量------
// 缓存
const store = catchDataesStore();
// 获取父组件值、方法
const props = defineProps({})
//初始化路由
const router = useRouter() //获取全局
const route = useRoute() //获取局部
const emit = defineEmits() //子组件获取父组件事件传值
let fromData = ref({}) //新增编辑表单数据
let typeData = reactive([]) //类型数据
let typeMap = reactive([]) //类型数据的id映射
const ruleFormRef = ref() //表单校验ref
let nameNumVal = ref(0) //名称字数显示
let courseId = ref(null) //当前课程id
// // 领用期限当天时间之前(包含当天)禁止选择
// const dateReceiveTimeOption = (time) => {
//   return time.getTime() <= new Date().getTime();
// };
// // 使用期限应该晚于领用开始时间
// const dateUseTimeOption = (time) => {
//   const dateReceivePicker = fromData.value.dateReceivePicker;
//   if (dateReceivePicker) {
//     return time.getTime() < new Date(dateReceivePicker[0]).getTime();
//     // 时间段选择
//     // return time.getTime()< new Date(dateReceivePicker[0]).getTime() || time.getTime()> new Date(dateReceivePicker[1]).getTime();
//   }
// };
// 满减金额校验是否为空
const validatorAmountCondition = (rule, value, callback) => {
  if (!value) {
    callback(new Error("满减金额为空，请输入满减金额"))
  } else {
    amountConditionValid(rule, value, callback)
  }
}
// 满减金额不能高于总金额
const validatorAmountCon = (rule, value, callback) => {
  if (fromData.value.type === 3) {
    if (!value) {
      callback(new Error("无门槛金额为空，请输入无门槛金额"))
    }
    // 满减金额不能高于总金额
    thresholdValid(rule, value, callback, fromData.value.discountAmount)
  }
  if (
    fromData.value.type === 1 ||
    fromData.value.type === 2 ||
    fromData.value.type === 4
  ) {
    // 满减金额不能高于总金额
    discountAmountValid(rule, value, callback, fromData.value.amountCondition)
  }
}
// 最高满减金额需大于满减金额
const validatorMaxAmount = (rule, value, callback) => {
  const froms = fromData.value
  // 无门槛
  if (froms.type === 3) {
    thresholdValid(rule, value, callback, froms.discountAmount)
  } else {
    // 每满金额减去打折后的价格，最高满减金额不能小于折扣后的价格
    if (fromData.value.type === 2) {
      discountRateValid(
        rule,
        value,
        callback,
        froms.amountCondition - froms.amountCondition * (froms.discountRate / 10)
      )
    } else {
      // 最高满减金额校验
      validatorMaxAmountValid(rule, value, callback, froms.discountAmount)
    }

  }

}
// 发放数量校验
const validatorTotalNum = (rule, value, callback) => {
  if (!value) {
    callback(new Error("发放数量为空，请设置发放数量"))
  }
  first(value, callback)
  if (Number(value) < Number(fromData.value.perNum)) {
    callback(new Error("发放数量需大于限领数量"))
  }
  if (Number(value) > 5000 || Number(value) <= 0) {
    callback(new Error("请输入1～5000的整数"))
  }
  callback()
}
// 每人限领数量校验
const validatorPerNum = (rule, value, callback) => {
  if (!value) {
    callback(new Error("每人限领数量为空，请设置每人限领数量"))
  }
  first(value, callback)
  if (Number(value) > 10 || Number(value) <= 0) {
    callback(new Error("请输入大于0，小于11的正整"))
  }
  if (Number(value) > Number(fromData.value.totalNum)) {
    callback(new Error("限量数量不能大于发放数量"))
  }

  callback()
}
// 表单校验
let amountCondition = [
  {
    required: true,
    validator: validatorAmountCondition,
    trigger: "blur",
  },
]
// 减
let discountAmount = [
  {
    required: true,
    validator: validatorAmountCon,
    trigger: "blur",
  },
]
// 最高减
let maxDiscountAmount = [
  {
    required: true,
    validator: validatorMaxAmount,
    trigger: "blur",
  },
]
let discountRate = [
  {
    required: true,
    validator: validatorDiscountRate,
    trigger: "blur",
  },
]
const rules = reactive({
  name: [
    {
      required: true,
      message: "优惠券名称为空，请输入优惠券名称",
      trigger: "blur",
    },
    {
      min: 2,
      message: "请输入4-20个字符",
      trigger: "blur",
    },
  ],
  rangeType: [
    {
      required: true,
      message: "适用范围为空，请设置使用范围",
      trigger: "change",
    },
  ],
  cateIds: [
    {
      required: true,
      message: "请选择课程分类",
      trigger: "change",
    },
  ],
  type: [
    {
      required: true,
      message: "优惠券类型为空，请设置优惠券类型",
      trigger: "change",
    },
  ],
  amountCondition,
  discountAmount,
  maxDiscountAmount,
  discountRate,
  obtainWay: [
    {
      required: true,
      message: "推广方式为空，请设置推广方式",
      trigger: "change",
    },
  ],
  totalType: [
    {
      required: true,
      message: "发放数量为空，请设置发放数量",
      trigger: "change",
    },
  ],
  totalNum: [
    {
      required: true,
      validator: validatorTotalNum,
      trigger: "blur",
    },
  ],
  perNum: [
    {
      required: true,
      validator: validatorPerNum,
      trigger: "blur",
    },
  ],
})
watch("formData", (newValue, oldValue) => {
  switch (newValue.type) {
    case 1:
      // baseRules = {
      //   ...baseRules,
      //   // 每满
      //   amountCondition: [
      //     {
      //       required: true,
      //       validator: validatorAmountCondition,
      //       trigger: "blur",
      //     },
      //   ],
      //   // 减
      //   discountAmount: [
      //     {
      //       required: true,
      //       validator: validatorAmountCon,
      //       trigger: "blur",
      //     },
      //   ],
      //   // 最高减
      //   maxDiscountAmount: [
      //     {
      //       required: true,
      //       validator: validatorMaxAmount,
      //       trigger: "blur",
      //     },
      //   ],
      // };
      Object.assign(rules, {
        amountCondition,
        discountAmount,
        maxDiscountAmount,
        discountRate,
      })

      setTimeout(() => {
        // 添加事件队列很重要
        ruleFormRef.value.validateField([
          "amountCondition",
          "discountAmount",
          "maxDiscountAmount",
        ])
        ruleFormRef.value.clearValidate(["discountRate"])
      }, 0)
      break
    case 2:
      // baseRules = {
      //   ...baseRules,
      //   // 满
      //   amountCondition: [
      //     {
      //       required: true,
      //       validator: validatorAmountCondition,
      //       trigger: "blur",
      //     },
      //   ],
      //   // 折扣
      //   discountRate: [
      //     {
      //       required: true,
      //       validator: validatorDiscountRate,
      //       trigger: "blur",
      //     },
      //   ],
      //   // 最高减
      //   maxDiscountAmount: [
      //     {
      //       required: true,
      //       validator: validatorMaxAmount,
      //       trigger: "blur",
      //     },
      //   ],
      // };
      Object.assign(rules, {
        amountCondition,
        discountAmount,
        maxDiscountAmount,
        discountRate,
      })
      setTimeout(() => {
        // 添加事件队列很重要
        ruleFormRef.value.validateField([
          "amountCondition",
          "discountRate",
          "maxDiscountAmount",
        ])
        ruleFormRef.value.clearValidate(["discountAmount"])
      }, 0)
      break
    case 3:
      // baseRules = {
      //   ...baseRules,
      //   // 最高减
      //   discountAmount: [
      //     {
      //       required: true,
      //       validator: validatorMaxAmount,
      //       trigger: "blur",
      //     },
      //   ],
      // };
      Object.assign(rules, {
        amountCondition,
        discountAmount,
        maxDiscountAmount,
        discountRate,
      })
      setTimeout(() => {
        // 添加事件队列很重要
        ruleFormRef.value.validateField(["discountAmount"])
        ruleFormRef.value.clearValidate([
          "amountCondition",
          "discountRate",
          "maxDiscountAmount",
        ])
      }, 0)
      break
    case 4:
      // baseRules = {
      //   ...baseRules,
      //   // 每满
      //   amountCondition: [
      //     {
      //       required: true,
      //       validator: validatorAmountCondition,
      //       trigger: "blur",
      //     },
      //   ],
      //   // 减
      //   discountAmount: [
      //     {
      //       required: true,
      //       validator: validatorAmountCon,
      //       trigger: "blur",
      //     },
      //   ],
      // };
      Object.assign(rules, {
        amountCondition,
        discountAmount,
        maxDiscountAmount,
        discountRate,
      })
      setTimeout(() => {
        // 添加事件队列很重要
        ruleFormRef.value.validateField(["amountCondition", "discountAmount"])
        ruleFormRef.value.clearValidate(["maxDiscountAmount", "discountRate"])
      }, 0)
      break
    default:
      break
  }
})

// // 监听修改金额数值，小数点后保留2位
watch(fromData, (newValue, oldValue) => {
  // 课程名称默认的字数
  nextTick(() => {
    const discountVal = newValue.discountAmount //减满金额
    const maxDiscountVal = newValue.maxDiscountAmount //最高减
    const discountRate = newValue.discountRate //折扣
    // 减满金额要小于10000
    if (discountVal < 10000 && discountVal > 0) {
      fromData.value.discountAmount = parseInt(discountVal * 100) / 100
    }
    if (maxDiscountVal < 10000 && maxDiscountVal > 0) {
      fromData.maxDiscountAmount = parseInt(maxDiscountVal * 100) / 100
    }
    if (discountRate < 11 && discountRate > -1) {
      fromData.discountRate = parseInt(discountRate * 10) / 10
    }
  })
})
//控制指定数量是否输入了小数点和负数
const inputTotalNum = (e) => {
  fromData.value.totalNum = inputNum(e)
}
////控制没人限领量量是否输入了小数点和负数
const inputPerNum = (e) => {
  fromData.value.perNum = inputNum(e)
}
// ------生命周期------


onMounted(() => {
  getTypeList() //获取课程分类
})
// 获取课程分类
const getTypeList = () => {
  let cateTree = store.getCategoryTree;
  let cateMap = store.getCategoryMap;
  typeData.value = cateTree;
  typeMap.value = cateMap;
  courseId.value = route.params.id
  if (courseId.value !== "null") {
    getDetailData()
  }
}
// 获取详情
let getDetailData = () => {
  getDetails(courseId.value)
    .then((res) => {
      if (res.code === 200) {
        let {discountType, discountValue, maxDiscountAmount, termDays, thresholdAmount, userLimit, ... data} = res.data;
        data.perNum = userLimit;
        data.rangeType = res.data.scopes ? 1 : 0;
        if(data.rangeType){
          let scopes = res.data.scopes;
          // 遍历课程分类，与后端给的三级id做匹配，重新构建数据结构，回显在页面
          let arr = []
          scopes.forEach(s => {
            let cm = typeMap.value;
            let c3 = cm[s.id];
            let c2 = cm[c3.parentId];
            let c1 = cm[c2.parentId];
            arr.push([c1.id,c2.id,c3.id])
          })
          data.cateIds = arr
        }
        data.type = discountType;
        data.amountCondition =
          Number(thresholdAmount) / 100 //满元

        data.maxDiscountAmount =
          Number(maxDiscountAmount) / 100 //最高减
        if(discountType === 2){
          // 折扣
          data.discountRate = discountValue / 10
        }else{
          data.discountAmount =
              Number(discountValue) / 100 //最高金额
        }
        // 推广方式
        fromData.value = data;
      }
    })
    .catch((err) => console.log(err))
}
// ------定义方法------
// 保存
const handleSubmit = async (str) => {
  let {...froms} = fromData.value
  let scopes = []
  // 取分类三级的ids
  if (froms.cateIds !== undefined) {
    froms.cateIds.map((val) => {
      if (val[2] !== undefined) {
        scopes.push(val[2])
      }
    })
  }
  // 校验数据
  const valid = await ruleFormRef.value.validate()
  // 校验成功
  if (valid) {
    //  获取购买周期
    let parent = {
      id: froms.id, //添加的时候id会是空，编辑直接传优惠券id
      name: froms.name, //优惠券名称
      specific: Number(froms.rangeType) > 0, //使用范围
      scopes, //限定范围
      discountType: froms.type, //优惠券类型
      thresholdAmount: Number(froms.amountCondition) * 100, //满元
      discountValue: Number(froms.discountAmount) * 100 || Number(froms.discountRate) * 10, //最高或者打折
      maxDiscountAmount: Number(froms.maxDiscountAmount) * 100, //最高减
      // startTime: froms.dateReceivePicker[0], //领取开始时间
      // endTime: froms.dateReceivePicker[1], //领取结束时间
      // termStartTime: froms.dateUsePicker[0], //使用开始时间
      // termEndTime: froms.dateUsePicker[1], //使用结束时间
      obtainWay: froms.obtainWay, //推广方式
      totalNum:  Number(froms.totalNum), //如果发放数量是无限量，指定数量是0，否则是指定数量
      userLimit: Number(froms.perNum), //每人限领
    }
    let commitForm = parent.id ? updateCoupon : saveMarket;
    await commitForm(parent)
      .then((res) => {
        if (res.code === 200) {
          ElMessage({

            message: "恭喜你，操作成功！",
            type: "success",
            showClose:false,
          })
          ruleFormRef.value.resetFields() //清空表单数据
          fromData.value.cateIds = []
          nameNumVal.value = 0
          // 保存并返回跳转
          if (str === "getback") {
            router.push({
              path: "/marketing/index",
            })
          } else {
            // 保存并继续跳转
            router.push({
              path: `/marketing/add/null`,
            })
          }
        } else {
          ElMessage({
            message: res.data.msg,
            type: "error",
            showClose:false,
          })
        }
      })
      .catch((err) => { console.log(err)})
  }
}

// 名字控制20个字符
const nameTextInput = () => {
  nextTick(() => {
    let data = fromData.value
    const value = validateTextLength(data.name, 20)
    data.name = value.val
    nameNumVal.value = value.numVal
  })
}
//选择优惠券类型时清空金额
const handleType = () => {
  // 清除校验
  ruleFormRef.value.clearValidate(["amountCondition", "discountAmount", "maxDiscountAmount",
    "discountRate",])
  fromData.value.amountCondition = ""
  fromData.value.discountAmount = ""
  fromData.value.maxDiscountAmount = ""
  fromData.value.discountRate = ""
}

//根据推广类型来判断，如果是指定发放，默认选中指定数量，无限量置灰
const handWay = (val) => {
  if (val === 2) {
    fromData.value.totalType = "2"
  }
}
// 向父组件暴露方法
defineExpose({
  handleSubmit,
});
</script>
<style lang="scss" scoped>
.el-input{
  .el-input{
    .el-input__wrapper{
      .el-input__inner{
        color: #000;
      }
  }
  }
}

</style>