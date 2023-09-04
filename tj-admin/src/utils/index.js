// 文字字节数限制
export const validateTextLength = (value, maxLimitStr) => {
  // 中文、中文标点、全角字符按1长度，英文、英文符号、数字按0.5长度计算
  let cnReg = /([\u4e00-\u9fa5]|[\u3000-\u303F]|[\uFF00-\uFF60])/;
  let substringStr = "";
  let length = 0;
  let strArr = value.split("");
  let obj = {};
  strArr.map((str) => {
    if (cnReg.test(str)) {
      //中文
      length++;
    } else {
      //英文
      length += 0.5;
    }
    //大于最大限制的时候
    if (length > maxLimitStr) {
      substringStr = substringStr;
    } else {
      substringStr += str;
    }
  });
  if (length > maxLimitStr) {
    length = maxLimitStr;
  }
  // nameNumVal.value = Math.floor(length);
  return (obj = {
    numVal: Math.floor(length),
    val: substringStr,
  });
};
// 文字字节数限制,占位均为1
export const validateLength = (value, maxLimitStr) => {
  // 中文、中文标点、全角字符按1长度，英文、英文符号、数字按0.5长度计算
  let cnReg = /([\u4e00-\u9fa5]|[\u3000-\u303F]|[\uFF00-\uFF60])/;
  let substringStr = "";
  let length = 0;
  let strArr = value.split("");
  let obj = {};
  strArr.map((str) => {
    if (cnReg.test(str)) {
      //中文
      length++;
    } else {
      //英文
      length++;
    }
    //大于最大限制的时候
    if (length > maxLimitStr) {
      substringStr = substringStr;
    } else {
      substringStr += str;
    }
  });
  if (length > maxLimitStr) {
    length = maxLimitStr;
  }
  // nameNumVal.value = Math.floor(length);
  return (obj = {
    numVal: Math.floor(length),
    val: substringStr,
  });
};
// ==== isNumber  函数====
const toString = Object.prototype.toString;
export function is(val, type) {
  return toString.call(val) === `[object ${type}]`;
}
export function isNumber(val) {
  return is(val, "Number");
}

// ==== buildShortUUID  函数====
export function buildShortUUID(prefix = "") {
  const time = Date.now();
  let unique = 0;
  const random = Math.floor(Math.random() * 1000000000);
  unique++;
  return prefix + "_" + random + unique + String(time);
}
// ==== 去除html标签
export function removeHTMLTag(str) {
  str = str.replace(/<\/?[^>]*>/g, ""); //去除HTML tag
  str = str.replace(/[ | ]*\n/g, "\n"); //去除行尾空白
  //str = str.replace(/\n[\s| | ]*\r/g,'\n'); //去除多余空行
  str = str.replace(/ /gi, ""); //去掉
  return str;
}
// 秒转时分秒
export function formatSeconds(value) {
  var theTime = parseInt(value); // 秒
  var theTime1 = 0; // 分
  var theTime2 = 0; // 小时
  if (theTime > 60) {
    theTime1 = parseInt(theTime / 60);
    theTime = parseInt(theTime % 60);
    if (theTime1 > 60) {
      theTime2 = parseInt(theTime1 / 60);
      theTime1 = parseInt(theTime1 % 60);
    }
  }
  // 时分秒小于10的在前面补零
  if (theTime < 10) {
    theTime = "0" + theTime;
  } else {
    theTime = theTime;
  }
  if (theTime2 < 10) {
    theTime2 = "0" + theTime2;
  } else {
    theTime2 = theTime2;
  }
  if (theTime1 < 10) {
    theTime1 = "0" + theTime1;
  } else {
    theTime1 = theTime1;
  }
  var result = theTime2 + ":" + theTime1 + ":" + theTime;
  return result;
}

// 数字对应的英文字母
export function numLetter(val) {
  let alphabet = String.fromCharCode(64 + parseInt(val));
  return alphabet;
}
//钱数自动补充两位小数
export function decimalsReplenish(value) {
  if (!value) return "0.00";
  value = value.toFixed(2);
  var intPart = Math.trunc(value); // 获取整数部分
  var intPartFormat = intPart.toString().replace(/(\d)(?=(?:\d{3})+$)/g, "$1,"); // 将整数部分逢三一断
  // 去除，分隔符
  if (intPartFormat.indexOf(",") !== -1) {
    intPartFormat = intPartFormat.split(",").join("");
  }
  var floatPart = ".00"; // 预定义小数部分
  var value2Array = value.split(".");
  // =2表示数据有小数位
  if (value2Array.length === 2) {
    floatPart = value2Array[1].toString(); // 拿到小数部分
    if (floatPart.length === 1) {
      return intPartFormat + "." + floatPart + "0";
    } else {
      return intPartFormat + "." + floatPart;
    }
  } else {
    return intPartFormat + floatPart;
  }
}
// 禁止输入特殊符号
export function forbidSymbol(val) {
  let num = val.replace(
    /[`~!@#$%^&*()_\-+=<>?:"{}|,.\/;'\\[\]·~！@#￥%……&*（）——\-+={}|《》？：“”【】、；‘’，。、]/g,
    ""
  );
  return num;
}
// 只能输入正整数
export const inputNum = (val) => {
  return val.replace(/^(0+)|[^\d]+/g, "");
};
// 普通时间格式化
export const formatTimeOrdinary = (data) => {
  if(data!==undefined){
    return time(data)
  }
  
}
// 表格时间格式化
export const formatTime = (row, column) => {
  let data = row[column.property];
  return time(data)
};
const time=(data)=>{
  if (data == null) {
    return null;
  }
  let dt = new Date(data);
  let month = dt.getMonth() + 1 //月
  let date = dt.getDate()  //日
  let hours = dt.getHours()  //时
  let minutes = dt.getMinutes()  //分
  let seconds = dt.getSeconds()  //秒
  if(month<10){
    month='0'+month
  }else{
    month=month
  }
  if(date<10){
    date='0'+date
  }else{
    date=date
  }
  if(hours<10){
    hours='0'+hours
  }else{
    hours= hours
  }
  if(minutes<10){
    minutes='0'+minutes
  }else{
    minutes= minutes
  }
  if(seconds<10){
    seconds='0'+seconds
  }else{
    seconds= seconds
  }
  return (
    dt.getFullYear() +
    "." +
    month +
    "." +
    date +
    " " +
    hours +
    ":" +
    minutes +
    ":" +
    seconds
  );
}
export const formatYear = (dates) => {
  var date = new Date(new Date());
  var y = date.getFullYear();
  var m = date.getMonth() + 1;
  m = m < 10 ? "0" + m : m;
  var d = date.getDate();
  d = d < 10 ? "0" + d : d;
  const time = y + "-" + m + "-" + d;
  return time;
};
// 字数显示...
export const ellipsis = (value,num) => {
  if (!value) return "";
  // 中文、中文标点、全角字符按1长度，英文、英文符号、数字按0.5长度计算
  let cnReg = /([\u4e00-\u9fa5]|[\u3000-\u303F]|[\uFF00-\uFF60])/;
  let substringStr = "";
  let length = 0;
  let strArr = value.split("");
  let obj = {};
  strArr.map((str) => {
    if (cnReg.test(str)) {
      //中文
      length++;
    } else {
      //英文
      length += 0.5;
    }
    //大于最大限制的时候
  });
  if (length > num) {
    return value.slice(0, num) + "...";
  }
  return value;
};

// input为number类型，禁止输入e/-/+
export const channelInputLimit = (e) => {
  let key = e.key;
  // 不允许输入'e'和'-/+'
  if (key === "e" || key === "-" || key === "+") {
    e.returnValue = false;
    return false;
  }
  return true;
};
// 禁止输入空格
export const channelInputKeyCode = (e) => {
  let keyCode = e.keyCode;
  // 不允许输入'e'和'-/+'
  if (keyCode === 32) {
    e.returnValue = false;
    return false;
  }
  return true;
};
