import Vue from 'vue'   //引入Vue
import VueRouter from 'vue-router'
import loginPage from "@/view/loginPage";
import manggerCenter from "@/view/manggerCenter";
import siteSettingPage from "@/view/manngger-main/siteSettingPage";
import {globalValue} from "@/config/varconfig";
import apiDoc from "@/view/manngger-main/apiDoc";
import classPage from "@/view/manngger-main/classPage";
import articlePage from "@/view/manngger-main/articlePage";
import ariticlePublishPage from "@/view/article-main/ariticlePublishPage";  //引入vue-router

Vue.use(VueRouter)  //Vue全局使用Router
const router = new VueRouter({
    mode:'history',//去除#号
    routes: [              //配置路由，这里是个数组
        {   //配置默认路由
            path:'/',
            component: manggerCenter,
            meta:{
                title:globalValue.protectTitle()+globalValue.protectTitle,
                mustLogin:true
            }
        },
        {
            path:'/login',
            component:loginPage,
            meta:{
                title:globalValue.protectTitle()+globalValue.protectTitle+'用户登录'
            }
        },
        {
            path:'/manggerCenter',
            component: manggerCenter,
            meta:{
                title:globalValue.protectTitle,
                mustLogin:true
            },
            children:[
                {
                    path:'/settingPage',
                    component:siteSettingPage,
                    meta:{
                        title:globalValue.protectTitle()+'站点设置',
                        mustLogin:true
                    }
                },
                {
                    path:'/apidoc',
                    component:apiDoc,
                    meta:{
                        title:globalValue.protectTitle()+'API文档',
                        mustLogin:true
                    }
                },
                {
                  path:'/classPage',
                  component:classPage,
                  meta:{
                      title:globalValue.protectTitle()+'分类管理',
                      mustLogin:true
                  }
                },
                {
                    path:'/articlePage',
                    component:articlePage,
                    meta:{
                        title:globalValue.protectTitle()+'文章管理',
                        mustLogin:true
                    }
                },
                {
                    path:'/articlePublish',
                    component:ariticlePublishPage
                },
            ]
        },
    ]
});
export default router;
