package com.karos.project.model.vo;

import com.baomidou.mybatisplus.annotation.*;
import com.karos.project.model.entity.Article;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 文章表
 * @TableName article
 */
@Data
public class ArticleVo implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 所属用户ID
     */
    private Long userId;

    /**
     * 用户文章ID
     */
    private Long userArticleid;
    /**
     * 文章标题
     */
    private String articleTitle;
    /**
     * 文章地址
     */
    private String articleUrl;
    /**
     * 文章简介
     */
    private String articleIntroduction;
    private String type;
    /**
     * 是否公开（0为否，1为公开）
     */
    private Integer isPublic;

    /**
     * 浏览量
     */
    private Long viewNum;


    /**
     * 点赞量
     */
    private Long thumbNum;

    /**
     * 注册时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date upDateTime;

    /**
     * 是否已点赞
     */
    private Boolean hasThumb;

    private static final long serialVersionUID = 1L;
}