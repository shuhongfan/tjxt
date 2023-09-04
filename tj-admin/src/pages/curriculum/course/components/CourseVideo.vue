<template>
  <div class="contentBox">
    <div class="courseList">
      <el-collapse accordion v-model="activeNames">
        <el-collapse-item v-for="(item, index) in free" :key="index">
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
              <span>小节名称</span>
              <span>视频名称</span>
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
                    <div>
                      {{ val.name }}
                    </div>
                    <div class="videoName">
                      <div style="display:flex;justify-content: center;">
                        <span @click="handleSeeVideo(val.mediaId)">{{ val.mediaName!==''?ellipsis(val.mediaName,10)+'.mp4':null }}</span>
                      </div>
                    </div>
                    <div>{{formatSeconds(val.mediaDuration)}}</div>
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
        <!-- 预览弹层 -->
      <Preview
      ref="preview"
      :title="title"
      :mediaId="mediaId"
      :dialogFormVisible="dialogFormVisible"
      @handleClose="handlePreviewClose"
    ></Preview>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted, watch } from "vue";
import {formatSeconds} from '@/utils/index'
import {ellipsis} from "@/utils/index"
// 预览弹层
import Preview from "@/components/Preview/index.vue"
// 导入组件

// ------定义变量------
// 获取父组件值、方法
const props = defineProps({
  courseVideoData:{
    type: Object,
    default:{}
  },
});
let itemData = reactive([]); //目录数据
let free = ref(false); //是否购买，是否免费
let mediaId = ref("")//视频id
let preview = ref(null)
let dialogFormVisible = ref(false) //弹层隐藏显示


watch("itemData", (newValue, oldValue) => {
  itemData.value = newValue
})
watch(props, () => {
  getDetailData();
  // console.log(props.courseVideoData);
})
// ------定义方法------
// 获取详情
let getDetailData = () => {
  free.value = props.courseVideoData
};

// 是否观看
const handleTrailer = (e, value) => {
  itemData.value.map((obj) => {
    obj.sections.map((val) => {
      if (value.id === val.id) {
        val.trailer = e;
      }
    });
  });
};
// 视频观看
const handleSeeVideo = (id) => {
  mediaId.value = id
  preview.value.getId(id)
  dialogFormVisible.value = true
}
// 关闭视频 弹层
const handlePreviewClose = () => {
  dialogFormVisible.value = false;
};

</script>
<style lang="scss">
  .courseList{
  margin-top: 40px;
}
.el-loading-parent--relative .el-loading-mask {
  width: 150px !important;
  height: 40px !important;
  line-height: 40px !important;
  border-radius: 8px !important;
  transform: translate(-50%, -50%) !important;
  left: 50% !important;
  top: 50% !important;
  &.is-fullscreen .el-loading-spinner {
    display: flex;
    align-items: center;
    align-content: center;
    justify-content: center;
    .circular {
      width: 14px;
      height: 14px;
      margin-right: 8px;
    }
    .path {
      stroke: #00be76;
    }
    .el-loading-text {
      color: #fff;
    }
  }
}
</style>
<style lang="scss" scoped>
  .videoBox{
    .searchBox{
      padding-bottom: 5px;
    }
    .tableBox{
      padding-top: 0;
      :deep(.el-table) {
       .el-table__header-wrapper{
        .el-table__cell{
          color: #332929;
          padding: 0 !important;
        }
       } 
      }
      :deep(.el-table__row){
        color: #332929;
      }
    }
    :deep(.el-radio){
        margin-left: 45px;
    }
  } 
  :deep(.el-input){
    .el-input__wrapper{
    .el-input__inner{
      &::placeholder{
        color: #B5ABAB;
      }
    }
  }
  }
  :deep(.el-dialog){
    .el-dialog__body{
      padding: 20px 32px 32px 30px ;
    }
  }
  :deep(.el-scrollbar){
    overflow: initial;
    .el-scrollbar__bar{
      position: absolute;
      right: -30px;
  }
  }
  :deep(.el-table__body-wrapper){
    overflow: initial;
  }
  :deep(.el-table){
    overflow: initial;
  }
  
   //调整表头高度
   :deep(.el-table__header tr),
    :deep(.el-table__header th) {
      padding: 0;
      height: 40px;
    }
    :deep(.topicBox){
      .tableBox{
        .el-table{
          .el-table__header-wrapper{
            .el-table__cell{
              padding: 0;
            }
          }
        }
      }
    }
  
  </style>
  <style lang="scss" scoped>
    :deep(.el-switch.is-checked .el-switch__core){
    border-color: transparent;
  }
  :deep(.el-scrollbar__bar.is-vertical>div){
      background-color: rgba(0,0,0,0.45);
    }
    
.courseList .titText .textL span{
  font-size: 16px;
  color: #332929;
}
.headTitle{
  color: #332929;
}
.textR{
  font-size: 14px;
  font-family: PingFangSC-Regular;
  font-weight: 400;
}
.courseList .itemCon .headTitle > span:nth-child(2), .courseList .itemCon .headTitle > div:nth-child(2), .courseList .itemCon .item li .con > span:nth-child(2), .courseList .itemCon .item li .con > div:nth-child(2){
  width: auto;
  min-width: 300px;
  }

  </style>
  
