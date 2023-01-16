//基础API配置
var api_run="http://articlemannger.api.wzl1.top/api"; //运行环境接口
const api_dev="http://127.0.0.1:8681/api";      //开发环境接口
//环境切换，自动判断开发环境和运行环境
export const baseAPI=process.env.NODE_ENV ==='production'?api_run:api_dev;
//项目名
export const protectTitle='XXXX管理系统';


//点赞
const doThumbArticle  ="/article/thumb"
//删除日记API POST
const deleteArticle    ="/article/delete"
//更新日记API post
const updateArticles   ="/article/update"
const getArticleByid="/article/get?id="
const getArticleslistByFavorite="/article/list/myfavorite"
const getArticleslistByArticleTitle="/article/list/page?sortField=createTime&articleTitle=";
//获取某个用户笔记列表
const getArticleslistByuserid="/article/list/page?sortField=createTime&userId=";
//新增日记api post
const addArticles="/article/add"
//用户密码修改API POST
const userUpdatePassword="/user/update/resetpassword"
//用户信息更新api - 普通信息
const userUpdateMessage="/user/update"
//邮箱验证码发送api
const checkCodeSend = "/checkcode/send";
//用户注册api
const userRegister="/user/register";
//用户登录api
const userLogin="/user/login";
//图形验证码生成api
const imgcheckcodeTouch="/checkcode/touch/";
//图片转base64api（文件） POST
const img2base64File="/file/i2b/img";
//图片转base64api（链接） GET
const img2base64Url="/file/i2b/src?url=";
//通过id获取用户信息 GET
const getUserById="/user/get?id=";
//获取用户昵称
const getUserNamebyID="/user/get/userName?id=";
//获取用户头像api get
const getUserAvatarByUserAccount="/user/get/userAvatar?useraccount=";
const getUserAvatarById="/user/get/userAvatar?id=";
//获取当前登录用户 get
const sysGetlogin="/user/get/login";
//用户注销 post
const sysLogout="/user/logout";
export {
        doThumbArticle,
        deleteArticle,
        getArticleslistByFavorite,
        getArticleslistByArticleTitle,
        getArticleslistByuserid,
        getArticleByid,
        updateArticles,
        addArticles,
        userUpdatePassword,
        userUpdateMessage,
        userRegister,
        userLogin,
        getUserNamebyID,
        getUserAvatarByUserAccount,
        getUserAvatarById,
        img2base64File,
        img2base64Url,
        imgcheckcodeTouch,
        sysGetlogin,
        sysLogout,
        checkCodeSend,
        getUserById
}