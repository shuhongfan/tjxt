<template>
  <editor
    v-model="content"
    :id="tinymceId"
    tag-name="div"
    :init="init"
    placeholder="请输入，最多可添加200个字"
    @input="handleInput"
  />
  <!-- <span class="numText" :class="numVal === 0 ? 'tip' : ''"
    >{{ numVal }}/15</span
  > -->
</template>

<script>
import tinymce from "tinymce/tinymce";
import Editor from "@tinymce/tinymce-vue";
import "tinymce/themes/silver/theme"; // 引用主题文件
import "tinymce/icons/default"; // 引用图标文件
import "tinymce/plugins/link";
import "tinymce/plugins/code";
import "tinymce/plugins/table";
import "tinymce/plugins/lists";
import "tinymce/plugins/advlist";
import "tinymce/plugins/colorpicker";
import "./placeholder";
import "tinymce/plugins/contextmenu";
import "tinymce/plugins/textcolor";
import "tinymce/plugins/anchor";
import "tinymce/plugins/autolink"; //锚点
import "tinymce/plugins/autoresize";
import "tinymce/plugins/autosave";
import "tinymce/plugins/charmap"; //特殊字符
import "tinymce/plugins/code"; //查看源码
import "tinymce/plugins/codesample"; //插入代码
import "tinymce/plugins/directionality"; //
import "tinymce/plugins/fullpage"; //页面属性编辑
import "tinymce/plugins/fullscreen"; //全屏
import "tinymce/plugins/help"; //帮助
import "tinymce/plugins/hr"; //横线
import "tinymce/plugins/image"; //图片
import "tinymce/plugins/imagetools"; //图片工具
import "tinymce/plugins/importcss"; //图片工具
import "tinymce/plugins/insertdatetime"; //时间插入
import "tinymce/plugins/media"; //媒体插入
import "tinymce/plugins/nonbreaking"; //
import "tinymce/plugins/noneditable"; //不间断空格
import "tinymce/plugins/pagebreak"; //分页
import "tinymce/plugins/paste"; //粘贴
import "tinymce/plugins/preview"; //预览
import "tinymce/plugins/print"; //打印
import "tinymce/plugins/quickbars"; //快捷菜单
import "tinymce/plugins/save"; //保存
import "tinymce/plugins/searchreplace"; //查询替换
import "tinymce/plugins/spellchecker"; //拼写检查
import "tinymce/plugins/tabfocus"; //
import "tinymce/plugins/template"; //插入模板
import "tinymce/plugins/textpattern"; //
import "tinymce/plugins/toc"; //
import "tinymce/plugins/visualblocks"; //
import "tinymce/plugins/visualchars"; //
import "tinymce/plugins/wordcount"; //数字统计
import { validateTextLength } from "@/utils/index.js";
export default {
  props: {
    modelValue: String,
    // 用于区分单个tinymce
    tinymceId: String,
    fromData: Object,
    isClear: Boolean,
  },
  components: {
    editor: Editor,
  },
  emits: { "update:modelValue": null },
  setup(props, context) {
    const init = {
      selector: `#Editor${props.tinymceId}`, //多个富文本的时候加上itemkey用于区分
      max_chars: 10,
      language_url: import.meta.globEager("./../../utils/zh_CN.js"), // 中文语言包路径
      language: "zh_CN",
      skin_url: import.meta.globEager("tinymce/skins/ui/oxide/skin.css"), // 编辑器皮肤样式
      menubar: false, // 隐藏菜单栏
      autoresize_bottom_margin: 50,
      max_height: 300,
      min_height: 200,
      //   height: 320,
      toolbar_mode: "none",
      plugins:
        "placeholder advlist autolink autosave code codesample colorpicker colorpicker contextmenu directionality fullscreen hr image imagetools insertdatetime link lists media nonbreaking noneditable pagebreak paste preview print save searchreplace spellchecker tabfocus table template textcolor textpattern visualblocks visualchars wordcount", // 插件需要import进来
      toolbar:
        "searchreplace bold italic underline strikethrough alignleft aligncenter alignright outdent indent  blockquote undo redo removeformat subscript superscript code codesample', 'hr bullist numlist link image charmap preview anchor pagebreak insertdatetime media table emoticons forecolor backcolor fullscreen",
      content_style: "p {margin: 5px 0; font-size: 14px}",
      fontsize_formats: "12px 14px 16px 18px 24px 36px 48px 56px 72px",
      font_formats:
        "微软雅黑=Microsoft YaHei,Helvetica Neue,PingFang SC,sans-serif;苹果苹方=PingFang SC,Microsoft YaHei,sans-serif;宋体=simsun,serif;仿宋体=FangSong,serif;黑体=SimHei,sans-serif;Arial=arial,helvetica,sans-serif;Arial Black=arial black,avant garde;Book Antiqua=book antiqua,palatino;",
      branding: false,
      elementpath: false,
      resize: false, // 禁止改变大小
      statusbar: false, // 隐藏底部状态栏
    };
    tinymce.init; // 初始化
    const revert_data = (content) => {
      context.emit("update:modelValue", content);
    };
    // function handleChange(e) {
    // }
    return {
      init,
      revert_data,
      // handleChange,
    };
  },

  data() {
    return {
      content: this.modelValue,
      numVal: 0, //分类名称字数显示
      name: "",
      arr: [],
      seletearr: [],
    };
  },
  watch: {
    //提交添加题目时清空富文本
    isClear() {
      let tinymces = tinymce.get(this.tinymceId);
      tinymce.editors.map((item) => {
        item.setContent("");
      });
    },
    fromData() {
      // 触发列表编辑时回显题目名称和选项，由于富文本时公用的， 所以利用文本id来回显相应的内容
      if (!this.isClear) {
        let baseVal = this.fromData;
        let tinymces = tinymce.get(this.tinymceId);
        if (tinymce.editors.length > 0) {
          tinymce.editors.map((item) => {
            // 题目名称
            if (item.id === "tinymce01") {
              item.setContent(baseVal.name);
            }
            // 答案解析
            if (item.id === "tinymce03") {
              item.setContent(baseVal.analysis);
            }
            // 选项A
            if (item.id === "edito0") {
              item.setContent(baseVal.options[0]);
            }
            // 选项B
            if (item.id === "edito1") {
              item.setContent(baseVal.options[1]);
            }
            // 选项C
            if (item.id === "edito2") {
              item.setContent(baseVal.options[2]);
            }
            // 选项D
            if (item.id === "edito3") {
              item.setContent(baseVal.options[3]);
            }
            // end
            // if (item.id === "tinymce2" && baseVal.options[2] !== undefined) {
            //   item.setContent(baseVal.options[2]);
            // }
            // if (item.id === "tinymce3" && baseVal.options[3] !== undefined) {
            //   item.setContent(baseVal.options[3]);
            // }
          });
        }
      }
    },
    content() {
      this.revert_data(this.content);
      // var activeEditor = tinymce.activeEditor;
      // var editBody = activeEditor.getBody();
      // activeEditor.selection.select(editBody);
      // var text = activeEditor.selection[0].getContent({ format: "text" });
      // const value = validateTextLength(text, 15);
      // // activeEditor.selection.setContent('设置内容');
      // this.numVal = value.numVal;
      // if (value.numVal < 15) {
      //   return false;
      // } else {
      //   // activeEditor.selection.setContent(value.numVal);
      // }
      // this.$emit('onClick',text)
      // 因为可能会有多个 选项，所以这块用了一个列表循环，前三个是必填项
      const tinymces = tinymce.get(this.tinymceId);
      let arr = [];
      tinymce.editors.map((item) => {
        if (item.id !== "tinymce01" && item.id !== "tinymce03") {
          if (item.getContent() !== "") {
            arr.push(item.getContent());
          }
        }
      });
      this.$emit("getNswers", arr);
      if (tinymces.id === "tinymce01") {
        this.$emit("getName", tinymces.getContent());
        this.$emit("getFlag", true);
      }
      if (tinymces.id === "tinymce03") {
        this.$emit("getAnalysis", tinymces.getContent());
      }
    },
    handleInput(val) {},
  },
};
</script>
<style lang="scss">
:deep(.tox.tox-tinymce) {
  border: 1px solid var(--color-font3) !important;
  border-radius: 4px;
}
</style>