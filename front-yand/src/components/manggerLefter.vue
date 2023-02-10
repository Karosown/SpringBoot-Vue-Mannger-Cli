<template>
    <el-menu
        default-active="/articlePage"
        class="el-menu-vertical-demo"
        router
    >
      <el-avatar id="logo" shape="squre" :size="78" :src="logo" style="background-color: #ffffff"></el-avatar>
      <el-menu-item index="/articlePage">
        <i class="el-icon-notebook-2"></i>
        <span slot="title">文章管理</span>
      </el-menu-item>
      <el-menu-item index="/articleTypePage">
        <i class="el-icon-chat-square"></i>
        <span slot="title">分类管理</span>
      </el-menu-item>
      <el-menu-item index="/userAdminPage"  >
        <i class="el-icon-user"></i>
        <span slot="title">用户管理</span>
      </el-menu-item>
      <el-menu-item index="/settingPage" >
        <i class="el-icon-set-up"></i>
        <span slot="title">站点设置</span>
      </el-menu-item>
      <!--   @click="logout()" index="/loginpage"-->
      <el-menu-item @click="logout()">
        <i class="el-icon-user-solid"></i>
        <span slot="title">用户退出</span>
      </el-menu-item>
      <el-menu-item index="/apidoc" >
        <i class="el-icon-set-up"></i>
        <span slot="title">前台对接API文档</span>
      </el-menu-item>
    </el-menu>
</template>

<script>

import {getAttributeByCommon} from "@/config/ApiConfig/commonApiConfig/commonApiConfig";
import { sysLogout } from '@/config/ApiConfig/apiconfig';

export default {
  name: "manggerLefter",
  data(){
    return{
      logo:null
    }
  },
  methods:{
    logout(){
      this.axios.post(sysLogout)
        .then(res=>{
          if(!res.data.code){
            this.$message.success(res.data.message);
            sessionStorage.removeItem('loginStatus')
          this.$router.push('/login');
          }
        })

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