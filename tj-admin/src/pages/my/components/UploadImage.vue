<template>
  <div class="uploadBox" :class="imageUrl!==''||upladImg!==''?'solidLine':''">
    <el-upload
      ref="uploadfiles"
      class="avatar-uploader"
      :class="isCourse ? 'courseBox' : ''"
      v-model:file-list="fileList"
      :action="actions"
      :headers="uploadHeaders"
      :on-success="handleAvatarSuccess"
      :on-change="handleChangeSuccess"
      :before-upload="beforeAvatarUpload"
      :show-file-list="false"
    >
      <img
        v-if="imageUrl"
        :src="imageUrl"
        class="avatar"
      />
      <div v-else class="avatar-uploader-icon"><span></span>上传<Plus /></div>
      <div v-if="imageUrl || upladImg" class="el-upload-list__item-actions">
        <span class="el-delect" @click.stop="handleRemove"></span>
        <div class="avatar-uploader-icon"><span></span>重新上传<Plus /></div>
      </div>
    </el-upload>
    <div class="Prompttext">
      图片大小不超过2M<br /><span
        >建议图片尺寸800*800<br /></span
      >仅能上传PNG JPG JPEG类型图片
    </div>
  </div>
</template>
<script setup>
import { ref, watch } from "vue";
import { ElMessage } from "element-plus";
import { Plus } from "@element-plus/icons-vue";
import proxy from '@/config/proxy';
import {TOKEN_NAME} from '@/config/global';
const env = import.meta.env.MODE || "development";
// 获取父组件值、方法
const props = defineProps({
  // 搜索对象
  isCourse: {
    type: Boolean,
    default: false,
  },
  upladImg: {
    type: String,
    default: "",
  },
});
// fileList,上传图片列表
const fileList = ref()
// 监听器，监听父组件传值，改变fileList，显示图片
watch(
  () => props.upladImg,
  (val) => {
    imageUrl.value = val;
  }
);
// 定义变量
let imageUrl = ref(""); //上传的图片路径
const emit = defineEmits(); //子组件获取父组件事件传值
let actions = proxy[env].host+'/ms/files' //上传图片地址
let uploadHeaders={"authorization":sessionStorage.getItem(TOKEN_NAME)}//上传图片时需要添加token
// ------定义方法------
// 获取上传的图片路径
const handleAvatarSuccess = (response) => {
  emit("getCoverUrl", response.data.path);
};
// 获取上传的图片路径
const handleChangeSuccess = () => {
};
// 上传前判断图片是否符合上传要求
const beforeAvatarUpload = (file) => {
  let types = ["image/jpeg", "image/jpg", "image/png"];
  let isImage = types.includes(file.type);
  let isLt2M = file.size / 1024 / 1024 < 2;
  // 判断图片格式
  if (!isImage) {
    ElMessage({

      message: "仅能是上传PNG、JPG、JPEG类型图片!",
      type: "error",
      showClose:false,
    });
    return false
  }
  // 判断图片大小
  if (!isLt2M) {
    ElMessage({

      message: "图片大小不超过 2MB!",
      type: "error",
      showClose:false,
    });
    return false
  }
  imageUrl.value = URL.createObjectURL(file);
  emit("getFlag", true);
};
// 删除图片
const handleRemove = () => {
  imageUrl.value = "";
  emit('setUplad','')
  emit("getFlag", false);
};
// 向父组件暴露方法
defineExpose({
	imageUrl,
});
</script>

<style lang="scss" scoped>
.avatar-uploader .el-icon-plus:after {
  position: absolute;
  display: inline-block;
  content: " " !important;
  left: calc(50% - 20px);
  top: calc(50% - 40px);
  width: 40px;
  height: 40px;
  background: url("./../../assets/icons/icon_upload.png") center center
    no-repeat;
  background-size: 20px;
}

.el-upload-list__item-actions:hover .upload-icon {
  display: inline-block;
}
.el-icon-zoom-in:before {
  content: "\E626";
}
.el-icon-delete:before {
  content: "\E612";
}
.el-upload-list__item-actions:hover {
  opacity: 1;
}
.upload-item {
  display: flex;
  align-items: center;
  .el-form-item__content {
    width: 500px !important;
  }
}
.upload-tips {
  font-size: 12px;
  color: #666666;
  display: inline-block;
  line-height: 17px;
  margin-left: 36px;
}
.el-upload-list__item-actions {
  position: absolute;
  width: 100%;
  height: 100%;
  left: 0;
  top: 0;
  cursor: default;
  text-align: center;
  color: #fff;
  opacity: 0;
  font-size: 20px;
  background-color: rgba(0, 0, 0, 0.5);
  transition: opacity 0.3s;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}
.avatar {
  width: 157px;
  height: 88px;
  display: block;
}
</style>
<style lang="scss" scoped>
.tearchUpload{
 .uploadBox{
    font-weight: 400;
    font-size: 12px;
    color: #887E7E;
    .avatar-uploader{
      width: 160px;
      height: 160px;
      :deep(.el-upload.el-upload){
        width: 160px;
        height: 160px;
      }
    }
  } 
}
  

</style>