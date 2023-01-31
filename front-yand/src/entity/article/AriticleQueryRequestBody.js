import {PageRequestBody} from "@/entity/common/pageRequestBody";


export class articleQureyRequestBody extends PageRequestBody{
    userId=null

    /**
     * 文章标题
     */
    articleTitle=null
    /**
     * 类型
     */
    type=0
}
