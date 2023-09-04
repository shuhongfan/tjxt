// 手机校验
export function validatePhone (rule, value, callback) {
  const reg = /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/
  if (value === "" || value === undefined || value === null) {
    if (rule.required) {
      callback("教师手机号为空，请输入教师手机号")
    } else {
      callback()
    }
  } else {
    if (!reg.test(value) && value !== "") {
      callback("教师手机号格式错误，请重新输入")
    } else {
      callback()
    }
  }
}
// 用户手机校验
export function validateuserPhone (rule, value, callback) {
  const reg = /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/
  if (value === "" || value === undefined || value === null) {
    if (rule.required) {
      callback("后台用户手机号为空，请输入后台用户手机号")
    } else {
      callback()
    }
  } else {
    if (!reg.test(value) && value !== "") {
      callback("后台用户手机号格式错误，请重新输入")
    } else {
      callback()
    }
  }
}
// 学生手机校验
export function validatestudentsPhone (rule, value, callback) {
  const reg = /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/
  if (value === "" || value === undefined || value === null) {
    if (rule.required) {
      callback("学员手机号为空，请输入学员手机号")
    } else {
      callback()
    }
  } else {
    if (!reg.test(value) && value !== "") {
      callback("学员手机号格式错误，请重新输入")
    } else {
      callback()
    }
  }
}
// 只能输入中英文
// 教师
export const validateContacts = (rule, value, callback) => {
  const reg = /[^\a-\z\A-\Z\u4E00-\u9FA5]$/g
  if (value === "" || value === undefined || value == null) {
    callback(new Error("教师名称为空，请输入教师名称"))
  } else {
    if (reg.test(value)) {
      callback(new Error("教师名称格式错误，请输入中、英文"))
    } else {
      callback()
    }
  }
}
// 员工
export const validatestaffs = (rule, value, callback) => {
  const reg = /[^\a-\z\A-\Z\u4E00-\u9FA5]$/g
  if (value === "" || value === undefined || value == null) {
    callback(new Error("员工名称为空，请输入员工名称"))
  } else {
    if (reg.test(value)) {
      callback(new Error("员工名称格式错误，请输入中、英文"))
    } else {
      callback()
    }
  }
}
// 学生
export const validatestudents = (rule, value, callback) => {
  const reg = /[^\a-\z\A-\Z\u4E00-\u9FA5]$/g
  if (value === "" || value === undefined || value == null) {
    callback(new Error("学员名称为空，请输入学员名称"))
  } else {
    callback()
  }
}
// 后台用户
export const validateuser = (rule, value, callback) => {
  const reg = /[^\a-\z\A-\Z\u4E00-\u9FA5]$/g
  if (value === "" || value === undefined || value == null) {
    callback(new Error("后台用户名称为空，请输入后台用户名称"))
  } else {
    // if (reg.test(value)) {
    //   callback(new Error("后台用户名称格式错误，请输入中文、英文"))
    // } 
    // else {
    callback()
    // }
  }
}
// 只能输入中、英文、数字
export const validateContent = (rule, value, callback) => {
  const reg = /[^\a-\z\A-\Z0-9\u4E00-\u9FA5]$/g
  if (value === "" || value === undefined || value == null) {
    callback(new Error("岗位为空，请输入岗位名称"))
  } else {
    if (reg.test(value)) {
      callback(new Error("岗位名称格式错误，请输入中英文、数字"))
    } else {
      callback()
    }
  }
}
// 倒计时
export const timeCountdown = (obj) => {
  // obj包括timer、times show
  const TIME_COUNT = 60 // 默认倒计时秒数
  if (!obj.timer) {
    obj.times = TIME_COUNT
    obj.show = false
    obj.timer = setInterval(() => {
      if (obj.times > 0 && obj.times <= TIME_COUNT) {
        obj.times--
      } else {
        obj.show = true
        clearInterval(obj.timer) // 清空定时器
        obj.timer = null
      }
    }, 1000)
  }
  return {
    timer: obj.timer,
    show: obj.show,
    times: obj.times,
  }
}
// 身份证校验
export const validateIdentityCard = (value) => {
  const accountreg = /(^\d{15}$)|(^\d{17}(\d|X|x)$)/g
  let tipVal = ""
  if (value === undefined || value === "") {
    return "请输入身份证号"
  } else if (!accountreg.test(value)) {
    return "身份证长度或格式错误"
  } else {
    return true
  }
}
export const indexValidate = (rule, value, callback) => {
  if (value === "" || value === undefined || value == null) {
    callback(new Error("分类序号不能为空"))
  } else {
    const re = /^[1-9][0-5]{0,1}$/
    const rsCheck = re.test(value)
    if (!rsCheck) {
      callback(new Error("请输入1-15的数字"))
    } else {
      callback()
    }
  }
}
// 分值只能输入正整数
export const isPositiveInteger = (rule, value, callback) => {
  if (value === undefined) {
    return callback(new Error("分值为空，请设置分值"))
  }
  if (!Number(value)) {
    callback(new Error("请设置1～50的整数"))
  } else {
    const re = /(^[1-9]\d*$)/
    const rsCheck = re.test(value)
    if (!rsCheck) {
      callback(new Error("请设置1～50的整数"))
    } else {
      callback()
    }
  }
}
// 解决首位是0的问题
export const first = (value, callback) => {
  if (value !== "") {
    // 以上已经过滤，此处控制的是如果没有小数点，首位不能为类似于 01、02的金额
    // toString()转换成字符串
    if (value.toString().substr(0, 1) === "0" && value.length === 2) {
      callback(new Error("格式错误，请重新输入"))
    }
  }
}
// 价格校验
export const validatorPrice = (rule, value, callback) => {
  if (!value) {
    return callback(new Error("课程价格为空，请输入课程价格"))
  }
  if (value > 100000 || value <= 0) {
    callback(new Error("课程价格需在0.01～100000.00元，请重新输入"))
  }
  if (!Number(value)) {
    callback(new Error("课程价格格式错误，请重新输入"))
  } else {
    const reg = /(^\d+\.?\d{0,2}$)/
    const rsCheck = reg.test(value)
    if (!rsCheck) {
      callback(new Error("课程价格格式错误，请重新输入"))
    } else {
      callback()
    }
  }
}
// 金额校验
const amount = (value, callback) => {
  if (value) {
    first(value, callback)
  }
  if (!Number(value)) {
    callback(new Error("金额格式错误，请重新输入"))
  } else {
    const re = /(^\d+\.?\d{0,2}$)/
    const rsCheck = re.test(value)
    if (!rsCheck) {
      callback(new Error("金额格式错误，最多两位小数"))
    } else {
      callback()
    }
  }
}
// 每满
export const amountConditionValid = (rule, value, callback) => {
  if (!value) {
    return callback(new Error("满减金额为空，请输入满减金额"))
  }
  if (value >= 10000 || value <= 0) {
    callback(new Error("金额需大于0元, 小于10000.00元，请重新输入"))
  }

  amount(Number(value), callback)
}
// 减
export const discountAmountValid = (rule, value, callback, num) => {
  if (!value) {
    return callback(new Error("优惠金额为空，请输入优惠金额"))
  }
  if (value >= 10000 || value <= 0) {
    callback(new Error("金额需大于0元, 小于10000.00元，请重新输入"))
  }
  if (Number(value) >= Number(num)) {
    callback(new Error("优惠金额不能大于/等于满减金额"))
  }
  amount(Number(value), callback)
}
// 最高减
export const validatorMaxAmountValid = (rule, value, callback, num) => {
  if (!value) {
    return callback(new Error("最高满减金额为空，请输入最高满减金额"))
  }
  if (value >= 10000 || value <= 0) {
    callback(new Error("金额需大于0元, 小于10000.00元，请重新输入"))
  }
  if (Number(value) < Number(num)) {
    callback(new Error("最高满减金额需大于/等于满减金额"))
  }
  amount(Number(value), callback)
}
// 每满金额减去打折后的价格，最高满减金额不能小于折扣后的价格 校验
export const discountRateValid = (rule, value, callback, num) => {
  if (!value) {
    return callback(new Error("最高满减金额为空，请输入最高满减金额"))
  }
  if (value >= 10000 || value <= 0) {
    callback(new Error("金额需大于0元, 小于10000.00元，请重新输入"))
  }
  if (Number(value) < num) {
    callback(new Error("最高满减金额不能小于折扣后的价格"))
  } else {
    callback()
  }
  amount(Number(value), callback)
}
// 无门榄
export const thresholdValid = (rule, value, callback, num) => {
  if (!value) {
    return callback(new Error("无门槛金额为空，请输入无门槛金额"))
  }
  if (value >= 10000 || value <= 0) {
    callback(new Error("金额需大于0元, 小于10000.00元，请重新输入"))
  }
  amount(Number(value), callback)
}
// 校验折扣
export const validatorDiscountRate = (rule, value, callback) => {
  if (!value) {
    return callback(new Error("折扣为空，请输折扣"))
  }
  if (value >= 10 || value <= 0.01) {
    callback(new Error("折扣需大于0，小于10.0"))
  }
  if (!Number(value)) {
    callback(new Error("折扣格式错误，请重新输入"))
  } else {
    const re = /(^\d+\.?\d{0,1}$)/
    const rsCheck = re.test(value)
    if (!rsCheck) {
      callback(new Error("折扣格式错误，最多一位小数"))
    } else {
      callback()
    }
  }
}
// 学习有效期校验
export const validatorValidDuration = (rule, value, callback) => {
  if (!value) {
    return callback(new Error("学习有效期为空，请设置学习有效期"))
  }
  first(value, callback)
  const re = /(^[1-9]\d*$)/
  const rsCheck = re.test(value)
  if (value <= 0 || value > 99) {
    callback(new Error("请输入1～99的正整数"))
  } else if (!rsCheck) {
    callback(new Error("格式错误，请重新输入"))
  } else {
    callback()
  }
}
// 结束时间应该晚于(大于)开始时间
export const validatorTime = (rule, value, callback, time, text) => {
  if (!value) {
    callback(new Error("领用期限为空，请设置领用期限"))
  } else {
    let start = new Date(time).getTime() //开始时间戳
    let end = new Date(value).getTime() //结束时间戳
    if (start === end) {
      callback(new Error(text))
    } else {
      callback()
    }
  }
}
