<!--查看兑换码列表-->
<template>
  <div class="dialogMain redeemDialog ">
    <el-dialog
      v-model="dialogVisible"
      title="查看优惠券兑换码"
      :before-close="handleClose"
    >
      <!-- tab -->
      <div class="tab">
        <el-tabs
          v-model="activeName"
          class="demo-tabs"
          @tab-click="handleClick"
        >
          <el-tab-pane
            v-for="(item, index) in redeemTypeData"
            :key="index"
            :label="item.label"
            :name="index"
          ></el-tab-pane>
        </el-tabs>
      </div>
      <!-- end -->
      <div v-if="baseData.value">
        <!-- 兑换码列表 -->
        <div class="redeemBox">
          <ul>
            <li v-for="(item, index) in baseData.value" :key="index">
              {{ item.code }}
            </li>
          </ul>
        </div>
        <!-- 分页 -->
        <el-pagination
          v-if="total > 10"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :page-sizes="[20, 40, 60, 80]"
          :page-size="searchData.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="Number(total)"
          class="paginationBox"
        >
        </el-pagination>
      </div>

      <!-- end -->
      <div v-else>
       <EmptyPage ></EmptyPage>
      </div>
      <!-- end -->
      <template #footer>
        <span class="dialog-footer">
          <el-button class="button primary" @click="handleClose"
            >我知道了</el-button
          >
        </span>
      </template>
    </el-dialog>
  </div>
</template>
<script setup>
import { ref, reactive, nextTick, onMounted, watch } from "vue";
// 接口api
import { getCodePage } from "@/api/marketing";
// 公用数据
import { redeemTypeData } from "@/utils/commonData";
// 空页面
import EmptyPage from "@/components/EmptyPage/index.vue";
// 获取父组件值、方法
const props = defineProps({
  // 弹层隐藏显示
  dialogVisible: {
    type: Boolean,
    default: false,
  },
  // 删除信息
  deleteText: {
    type: String,
    default: "",
  },
  // 优惠券id
  couponId: {
    type: String,
    default: "",
  },
});
// ------定义变量------
const emit = defineEmits(); //子组件获取父组件事件传值
let fromData = ref({}); //表单数据
const activeName = ref(0); //当前选中的tab值
const loading = ref(false); //加载数据
let total = ref(null); //数据总条数
let searchData = reactive({
  pageSize: 20,
  pageNo: 1,
  status: 1,
}); //搜索对象
let baseData = reactive([]); //表格数据
const expireTimeOption = (time) => {
  // 立刻发放当天时间之前
  return time.getTime() < Date.now() - 24 * 60 * 60 * 1000;
};
// 领用期限当天时间之前(包含当天)禁止选择
const dateReceiveTimeOption = (time) => {
  return time.getTime() <= new Date().getTime();
};
// 使用期限应该晚于领用开始时间
const dateUseTimeOption = (time) => {
  const dateReceivePicker = fromData.value.dateReceivePicker;
  if (dateReceivePicker) {
    return time.getTime() < new Date(dateReceivePicker[0]).getTime();
  }
};
// ------定义方法------
// 获取列表值
const getList = async (id) => {
  loading.value = true;
  let parent = {
    ...searchData,
    couponId: id,
  };
  await getCodePage(parent)
    .then((res) => {
      if (res.code === 200) {
        loading.value = false;
        baseData.value = res.data.list;
        ;
        total.value = res.data.total;
      }
    })
    .catch((err) => {});
};
// 选择发放方式
const handleType = (val) => {
};
//关闭弹层
const handleClose = () => {
  emit("handleClose");
};
// 触发tab切换,根据不同的tab值获取不同的数据
const handleClick = (val) => {
  const index = val.index;
  if (index === "0") {
    searchData.status = 1;
  } else {
    searchData.status = 2;
  }
  getList(props.couponId); //刷新列表
};
// 设置每页条数
const handleSizeChange = (val) => {
  searchData.pageSize = val;
  getList(props.couponId); //刷新列表
};
// 当前页
const handleCurrentChange = (val) => {
  searchData.pageNo = val;
  getList(props.couponId); //刷新列表
};
// 向父组件暴露方法
defineExpose({
  getList,
});
</script>
<style lang="scss" scoped>
:deep(.tab .el-tabs__nav){
  margin-top: 17px;
}
:deep(.el-dialog__title){
  font-family: MicrosoftYaHei-Bold;
  font-weight: Bold;
  font-size: 16px;
  color: #332929;
}
:deep(.el-dialog .el-dialog__header){
  padding-top: 17px;
}
</style>