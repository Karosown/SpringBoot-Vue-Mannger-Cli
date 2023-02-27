package com.karos.project.model.dto.article;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.karos.project.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 查询请求
 *
 * @author karos
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleQueryRequest extends PageRequest implements Serializable {


    /**
     * 所属用户ID
     */
    private Long userId;

    /**
     * 文章标题
     */
    private String articleTitle;
    private List<String> labelList;

    /**
     * 类型
     */
    private int type;
    private String typeName;
    /**
     * 类型名
     */
    private static final long serialVersionUID = 1L;
}