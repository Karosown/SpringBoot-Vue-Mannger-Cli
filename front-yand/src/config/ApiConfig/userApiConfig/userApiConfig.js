const base='/user';

//用户密码修改API POST
const userUpdatePassword=base+"/update/resetpassword"
//用户信息更新api - 普通信息
const userUpdateMessage=base+"/update"
//邮箱验证码发送api
const checkCodeSend = "/checkcode/send";
//用户注册api
const userRegister=base+"/register";
//通过id获取用户信息 GET
const getUserById=base+"/get?id=";
//获取用户昵称
const getUserNamebyID=base+"/get/userName?id=";
//获取用户头像api get
const getUserAvatarByUserAccount=base+"/get/userAvatar?useraccount=";
const getUserAvatarById=base+"/get/userAvatar?id=";

export {
    checkCodeSend,
    userUpdatePassword,
    userUpdateMessage,
    userRegister,
    getUserById,
    getUserNamebyID,
    getUserAvatarByUserAccount,
    getUserAvatarById,
}