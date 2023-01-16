package com.karos.project.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 文章点赞记录表
 * @TableName articlethumbrecords
 */
@TableName(value ="articlethumbrecords")
@Data
public class Articlethumbrecords implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 文章ID
     */
    private String articleId;

    /**
     * 点赞时间
     */
    private Date thumbTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Articlethumbrecords that = (Articlethumbrecords) o;
        return userId.equals(that.userId) && articleId.equals(that.articleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, articleId);
    }
}