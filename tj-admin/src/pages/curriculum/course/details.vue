<!-- 教师列表-->
<template>
  <div class="contentBox">
    <div class="bg-wt marg-tp-20">
      <div class="headerBox">
        <!-- 创建左右两个分别放图片展示和内容概要 -->
        <div class="headerBox-left">
          <div>
            <img
              :src="courseData.data.coverUrl"
              alt=""
              style="width: 382px; height: 215px; border-radius: 8px"
            />
          </div>
        </div>
        <div class="headerBox-right">
          <div class="title">
            {{ courseData.data.cateNames }}
            <!-- 1：待上架，2：已上架，3：下架，4：已完结 -->
            <span v-if="courseData.data.status == 1" class="waitgrounding"
              >待上架</span
            >
            <span v-else-if="courseData.data.status == 2" class="ongrounding"
              >已上架</span
            >
            <span v-else-if="courseData.data.status == 3" class="undercarriage"
              >已下架</span
            >
            <span v-else-if="courseData.data.status == 4" class="end"
              >已完结</span
            >
          </div>
          <div class="item-card">
            <div class="card">
              <div class="tit">报名人数</div>
              <div class="info">{{ courseData.data.enrollNum }}人</div>
            </div>
            <div class="card">
              <div class="tit">学习人数</div>
              <div class="info">{{ courseData.data.studyNum }}人</div>
            </div>
            <div class="card">
              <div class="tit">退款人数</div>
              <div class="info">{{ courseData.data.refundNum }}人</div>
            </div>
            <div class="card bd-non">
              <div class="tit">课程实付金额</div>
              <div class="info">
                {{ (courseData.data.price / 100).toFixed(2) }}元
              </div>
            </div>
          </div>
          <div class="item-card-bottom">
            <div class="card-bottom">
              <div class="tit-botom">
                课时数量：
                <span class="text-bottom">{{
                  courseData.data.cataTotalNum
                }}</span>
              </div>
              <div class="tit-botom">
                课程创建人：
                <span class="text-bottom">{{
                  courseData.data.createrName
                }}</span>
              </div>
              <div class="tit-botom">
                创建时间：
                <span class="text-bottom">{{
                  formatTimeOrdinary(courseData.data.createTime)
                }}</span>
              </div>
            </div>
            <div class="card-bottom">
              <div class="tit-botom">
                课程评分：
                <span class="text-bottom">{{
                  courseData.data.coureScore
                }}</span>
              </div>
              <div class="tit-botom">
                课程更新人：
                <span class="text-bottom">{{
                  courseData.data.updaterName
                }}</span>
              </div>
              <div class="tit-botom">
                更新时间：
                <span class="text-bottom">{{
                  formatTimeOrdinary(courseData.data.updateTime)
                }}</span>
              </div>
            </div>
          </div>
          <div class="bg-wt radius marg-tp-20"></div>
        </div>
      </div>
      <div class="bodyBox detailBox">
        <TableSwitchBar
          :data="tableBar"
          @changeTable="changeTable"
        ></TableSwitchBar>
        <!-- 课程介绍 -->
        <CourseAbout
          v-show="actId == 1"
          :courseData="courseData.data"
          :courseTeacher="courseTeacher"
        ></CourseAbout>
        <!-- 课程目录 -->
        <courseCatalogue
          v-show="actId == 2"
          :courseListData="courseListData.value"
        ></courseCatalogue>
        <!-- 课程视频 -->
        <CourseVideo
          v-show="actId == 3"
          :courseVideoData="courseListData.value"
        ></CourseVideo>
        <!-- 课程题目 -->
        <Topic
          v-show="actId == 4"
          :courseTopicData="courseListData.value"
        ></Topic>
      </div>
      <div class="BoxBottom">
        <div class="btn">
          <el-button class="button primary" @click="handleGetback"
            >返回</el-button
          >
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from "vue"
import { useRoute, useRouter } from "vue-router"
import { formatTimeOrdinary } from "@/utils/index";
// 获取vuex存储数据
import { useUserStore } from "@/store"
// 公用数据

// 接口api
import { getCourseDetail, getcourseTeacher, getcoursesListData } from "@/api/curriculum"
// 导入组件
import TableSwitchBar from "./components/TableSwitch.vue" // 切换表格
import CourseAbout from "./components/CourseAbout.vue"// 课程介绍
import courseCatalogue from "./components/courseCatalogue.vue"// 课程目录
import CourseVideo from "./components/CourseVideo.vue"// 课程视频
import Topic from "./components/detailTopic.vue" // 课程题目
// ------定义变量------

//初始化路由
const router = useRouter()
const route = useRoute()
//搜索对象
// 所有的详细数据
const courseData = reactive({
  data: {},
})
const courseTeacher = reactive({
  data: [],
})
//新增编辑表单数据
const detailId = ref(null)//当前课程详情id

// table切换数据 - 静态数据
const tableBar = [{ id: 1, name: '课程介绍' }, { id: 2, name: '课程目录' }, { id: 3, name: '课程视频' }, { id: 4, name: '课程题目' }]
// 课程目录数据
const courseListData = reactive({
})
// ------mounted周期------
onMounted(async () => {
  // init()是初始化函数
  // 获取当前的id
  detailId.value = route.params.id
  // 获取课程信息-详情
  await getcourseDetailData()
  // 获取课程信息-讲师
  await getcourseTeacherData()
  await getcourseListData()

})

// ------定义方法------

// 获取课程详情数据
const getcourseDetailData = async () => {
  await getCourseDetail(detailId.value)
    .then((res) => {
      if (res.code === 200) {
        // courseData.value = res.data;
        courseData.data = res.data
      }
      else {
        ElMessage({
          message: res.data.msg,
          type: 'error',
          showClose: false,
        })
      }
    })
    .catch((err) => { })
}
// 获取课程讲师详情数据
const getcourseTeacherData = async () => {
  await getcourseTeacher(detailId.value)
    .then((res) => {
      if (res.code === 200) {
        courseTeacher.data = res.data
      }
      else {
        ElMessage({
          message: res.data.msg,
          type: 'error',
          showClose: false,

        })
      }
    })
    .catch((err) => { })
}
// 获取课程详情数据
const getcourseListData = async () => {
  await getcoursesListData(detailId.value)
    .then((res) => {
      if (res.code == 200) {
        courseListData.value = res.data
        // console.log(courseListData.value);
      } else {
        ElMessage({
          message: res.data.msg,
          type: 'error',
          showClose: false,

        })
      }
    })
    .catch(() => {
    })
}
// 底部返回按钮
const handleGetback = (row) => {
  router.push({
    path: "/curriculum",
  })
}
// 当前的table选项
const actId = ref(1)
// table切换 当前展示信息 课程介绍、课程目录
const changeTable = id => {
  actId.value = id
  switch (id) {
    case 2: {
      break
    }
    case 3: {
      break
    }
    case 4: {
      break
    }
  }
}
</script>
<style src="../index.scss" lang="scss" scoped></style>
<style lang="scss" scoped>
.contentBox {
  .bg-wt {
    background-color: #f4f8fb;
    .headerBox {
      background-color: #fff;
      // width: 1100px;
      height: 278px;
      padding: 31px 0 31px 30px;
      display: flex;
      margin: 0, auto;
      border-radius: 12px;
      .headerBox-right {
        .title {
          width: 481px;
          height: 33px;
          font-family: PingFangSC-Medium;
          font-weight: 500;
          font-size: 24px;
          color: #332929;
          letter-spacing: 0;
          position: relative;
        }
        .item-card {
          margin-bottom: 40px;
          display: flex;
          margin-top: 22px;
        }
        .item-card-bottom {
          width: 577px;
          height: 72px;
          background: #fdfcfa;
          margin-bottom: 41px;
          margin-top: 22px;
          padding: 12px 19.5px 13px 19.5px;
          .card-bottom {
            display: flex;
            justify-content: space-between;
            // 将里面的内容全部左对齐;
            &:nth-child(2) {
              margin-top: 10px;
            }
            .tit-botom {
              font-family: PingFangSC-Regular;
              font-weight: 400;
              font-size: 14px;
              color: #887e7e;
              letter-spacing: 0;
              text-align: left;
              // 将第一个的宽度设置为110px
              &:first-child {
                width: 110px;
              }
              &:nth-child(2) {
                display: flex;
                .text-bottom {
                  width: 65px;
                }
              }
              &:last-child {
                width: 230px;
              }
              .text-bottom {
                font-family: PingFangSC-Regular;
                font-weight: 400;
                font-size: 14px;
                color: #332929;
                letter-spacing: 0.16px;
              }
            }
          }
        }
        .card {
          border-right: 1px solid rgb(245, 239, 238);
          margin-right: 30px;
          padding-right: 30px;
          .tit {
            width: 84px;
            height: 20px;
            font-family: PingFangSC-Regular;
            font-weight: 400;
            font-size: 14px;
            color: #887e7e;
            letter-spacing: 0;
            margin-bottom: 10px;
          }
          .info {
            // width: 40px;
            height: 25px;
            font-family: PingFangSC-S0pxibold;
            font-weight: 600;
            font-size: 18px;
            color: #332929;
            letter-spacing: 0;
          }
        }
      }
    }
    .bodyBox {
      // width: 1100px;
      // height: 1229px;
      background: #fff;
      border-radius: 12px;
      margin-top: 20px;
      padding: 38px 29px 40px 30px;
      border-bottom-left-radius: 0;
      border-bottom-right-radius: 0;
    }
    .BoxBottom {
      height: 100px;
      border-top: 1px solid #f5efee;
      // margin-bottom: 20px;
      background: #ffffff;
      border-radius: 0 0 12px 12px;
      .btn {
        // 让按钮居中
        display: flex;
        justify-content: center;
        align-items: center;
        padding-bottom: 30px;
        .button {
          width: 130px;
          height: 40px;
          color: #fff;
          background: #ff734f;
          border-radius: 20px;
          // margin-top: -50px;
        }
      }
    }
  }
}
.waitgrounding {
  margin-left: 10px;
}
.ongrounding {
  margin-left: 10px;
  position: absolute;
  // left: 288PX;
  top: 3px;
}
.undercarriage {
  margin-left: 10px;
}
.end {
  margin-left: 10px;
}
.headerBox-left {
  margin-right: 40px;
}
.contentBox {
  margin-bottom: 20px;
}
.detailBox {
  margin-bottom: 0;
}
</style>
