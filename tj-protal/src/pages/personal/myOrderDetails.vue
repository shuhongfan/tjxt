<!-- 个人中心-我的订单-详情 -->
<template>
  <div class="myOrderDetailsWrapper ">
    <div class="content marg-bt-20">
      <BreadCrumb></BreadCrumb>
      <div class="label"><span>订单详情 </span>订单号 ：{{ orderDetails && orderDetails.id }}</div>
      <div class="orderTit">{{ orderDetails && orderDetails.message }}</div>
      <div class="linePint">
        <div class="nodePoint" v-for="item in orderDetails && orderDetails.progressNodes" :key="item.id">
          <div class="pintTit">{{ item.name }}</div>
          <div class="circular"></div>
          <div class="time">
            <p v-for="it in item.time.split(' ')" :key="it.name">{{ it }}</p>
          </div>
        </div>
      </div>
    </div>
    <div class="content">
      <CardsTitle class="marg-bt-20" title="课程信息"/>
      <div>
        <el-table class="table" :data="orderDetails && orderDetails.details">
          <el-table-column prop="courseName" center label="课程名称">
            <template #default="scope">
              <div>{{ scope.row.name }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" label="课程价格" width="180">
            <template #default="scope">
              <div>{{ amountConversion(scope.row.price) }}</div>
            </template>
          </el-table-column>
          <el-table-column align="center" label="实付金额" width="180">
            <template #default="scope">
              <div>{{ amountConversion(scope.row.realPayAmount) }}</div>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="120">
            <template #default="scope">
              <div v-if="scope.row.canRefund" class="font-bt1" @click="openRefundDialog('refund', scope.row)">申请退款</div>
              <span v-else-if="!scope.row.refundStatus || scope.row.refundStatus === 3"> -- </span>
              <div v-else class="font-bt1" @click="openRefundDialog('details', scope.row)">退款详情</div>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div class="info">
        <div><span>订单总价：</span>
          <span class="pirc">￥ {{ orderDetails && amountConversion(orderDetails.totalAmount) || 0 }}</span></div>
        <div class="fx">
          <span>优惠券：</span>
          <span v-if="orderDetails && orderDetails.couponRule.length" class="ruleBox">
            <span v-for="(r,i) in orderDetails.couponRule" :key="i">
            {{ r }}
            </span>
          </span>
          <span v-else class="pirc">无</span>
        </div>
        <div><span>优惠金额：</span><span
            class="pirc">￥ {{ orderDetails && amountConversion(orderDetails.discountAmount) || 0 }}</span></div>
        <div><span>实付金额：</span><span
            class="pirc red">￥ {{ orderDetails && amountConversion(orderDetails.realAmount) || 0 }}</span></div>
      </div>
    </div>
    <!-- 申请退款 - start -->
    <el-dialog
        v-model="refundDialog"
        title="申请退款"
        width="40%"
    >
      <div class="refundCont">
        <div class="fx"><span class="lab">退款原因：</span>
          <el-select v-model="refundReason" class="m-2" style="width:100%;" placeholder="请选择退款原因">
            <el-option
                v-for="item in refundsOptions"
                :key="item"
                :label="item"
                :value="item"
            />
          </el-select>
        </div>
        <div class="fx">
          <span class="lab">问题描述：</span>
          <el-input
              v-model="questionDesc"
              rows="4"
              maxlength="200"
              placeholder="请输入退款详细描述"
              show-word-limit
              type="textarea"
          />
        </div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <div class="bt bt-grey" @click="refundDialog = false">取消</div>
          <div class="bt" type="primary" @click="refundApplyReq">提交</div>
        </span>
      </template>
    </el-dialog>
    <!-- 申请退款 - end -->
    <!-- 退款详情 - start -->
    <el-dialog
        v-model="refundDetailsDialog"
        title="退款详情"
        width="60%"
    >
      <div class="refundDetailsCont">
        <div class="tab">
          <div class="ut"><p class="ft-wt-600">退款ID</p>
            <p class="ft-cl-des">{{ refundDetailsData.id }}</p></div>
          <div class="ut"><p class="ft-wt-600">支付方式</p>
            <p class="ft-cl-des">{{ refundDetailsData.payChannel || "--" }}</p></div>
          <div class="ut"><p class="ft-wt-600">退款流水号</p>
            <p class="ft-cl-des">{{ refundDetailsData.refundOrderNo }}</p></div>
          <div class="ut"><p class="ft-wt-600">退款方式</p>
            <p class="ft-cl-des">{{ refundDetailsData.refundChannel }}</p></div>
          <div class="ut"><p class="ft-wt-600">订单ID</p>
            <p class="ft-cl-des">{{ refundDetailsData.orderId }}</p></div>
          <div class="ut"><p class="ft-wt-600">申请原因</p>
            <p class="ft-cl-des">{{ refundDetailsData.refundReason }}</p></div>
          <div class="row">
            <div class="ft-wt-600">操作时间</div>
            <div class="ft-cl-des fx-wp">
              <span>下单时间：{{ refundDetailsData.orderTime }}</span><span>支付时间{{ refundDetailsData.paySuccessTime }}</span>
              <span>退款申请时间：{{ refundDetailsData.createTime }}</span><span>退款审批时间：{{ refundDetailsData.approveTime }}</span>
            </div>
          </div>
          <div class="row">
            <p class="ft-wt-600">处理结果：</p>
            <p class="ft-cl-des" v-if="refundDetailsData.remark != null">
              审批结果：{{ refundDetailsData.remark ? '同意' : '拒绝退款' }}</p>
            <p class="ft-cl-des">审批意见：{{ refundDetailsData.approvalOpinion || '暂无！' }}</p>
          </div>
        </div>
      </div>
    </el-dialog>
    <!-- 退款详情 - end -->
  </div>
</template>
<script setup>

/** 数据导入 **/
import {onMounted, ref} from "vue";
import {ElMessage} from "element-plus";
import {getOrderDetails, refundsApply, refundsDetails} from "@/api/order.js";
import {useRoute} from "vue-router";
import {dataCacheStore} from "@/store"
import {amountConversion} from "@/utils/tool.js"
import {ElMessageBox} from 'element-plus'

// 组件导入
import BreadCrumb from "./components/BreadCrumb.vue";
import CardsTitle from './components/CardsTitle.vue'
import router from "../../router";

const route = useRoute()
const store = dataCacheStore()
const refundOrderDetail = ref({});
// 打开退款详情弹窗
const refundDialog = ref(false)
const openRefundDialog = (val, detail) => {
  refundOrderDetail.value = detail || '';
  val === 'details' ? refundDetailsReq() : refundDialog.value = true
}

// mounted生命周期
onMounted(async () => {
  getOrderDetailsData()
});

// 申请退款
const refundsOptions = ['由于技术故障引发课程无法学习', '未使用优惠券', '个人原因（个人网络原因，计划有变，账号异常等）', '课程内容不感兴趣', '其他']
const questionDesc = ref('')
const refundReason = ref('')

const refundDetailsDialog = ref(false)
const refundApplyReq = () => {
  if (refundReason.value === '') {
    ElMessage({
      message: '请选择退款原因',
      type: "error",
    });
    return false
  }
  if (questionDesc.value === "") {
    ElMessage({
      message: '请输入退款问题描述',
      type: "error",
    });
    return false
  }
  const params = {
    orderDetailId: refundOrderDetail.value.id,
    questionDesc: questionDesc.value,
    refundReason: refundReason.value
  }
  // 上面验证通过后申请退款
  refundsApply(params)
      .then((res) => {
        if (res.code === 200) {
          refundDialog.value = false;
          ElMessage({
            message: "退款成功",
            type: 'success'
          });
          router.push('/personal/main/myOrder')
        } else {
          ElMessage({
            message: res.msg,
            type: 'error'
          });
        }
      })
      .catch(() => {
        ElMessage({
          message: "订单列表请求失败！",
          type: 'error'
        });
      });
}
// 退款详情
const refundDetailsData = ref()
const refundDetailsReq = () => {
  refundsDetails(refundOrderDetail.value.id)
      .then((res) => {
        if (res.code === 200) {
          refundDetailsData.value = res.data
          refundDetailsDialog.value = true
        } else {
          ElMessage({
            message: res.msg,
            type: 'error'
          });
        }
      })
      .catch(() => {
        ElMessage({
          message: "订单列表请求失败！",
          type: 'error'
        });
      });
}

// 获取订单详情信息
const orderDetails = ref()
const getOrderDetailsData = async () => {
  await getOrderDetails(route.query.id)
      .then((res) => {
        if (res.code === 200) {
          res.data.couponRule = res.data.couponDesc ? res.data.couponDesc.split("/") : [];
          orderDetails.value = res.data
        } else {
          ElMessage({
            message: res.msg,
            type: 'error'
          });
        }
      })
      .catch(() => {
        ElMessage({
          message: "订单列表请求失败！",
          type: 'error'
        });
      });
}

// 
</script>
<style lang="scss" src="./index.scss"></style>
<style>
.ruleBox {
  display: flex;
  flex-direction: column;
  width: 200px;
}

.ruleBox span {
  width: 200px;
  overflow: hidden;
  line-height: 20px;
  height: 20px;
  text-align: right;
}
.myOrderDetailsWrapper .info .pirc {
  width: 200px;
}
</style>
