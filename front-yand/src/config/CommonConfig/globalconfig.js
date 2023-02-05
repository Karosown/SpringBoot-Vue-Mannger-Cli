//项目名
import {getAttributeByCommon} from "@/config/ApiConfig/commonApiConfig/commonApiConfig";
import {baseAPI} from "@/config/ApiConfig/apiconfig";

let globalValue={
protectTitle() {
    var xhr=new XMLHttpRequest()
    xhr.open('GET',baseAPI+getAttributeByCommon+'siteName',false)
    xhr.send(null)
    if (xhr.status==200) return JSON.parse(xhr.responseText).data
    else return "XXXX管理系统"
   },
    BASE64HEADER:"data:image/png;base64,"
}

export {
    globalValue
}