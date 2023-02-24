package com.karos.project.service;

import com.karos.project.model.entity.ArticleType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.karos.project.model.vo.article.ArticleTypeVo;

import java.util.List;

/**
* @author 30398
* @description 针对表【articletype】的数据库操作Service
* @createDate 2023-02-01 03:24:40
*/
public interface ArticleTypeService extends IService<ArticleType> {
    public List<ArticleTypeVo> allList();

    public Integer getMaxId();
}
