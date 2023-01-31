/**
 * Title
 *
 * @ClassName: ArticleTypeVo
 * @Description:
 * @author: 巫宗霖
 * @date: 2023/2/1 3:26
 * @Blog: https://www.wzl1.top/
 */

package com.karos.project.model.vo.article;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.karos.project.model.entity.ArticleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleTypeVo {
    @TableId(type = IdType.AUTO)
    @JsonProperty("value")
    private Integer id;

//    /**
//     * '父类型'
//     */
//    private Integer fid;

    /**
     * 类型名
     */
    @JsonProperty("label")
    private String typeName;
    private List<ArticleTypeVo> children;
}
