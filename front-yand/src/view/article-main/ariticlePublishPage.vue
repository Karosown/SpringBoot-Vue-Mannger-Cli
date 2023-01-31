<template>
<el-container>
  <el-header>
    <el-col span="24">
      <el-input placeholder="请输入文章标题"></el-input>
    </el-col>
  </el-header>
  <el-main style="height: 100%">
    <el-col span="17">
      <editor-box></editor-box>
    </el-col>
    <el-col span="6" style="border: ridge 1px; margin-left: 3px;height: 11vh">
      <article-aside-box></article-aside-box>
    </el-col>
  </el-main>
</el-container>
</template>

<script>
import ArticleAsideBox from "@/components/articleComponents/articleAsideBox";
import EditorBox from "@/components/Editor/editorBox";
import {ArticleAddRequest} from "@/entity/article/ArticleAddRequestBody";
export default {
  name: "ariticlePublishPage",
  components: {EditorBox, ArticleAsideBox},
  data(){
    return{
      articleAddRequest:new ArticleAddRequest()
    }
  }
}
</script>

<style scoped>

</style>