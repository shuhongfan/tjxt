<!--视频预览-->
<template>
  <div class="preview" style="width: 60%">
    <el-dialog v-model="dialogFormVisible" :title="title">
      <span class="close" @click="handleClose"></span>
      <div class="video">
        <video id="videoRef" ref="videoRef"  preload="auto"></video>
      </div>
    </el-dialog>
  </div>
</template>
<script setup>
import {
  ref,
} from "vue";
import { ElMessage } from "element-plus";
// 接口api
import { getMediasSignature } from "@/api/media";
// 获取父组件值、方法
const props = defineProps({
  // 弹层隐藏显示
  dialogFormVisible: {
    type: Boolean,
    default: false,
  },
  // 媒资id
  mediaId: {
    type: String,
    default: "",
  },
});
// ------定义变量------
const emit = defineEmits(); //子组件获取父组件事件传值
const videoRef = ref(null);
// 初始化视频播放器并播放视频 视频ID、播放器签名
const player = ref(null);
const fileId = ref("");
const signature = ref("");

const getId = async (mediaId) => {
  
  await getMediasSignatureData(mediaId)
    player.value.loadVideoByID(
      {
        appID: '1312394356',
        fileID:fileId.value,
        psign: signature.value,
      }
    )
    player.value.currentTime(0)
    player.value.play()
    
};
// ------定义方法------
const initPlay = (fileID, psign) => {
  player.value = new TCPlayer(videoRef.value.id, {
    appID: "1312394356",
    fileID,
    psign,
    posterImage: true,
    autoplay: true,
    width: 100 + "%",
    preload: "auto",
    hlsConfig: {},
  });
  player.value.on('timeupdate', function() {
  });
  player.value.on('pause', function() {
  });
  player.value.on('play', function() {
  });
  player.value.on('ended', function() {
  });
  player.value.ready(() => {
    player.value.currentTime()
    player.value.play()
  })
};
// 通过媒资id获取视频的fileId
const getMediasSignatureData = async (mediaId) => {
  await getMediasSignature({ mediaId })
    .then((res) => {
      if (res.code == 200) {
        if (player.value == null) {
          fileId.value = res.data.fileId;
          signature.value = res.data.signature;
          initPlay(res.data.fileId, res.data.signature);
        }
      } else {
        ElMessage({
          message: res.data.msg,
          type: "error",
          showClose:false,
        });
      }
    })
    .catch(() => {});
};
// 关闭弹层
const handleClose = () => {
  player.value.pause();
  player.value = null
  emit("handleClose");
};
// 向父组件暴露方法
defineExpose({
  getId,
});
</script>

