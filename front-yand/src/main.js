import Vue from 'vue'
import App from './App.vue'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';

Vue.use(ElementUI);
// 引入axios
import axios from 'axios';
import {baseAPI, protectTitle, sysGetlogin} from "@/config/apiconfig";
import router from "@/router";
import {globalValue} from "@/config/varconfig";
// 挂载到vue原型链上
axios.defaults.baseURL=baseAPI;
axios.defaults.crossDomain = true;
axios.defaults.withCredentials = true;  //设置cross跨域 并设置访问权限 允许跨域携带cookie信息
Vue.prototype.axios=axios;
Vue.prototype.globalValue=globalValue;
Vue.config.productionTip = false;

Vue.prototype.resetSetItem = function (key, newVal) {
  if (key === 'articlelist') {
    // 创建一个StorageEvent事件
    var newStorageEvent = document.createEvent('StorageEvent');
    const storage = {
      setItem: function (k, val) {
        sessionStorage.setItem(k, val);
        // 初始化创建的事件
        newStorageEvent.initStorageEvent('setItem', false, false, k, null, val, null, null);
        // 派发对象
        window.dispatchEvent(newStorageEvent)
      }
    }
    return storage.setItem(key, newVal);
  }
}


router.beforeEach((to,from,next)=>{
  if (to.meta.title) document.title=to.meta.title
  else document.title=protectTitle;
  if (to.path=='/login'){
    if (sessionStorage.getItem('loginStatus')==null)  next();
    else next('/manggerCenter')
  }
  else{
    if(to.meta.mustLogin==true){
      if (sessionStorage.getItem('loginStatus')==null){
        axios.get(sysGetlogin)
            .then(res =>{
              if (res.data.data==null) {
                console.log(res.data.message)
                // router.push({path: '/login'})
                next('/login')
              }
              else{
                Vue.prototype.$loginStatus=res.data.data
                sessionStorage.setItem('loginStatus',JSON.stringify(res.data.data))
                console.log(Vue.prototype.$loginStatus);
                next();
              }
            })
            .catch(()=>{
              next('/login');
            })
      }
      else{
        Vue.prototype.$loginStatus=JSON.parse(sessionStorage.getItem('loginStatus'))
        next();
      }
    }
    else next();
  }
})

Vue.config.productionTip = false

new Vue({
  router,
  render: h => h(App),
}).$mount('#app')
