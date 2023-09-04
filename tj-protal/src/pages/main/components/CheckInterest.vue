<!-- 选择兴趣弹窗 -->
<template>
<div class="Interest fx-wp">
  <div class="item" v-for="(item, index) in data" :key="index" >
    <div class="ft-cl-des ft-12">{{item.name}}</div>
      <div>
        <span :class="{active: activeList.has(it.id), checkBox:true}" v-for="(it, ind) in item.children" :key="`${index}-${ind}`" @click="activeHandle(it.id)">
          {{it.name}}
        </span>
      </div>
  </div>
</div>
</template>
<script setup>
import { ref } from 'vue'
import { ElMessageBox } from 'element-plus'

// 引入父级传参
const props = defineProps({
  data:{
    type: Array,
    default:[]
  }, 
  initValue:{
    type: Set,
    default: new Set()
  }
})
const emit = defineEmits(['setInterestList'])
const activeList = ref(fileterinit(props.initValue))
// 处理initValue
function fileterinit(item){
  let catchData = new Set()
  for(const val of item){
    catchData.add(val.id)
  }
  return catchData
}
// 点击选中
const activeHandle = id => {
  let { value } = activeList
  const n = value.has(id)
  if (value.size >= 6 && !n){
    // 提示
    ElMessageBox.alert('最多可以选择6个兴趣哦', '提示：',{confirmButtonText: '知道了', callback:() => {}})
    return false
  }
  n ? value.delete(id) : value.add(id)
  emit('setInterestList', value)
}
</script>
<style lang="scss" scoped>
.Interest {
  .item{
    width: 50%;
    .checkBox{
      cursor: pointer;
      display: inline-block;
      background: #F5F6F8;
      border: 1px solid #EEEEEE;
      border-radius: 16px;
      padding: 0 20px;
      font-size: 12px;
      line-height: 30px;
      margin:10px 20px 10px 0;
    }
    .active{
      background: #D5E8FF;
      border: 1px solid #2080F7;
      color: #2080F7;
      border-radius: 16px;
    }
  }
}
</style>
