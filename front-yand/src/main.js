import Vue from 'vue'
import App from './App.vue'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
Vue.config.silent = true
Vue.use(ElementUI)
// 引入axios
import axios from 'axios'
import { baseAPI, sysGetlogin } from '@/config/ApiConfig/apiconfig'
import router from '@/router'
import { globalValue } from '@/config/CommonConfig/globalconfig'
// import { sysLogout } from '@/config/ApiConfig/apiconfig';
// 挂载到vue原型链上
axios.defaults.baseURL = baseAPI
axios.defaults.crossDomain = true
axios.defaults.withCredentials = true //设置cross跨域 并设置访问权限 允许跨域携带cookie信息
Vue.prototype.axios = axios
Vue.config.productionTip = false

// 对于一些需要监视的sessionStorage的变量可任意选择使用resetSetItem函数，当然，也要用监视器
/**
 *      window.addEventListener('setItem',()=>{
 *         this.list=JSON.parse(sessionStorage.getItem('notelist'))
 *         this.isSearchOrFavorite=JSON.parse(sessionStorage.getItem('isSearchOrFavorite'))
 *       })
 */
Vue.prototype.resetSetItem = function (key, newVal) {
  switch (key) {
    case 'articlelist':
    case 'updateText':
      var newStorageEvent = document.createEvent('StorageEvent');
      var storage = {
        setItem: function (k, val) {
          sessionStorage.setItem(k, val);
          // 初始化创建的事件
          newStorageEvent.initStorageEvent('setItem', false, false, k, null, val, null, null);
          // 派发对象
          window.dispatchEvent(newStorageEvent)
        }
      }
      return storage.setItem(key, newVal);
    default:
      break
  }
}

// 进入页面前校验
router.beforeEach((to, from, next) => {
  // if (to.path == '/loginpage') {
  //   axios.post(sysLogout)
  //         .then(res=>{
  //          if(!res.data.code){
  //         this.$message.success(res.data.message);
  //            sessionStorage.removeItem('loginStatus')
  //         //  this.$router.push({path:'/login'});
  //         next('/login');
  //           }
  //        })
  //       }
  // } else {
  //   next()
  // }
  if (to.meta.title) document.title = to.meta.title
  else document.title = globalValue.protectTitle()
  if (to.path == '/login') {
    if (sessionStorage.getItem('loginStatus') == null) next()
    else next('/manggerCenter')
  } else {
    if (to.meta.mustLogin == true) {
      if (sessionStorage.getItem('loginStatus') == null) {
        axios
          .get(sysGetlogin)
          .then((res) => {
            if (res.data.data == null) {
              console.log(res.data.message)
              // router.push({path: '/login'})
              next('/login')
            } else {
              Vue.prototype.$loginStatus = res.data.data
              sessionStorage.setItem(
                'loginStatus',
                JSON.stringify(res.data.data)
              )
              console.log(Vue.prototype.$loginStatus)
              next()
            }
          })
          .catch(() => {
            next('/login')
          })
      } else {
        Vue.prototype.$loginStatus = JSON.parse(
          sessionStorage.getItem('loginStatus')
        )
        next()
      }
    } else next()
  }
})

Vue.config.productionTip = false

new Vue({
  router,
  render: (h) => h(App),
}).$mount('#app')
