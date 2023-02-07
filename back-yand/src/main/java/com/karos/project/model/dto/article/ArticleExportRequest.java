/**
 * Title
 *
 * @ClassName: ArticleExportRequest
 * @Description:
 * @author: 巫宗霖
 * @date: 2023/2/7 2:42
 * @Blog: https://www.wzl1.top/
 */

package com.karos.project.model.dto.article;

import com.alibaba.excel.metadata.BaseRowModel;
import com.karos.project.model.vo.article.ArticleVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleExportRequest{
    private List<ArticleVo> articleVoList;
}
