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
    BASE64HEADER:"data:image/png;base64,",
    downloadFn: (flow = null) => {
        if (!flow) return
        const blob = new Blob([flow])
        const blobUrl = window.URL.createObjectURL(blob)

        const a = document.createElement('a')
        a.style.display = 'none'
        a.download = new Date().getTime()+'.xls' // 自定义下载的文件名
        a.href = blobUrl
        a.click()
    }
}

export {
    globalValue
}