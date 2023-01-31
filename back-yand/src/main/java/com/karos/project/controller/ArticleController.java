/**
 * Title
 *
 * @ClassName: ArticleController
 * @Description:
 * @author: Karos
 * @date: 2022/12/28 1:58
 * @Blog: https://www.wzl1.top/
 */

package com.karos.project.controller;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.crypto.digest.DigestUtil;
import cn.katool.Exception.KaToolException;
import cn.katool.dateutil.expDateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import cn.katool.iputils.IpUtils;
import cn.katool.lock.LockUtil;
import cn.katool.qiniu.impl.QiniuServiceImpl;
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
import com.karos.project.model.vo.ArticleVo;
import com.karos.project.service.ArticleService;
import com.karos.project.service.ArticlehistoryService;
import com.karos.project.service.ArticlethumbrecordsService;
import com.karos.project.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/article")
@Slf4j
public class ArticleController {
    @Resource
    ArticleService articleService;
    @Resource
    QiniuServiceImpl qnsi;
    @Resource
    RedisTemplate redisTemplate;
    @Resource
    ArticlehistoryService articlehistoryService;
    @Resource
    private UserService userService;
    @Resource
    private ArticlethumbrecordsService articlethumbrecordsService;
    @Resource
    LockUtil lockUtil;

    @AuthCheck(mustRole = "admin")
    @GetMapping("/LockTest")
    public BaseResponse<String> test(@RequestParam("expTime") Long expTime){
        try {
            lockUtil.DistributedLock(RedisKeysConstant.ThumbsHistoryHash.intern(),expTime, TimeUnit.SECONDS);
        } catch (KaToolException e) {
            throw new BusinessException(e.getCode(),e.getMessage());
        }
        return ResultUtils.success("上锁成功，请在20s内进行测试操作");
    }
    @PostMapping("/thumb")
    @ApiOperationSupport(author = "Karos")
    @ApiOperation(value = "文章点赞接口")
    public BaseResponse<Boolean> thumbArticle(@RequestBody ArticleDoThumbRequest articleDoThumbRequest, HttpServletRequest request) throws KaToolException {
        Articlethumbrecords articlethumbrecords = new Articlethumbrecords();
        articlethumbrecords.setArticleId(articleDoThumbRequest.getArticleId());
        articlethumbrecords.setThumbTime(new Date());
        Boolean result = articlethumbrecordsService.thumb(articlethumbrecords, request);
        if (result==null){
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(result,()->{
            if (BooleanUtil.isTrue(result)) {
                return "已放入收藏夹";
            }
            if (BooleanUtil.isFalse(result)){
                return "已取消收藏";
            }
            return "收藏夹服务错误";
        });
    }
    /**
     * 创建
     *
     * @param articleAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @ApiOperationSupport(author = "Karos")
    @ApiOperation(value = "新增文章接口")
    public BaseResponse<String> addArticle(@RequestBody ArticleAddRequest articleAddRequest, HttpServletRequest request) {
        if (articleAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //使用工具类，Nginx反向代理后仍为真实IP
        articleAddRequest.setIP(IpUtils.getIpAddr(request));
        //讲文章内容存入七牛云
        String articleText = articleAddRequest.getArticleText();
        String articleUrl =null;
        try {
            File tempFile = File.createTempFile("temp",".html");
            FileOutputStream fos= new FileOutputStream(tempFile);
            //装饰者模式
            OutputStreamWriter osw=new OutputStreamWriter(fos,"utf-8");
            osw.write(articleText);
            osw.flush();
            osw.close();
            User loginUser = userService.getLoginUser(request);
            Long id = loginUser.getId();
            //防止文章重名，加上随机数
            long random = ((long)Math.random())%1000000+1;
            String fileName= DigestUtil.md5Hex(articleAddRequest.getArticleTitle()+ id+random);
            articleUrl=qnsi.uploadFile(tempFile, "articleFile", fileName, ".html", true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Article article = new Article();
        BeanUtils.copyProperties(articleAddRequest, article);
        // 校验
        article.setCreateTime(new Date());
        article.setArticleUrl(articleUrl);
        article.setArticleIntroduction(articleService.getIntroduction(articleAddRequest));
        String newArticleId=null;
        synchronized (article.getArticleUrl().intern()) {
            articleService.validArticle(article, true);
            User loginUser = userService.getLoginUser(request);
            article.setUserId(loginUser.getId());
            boolean result = articleService.save(article);
            if (!result) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR);
            }
            newArticleId = article.getId();
            //添加第一条历史记录
            Articlehistory articlehistory = new Articlehistory();
            articlehistory.setId(newArticleId);
            articlehistory.setArticleUrl(articleUrl);
            articlehistory.setIp(article.getIP());
            articlehistory.setVersion(1L);
            articlehistory.setUpdateTime(new Date());
            Date publishTime = articleAddRequest.getPublishTime();
            if (ObjectUtil.isNotEmpty(publishTime)){
                String corn =null;
                try {
                    corn=expDateUtil.getCorn(publishTime);
                } catch (KaToolException e) {
                    throw new BusinessException(e);
                }
                String finalNewArticleId = newArticleId;
                CronUtil.schedule(corn, new Task() {
                    @Override
                    public void execute() {
                        QueryWrapper<Article> queryWrapper=new QueryWrapper<>();
                        queryWrapper.eq("id", finalNewArticleId);
                        Article one = articleService.getOne(queryWrapper);
                        one.setIsPublic(1);
                        articleService.updateById(one);
                        log.info("定时文章ID={}发布添加成功,添加时间{},等待发布时间{}",finalNewArticleId,new Date(),publishTime);
                    }
                });
            }
            articlehistoryService.save(articlehistory);
        }
        return ResultUtils.success(newArticleId,"添加成功");
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @ApiOperationSupport(author = "Karos")
    @ApiOperation(value = "删除文章接口")
    public BaseResponse<Boolean> deleteArticle(@RequestBody DeleteRequest<String> deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || StringUtils.isAnyBlank(deleteRequest.getId())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        String id = deleteRequest.getId();
        // 判断是否存在
        Article oldArticle = articleService.getById(id);
        if (oldArticle == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        if (!oldArticle.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        //逻辑上删除
        boolean b = articleService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新
     *
     * @param articleUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @ApiOperationSupport(author = "Karos")
    @ApiOperation(value = "文章更新")
    public BaseResponse<Boolean> updateArticle(@RequestBody ArticleUpdateRequest articleUpdateRequest,
                                            HttpServletRequest request) {
        if (articleUpdateRequest == null || articleUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Article article = new Article();
        BeanUtils.copyProperties(articleUpdateRequest, article);
        String articleText = articleUpdateRequest.getArticleText();
        // 参数校验
        articleService.validArticle(article, false);
        User user = userService.getLoginUser(request);
        String id = articleUpdateRequest.getId();
        // 判断是否存在
        Article oldArticle = articleService.getById(id);
        if (oldArticle == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        if (!oldArticle.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        String oldarticleUrl = oldArticle.getArticleUrl();
        String originName = qnsi.getOriginName(oldarticleUrl);
        String articleUrl=null;
        try {
            File tempFile = File.createTempFile("temp",".html");
            FileOutputStream fos= new FileOutputStream(tempFile);
            //装饰者模式
            OutputStreamWriter osw=new OutputStreamWriter(fos,"utf-8");
            osw.write(articleText);
            osw.flush();
            osw.close();
            articleUrl=qnsi.uploadFile(tempFile, "articleFile", originName, true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
//        todo
        article.setArticleUrl(articleUrl);
        article.setArticleIntroduction(articleService.getIntroduction(articleUpdateRequest));
        Articlehistory articlehistory = new Articlehistory();
        articlehistory.setId(article.getId());
        articlehistory.setArticleUrl(articleUrl);
        articlehistory.setIp(IpUtils.getIpAddr(request));
        QueryWrapper<Articlehistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",article.getId());
        articlehistory.setVersion(articlehistoryService.count(queryWrapper)+1);
        articlehistoryService.validArticle(articlehistory,true);
        article.setUserId(null);
        boolean result = articleService.updateById(article)&&articlehistoryService.save(articlehistory);
        log.info("用户{} 身份{} 修改帖子ID{}-{}",user.getUserAccount(),user.getUserRole(),article.getId(),article.getArticleTitle());
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    @ApiOperationSupport(author = "Karos")
    @ApiOperation(value = "通过文章ID获得文章")
    public BaseResponse<ArticleVo> getArticleById(String id) {
        if (StringUtils.isAnyBlank(id)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Article article = articleService.getById(id);
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article, articleVo);
        return ResultUtils.success(articleVo);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param articleQueryRequest
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    @ApiOperationSupport(author = "Karos")
    @ApiOperation(value = "分页获取文章列表 - 管理员")
    public BaseResponse<Page<Article>> listArticle(ArticleQueryRequest articleQueryRequest) {
        Article articleQuery = new Article();
        if (articleQueryRequest != null) {
            BeanUtils.copyProperties(articleQueryRequest, articleQuery);
        }
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>(articleQuery);
        List<Article> articleList = articleService.list(queryWrapper);
        Page<Article> articlePage=new Page<>(articleQueryRequest.getCurrent(),articleQueryRequest.getPageSize(),articleList.size());
        articlePage.setRecords(articleList);
        return ResultUtils.success(articlePage);
    }

    @AuthCheck
    @GetMapping("/list/myfavorite")
    @ApiOperationSupport(author = "Karos")
    @ApiOperation(value = "获取用户 点赞/收藏 文章")
    public BaseResponse<Page<ArticleVo>> listArticleByFavorite(HttpServletRequest request){
        User loginUser = userService.getLoginUser(request);

        Long id = loginUser.getId();
        String userName = loginUser.getUserName();
        String userAccount = loginUser.getUserAccount();
        String userAvatar = loginUser.getUserAvatar();
        Integer gender = loginUser.getGender();
        String userRole = loginUser.getUserRole();
        String userPassword = loginUser.getUserPassword();
        Date createTime = loginUser.getCreateTime();
        Date updateTime = loginUser.getUpdateTime();
        String userMail = loginUser.getUserMail();
        Integer isDelete = loginUser.getIsDelete();

        HashOperations hashOperations = redisTemplate.opsForHash();
        List<Articlethumbrecords> list = (List) hashOperations.get(RedisKeysConstant.ThumbsHistoryHash, id.toString());
        //如果缓存中有，那么从缓存里面取
        if (list==null||list.size()<=0){
            QueryWrapper<Articlethumbrecords> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("userId",id);
            list=articlethumbrecordsService.list(queryWrapper);
            //把list存到redis
            hashOperations.put(RedisKeysConstant.ThumbsHistoryHash,id.toString(),list);
        }
        Page<Articlethumbrecords> articlethumbrecordsPage=new Page<>(0,list.size());
        articlethumbrecordsPage.setRecords(list);
        List<Articlethumbrecords> finalList = list;
        Page<ArticleVo> voList=(Page<ArticleVo>) articlethumbrecordsPage.convert(u->{
                ArticleVo v=new ArticleVo();
                Article a=articleService.getById(u.getArticleId());
                BeanUtils.copyProperties(a,v);
                Boolean thumb=false;
                if (ObjectUtil.isNotEmpty(finalList)){
                    Iterator<Articlethumbrecords> iterator = finalList.iterator();
                    while(iterator.hasNext()){
                        Articlethumbrecords next = iterator.next();
                        if (next.getArticleId().equals(v.getId())){
                            thumb=true;
                            break;
                        }
                    }
                }
                v.setHasThumb(thumb);
                if (hashOperations.hasKey(RedisKeysConstant.ThumbsNum,v.getId()))
                    v.setThumbNum(Long.valueOf((Integer)hashOperations.get(RedisKeysConstant.ThumbsNum,v.getId())));
                return v;
            });
        return ResultUtils.success(voList);
    }
    /**
     * 分页获取列表
     *
     * @param articleQueryRequest
     * @param request
     * @return
     */
    @AuthCheck
    @GetMapping("/list/page")
    @ApiOperationSupport(author = "Karos")
    @ApiOperation(value = "分页获取文章列表")
    public BaseResponse<Page<ArticleVo>> listArticleByPage(ArticleQueryRequest articleQueryRequest, HttpServletRequest request) {
        if (articleQueryRequest == null||(ObjectUtil.isNull(articleQueryRequest.getUserId())&&StringUtils.isAnyBlank(articleQueryRequest.getArticleTitle()))) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Article articleQuery = new Article();
        BeanUtils.copyProperties(articleQueryRequest, articleQuery);
        long current = articleQueryRequest.getCurrent();
        long size = articleQueryRequest.getPageSize();
        String sortField = articleQueryRequest.getSortField();
        String sortOrder = articleQueryRequest.getSortOrder();
        //模糊查询字段
        // content 需支持模糊搜索
        Long userId = articleQuery.getUserId();
        String articleTitle = articleQuery.getArticleTitle();
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //ToDo: 直接查询数据库效率低 后期改进为ElasticSearch
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(Article.class,info -> !"IP".equals(info.getColumn()));
        Long loginId = userService.getLoginUser(request).getId();
        queryWrapper.like(StringUtils.isNotBlank(articleTitle),"articleTitle",articleTitle)
                        .eq(StringUtils.isNotBlank(articleTitle),"isPublic",1)
                                .or()
                                     .like(StringUtils.isNotBlank(articleTitle),"articleTitle",articleTitle)
                                        .eq(ObjectUtil.isEmpty(userId),"userId", loginId)
                .eq(ObjectUtil.isNotEmpty(userId),"userId",userId)
                                                .eq(StringUtils.isNotBlank(articleTitle),"isPublic",0);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_DESC), sortField);
        Page<Article> articlePage = articleService.page(new Page<>(current, size), queryWrapper);
        HashOperations hashOperations = redisTemplate.opsForHash();
        //从缓存里面读取
        ArrayList<Articlethumbrecords> list = (ArrayList<Articlethumbrecords>) hashOperations.get(RedisKeysConstant.ThumbsHistoryHash, String.valueOf(userId!=null?userId:loginId));
        Page<ArticleVo> articleVoPage= (Page<ArticleVo>) articlePage.convert(u->{
            ArticleVo v=new ArticleVo();
            BeanUtils.copyProperties(u,v);
            Boolean thumb=false;
            if (ObjectUtil.isNotEmpty(list)){
                Iterator<Articlethumbrecords> iterator = list.iterator();
                while(iterator.hasNext()){
                    Articlethumbrecords next = iterator.next();
                    if (next.getArticleId().equals(v.getId())){
                        thumb=true;
                        break;
                    }
                }
            }
            v.setHasThumb(thumb);
            if (hashOperations.hasKey(RedisKeysConstant.ThumbsNum,v.getId()))
                v.setThumbNum(Long.valueOf((Integer)hashOperations.get(RedisKeysConstant.ThumbsNum,v.getId())));
            return v;
        });
        return ResultUtils.success(articleVoPage);
    }

    // endregion

}
