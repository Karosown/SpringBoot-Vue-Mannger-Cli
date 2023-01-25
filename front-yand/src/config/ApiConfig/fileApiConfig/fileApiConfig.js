const base='/file';

//图片转base64api（文件） POST
const img2base64File=base+"/i2b/img";
//图片转base64api（链接） GET
const img2base64Url=base+"/i2b/src?url=";
const siteNavUpload = base +"/upload";

export {
    img2base64File,
    img2base64Url,
    siteNavUpload
}