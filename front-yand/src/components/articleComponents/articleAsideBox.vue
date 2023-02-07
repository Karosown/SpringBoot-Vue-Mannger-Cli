<template>
<!--

摘要 存为草稿、发布

文章分类

文章标签

热点图

修改历史

-->

  <el-collapse>
    <el-collapse-item title="文章摘要" name="1">
      <el-row>
        <el-col span="8">
            <span class="text-title">发布</span>
        </el-col>
        <el-col span="8">
          <el-dropdown>
        <el-button type="text">{{!articleBody.isPublic?"草稿":"发布"}}</el-button>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item @click.native="articleBody.isPublic=0">草稿</el-dropdown-item>
          <el-dropdown-item @click.native="articleBody.isPublic=1">发布</el-dropdown-item>
        </el-dropdown-menu>
          </el-dropdown>
          </el-col>
      </el-row>
      <el-row>
        <el-col span="8">
         <span class="text-title">定时发布</span>
        </el-col>
          <el-col span="8">

          <el-date-picker
            v-model="publishTime"
            type="datetime"
            placeholder="选择日期时间,留空为不选择">
        </el-date-picker>
          </el-col>
      </el-row>
    </el-collapse-item>
    <el-collapse-item title="分类与标签" name="2">
        <e-row>
          <el-col span="8">
          <span>分类</span>
          </el-col>
          <el-col span="16">
           <el-cascader
              :options="types"
              v-model="type"
              clearable>
           </el-cascader>
          </el-col>
        </e-row>
      <e-row>
        <e-row>
            <e-col span="8">
              <span>标签</span>
            </e-col>
          <e-col span="16">
            <el-input v-model="tempLabel" @keyup.enter.native="addLabel" placeholder="请输入标签"></el-input>
          </e-col>
        </e-row>
        <e-row>
          <el-tag
              style="margin: 5px"
              :key="tag"
              v-for="tag in articleBody.labelList"
              closable
              :disable-transitions="false"
              @close="articleBody.labelList.splice(articleBody.labelList.indexOf(tag),1)"
          >
            {{tag}}
          </el-tag>
        </e-row>
      </e-row>
    </el-collapse-item>
    <el-collapse-item title="特色图片" name="3">
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
          drag>
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过500kb</div>
      </el-upload>
    </el-collapse-item>
    <el-collapse-item title="文章简介" name="4">
        <el-input type="textarea"
                  :rows="4"
                  placeholder="留空则自动填写"
                v-model="articleBody.articleIntroduction"
        >

        </el-input>
    </el-collapse-item>
  </el-collapse>
</template>

<script>



import {getTypelist} from "@/config/ApiConfig/articleApiConfig/articleTypeApiConfig";
import {img2base64File} from "@/config/ApiConfig/fileApiConfig/fileApiConfig";

export default {
  name: "articleAsideBox",
  data(){
    return{
      type:[],
      publishTime:null,
      articleBody:this.$parent.$parent.$parent.$parent.articleAddRequest,
      tempLabel:null,
      types:null
    }
  },
  methods:{
    addLabel(){
      let items = this.tempLabel;
      this.articleBody.labelList.push(items)
    },

    uploadAvatar(param){
      var file = param.file;
      var data=new FormData();
      data.append("image",file)
      this.axios.post(img2base64File,data)
          .then(res=>{
            console.log(res.data)
            if(!res.data.code){
              // globalValue.BASE64HEADER
              this.articleBody.featImg=res.data.code
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
  },
  mounted() {
    this.axios.get(getTypelist)
        .then(res=>{
          if (!res.data.code){
            this.types=res.data.data
          }
        })
  },
  watch:{
    type(){
      this.articleBody.type=this.type[this.type.length-1]
    },
    publishTime(){
      this.articleBody.publishTime=this.publishTime.getTime()
    }
  }
}
</script>

<style scoped>
span{
  margin-left: 25px;
}
</style>