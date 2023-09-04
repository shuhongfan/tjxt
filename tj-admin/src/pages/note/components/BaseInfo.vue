<!--笔记课程信息-->
<template>
  <div class="orderInfo">
    <!-- 标题 -->
    <div class="tit"><span>笔记课程信息</span></div>
    <!-- end -->
    <div class="noteItem">
      <table class="tableBox">
        <tr>
          <td>ID编号</td>
          <td colspan="2">{{fromData.id}}</td>
          <td>课程分类</td>
          <td colspan="2">{{fromData.categoryNames}}</td>
        </tr>
        <tr>
          <td>所属章节</td>
          <td colspan="5">{{fromData.chapterName}}/{{fromData.chapterName}}</td>
        </tr>
        <tr>
          <td>所选时间</td>
          <td width="20%">{{formatSeconds(fromData.noteMoment)}}</td>
          <td>被引用次数</td>
          <td>{{fromData.usedTimes?fromData.usedTimes:0}}</td>
          <td width="60%">是否展示</td>
          <td width="20%">{{!fromData.hidden?'显示':'隐藏'}}</td>
        </tr>
      </table>
      <!-- <ul>
        <li>
          <div class="tit">ID编号</div>
          <div>ID编号</div>
          <div class="tit">课程分类</div>
          <div>ID编号</div>
        </li>
        <li>
          <div class="tit">所属章节</div>
          <div>ID编号</div>
        </li>
        <li>
          <div class="tit">所选时间</div>
          <div>ID编号</div>
          <div class="tit">点赞数量</div>
          <div>ID编号</div>
          <div class="tit">被引用次数</div>
          <div>ID编号</div>
          <div class="tit">是否展示</div>
          <div>ID编号</div>
        </li>
      </ul> -->
    </div>
    <div>
    <!-- 标题 -->
    <div class="tit"><span>笔记内容</span></div>
    <!-- end -->
    <!-- 笔记内容 -->
    <div class="noteItem">
      <table class="tableBox">
        <tr>
          <td>笔记作者</td>
          <td>{{fromData.authorName}}</td>
          <td>作者手机号</td>
          <td>{{fromData.authorPhone}}</td>
          <td>笔记发布时间</td>
          <td>{{formatTimeOrdinary(fromData.createTime)}}</td>
        </tr>
        <tr>
          <td>引用人信息</td>
          <td colspan="5">{{fromData.gathers?fromData.gathers.toString():null}}</td>
        </tr>
        <tr>
          <td>笔记内容</td>
          <td colspan="5">{{fromData.content}}</td>
        </tr>
      </table>
      <!-- <ul>
        <li>
          <div class="tit">笔记作者</div>
          <div>ID编号</div>
          <div class="tit">作者手机号</div>
          <div>ID编号</div>
          <div class="tit">笔记发布时间</div>
          <div>ID编号</div>
        </li>
        <li>
          <div class="tit">引用人信息</div>
          <div>ID编号</div>
        </li>
        <li>
          <div class="tit">笔记内容</div>
          <div>ID编号</div>
        </li>
        
      </ul> -->
    </div>
    <!-- end -->
    </div>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted, watch } from "vue";
import { ElMessage } from "element-plus";
import { useRouter, useRoute } from "vue-router";
import {formatSeconds,formatTimeOrdinary} from "@/utils/index"
// ------定义变量------
// 获取父组件值、方法
const props = defineProps({
  // 订单信息
  fromData: {
    type: Object,
    default: () => ({}),
  },
});
//初始化路由
const router = useRouter(); //获取全局
const route = useRoute(); //获取局部
const emit = defineEmits(); //子组件获取父组件事件传值
// ------生命周期------
onMounted(() => {});

// ------定义方法------

// 向父组件暴露方法
defineExpose({});
</script>
<style src="./../index.scss" lang="scss" scoped></style>

