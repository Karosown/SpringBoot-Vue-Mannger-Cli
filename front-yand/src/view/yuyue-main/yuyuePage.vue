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
        <el-button @click="addyuyue=true,select=null" type="primary">新增预约记录</el-button>
      </el-col>
    </el-header>
    <el-main>
      <el-table border lazy
                ref="multipleTable"
                :data="yuyueDatas"
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
            property="userId"
            label="学号"
            width="150">
        </el-table-column>
        <el-table-column
            property="userName"
            label="姓名"
            width="122">
        </el-table-column>
        <el-table-column
            property="gender"
            label="性别"
            sortable
            width="75">
          <template slot-scope="scope">
            <el-tag type="primary" v-if="scope.row.gender==1">男</el-tag>
            <el-tag type="danger" v-else-if="scope.row.gender==0">女</el-tag>
            <el-tag type="info" v-else>未知</el-tag>
          </template>
        </el-table-column>
        <el-table-column
            property="phoneNumber"
            label="手机号"
            sortable
            width="159">
        </el-table-column>
        <el-table-column
            property="userEmail"
            label="用户邮箱"
            width="150">
        </el-table-column>
        <el-table-column
            property="message"
            label="留言"
            width="110">
          <template slot-scope="scope">
            <el-button size="mini" @click="openInner=true,select=scope.row.message">点击查看
            </el-button>
          </template>
        </el-table-column>
        <el-table-column
            property="createTime"
            label="创建日期"
            sortable
            width="159">
        </el-table-column>
        <el-table-column
            property="updateTime"
            label="最近修改日期"
            sortable
            width="159">
        </el-table-column>
        <el-table-column
            property="isPass"
            label="当前状态"
            width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isPass?'success':'info'">
              {{scope.row.isPass?'通过':'未通过'}}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220">
          <template slot-scope="scope">

            <el-button :key="new Date().getTime()" :type="scope.row.isPass?'danger':'success'"
                       size="mini"
                       @click="pass(scope.row.id,scope.row.isPass)">{{ scope.row.isPass?'撤销':'通过' }}</el-button>
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
    <el-dialog :visible.sync="openInner" title="对母校的留言">
        <el-input :value="this.select">
        </el-input>
    </el-dialog>
    <el-dialog :visible.sync="edit" title="编辑">
      <yuyue-message-box :key="new Date().getTime()"></yuyue-message-box>
    </el-dialog>
    <el-dialog :visible.sync="addyuyue" title="新增预约记录">
      <yuyue-message-box ></yuyue-message-box>
    </el-dialog>
  </el-container>
</template>

<script>
import {DeleteRequestBody} from "@/entity/common/deleteRequestBody";
// import {globalValue} from "@/config/CommonConfig/globalconfig";
import yuyue from "@/config/yuyue";
import {YuyueQueryBody} from "@/entity/yuyue/yuyueQueryBody";

export default {
  name: "yuyuePage",
  data(){
    return{
      select:null,
      edit:false,
      yuyue:sessionStorage.getItem('loginStatus'),
      total:null,
      yuyueDatas:null,
      currentPage:1,
      searchText:null,
      addyuyue:false,
      yuyueQureyPageBody: {},
      openInner:false,
      reqBody:{
        yuyueVoList:[]
      }
    }
  },
  methods:{
    async pass(id, ispass) {
      ispass ^= true
      await this.axios.put(yuyue.updateAppointment + `/${id}`, {
        // id:id,
        isPass: ispass
      })
          .then(response => {
            var res = response.data
            if (!res.code) {
              this.$message.success(res.message)
            } else this.$message.error(res.message)
            location.reload();
          })
          .catch(err => {
            this.$message.info(err.data.message)
          })
    },
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
      this.$router.push('/yuyueGarbagePage')
    },
    delete(data){
      var deleteRequestBody = new DeleteRequestBody();
      deleteRequestBody.id=data.id
      this.axios.delete(yuyue.deleteAppointmentm,{
        params:{
          id:data.id
        }
      })
          .then(res=>{
            if (!res.data.code){
              this.$message.success(res.data.message)
              this.yuyueDatas.splice(this.yuyueDatas.indexOf(data),1)
            }
            else this.$message.error(res.data.message)
          })
    },
    handleDelete(data){
      if (data.yuyueRole=='admin'){
        this.$confirm('用户 Id='+data.id+' '+data.yuyueAccount+'('+data.yuyueName+') 为管理员账户, 是否继续删除?', '提示', {
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
      for(var i=0;i<this.reqBody.yuyueVoList.length;i++)
        this.handleDelete(this.reqBody.yuyueVoList[i]);

    },
    // downloadExcel(){
    //   this.axios.post(exportVoExcel, this.reqBody,{
    //     responseType:"blob"
    //   })
    //       .then(res=>{
    //         globalValue.downloadFn(res.data)
    //       })
    // },
    addCheck(val){
      this.reqBody.yuyueVoList=val;
    },
    handleCurrentChange(val){
      this.yuyueQureyPageBody.current=val
      this.send()

    },
    handleSizeChange(val){
      this.yuyueQureyPageBody.pageSize=val;
      this.send()
    },
    search(){

    },
    send(){
      this.axios.get(yuyue.getAllAppointments,new YuyueQueryBody)
          .then(res=>{

            this.total=res.data.data.total
            this.yuyueDatas=res.data.data.records
          })
    },
    // newyuyue(){
    //   this.$router.push({path:'/yuyuePublish'});
    // }
  },
  mounted() {
    // this.yuyueQureyPageBody.sortField='createTime'
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