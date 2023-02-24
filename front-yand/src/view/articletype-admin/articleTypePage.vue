<template>
  <el-container>
    <el-header>
      <el-card>
        文章分类管理
      </el-card>
      </el-header>
    <el-main>
      <el-col :span="userRole=='admin'?8:24">
        <el-card class="card">
          <el-input
              placeholder="输入关键字进行过滤"
              v-model="filterText">
          </el-input>
          <el-tree :data="typelist"
                   :filter-node-method="filterNode"
                   default-expand-all
                   ref="tree"></el-tree>
        </el-card>
      </el-col>
      <el-col span="8" v-if="userRole=='admin'">
        <el-card class="card">
          <p>添加分类</p>
          <el-cascader
              v-model="checkList"
              :options="typelist"
              placeholder="试试搜索"
              filterable
              :props="{ checkStrictly: true }"
          >
          </el-cascader>
          <el-input v-model="articleTypeAddRequestBody.typeName" placeholder="请输入分类名"  @keyup.enter.native="add">
          </el-input>
          <el-button type="primary" @click="add">添加</el-button>
        </el-card>
      </el-col>
      <el-col span="8" v-if="userRole=='admin'">
        <el-card class="card">
          <p>删除分类</p>
          <el-cascader
              v-model="checkList"
              :options="typelist"
              placeholder="试试搜索"
              filterable
              :props="{ checkStrictly: true }"
          >
          </el-cascader>
          <el-button type="danger" @click="Delete">删除</el-button>
        </el-card>
      </el-col>
    </el-main>
  </el-container>
</template>

<script>

import {addType, deleteType, getTypelist} from "@/config/ApiConfig/articleApiConfig/articleTypeApiConfig";
import {ArticleTypeAddRequestBody} from "@/entity/articleType/ArticleTypeAddRequestBody";
import {DeleteRequestBody} from "@/entity/common/deleteRequestBody";

export default {
  name: "articleTypePage",
  data(){
    return{
      checkList:[],
      typelist:null,
      filterText:null,
      articleTypeAddRequestBody:new ArticleTypeAddRequestBody(),
      userRole:JSON.parse(sessionStorage.getItem('loginStatus')).userRole
    }
  },
  methods:{
    Delete(){
      var deleteRequestBody = new DeleteRequestBody();
      deleteRequestBody.id=this.articleTypeAddRequestBody.fid
      this.$confirm("您确定要删除 id="+deleteRequestBody.id+" 的type吗？同时您也会删除所有子类型，但是其下所有的文章均存在，不过会移动到【未分类】类型中",{
        confirmButtonText:"确定",
        cancelButtonText:"取消",
        type:"warning"
      })
          .then(()=>{
            this.axios.post(deleteType,deleteRequestBody)
                .then(res=>{
                  if(!res.data.code){

                    this.$message.success(res.data.message)
                  }
                  else this.$message.error(res.data.message)
                })
                .catch(err=>{
                  this.$message.info(err.data.message)
                })
          })
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    add(){
      this.axios.post(addType,this.articleTypeAddRequestBody)
          .then(res=>{
              if(!res.data.code){

                  this.$message.success(res.data.message)
              }
              else this.$message.error(res.data.message)
          })
          .catch(err=>{
             this.$message.info(err.data.message)
          })
    },

  },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val);
    },
    checkList(){
      this.articleTypeAddRequestBody.fid=this.checkList[this.checkList.length-1]
    }
  },
  mounted() {
    this.axios.get(getTypelist)
        .then(res=>{
          if (!res.data.code){
            this.typelist=res.data.data
          }
        })
  }
}
</script>

<style scoped>
.card{
  margin-right: 5px;
}
.card *{
  margin:3px;
}
</style>