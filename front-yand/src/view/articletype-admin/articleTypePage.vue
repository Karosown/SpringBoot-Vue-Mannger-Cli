<template>
  <el-container>
    <el-header>
      <el-card>
        文章分类管理
      </el-card>
      </el-header>
    <el-main>
      <el-col span="8">
        <el-card>
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
      <el-col span="6">
        <el-card>
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
          <el-button @click="add">添加</el-button>
        </el-card>
      </el-col>
    </el-main>
  </el-container>
</template>

<script>

import {addType, getTypelist} from "@/config/ApiConfig/articleApiConfig/articleTypeApiConfig";
import {ArticleTypeAddRequestBody} from "@/entity/articleType/ArticleTypeAddRequestBody";

export default {
  name: "articleTypePage",
  data(){
    return{
      checkList:[],
      typelist:null,
      filterText:null,
      articleTypeAddRequestBody:new ArticleTypeAddRequestBody(),
    }
  },
  methods:{
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    add(){
      this.axios.post(addType,this.articleTypeAddRequestBody)
          .then(res=>{
              if (!res.data.code){
                this.$message.success(res.data.message)
              }
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

</style>