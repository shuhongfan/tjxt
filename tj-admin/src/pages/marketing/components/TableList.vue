<!--优惠券列表-->
<template>
  <div>
    <!-- 表格 -->
    <el-table :data="baseData" border stripe v-loading="loading">
      <el-table-column type="index" align="center" width="100" label="序号" />
      <el-table-column label="优惠券名称" min-width="200" class-name="textLeft">
        <template #default="scope">
          <div class="ellipsisHidden2">
            {{ scope.row.name }}
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="rule" label="优惠券规则" min-width="320" />
      <el-table-column label="使用范围" prop="" min-width="150">
        <template #default="scope">
          {{ scope.row.specific ? "指定课程分类" : "全部课程分类" }}
        </template>
      </el-table-column>
      <el-table-column prop="obtainWay" label="推广方式" min-width="150">
        <template #default="scope">
          {{ scope.row.obtainWay === 1 ? "手动领取" : "指定发放" }}
        </template>
      </el-table-column>
      <el-table-column
        prop="totalNum"
        label="使用/领取/发放"
        min-width="180"
      >
        <template #default="scope">
          {{ scope.row.usedNum }}/{{ scope.row.issueNum }}/{{
            scope.row.totalNum
          }}
        </template>
      </el-table-column>
      <el-table-column prop="createTime" min-width="220" label="创建时间" :formatter="formatTime">
      </el-table-column>
      <el-table-column min-width="220" label="领用期限">
        <template #default="scope">
          <div v-if="scope.row.issueBeginTime && scope.row.issueEndTime">
            <p>{{ scope.row.issueBeginTime }}</p>
            <p>{{ scope.row.issueEndTime }}</p>
          </div>
          <div v-else>--</div>
        </template>
      </el-table-column>
      <el-table-column min-width="220" label="使用期限">
        <template #default="scope">
          <div v-if="scope.row.termDays">
            领券起{{scope.row.termDays}}天有效
          </div>
          <div v-else-if="scope.row.termBeginTime&&scope.row.termEndTime">
            <p>{{ scope.row.termBeginTime }}</p>
            <p>{{ scope.row.termEndTime }}</p>
          </div>
          <div v-else>--</div>
        </template>
      </el-table-column>

      <el-table-column label="状态" min-width="120">
        <template #default="scope">
          <span v-if="scope.row.status === 1">待发放</span>
          <span v-if="scope.row.status === 3">进行中</span>
          <span v-if="scope.row.status === 4">已结束</span>
          <span v-if="scope.row.status === 5">已暂停</span>
          <span v-if="scope.row.status === 2">未开始</span>
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        align="center"
        min-width="255"
        class-name="table-operation"
      >
        <template #default="scope">
          <div class="operate">
            <span
              @click="handleCheck(scope.row)"
              v-if="scope.row.status !== 1"
              :class="scope.row.status !== 1 ? 'textDefault' : 'textForbidden'"
              >查看</span
            >
            <span
              v-else
              @click="handleEdit(scope.row)"
              :class="scope.row.status === 1 ? 'textDefault' : 'textForbidden'"
              >编辑</span
            >
            <span
              @click="handleStatusOpen(scope.row)"
              :class="scope.row.status === 3 || scope.row.status === 4  ? 'textForbidden' : 'textDefault'"
              v-if="scope.row.status === 1 || scope.row.status === 5"
              >发放</span
            >
            <span
              @click="handleGrant(scope.row)"
              :class="(scope.row.status !== 3 && scope.row.status !== 2) ? 'textForbidden' : 'textDefault'"
              v-else
              >暂停</span
            >
            <span
              @click="handleLookOpen(scope.row)"
              :class="
               scope.row.obtainWay === 2 ? 'textDefault' : 'textForbidden'
              "
              >查看兑换码</span
            >
            <span
              @click="handleDeleteOpen(scope.row)"
              :class="scope.row.status === 1 ? 'textWarning' : 'textForbidden'"
            >
              删除
            </span>
          </div>
        </template>
      </el-table-column>
      <!-- 空页面 -->
      <template #empty>
        <div class="emptyPage">
        <EmptyPage :isSearch="isSearch" :baseData="baseData"></EmptyPage>
        </div>
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
    <!-- 删除 -->
    <Delete
      :dialogDeleteVisible="dialogDeleteVisible"
      :deleteText="deleteText"
      @handleDelete="handleDelete"
      @handleClose="handleClose"
    ></Delete>
    <!-- end -->
    <!-- 发放弹层 -->
    <Grant
      :dialogVisible="dialogStatusVisible"
      :grantData="grantData.value"
      @getList="getList"
      @handleClose="handleGrantClose"
    ></Grant>
    <!-- end -->
    <!-- 查看兑换码 -->
    <RedeemLook
      :dialogVisible="dialogRedeemVisible"
      ref="redeem"
      :couponId="couponId"
      @handleClose="handleLookClose"
    ></RedeemLook>
    <!-- end -->
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from "vue";
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";
import {formatTime} from "@/utils/index"
// 接口
import { configStopGrant, deleteMarket } from "@/api/marketing";
// 导入组件
// 删除弹出层
import Delete from "@/components/Delete/index.vue";
// 发放弹层
import Grant from "./Grant.vue";
// 查看兑换码
import RedeemLook from "./RedeemLook.vue";
// 空页面
import EmptyPage from "@/components/EmptyPage/index.vue";
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
const router = useRouter();
const deleteText = ref("此操作将永久删除该优惠券，是否继续？");
let redeem = ref(); //定义兑换码弹层的ref
let couponId = ref(""); //优惠券id
let dialogDeleteVisible = ref(false); //删除弹层显示隐藏
let dialogStatusVisible = ref(false); //发放弹层显示隐藏
let dialogRedeemVisible = ref(false); //兑换码弹层显示隐藏
let grantData = reactive({}); //优惠券发放信息
// ------定义方法------

// 确定删除
const handleDelete = async () => {
  await deleteMarket(couponId.value)
    .then((res) => {
      if (res.code === 200) {
        ElMessage({

          message: "删除成功!",
          type: "success",
          showClose:false,
        });
        handleClose(); //关闭弹层
        emit("getList"); //刷新列表
      }
    })
    .catch((err) => {});
};
// 查看
const handleCheck = (row) => {
  if (row.status === 1) {
    return
  }
  router.push({
    path: "/marketing/details/" + row.id,
  });
};
//编辑详情
const handleEdit = (row) => {
  if (row.status !== 1) {
    return
  }
  router.push({
    path: "/marketing/add/" + row.id,
  });
};
// 获取父组件的列表
const getList = () => {
  emit("getList");
};
// 暂停
const handleGrant = async (row) => {
  if (row.status !== 2 && row.status !== 3) {
    return
  }
  await configStopGrant(row.id)
    .then((res) => {
      if (res.code === 200) {
        ElMessage({

          message: "恭喜你，操作成功！",
          type: "success",
          showClose:false,
        });
        getList();
      }
    })
    .catch((err) => {
      console.log(err)
      ElMessage({
        message: "操作失败！",
        type: "error",
        showClose:false,
      });
    });
};
// 触发发放按钮弹出发放弹层
const handleStatusOpen = (row) => {
  if (row.status !== 1 && row.status !== 5) {
    return
  }
  grantData.value = row;
  dialogStatusVisible.value = true;
};
// 关闭发放弹层
const handleGrantClose = () => {
  dialogStatusVisible.value = false;
};
//
//打开查看兑换码弹层
const handleLookOpen = async (row) => {
  if (row.obtainWay === 1 || row.status === 1) {
    return
  }
  couponId.value = row.id;
  dialogRedeemVisible.value = true; //打开查看兑换码弹层
  redeem.value.getList(couponId.value); //通过ref刷新子组件的兑换码数据
  // await codeDownload(row.id)
  //   .then((res) => {
  //     console.log(res.data);
  //     const blob = new Blob([res.data]); //处理文档流
  //     const fileName = "优惠券兑换码.xls";
  //     const down = document.createElement("a");
  //     down.download = fileName;
  //     down.style.display = "none"; //隐藏,没必要展示出来
  //     down.href = URL.createObjectURL(blob);
  //     document.body.appendChild(down);
  //     down.click();
  //     URL.revokeObjectURL(down.href); // 释放URL 对象
  //     document.body.removeChild(down); //下载完成移除
  //     ElMessage({
  //
  //       message: "下载成功!",
  //       type: "success",
  //     });
  //   })
  //   .catch((err) => {});
};
//关闭查看兑换码弹层
const handleLookClose = async (row) => {
  dialogRedeemVisible.value = false;
};
// 打开删除弹层
const handleDeleteOpen = (item) => {
  if (item.status !== 1) {
    return
  }
  couponId.value = item.id;
  dialogDeleteVisible.value = true;
};
// 关闭删除弹层
const handleClose = () => {
  dialogDeleteVisible.value = false;
};
// 设置每页条数
const handleSizeChange = (val) => {
  emit("handleSizeChange", val);
};
// 当前页
const handleCurrentChange = (val) => {
  emit("handleCurrentChange", val);
};
</script>
<style lang="scss">
.table-operation{
  .cell{
    padding: 0 !important;
  }
}
</style>