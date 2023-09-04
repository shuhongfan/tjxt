 // 转换 A、B、C、D
export const upperAlpha = (num, type) => {
  let str = ''
  let n = type && type == 0 ? num : num - 1
  switch(n){
    case 0 :
      str = 'A';
      break
    case 1 :
      str = 'B';
      break
    case 2 :
      str = 'C';
      break
    case 3 :
      str = 'D';
      break
    case 4 :
      str = 'E';
      break
  }
  return str
}
// 时间转换 h:m:s
export const timeFormat = (time) => {
    //  秒
    let second = parseInt(time)
    //  分
    let minute = '00'
    //  小时
    let hour = '00'

    if (second > 60) {
        //  获取分钟，除以60取整数，得到整数分钟
        minute = parseInt(second / 60)
        //  获取秒数，秒数取佘，得到整数秒数
        second = parseInt(second % 60)
        //  如果分钟大于60，将分钟转换成小时
        if (minute > 60) {
          //  获取小时，获取分钟除以60，得到整数小时
          hour = parseInt(minute / 60)
          //  获取小时后取佘的分，获取分钟除以60取佘的分
          minute = parseInt(minute % 60)
        }
      }
    return `${hour} : ${minute} : ${second}`
}

export const amountConversion = (item) => {
  let amount = '0'
  if (item){
    amount = (item / 100).toFixed(2)
  } 
  return amount
}