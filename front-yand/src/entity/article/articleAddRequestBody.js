export class ArticleAddRequest{


    /**
     * 所属用户ID
     */
    userId;

    /**
     * 文章标题
     */
    articleTitle;
    /**
     * 文章内容
     */
    articleText;
    /**
     * 类型
     */
    type;
    /**
     * 标签
     */
    labelList;

    /**
     * 是否公开（0为否，1为公开）
     */
   isPublic;


    /**
     * IP地址
     */
    IP;

}