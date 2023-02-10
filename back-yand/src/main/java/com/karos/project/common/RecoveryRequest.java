/**
 * Title
 *
 * @ClassName: RecoveryRequest
 * @Description:
 * @author: 巫宗霖
 * @date: 2023/2/9 5:42
 * @Blog: https://www.wzl1.top/
 */

package com.karos.project.common;

import lombok.Data;

import java.util.List;

@Data
public class RecoveryRequest {
    /**
     * id
     */
    private String id;
    private List<String> ids;
    private static final long serialVersionUID = 1L;
}
