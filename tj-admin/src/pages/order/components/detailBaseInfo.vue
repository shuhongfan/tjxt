<!--基本信息-->
<template>
  <div class="orderInfo">
    <!-- 标题 -->
    <div class="tit"><span>订单进度</span></div>
    <!-- end -->
    <div class="stepsBox1" v-if="!!fromData.nodes">
      <div class="boxstate">
        <span class="iconstate" v-if="fromData.status < 6">
          <img :src="statusImg(fromData.status)" alt="" class="Promptpicture" />
        </span>
        <span class="textstate"> {{ fromData.message }} </span>
        <span class="textstate" v-if="fromData.refundStatus">
          {{refundStatusArr[fromData.refundStatus - 1].msg}}
        </span>
      </div>
      <el-steps
        :active="fromData.nodes.length"
        finish-status="success"
        align-center
      >
        <el-step
          :title="item.name"
          :description="formatTimeOrdinary(item.time)"
          v-for="(item, i) in fromData.nodes"
          :status="i === 6 ? 'process' : 'success'"
        >
        </el-step>
      </el-steps>
    </div>
    <div class="infoItem">
      <ul>
        <li>
          <div class="tit">ID编号</div>
          <p>
            总订单<span>{{
              fromData.orderId === '' ? '无' : fromData.orderId
            }}</span>
          </p>
          <p>
            子订单ID<span>{{
              fromData.id === '' ? '无' : fromData.id
            }}</span>
          </p>
          <p>
            支付流水号<span>{{
              fromData.payOrderNo === '' ? '无' : fromData.payOrderNo
            }}</span>
          </p>
          <p>
            退款ID<span>{{
              !fromData.refundApplyId ? '无' : fromData.refundApplyId
            }}</span>
          </p>
          <p>
            退款流水号<span>{{
              !fromData.refundOrderNo ? '无' : fromData.refundOrderNo
            }}</span>
          </p>
        </li>
        <li>
          <div class="tit">申请人信息</div>
          <p>
            学员名称<span>{{
              fromData.studentName === undefined ? '无' : fromData.studentName
            }}</span>
          </p>
          <p>
            手机号<span>{{
              fromData.mobile === undefined ? '无' : fromData.mobile
            }}</span>
          </p>
          <p>
            退款申请人<span
              >{{
                fromData.refundProposerName === undefined
                  ? '无'
                  : fromData.refundProposerName
              }}
              <span v-if="fromData.refundProposerName">{{
                fromData.refundProposerName === fromData.studentName
                  ? '（学员）'
                  : '（管理员）'
              }}</span></span
            >
          </p>
        </li>
        <li>
          <div class="tit">支付/退款方式</div>
          <p>
            支付方式<span>{{
              fromData.payChannel === undefined
                ? '无'
                : fromData.payChannel
            }}</span>
          </p>
          <p>
            退款方式<span>{{
              fromData.refundChannel === undefined
                ? '无'
                : fromData.refundChannel
            }}</span>
          </p>
          <p
            v-if="
              fromData.refundStatus == 6
            "
          >
            退款失败原因<span>{{
              fromData.failedReason === undefined
                ? '无'
                : fromData.failedReason
            }}</span>
          </p>
        </li>
        <!-- <li>
          <div class="tit">操作时间</div>
          <p>
            下单时间<span>{{
              fromData.orderTime === undefined ? "无" : fromData.orderTime
            }}</span>
          </p>
        </li> -->
      </ul>
      <ul>
        <li>
          <div class="tit">申请/审批意见</div>
          <p class="opinion">
            <span class="opiniontitle">申请退款原因</span>
            <span class="opinioncontent">{{
              !fromData.refundReason ? '无' : fromData.refundReason
            }}</span>
          </p>
          <p class="opinion">
            <span class="opiniontitle">申请退款说明</span>
            <span class="opinioncontent">{{
              !fromData.refundMessage ? '无' : fromData.refundMessage
            }}</span>
          </p>
          <p class="opinion">
            <span class="opiniontitle">审批意见</span>
            <span class="opinioncontent">{{
              !fromData.remark ? '无' : fromData.remark
            }}</span>
          </p>
        </li>
      </ul>
    </div>
    <div v-if="fromCourseData">
      <!-- 标题 -->
    </div>
  </div>
</template>
<script setup>
import { formatTimeOrdinary } from "@/utils/index";
// 导入组件
// ------定义变量------
// 获取父组件值、方法
const props = defineProps({
  // 订单信息
  fromData: {
    type: Object,
    default: () => ({}),
  }
})
const statusArr = [
  {img:"/src/assets/icon-wzf.png", msg:""},
  {img:"/src/assets/icon-yzf.png", msg:""},
  {img:"/src/assets/icon-gb.png", msg:""},
  {img:"/src/assets/icon-ywc.png", msg:""},
  {img:"/src/assets/icon-ybm.png", msg:""},
]
const refundStatusArr = [
  {img:"/src/assets/icon-dsp.png", msg:"待审批"},
  {img:"/src/assets/icon-qx.png", msg:"取消退款"},
  {img:"/src/assets/icon-ty.png", msg:"同意退款"},
  {img:"/src/assets/icon-jj.png", msg:"拒绝退款"},
  {img:"/src/assets/icon-cg.png", msg:"退款成功"},
  {img:"/src/assets/icon-sb.png", msg:"退款失败"},
]
const statusImg = (status) => {
  let s = statusArr[status - 1];
  return s ? s.img : "";
}
</script>
<style lang="scss" scoped>
.infoItem {
  margin-top: 30px;
  padding-bottom: 5px;
  ul {
    &:first-child {
      border-bottom: 0;
    }
  }
}
.opinion {
  display: block;
  .opiniontitle {
    width: 85px;
    font-size: 14px;
    color: #332929;
    text-align: left;
    display: inline-block;
    margin-right: 11px;
  }
  .opinioncontent {
    font-size: 14px;
    color: #887e7e;
    flex-shrink: 0;
    text-align: left;
  }
}
</style>
<style lang="scss" scoped>
:deep(.stepsBox1) {
  font-size: 16px;
  padding-bottom: 35px;
  .el-step.is-simple .el-step__icon {
    display: inline-block;
    width: 24px;
    height: 24px;
    border: 1px solid #332929;
    border-radius: 50%;
    line-height: 22px;
    text-align: center;
    font-size: 16px;
    // background: url(@/assets/tianwanActive.png) no-repeat;
    //       background-size: contain;
    //       display: inline-block;
    //       width: 25px;
    //       height: 25px;
    //       border: 0 none;
    //       .stepNum{
    //         display: none;
    //       }
  }
  .el-steps--simple {
    border-radius: 4px;
    background: var(--color-tabbackground);
    border: 1px solid var(--color-tabborder);
  }
  // 未触发step样式
  .el-step.is-simple .is-wait .el-step__icon {
    border-color: var(--color-font9);
  }
  // 当前step样式
  .el-step__title.is-process {
    color: var(--color-white);
    font-weight: normal;
  }
  .el-step__head.is-process {
    .el-step__icon.is-text {
      border-color: #d9021c;
      .el-step__icon-inner {
        display: none;
      }
      &::after {
        content: '';
        display: block;
        width: 14px;
        height: 14px;
        background: url(../../../assets/cha.png) no-repeat;
        background-size: 14px 14px;
        border-radius: 50%;
        color: #d9021c;
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
      }
    }
  }
  .el-step__title.is-process {
    color: #d9021c;
  }
  .el-step__description.is-process {
    color: #d9021c;
  }
  // 触发后的样式
  .el-step__head.is-success {
    border-color: var(--color-btnbackground);
    color: var(--color-btnbackground);
    font-weight: normal;
  }
  .el-step__title.is-success {
    color: #332929;
  }
  .el-step__description.is-success {
    font-weight: 400;
    font-size: 13px;
    color: #b5abab;
  }
  .el-step.is-simple .is-success .el-step__icon {
    background: url(@/assets/icon-tianwan.png) no-repeat;
    background-size: contain;
    display: inline-block;
    width: 25px;
    height: 25px;
    border: 0 none;
  }
  //
  .el-step {
    &.stepSuc {
      cursor: pointer;
      .el-step__icon {
        background: url(@/assets/tianwanActive.png) no-repeat;
        background-size: contain;
        display: inline-block;
        width: 25px;
        height: 25px;
        border: 0 none;
        .stepNum {
          display: none;
        }
      }
      .el-step__head.is-wait,
      .el-step__title.is-wait {
        color: var(--color-btnbackground);
        font-weight: normal;
        .el-step__icon {
          border-color: var(--color-btnbackground);
        }
        .el-step__icon {
          background: url(@/assets/icon-tianwan.png) no-repeat;
          background-size: contain;
          display: inline-block;
          width: 25px;
          height: 25px;
          border: 0 none;
          .stepNum {
            display: none;
          }
        }
      }
    }
  }
}
:deep(.stepsBox) {
  font-size: 16px;
  .el-step.is-simple .el-step__icon {
    display: inline-block;
    width: 24px;
    height: 24px;
    border: 1px solid #332929;
    border-radius: 50%;
    line-height: 22px;
    text-align: center;
    font-size: 16px;
    // background: url(@/assets/tianwanActive.png) no-repeat;
    //       background-size: contain;
    //       display: inline-block;
    //       width: 25px;
    //       height: 25px;
    //       border: 0 none;
    //       .stepNum{
    //         display: none;
    //       }
  }
  .el-steps--simple {
    border-radius: 4px;
    background: var(--color-tabbackground);
    border: 1px solid var(--color-tabborder);
  }
  // 未触发step样式
  .el-step.is-simple .is-wait .el-step__icon {
    border-color: var(--color-font9);
  }
  // 当前step样式
  .el-step__title.is-process {
    color: var(--color-white);
    font-weight: normal;
  }
  .el-step__head.is-process {
    .el-step__icon.is-text {
      border-color: #b5abab;
      .el-step__icon-inner {
        color: #b5abab;
      }
    }
  }
  .el-step__title.is-process {
    color: #b5abab;
  }
  .el-step__description.is-process {
    color: #b5abab;
  }
  // 触发后的样式
  .el-step__head.is-success {
    border-color: var(--color-btnbackground);
    color: var(--color-btnbackground);
    font-weight: normal;
  }
  .el-step__title.is-success {
    color: #332929;
  }
  .el-step__description.is-success {
    font-weight: 400;
    font-size: 13px;
    color: #b5abab;
  }
  .el-step.is-simple .is-success .el-step__icon {
    background: url(@/assets/icon-tianwan.png) no-repeat;
    background-size: contain;
    display: inline-block;
    width: 25px;
    height: 25px;
    border: 0 none;
  }
  //
  .el-step {
    .el-step__line {
      background-color: #b5abab;
    }
    &.stepSuc {
      cursor: pointer;
      .el-step__icon {
        background: url(@/assets/tianwanActive.png) no-repeat;
        background-size: contain;
        display: inline-block;
        width: 25px;
        height: 25px;
        border: 0 none;
        .stepNum {
          display: none;
        }
      }
      .el-step__head.is-wait,
      .el-step__title.is-wait {
        color: var(--color-btnbackground);
        font-weight: normal;
        .el-step__icon {
          border-color: var(--color-btnbackground);
        }
        .el-step__icon {
          background: url(@/assets/icon-tianwan.png) no-repeat;
          background-size: contain;
          display: inline-block;
          width: 25px;
          height: 25px;
          border: 0 none;
          .stepNum {
            display: none;
          }
        }
      }
    }
  }
}
.boxstate {
  // 使内容居中
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 22px;
  .textstate {
    margin-left: 16px;
    display: inline-block;
    height: 36px;
    font-weight: 700;
    font-size: 24px;
    color: #000000;
    line-height: 36px;
    .iconstate {
      display: inline-block;
      width: 32px;
      // height: 36px;
      // line-height: 36px;
    }
  }
}
p {
  margin-bottom: 4px;
}
:deep(.el-step.is-center .el-step__line) {
  background-color: #b5abab;
}
:deep(.el-step.is-horizontal .el-step__line) {
  height: 1px;
}
.Promptpicture{
  width: 32px;
}
</style>

