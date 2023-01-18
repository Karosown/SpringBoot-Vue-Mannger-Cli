<template>
  <div id="login" >
    <el-form v-model="loginForm"  label-width="80px" class="login-box">
      <h3 class="login-title">欢迎登录</h3>
      <el-form-item  label="账号" prop="username">
        <el-input type="text" v-model="loginForm.userAccount" placeholder="请输入账号"/>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input type="password" v-model="loginForm.userPassword" placeholder="请输入密码"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import {sysLogin} from "@/config/apiconfig";
import {loginForm} from "@/config/varConfig/userVarConfig/userVarConfig";

export default {
  name: "loginPage",
  data(){
    return{
      loginForm:loginForm
    }
  },
  methods:{
    onSubmit(){
      this.axios.post(sysLogin,loginForm)
          .then(res=>{
            if (!res.data.code){
              sessionStorage.setItem('loginStatus',JSON.stringify(res.data.data))
              this.$message.success("登录成功")
              this.$router.push({path:'/manggerCenter'})
            }
            else{
              this.$message.error("登录失败！\n"+res.data.message)
            }
          })
          .catch(err=>{
            this.$message.info("服务器错误"+err.data.methods)
          })
    },
    reg(){

    }
  }
}
</script>

<style scoped>
#login{
  background: url("https://tu.ltyuanfang.cn/api/fengjing.php");
  height: 100%;
  width: 100%;
  /*background: no-repeat;*/
  background-size: 100%;
  position: absolute;
}
.login-box {
  border: 0px solid #DCDFE6;
  width: 350px;
  margin: 180px auto;
  padding: 35px 35px 15px 35px;
  border-radius: 5px;
  -webkit-border-radius: 5px;
  -moz-border-radius: 5px;
  box-shadow: 0 0 25px #999090;
  background-color: rgba(255,255,255, 0.51);
}

.login-title {
  text-align: center;
  margin: 0 auto 40px auto;
  color: #000000;
}
</style>