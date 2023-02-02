const base='/article';
//点赞
const doThumbArticle  =base+"/thumb"
//删除日记API POST
const deleteArticle    =base+"/delete"
//更新日记API post
const updateArticles   =base+"/update"
const getArticleByid=base+"/get?id="
const getArticleslistByFavorite=base+"/list/myfavorite"
const getArticleslistPage=base+"/list"
const getArticleslistByArticleTitle=base+"/list/common?sortField=createTime&articleTitle=";
//获取某个用户笔记列表
const getArticleslistByuserid=base+"/list/common?sortField=createTime&userId=";
//新增日记api post
const addArticles=base+"/add"
const getTypelist=base+"/get/typelist";
export {
    getTypelist,
    doThumbArticle,
    deleteArticle,
    getArticleslistByFavorite,
    getArticleslistPage,
    getArticleslistByArticleTitle,
    getArticleslistByuserid,
    getArticleByid,
    updateArticles,
    addArticles,
}