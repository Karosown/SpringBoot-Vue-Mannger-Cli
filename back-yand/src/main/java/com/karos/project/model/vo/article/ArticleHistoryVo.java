package com.karos.project.model.vo.article;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 笔记历史
 * @TableName articlehistory
 */
@TableName(value ="articlehistory")
@Data
public class ArticleHistoryVo implements Serializable {
    /**
     * 日记id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 日记地址
     */
    private String articleUrl;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 日记
     */
    private Long version;
    /**
     * 修改用户
     */
    private Long userId;
    /**
     * 更新时间
     */
    private Date updateTime;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}