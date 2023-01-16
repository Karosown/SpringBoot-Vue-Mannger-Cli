package com.karos.project.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 文章历史
 * @TableName articlehistory
 */
@TableName(value ="articlehistory")
@Data
public class Articlehistory implements Serializable {
    /**
     * 文章id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 文章地址
     */
    private String articleUrl;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 文章
     */
    private Long version;
    /**
     * 更新时间
     */
    private Date updateTime;


    /**
     * 是否删除（0否，1是）
     */
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}