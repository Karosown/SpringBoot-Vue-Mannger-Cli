package com.karos.project.model.entity;

import cn.hutool.json.JSON;
import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

/**
 * 文章表
 * @TableName article
 */
@TableName(value ="article")
@Data
public class Article implements Serializable {
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
    private int type;

    private String labelList;
    /**
     * 是否公开（0为否，1为公开）
     */
    private Integer isPublic;
    /**
     * 浏览量
     */
    private Long viewNum;

    /**
     * IP地址
     */
    private String IP;

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
    private Date updateTime;
    /**
     * 特色图片
     */
    private String featImg;
    /**
     * 是否删除（0否，1是）
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}