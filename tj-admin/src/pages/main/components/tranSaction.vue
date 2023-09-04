<template>
  <div class="box" ref="EcharRef" style="height: 455px; min-width: 710px; width: 100%" ></div>
</template>

<script setup>
import { ref, onMounted, reactive, watch } from "vue"
import * as echarts from "echarts"
import { getGrantInfo } from "@/api/main"
import { get } from "lodash"

// 创建一个数组，用于保存数据和监听器的监听，内容为数组visitors，volume，visitors
const watchArr = reactive({
  visitors: {}, // 访客数
  volume: {}, // 购买量
})
// 查询参数
const params = reactive({
  types: '8,9',
})// 购买量 
const legendData = ["访客数", "购买量"]
// 创建一个erchart柱状图
const EcharRef = ref(null)
// 生命周期
onMounted(() => {
  getOrder()
  // console.log("watchArr", watchArr)
})


const init = (()=>{
  const myChart = echarts.init(EcharRef.value)
    const option = {
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'line'
        },
        backgroundColor: 'rgba(255,255,255,0.8)', 
        formatter: function (params) {
          // console.log(params, 'params56465')
          let res = '<div style="font-family: HelveticaNeue;font-size: 12px;color: #887E7E;letter - spacing: -0.2px; margin-top:6px;padding-right:19px;">' + '2022.' + params[0].name 
          for (let i = 0; i < params.length; i++) {
            let color = params[i].color // 获取当前点的颜色
            // 创建数组，用于存放各项数据的最大值和最小值
            let arr = {
              maxvisitors: get(watchArr.visitors, "series.max"),
              minvisitors: get(watchArr.visitors, "series.min"),
              maxvolume: get(watchArr.volume, "series.max"),
              minvolume: get(watchArr.volume, "series.min"),
            }
            res += '<div style="font-family:PingFangSC-Regular;font-size: 12px;color: #332929;letter - spacing: -0.2px;width:236px;margin-top:5px;margin-bottom:15px">' + `<span style="width: 5px;height: 5px;background: ${color} ;display:inline-block;border-radius: 50%;overflow:hidden;;margin-bottom:4px;margin-right:7px"> + </span>` + params[i].seriesName + ' : ' + params[i].value + `<span>${params[i].seriesName == '访客数' ? '个' : params[i].seriesName !== '访客数 ' ? '笔' : ''}<span>` + '<br/>' + `<span style="display:flex;justify-content: space-between;margin-top:6px;"><span>峰值：${params[i].seriesName == '访客数' ? arr.maxvisitors : arr.maxvolume}</span><span>谷值：${params[i].seriesName == '访客数' ? arr.minvisitors : arr.minvolume}</span></span>`
              + '</div>'+ '</div>'
          }
          return res
        }
      },
      legend: {
        data: legendData,
        selectedMode: 'multiple'
      },
      xAxis: {
        data: watchArr.visitors.xaxis.data,
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
          type: watchArr.visitors.yaxis.type,
          name: "访客数（人）",
          min: parseInt(watchArr.visitors.yaxis.min),
          max: parseInt(watchArr.visitors.yaxis.max),
          average: parseInt(watchArr.visitors.yaxis.average),
          interval: watchArr.visitors.yaxis.interval,
          nameLocation: 'end',
          // 位置和偏移量
          nameTextStyle: {
            padding: [0, 50, 0, 0 ],
            fontSize: 12,
            color: '#887E7E',
          },
          axisLabel: {
            color: '#332929',
            fontSize: 12,
            fontFamily: 'HelveticaNeue',
            formatter: "{value}"
          }
        },
        {
          type: watchArr.volume.yaxis.type,
          name: "购买量（笔）",
          min: parseInt(watchArr.volume.yaxis.min),
          max: parseInt(watchArr.volume.yaxis.max),
          average: parseInt(watchArr.volume.yaxis.average),
          interval: watchArr.volume.yaxis.interval,
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
            formatter: "{value}"
          }
        },
      ],
      series: [

        {
          name: "访客数",
          type: "bar",
          yAxisIndex: 0,
          barWidth: 16,
          data: watchArr.visitors.series.data,
          itemStyle: {
            borderRadius: [8, 8, 0, 0],
            color: "#FF734F"
          },
          markLine: {
            silent: true,
            data: [
              {
                yAxis: watchArr.visitors.yaxis.average,
                label: {
                  show: true,
                  position: 'middle',
                  formatter: watchArr.visitors.yaxis.average
                }
              },
            ]
          },
        },

        {
          name: "购买量",
          type: "line",
          yAxisIndex: 1,
          data: watchArr.volume.series.data,
          itemStyle: {
            normal: {
              color: '#FFB057'
            }
          },
          markLine: {
            silent: true,
            data: [
              {
                yAxis: watchArr.volume.yaxis.average,
                label: {
                  show: true,
                  position: 'middle',
                  formatter: watchArr.volume.yaxis.average
                }
              },
            ]
          },
        },
      ]
    }
    myChart.setOption(option)
    window.addEventListener('resize', function () {
    myChart.resize()
    })
})
// 创建监听器，监听数据变化
watch(watchArr, () => {
  // console.log(watchArr.visitors)
  init()
})
// 获取访客数
const getOrder = async () => {
  await getGrantInfo(params)
    .then(res => {
      if (res.code === 200) {
        watchArr.visitors.series = res.data.series[0]
        watchArr.visitors.xaxis = res.data.xaxis[0]
        watchArr.visitors.yaxis = res.data.yaxis[0]
        watchArr.volume.series = res.data.series[1]
        watchArr.volume.xaxis = res.data.xaxis[1]
        watchArr.volume.yaxis = res.data.yaxis[1]
        // console.log(watchArr.visitors, "watchArr.visitors")
      }
    })
    .catch(error => {
      reject(error)
    })
}
</script>

<style lang="scss" scoped>
</style>