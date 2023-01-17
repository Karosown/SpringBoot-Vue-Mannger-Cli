<template>
<div id="manngerCenter">
  <el-container>
    <el-aside width="300px">
      <mangger-lefter></mangger-lefter>
    </el-aside>
    <el-main>
      <router-view></router-view>
    </el-main>
  </el-container>
</div>
</template>

<script>
import ManggerLefter from "@/components/manggerLefter";
export default {
  name: "manngerCenter",
  components: {ManggerLefter}
}
</script>

<style scoped>
#manngerCenter{
  height: 100vh;
}

</style>