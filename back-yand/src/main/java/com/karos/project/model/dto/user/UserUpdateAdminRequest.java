package com.karos.project.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户创建请求
 *
 * @author karos
 */
@Data
public class UserUpdateAdminRequest implements Serializable {

    private long id;
    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 用户角色: user, admin
     */
    private String userRole;
    private String userMail;
    /**
     * 密码
     */
    private String userPassword;

    private String checkPassword;

    private static final long serialVersionUID = 1L;
}