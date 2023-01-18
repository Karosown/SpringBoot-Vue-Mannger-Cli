<template>
<div id="manggerCenter">
  <el-container>
    <el-aside width="300px">
      <mangger-lefter></mangger-lefter>
    </el-aside>
    <el-main>
      <manngger-main></manngger-main>
    </el-main>
  </el-container>
</div>
</template>

<script>
import ManggerLefter from "@/components/manggerLefter";
import MannggerMain from "@/components/mannggerMain";
export default {
  name: "manggerCenter",
  components: {MannggerMain, ManggerLefter}
}
</script>

<style scoped>
#manggerCenter{
  height: 100vh;
}

</style>