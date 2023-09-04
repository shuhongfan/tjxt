<!-- 添加、编辑题目 -->
<template>
  <div class="contentBox">
    <div class="bg-wt radius marg-tp-20">
      <div class="detailBox">
        <el-form
          :model="fromData"
          ref="ruleFormRef"
          :rules="rules"
          label-width="130px"
          class="demo-ruleForm"
        >
          <el-form-item label="所属题目分类：" prop="cates">
            <div class="el-input">
              <el-cascader
                v-model="fromData.cateIds"
                :options="typeData.value"
                @change="handleChange"
                :props="{
                  label: 'name',
                  value: 'id',
                  children: 'children',
                }"
                clearable
                style="width: 100%"
              >
              </el-cascader>
            </div>
          </el-form-item>
          <el-form-item label="所属课程：">
            <div class="el-input">
              <el-select
                v-model="fromData.courseIds"
                clearable
                placeholder="请选择"
                style="width: 100%"
                filterable
                @change="handleCourse"
              >
                <el-option
                  v-for="item in courseData.value"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                ></el-option>
              </el-select>
            </div>
          </el-form-item>
          <el-form-item label="题目类型：" prop="type">
            <el-radio-group v-model="fromData.type" @change="handleSubjectType">
              <el-radio
                v-for="(item, index) in titleTypeData"
                :key="index"
                :label="index + 1"
                >{{ item.label }}</el-radio
              >
            </el-radio-group>
          </el-form-item>
          <el-form-item label="难易程度：" prop="difficulty">
            <el-radio-group v-model="fromData.difficulty">
              <el-radio
                v-for="(item, index) in difficultyData"
                :key="index"
                :label="index + 1"
                >{{ item.label }}</el-radio
              >
            </el-radio-group>
          </el-form-item>
          <el-form-item label="分值：" prop="score">
            <el-input-number
              v-model="fromData.score"
              :min="0"
              :max="50"
              placeholder="0"
            ></el-input-number>
          </el-form-item>
          <div class="selectBox">
            <el-form-item label="题目名称：" prop="name">
              <!-- TODO由于此组件不满足设计需求，因此先暂时停用此组件，放在二期 -->
              <!-- <Tinymce
              :content="'editor01'"
              tinymceId="tinymce01"
              :height="200"
              :fromData="fromData"
              @getName="getName"
              @getFlag="getFlag"
              :isClear="isClear"
              v-model="fromData.name"
              @blur="getCheckName"
              :key="keyTime"

            ></Tinymce> -->
              <div class="inputText">
                <el-input
                  v-model="fromData.name"
                  type="textarea"
                  placeholder="请输入，最多可添加200个字"
                  resize="none"
                  @input="nameTextInput"
                />
                <span class="numText" :class="nameNumVal === 0 ? 'tip' : ''"
                  >{{ nameNumVal }}/200</span
                >
              </div>
            </el-form-item>
            <span class="seatBox"></span>
          </div>
          <div class="errTip" v-if="nameTipText !== ''">{{ nameTipText }}</div>
          <!-- 判断题 -->
          <div v-if="fromData.type === 4">
            <el-form-item label="正确答案：" prop="radioanswer">
              <el-radio-group v-model="fromData.radioanswer">
                <el-radio
                  v-for="(item, index) in titleAnswerData"
                  :key="index"
                  :label="index + 1"
                  >{{ item.label }}</el-radio
                >
              </el-radio-group>
            </el-form-item>
          </div>
          <!-- end -->
          <!-- 单选题、多选题、不定项选择 -->
          <div v-else>
            <div v-for="(item, index) in list" :key="index">
              <div class="selectBox">
                <el-form-item :prop="item.prop" :label="'选项' + item.name">
                  <!-- <template #label>
                    <span class="star">*</span>选项{{ item.name }}
                  </template> -->
                  <div class="inputText">
                    <el-input
                      v-model="fromData[item.prop]"
                      type="textarea"
                      placeholder="请输入，最多可添加200个字"
                      resize="none"
                      @input="optionTextInput(item)"
                      @blur="optionTextBlur(item)"
                    />
                    <span
                      class="numText"
                      :class="item.numVal === 0 ? 'tip' : ''"
                      >{{ item.numVal }}/200</span
                    >
                  </div>
                </el-form-item>
                <span class="checkBox">
                  
                  <el-checkbox
                    v-model="item.checked"
                    v-if="
                      fromData.type === 2 || fromData.type === 3
                    "
                    @change="handleCheck($event, index)"
                    >正确</el-checkbox
                  >
                  <el-radio v-model="radio" :label="index + 1" v-else
                    >正确</el-radio
                  >
                  
                </span>
              </div>
            </div>
          </div>
          <!-- end -->
          <div class="selectBox">
            <el-form-item label="答案解析：" prop="analysis">
              <!-- <Tinymce
              :content="'editor03'"
              :height="200"
              tinymceId="tinymce03"
              @getAnalysis="getAnalysis"
              :isClear="isClear"
              :key="keyTime"
            ></Tinymce> -->
              <div class="inputText">
                <el-input
                  v-model="fromData.analysis"
                  type="textarea"
                  placeholder="请输入，最多可添加300个字"
                  resize="none"
                  @input="analysisTextInput"
                />
                <span class="numText" :class="analysisNumVal === 0 ? 'tip' : ''"
                  >{{ analysisNumVal }}/300</span
                >
              </div>
            </el-form-item>
            <span class="seatBox"></span>
          </div>
        </el-form>
      </div>
      <div
        class="detailBox"
        style="border-top: 1px solid #f5efee; padding-top: 0"
      >
        <div class="btn">
          <el-button
            class="button buttonSub"
            @click="handleGo"
            style="width: 130px"
            >取消</el-button
          >
          <el-button
            class="button buttonSub"
            v-preventReClick
            @click="handleSubmit('getback')"
            >保存并返回</el-button
          >
          <el-button
            class="button primary"
            v-preventReClick
            @click="handleSubmit()"
            >保存并继续</el-button
          >
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted, watch, watchEffect, nextTick } from "vue";
import { useRouter, useRoute } from "vue-router";
import { ElMessage } from "element-plus";
// 控制字节数
import { validateTextLength } from "@/utils/index.js";
// 接口api
import { getDetails, addSubject, editSubject, checkName } from "@/api/title";
import { getTypeAll, getCoursesAll } from "@/api/api";
// 公用数据
import {
  difficultyData,
  titleTypeData,
  titleAnswerData,
} from "@/utils/commonData";
// 表单校验
import { isPositiveInteger } from "@/utils/validate";
// 导入组件
// 富文本
import Tinymce from "@/components/Tinymce/index.vue";
// ------定义变量------
//初始化路由
const router = useRouter(); //获取全局
const route = useRoute(); //获取局部
let fromData = ref({}); //新增编辑表单数据
let typeData = reactive([]); //类型数据
const emit = defineEmits(); //子组件获取父组件事件传值
const ruleFormRef = ref(); //表单校验ref
let radio = ref(""); //单选当前
let options = reactive([]); //已经选择的多选框
let checkBoxData = reactive([]); //处理已经选择的多选框
let answer = ref(""); //判断提的正确答案
let courseData = reactive([]); //根据题目分类获取所属课程
let courseIds = reactive([]); //所属课程ids
let courseId = ref(null); //当前标题id
let tinymces = ref([]);
let keyTime = ref(null); //解决组件强制重新加载的问题
let isClear = ref(false); //清空表单
let flag = ref(false); //用来控制上传图片是否校验成功
let nameTipText = ref(""); //名字重复提示
let nameNumVal = ref(0); //题目名称字数显示
let analysisNumVal = ref(0); //答案解析字数显示
let optionANumVal = ref(0); //选项A字数显示
let optionBNumVal = ref(0); //选项B字数显示
let optionCNumVal = ref(0); //选项C字数显示
let optionDNumVal = ref(0); //选项D字数显示

let list = reactive([
  {
    value: 1,
    name: "A",
    checked: false,
    content: null,
    numVal: 0,
    prop: "optionA", //prop与value的名字一致，然后需要将他们的结构一致。
  },
  {
    value: 2,
    name: "B",
    checked: false,
    content: null,
    numVal: 0,
    prop: "optionB",
  },
  {
    value: 3,
    name: "C",
    checked: false,
    content: null,
    numVal: 0,
    prop: "optionC",
  },
  {
    value: 4,
    name: "D",
    checked: false,
    content: null,
    numVal: 0,
    prop: "optionD",
  },
]);
// 校验题目名字
const validateName = async (rule, value, callback) => {
  if (value !== undefined && value !== "") {
    const nameValue = validateTextLength(value, 300);
    if (nameValue.numVal < 5) {
      callback(new Error("题目名称需输入5-200个字"));
    }
    let parent = {
      name: value,
      id: fromData.value.id,
    };
    await checkName(parent)
      .then((res) => {
        if (res.code === 200) {
          const isexisted = res.data.existed;
          if (isexisted) {
            callback(new Error("该题目已存在"));
          } else {
            callback();
          }
        }
      })
      .catch((err) => {});
  } else {
    callback(new Error("题目为空，请设置题目"));
  }
};
// 校验选项
const validateOption = (rule, value, callback) => {
  if (value !== undefined && value !== "") {
    const nameValue = validateTextLength(value, 200);
    if (nameValue.numVal < 1) {
      callback(new Error("选项内容需输入1-200个字"));
    } else {
      // 四个选项名字不能重复
      let name = rule.field.substring(6, 7);
      list.forEach((val) => {
        if (value === val.content) {
          if (val.name !== name) {
            callback(new Error("选项重复，请重新输入"));
          } else {
            callback();
          }
        }
      });
    }
  } else {
    callback(new Error("选项内容为空，请输入选项内容"));
  }
};
const validateAnalysis = (rule, value, callback) => {
  if (value !== undefined && value !== "") {
    const nameValue = validateTextLength(value, 300);
    if (nameValue.numVal < 5) {
      callback(new Error("答案解析需输入5-300个字"));
    } else {
      callback();
    }
  } else {
    callback(new Error("答案解析为空，请输入答案解析"));
  }
};
// 表单校验
const rules = reactive({
  cateIds: [
    {
      required: true,
      message: "所属题目分类为空，请选择所属题目分类",
      trigger: "change",
    },
  ],
  type: [
    {
      required: true,
      message: "题目类型为空，请设置题目类型",
      trigger: "change",
    },
  ],
  difficulty: [
    {
      required: true,
      message: "难易程度为空，请设置难易程度",
      trigger: "change",
    },
  ],
  score: [
    {
      required: true,
      validator: isPositiveInteger,
      trigger: "blur",
    },
    {
      required: true,
      validator: isPositiveInteger,
      trigger: "change",
    },
  ],
  name: [
    {
      required: true,
      validator: validateName,
      trigger: "blur",
    },
  ],
  analysis: [
    {
      required: true,
      validator: validateAnalysis,
      trigger: "blur",
    },
  ],
  optionA: [
    {
      required: true,
      // message: '选项内容为空，请输入选项内容',
      validator: validateOption,
      trigger: "blur",
    },
  ],
  optionB: [
    {
      required: true,
      // message: '选项内容为空，请输入选项内容',
      validator: validateOption,
      trigger: "blur",
    },
  ],
  optionC: [
    {
      required: true,
      // message: '选项内容为空，请输入选项内容',
      validator: validateOption,
      trigger: "blur",
    },
  ],
  optionD: [
    {
      required: true,
      // message: '选项内容为空，请输入选项内容',
      validator: validateOption,
      trigger: "blur",
    },
  ],
  answer: [
    {
      required: true,
      message: "正确答案为空，请设置正确答案",
      trigger: "change",
    },
  ],
  radioanswer: [
    {
      required: true,
      message: "正确答案为空，请设置正确答案",
      trigger: "change",
    },
  ],
});
// ------生命周期------
onMounted(() => {
  init();
});
// 保存并继续时返回到重新添加页面，这时候会有残余的数据，所以要强制刷新加载页面
// 值发生改变时说明不是同一个组件，将会进行重新加载和渲染
watchEffect(() => {
  if (route.path === "/title/details/null") {
    keyTime.value = new Date().getTime();
    isClear.value = false;
  }
});
watch(fromData, (newValue, oldValue) => {
  // 题目名称默认的字数
  const nameValue = validateTextLength(newValue.name, 200);
  nameNumVal.value = nameValue.numVal;
  // 题目名称默认的字数
  const analysisValue = validateTextLength(newValue.analysis, 200);
  analysisNumVal.value = analysisValue.numVal;
  // 选项默认的字数
  // const objData = fromData.value;
  if(!newValue.options) return
  list.forEach((obj, index) => {
    newValue.options.forEach((val, i) => {
      if (index === i) {
        const optionValue = validateTextLength(val, 200);
        obj.content = optionValue.val;
        obj.numVal = optionValue.numVal;
      }
    });
  });
});
// ------定义方法------
// 获取初始值
const init = () => {
  // 获取课程id
  courseId.value = route.params.id;
  if (courseId.value !== "null") {
    getDetailData(); //详情
  }
  getCourse(); //获取所属课程
  getTypeList(); //获取分类
};
// 获取详情
const getDetailData = async () => {
  await getDetails(courseId.value)
    .then((res) => {
      if (res.code === 200) {
        res.data.answer = res.data.answer.split(",").map(s => parseInt(s));
        fromData.value = res.data;
        const objData = fromData.value;
        // 处理选项内容
        if (fromData.value.type !== 4) {
          objData.optionA = objData.options[0];
          objData.optionB = objData.options[1];
          objData.optionC = objData.options[2];
          objData.optionD = objData.options[3];
          // list.forEach((obj, index) => {
          //   fromData.value.options.forEach((val, i) => {
          //     if (index === i) {
          //       obj.content = val;
          //     }
          //   });
          // });
        }

        // 处理单选数据
        if (fromData.value.type === 1) {
          radio.value = fromData.value.answer[0];
        } else if (fromData.value.type === 4) {
          fromData.value.radioanswer = fromData.value.answer[0];
        } else {
          // 处理多选数据
          list.forEach((val) => {
            fromData.value.answer.forEach((obj) => {
              if (val.value === obj) {
                val.checked = true;
              }
            });
          });
        }
        // 题目分类
        let thirdCateId = [res.data.cateId3];
        fromData.value.cateIds = [res.data.cateId1,res.data.cateId2,res.data.cateId3];
        // 根据题目三级分类获取所属课程
        getCourse(thirdCateId);
        // 处理所属课程
        fromData.value.courseIds = res.data.courseIds ? res.data.courseIds : [];
        courseIds.push(fromData.value.courseIds);
      }
    })
    .catch((err) => {});
};
// 保存
const handleSubmit = async (str) => {
  const objData = fromData.value;

  let arrItem = [
    objData.optionA,
    objData.optionB,
    objData.optionC,
    objData.optionD,
  ];
  // // 把填写的选项赋值给options
  // list.forEach((val) => {
  //   arrItem.push(val.content);
  // });
  fromData.value.options = arrItem;
  let baseVal = fromData.value;
  // 提交前先把获取详情的图片赋值给photoImg，要不然校验不到
  if (baseVal.name !== undefined) {
    flag.value = true;
  }
  // 表单校验
  const valid = await ruleFormRef.value.validate();
  if (valid) {
    if (!baseVal.name || baseVal.name === "") {
      ElMessage({

        message: "题目为空，请设置题目",
        type: "error",
        showClose: false,
      });
      return false;
    } else if (
      baseVal.type !== 4 &&
      baseVal.type !== 1 &&
      baseVal.options === undefined
    ) {
      // 选项内容
      ElMessage({

        message: "选项内容为空，请输入选项内容",
        type: "error",
        showClose: false,
      });
      return false;
    }
    // 单选逻辑 1
    if (baseVal.type === 1) {
      if (baseVal.options === undefined || baseVal.options.length < 4) {
        ElMessage({

          message: "选项内容为空，请输入选项内容",
          type: "error",
          showClose: false,
        });
        return false;
      } else if (radio.value === "") {
        ElMessage({

          message: "正确答案为空，请设置正确答案",
          type: "error",
          showClose: false,
        });
        return false;
      } else {
        options.push(radio.value);
      }
    }
    // 多选2、不定项3选择逻辑
    if (baseVal.type === 2 || baseVal.type === 3) {
      if (baseVal.options === undefined || baseVal.options.length < 4) {
        ElMessage({

          message: "选项内容为空，请输入选项内容",
          type: "error",
          showClose: false,
        });
        return false;
      }
      if (baseVal.answer === undefined || baseVal.answer.length === 0) {
        ElMessage({

          message: "正确答案为空，请设置正确答案",
          type: "error",
          showClose: false,
        });
        return false;
      }
      if (baseVal.type === 2) {
        if (baseVal.answer === undefined || baseVal.answer.length < 2) {
          ElMessage({

            message: "请至少设置两个答案",
            type: "error",
            showClose: false,
          });
          return false;
        }
      }
    }

    if (baseVal.type === 4) {
      baseVal.answer = [];
      fromData.value.options = null;
      baseVal.answer.push(baseVal.radioanswer);
    }
    baseVal.courseIds = courseIds;
    // 处理课程分类数据，提交的时候只传1-3级的id
    // let thirdCateId = [];
    // if (courseId.value !== "null") {
    //   baseVal.cates.map((obj) => {
    //     let arr = [obj.firstCateId, obj.secondCateId, obj.thirdCateId];
    //     thirdCateId.push(arr);
    //     fromData.value.cates = thirdCateId;
    //   });

    // }

    if (baseVal.analysis === "") {
      baseVal.analysis = null;
    }
    let params = {
      analysis: baseVal.analysis,
      answer: (baseVal.type === 1 ? options : baseVal.answer).join(","),
      cateIds: baseVal.cateIds,
      courseIds: baseVal.courseIds,
      difficulty: baseVal.difficulty,
      name: baseVal.name,
      score: baseVal.score,
      type: baseVal.type,
      options: baseVal.options,
      id:baseVal.id,
    };
    let func = !baseVal.id ? addSubject : editSubject;
    await func(params)
      .then((res) => {
        if (res.code === 200) {
          ElMessage({

            message: "恭喜你，操作成功！",
            type: "success",
            showClose: false,
          });
          if (str === "getback") {
            handleGo();
          } else {
            ruleFormRef.value.resetFields(); //清空表单数据

            fromData.value.courseIds = [];
            // 清空选择的答案
            fromData.value.answer = [];
            checkBoxData = [];
            options = [];
            // 处理多选数据清除多选
            list.map((val) => {
              val.checked = false;
              val.content = null;
            });
            fromData.value.optionA = "";
            fromData.value.optionB = "";
            fromData.value.optionC = "";
            fromData.value.optionD = "";
            // 清空答案解析
            fromData.value.analysis = null;
            // 清空单选
            radio.value = "";
            // 用来控制保存后清除富文本内容
            isClear.value = true;
            router.push({
              path: "/title/add/null",
            });
          }
        } else {
          ElMessage({

            message: res.data.msg,
            type: "error",
            showClose: false,
          });
        }
      })
      .catch((err) => {});
  }
};
// 获取分类
const getTypeList = async () => {
  await getTypeAll({ admin: true })
    .then((res) => {
      if (res.code === 200) {
        typeData.value = res.data;
      }
    })
    .catch((err) => {});
};
// 根据题目三级分类获取所属课程
const getCourse = async (ids) => {
  await getCoursesAll(ids)
    .then((res) => {
      if (res.code === 200) {
        courseData.value = res.data;
      }
    })
    .catch((err) => {});
};
// 多选
const handleCheck = (e, index) => {
  if (e) {
    // 处理触发的多选框
    let obj = {
      e: e,
      i: index + 1,
    };
    
    if (courseId.value !== "null") {
      let checkArr = [];
      checkArr.push(obj);
      
      if (fromData.value.answer.length > 0) {
        fromData.value.answer.map((val) => {
          checkArr.push({ e: true, i: val });
        });
        checkBoxData = checkArr;
      }else{
        checkBoxData = checkArr;
      }
      
    } else {
      checkBoxData.push(obj);
    }
  } else {
    // 筛选选择后又取消的多选框
    // 获取详情里的数据push到checkBoxData
    if (courseId.value !== "null") {
      fromData.value.answer.map((val) => {
        checkBoxData.push({ e: true, i: val });
      });
    }
    if (checkBoxData.length > 0) {
      for (let i = 0; i < checkBoxData.length; i++) {
        if (!e && index + 1 === checkBoxData[i].i) {
          checkBoxData.splice(i, 1);
        }
      }
    }
  }
 
  var str = [];

  checkBoxData.map((val) => {
    str.push(val.i);
  });

  options = str;
  fromData.value.answer = str;
};
// 获取名字
const getName = (content) => {
  fromData.value.name = content;
};
// 获取选项
const getNswers = (content) => {
  fromData.value.options = content;
};
// 获取答案解析
const getAnalysis = (content) => {
  fromData.value.analysis = content;
};
// 触发题目分类获取所属课程
const handleChange = (val) => {
  if (!val || val.length === 0) {
    return;
  } else {
    fromData.value.cateIds = val;
    let arr = [];
    val.map((value) => {
      arr.push(value[2]);
    });
    //根据题目三级分类获取所属课程
    getCourse(arr);
  }
};
// 所属课程
const handleCourse = (val) => {
  courseIds = [];
  courseIds.push(val);
};
// 清空表单
const handleGo = () => {
  router.push({
    path: "/title/index",
  });
};
const getFlag = (val) => {
  flag.value = val;
};
const getCheckName = async (e, editor) => {
  let name = fromData.value.name;
  let parent = {
    name: name,
    id: fromData.value.id,
  };
  await checkName(parent)
    .then((res) => {
      if (res.code === 200) {
        const isexisted = res.data.existed;
        if (isexisted) {
          nameTipText.value = "该题目已存在";
        } else {
          nameTipText.value = "";
        }
      }
    })
    .catch((err) => {});
};
// 题目名称字数限制
const nameTextInput = () => {
  nextTick(() => {
    let data = fromData.value;
    const value = validateTextLength(data.name, 200);
    data.name = value.val;
    nameNumVal.value = value.numVal;
  });
};
// 答案解析字数限制
const analysisTextInput = () => {
  nextTick(() => {
    let data = fromData.value;
    const value = validateTextLength(data.analysis, 300);
    data.analysis = value.val;
    analysisNumVal.value = value.numVal;
  });
};
//选项字数控制
const optionTextInput = (item) => {
  let arr = [];
  const objData = fromData.value;
  if (objData.optionA !== "") {
    arr[0] = objData.optionA;
  }
  if (objData.optionB !== "") {
    arr[1] = objData.optionB;
  }
  if (objData.optionC !== "") {
    arr[2] = objData.optionC;
  }
  if (objData.optionD !== "") {
    arr[3] = objData.optionD;
  }
  // let val = null;
  arr.forEach((obj, i) => {
    if (i + 1 === item.value && obj !== undefined) {
      const value = validateTextLength(obj, 200);
      item.content = value.val;
      item.numVal = value.numVal;
    }
  });
};
//
const optionTextBlur = (item) => {};
const handleSubjectType = () => {
  fromData.value.answer = [];
  list.forEach(val=>{
    val.checked = false
  })
};
</script>
<style src="./index.scss" lang="scss" scoped></style>
<style lang="scss" scoped>
:deep(.tox) {
  .tox-editor-container {
    width: 1100px;
  }
  .tox-edit-area {
    font-family: PingFangSC-Regular;
    font-weight: 400;
    font-size: 14px;
    label {
      top: -5px !important;
      left: -4px !important;
      color: #b5abab !important;
    }
  }
}
:deep(.tox-tinymce) {
  overflow: initial;
}
:deep(.el-input) {
  .el-input__wrapper {
    .el-input__inner {
      &::placeholder {
        color: #b5abab;
      }
    }
  }
}
.detailBox {
  :deep(.el-form-item__label) {
    font-family: PingFangSC-Regular;
    font-weight: 400;
    font-size: 14px;
    color: #332929;
  }
}
:deep(.tox) *:not(svg):not(rect) {
  font-family: PingFangSC-Regular;
  font-weight: 400;
  font-size: 14px;
  color: #b5abab !important;
}

:deep(.el-input .el-input__wrapper .el-input__inner) {
  color: #332929;
}
:deep(.el-textarea textarea.el-textarea__inner) {
  color: #332929;
  // max-width: 1250px;
  &::placeholder {
    color: #b5abab;
  }
}
</style>
<style lang="scss" scoped>
:deep(.el-input-number__increase) {
  .el-icon {
    background: #faf6f5;
    // border: 1px solid #E9E9EB;
    width: 40px;
    height: 40px;
    border-radius: 0 6px 6px 0;
    bottom: 0px;
    right: 0px;
    top: 0;
    color: #000;
    &:hover {
      color: var(--color-btnbackground);
    }
  }
}
:deep(.el-input-number__decrease.is-disabled) {
  .el-icon {
    background: #faf6f5;
    // border: 1px solid #E9E9EB;
    width: 40px;
    height: 40px;
    border-radius: 6px 0 0 6px;
    bottom: 1px;
    left: 1px;
    top: 0;
    // 不可点击
    color: #b5abab;
    &:hover {
      color: #b5abab;
    }
  }
}
:deep(.el-input-number__decrease) {
  .el-icon {
    background: #faf6f5;
    // border: 1px solid #E9E9EB;
    width: 40px;
    height: 40px;
    border-radius: 6px 0 0 6px;
    bottom: 0px;
    left: 0px;
    top: 0;
    &:hover {
      color: var(--color-btnbackground);
    }
  }
}
:deep(.el-input-number__increase) {
  border-left: 0;
}
:deep(.el-input-number__decrease) {
  border-right: 0;
  background: #faf6f5;
  width: 40px;
  height: 40px;
  border-radius: 6px 0 0 6px;
  bottom: 1px;
  left: 1px;
}
:deep(.el-input .el-input__wrapper) {
  height: 42px;
}
:deep(.el-input) {
  width: 293px;
}
.seatBox {
  display: block;
  width: 144px;
}
// :deep(.detailBox .checkBox) {
//   .el-checkbox {
//     color: var(--el-radio-text-color);
//     font-weight: var(--el-radio-font-weight);
//     position: relative;
//     cursor: pointer;
//     display: inline-flex;
//     align-items: center;
//     white-space: nowrap;
//     outline: 0;
//     font-size: var(--el-font-size-base);
//     -webkit-user-select: none;
//     -moz-user-select: none;
//     -ms-user-select: none;
//     user-select: none;
//     margin-right: 44px !important;
//     min-width: 80px;
//     height: 32px;
//   }
// }
// :deep(.el-checkbox__inner) {
//   border-radius: 14px;
// }
// :deep(.el-checkbox__input.is-checked .el-checkbox__inner::after) {
//   width: 3px;
//   height: 3px;
//   border-radius: 3px;
//   background-color: var(--el-color-white);
//   content: "";
//   position: absolute;
//   left: 52%;
//   top: 52%;
//   transform: translate(-50%, -50%) scale(1);
// }
// :deep(.el-checkbox__inner::after) {
//   transition: auto;
// }
// :deep(.el-checkbox__input.is-checked .el-checkbox__inner) {
//   // hover状态
//   &:hover {
//     background: #f16440;
//   }
// }
// :deep(.el-checkbox__input.is-checked + .el-checkbox__label) {
//   color: #332929;
// }
</style>
