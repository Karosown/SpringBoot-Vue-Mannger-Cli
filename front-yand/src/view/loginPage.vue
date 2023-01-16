<template>
  <div id="login" >
    <el-form ref="loginForm"  label-width="80px" class="login-box">
      <h3 class="login-title">欢迎登录</h3>
      <el-form-item  label="账号" prop="username">
        <el-input type="text" placeholder="请输入账号"/>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input type="password" placeholder="请输入密码"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" v-on:click="onSubmit('loginForm')">登录</el-button>
      </el-form-item>
    </el-form>

    <el-dialog
        title="温馨提示"
        :visible.sync="dialogVisible"
        width="30%"
        :before-close="handleClose">
      <span style="color: #ffffff">请输入账号和密码</span>
      <span slot="footer" class="dialog-footer">
        <el-button style="color: #ffffff" type="primary" @click="dialogVisible = false">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "loginPage"
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