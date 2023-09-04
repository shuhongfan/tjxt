<!-- 教师列表-->
<template>
  <div class="contentBox">
    <div class="bg-wt radius marg-tp-20">
      <div class="courseBox">
        <ul>
          <li>
            <span class="img"></span>
            <div class="text">
              <p>81</p>
              <p>课程总数量（个）</p>
            </div>
          </li>
          <li>
            <span class="img"></span>
            <div class="text">
              <p>27</p>
              <p>上架课程（个）</p>
            </div>
          </li>
          <li>
            <span class="img"></span>
            <div class="text">
              <p>11</p>
              <p>下架课程（个）</p>
            </div>
          </li>
          <li>
            <span class="img"></span>
            <div class="text">
              <p>12</p>
              <p>待上架课程（个）</p>
            </div>
          </li>
          <li>
            <span class="img"></span>
            <div class="text">
              <p>31</p>
              <p>完结课程（个）</p>
            </div>
          </li>
        </ul>
      </div>
    </div>
    <!-- 搜索 -->
    <Search
      :searchData="searchData"
      ref="serchInfo"
      @getStatus="getStatus"
      @getTime="getTime"
      @getList="getList"
      @handleSearch="handleSearch"
      @getTypeData="getTypeData"
      @getFree="getFree"
      @handleReset="handleReset"
    ></Search>
    <!-- end -->
    <div class="bg-wt radius marg-tp-20">
      <div class="tableBox">
        <div class="conHead pad-30">
          <!-- 新增 -->
          <AddButton @handleAdd="handleAdd" :text="text"></AddButton>
          <!-- end -->
          <!-- tab -->
          <div class="tab">
            <el-tabs
              v-model="activeName"
              class="demo-tabs"
              @tab-click="handleClick"
            >
              <el-tab-pane
                v-for="(item, index) in courseStatusData"
                :key="index"
                :label="item.label"
                :name="index"
              ></el-tab-pane>
            </el-tabs>
          </div>
          <!-- end -->
        </div>
        <!-- 表格数据 -->

        <TableList
          ref="table"
          :baseData="baseData.value"
          :total="total"
          :loading="loading"
          :pageSize="searchData.pageSize"
          :status="status"
          :isSearch="isSearch"
          :pageShow="pageShow"
          @getList="getList"
          @handleEdit="handleEdit"
          @handleSizeChange="handleSizeChange"
          @handleCurrentChange="handleCurrentChange"
        ></TableList>
        <!-- end -->
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted, nextTick, watch } from "vue";
import { useRouter } from "vue-router";
import { decimalsReplenish } from "@/utils/index.js";
// 获取vuex存储数据
import { useUserStore } from "@/store";

// 公用数据
import { courseStatusData } from "@/utils/commonData";
// 接口api
import { getCoursesPage, getDetails } from "@/api/curriculum";
// 导入组件
// 新增按钮
import AddButton from "@/components/AddButton/index.vue";
// 搜索
import Search from "./components/Search.vue";
// 表格
import TableList from "./components/TableList.vue";
// ------定义变量------
// ------vuex存储数据------
const store = useUserStore();
//初始化路由
const router = useRouter();
const text = ref("新增课程"); //弹层标题
const loading = ref(false); //加载数据
const serchInfo = ref(); //定义搜索ref
let total = ref(null); //数据总条数
const table = ref(); //table ref
let free = ref(null); //售卖模式
let categoryIdLv3 = reactive([]); //存放分类选择后的id
let datePicker = reactive([]); //存放搜索的时间
let searchData = reactive({
  pageSize: 10,
  pageNo: 1,
  isAsc: false,
  // sortBy:'updateTime'
}); //搜索对象
let status = ref(1);
let baseData = reactive([]); //表格数据
let fromData = reactive({}); //新增编辑表单数据
const activeName = ref(0); //当前选中的tab值
let isSearch = ref(false); //是否触发了搜索按钮,用来控制没有搜索出数据和正常列表无数据的区分，显示的图片和提示语不一样
let pageShow = ref(false); //tab切换是分页重新加载，解决tab切换后分页的当前页不刷新问题
// ------生命周期------
onMounted(() => {
  // 需求：当筛选待上架、已上架、已下架、已完结tab标签后，刷新页面后还停留再已选tab的页面上
  if (store.getTabNumber) {
    activeName.value = Number(store.getTabNumber);
    status.value = Number(store.getTabNumber) + 1; //tab当前页是从0开始，所以+1
  }
  init();
});
// ------定义方法------
// 获取初始值
const init = () => {
  getList();
};
// 获取列表值
const getList = async () => {
  loading.value = true;
  searchData.status = status.value;
  let parent = {};
  // 此处做了特殊处理
  // 课程分类和时间搜索默认的时一个数组，但是后端需要的分别是一级、二级、三级的id和时间字符串，组件还得有清除按钮，所以做了特殊处理，正常情况下不建议这么做

  let data = {
    pageSize: 10,
    pageNo: searchData.pageNo,
    isAsc: false,
    status: status.value,
    free: free.value,
    keyword: searchData.keyword,
  };
  if (categoryIdLv3.value) {
    parent = {
      ...data,
      firstCateId: categoryIdLv3.value[0],
      secondCateId: categoryIdLv3.value[1],
      thirdCateId: categoryIdLv3.value[2],
    };
  } else if (datePicker.value) {
    parent = {
      ...data,
      beginTime: datePicker.value[0],
      endTime: datePicker.value[1],
    };
  } else {
    parent = {
      ...data,
    };
  }

  await getCoursesPage(parent)
    .then((res) => {
      if (res.code === 200) {
        loading.value = false;
        baseData.value = res.data.list;
        total.value = res.data.total;
        baseData.value.map((val) => {
          let price = Number(val.price) / 100;
          if (price > 0) {
            val.price = decimalsReplenish(Number(price));
          }
        });
      }
    })
    .catch((err) => {});
};
// 获取详情
const getDetailData = async (id) => {
  await getDetails(id)
    .then((res) => {
      if (res.code === 200) {
        fromData.value = res.data;
      }
    })
    .catch((err) => {});
};
// 搜索
const handleSearch = () => {
  isSearch.value = true; //是否触发了搜索按钮
  getList();
};
// 设置每页条数
const handleSizeChange = (val) => {
  searchData.pageSize = val;
  // 刷新列表
  getList();
};
// 当前页
const handleCurrentChange = (val) => {
  searchData.pageNo = val;
  // 刷新列表
  getList();
};
// 打开新增弹层
const handleAdd = () => {
  store.setStepActive(0);
  router.push({
    path: "/curriculum/add/null",
  });
};
// 打开编辑弹层
const handleEdit = (id) => {
  getDetailData(id);
};
// 获取分类ids
const getTypeData = (val) => {
  // searchData.firstCateId = val[0];
  // searchData.secondCateId = val[1];
  // searchData.thirdCateId = val[2];
  categoryIdLv3.value = val;
};
// 获取时间
const getTime = (val) => {
  // searchData.beginTime = val[0]; //更新开始时间
  // searchData.endTime = val[1]; //更新结束时间
  datePicker.value = val;
};
// 获取售卖模式
const getFree = (val) => {
  free.value = val === 1 ? false : val === 0 ? true : null;
};
// 获取课程状态
const getStatus = (val) => {
  status.value = val;
};
// 重置表单
const handleReset = () => {
  // 清空搜索表单内容
  searchData.beginTime = null;
  searchData.endTime = null;
  free.value = null;
  categoryIdLv3.value = [];
  datePicker.value = [];
  searchData.firstCateId = null;
  searchData.secondCateId = null;
  searchData.thirdCateId = null;
  isSearch.value = false;
};
// 触发tab切换,根据不同的tab值获取不同的数据
const handleClick = (val, event) => {
  // 触发tab切换的时候清除排序
  if (table.value.tableItem) {
    table.value.tableItem.clearSort(); // 清除排序
  }
  store.setTabNumber(val.props.name); //当前触发tab值保存在缓存中
  const index = val.props.name;
  pageShow.value = false;
  if (index === 0) {
    status.value = 1;
  } else if (index === 1) {
    status.value = 2;
    searchData.sortBy = "publishTime";
  } else if (index === 2) {
    status.value = 3;
    searchData.sortBy = "publishTime";
  } else {
    status.value = 4;
    searchData.sortBy = "publishTime";
  }
  nextTick(() => {
    searchData.pageNo = null;
    pageShow.value = true;
    getList();
  });
};
</script>
<style src="./../index.scss" lang="scss" scoped></style>
<style lang="scss" scoped>
.courseBox {
  min-width: 1005px;
}
:deep(.tab .el-tabs .el-tabs__item) {
  font-weight: 400;
}
.bg-wt {
  margin-bottom: 20px;
}
</style>
