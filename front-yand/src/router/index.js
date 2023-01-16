import Vue from 'vue'   //引入Vue
import VueRouter from 'vue-router'
import loginPage from "@/view/loginPage";
import {protectTitle} from "@/config/apiconfig";
import manngerCenter from "@/view/manngerCenter";  //引入vue-router

Vue.use(VueRouter)  //Vue全局使用Router
const router = new VueRouter({
    mode:'history',//去除#号
    routes: [              //配置路由，这里是个数组
        {   //配置默认路由
            path:'/',
            component: manngerCenter,
            meta:{
                title:protectTitle,
                mustLogin:true
            }
        },
        {
            path:'/login',
            component:loginPage,
            meta:{
                title:protectTitle+'用户登录'
            }
        },
        {
            path:'/manngerCenter',
            component: manngerCenter,
            meta:{
                title:protectTitle,
                mustLogin:true
            }
        }
    ]
});
export default router;
