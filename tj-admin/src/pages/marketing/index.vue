<!-- 营销中心列表 -->
<template>
  <div class="contentBox">
    <!-- 搜索 -->
    <Search
      :searchData="searchData"
      @handleSearch="handleSearch"
      @getList="getList"
    ></Search>
    <!-- end -->
    <div class="bg-wt radius marg-tp-20">
      <div class="tableBox">
        <div class="subHead pad-30">
          <!-- 新增 -->
          <AddButton @handleAdd="handleAdd" :text="text"></AddButton>
          <!-- end -->
        </div>
        <!-- 表格数据 -->
        <TableList
          :baseData="baseData.value"
          :total="total"
          :loading="loading"
          :pageSize="searchData.pageSize"
          :isSearch="isSearch"
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
import { ref, reactive, onMounted } from "vue"
import { useRouter } from "vue-router"
// 导入接口
import { getMarketPage, formatRule } from "@/api/marketing"
// 导入组件
// 新增按钮
import AddButton from "@/components/AddButton/index.vue"
// 搜索
import Search from "./components/Search.vue"
// 表格
import TableList from "./components/TableList.vue"
// ------定义变量------
//初始化路由
const router = useRouter()
const text = ref("新增优惠券") //新增优惠卷标题
const loading = ref(false) //列表加载loading
let total = ref(null) //数据总条数
let searchData = reactive({
  pageSize: 10,
  pageNo: 1
}) //搜索对象
let baseData = reactive([]) //表格数据
let isSearch = ref(false)//是否触发了搜索按钮,用来控制没有搜索出数据和正常列表无数据的区分，显示的图片和提示语不一样
// ------生命周期------
onMounted(() => {
  init()
})
// ------定义方法------
// 获取初始值
const init = () => {
  getList()
}

// 获取列表值
const getList = async () => {
  loading.value = true
  await getMarketPage(searchData)
    .then((res) => {
      if (res.code === 200) {
        loading.value = false
        res.data.list.forEach(c => c.rule = formatRule(c))
        baseData.value = res.data.list
        total.value = res.data.total

      }
    })
    .catch((err) => { })
}
// 搜索
const handleSearch = () => {
  isSearch.value = true//是否触发了搜索按钮
  getList()
}
// 设置每页条数
const handleSizeChange = (val) => {
  searchData.pageSize = val
  // 刷新列表
  getList()
}
// 当前页
const handleCurrentChange = (val) => {
  searchData.pageNo = val
  // 刷新列表
  getList()
}
// 打开新增弹层
const handleAdd = () => {
  router.push({
    path: "/marketing/add/null",
  })
};
</script>
<style src="./index.scss" lang="scss" scoped></style>
<style lang="scss" scoped>
.contentBox{
  margin-bottom: 20px;
}
</style>
