<template>
<el-container>
  <el-header>
    <el-col span="24">
      <el-input v-model="articleAddRequest.articleTitle" placeholder="请输入文章标题"></el-input>
    </el-col>
  </el-header>
  <el-main style="height: 100%">
    <el-col span="17">
      <editor-box></editor-box>
    </el-col>
    <el-col span="6" style="border: ridge 1px; margin-left: 3px;height: 11vh">
      <article-aside-box></article-aside-box>
      <el-button @click="publish" type="primary">
        发布
      </el-button>
    </el-col>
  </el-main>
</el-container>
</template>

<script>
import ArticleAsideBox from "@/components/articleComponents/articleAsideBox";
import EditorBox from "@/components/Editor/editorBox";
import {ArticleAddRequest} from "@/entity/article/ArticleAddRequestBody";
import {addArticles} from "@/config/ApiConfig/articleApiConfig/articleApiConfig";
export default {
  name: "ariticlePublishPage",
  components: {EditorBox, ArticleAsideBox},
  data() {
    return {
      articleAddRequest: new ArticleAddRequest()
    }
  },
  methods:{
    async publish() {
      this.articleAddRequest.labelList = JSON.stringify(this.articleAddRequest.labelList)
      await this.axios.post(addArticles, this.articleAddRequest)
          .then(res => {
            if (!res.data.code) {
              this.$message.success(res.data.message)
            } else {
              this.$message.error(res.data.message)
            }
            this.articleAddRequest.labelList=JSON.parse(this.articleAddRequest.labelList)
          })
          .catch(err => {
            this.$message.info(err.data.message)
          })

    }
  }
}
</script>

<style scoped>

</style>