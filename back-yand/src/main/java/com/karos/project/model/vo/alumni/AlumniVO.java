/**
 * Title
 *
 * @ClassName: AlumniVO
 * @Description:
 * @author: 巫宗霖
 * @date: 2023/4/5 18:44
 * @Blog: https://www.wzl1.top/
 */

package com.karos.project.model.vo.alumni;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.karos.project.model.entity.Alumni;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

public class AlumniVO extends Alumni {


    /**
     * 校友预约表
     * @TableName alumni
     */

        /**
         * 主键id
         */

        private Long id;

        /**
         * 用户id
         */

        private String userId;

        /**
         * 用户姓名
         */

        private String userName;

        /**
         * 性别：0女，1男
         */

        private Integer gender;

        /**
         * 电话号码
         */

        private String phoneNumber;

        /**
         * 用户邮箱
         */

        private String userEmail;

        /**
         * 入学年份
         */

        private String admissionYear;

        /**
         * 专业
         */
        private String major;

        /**
         * 当前工作或学习单位
         */
        private String currentEmployer;

        /**
         * 当前工作地点
         */
        private String currentLocation;

        /**
         * 是否当天（2023.04.28）能回学校参加活动：0否，1是
         */
        private Integer canAttendEvent;

        /**
         * 对母校留言
         */

        private String message;

        /**
         * 创建时间
         */
        private Date createTime;

        /**
         * 更新时间
         */

        private Date updateTime;

        /**
         * 是否通过
         */
        private Boolean isPass;

        private static final long serialVersionUID = 1L;

}
