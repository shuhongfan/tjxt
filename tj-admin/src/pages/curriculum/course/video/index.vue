<!--课程视频-->
<template>
  <div class="contentBox">
    <div class="courseList">
      <el-collapse accordion v-model="activeNames">
        <el-collapse-item v-for="(item, index) in itemData.value" :key="index">
          <template v-slot:title>
            <div class="titText">
              <span class="icon" v-if="item.sections.length > 0"></span>
              <div class="textL">
                <span
                  ><span v-if="index + 1 > 9">{{ index + 1 }}</span
                  ><span v-else>{{ "0" + (index + 1) }}</span></span
                >
                <span>{{ item.name }}</span>
              </div>
            </div>
          </template>
          <div class="itemCon" v-if="item.sections.length > 0">
            <div class="headTitle">
              <span>序号</span>
              <span style="margin-left: 14px">小节名称</span>
              <span class="textLeft">视频名称</span>
              <span>视频时长</span>
              <span>免费试看</span>
            </div>
            <div class="item">
              <ul>
                <li v-for="(val, i) in item.sections" :key="i">
                  <div class="leftLine"></div>
                  <div class="con">
                    <!-- 序号 -->
                    <div>
                      <span v-if="i + 1 > 9">{{ i + 1 }}</span
                      ><span v-else>{{ "0" + (i + 1) }}</span>
                    </div>
                    <div style="margin-left: 14px; color: #332929">
                      {{ val.name }}
                    </div>
                    <div class="videoName">
                      <div v-if="val.mediaName !== ''"  class="textLeft">
                        <span @click="handleSeeVideo(val.mediaId)"
                          >{{ ellipsis(val.mediaName,8) }} .mp4</span
                        >
                        <i
                          class="deleteIcon"
                          @click="handleDelete(val)"
                          style="margin: 0 0 4px 4px"
                        ></i>
                      </div>
                      <div v-else class="textLeft">
                        <span
                          class="textDefault"
                          @click="handleOpen(val.id)"
                          style="margin-left: 0px"
                          >选择视频</span
                        ><span class="textDefault">
                          <el-upload
                            class="upload-demo"
                            action="#"
                            :multiple="true"
                            :http-request="
                              (param) => httpRequest(param, val.id)
                            "
                            :accept="accept"
                            :limit="100"
                            :show-file-list="false"
                            :on-remove="handleRemove"
                            :on-change="handleChange(val.id)"
                            :on-progress="handleProgress"
                            :file-list="fileList"
                          >
                            <el-button size="small" type="primary"
                              >本地上传</el-button
                            >
                          </el-upload></span
                        >
                      </div>
                    </div>
                    <div>
                      {{
                        val.mediaDuration > 0
                          ? formatSeconds(val.mediaDuration)
                          : ""
                      }}
                    </div>
                    <div class="textWarning">
                      <el-switch
                        v-model="val.trailer"
                        active-color="#00BE76"
                        active-text="试看3分钟"
                        :disabled="free"
                        @change="handleTrailer($event, val)"
                      >
                      </el-switch>
                    </div>
                  </div>
                </li>
              </ul>
            </div>
            <div class="cover"></div>
          </div>
        </el-collapse-item>
      </el-collapse>
    </div>
    <!-- 添加媒资视频弹层 -->
    <AddVideo
      :dialogVisible="dialogVisible"
      :itemData= "itemData.value"
      @setVideoInfo="setVideoInfo"
      @handleClose="handleClose"
    ></AddVideo>
    <!-- end -->
    <!-- 预览弹层 -->
    <Preview
      ref="preview"
      :title="title"
      :mediaId="mediaId"
      :dialogFormVisible="dialogFormVisible"
      @handleClose="handlePreviewClose"
    ></Preview>
    <!-- end -->
  </div>
</template>
<script setup>
import { ref, reactive, onMounted, nextTick, watch } from "vue"
import { useRouter, useRoute } from "vue-router"
import { ElMessage, ElLoading } from "element-plus"
import TcVod from "vod-js-sdk-v6"
import { formatSeconds,ellipsis } from '@/utils/index'
// 接口
import {
  getCoursesCatalogue,
  baseVideoSave,
  getCoursesDetail,
} from "@/api/curriculum"
import { getMediaUpload, mediaUpload } from "@/api/media"
// 导入组件
// 删除弹层
import Delete from "@/components/Delete/index.vue"
// 添加视频弹层
import AddVideo from "./addVideo.vue"
// 预览弹层
import Preview from "@/components/Preview/index.vue"
// ------定义变量------
// 获取父组件值、方法
const props = defineProps({
  // // 课程id
  // courseId: {
  //   type: Number,
  //   default: 0,
  // },
})
const router = useRouter() //获取全局
const route = useRoute() //获取局部
const emit = defineEmits() //子组件获取父组件事件传值
let itemData = reactive([]) //目录数据
let dialogVisible = ref(false) //选择视频列表弹层
let dialogFormVisible = ref(false) //弹层隐藏显示
let sectionId = ref("") //小节id
let uploaderG = ref(null)//定义全局用于取消上传
let videoName = ref("") //上传的视频名称
let loadingInstance = ref(null)
let free = ref(false) //是否免费
let courseId = route.params.id //课程id
let mediaId = ref("")//视频id
let preview = ref(null)
const startLoading = () => {
  loadingInstance = ElLoading.service({
    lock: true,
    text: "视频上传中…",
    background: "rgba(51, 51, 51, 0.4)",
  })
}
// ------生命周期------
onMounted(() => {
  getCatalogue()//获取目录数据
  getDetailData()//
})
watch("itemData", (newValue, oldValue) => {
  itemData.value = newValue
})
// ------定义方法------
// 获取目录数据
const getCatalogue = async () => {
  let params = {
    id: courseId,
    see: false,
    withPractice: 0 //是否带着练习题，1：带着练习题，0：不带练习题，默认1
  }
  await getCoursesCatalogue(params)
    .then((res) => {
      if (res.code === 200) {
        itemData.value = res.data
      }
    })
    .catch((err) => { })
}
// 获取详情
let getDetailData = async () => {
  await getCoursesDetail(courseId)
    .then((res) => {
      if (res.code === 200) {
        free.value = res.data.free
      }
    })
    .catch((err) => { })
}
// 提交
const handleSubmit = async (str) => {
  let arr = []
  let data = {}
  itemData.value.map((obj) => {
    obj.sections.map((val) => {
      data = {
        cataId: val.id,
        mediaId: val.mediaId,
        trailer: val.trailer,
        videoName: val.mediaName,
        mediaDuration: val.mediaDuration,
      }
      arr.push(data)
    })
  })
  let params = {
    datas: arr,
    id: courseId,
  }
  await baseVideoSave(params)
    .then((res) => {
      if (res.code === 200) {
        ElMessage({

          message: "恭喜你，操作成功！",
          type: "success",
          showClose:false,
        })
        emit("getActive", 3)
        if (str === "getback") {
          router.push({
            path: "/curriculum/index",
          })
        }
      } else {
        ElMessage({

          message: res.data.msg,
          type: "error",
          showClose:false,
        })
      }
    })
    .catch((err) => { })
}
// 获取签名 这里的签名请求是由后端提供的，只需要拿到后端给的签名请求即可
const getVodSignature = async () => {
  await getMediaUpload()
    .then((res) => { })
    .catch((err) => { })
}
// 自定义上传
const httpRequest = (file, id) => {
  sectionId.value = id
  // 限制视频格式
  if (["video/mp4", "video/quicktime"].indexOf(file.file.type) == -1) {
    ElMessage({

      message: "仅支持上传mp4格式的文件!",
      type: "error",
      showClose:false,
    })
    return false
  }

  // 获取视频签名
  const getSignature = async function () {
    return await getMediaUpload({
      //这里就是发axios请求
    }).then((res) => {
      return res.data
    })
  }
  startLoading()
  // 前文中所述的获取上传签名的函数
  const tcVod = new TcVod({
    getSignature: getSignature,
  })
  videoName.value = file.file.name //获取视频名称
  const uploader = tcVod.upload({
    mediaFile: file.file, // 这里腾讯云需要获取到file文件里的name，根据你file结构进行填写
    fileParallelLimit: 1,
    chunkParallelLimit: 1,
  })
  uploaderG.value = uploader // 定义全局用于取消上传
  // 上传进度
  uploader.on("media_progress", function (info) {
    if (info.percent === 1) {
      // 上传成功后关闭tost加载弹层
      loadingInstance.close()
    }
  })
  // 视频上传完成时
  uploader.on("media_upload", function (info) {
    // uploaderInfo.isVideoUploadSuccess = true;
  })

  // 上传成功
  uploader
    .done()
    .then(async (doneResult) => {
      // 这里发请求给后端进行转码操作
      let val = {
        fileId: doneResult.fileId, // 腾讯云file_id
        // video_type: "operating_activity", // 视频类型
        // filename: videoName.value, // 视频名称
        // mediaUrl:
        //   doneResult.video && doneResult.video.url ? doneResult.video.url : "", // 视频地址
      }
      await mediaUpload(val)
        .then((res) => {
          const data = res.data
          if (res.code === 200) {
            loadingInstance.close()
            ElMessage({

              message: "上传成功",
              type: "success",
              showClose:false,
            })
            itemData.value.map((obj) => {
              obj.sections.map((ele) => {
                if (sectionId.value === ele.id) {
                  ele.mediaName = data.filename
                  ele.mediaId = data.id
                  ele.mediaDuration = data.duration
                }
              })
            })
          }
        })
        .catch((error) => { })
    })
    .catch((err) => {
    })
}
// 打开选择视频列表弹层
const handleOpen = (id) => {
  sectionId.value = id
  dialogVisible.value = true
}
// 上传视频前获取小节id
const handleChange = (id) => {
  // sectionId.value = id;
}
//获取视频信息
const setVideoInfo = (value) => {
  itemData.value.map((obj) => {
    obj.sections.map((val) => {
      if (sectionId.value === val.id) {
        val.mediaName = value.filename
        val.mediaDuration = value.duration
        val.mediaId = value.id
      }
    })
  })
}
// 是否观看
const handleTrailer = (e, value) => {
  itemData.value.map((obj) => {
    obj.sections.map((val) => {
      if (value.id === val.id) {
        val.trailer = e
      }
    })
  })
}
// 关闭视频 弹层
const handleClose = () => {
  dialogVisible.value = false
}
// 删除视频
const handleDelete = (value) => {
  itemData.value.map((obj) => {
    obj.sections.map((val) => {
      if (value.id === val.id) {
        val.mediaName = ""
        val.mediaDuration = ""
      }
    })
  })
}
// 视频观看
const handleSeeVideo = (id) => {
  mediaId.value = id
  preview.value.getId(id)
  dialogFormVisible.value = true
}
// 关闭弹层
const handlePreviewClose = () => {
  dialogFormVisible.value = false
};
// 向父组件暴露方法
defineExpose({
  handleSubmit,
});
</script>
<style lang="scss" scoped>
:deep(.videoBox .el-dialog){
  width: 1096px;
}
.courseList .el-collapse, .courseList .el-collapse-item__wrap{
  min-width: 1065px;
}
:deep(.videoBox .el-dialog){
  min-width: 1096px !important;
}
</style>