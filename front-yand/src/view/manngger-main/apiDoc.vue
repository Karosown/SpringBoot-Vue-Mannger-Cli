<template>
<div >
  <el-radio v-model="open" :label='0'>彩色版</el-radio>
  <el-radio v-model="open" :label='1'>简洁版</el-radio>
  <div :key="new Date().getTime()">
    <iframe src="/doc/Apidocstyle.html" v-if="!open">
    </iframe>
    <iframe src="/doc/Apidocmarkdown.html" v-else>
    </iframe>
  </div>
</div>
</template>

<script>
export default {
  name: "apiDoc",
  data(){
    return{
      open:0
    }
  }
}
</script>

<style scoped>
*{
  margin: 0px;
  padding: 0px;

}

div,iframe{
  top: 0;
  left: 0;
  margin: 0px;
  padding: 0px;
  height: 100%;
  width: 100%;
}
</style>