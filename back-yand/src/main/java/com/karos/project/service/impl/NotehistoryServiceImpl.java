package com.karos.project.service.impl;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.karos.project.common.ErrorCode;
import com.karos.project.exception.BusinessException;
import com.karos.project.model.entity.Articlehistory;
import com.karos.project.service.ArticlehistoryService;
import com.karos.project.mapper.ArticlehistoryMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* @author 30398
* @description 针对表【articlehistory(文章历史)】的数据库操作Service实现
* @createDate 2022-12-27 18:39:39
*/
@Service
public class ArticlehistoryServiceImpl extends ServiceImpl<ArticlehistoryMapper, Articlehistory>
    implements ArticlehistoryService {

    @Override
    public void validArticle(Articlehistory articlehistory, boolean add) {
        if (articlehistory==null)
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
     String id = articlehistory.getId();
     String articleUrl = articlehistory.getArticleUrl();
     String ip = articlehistory.getIp();
     Long version = articlehistory.getVersion();
        if (add){
            if (StringUtils.isAnyBlank(id,articleUrl,ip)){
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            if (ObjectUtils.anyNull(version)){
                QueryWrapper<Articlehistory> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("id",id);
                version=this.count(queryWrapper)+1;
            }
        }

    }
}




