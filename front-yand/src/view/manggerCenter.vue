<template>
<div id="manggerCenter">
  <el-container>

    <el-aside width="300px">
      <mangger-lefter></mangger-lefter>
    </el-aside>
    <el-main>
      <manngger-main></manngger-main>
      <globel-footer></globel-footer>
    </el-main>
  </el-container>
</div>
</template>

<script>
import ManggerLefter from "@/components/manggerLefter";
import MannggerMain from "@/components/mannggerMain";
import GlobelFooter from "@/components/globelFooter";

export default {
  name: "manggerCenter",
  components: {GlobelFooter, MannggerMain, ManggerLefter}
}
</script>

<style scoped>
#manggerCenter{
  height: 100vh;
}

</style>