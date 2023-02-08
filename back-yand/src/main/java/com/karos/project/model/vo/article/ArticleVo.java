package com.karos.project.model.vo.article;

import cn.hutool.json.JSON;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.baomidou.mybatisplus.annotation.*;
import com.karos.project.model.entity.Article;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 文章表
 * @TableName article
 */
@Data
public class ArticleVo  extends BaseRowModel implements Serializable {
    /**
     * id
     */
    @ExcelProperty(value = "文章ID",index = 0)
    private String id;

    /**
     * 所属用户ID
     */
    @ExcelProperty(value = "用户ID",index = 1)
    private Long userId;

    /**
     * 用户文章ID
     */
    @ExcelProperty(value = "文章ID by user",index = 2)
    private Long userArticleid;
    /**
     * 文章标题
     */
    @ExcelProperty(value = "文章标题",index = 3)
    private String articleTitle;
    /**
     * 文章地址
     */
    @ExcelProperty(value = "文章OSS地址",index = 4)
    private String articleUrl;
    /**
     * 文章简介
     */
    @ExcelProperty(value = "文章简介",index = 5)
    private String articleIntroduction;
    @ExcelProperty(value = "文章类型",index = 6)
    private String type;
    @ExcelProperty(value = "文章类型Id",index = 6)
    private Integer typeId;
    @ExcelProperty(value = "文章标签",index = 7)
    private String labelList;

    /**
     * 是否公开（0为否，1为公开）
     */
    @ExcelProperty(value = "是否公开",index = 8)
    private Integer isPublic;

    /**
     * 浏览量
     */
    @ExcelProperty(value = "访问量",index = 9)
    private Long viewNum;


    /**
     * 点赞量
     */
    @ExcelProperty(value = "点赞数量",index = 10)
    private Long thumbNum;

    /**
     * 注册时间
     */
    @ExcelProperty(value = "创建时间",index = 11)
    private Date createTime;

    /**
     * 更新时间
     */
    @ExcelProperty(value = "最后一次修改时间",index = 12)
    private Date updateTime;

    private Date publishTime;
    /**
     * 是否已点赞
     */
    private Boolean hasThumb;
    /**
     * 特色图片
     */
    @ExcelProperty(value = "特色图片地址",index = 13)
    private String featImg;
    /**
     * 定时任务ID
     */
    private String schedId;
    private static final long serialVersionUID = 1L;
}