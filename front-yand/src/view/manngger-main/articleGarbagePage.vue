<template>
  <el-container >
    <el-header>
      <el-col span="12">
        <el-button type="info" @click="downloadExcel">导出为excel</el-button>
        <el-button type="success" @click="recoveryByList">批量恢复</el-button>
        <el-button type="info" @click="gotoArticleMangger">返回文章管理</el-button>
      </el-col>
      <el-col span="12">
        <el-col span="12" style="margin-right: 3px">
        <el-input v-model="searchText" @keyup.enter.native="search">
          <i slot="suffix" class="el-icon-search" @click="search"></i>
        </el-input>
        </el-col>
      </el-col>
    </el-header>
    <el-main>
      <el-table border lazy
                ref="multipleTable"
          :data="articleDatas"
          style="width: 100%"
                @selection-change="addCheck"
      >
        <el-table-column type="selection" width="39"></el-table-column>
        <el-table-column
            property="createTime"
            label="日期"
            sortable
            width="159">
        </el-table-column>
        <el-table-column
            property="articleTitle"
            label="文章标题"
            width="230">
        </el-table-column>
        <el-table-column
            property="articleUrl"
            label="OSS地址"
            width="210">
          <template slot-scope="scope">
            <el-tooltip effect="dark" :content="scope.row.articleUrl" placement="bottom">
              <el-link >              {{scope.row.articleUrl.substring(0,25)}}...
              </el-link>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column
            property="articleIntroduction"
            label="内容简介"
            width="250">
        </el-table-column>
        <el-table-column
            property="type"
            label="文章分类"
            width="145">
        </el-table-column>
        <el-table-column
            property="labelList"
            label="文章标签"
            width="180">
          <template slot-scope="scope">
            <el-tag v-for="key in JSON.parse(scope.row.labelList)" :key="key"
                    size="small"
                    type="primary"
                    style="margin-right: 5px"
            >{{key}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
            property=""
            label="作者"
            sortable
            width="80">
        </el-table-column>
        <el-table-column
            property="isPublic"
            label="文章状态"
            sortable
            width="80">
          <template slot-scope="scope">
            <el-tag type="warning" v-if="!scope.row.isPublic">草&emsp;稿</el-tag>
            <el-tag v-else>已发布</el-tag>

          </template>
        </el-table-column>
        <el-table-column label="操作" width="143">
          <template slot-scope="scope">
            <el-button type="primary"
                size="mini"
                @click="handleFind(scope.row)">找回</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-main>
    <el-footer>


      <el-pagination
      @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="currentPage"
          :page-sizes="[10, 20, 30, 40,50]"
          :page-size="10"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </el-footer>
  </el-container>
</template>

<script>
import {
  exportVoExcel, getArticlesGarbagePage, recoveryArticle,
} from "@/config/ApiConfig/articleApiConfig/articleApiConfig";
import {articleQureyRequestBody} from "@/entity/article/AriticleQueryRequestBody";
import {globalValue} from "@/config/CommonConfig/globalconfig";
import {RecoveryRequestBody} from "@/entity/common/recoveryRequestBody";

export default {
  name: "articleGarbagePage",
  data(){
    return{
      total:null,
      articleDatas:null,
      currentPage:1,
      searchText:null,
      articleQureyPageBody:new articleQureyRequestBody(),
      reqBody:{
        articleVoList:[]
      }
    }
  },
  methods:{
    toggleSelection(rows) {
      if (rows) {
        rows.forEach(row => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    },
    recoveryByList(){
      var recoveryRequestBody = new RecoveryRequestBody();
      recoveryRequestBody.ids=this.reqBody.articleVoList.map(item=>{
        return item.id;
      });
      this.axios.post(recoveryArticle,recoveryRequestBody)
          .then(res=>{
              if(!res.data.code){
                for (const key in this.reqBody.articleVoList) {
                  this.articleDatas.splice(this.articleDatas.indexOf(key),1)
                }
                this.toggleSelection()
                  this.$message.success(res.data.message)
              }
              else this.$message.error(res.data.message)
          })
          .catch(err=>{
             this.$message.info(err.data.message)
          })

    },
    gotoArticleMangger(){
      this.$router.push('/articlePage')
    },
    downloadExcel(){
      this.axios.post(exportVoExcel, this.reqBody,{
        responseType:"blob"
      })
          .then(res=>{
            globalValue.downloadFn(res.data)
          })
    },
    addCheck(val){
      this.reqBody.articleVoList=val;
    },
    handleCurrentChange(val){
      this.articleQureyPageBody.current=val
      this.send()
    },
    handleSizeChange(val){
      this.articleQureyPageBody.pageSize=val;
      this.send()
    },
    search(){

    },
    send(){
      this.axios.get(getArticlesGarbagePage,{
        params:this.articleQureyPageBody
      })
          .then(res=>{
            this.total=res.data.data.total
            this.articleDatas=res.data.data.records
          })
    },
    handleFind(data){
      var recoveryRequestBody = new RecoveryRequestBody();
      recoveryRequestBody.id=data.id
      this.axios.post(recoveryArticle,recoveryRequestBody)
          .then(res=>{
            if (!res.data.code){
              this.$message.success(res.data.message)
              this.articleDatas.splice(this.articleDatas.indexOf(data),1)
            }
            else{
              this.$message.error(res.data.message)
            }
          })
    }
  },
  mounted() {
    this.articleQureyPageBody.sortField='createTime'
    this.send()
  }
}
</script>

<style scoped>
.el-icon-search{
  border-radius: 3px;
  padding:8px;
  margin-top: 2px;
  margin-right: -2px;
  font-size: 20px
}
.el-icon-search:hover{
  background-color: #f1f1f1;
}
</style>