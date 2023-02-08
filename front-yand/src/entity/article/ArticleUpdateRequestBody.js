export class ArticleUpdateRequest{
    /**
     * id
     */
    id;

    /**
     * 所属用户ID
     */
    userId;

    /**
     * 用户文章ID
     */
    userArticleid;
    /**
     * 文章标题
     */
    articleTitle;
    /**
     * 文章内容
     */
    articleText;
    articleIntroduction;
    /**
     * 是否公开（0为否，1为公开）
     */
    isPublic;
    publishTime;
    type;

    labelList;
    /**
     * 特色图片
     */
    featImg;
    /**
     * 定时任务ID
     */
    schedId;
}