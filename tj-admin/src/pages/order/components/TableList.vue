<!--订单管理列表-->
<template>
  <div>
    <!-- 表格 -->
    <el-table :data="baseData" border stripe v-loading="loading" :default-sort="{prop:'create_time',order:'descending'}">
      <el-table-column type="index" align="center" width="100" label="序号" />
      <el-table-column label="子订单ID" prop="id" min-width="230">
      </el-table-column>
      <el-table-column label="总订单ID" prop="orderId" min-width="230">
      </el-table-column>
      <el-table-column label="学员昵称" prop="name" width="150">
      </el-table-column>
      <el-table-column label="学员手机号" prop="mobile" min-width="160">
      </el-table-column>
      <el-table-column label="实付金额" min-width="140">
        <template #default="scope">
          {{
            scope.row.realPayAmount === "0"
              ? "免费"
              : "￥" + scope.row.realPayAmount
          }}
        </template>
      </el-table-column>
      <el-table-column label="订单金额" min-width="140">
        <template #default="scope">
          {{
            scope.row.price === "0"
              ? "免费"
              : "￥" + scope.row.price
          }}
        </template>
      </el-table-column>
      <el-table-column label="订单状态" min-width="120">
        <template #default="scope">
          <span v-if="scope.row.status === 1"
            ><span class="iconTip forbidIcon"></span>未支付</span
          >
          <span v-if="scope.row.status === 2"
            ><span class="iconTip normalIcon"></span>已支付</span
          >
          <span v-if="scope.row.status === 3"
            ><span class="iconTip accomplishIcon"></span>已关闭</span
          >
          <span v-if="scope.row.status === 4"
            ><span class="iconTip accomplishIcon"></span>已完成</span
          >
          <span v-if="scope.row.status === 5"
            ><span class="iconTip normalIcon"></span>已报名</span
          >
          <span v-if="scope.row.status === 6"
            ><span class="iconTip normalIcon"></span>已退款</span
          >
        </template>
      </el-table-column>
      <el-table-column label="退款状态" min-width="150">
        <template #default="scope">
          <span
            v-if="
              scope.row.refundStatus === undefined || !scope.row.refundStatus
            "
            >--</span
          >
          <span v-if="scope.row.refundStatus === 1"
            ><span class="iconTip forbidIcon"></span>待审批</span
          >
          <span v-if="scope.row.refundStatus === 2"
            ><span class="iconTip accomplishIcon"></span>取消退款</span
          >
          <span v-if="scope.row.refundStatus === 3"
            ><span class="iconTip normalIcon"></span>同意退款</span
          >
          <span v-if="scope.row.refundStatus === 4"
            ><span class="iconTip accomplishIcon"></span>拒绝退款</span
          >
          <span v-if="scope.row.refundStatus === 5"
            ><span class="iconTip normalIcon"></span>退款成功</span
          >
          <span v-if="scope.row.refundStatus === 6"
            ><span class="iconTip forbidIcon"></span>退款失败</span
          >
        </template>
      </el-table-column>
      <el-table-column label="支付方式" prop="payChannel" min-width="120">
        <template #default="scope">
          {{
            scope.row.payChannel === undefined || !scope.row.payChannel
              ? "--"
              : scope.row.payChannel
          }}
        </template>
      </el-table-column>
      <el-table-column
        prop="createTime"
        label="下单时间"
        min-width="200"
        :formatter="formatTime"
      >
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        align="center"
        min-width="70"
        class-name="orderTable"
      >
        <template #default="scope">
          <div class="operate">
            <span class="textDefault" @click="handleCheck(scope.row)"
              >查看</span
            >
          </div>
        </template>
      </el-table-column>
      <!-- 空页面 -->
      <template #empty>
        <EmptyPage :isSearch="isSearch" :baseData="baseData"></EmptyPage>
      </template>
      <!-- end -->
    </el-table>
    <!-- end -->
    <!-- 分页 -->
    <el-pagination
      v-if="total > 10"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :page-sizes="[10, 20, 30, 40]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="Number(total)"
      class="paginationBox"
    >
    </el-pagination>
    <!-- end -->
  </div>
</template>
<script setup>
import { ref, reactive } from "vue"
import { useRouter, useRoute } from "vue-router"
import {formatTime} from "@/utils/index"
// 空页面
import EmptyPage from "@/components/EmptyPage/index.vue"
// 获取父组件值、方法
const props = defineProps({
  // 搜索对象
  baseData: {
    type: Array,
    default: () => [],
  },
  // 总条数
  total: {
    type: Number,
    default: 0,
  },
  // 每页的数量
  pageSize: {
    type: Number,
    default: 0,
  },
  // loading
  loading: {
    type: Boolean,
    default: false,
  },
  // 是否触发了搜索按钮
  isSearch: {
    type: Boolean,
    default: false,
  },
})
// ------定义变量------
const emit = defineEmits() //子组件获取父组件事件传值
const router = useRouter() //获取全局
const route = useRoute() //获取局部
let detailData = reactive({}) //详情数据

// ------定义方法------
// 查看
const handleCheck = (row) => {
  router.push({
    path: "/order/details/" + row.id,
  })
}
// 设置每页条数
const handleSizeChange = (val) => {
  emit("handleSizeChange", val)
}
// 当前页
const handleCurrentChange = (val) => {
  emit("handleCurrentChange", val)
};
</script>
<style lang="scss">
.orderTable{
  .cell{
    padding: 0 !important;
  }
}
</style>