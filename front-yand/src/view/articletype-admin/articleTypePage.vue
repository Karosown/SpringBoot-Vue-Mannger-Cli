<template>
  <el-container>
    <el-header><el-input
        placeholder="输入关键字进行过滤"
        v-model="filterText">
    </el-input>
      </el-header>
    <el-main>
      <el-tree :data="typelist"
               :filter-node-method="filterNode"
               default-expand-all
               ref="tree"></el-tree>

    ——————文章分类添加
              <el-col span="16">
           <el-cascader
           v-model="selectV"
           :props="{ checkStrictly: true }"
              :options="typelist"
              clearable></el-cascader>
          </el-col>
    <el-input v-model="input" placeholder="请输入内容"></el-input>
    <el-button @click="add"></el-button>
    </el-main>
  </el-container>
</template>

<script>

import {getTypelist} from "@/config/ApiConfig/articleApiConfig/articleTypeApiConfig";

export default {
  name: "articleTypePage",
  data(){
    return{
      typelist:null,
      filterText:null,
      input:null,
      selectV:null
    }
  },
  methods:{
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    add(){

    }
  },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val);
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