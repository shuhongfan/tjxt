<!--退款审批列表-->
<template>
  <div>
      <!-- 表格 -->
      <el-table :data="baseData" border stripe v-loading="loading">
        <el-table-column type="index" align="center" width="100" label="序号" />
        <el-table-column label="退款ID" prop="id" min-width="230">
        </el-table-column>
        <el-table-column label="子订单ID" prop="orderDetailId" min-width="230">
        </el-table-column>
        <el-table-column label="总订单ID" prop="orderId" min-width="230">
        </el-table-column>
        <el-table-column label="退款金额" prop="" width="150">
          <template #default="scope">
            {{ (scope.row.refundAmount / 100).toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column label="申请人" prop="proposerName" width="150">
        </el-table-column>
        <el-table-column
          label="申请人手机号"
          prop="proposerMobile"
          min-width="160"
        >
        </el-table-column>
        <el-table-column label="状态" min-width="150">
          <template #default="scope">
            <span v-if="scope.row.status === 1"
              ><span class="iconTip forbidIcon"></span>待审批</span
            >
            <span v-if="scope.row.status === 2"
              ><span class="iconTip accomplishIcon"></span>取消退款</span
            >
            <span v-if="scope.row.status === 3"
              ><span class="iconTip normalIcon"></span>同意退款</span
            >
            <span v-if="scope.row.status === 4"
              ><span class="iconTip accomplishIcon"></span>拒绝退款</span
            >
            <span v-if="scope.row.status === 5"
              ><span class="iconTip normalIcon"></span>退款成功</span
            >
            <span v-if="scope.row.status === 6"
              ><span class="iconTip forbidIcon"></span>退款失败</span
            >
          </template>
        </el-table-column>
        <el-table-column
          prop="createTime"
          label="申请时间"
          min-width="200"
        >
          <template #default="scope">
            {{ formatTime(scope.row.createTime) }}
          </template>
      </el-table-column>
        <el-table-column label="处理人" width="150">
          <template #default="scope">
            {{ scope.row.approverName ? scope.row.approverName : "--" }}
          </template>
        </el-table-column>
        <el-table-column label="处理时间" min-width="220">
          <template #default="scope">
            {{ scope.row.approveTime ? formatTime(scope.row.approveTime) : "--" }}
          </template>
        </el-table-column>
        <el-table-column
          label="退款时间"
          min-width="220"
        >
        <template #default="scope">
            {{ scope.row.refundSuccessTime ? formatTime(scope.row.refundSuccessTime) : "--" }}
          </template>
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
          align="center"
          min-width="70"
          class-name="refund"
        >
          <template #default="scope">
            <div class="operate">
              <span class="textDefault" @click="handleEdit(scope.row)">{{
                scope.row.status === 1 ? "处理" : "查看"
              }}</span>
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
import { useRouter, useRoute } from "vue-router";
// 空页面
import EmptyPage from "@/components/EmptyPage/index.vue";
// moment插件
import moment from "moment";
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
});
// ------定义变量------
const emit = defineEmits(); //子组件获取父组件事件传值
const router = useRouter(); //获取全局
const route = useRoute(); //获取局部
// ------定义方法------
// 查看
const handleEdit = (row) => {
  router.push({
    path: "/order/refundDetails/" + row.id,
  });
};

// 设置每页条数
const handleSizeChange = (val) => {
  emit("handleSizeChange", val);
};
// 当前页
const handleCurrentChange = (val) => {
  emit("handleCurrentChange", val);
};
// 时间格式化
const formatTime = (time) => {
  return moment(time).format("YYYY.MM.DD HH:mm:ss");
};
</script>
<style lang="scss">
.refund{
  .cell{
    padding: 0 !important;
  }
}
</style>
