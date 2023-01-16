package com.karos.project.model.dto.articlehistory;

import com.karos.project.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *
 * @author karos
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleHistoryQueryRequest extends PageRequest implements Serializable {

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
    private static final long serialVersionUID = 1L;
}