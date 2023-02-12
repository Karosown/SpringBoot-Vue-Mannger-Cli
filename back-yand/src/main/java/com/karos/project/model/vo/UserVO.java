package com.karos.project.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户视图
 *
 * @TableName user
 */
@Data
public class UserVO  extends BaseRowModel implements Serializable {
    /**
     * id
     */
    @ExcelProperty(value = "id",index = 0)
    private Long  id;

    /**
     * 用户昵称
     */
    @ExcelProperty(value = "用户昵称",index = 1)
    private String userName;

    /**
     * 账号
     */
    @ExcelProperty(value = "用户账号",index = 2)
    private String userAccount;

    /**
     * 用户头像
     */
    @ExcelProperty(value = "用户头像地址",index = 3)
    private String userAvatar;

    /**
     * 性别
     */
    @ExcelProperty(value = "性别",index = 4)
    private Integer gender;

    /**
     * 用户角色: user, admin
     */
    @ExcelProperty(value = "角色",index = 5)
    private String userRole;
    /**
     * 用户邮箱
     */
    @ExcelProperty(value = "用户邮箱",index = 6)
    private String userMail;
    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建日期",index = 7)
    private Date createTime;

    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新日期",index = 8)
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}