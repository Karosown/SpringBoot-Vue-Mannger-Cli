package com.karos.project.model.dto.article;

import lombok.Data;

import java.io.Serializable;

/**
 * 点赞 / 取消点赞请求
 *
 * @author karos
 */
@Data
public class ArticleDoThumbRequest implements Serializable {

    /**
     * 文章 id
     */
    private String articleId;

    private static final long serialVersionUID = 1L;
}