<template>
<!--

摘要 存为草稿、发布

文章分类

文章标签

热点图

修改历史

-->

  <el-collapse v-model="activeNames" @change="handleChange">
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
            v-model="articleBody.publishTime"
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
              clearable></el-cascader>
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
      <div>简化流程：设计简洁直观的操作流程；</div>
      <div>清晰明确：语言表达清晰且表意明确，让用户快速理解进而作出决策；</div>
      <div>帮助用户识别：界面简单直白，让用户快速识别而非回忆，减少用户记忆负担。</div>
    </el-collapse-item>
    <el-collapse-item title="可控 Controllability" name="4">
      <div>用户决策：根据场景可给予用户操作建议或安全提示，但不能代替用户进行决策；</div>
      <div>结果可控：用户可以自由的进行操作，包括撤销、回退和终止当前操作等。</div>
    </el-collapse-item>
  </el-collapse>
</template>

<script>



import {getTypelist} from "@/config/ApiConfig/articleApiConfig/articleTypeApiConfig";

export default {
  name: "articleAsideBox",
  data(){
    return{
      articleBody:this.$parent.$parent.$parent.$parent.articleAddRequest,
      tempLabel:null,
      types:null
    }
  },
  methods:{
    addLabel(){
      let items = this.tempLabel;
      this.articleBody.labelList.push(items)
    }
  },
  mounted() {
    this.axios.get(getTypelist)
        .then(res=>{
          if (!res.data.code){
            this.types=res.data.data
          }
        })
  }
}
</script>

<style scoped>
span{
  margin-left: 25px;
}
</style>