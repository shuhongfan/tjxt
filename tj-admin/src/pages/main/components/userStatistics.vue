<template>
  <div class="box" ref="EcharRef" style="height: 460px; min-width: 710px"></div>
</template>

<script setup>
import { ref, onMounted, reactive } from "vue"
import * as echarts from "echarts"
import { getGrantInfo } from "@/api/main"
import { get } from "lodash"

// 创建一个数组，用于保存数据和监听器的监听，内容为数组students，newstudents，users
const watchArr = reactive({
  students: {}, // 学员总数
  newstudents: {}, // 新增学员
  users: {},  //日活跃用户数
})
// 查询参数
const params = reactive({
  types: '6,4,7',
})

// 数组标题
const watchArr2 = reactive({
  data: {},
  data2: {},
})

const legendData = ["学员总数", "新增学员", "日活跃用户数"]
const selectedData =  ref({ '学员总数': true, '新增学员': true, '日活跃用户数': false})

// 订单金额所取的y轴，内容为数字0
const yAxisIndex = ref({students: 0, newstudents: 1, users: 1})
// 创建一个erchart柱状图
const EcharRef = ref(null)
// 定义图表的option
const option = ref(null)
let charts = 
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
          let color = params[i].color
          let arr = {
            maxstudents: get(watchArr.students, "series.max"),
            minstudents: get(watchArr.students, "series.min"),
            maxnewstudents: get(watchArr.newstudents, "series.max"),
            minnewstudents: get(watchArr.newstudents, "series.min"),
            maxusers: get(watchArr.users, "series.max"),
            minusers: get(watchArr.users, "series.min"),
          }
          res += '<div style="font-family:PingFangSC-Regular;font-size: 12px;color: #332929;letter - spacing: -0.2px;width:236px;margin-top:5px;margin-bottom:15px">' + `<span style="width: 5px;height: 5px;background: ${color} ;display:inline-block;border-radius: 50%;overflow:hidden;;margin-bottom:4px;margin-right:7px"> + </span>` + params[i].seriesName + ' : ' + params[i].value + `<span>${params[i].seriesName == legendData[0] ? '人' : params[i].seriesName == legendData[1] ? '人' : params[i].seriesName == legendData[2] ? '个' : ''}<span>` + '<br/>' + `<span style="display:flex;justify-content: space-between;margin-top:6px;"><span>峰值：${params[i].seriesName == legendData[0] ? arr.maxstudents : params[i].seriesName == legendData[1] ? arr.maxnewstudents : arr.maxusers}</span><span>谷值：${params[i].seriesName == legendData[0] ? arr.minstudents : params[i].seriesName == legendData[1] ? arr.minnewstudents : arr.minusers}</span></span>` + '</div>'
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
      data: watchArr.students.xaxis.data,
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
    yAxis: [
      {
        type: item.data.yaxis.type,
        name: item.data.series.name,
        min: parseInt(item.data.yaxis.min),
        max: parseInt(item.data.yaxis.max),
        nameLocation: 'end',
        // 位置和偏移量
        nameTextStyle: {
          padding: [0, 50, 0, 0],
          fontSize: 12,
          color: '#887E7E',
        },
        average: parseInt(item.data.yaxis.average),
        interval: item.data.yaxis.interval,
        axisLabel: {
          color: '#332929',
          fontSize: 12,
          fontFamily: 'HelveticaNeue',
          formatter: "{value} "
        },
      },
      {
        type: item.data2.yaxis.type,
        name: item.data2.series.name,
        min: parseInt(item.data2.yaxis.min),
        max: parseInt(item.data2.yaxis.max),
        average: parseInt(item.data2.yaxis.average),
        interval: item.data2.yaxis.interval,
        yAxisIndex:  yAxisIndex.value.users,
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
        name: "日活跃用户数",
        type: "bar",
        barGap: '-100%',
        barWidth: 16,
        data: watchArr.users.series.data,
        yAxisIndex: yAxisIndex.value.users,
        legendHoverLink: false,
        markLine: {
          silent: true,
          data: [
            {
              yAxis: watchArr.users.yaxis.average,
              label: {
                show: true,
                position: 'middle',
                formatter: watchArr.users.yaxis.average
              }
            }
          ]
        },
        itemStyle: {
          borderRadius: [8, 8, 0, 0],
            color: '#FFE1D9'
        },
      },
      {
        name: "学员总数",
        type: "bar",
        barWidth: 16,
        data: watchArr.students.series.data,
        yAxisIndex: yAxisIndex.value.students,
        legendHoverLink: false,
        markLine: {
          silent: true,
          data: [
            {
              yAxis: watchArr.students.yaxis.average,
              label: {
                show: true,
                position: 'middle',
                // padding: [0, 0, 0, 500],
                formatter: watchArr.students.yaxis.average
              }
            },
          ]
        },
        itemStyle: {
          borderRadius: [8, 8, 0, 0],
          color: "#FF734F"
        },
      },

      {
        name: "新增学员",
        type: "line",
        data: watchArr.newstudents.series.data,
        yAxisIndex: yAxisIndex.value.newstudents,
        legendHoverLink: false,
        markLine: {
          silent: true,
          data: [
            {
              yAxis: watchArr.newstudents.yaxis.average,
              label: {
                show: true,
                position: 'middle',
                formatter: watchArr.newstudents.yaxis.average
              }
            },
          ]
        },
        itemStyle: {
          normal: {
            color: '#FFB057'
          }
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
const arrTag = ref(["学员总数", "新增学员"])  // 上一次的值
const tag = ref('学员总数') // 上一次更新的值
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
    let sl = { '学员总数': false, '新增学员': false, '日活跃用户数': false}
    // let yA = {amount: 1, count: 1, newStudents: 1, Visits:1 }
    arrTag.forEach((val, ind) => {
      sl[val] = true
      ind == 0 ? watchArr2.data = watchArr[filterTag(val)] : watchArr2.data2 = watchArr[filterTag(val)]
    })
    selectedData.value = sl
    yAxisIndex.value[filterTag(watchArr2.data.series.name.split(' ')[0])] = 0
    yAxisIndex.value[filterTag(watchArr2.data2.series.name.split(' ')[0])] = 1
    console.log(33333, yAxisIndex.value)
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
      case '学员总数' : 
        stCatch = 'students'
        break
      case '新增学员' : 
        stCatch = 'newstudents'
        break
      case '日活跃用户数' : 
        stCatch = 'users'
        break
      case '日活用户' : 
      stCatch = 'users'
      break
    }
    return stCatch
}  

// 获取学员总数
const getOrder = async () => {
  await getGrantInfo(params)
    .then(res => {
      if (res.code === 200) {
        watchArr.students.series = res.data.series[0]
        watchArr.students.yaxis = res.data.yaxis[0]
        watchArr.students.xaxis = res.data.xaxis[0]
        watchArr.newstudents.series = res.data.series[1]
        watchArr.newstudents.yaxis = res.data.yaxis[1]
        watchArr.newstudents.xaxis = res.data.xaxis[1]
        watchArr.users.series = res.data.series[2]
        watchArr.users.yaxis = res.data.yaxis[2]
        watchArr.users.xaxis = res.data.xaxis[2]
        watchArr2.data = watchArr.students
        watchArr2.data2 = watchArr.newstudents
        init()
      }
    })
}

</script>

<style lang="scss" scoped>
</style>