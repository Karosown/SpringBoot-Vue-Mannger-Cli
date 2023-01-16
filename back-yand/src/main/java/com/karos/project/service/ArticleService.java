package com.karos.project.service;

import com.karos.project.common.RegexValid;
import com.karos.project.model.dto.article.ArticleAddRequest;
import com.karos.project.model.dto.article.ArticleUpdateRequest;
import com.karos.project.model.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.commons.lang3.RegExUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author 30398
* @description 针对表【article(文章表)】的数据库操作Service
* @createDate 2022-12-27 03:48:26
*/
public interface ArticleService extends IService<Article> {
    /**
     * 校验
     *
     * @param article
     * @param add 是否为创建校验
     */
    void validArticle(Article article, boolean add);

    void validArticle(ArticleAddRequest article, boolean add);

    default String getIntroduction(String data){
        data=data.replaceAll(RegexValid.scriptRegex, "");
        data=data.replaceAll(RegexValid.styleRegex,"");
        data=data.replaceAll(RegexValid.htmlRegex,"");
        data=data.replaceAll(RegexValid.spaceRegex," ");

        if (data.length()>200){
            data=data.substring(0,200);
        }
        return data+"...";
    }

    default String getIntroduction(ArticleAddRequest articleAddRequest){
        return getIntroduction(articleAddRequest.getArticleText());
    }
    default String getIntroduction(ArticleUpdateRequest updateRequest){
        return getIntroduction(updateRequest.getArticleText());
    }

}
