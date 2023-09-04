<!-- 题目-->
<template>
  <div class="contentBox">
    <!-- 搜索 -->
    <Search
      :searchData="searchData"
      ref="search"
      @handleSearch="handleSearch"
      @getTypeData="getTypeData"
      @getList="getList"
      @handleReset="handleReset"
    ></Search>
    <!-- end -->
    <div class="bg-wt radius marg-tp-20">
      <div class="tableBox">
        <div class="subHead pad-30">
          <div class="titHead">
            <!-- 新增 -->
            <AddButton @handleAdd="handleAdd" :text="text"></AddButton>
            <!-- end -->
            <!-- 筛选 -->
            <div class="checkBox">
              <el-checkbox
                :indeterminate="isIndeterminate"
                v-model="checkAll"
                @change="handleCheckAllChange"
                >全选</el-checkbox
              >
              <el-checkbox-group
                class="groupBox"
                v-model="checkedCities"
                @change="handleCheckedCitiesChange"
              >
                <el-checkbox
                  v-for="(item, index) in titleData"
                  :label="item.label"
                  :key="index"
                  :value="item.value"
                  >{{ item.label }}</el-checkbox
                >
              </el-checkbox-group>
              <el-checkbox v-model="ownChecked" @change="handleOwnChange"
                >仅看我录入的</el-checkbox
              >
            </div>
            <!-- end -->
          </div>
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
// 公用数据
import { statusData, titleTypeData } from "@/utils/commonData"
// 接口api
import { getSubjectPage, getDetails } from "@/api/title"
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
const search = ref() //定义搜索ref
const statusBase = statusData //状态数据
const text = ref("新增题目") //弹层标题
const loading = ref(false) //加载数据
let total = ref(null) //数据总条数
let dialogSucceeVisible = ref(false) //创建成功弹层显示
let searchData = reactive({
  pageSize: 10,
  pageNo: 1,
  sortBy: "update_time",
  isAsc: false,
}) //搜索对象
let titleData = ref(titleTypeData)
let baseData = reactive([]) //表格数据
let fromData = reactive({}) //新增编辑表单数据
const checkAll = ref(false) //全选
const isIndeterminate = ref(false)
let checkedCities = ref([]) //筛选多选
const ownChecked = ref(false) //紧看我的
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
  let {subjectTypes , ... params} = searchData;
  params.types = subjectTypes;
  await getSubjectPage(params)
    .then((res) => {
      if (res.code === 200) {
        loading.value = false
        baseData.value = res.data.list
        total.value = res.data.total
      }
    })
    .catch((err) => { })
}
// 获取详情
const getDetailData = async (id) => {
  await getDetails(id)
    .then((res) => {
      if (res.code === 200) {
        fromData.value = res.data
      }
    })
    .catch((err) => { })
}
// 搜索
const handleSearch = () => {
  isSearch.value = true//是否触发了搜索按钮
  if (searchData.difficulty) {
    searchData.difficulty = Number(searchData.difficulty)
  }
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
    path: "/title/add/null",
  })
}
// 打开编辑弹层
const handleEdit = (id) => {
  getDetailData(id)
}
// 账号创建成功弹层显示
const handleSuccee = () => {
  dialogSucceeVisible.value = true
  handleClose()
}
// 关闭创建账号成功弹层
const handleSucceeClose = () => {
  dialogSucceeVisible.value = false
}
// 获取分类ids
const getTypeData = (val) => {
  let thirdCateId = []
  if (val.length > 0) {
    val.map((obj) => {
      if (obj[2]) {
        thirdCateId.push(obj[2]);
      }else{
        thirdCateId.push(obj[0]);
      }
    });
  }
  searchData.cateIds = thirdCateId.join(",")
}
// 重置表单
const handleReset = () => {
  searchData.thirdCateIds = []
  search.value.typeData = []
  isSearch.value = false
}
// 全部
const handleCheckAllChange = (val) => {
  checkedCities.value = val ? titleData.value : []
  if (val) {
    let arr = []
    titleData.value.map((obj) => {
      arr.push(obj.label)
    })
    checkedCities.value = arr
  } else {
    checkedCities.value = []
    searchData.subjectTypes = null
  }
  isIndeterminate.value = false
  getList()
}
// 选择多选框
const handleCheckedCitiesChange = (value) => {
  const checkedCount = value.length
  checkAll.value = checkedCount === titleData.value.length
  isIndeterminate.value =
    checkedCount > 0 && checkedCount < titleData.value.length
  let types = []
  checkedCities.value.map((val) => {
    if (val === "单选题") {
      types.push(1)
    } else if (val === "多选题") {
      types.push(2)
    } else if (val === "不定向选择") {
      types.push(3)
    } else if (val === "判断题") {
      types.push(4)
    }
  })
  searchData.subjectTypes = types.join(",")
  getList()
}
//紧录入我的
const handleOwnChange = () => {
  searchData.own = ownChecked.value
  getList()
};
</script>
<style src="./index.scss" lang="scss" scoped></style>
<style lang="scss" scoped>
.contentBox{
  margin-bottom: 20px;
}
</style>
