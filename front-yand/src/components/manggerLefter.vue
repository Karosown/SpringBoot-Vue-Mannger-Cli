<template>
    <el-menu
        default-active="/articlePage"
        class="el-menu-vertical-demo"
        router="true"
    >
      <el-avatar id="logo" shape="squre" :size="78" :src="logo" style="background-color: #ffffff"></el-avatar>
      <el-menu-item index="/articlePage">
        <i class="el-icon-notebook-2"></i>
        <span slot="title">文章管理</span>
      </el-menu-item>
      <el-menu-item index="/class"  disabled>
        <i class="el-icon-chat-square"></i>
        <span slot="title">分类管理</span>
      </el-menu-item>
      <el-menu-item index="2"  disabled>
        <i class="el-icon-user"></i>
        <span slot="title">用户管理</span>
      </el-menu-item>
      <el-menu-item index="/settingPage" >
        <i class="el-icon-set-up"></i>
        <span slot="title">站点设置</span>
      </el-menu-item>
<!--      <el-menu-item index="4">-->
<!--        <i class="el-icon-setting"></i>-->
<!--        <span slot="title">引导页设置</span>-->
<!--      </el-menu-item>-->
      <el-menu-item index="/apidoc" >
        <i class="el-icon-set-up"></i>
        <span slot="title">前台对接API文档</span>
      </el-menu-item>
    </el-menu>
</template>

<script>

import {getAttributeByCommon} from "@/config/ApiConfig/commonApiConfig/commonApiConfig";

export default {
  name: "manggerLefter",
  data(){
    return{
      logo:null
    }
  },
  mounted() {
    this.axios.get(getAttributeByCommon+'siteLogo')
        .then(res=>{
          if (!res.data.code){
            this.logo=res.data.data
          }
        })
  }
}
</script>

<style scoped>
.el-menu-vertical-demo{
  height: 100vh;
  text-align: center;
}
#logo{
  margin-top: 50px;
}
</style>