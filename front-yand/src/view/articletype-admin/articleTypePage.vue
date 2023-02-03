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
      filterText:null
    }
  },
  methods:{
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
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