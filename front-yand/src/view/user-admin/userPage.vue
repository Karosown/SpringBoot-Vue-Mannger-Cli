<template>
  <el-container >
    <el-header>
      <el-col span="12">
        <el-button type="info" icon="el-icon-download" @click="downloadExcel">导出为excel</el-button>
        <el-button type="danger" icon="el-icon-delete" @click="deleteByList">批量删除</el-button>
<!--        <el-button type="primary" @click="gotoGarbage">回收站</el-button>-->
      </el-col>
      <el-col span="12">
        <el-col span="12" style="margin-right: 3px">
          <el-input v-model="searchText" @keyup.enter.native="search">
            <i slot="suffix" class="el-icon-search" @click="search"></i>
          </el-input>
        </el-col>
        <el-button @click="addUser=true,select=null" type="primary">新增用户</el-button>
      </el-col>
    </el-header>
    <el-main>
      <el-table border lazy
                ref="multipleTable"
                :data="userDatas"
                style="width: 100%"
                @selection-change="addCheck"
      >
        <el-table-column type="selection" width="39"></el-table-column>
        <el-table-column
            property="id"
            label="id"
            sortable
            width="59">
        </el-table-column>
        <el-table-column
            property="createTime"
            label="注册日期"
            sortable
            width="159">
        </el-table-column>
        <el-table-column
            property="userAccount"
            label="用户账号"
            width="230">
        </el-table-column>
        <el-table-column
            property="userName"
            label="用户昵称"
            width="230">
        </el-table-column>
        <el-table-column
            property="gender"
            label="性别"
            sortable
            width="80">
          <template slot-scope="scope">
            <el-tag type="primary" v-if="scope.row.gender==1">男</el-tag>
            <el-tag type="danger" v-else-if="scope.row.gender==0">女</el-tag>
            <el-tag type="info" v-else>未知</el-tag>
          </template>
        </el-table-column>
        <el-table-column
            property="userMail"
            label="用户邮箱"
            width="250">
        </el-table-column>
        <el-table-column
            property="userRole"
            label="角色"
            width="90">
          <template slot-scope="scope">
            <el-tag type="warning" v-if="scope.row.userRole=='admin'">管理员</el-tag>
            <el-tag v-else>用户</el-tag>
          </template>
        </el-table-column>
        <el-table-column
            property="updateTime"
            label="最近修改日期"
            sortable
            width="159">
        </el-table-column>
        <el-table-column
            property="userAvatar"
            label="用户头像"
            width="100">
          <template slot-scope="scope">
            <el-avatar :src="scope.row.userAvatar"></el-avatar>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="143">
          <template slot-scope="scope">
            <el-button type="primary"
                       size="mini"
                       @click="edit=true,select=scope.row">编辑</el-button>
            <el-button
                size="mini"
                type="danger"
                @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-main>
    <el-footer>


      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[10, 20, 30, 40,50]"
          :page-size="10"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </el-footer>
    <el-dialog :visible.sync="edit" title="编辑用户">
      <user-message-box :key="new Date().getTime()"></user-message-box>
    </el-dialog>
    <el-dialog :visible.sync="addUser" title="新增用户">
      <user-message-box ></user-message-box>
    </el-dialog>
  </el-container>

</template>

<script>
import {deleteUser, exportVoExcel, getUserslistPage} from "@/config/ApiConfig/userApiConfig/userApiConfig";
import {userQureyRequestBody} from "@/entity/user/UserQueryRequestBody";
import {globalValue} from "@/config/CommonConfig/globalconfig";
import {DeleteRequestBody} from "@/entity/common/deleteRequestBody";
import UserMessageBox from "@/components/userComponents/userMessageBox";

export default {
  name: "userPage",
  components: {UserMessageBox},
  data(){
    return{
      select:null,
      edit:false,
      user:sessionStorage.getItem('loginStatus'),
      total:null,
      userDatas:null,
      currentPage:1,
      searchText:null,
      addUser:false,
      userQureyPageBody:new userQureyRequestBody(),
      reqBody:{
        userVoList:[]
      }
    }
  },
  methods:{
    toggleSelection(rows) {
      if (rows) {
        rows.forEach(row => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    },
    gotoGarbage(){
      this.$router.push('/userGarbagePage')
    },
    delete(data){
      var deleteRequestBody = new DeleteRequestBody();
      deleteRequestBody.id=data.id
      this.axios.post(deleteUser,deleteRequestBody)
          .then(res=>{
            if (!res.data.code){
              this.$message.success(res.data.message)
              this.userDatas.splice(this.userDatas.indexOf(data),1)
            }
            else this.$message.error(res.data.message)
          })
    },
    handleDelete(data){
      if (data.userRole=='admin'){
        this.$confirm('用户 Id='+data.id+' '+data.userAccount+'('+data.userName+') 为管理员账户, 是否继续删除?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        .then(()=>{
          this.delete(data);
        })
            .catch(()=>{
            })
      }
      else{
        this.delete(data);
      }
    },
    deleteByList(){
      for(var i=0;i<this.reqBody.userVoList.length;i++)
        this.handleDelete(this.reqBody.userVoList[i]);

    },
    downloadExcel(){
      this.axios.post(exportVoExcel, this.reqBody,{
        responseType:"blob"
      })
          .then(res=>{
            globalValue.downloadFn(res.data)
          })
    },
    addCheck(val){
      this.reqBody.userVoList=val;
    },
    handleCurrentChange(val){
      this.userQureyPageBody.current=val
      this.send()

    },
    handleSizeChange(val){
      this.userQureyPageBody.pageSize=val;
      this.send()
    },
    search(){

    },
    send(){
      this.axios.get(getUserslistPage,{
        params:this.userQureyPageBody
      })
          .then(res=>{
            this.total=res.data.data.total
            this.userDatas=res.data.data.records
          })
    },
    // newuser(){
    //   this.$router.push({path:'/userPublish'});
    // }
  },
  mounted() {
    this.userQureyPageBody.sortField='createTime'
    this.send()
  }
}
</script>

<style scoped>
.el-icon-search{
  border-radius: 3px;
  padding:8px;
  margin-top: 2px;
  margin-right: -2px;
  font-size: 20px
}
.el-icon-search:hover{
  background-color: #f1f1f1;
}
</style>