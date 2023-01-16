package com.karos.project.service.impl;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.karos.project.common.ErrorCode;
import com.karos.project.exception.BusinessException;
import com.karos.project.model.dto.article.ArticleAddRequest;
import com.karos.project.model.entity.Article;
import com.karos.project.model.entity.Post;
import com.karos.project.model.enums.PostGenderEnum;
import com.karos.project.model.enums.PostReviewStatusEnum;
import com.karos.project.service.ArticleService;
import com.karos.project.mapper.ArticleMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
* @author 30398
* @description 针对表【article(文章表)】的数据库操作Service实现
* @createDate 2022-12-27 03:48:26
*/
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
    implements ArticleService{
    @Resource
    private ArticleMapper articleMapper;
    @Override
    public void validArticle(Article article, boolean add) {

        if (article == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String id = article.getId();
        Long userId = article.getUserId();
        Long userArticleid = article.getUserArticleid();
        String articleTitle = article.getArticleTitle();
        String articleUrl = article.getArticleUrl();
        Integer isPublic = article.getIsPublic();
//        ip可以为空，再controller中进行获取
//        String iP = article.getIP();
        // 创建时，所有参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(articleTitle,articleUrl) || ObjectUtils.anyNull(userId,isPublic)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            if (ObjectUtils.anyNull(userArticleid)){
                QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("userId",userId);
                article.setUserArticleid(this.count(queryWrapper)+1);
            }
        }
    }

    @Override
    public void validArticle(ArticleAddRequest article, boolean add) {

        if (article == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long userId = article.getUserId();
        String articleTitle = article.getArticleTitle();
        String articleText = article.getArticleText();
        Integer isPublic = article.getIsPublic();
        String iP = article.getIP();

        // 创建时，所有参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(articleTitle,articleText,iP) || ObjectUtils.anyNull(userId,isPublic)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
    }



}




