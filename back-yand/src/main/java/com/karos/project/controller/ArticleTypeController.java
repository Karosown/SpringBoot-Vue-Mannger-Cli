/**
 * Title
 *
 * @ClassName: ArticleTypeController
 * @Description:
 * @author: Karos
 * @date: 2022/12/28 1:58
 * @Blog: https://www.wzl1.top/
 */

package com.karos.project.controller;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.crypto.digest.DigestUtil;
import cn.katool.Exception.KaToolException;
import cn.katool.util.expDateUtil;
import cn.katool.iputils.IpUtils;
import cn.katool.lock.LockUtil;
import cn.katool.qiniu.impl.QiniuServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.karos.project.annotation.AuthCheck;
import com.karos.project.common.BaseResponse;
import com.karos.project.common.DeleteRequest;
import com.karos.project.common.ErrorCode;
import com.karos.project.common.ResultUtils;
import com.karos.project.constant.CommonConstant;
import com.karos.project.constant.RedisKeysConstant;
import com.karos.project.exception.BusinessException;
import com.karos.project.model.dto.article.ArticleAddRequest;
import com.karos.project.model.dto.article.ArticleDoThumbRequest;
import com.karos.project.model.dto.article.ArticleQueryRequest;
import com.karos.project.model.dto.article.ArticleUpdateRequest;
import com.karos.project.model.dto.articletype.ArticleTypeAddRequestBody;
import com.karos.project.model.entity.*;
import com.karos.project.model.vo.article.ArticleTypeVo;
import com.karos.project.model.vo.article.ArticleVo;
import com.karos.project.service.*;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/article/type")
@Slf4j
public class ArticleTypeController {

    @Resource
    private ArticleTypeService articleTypeService;

    @GetMapping("/get")
    @ApiOperationSupport(author = "Karos")
    @ApiOperation(value = "文章分类列表获取接口")
    public BaseResponse<List<ArticleTypeVo>> getTypeList(){
        return ResultUtils.success(articleTypeService.allList());
    }

    @AuthCheck(mustRole = "admin")
    @PostMapping("/add")
    public BaseResponse<Boolean> addType(@RequestBody ArticleTypeAddRequestBody articleTypeAddRequestBody){
        if (ObjectUtils.isEmpty(articleTypeAddRequestBody)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (ObjectUtils.isNotEmpty(articleTypeAddRequestBody.getFid())
                &&ObjectUtils.isEmpty(articleTypeService.getById(articleTypeAddRequestBody.getFid()))
                &&articleTypeAddRequestBody.getFid()!=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ArticleType articleType=new ArticleType();
        BeanUtils.copyProperties(articleTypeAddRequestBody,articleType);
        boolean save = articleTypeService.save(articleType);
        return ResultUtils.success(save);
    }
    private Integer[] typeIds=null;

    private Integer find(int x){
        if (typeIds[x]==x)return x;
        return typeIds[x]=find(typeIds[x]);
    }
    private Boolean merge(int x,int y){
        int _x=find(x);
        int _y=find(y);
        if (_x==_y)return true;
        typeIds[_y]=_x;
        return true;
    }

    private Boolean isChild(int f,int c){
        int _c=find(c);
        return f==_c;
    }
    @AuthCheck(mustRole = "admin")
    @PostMapping("/delete")
    public BaseResponse<Boolean> delete(@RequestBody DeleteRequest deleteRequest){
        String id = deleteRequest.getId();
        //无法解决子子分类的问题
//        LambdaQueryWrapper<ArticleType> queryWrapper=new LambdaQueryWrapper<>();
//
//        queryWrapper.eq(id!=null,ArticleType::getId,id)
//                .or()
//                        .eq(id!=null,ArticleType::getFid,id);
//        boolean remove = articleTypeService.remove(queryWrapper);
        List<ArticleType> list = articleTypeService.list();
        typeIds=new Integer[articleTypeService.getMaxId()+10];
        for (int i=0;i<typeIds.length;i++) typeIds[i]=i;
        Integer idInteger = Integer.parseInt(id);
        for(ArticleType type:list){
            if (type.getId()== idInteger){
                continue;
            }
            merge(type.getFid(),type.getId());
        }
        List<ArticleType> dlist=new ArrayList<>();
        for(ArticleType type:list){
            if (typeIds[type.getId()]== idInteger ||isChild(idInteger,type.getId())){
                dlist.add(type);
            }
        }
        boolean remove = articleTypeService.removeByIds(dlist);
        if (!remove){
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(remove);
    }
    // endregion

}
