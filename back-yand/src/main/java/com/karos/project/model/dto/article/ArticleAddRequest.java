package com.karos.project.model.dto.article;

import cn.hutool.json.JSON;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 创建请求
 *
 * @TableName product
 */
@Data
public class ArticleAddRequest implements Serializable {


    /**
     * 所属用户ID
     */
    private Long userId;

    /**
     * 文章标题
     */
    private String articleTitle;
    /**
     * 文章内容
     */
    private String articleText;
    /**
     * 类型
     */
    private int type;
    private String labelList;
    /**
     * 文章简介
     */
    private String articleIntroduction;
    /**
     * 是否公开（0为否，1为公开）
     */
    private Integer isPublic;

    /**
     * 定时发布
     */
    private Date publishTime;
    /**
     * 特色图片
     */
    private String featImg;
    /**
     * IP地址
     */
    private String IP;



    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}