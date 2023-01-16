package com.karos.project.model.dto.article;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 更新请求
 *
 * @TableName product
 */
@Data
public class ArticleUpdateRequest implements Serializable {
    /**
     * id
     */
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
     * 文章内容
     */
    private String articleText;
    /**
     * 是否公开（0为否，1为公开）
     */
    private Integer isPublic;
    private int type;


    private static final long serialVersionUID = 1L;
}