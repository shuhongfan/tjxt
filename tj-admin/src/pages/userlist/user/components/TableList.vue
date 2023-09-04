<template>
  <div @keydown="handleEsc">
    <!-- 表格 -->
    <el-table :data="userData" border stripe v-loading="loading" class="userTableList">
      <el-table-column type="index" align="center" width="100" label="序号" />
      <el-table-column label="后台用户名称" min-width="240" class-name="textLeft">
        <template #default="scope">
          <div class="head">
            <span @click="handleMagnify(scope.row.icon)">
              <img :src="scope.row.icon" />
              <span class="shade"><i></i></span>
            </span>

            {{ scope.row.name }}
          </div>
        </template>
      </el-table-column>
      <el-table-column
        prop="cellPhone"
        label="后台用户手机号"
        min-width="160"
      />
      <el-table-column prop="roleName" label="角色" min-width="150">
      </el-table-column>
      <el-table-column prop="createTime" :formatter="formatTime" min-width="220" label="注册时间">
      </el-table-column>
      <el-table-column label="状态" min-width="120">
        <template #default="scope">
          <span
            class="iconTip"
            :class="scope.row.status === 0 ? 'forbidIcon' : 'normalIcon'"
          ></span>
          {{ scope.row.status === 0 ? "禁用" : "正常" }}
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        align="center"
        min-width="190"
        class-name="userTable"
      >
        <template #default="scope">
          <div class="operate">
            <!-- 禁止的时候不能触发查看和重置密码,因此按钮置灰 -->
            <span
              @click="handleEdit(scope.row)"
              :class="scope.row.status === 0 ? 'textForbidden' : 'textDefault'"
              >编辑</span
            >
            <span
              @click="handleOpenStatus(scope.row)"
              :class="scope.row.status === 0 ? 'textDefault' : 'textWarning'"
            >
              {{ scope.row.status === 1 ? "禁用" : "启用" }}
            </span>
            <span
              @click="handleResetPwd(scope.row)"
              :class="scope.row.status === 0 ? 'textForbidden' : 'textDefault'"
              >重置密码</span
            >
            <!-- <span
              @click="handleDelete(scope.row)"
              :class="scope.row.status === 1 ? 'textForbidden' : 'textDefault'"
              >删除</span
            > -->
          </div>
        </template>
      </el-table-column>
      <template #empty>
        <div class="emptyPage">
          <EmptyPage :isSearch="isSearch" :baseData="baseData"></EmptyPage>
        </div>
      </template>
    </el-table>
    <!-- end -->
    <!-- 分页 -->
    <el-pagination
      v-if="total >= 10"
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
    <!-- 放大图片弹层 -->
    <ImageMagnify
      :dialogPicVisible="dialogPicVisible"
      :pic="pic"
      @handleMagnifyClose="handleMagnifyClose"
    ></ImageMagnify>
    <!-- end -->
    <!-- 重置密码 -->
    <resetpassword
      :userName="userName"
      :dialogVisible="dialogResetVisible"
      @handleReset="handleReset"
      @handleClose="handleClose"
    ></resetpassword>
    <!-- end -->
    <!-- 启用、禁用 -->
    <Status
      :statusText="statusText"
      :dialogVisible="dialogStatusVisible"
      @handleStatus="handleStatus"
      @handleClose="handleCloseStatus"
    ></Status>
    <!-- end -->
  </div>
</template>
<script setup>
import { ref } from "vue";
import { ElMessage } from "element-plus";
import { formatTime } from "@/utils/index";
// 接口
import { pwdReset, usersStatus } from "@/api/user";
// 导入组件
// 图片放大弹层
import ImageMagnify from "@/components/ImageMagnify/index.vue";
// 密码重置弹层
import resetpassword from "./resetpassword.vue";
// 启用禁用
import Status from "@/components/Status/index.vue";
// 空页面
import EmptyPage from "@/components/EmptyPage/index.vue";
// 获取父组件值、方法
const props = defineProps({
  // 搜索对象
  userData: {
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
const statusText = ref("");
let pic = ref(""); //要放大的图片
let userId = ref(""); //要重置的教师id
let userName = ref(""); //教师名称
let userStatus = ref(0); //状态内容
let dialogPicVisible = ref(false); //控制放大图片弹层显示隐藏
let dialogResetVisible = ref(false); //重置密码弹层显示隐藏

let dialogStatusVisible = ref(false); //禁用启用弹层显示隐藏
// ------定义方法------
// 启用禁用接口
const handleStatus = async (status) => {
  let params = {
    id: userId.value,
    status: userStatus.value == 0 ? 1 : 0,
  };
  await usersStatus(params)
    .then((res) => {
      if (res.code === 200) {
        let mes = "";
        if (status === 1) {
          mes = "禁用成功!";
        } else {
          mes = "启用成功!";
        }
        ElMessage({

          message: mes,
          type: "success",
          showClose: false,
        });
        handleCloseStatus();
        emit("getList");
      }
    })
    .catch((err) => {});
};
// 密码重置
const handleReset = async () => {
  await pwdReset(userId.value)
    .then((res) => {
      if (res.code === 200) {
        ElMessage({

          message: "密码重置成功!",
          type: "success",
          showClose: false,
        });
        handleClose();
        emit("getList");
      }
    })
    .catch((err) => {});
};
// 按esc关闭弹层
const handleEsc = (e) => {
  if (e.keyCode === 27) {
    dialogPicVisible.value = false;
    dialogResetVisible.value = false;
    dialogStatusVisible.value = false;
    pic.value = ""
  }
};
// 编辑
const handleEdit = (row) => {
  if (row.status === 1) {
    emit("handleEdit", row.id);
    // emit的两个参数，第一个是父组件的方法名，第二个是传给父组件的参数
  }
};
// 启用禁用状态
const handleOpenStatus = (row) => {
  userId.value = row.id;
  userStatus.value = row.status;
  if (row.status === 1) {
    dialogStatusVisible.value = true;
    statusText.value = row.name;
  } else {
    handleStatus(row.status);
  }

  // emit("getList");
};
// 关闭启用禁用状态弹层
const handleCloseStatus = () => {
  dialogStatusVisible.value = false;
};
// 设置每页条数
const handleSizeChange = (val) => {
  emit("handleSizeChange", val);
};
// 当前页
const handleCurrentChange = (val) => {
  emit("handleCurrentChange", val);
};
//打开放大图弹层
const handleMagnify = (val) => {
  dialogPicVisible.value = true;
  pic.value = val;
};
// 关闭放大图弹层
const handleMagnifyClose = () => {
  dialogPicVisible.value = false;
  pic.value = "";
};
// 打开启用禁用弹层
const handleResetPwd = (row) => {
  if (row.status === 1) {
    dialogResetVisible.value = true;
    userId.value = row.id;
    userName.value = row.name;
  }
};
// 关闭启用禁用弹层
const handleClose = () => {
  dialogResetVisible.value = false;
};
</script>
<style lang="scss" scoped>
.paginationBox {
  border-top: 1px solid #ebeef5;
}
:deep(.el-table__body-wrapper tr td.el-table-fixed-column--right) {
  .cell {
    padding: 0 20px;
  }
}

</style>
<style lang="scss">
.userTable{
  .cell{
    padding: 0 !important;
  }
}
.userTableList {
  .el-table__inner-wrapper {
    .el-table__body-wrapper {
      .el-scrollbar {
        .el-scrollbar__wrap {
          .el-scrollbar__view {
            .el-table__body {
              tbody {
                .el-table__row {
                  .el-table__cell {
                    padding: 0 !important;
                    height: 60px;
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
</style>