/**
 * Title
 *
 * @ClassName: ArticletypeAddRequestBody
 * @Description:
 * @author: 巫宗霖
 * @date: 2023/2/4 17:22
 * @Blog: https://www.wzl1.top/
 */

package com.karos.project.model.dto.articletype;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleTypeAddRequestBody {
    private Integer id;
    private Integer fid;
    private String typeName;
    private static final long serialVersionUID = 1L;
}
