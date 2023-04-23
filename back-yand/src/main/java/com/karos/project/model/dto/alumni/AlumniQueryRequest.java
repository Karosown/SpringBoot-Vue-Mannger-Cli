/**
 * Title
 *
 * @ClassName: AlumniQueryRequest
 * @Description:
 * @author: 巫宗霖
 * @date: 2023/4/5 18:41
 * @Blog: https://www.wzl1.top/
 */

package com.karos.project.model.dto.alumni;

import com.karos.project.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class AlumniQueryRequest extends PageRequest implements Serializable{

        private Long id;


        private String userId;


        private String userName;

        private Integer gender;

        private String phoneNumber;

        private String userEmail;


        private String admissionYear;


        private String major;


        private String currentEmployer;


        private String currentLocation;


        private Integer canAttendEvent;


        private String message;


        private Date createTime;


        private Date updateTime;

        private Boolean isPass;

        private static final long serialVersionUID = 1L;
}
