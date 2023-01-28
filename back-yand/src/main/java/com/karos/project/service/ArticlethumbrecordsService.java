package com.karos.project.service;

import cn.katool.Exception.KaToolException;
import com.karos.project.model.entity.Articlethumbrecords;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author 30398
* @description 针对表【articlethumbrecords(文章点赞记录表)】的数据库操作Service
* @createDate 2023-01-05 04:22:14
*/
public interface ArticlethumbrecordsService extends IService<Articlethumbrecords> {

    Boolean thumb(Articlethumbrecords entity, HttpServletRequest request) throws KaToolException;
}
