<template>
  <el-container>
    <el-header>
      <el-col span="24">
        <el-input v-model="articleUpdateRequest.articleTitle" placeholder="请输入文章标题"></el-input>
      </el-col>
    </el-header>
    <el-main style="height: 100%">
      <el-col span="17">
        <editor-box :url="articleVo.articleUrl"></editor-box>
      </el-col>
      <el-col span="6" style="border: ridge 1px; margin-left: 3px;height: 11vh">
        <article-aside-box></article-aside-box>
        <el-button @click="update" type="primary">
          更新
        </el-button>
        <el-button  @click="openHistory=true" type="info">
          历史版本
        </el-button>
      </el-col>
    </el-main>
    <el-dialog title="历史版本" :visible.sync="openHistory" :modal="false" width="50%" center="true">
      <article-history-box :ArticleId="articleVo.id"></article-history-box>
    </el-dialog>
  </el-container>
</template>

<script>
import ArticleAsideBox from "@/components/articleComponents/articleAsideBox";
import EditorBox from "@/components/Editor/editorBox";
import {getArticleByid, updateArticles} from "@/config/ApiConfig/articleApiConfig/articleApiConfig";
import {ArticleUpdateRequest} from "@/entity/article/ArticleUpdateRequestBody";
import ArticleHistoryBox from "@/components/articleComponents/articleHistoryBox";
export default {
  name: "articleUpdateRequest",
  components: {ArticleHistoryBox, EditorBox, ArticleAsideBox},
  data() {
    return {
      articleUpdateRequest: new ArticleUpdateRequest(),
      articleVo:this.$route.params.articleVo,
      openHistory:false
    }
  },
  methods:{
    async update() {
      this.articleUpdateRequest.labelList = JSON.stringify(this.articleUpdateRequest.labelList)
      await this.axios.post(updateArticles, this.articleUpdateRequest)
          .then(res => {
            if (!res.data.code) {
              this.$message.success(res.data.message)
            } else {
              this.$message.error(res.data.message)
            }
            this.articleUpdateRequest.labelList=JSON.parse(this.articleUpdateRequest.labelList)
          })
          .catch(err => {
            this.$message.info(err.data.message)
          })

    },
    getHistory(){

    }
  },
 async mounted() {
   await this.axios.get(getArticleByid + this.$route.query.id)
       .then(res => {
         this.articleVo = res.data.data
         this.articleUpdateRequest.articleTitle = this.articleVo.articleTitle
         this.articleUpdateRequest.userArticleid = this.articleVo.userArticleid
         this.articleUpdateRequest.id = this.articleVo.id
         this.articleUpdateRequest.type = this.articleVo.typeId
         this.articleUpdateRequest.labelList = JSON.parse(this.articleVo.labelList)
         this.articleUpdateRequest.articleIntroduction = this.articleVo.articleIntroduction
         this.articleUpdateRequest.featImg = this.articleVo.featImg
         this.articleUpdateRequest.schedId = this.articleVo.scoped
         this.articleUpdateRequest.publishTime  = this.articleVo.publishTime
         var xhr = new XMLHttpRequest();
         xhr.open('GET', this.articleVo.articleUrl,true);
         xhr.send(null);
         xhr.onreadystatechange= ()=>{
           setTimeout(()=>{
             this.resetSetItem('updateText',xhr.responseText)
           },500)
         }
       })
   window.addEventListener('setItem', () => {
     this.articleUpdateRequest.articleText = sessionStorage.getItem('updateText')
   })
 }
}
</script>

<style scoped>

</style>