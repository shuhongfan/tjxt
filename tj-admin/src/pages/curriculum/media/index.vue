<!-- 媒资列表-->
<template>
  <div class="contentBox">
    <!-- 搜索 -->
    <Search :searchData="searchData" @handleSearch="handleSearch"></Search>
    <!-- end -->
    <div class="bg-wt radius marg-tp-20">
      <div class="tableBox">
        <div class="conHead pad-30">
          <!-- 上传视频 -->
          <div class="addBox">
            <el-upload
              class="upload-demo"
              action="#"
              :multiple="true"
              :http-request="httpRequest"
              :accept="accept"
              :limit="100"
              :show-file-list="false"
              :on-remove="handleRemove"
              :on-change="handleChange"
              :on-progress="handleProgress"
              :file-list="fileList"
            >
              <el-button
                size="small"
                type="primary"
                style="
                  font-family: PingFangSC-Medium;
                  font-weight: 500;
                  font-size: 14px;
                "
                >上传视频</el-button
              >
            </el-upload>
            <!-- <el-button
              class="button buttonSub"
              v-if="activeName === 'second'"
              @click="handleBatchStop"
              >批量暂停</el-button
            >
            <el-button
              class="button buttonSub"
              v-if="activeName === 'second'"
              @click="handleBatchDelet"
              >批量删除</el-button
            > -->
          </div>
          <!-- end -->
          <!-- tab -->
          <div class="tab">
            <el-tabs
              v-model="activeName"
              class="demo-tabs"
              @tab-click="handleClick"
            >
              <el-tab-pane label="已上传" name="first"> </el-tab-pane>
              <el-tab-pane label="上传中" name="second"></el-tab-pane>
            </el-tabs>
          </div>
          <!-- end -->
        </div>
        <!-- 已上传列表 -->
        <TableList
          v-if="activeName === 'first'"
          :baseData="baseData.value"
          :total="total"
          :loading="loading"
          :pageSize="searchData.pageSize"
          :isSearch="isSearch"
          @getList="getList"
          @handleSizeChange="handleSizeChange"
          @handleCurrentChange="handleCurrentChange"
        ></TableList>
        <!-- end -->
        <!-- 上传中列表 -->
        <UploadCenter
          v-else
          ref="upload"
          :baseData="fileList"
          :total="total"
          :loading="loading"
          :getList="getList"
          :videoFlag="videoFlag"
          :videoUploadPercent="videoUploadPercent"
          :pageSize="searchData.pageSize"
          @continueUpload="continueUpload"
          @suspendUpload="suspendUpload"
          @handleSizeChange="handleSizeChange"
          @handleCurrentChange="handleCurrentChange"
          @deleteUpload="deleteUpload"
        ></UploadCenter>
        <!-- end -->
      </div>
    </div>
    <!-- 上传视频结束后弹层 -->
    <UploadVideo
      :dialogVisible="uploadVideoVisible"
      :videoNumber="videoNumber"
      @handleClose="handleClose"
    ></UploadVideo>
    <!-- end -->
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from "vue"
import TcVod from "vod-js-sdk-v6"
import { ElMessage } from "element-plus"
// 公用数据
import { statusData } from "@/utils/commonData"
// 导入接口
import { getMedia, getMediaUpload, mediaUpload } from "@/api/media"
// 导入组件
// 搜索
import Search from "./components/Search.vue"
// 已上传
import TableList from "./components/TableList.vue"
// 已上传
import UploadCenter from "./components/UploadCenter.vue"
// 上传视频结束后弹层
import UploadVideo from "@/components/Video/index.vue"
// ------定义变量------
const loading = ref(false)
let total = ref(null) //数据总条数
let upload = ref()
const activeName = ref("first") //当前选中的tab值
let searchData = reactive({
  pageSize: 10,
  pageNo: 1,
  sortBy: "id",
  isAsc: false,
}) //搜索对象
let baseData = reactive([]) //表格数据
let fileList = ref([]) //上传列表
let videoName = ref("") //上传的视频名称
let uploaderG = ref([])
let isSearch = ref(false) //是否触发了搜索按钮,用来控制没有搜索出数据和正常列表无数据的区分，显示的图片和提示语不一样
let tcVod = null
let uploader = {}
let videoNumber = ref() //共上传视频数量
let videoSucceedNumber = ref() //上传视频成功数量
let videoFailNumber = ref() //上传视频成功数量
let uploadVideoVisible = ref(false) //上传视频结束后提示弹层
// ------生命周期------
onMounted(() => {
  init()
})
// ------定义方法------
// 获取初始值
const init = () => {
  getList()
  // 获取视频签名
  const getSignature = async function () {
    return await getMediaUpload({
      //这里就是发axios请求
    }).then((res) => {
      return res.data
    })
  }

  // 前文中所述的获取上传签名的函数
  tcVod = new TcVod({
    getSignature: getSignature,
  })
}
// 获取列表值
const getList = async () => {
  loading.value = true
  await getMedia(searchData)
    .then((res) => {
      if (res.code === 200) {
        loading.value = false
        baseData.value = res.data.list
        total.value = res.data.total
      }
    })
    .catch((err) => { })
}
// 触发tab切换
const handleClick = () => {
  if (activeName.value === "first") {
    getList()
  }
}
// 获取签名 这里的签名请求是由后端提供的，只需要拿到后端给的签名请求即可
// const getVodSignature = async () => {
//   await getMediaUpload()
//     .then((res) => {})
//     .catch((err) => {});
// };
// 文件列表改变时 将文件列表保存到本地
const handleChange = (file, fileItem) => {
  activeName.value = "second"
  fileItem.forEach((val,index) => {
    // 限制视频格式
    if (["video/mp4", "video/quicktime"].indexOf(val.raw.type) == -1) {
      fileItem.splice(index, 1)
      return false
    }
  })
fileList.value=fileItem 
}
// 自定义上传
const httpRequest = (file) => {

  // 显示进度条从0开始
  fileList.value.map((val) => {
    val.videoFlag = true
    val.videoUploadPercent = 0
  })
  videoName.value = file.file.name //获取视频名称

  const startLength = videoName.value.lastIndexOf(".") + 1
  const type = videoName.value.substr(startLength).toLowerCase()
  if (type === "mp4" || type === "mpg" || type === "vob") {
    const isLt2G = file.file.size / 1024 / 1024 < 2048
    if (!isLt2G) {
      ElMessage({

        message: "上传视频大小不能超过2G!",
        type: "success",
        showClose:false,
      })
      return false
    } else {
      uploader = tcVod.upload({
        mediaFile: file.file, // 这里腾讯云需要获取到file文件里的name，根据你file结构进行填写
        fileParallelLimit: 1,
        chunkParallelLimit: 1,
      })
      uploaderG.value.push(uploader) // 定义全局用于取消上传
      // 视频上传完成时
      uploader.on("media_upload", function (info) {
        // uploaderInfo.isVideoUploadSuccess = true;
      })
      // 进度
      uploader.on("media_progress", (info) => {
        fileList.value.map((val) => {
          if (val.size === info.total) {
            val.videoUploadPercent = parseInt(info.percent * 100)
            if (info.percent === 1) {
              val.videoFlag = true
            }
          }
        })
      })

      // 上传成功
      uploader
        .done()
        .then(async (doneResult) => {
          videoNumber.value = fileList.value.length
          videoSucceedNumber.value = videoNumber.value
          fileList.value.map((val, index) => {
            if (val.videoFlag) {
              fileList.value.splice(index, 1)
            }
          })
          if (fileList.value.length === 0) {
            uploadVideoVisible.value = true //打开上传视频结束后的弹层
          }
          // 这里发请求给后端进行转码操作
          let val = {
            fileId: doneResult.fileId, // 腾讯云file_id
            // video_type: "operating_activity", // 视频类型
            filename: videoName.value, // 视频名称
            mediaUrl:
              doneResult.video && doneResult.video.url
                ? doneResult.video.url
                : "", // 视频地址
          }
          await mediaUpload(val)
            .then((res) => {
              if (res.code === 200) {
                getList()
              }
            })
            .catch((error) => { })
        })
        .catch((error) => {
          ElMessage({

            message: error,
            type: "success",
          })
        })
    }
  } else {
    ElMessage({

      message: "仅支持上传mp4格式的文件!",
      type: "error",
      showClose:false,
    })
    return false
  }
}
// 恢复上传
const continueUpload = (row) => {
  // 续传视频
  uploaderG.value.forEach((obj, index) => {
    // 判断触发的是那条视频
    // 可以用uid做对比
    if (row.uid === obj.videoFile.uid) {
      const upload = tcVod.upload({
        mediaFile: obj.videoFile,
      })
      // 把续传的数据赋值给uploaderG
      uploaderG.value[index] = upload
      fileList.value.forEach((item) => {
        if (item.uid === obj.videoFile.uid) {
          item.isStop = false
          // 续传进度，暂停前的进度加上正在上传的进度
          upload.on("media_progress", (info) => {
            const num = item.videoUploadPercent / 100 + info.percent
            if (num <= 1) {
              item.videoUploadPercent = parseInt(num * 100)
            }

            if (num === 1) {
              item.videoFlag = true
            }
          })

          // 继续上传成功
          upload
            .done()
            .then(async (doneResult) => {
              videoNumber.value = fileList.value.length
              videoSucceedNumber.value = videoNumber.value
              fileList.value.map((val, index) => {
                if (val.videoFlag) {
                  fileList.value.splice(index, 1)
                }
              })
              if (fileList.value.length === 0) {
                uploadVideoVisible.value = true //打开上传视频结束后的弹层
              }
              // 这里发请求给后端进行转码操作
              let val = {
                fileId: doneResult.fileId, // 腾讯云file_id
                // video_type: "operating_activity", // 视频类型
                filename: videoName.value, // 视频名称
                mediaUrl:
                  doneResult.video && doneResult.video.url
                    ? doneResult.video.url
                    : "", // 视频地址
              }
              await mediaUpload(val)
                .then((res) => {
                  if (res.code === 200) {
                    getList()
                  }
                })
                .catch((error) => { })
            })
            .catch((err) => { })
        }
      })
    }
  })
}
// 暂停上传
const suspendUpload = (row) => {
  uploaderG.value.forEach((obj, index) => {
    // 对比触发的时那条视频
    if (row.uid === obj.videoFile.uid) {
      // 取消上传的方法
      obj.cancel()
    }
    // 遍历上传中的数据，判断是暂停还是继续上传
    fileList.value.forEach((item) => {
      if (row.uid === item.uid) {
        item.isStop = true
      }
    })
  })
}
//取消上传
const uploadVideoFileCancel = () => { }
const handleProgress = (event, file, fileList) => { }
// 搜索
const handleSearch = () => {
  isSearch.value = true //是否触发了搜索按钮
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

// 删除正在上传的视频
const deleteUpload = (row) => {
  uploaderG.value.forEach((obj) => {
    // 先取消上传视频
    if (row.uid === obj.videoFile.uid) {
      obj.cancel()
    }
    // 再把次视频从列表里删除
    fileList.value.forEach((item, index) => {
      if (item.uid === obj.videoFile.uid) {
        fileList.value.splice(index, 1)
        upload.value.dialogDeleteVisible = false
      }
    })
  })
}
// 关闭上传结束后弹层
const handleClose = () => {
  uploadVideoVisible.value = false //关闭上传视频结束后的弹层
  activeName.value = "first"
};
</script>
<style src="./../index.scss" lang="scss" scoped></style>
<style lang="scss" scoped>
.contentBox{
  margin-bottom: 20px;
}
</style>
