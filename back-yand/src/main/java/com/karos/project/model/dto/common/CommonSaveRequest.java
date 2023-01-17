/**
 * Title
 *
 * @ClassName: CommonSaveRequest
 * @Description:
 * @author: 巫宗霖
 * @date: 2023/1/17 9:05
 * @Blog: https://www.wzl1.top/
 */

package com.karos.project.model.dto.common;

import com.karos.project.model.entity.Common;
import lombok.Data;

import java.util.List;

@Data
public class CommonSaveRequest {
    List<Common> commonList;
}
