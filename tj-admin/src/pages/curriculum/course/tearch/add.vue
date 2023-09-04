<template>
  <div class="dialogMain selectBox">
    <el-dialog
      v-model="dialogVisible"
      title="选择老师"
      :before-close="handleClose"
    >
      <div class="searchForm">
        <el-form ref="ruleForm" :inline="true" :model="searchData">
          <el-row :gutter="30">
            <el-col :span="12">
              <el-form-item label="" prop="name">
                <el-input
                  placeholder="请输入教师名称"
                  v-model="searchData.name"
                  clearable
                  class="el-input"
                  @clear="handleReset(ruleForm)"
                />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <div class="btn">
                <el-button class="button primary" @click="handleSearch"
                  >搜索</el-button
                >
              </div>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <!-- 表格 -->
      <div class="tableBox">
        <el-table
          :data="baseData"
          ref="regTable"
          border
          stripe
          height="403"
          v-loading="loading"
          :row-key="getKey"
          @select="handleChangeSelection($event,'')"
          @select-all="handleChangeSelection($event,'isAll')"
        >
          <el-table-column
            type="selection"
            reserve-selection
            align="center"
            width="90"
          />
          <el-table-column label="教师名称" align="center" min-width="240">
            <template #default="scope">
              <div class="head">
                {{ scope.row.name }}
              </div>
            </template>
          </el-table-column>
          <el-table-column
            prop="cellPhone"
            align="center"
            label="教师手机号"
            min-width="160"
          />
          <el-table-column
            prop="courseAmount"
            align="center"
            label="负责课程数量"
            min-width="160"
          />
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
          :page-size="searchData.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="Number(total)"
          class="paginationBox paginationTearch"
        >
        </el-pagination>
        <!-- end -->
      </div>

      <template #footer>
        <span class="dialog-footer">
          <el-button class="button buttonPrimary" @click="handleClose"
            >取消</el-button
          >
          <el-button class="button primary" @click="handleSubmit"
            >提交</el-button
          >
        </span>
      </template>
    </el-dialog>
  </div>
</template>
<script setup>
import { ref, reactive, nextTick, watch } from "vue";
import { ElMessage } from "element-plus";
// 空页面
import EmptyPage from "@/components/EmptyPage/index.vue";
// 获取父组件值、方法
const props = defineProps({
  // 弹层隐藏显示
  dialogVisible: {
    type: Boolean,
    default: false,
  },
  //
  selectData: {
    type: Object,
    default: () => [],
  },
  // 搜索对象
  searchData: {
    type: Object,
    default: () => ({}),
  },
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
  // loading
  loading: {
    type: Boolean,
    default: false,
  },
});
// ------定义变量------
const emit = defineEmits(); //子组件获取父组件事件传值
const regTable = ref(); //定义表格ref
const ruleForm = ref(); //定义表单ref
let num = ref(1); //排序值
let checkTableList = reactive([]); //选中的list
let checkItem = ref([]);
let isSearch = ref(false); //是否触发了搜索按钮,用来控制没有搜索出数据和正常列表无数据的区分，显示的图片和提示语不一样
let arrSelection = reactive(false)
// ------定义方法------
//
// watch(
//   () => props.dialogVisible,
//   (count, prevCount) => {
//     if (count) {
//       // 赋值已经选择的老师
//       nextTick(() => {
//         props.baseData.forEach((row) => {
//           props.selectData.forEach((selected) => {
//             if (selected.id === row.id) {
//               checkTableList.value = props.selectData;
//               regTable.value.toggleRowSelection(row, true);
//             }
//           });
//         });
//       });
//     }
//   }
// );
watch(
  () => props.dialogVisible,
  (count, prevCount) => {
    if (count) {
      nextTick(() => {
        props.baseData.forEach((row) => {
          props.selectData.forEach((selected) => {
            if (selected.id === row.id) {
              checkTableList.value = props.selectData;
              regTable.value.toggleRowSelection(row, true);
            }
          });
        });
      });
    }
  }
);
watch(
  () => props.baseData,
  (count, prevCount) => {
    if (count) {
      nextTick(() => {
        count.forEach((row) => {
          props.selectData.forEach((selected) => {
            if (selected.id === row.id) {
              setTimeout(()=>{
              checkTableList.value = props.selectData;
              regTable.value.toggleRowSelection(row, true);
              },10)             
            }
          });
        });

      });
    }
  }
);
// 搜索
const handleSubmit = async () => {
  
  if (
    checkTableList.value !== undefined ||
    checkTableList.value.length !== 0 ||
    props.selectData.length !== 0
  ) {
    emit("getValue", checkTableList.value);
    ruleForm.value.resetFields();
    emit("handleCurrentChange", 1);
    emit("handleClose");
  } else {
    ElMessage({

      message: "请至少设置一名教师!",
      type: "error",
      showClose: false,
    });
    return false;
  }
};
// 关闭弹层
const handleClose = () => {
  if (props.selectData === undefined) {
    if (checkTableList.value !== undefined) {
      checkTableList.value = [];
      emit("getValue", checkTableList.value);
    }
  }

  ruleForm.value.resetFields();
  emit("handleClose");
  regTable.value.clearSelection();
  emit("getList");
};
// 设置每页条数
const handleSizeChange = (val) => {
  emit("handleSizeChange", val);
};
// 当前页
const handleCurrentChange = (val) => {
  emit("handleCurrentChange", val);
};
//

// 全选
const handleSelectionAllChange = (data) => {
  // 过滤当前表格数据
  props.baseData.forEach((v, index) => {
    if (index > 5) {
      // 大于设置的条数，取消选择
      regTable.value.toggleRowSelection(v, false);
      return;
    }
  });
};
// // 单行触发
// const rowClick = (row) => {

//   // 单击行，设置选中
//   if (checkTableList.value) {
//     const check = checkTableList.value.find((v) => {
//       return v.id == row.id;
//     });
//     if (!check && checkTableList.value.length >= 5) {
//       ElMessage({
//
//         message: "最多可设置5名课程老师!",
//         type: "error",
//         showClose:false,
//       });
//       return;
//     }
//     row.introduce = row.intro;
//     regTable.value.toggleRowSelection(row);
//   }
// };
// 搜索
const handleSearch = () => {
  isSearch.value = true; //是否触发了搜索按钮
  emit("handleSearch");
};
// 重置搜索表单
const handleReset = (ruleForm) => {
  ruleForm.resetFields();
  isSearch.value = false;
  emit("getList");
};
const getKey = (row) => {
  return row.id;
};
const handleChangeSelection = (val, str) => {
  
  val.forEach((ele,i)=>{
    if(ele===undefined){
      val.splic(i,1)
    }
  })

  arrSelection= val;

  if (val) {
    if (val.length > 5) {
      // 截取5位后,不需要的数据
      let tempArr = val.slice(5);
      // 大于设置的条数，取消选择
      tempArr.forEach((ele) => {
        regTable.value.toggleRowSelection(ele, false);
      });
      // 截取前5位
      val = val.slice(0, 5);
      if (str !== "isAll") {
        ElMessage({

          message: "最多可设置5名课程老师!",
          type: "error",
        });
      }
    }
  }
  val.forEach((obj) => {
    obj.introduce = obj.intro;
  });
  checkTableList.value = val;
};
</script>
<style lang="scss" scoped>
.head {
  justify-content: center;
}
.paginationTearch {
  padding-bottom: 0;
}

:deep(.el-input) {
  .el-input__wrapper {
    .el-input__inner {
      color: #332929;
      &::placeholder {
        color: #b5abab;
      }
    }
  }
}
:deep(.el-table) {
  tr {
    color: #332929;
  }
  th {
    font-family: PingFangSC-Medium;
    font-weight: 500;
  }
  td {
    font-family: PingFangSC-Regular;
    font-weight: 400;
    // border-bottom: 1px solid var(--color-tabborder)
  }
}
.selectBox {
  :deep(.el-dialog) {
    // bottom: 146px;
    // .el-dialog__body {
    //   padding-bottom: 30px;
    // }
  }
}
.paginationBox {
  color: #332929;
  :deep(.el-input__wrapper) {
    height: 28px;
    background: #fdfcfa;
    border: 1px solid #f5efee;
    border-radius: 3px;
  }
}
:deep(.el-pagination__total) {
  color: #332929;
}
:deep(.el-select) {
  .el-input__inner {
    color: #332929;
  }
}
:deep(.el-pagination__jump) {
  color: #332929;
}
:deep(.el-pagination__editor) {
  .el-input {
    width: 66px !important;
  }
}
:deep(.el-button > span) {
  font-weight: 400;
}
:deep(.el-dialog) {
  height: 700px;
  width: 764px;
}
</style>
