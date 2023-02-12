/**
 * Title
 *
 * @ClassName: UserExportRequest
 * @Description:
 * @author: 巫宗霖
 * @date: 2023/2/11 13:53
 * @Blog: https://www.wzl1.top/
 */

package com.karos.project.model.dto.user;

import com.karos.project.model.vo.UserVO;
import com.karos.project.model.vo.article.ArticleVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserExportRequest {
    private List<UserVO> userVoList;
}
