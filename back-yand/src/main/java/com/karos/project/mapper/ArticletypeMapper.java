package com.karos.project.mapper;

import com.karos.project.model.entity.ArticleType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
* @author 30398
* @description 针对表【articletype】的数据库操作Mapper
* @createDate 2023-02-01 03:24:40
* @Entity com.karos.project.model.entity.ArticleType
*/
public interface ArticletypeMapper extends BaseMapper<ArticleType> {

    @Select("select max(id) from articletype")
    public Integer getMaxId();
}




