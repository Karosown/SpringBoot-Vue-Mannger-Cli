<template>
<div style="text-align: center;">
@{{Time}} PoweredBy {{SiteName}}
</div>
</template>

<script>
import {getValueofAttribute} from "@/config/ApiConfig/apiconfig";
import {globalValue} from "@/config/CommonConfig/globalconfig";

export default {
  name: "globelFooter",
  data(){
    return{
      Time:null,
      SiteName:null
    }
  },
  mounted() {
    var fullYear = new Date().getFullYear();
    this.axios.get(getValueofAttribute+"publishTime")
        .then(res=>{
          if (!res.data.code&&res.data.data == fullYear){
            this.Time=res.data.data
          }
          else {
            this.Time=fullYear.toString()
          }
        })
        .catch(()=>{
          this.Time=fullYear.toString()
        })
    this.axios.get(getValueofAttribute+"siteName")
        .then(res=>{
          if (!res.data.code){
            this.SiteName=res.data.data
          }
          else {

            this.SiteName=globalValue.protectTitle()
          }
        })
        .catch(()=>{
          this.SiteName=globalValue.protectTitle()
        })
  }
}
</script>

<style scoped>

</style>