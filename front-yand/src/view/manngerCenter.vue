<template>
<div id="manngerCenter">
  <el-container>
    <el-aside>
      <mannger-lefter></mannger-lefter>
    </el-aside>
    <el-main>
      <router-view></router-view>
    </el-main>
  </el-container>
</div>
</template>

<script>
export default {
  name: "manngerCenter"
}
</script>

<style scoped>

</style>