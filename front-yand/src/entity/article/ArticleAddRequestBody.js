export class ArticleAddRequest{


    /**
     * 所属用户ID
     */
    userId=null;

    /**
     * 文章标题
     */
    articleTitle=null;
    /**
     * 文章内容
     */
    articleText=null;
    /**
     * 类型
     */
    type=0;
    /**
     * 标签
     */
    labelList=[];

    /**
     * 是否公开（0为否，1为公开）
     */
    isPublic=0;

    publishTime=null;

    

    constructor() {
    }
}