package com.karos.project.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 普通配置表
 * @TableName common
 */
@TableName(value ="common")
@Data
@EqualsAndHashCode
public class Common implements Serializable {
    /**
     * 属性
     */
    @TableId
    @ApiModelProperty("属性名")
    private String attribute;

    /**
     * 值
     */
    @ApiModelProperty("属性值")
    private String value;

    @ApiModelProperty("属性备注")
    private String comment;

    @ApiModelProperty("属性类型")
    private boolean type;
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}