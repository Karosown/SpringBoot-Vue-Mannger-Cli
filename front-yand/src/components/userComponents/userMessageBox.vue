<template>
<div>
  <el-form :model="reqBody" label-position='left'>
    <el-row>
      <el-col :span="12">
        <el-form-item label="用户账号" property="userAccount">
          <el-input v-model="reqBody.userAccount">  </el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="用户昵称" property="userName">
          <el-input v-model="reqBody.userName">  </el-input>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item label="用户密码" property="userPassword">
          <el-input v-model="reqBody.userPassword"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="用户邮箱" property="userMail">
          <el-input v-model="reqBody.userMail">  </el-input>
        </el-form-item>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="12">
        <el-form-item label="用户性别" property="userSex">
          <el-select v-model="reqBody.gender" placeholder="请选择">
            <el-option
                v-for="item in [
                {label:'男',value:1},
            {label:'女',value:0}
            ]"
            :key="item.value"
                :label="item.label"
                :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="用户角色" property="userRole">
          <el-select v-model="reqBody.userRole" placeholder="请选择">
            <el-option
                v-for="item in [
              {label:'普通用户',value:'user'},
              {label:'管理员',value:'admin'}
            ]"
                :key="item.value"
                :label="item.label"
                :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>

    <el-form-item label="用户头像" property="userAvator">
      <el-col span="12">
        <el-upload
            ref="uploadlogo"
            class="avatar-uploader"
            action="#"
            :http-request="uploadAvatar"
            :show-file-list="true"
            list-type="picture"
            :before-upload="beforeAvatarUpload"
            :multiple="false"
            :limit="1"
            auto-upload
            drag
        >
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
          <div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过2MB</div>
        </el-upload>
      </el-col>
      <el-col :span="10">
        <el-avatar :size="200" :src="reqBody.userAvatar"></el-avatar>
      </el-col>
  </el-form-item>

  </el-form>
  <el-button type="success" v-if="!this.update" @click="addUser">提交</el-button>
  <el-button type="primary" v-else @click="updateUser">保存</el-button>
  <el-button stype="danger" @click="cancel">取消</el-button>
</div>
</template>

<script>
import {userAddRequestBody} from "@/entity/user/UserAddRequestBody";
import {img2base64File} from "@/config/ApiConfig/fileApiConfig/fileApiConfig";
import {globalValue} from "@/config/CommonConfig/globalconfig";
import {userAdd, userUpdateAdmin} from "@/config/ApiConfig/userApiConfig/userApiConfig";

export default {
  name: "userMessageBox",
  data(){
    return{
      update:this.$parent.$parent.$parent.select!=null,
      reqBody:this.$parent.$parent.$parent.select||new userAddRequestBody()
    }
  },
  methods:{
    updateUser(){
      this.axios.post(userUpdateAdmin,this.reqBody)
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
    cancel(){
      if(this.update){
        this.$parent.$parent.$parent.select=null
        this.$parent.$parent.$parent.edit=false
      }
      else this.$parent.$parent.$parent.addUser=false
    },
    uploadAvatar(param){
      var file = param.file;
      var data=new FormData();
      data.append("image",file)
      this.axios.post(img2base64File,data)
          .then(res=>{
            console.log(res.data)
            if(!res.data.code){
              this.reqBody.userAvatar=globalValue.BASE64HEADER+res.data.data
            }
            else{
              console.log(res.data.message)
            }
          })
          .catch(err=>{
            console.log(err.data)
          })
    },
    beforeAvatarUpload(file) {
      const isJPG = (file.type === 'image/jpeg') || (file.type === 'image/png') || (file.type === 'image/bmp');
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isJPG) {
        this.$message.error('上传头像只能是 jpg、png、bmp 格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      // this.$refs.upload.submit();
      return isJPG && isLt2M;
    },
    addUser(){
      this.axios.post(userAdd,this.reqBody)
          .then(res=>{
              if(!res.data.code){
                  
                  this.$message.success(res.data.message)
              }
              else this.$message.error(res.data.message)
          })
          .catch(err=>{
             this.$message.info(err.data.message)
          })
    }
  }
}
</script>

<style scoped>

</style>