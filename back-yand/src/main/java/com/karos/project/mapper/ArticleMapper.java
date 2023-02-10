package com.karos.project.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.karos.project.model.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.karos.project.model.vo.article.ArticleVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
* @author 30398
* @description 针对表【article(文章表)】的数据库操作Mapper
* @createDate 2022-12-27 03:48:26
* @Entity com.karos.project.model.entity.Article
*/
public interface ArticleMapper extends BaseMapper<Article> {

//    @Select("select id,userId,userArticleid,articleUrl,type,labelList,isPublist,viewNum,thumbNum,createTime,updateTime,hasThumb " +
//            "From article left join articletype")
//    List<ArticleVo> getVos();

    @Select("select " +
            "article.id,userId,userArticleid,articleTitle,articleUrl,articleIntroduction,articletype.typeName as 'type',type as 'typeId',labelList,isPublic,viewNum,thumbNum,createTime,updateTime,featImg,schedId\n" +
            "from article left join articletype on article.type=articletype.id where isDelete = 0")
    List<ArticleVo> VoList();

    @Select("select " +
            "article.id,userId,userArticleid,articleTitle,articleUrl,articleIntroduction,articletype.typeName as 'type',type as 'typeId',labelList,isPublic,viewNum,thumbNum,createTime,updateTime,featImg,schedId\n" +
            "from article left join articletype on article.type=articletype.id where isDelete = 0")
    Page<ArticleVo> VoPage(Page<ArticleVo> articleVoPage);

    @Select("select " +
            "article.id,userId,userArticleid,articleTitle,articleUrl,articleIntroduction,articletype.typeName as 'type',type as 'typeId',labelList,isPublic,viewNum,thumbNum,createTime,updateTime,featImg,schedId\n" +
            "from article left join articletype on article.type=articletype.id where isDelete = 0 and userId=#{userId}")
    Page<ArticleVo> VoPagebyUser(@Param("userId") String userid,Page<ArticleVo> articleVoPage);

    @Select("select " +
            "article.id,userId,userArticleid,articleTitle,articleUrl,articleIntroduction,articletype.typeName as 'type',type as 'typeId',labelList,isPublic,viewNum,thumbNum,createTime,updateTime,featImg,schedId\n" +
            "from article left join articletype on article.type=articletype.id where isDelete = 1")
    Page<ArticleVo> GarbageVoPage(Page<ArticleVo> articleVoPage);

    @Select("select * from article where id=#{id}")
    Article ingoreGetOneByID(@Param("id") String id);
    @Update("update article set isDelete=0 where id = #{id}")
    boolean recoveryById(@Param("id") String id);


    boolean recoveryByList(@Param("ids") List<String> idList);

    @Select("select " +
            "article.id,userId,userArticleid,articleTitle,articleUrl,articleIntroduction,articletype.typeName as 'type',type as 'typeId',labelList,isPublic,viewNum,thumbNum,createTime,updateTime,featImg,schedId\n" +
            "from article left join articletype on article.type=articletype.id where isDelete = 0 and isPublic=1")
    Page<ArticleVo> VoPageByGuest(Page<ArticleVo> articlePage);
}





