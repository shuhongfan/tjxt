<template>
  <div class="box" ref="EcharRef" style="height: 455px; min-width: 710px; width: 100%" ></div>
</template>

<script setup>
import { ref, onMounted, reactive, watch } from "vue"
import * as echarts from "echarts"
import { getGrantInfo } from "@/api/main"
import { get } from "lodash"

// 创建一个数组，用于保存数据和监听器的监听，内容为数组amount，count，amount
const watchArr = reactive({
  amount: {}, // 订单金额
  count: {}, // 订单数量
  price: {},  //客单价
})
// 查询参数
const params = reactive({
  types: '2,3,5',
})

// 数组标题
const watchArr2 = reactive({
  data: {},
  data2: {},
})

const legendData = ["订单金额", "订单笔数", "客单价"]
const selectedData =  ref({'客单价': false, '订单金额': true, '订单笔数': true, })

// 订单金额所取的y轴，内容为数字0
const yAxisIndex = ref({amount: 0, count: 1,price: 1})
// 创建一个erchart柱状图
const EcharRef = ref(null)
// 定义图表的option
const option = ref(null)
let charts = null
// 生命周期
onMounted(() => {
  getOrder()
})

// 初始化图表
const init = ()=>{
  // 初始化图表
  const myChart = echarts.init(EcharRef.value)
  // 配置options
  option.value = setOptionHandle(watchArr2)
  // 监听legend点击
  myChart.on('legendselectchanged', legendselectHandle)
  // 绘制图表
  myChart.setOption(option.value)
  // 监听浏览器变动 调整图表大小
  window.addEventListener('resize', function () {
    myChart.resize()
  })
  charts = myChart
}
// 配置options
const setOptionHandle = item => {
  console.log(item)
  const options = {
    tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'line'
        },
        backgroundColor: 'rgba(255,255,255,0.8)', 
        formatter: function (params) {
          let res = '<div style="font-family: HelveticaNeue;font-size: 12px;color: #887E7E;letter - spacing: -0.2px; margin-top:6px;padding-right:19px;">' + '2022.' + params[0].name 
          for (let i = 0; i < params.length; i++) {
            let color = params[i].color // 获取当前点的颜色
            // 创建数组，用于存放各项数据的最大值和最小值
            let arr = {
              // maxamount的内容为watchArr.amount.series[0].max
              maxamount: get(watchArr.amount, "series.max"),
              minamount: get(watchArr.amount, 'series.min'),
              maxcount: get(watchArr.count, 'series.max'),
              mincount: get(watchArr.count, 'series.min'),
              maxprice: get(watchArr.price, 'series.max'),
              minprice: get(watchArr.price, 'series.min'),
            }
          res += '<div style="font-family:PingFangSC-Regular;font-size: 12px;color: #332929;letter - spacing: -0.2px;width:236px;margin-top:5px;margin-bottom:15px">' + `<span style="width: 5px;height: 5px;background: ${color} ;display:inline-block;border-radius: 50%;overflow:hidden;;margin-bottom:4px;margin-right:7px"> + </span>` + params[i].seriesName + ' : ' + params[i].value + `<span>${params[i].seriesName == legendData[0] ? '元' : params[i].seriesName == legendData[1] ? '笔' : params[i].seriesName == legendData[2] ? '元': ''}<span>` +'<br/>' + `<span style="display:flex;justify-content: space-between;margin-top:6px;"><span>峰值${params[i].seriesName == legendData[0] ? arr.maxamount : params[i].seriesName == legendData[1] ? arr.maxcount : arr.maxprice}</span><span>谷值${params[i].seriesName == legendData[0] ? arr.minamount : params[i].seriesName == legendData[1] ? arr.mincount : arr.minprice}</span></span>`
            + '</div>'+ '</div>'
          }
          return res
        }
      },
    legend: {
      itemGap: 30,
      data: legendData,
      selectedMode: 'multiple',
      selected: selectedData.value
    },
    xAxis: {
      data: watchArr.amount.xaxis.data,
      axisTick: {
        show: false,
      },
      axisLine: {
        lineStyle: {
          color: '#E5E5E5'
        }
      },
      // 文字颜色为#332929，线段颜色为#E5E5E5
      axisLabel: {
        color: '#332929',
        fontSize: 12,
        fontFamily: 'HelveticaNeue',
      },
    },
    yAxis:[
        {
          type: item.data.yaxis.type,
          name: "金额（元）",
          min: parseInt(item.data.yaxis.min),
          max: parseInt(item.data.yaxis.max),
          average: parseInt(item.data.yaxis.average),
          interval: item.data.yaxis.interval,
          nameLocation: 'end',
          nameTextStyle: {
            padding: [0, 50, 0, 0],
            fontSize: 12,
            color: '#887E7E',
          },
          axisLabel: {
            color: '#332929',
            fontSize: 12,
            fontFamily: 'HelveticaNeue',
            formatter: "{value} "
          }
        },
        {
          type: item.data2.yaxis.type,
          name: "笔数（笔）",
          min: parseInt(item.data2.yaxis.min),
          max: parseInt(item.data2.yaxis.max),
          average: parseInt(item.data2.yaxis.average),
          interval: item.data2.yaxis.interval,
          nameLocation: 'end',
          // 位置和偏移量
          nameTextStyle: {
            padding: [0, 0, 0, 50],
            fontSize: 12,
            color: '#887E7E',
          },
          axisLabel: {
            color: '#332929',
            fontSize: 12,
            fontFamily: 'HelveticaNeue',
            formatter: "{value} "
          }
        },
      ],
    series: [
      {
          name: "订单金额",
          type: "bar",
          yAxisIndex: yAxisIndex.value.amount,
          barWidth: 16,
          data: watchArr.amount.series.data,
          legendHoverLink: false,
          itemStyle: {
              borderRadius: [8, 8, 0, 0],
              color: "#FF734F"
          },
          markLine: {
            silent: true,
            data: [
              {
                yAxis: watchArr.amount.yaxis.average,
                label: {
                  show: true,
                  position: 'middle',
                  formatter: watchArr.amount.yaxis.average
                }
              }
            ]
          },
        },
        {
          name: "客单价",
          type: "bar",
          barGap: '-100%',
          yAxisIndex: yAxisIndex.value.price,
          barWidth: 16,
          data: watchArr.price.series.data,
          legendHoverLink: false,
          itemStyle: {
              borderRadius: [8, 8, 0, 0],
              color: "#FFE1D9"
          },
          markLine: {
            silent: true,
            data: [
              {
                yAxis: watchArr.price.yaxis.average,
                label: {
                  show: true,
                  position: 'middle',
                  formatter: watchArr.price.yaxis.average
                }
              }
            ]
          },
        },


        {
          name: "订单笔数",
          type: "line",
          yAxisIndex: yAxisIndex.value.count,
          data: watchArr.count.series.data,
          legendHoverLink: false,
          itemStyle: {
            normal: {
              color: '#FFB057'
            }
          },
          markLine: {
            silent: true,
            data: [
              {
                yAxis: watchArr.count.yaxis.average,
                label: {
                  show: true,
                  position: 'middle',
                  formatter: watchArr.count.yaxis.average
                }
              }
            ]
          },
        },
      ]
  }
  return options
}

// 定义ercharts点击事件，限制最多同时选中两个 
// 拼装一个数组，内容为legendData中的第二项和第三项
/*
 * 老值  [a, b]              -- arrTag
 * 上一次改变的值 a           -- tag
 * 新值 c                    --  params.name   
 * 新值 + 老值  [a,b,c]      --  params.selected
 * 
 * 
 */
const arrTag = ref(["订单金额", "订单笔数"])  // 上一次的值
const tag = ref('订单金额') // 上一次更新的值
// legend点击触发事件
function legendselectHandle(params){
    // 如果是取消 
    if (!params.selected[params.name]){
      const num = arrTag.value.indexOf(params.name)
      arrTag.value.splice(num, 1)
      setEcharts(params, arrTag.value, 'reduce')
    } else {
      if (arrTag.value.length == 0){
        arrTag.value.push(params.name)
      } else if (arrTag.value.length == 1){
        arrTag.value.push(params.name)
      } else {
        const inda = arrTag.value.indexOf('访问量')
        const indb = arrTag.value.indexOf('订单金额')
        if(inda != -1){
          arrTag.value[inda] = params.name
        } else if (indb != -1) {
          arrTag.value[indb] = params.name
        } else {
          const num = arrTag.value.indexOf(tag.value)
          arrTag.value[1-num] = params.name
        }
      }
      setEcharts(params, arrTag.value, '')
    }
  }
// 调整 selectedData, yAxisIndex, watchArr2 重置 图标   
const setEcharts = (params, arrTag, type) => {
    // 记录本次更新的值和对应的轴
    tag.value = params.name
    // arrTag ['订单笔数', '新增学员' ]
    // yAxisIndex: {  }
    // 重置Y轴对应关系 并配置对应的数据
    // dataArr 数据
    let sl = {'客单价': false, '订单金额': false, '订单笔数': false}
    arrTag.forEach((val, ind) => {
      sl[val] = true
      ind == 0 ? watchArr2.data = watchArr[filterTag(val)] : watchArr2.data2 = watchArr[filterTag(val)]
    })
    selectedData.value = sl
    yAxisIndex.value[filterTag(watchArr2.data.series.name.split(' ')[0])] = 0
    yAxisIndex.value[filterTag(watchArr2.data2.series.name.split(' ')[0])] = 1
    // 配置options
    option.value = setOptionHandle(watchArr2)
    // 如果是减的话 不重绘 图表
    if(type == 'reduce'){
      return 
    } 
    // 重置图表
    charts.setOption(option.value)
}  
// 数据转换
const filterTag = (item) => {
  let stCatch = ''
    switch (item){
      case '订单金额' : 
        stCatch = 'amount'
        break
      case '订单笔数' : 
        stCatch = 'count'
        break
      case '客单价' : 
        stCatch = 'price'
        break
    }
    return stCatch
}  
// 获取订单金额
const getOrder = async () => {
  await getGrantInfo(params)
    .then(res => {
      if (res.code === 200) {
        watchArr.amount.series = res.data.series[0]
        watchArr.amount.yaxis = res.data.yaxis[0]
        watchArr.amount.xaxis = res.data.xaxis[0]
        watchArr.count.series = res.data.series[1]
        watchArr.count.yaxis = res.data.yaxis[1]
        watchArr.count.xaxis = res.data.xaxis[1]
        watchArr.price.series = res.data.series[2]
        watchArr.price.yaxis = res.data.yaxis[2]
        watchArr.price.xaxis = res.data.xaxis[2]
        watchArr2.data = watchArr.amount
        watchArr2.data2 = watchArr.count
        init()
      }
    })
}
</script>

<style lang="scss" scoped>
</style>