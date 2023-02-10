<template>
 <div id="siteSettingPage">
  <el-row style="margin-bottom: 25px">
    <b>
     <el-col span="3" style="margin-left: 40px">属性名</el-col>
     <el-col span="9">属性值</el-col>
     <el-col span="2">注释</el-col>
    </b>
  </el-row>
   <el-form ref="form" :model="form" label-width="100px">
<!--    把div改为checkbox-group-->
     <el-checkbox-group v-model="checklist">
       <el-row v-for="(item) in form.commonList" :key="item.attribute" style="margin-bottom: 10px">
       <el-col span="3">
         <el-checkbox  size="medium" style="float: left;"  :label="item.attribute">
           {{item.attribute}}
         </el-checkbox>
       </el-col>
       <el-col span="9">
         <input type="file" v-if="item.type" @change="submit($event,item)"  name="file" style="display: inline"/>
         <el-input v-model="item.value"></el-input>
       </el-col>
         <el-col span="10">
           <el-link style="margin-left:3px;color: #989aa2" :unline="false">{{item.comment}}</el-link>
         </el-col>
       </el-row>
       <el-col span="1">
         <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox>
       </el-col>
     </el-checkbox-group>
     <br>
     <el-form-item>
       <el-button type="primary" @click="save" :disabled="disabled">保存</el-button>
       <el-button type="primary" @click="addbox=true">新增一项</el-button>
       <el-button type="danger" @click="dodel">删除</el-button>
     </el-form-item>
   </el-form>
   <el-dialog
       title="新增配置"
       :visible.sync="addbox"
       width="30%"
       center>
         <el-table-column
      type="selection"
      width="55">
   
    </el-table-column>
     <el-form v-model="newCommon">
       <el-form-item label="属性名">
         <el-input v-model="newCommon.attribute"></el-input>
       </el-form-item>
       <el-form-item label="属性值">
         <el-input v-model="newCommon.value"></el-input>
       </el-form-item>
       <el-form-item label="属性备注">
         <el-input v-model="newCommon.comment"></el-input>
       </el-form-item>
       <el-form-item label="属性类型">
         <el-select v-model="newCommon.type" placeholder="请选择">
           <el-option
              label="文本型" :value=0>
           </el-option>
           <el-option
               label="文件型" :value=1>
           </el-option>
         </el-select>
       </el-form-item>
     </el-form>
     <span slot="footer" class="dialog-footer">
    <el-button @click="addbox = false">取 消</el-button>
    <el-button type="primary" @click="add">确 定</el-button>
  </span>
   </el-dialog>

 </div>
</template>

<script>


import {deletCommon, getCommonList, saveCommon} from "@/config/ApiConfig/commonApiConfig/commonApiConfig";
import {siteNavUpload} from "@/config/ApiConfig/fileApiConfig/fileApiConfig";
import {DeleteRequestBody} from "@/entity/common/deleteRequestBody"

export default {
  name: "siteSettingPage",
  data(){
    return{
      checkAll:false,
      isIndeterminate:false,
      checklist:[], //被勾选了的
      form:{
        commonList:[]
      },
      // index:'',
      //  msg:"",//记录每一条的信息，便于取attribute
      //  DeleteRequsetBody:[],//存放删除的数据
      disabled:false,
      newCommon:{
        attribute:null,
        value:null,
        comment:null,
        type:null,
      },
      addbox:false
    }
  },
  methods:{
    dodel(){
        for(var i=0;i<this.checklist.length;i++){
          for(var j=0;j<this.form.commonList.length;j++){
            if(this.checklist[i]==this.form.commonList[j].attribute){
              this.form.commonList.splice(j,1);
            }
          }
        }
    },
        handleCheckAllChange(val){
      if (val){
        this.checklist=[]
        for(var i=0;i<this.form.commonList.length;i++){
          this.checklist.push(this.form.commonList[i].attribute)
        }
        this.checkAll=this.isIndeterminate=true
      }
      else{
        this.checklist=[]
        this.checkAll=this.isIndeterminate=false
      }
    },
// handleDelete(index, row) {
//        this.index = index;
//        this.msg=row;//每一条数据的记录
//       this.DeleteRequsetBody.push(this.msg.attribute);//把单行删除的每条数据的id添加进放删除数据的数组
//       this.axios.get()
// },
    submit($event,item){
      var file=$event.target.files[0]
      if (file.size>1024*1024*320){
        this.$message.error("文件上传最多为320MB")
      }
      var data =new FormData();
        var suffname=file.name.toString();
        suffname=suffname.substring(suffname.lastIndexOf('.'));
        data.append('file',file,'sitePage'+item.attribute+suffname);
      this.$message.info(item.attribute+"正在上传，请稍后")
      this.axios.post(siteNavUpload,data)
            .then(res=>{
                if (!res.data.code){
                  item.value=res.data.data
                  this.$message.success(item.attribute+"上传成功！")
                }
            })
    },
    add(){
      this.form.commonList.push({
        attribute:this.newCommon.attribute,
        value:this.newCommon.value,
        comment:this.newCommon.comment,
        type:this.newCommon.type
      })
      this.addbox=false
    },
    save(){
      this.axios.post(saveCommon,this.form)
          .then(res=>{
            if (!res.data.code){
              this.$message.success(res.data.message)
            }
            else{
              this.$message(
                  {
                    message:("错误码:"+res.data.code+"\n服务器错误！\n错误信息："+res.data.message),
                    dangerouslyUseHTMLString:true,
                    type:"error"
                  }
              )
            }
          })
                var A=new DeleteRequestBody
      A.id=this.checklist
      this.axios.post(deletCommon,A)
      .then(res=>{
        if(!res.data.code){
          this.$message.success(res.data.message)
      
        }
      })
    }
  },
  mounted() {
    //   this.axios.get(getValueListByCommon)
    //       .then(res=>{
    //         this.form.values=res.data.data;
    //       })
    // this.axios.get(getAttributeListByCommon)
    //     .then(res=>{
    //       this.form.attributes=res.data.data;
    //     })
    this.axios.get(getCommonList)
        .then(res=>{
          if (!res.data.code){
            this.form.commonList=res.data.data
          }
          else{
            this.$message.error("错误码:"+res.data.code+"\n服务器错误！\n错误信息："+res.data.message)
          }
        })
  }
}
</script>

<style scoped>
#siteSettingPage{
  margin-top: 100px;
}
</style>