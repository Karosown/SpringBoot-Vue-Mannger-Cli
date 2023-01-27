# XXXX管理系统

## Project setup
```
npm install
```

### Compiles and hot-reloads for development
```
npm run serve
```

### Compiles and minifies for production
```
npm run build
```

### Lints and fixes files
```
npm run lint
```
### 安装router
```
npm install vue-router@4
```

###
```
npm install axios
npm install @wangeditor/editor --save
npm install @wangeditor/editor-for-vue --save
npm install @wangeditor/plugin-md--save
npm install element-ui --save
npm install view-ui-plus 
npm install prismjs
```
### Customize configuration
See [Configuration Reference](https://cli.vuejs.org/config/).

```text
├─assets                #静态资源
├─components            #界面组件
│  ├─articleComponents      #文章组件
│  └─Editor                 #编辑框组件
├─config                    #配置
│  ├─ApiConfig                  #API配置
│  │  ├─articleApiConfig            #文章API
│  │  ├─commonApiConfig             #站点设置API
│  │  ├─fileApiConfig               #文件API
│  │  └─userApiConfig               #用户API
│  ├─CommonConfig               #全局配置、框架配置等
│  └─varConfig                  #一些简单的JSON对象
│      └─userVarConfig
├─entity                 #实体类（建议以文件名命名类）
│  ├─article                #文章实体类 增、查
│  ├─common                 #常用的实体类
│  └─vo                  #视图层用实体类，一般情况下Query的可以直接使用，如果需要做些处理
|						 #可以处理后使用Vo层传出
│      └─article            #文章Vo层
├─router                 #路由
└─view                   #界面
    ├─article-main			#文章界面
    └─manngger-main			#后台管理界面
```