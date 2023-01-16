package com.karos.project.service;

import com.karos.project.model.entity.Article;
import com.karos.project.model.entity.Articlehistory;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 30398
* @description 针对表【articlehistory(文章历史)】的数据库操作Service
* @createDate 2022-12-27 18:39:39
*/
public interface ArticlehistoryService extends IService<Articlehistory> {
    //Todo：文章更新、版本记录
    void validArticle(Articlehistory articlehistory, boolean add);
}
