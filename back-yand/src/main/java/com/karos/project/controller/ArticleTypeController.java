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
import cn.katool.dateutil.expDateUtil;
import cn.katool.iputils.IpUtils;
import cn.katool.lock.LockUtil;
import cn.katool.qiniu.impl.QiniuServiceImpl;
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
import com.karos.project.model.entity.Article;
import com.karos.project.model.entity.Articlehistory;
import com.karos.project.model.entity.Articlethumbrecords;
import com.karos.project.model.entity.User;
import com.karos.project.model.vo.article.ArticleTypeVo;
import com.karos.project.model.vo.article.ArticleVo;
import com.karos.project.service.*;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
    QiniuServiceImpl qnsi;
    @Resource
    RedisTemplate redisTemplate;
    @Resource
    private UserService userService;

    @Resource
    private ArticleTypeService articleTypeService;

    @GetMapping("/get")
    @ApiOperationSupport(author = "Karos")
    @ApiOperation(value = "文章分类列表获取接口")
    public BaseResponse<List<ArticleTypeVo>> getTypeList(){
        return ResultUtils.success(articleTypeService.allList());
    }

    // endregion

}