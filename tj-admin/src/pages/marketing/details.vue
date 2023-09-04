<!-- 优惠券详情 -->
<template>
  <div class="contentBox">
    <div class="bg-wt radius marg-tp-20">
      <div class="detailBox" :key="keyTime">
        <!-- 标题 -->
        <div class="tit"><span>优惠券基本信息</span></div>
        <div style="border-top: 1px solid #e5e5e5; margin-bottom: 20px"></div>
        <!-- 基本信息 -->
        <div class="information">
          <div class="informations">
            <div class="title">
              <div class="itemtitle">优惠券名称</div>
              <div class="item">{{ detailData.data.name }}</div>
            </div>
            <div class="title">
              <div class="itemtitle">使用范围</div>
              <div style="line-height: 28px" v-if="!detailData.data.scopes">
                <span style="font-weight: normal"> 不限定范围 </span>
              </div>
              <div class="rangeitem" v-else>
                <span
                    class="item-range"
                    v-for="(s, index) in detailData.data.scopes"
                    :key="index"
                >{{ s.name }}
                </span>
              </div>
            </div>
            <div class="title">
              <div class="itemtitle">优惠券类型</div>
              <span class="item">{{ detailData.data.rule }}</span>
            </div>
            <div class="title">
              <div class="itemtitle">领用期限</div>
              <span class="item"
                >{{ formatTimeOrdinary(detailData.data.issueBeginTime) }}--{{
                  formatTimeOrdinary(detailData.data.issueEndTime)
                }}</span
              >
            </div>
            <div class="title">
              <div class="itemtitle">使用期限</div>
              <span
                class="item"
                v-if="
                  detailData.data.termBeginTime && detailData.data.termEndTime
                "
                >{{ formatTimeOrdinary(detailData.data.termBeginTime) }}--{{
                  formatTimeOrdinary(detailData.data.termEndTime)
                }}
              </span>
              <span class="item" v-else-if="detailData.data.termDays"
                >{{ detailData.data.termDays }}天</span
              >
            </div>
          </div>
        </div>

        <div class="tit" style="margin-top: 10px">
          <span>优惠券推广方式</span>
        </div>
        <div style="border-top: 1px solid #e5e5e5; margin-bottom: 20px"></div>
        <!-- 推广方式 -->
        <div class="information">
          <div class="informations">
            <div class="title">
              <span class="itemtitle">推广方式</span>
              <div class="">
              <span class="item" v-if="detailData.data.obtainWay === 1"
                >手动领取</span
              >
              <span class="item" v-if="detailData.data.obtainWay === 2"
                >指定发放</span
              >
            </div>
            </div>
            <div class="title">
              <span class="itemtitle">发放数量</span>
              <span class="item">{{ detailData.data.totalNum }}张</span>
            </div>
            <div class="title">
              <span class="itemtitle">每人限领</span>
              <span class="item">{{ detailData.data.userLimit }}张</span>
            </div>
          </div>
        </div>

        <!-- end -->
      </div>
      <div
        class="detailBox"
      >
        <!-- 按钮 -->
        <div class="btn">
          <el-button
            v-preventReClick
            class="button primary"
            @click="handleGetback"
            >返回</el-button
          >
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted, watch } from "vue"
import { useRouter, useRoute } from "vue-router"
import { formatTimeOrdinary } from "@/utils/index";
// 引入api
import { getDetails, formatRule } from "@/api/marketing.js"
//初始化路由
const router = useRouter() //获取全局
const route = useRoute() //获取局部
const detailId = ref(null)//当前优惠券id
// 存储优惠券详情数据
const detailData = reactive({
  data: {
    cateInfos: [],
  },
})
// ------生命周期------
onMounted(() => {
  // 获取当前的id
  detailId.value = route.params.id
})
// 监听获取到id触发获取优惠券详情信息
watch(detailId, () => {
  gettitleDetail()
})
// 获取题目详情信息
let gettitleDetail = async () => {
  await getDetails(detailId.value)
    .then((res) => {
      if (res.code === 200) {
        res.data.rule = formatRule(res.data)
        detailData.data = res.data
      }
    })
    .catch((err) => {
    })
}

// 返回键
const handleGetback = () => {
  router.push({
    path: "/marketing",
  })
};
</script>
<style lang="scss" scoped>
.information {
  display: flex;
  .informations {
    margin-left: 32px;
    .title {
      display: flex;
      font-weight: 600;
      font-size: 14px;
      color: #332929;
      letter-spacing: 0; //字间距
      display: flex;
      margin-bottom: 30px;
      &:nth-child(2) {
        margin-bottom: 20px;
        .itemtitle{
          align-self: start;
        }
      }
      .itemtitle {
        min-width: 70px;
        justify-content: end;
        align-self: center;
        text-align: right;
        margin-right: 29px;
        line-height: 28px;
      }
      .item {
        font-weight: 400;
        font-size: 14px;
        color: #332929;
        left: 96px;
        letter-spacing: 0;
        text-align: justify;
        line-height: 28px;
      }
      .rangeitem {
        min-width: 900px;
        .item-range {
          display: inline-block;
          padding: 6px 14px;
          margin-bottom: 10px;
          background: #ffede9;
          border-radius: 15.5px;
          font-weight: 400;
          font-size: 14px;
          color: #ff734f;
          margin-right: 24px;
        }
      }
    }
  }
}
.btn {
  padding-top: 0px;
}
:deep(.detailBox .tit) {
  padding-bottom: 10px;
  span {
    font-size: 18px;
  }
}
.button {
  width: 130px;
}
.detailBox {
  padding-bottom: 30px;
}
</style>

