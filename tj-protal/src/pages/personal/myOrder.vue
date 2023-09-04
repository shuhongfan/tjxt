<!-- 个人中心-我的订单 -->
<template>
  <div class="myOrderWrapper content">
    <CardsTitle class="marg-bt-20" title="我的订单" />
    <TableSwitchBar :data="tableBar" @changeTable="changeTable"></TableSwitchBar>
    <div class="table" >
      <div class="tabHead">
        <span class="fx-1 alignLeft">课程信息</span><span>订单金额</span><span>实付金额</span><span>交易状态</span><span>操作</span>
      </div>
      <div class="marg-bt-20" v-for="(item, index) in orderListData">
        <div class="tabInfo">
          <div><span class="time alignLeft">{{item.createTime}}</span>订单号：{{item.id}}</div>
        </div>
        <div class="tabCont">
          <div class="orderList">
            <div class="fx-1 alignLeft" >
              <OrderCards :data="it" v-for="it in item.details"></OrderCards>
            </div>
            <span>{{amountConversion(item.totalAmount)}}</span><span>{{amountConversion(item.realAmount)}}</span><span>{{orderStatus(item)}}</span>
            <span class="btCont">
              <span class="bt" v-if="isOrderPay(item)">评价课程</span>
              <span @click="() => $router.push({path: 'myOrderDetails',query: {id:item.id}})" class="bt bt-grey1">查看订单</span>
              <span v-if="item.status == 1 " @click="cancelOrderHandle(item)" class="bt bt-grey">取消订单</span>
              <span v-if="item.status == 1 " @click="() => $router.push({path: '/pay/payment',query: {orderId:item.id}})" class="bt">去支付</span>
              <span v-if="item.status == 3 || item.status == 5"  @click="delOrderHandle(item)" class="bt bt-grey1">删除订单</span>
            </span>
          </div>
        </div>
      </div>
    </div>
    <div class="pageination" v-show="count > 0">
      <el-pagination
        background
        layout="total, sizes, prev, pager, next, jumper"
        :total="count"
        class="mt-4"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>
<script setup>

/** 数据导入 **/
import { onMounted, ref, reactive } from "vue";
import { ElMessage } from "element-plus";
import { getOrderListes, cancelOrder, delOrder } from "@/api/order.js";
import { useRoute } from "vue-router";
import { dataCacheStore } from "@/store"
import {amountConversion} from "@/utils/tool.js"
import { ElMessageBox } from 'element-plus'

// 组件导入
import CardsTitle from './components/CardsTitle.vue'
import TableSwitchBar from "./components/TableSwitch.vue";
import OrderCards from "./components/OrderCards.vue";


const route = useRoute()
const store = dataCacheStore()

const tableBar = [
  {id: 0, name: '全部'},
  {id: 1, name: '待支付'},
  {id: 2, name: '已支付'},
  {id: 3, name: '已关闭'},
  {id: 4, name: '已完成'},
  {id: 5, name: '已报名'},
  {id: 6, name: '已退款'}
]

// tab切换
const actId = ref(0)
const changeTable = id => {
  actId.value = id
  params.status = actId.value === 0 ? undefined : actId.value
  getOrderListesData()
}
// 分页
const count = ref(0)
const params = reactive({
  status: actId.value === 0 ? undefined : actId.value, // 订单状态：1：待支付，2：已支付，3：已关闭，4：已完成，5：已报名
  // refundStatus: 1, // 退款状态1：待审批，2：取消退款，3：同意退款，4：拒绝退款，5：退款成功，6：退款失败
  pageNo: 1,
  pageSize: 10,
})

const handleSizeChange = (val) => {
  params.pageSize = val
  getOrderListesData()
}

const handleCurrentChange = (val) => {
  params.pageNo = val
  getOrderListesData()
}

// mounted生命周期
onMounted(async () => {
  getOrderListesData()
});

/** 方法定义 **/
// 获取订单列表
const orderListData = ref()
const getOrderListesData =  async () => {
  await getOrderListes(params)
    .then((res) => {
      if (res.code === 200 ){
        orderListData.value = res.data.list
        count.value = Number(res.data.total)
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
function isOrderPay(item){
  let status = item.status;
  return status === 2 || status === 4 || status === 5;
}
// 订单状态1：待支付，2：已支付，3：已关闭，4：已完成，5：已报名
function orderStatus(item) {
  let data = ''
  switch(item.status){
    case 1: {
      data = '待支付'
      break
    }
    case 2: {
      data = '已支付'
      break
    }
    case 3: {
      data = '已关闭'
      break
    }
    case 4: {
      data = '已完成'
      break
    }
    case 5: {
      data = '已报名'
      break
    }
    case 6: {
      let i = item.details.findIndex(d => d.refundStatus === 5);
      data = i !== -1 ? '已退款' : "退款中"
      break
    }
  }
  return data
}
// 取消订单
const cancelOrderHandle = async (item) => {
  ElMessageBox.confirm(
        `是否确认取消该订单吗？`,
        '取消订单',
        {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'delete',
        }
      )
        .then(() => {
          cancelOrderAction(item)
        })
        .catch(() => {
        })
}
// 取消订单
const cancelOrderAction = async (item) => {
  await cancelOrder(item.id)
    .then((res) => {
      if (res.code === 200 ){
        item.status = 3
      } else {
        ElMessage({
        message: res.msg,
        type: 'error'
      });
      }
    })
    .catch(() => {
      ElMessage({
        message: "取消订单请求失败！",
        type: 'error'
      });
    });
}
// 删除确认
const delOrderHandle = async (item) => {
  ElMessageBox.confirm(
        `您确认删除该订单吗，点击确认将永久消失？`,
        '确认删除',
        {
          confirmButtonText: '删除',
          cancelButtonText: '取消',
          type: 'delete',
        }
      )
        .then(() => {
          delOrderAction(item)
        })
        .catch(() => {
          ElMessage({
            message: "取消操作！",
            type: 'info'
          });
        })
}

// 删除订单
const delOrderAction = async (item) => {
  await delOrder(item.id)
    .then((res) => {
      if (res.code === 200 ){
        getOrderListesData()
        ElMessage({
          message: '订单删除成功',
          type: 'success'
        });
      } else {
        ElMessage({
          message: res.msg,
          type: 'error'
        });
      }
    })
    .catch(() => {
      ElMessage({
        message: "删除订单请求失败！",
        type: 'error'
      });
    });
}
</script>
<style lang="scss" src="./index.scss"> </style>
