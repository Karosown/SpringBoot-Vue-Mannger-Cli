<template>
  <el-container >
    <el-header>
      <el-col span="12">
        <el-button type="info">导出为excel</el-button>

      </el-col>
      <el-col span="12">
        <el-col span="12" style="margin-right: 3px">
        <el-input v-model="searchText" @keyup.enter.native="search">
          <i slot="suffix" class="el-icon-search" @click="search"></i>
        </el-input>
        </el-col>
        <el-button @click="newArticle" type="primary">新增文章</el-button>
        <el-button type="danger">回收站</el-button>
      </el-col>
    </el-header>
    <el-main>
      <el-table border lazy
          :data="articleDatas"
          style="width: 100%"

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
            width="250">
        </el-table-column>
        <el-table-column
            property="articleUrl"
            label="OSS地址"
            width="200">
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
            property="label"
            label="文章标签"
            sortable
            width="180">
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
                @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
            <el-button
                size="mini"
                type="danger"
                @click="handleDelete(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-main>
    <el-footer>


      <el-pagination
      @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="currentPage4"
          :page-sizes="[10, 20, 30, 40,50]"
          :page-size="10"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </el-footer>
  </el-container>
</template>

<script>
import {getArticleslistPage} from "@/config/ApiConfig/articleApiConfig/articleApiConfig";
import {articleQureyRequestBody} from "@/entity/article/AriticleQueryRequestBody";

export default {
  name: "articlePage",
  data(){
    return{
      total:null,
      articleDatas:null,
      searchText:null
    }
  },
  methods:{
    search(){

    },
    newArticle(){
      this.$router.push({path:'/articlePublish'});
    }
  },
  mounted() {
    var articleQureyPageBody = new articleQureyRequestBody()
    articleQureyPageBody.sortField='createTime'
    this.axios.get(getArticleslistPage,{
      params:articleQureyPageBody
    })
        .then(res=>{ 
          this.total=res.data.data.total
          this.articleDatas=res.data.data.records
        })

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