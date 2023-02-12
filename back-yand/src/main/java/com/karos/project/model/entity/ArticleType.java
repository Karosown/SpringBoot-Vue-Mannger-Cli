package com.karos.project.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName articletype
 */
@TableName(value ="articletype")
@Data
public class ArticleType implements Serializable {
    /**
     * 'ID'
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * '父类型'
     */
    private Integer fid;

    /**
     * 类型名
     */
    private String typeName;

    @TableLogic
    private Integer isDelete;
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}