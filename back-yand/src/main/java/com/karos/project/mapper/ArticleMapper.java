package com.karos.project.mapper;

import com.karos.project.model.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.karos.project.model.vo.article.ArticleVo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 30398
* @description 针对表【article(文章表)】的数据库操作Mapper
* @createDate 2022-12-27 03:48:26
* @Entity com.karos.project.model.entity.Article
*/
public interface ArticleMapper extends BaseMapper<Article> {

    @Select("select id,userId,userArticleid,articleUrl,type,labelList,isPublist,viewNum,thumbNum,createTime,upDateTime,hasThumb " +
            "From article left join articletype")
    List<ArticleVo> getVos();
}





