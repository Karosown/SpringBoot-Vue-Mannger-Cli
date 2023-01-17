//基础API配置
var api_run="http://articlemannger.api.wzl1.top/api"; //运行环境接口
const api_dev="http://127.0.0.1:8681/api";      //开发环境接口
//环境切换，自动判断开发环境和运行环境
export const baseAPI=process.env.NODE_ENV ==='production'?api_run:api_dev;
//项目名
export const protectTitle='XXXX管理系统';







//用户登录api
const sysLogin="/user/login";
//获取当前登录用户 get
const sysGetlogin="/user/get/login";
//用户注销 post
const sysLogout="/user/logout";
export {


        sysLogin,



        sysGetlogin,
        sysLogout,

}