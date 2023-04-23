import Vue from 'vue'   //引入Vue
import VueRouter from 'vue-router'
import loginPage from "@/view/loginPage";
import manggerCenter from "@/view/manggerCenter";
import siteSettingPage from "@/view/manngger-main/siteSettingPage";
import {globalValue} from "@/config/CommonConfig/globalconfig";
import apiDoc from "@/view/manngger-main/apiDoc";
import classPage from "@/view/manngger-main/classPage";
import articlePage from "@/view/article-main/articlePage";
import ariticlePublishPage from "@/view/article-main/ariticlePublishPage";
import articleTypePage from "@/view/articletype-admin/articleTypePage";
import articleUpdatePage from "@/view/article-main/articleUpdatePage";
import articleGarbagePage from "@/view/article-main/articleGarbagePage";
import userPage from "@/view/user-admin/userPage";
import yuyuePage from "@/view/yuyue-main/yuyuePage";
//引入vue-router

Vue.use(VueRouter)  //Vue全局使用Router
const router = new VueRouter({
    mode:'history',//去除#号
    routes: [              //配置路由，这里是个数组
        {   //配置默认路由
            path:'/',
            redirect:'/articlePage',
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
        // {
        //     path:'/loginpage',
        //     component:loginPage,
        //     meta:{
        //         title:globalValue.protectTitle(),
        //         mustLogin:true
        //     }
        // },
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
                    path:'/yuyuePage',
                    component:yuyuePage,
                    meta:{
                        title:globalValue.protectTitle()+'预约管理',
                        mustLogin:true
                    }
                },
                {
                    path:'/apidoc',
                    component:apiDoc,
                    meta:{
                        title:globalValue.protectTitle()+'API文档',
                        mustLogin:true,
                        mustAdmin:true
                    }
                },
                {
                  path:'/classPage',
                  component:classPage,
                  meta:{
                      title:globalValue.protectTitle()+'分类管理',
                      mustLogin:true,
                      mustAdmin:true
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
                    component:ariticlePublishPage,
                    meta:{
                        title:globalValue.protectTitle()+'文章发布',
                        mustLogin:true
                    }
                },
                {
                    path:'/articleUpdate',
                    component:articleUpdatePage,
                    meta:{
                        title:globalValue.protectTitle()+'文章修改',
                        mustLogin:true
                    }
                },
                {
                    path:'/articleTypePage',
                    component:articleTypePage,
                    meta:{
                        title:globalValue.protectTitle()+'文章分类管理',
                        mustLogin:true,
                        mustAdmin:true
                    }
                },
                {
                    path:'/articleGarbagePage',
                    component:articleGarbagePage,
                    meta:{
                        title:globalValue.protectTitle()+'文章回收站管理',
                        mustLogin:true
                    }
                },
                {
                    path:'/userPage',
                    component:userPage,
                    meta:{
                        title:globalValue.protectTitle()+'用户管理',
                        mustLogin:true,
                        mustAdmin:true
                    }
                }
            ]
        },
    ]
});
export default router;
