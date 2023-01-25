export class PageRequestBody{
    /**
     * 当前页号
     */
    current = 1;

    /**
     * 页面大小
     */
    pageSize = 10;

    /**
     * 排序字段
     */
    sortField='createTime';


    sortOrder = null;
}

