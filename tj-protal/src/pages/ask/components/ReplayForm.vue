<!-- 回复 -->
<template>
  <div class="answerCont bg-wt marg-bt-20">
    <div class="answer fx">
      <img :src="store.getUserInfo.icon" alt="" srcset="" />
      <div class="fx-1">
        <el-input
          v-model="description"
          rows="6"
          type="textarea"
          @input="ruleshandle"
          maxlength="500"
          show-word-limit
          :placeholder="`回复：${name}`"
        />
        <div class="fx-sb fx-al-ct">
          <div>
            <el-checkbox v-model="anonymity" label="匿名" size="large" />
          </div>
          <div class="subCont">
            <span
              class="bt ft-14"
              :class="{ 'bt-dis': !isSend }"
              @click="answerHandle(ruleFormRef)"
              >评论</span
            >
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, reactive } from "vue";
import { useUserStore } from "@/store";

const props = defineProps({
  name: String,
  askInfoId: String,
  id: String,
});
const emit = defineEmits(['commentHandle'])
const store = useUserStore();
const anonymity = ref(false);
const isSend = ref(false);
const ruleFormRef = ref();
const description = ref('');
const ruleshandle = () => {
  isSend.value = description.value != "" ? true : false;
};

// 提交回复数据
const params = reactive({
  answerId: "",
  questionId: props.askInfoId, // 当前问题的ID
  targetReplyId: "",
  targetUserId: "",
  content: "",
  anonymity: "",
});
// 提交回复
const answerHandle = async () => {
  params.content = description.value
  params.anonymity = anonymity.value
  emit('commentHandle', params);
  description.value = '';
  anonymity.value = ''
};
</script>
<style lang="scss">
.answerCont {
  padding: 20px 30px;
  font-size: 14px;
  border-radius: 8px;
  .subCont {
    margin-top: 20px;
    .bt {
      width: 103px;
      height: 32px;
      line-height: 32px;
      border-radius: 25px;
    }
  }
  .answer {
    img {
      width: 24px;
      height: 24px;
      border-radius: 24px;
      margin-right: 10px;
    }
  }
  .answerItems {
    .items {
      .cont {
        padding: 10px 10px 20px 34px;
        line-height: 24px;
      }
    }
    .replyCont {
      padding-left: 34px;
    }
  }
}
</style>
