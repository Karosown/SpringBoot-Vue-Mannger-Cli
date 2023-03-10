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
import java.io.*;
import java.util.*;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.pattern.CronPattern;
import cn.hutool.cron.task.Task;
import cn.hutool.crypto.digest.DigestUtil;
import cn.katool.Exception.KaToolException;
import cn.katool.util.expDateUtil;
import cn.katool.io.ImageUtils;
import cn.katool.util.expBase64Util;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import cn.katool.iputils.IpUtils;
import cn.katool.lock.LockUtil;
import cn.katool.qiniu.impl.QiniuServiceImpl;
import com.karos.project.annotation.AllLimitCheck;
import com.karos.project.annotation.AuthCheck;
import com.karos.project.common.*;
import com.karos.project.constant.CommonConstant;
import com.karos.project.constant.RedisKeysConstant;
import com.karos.project.exception.BusinessException;
import com.karos.project.mapper.ArticleMapper;
import com.karos.project.model.dto.article.*;
import com.karos.project.model.dto.articlehistory.ArticleHistoryQueryRequest;
import com.karos.project.model.entity.*;
import com.karos.project.model.vo.article.ArticleHistoryVo;
import com.karos.project.model.vo.article.ArticleVo;
import com.karos.project.service.*;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.connection.ConnectionUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    @GetMapping("/get/userAccount")
    public BaseResponse<String> getUserAccount(@RequestParam(value = "id",required = false) Long id,
                                               HttpServletResponse httpServletResponse){
        if (ObjectUtils.anyNull(id)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        HashOperations hashOperations = redisTemplate.opsForHash();
        String userAccount=null;
        hashOperations.get("UserAccountdb",id.toString());
        if (userAccount==null) {
            synchronized ("Lock".intern()){
                userAccount = userService.getUserAccount(id);
            }
            if (ObjectUtils.isNotEmpty(id))hashOperations.put("UserAccountdb",id.toString(),userAccount);
        }
        return ResultUtils.success(userAccount);
    }
    @AuthCheck
    @PostMapping("/exportVoExcel")
    public void exportExcel(@RequestBody ArticleExportRequest articleExportRequest, HttpServletResponse response) throws IOException {
        String fileName = String.valueOf(new Date().getTime());
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" +fileName + ".xls");
        ServletOutputStream out = response.getOutputStream();
        ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLS,true);
        Sheet sheet = new Sheet(1,0,ArticleVo.class);
        //?????????????????????
        sheet.setAutoWidth(Boolean.TRUE);
        sheet.setSheetName(fileName);
        writer.write(articleExportRequest.getArticleVoList(),sheet);
        writer.finish();
        out.flush();
        response.getOutputStream().close();
        out.close();
    }
    @AuthCheck(mustRole = "admin")
    @GetMapping("/LockTest")
    public BaseResponse<String> test(@RequestParam("expTime") Long expTime){
        try {
            lockUtil.DistributedLock(RedisKeysConstant.ThumbsHistoryHash.intern(),expTime, TimeUnit.SECONDS);
        } catch (KaToolException e) {
            throw new BusinessException(e.getCode(),e.getMessage());
        }
        return ResultUtils.success("?????????????????????20s?????????????????????");
    }
    @PostMapping("/thumb")
    @ApiOperationSupport(author = "Karos")
    @ApiOperation(value = "??????????????????")
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
                return "??????????????????";
            }
            if (BooleanUtil.isFalse(result)){
                return "???????????????";
            }
            return "?????????????????????";
        });
    }
    /**
     * ??????
     *
     * @param articleAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @ApiOperationSupport(author = "Karos")
    @ApiOperation(value = "??????????????????")
    public BaseResponse<String> addArticle(@RequestBody ArticleAddRequest articleAddRequest, HttpServletRequest request) {
        if (articleAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //??????????????????Nginx???????????????????????????IP
        articleAddRequest.setIP(IpUtils.getIpAddr(request));
        articleAddRequest.setUserId(userService.getLoginUser(request).getId());
        //??????????????????????????????
        String articleText = articleAddRequest.getArticleText();
        String articleUrl =null;
        try {
            File tempFile = File.createTempFile("temp",".html");
            FileOutputStream fos= new FileOutputStream(tempFile);
            //???????????????
            OutputStreamWriter osw=new OutputStreamWriter(fos,"utf-8");
            osw.write(articleText);
            osw.flush();
            osw.close();
            User loginUser = userService.getLoginUser(request);
            Long id = loginUser.getId();
            //????????????????????????????????????
            long random = ((long)Math.random())%1000000+1;
            String fileName= DigestUtil.md5Hex(articleAddRequest.getArticleTitle()+ id+random);
            articleUrl=qnsi.uploadFile(tempFile, "articleFile", fileName, ".html", true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Article article = new Article();
        BeanUtils.copyProperties(articleAddRequest, article);
        // ??????
        article.setCreateTime(new Date());
        article.setArticleUrl(articleUrl);
        if (StringUtils.isEmpty(article.getArticleIntroduction())){
            article.setArticleIntroduction(articleService.getIntroduction(articleAddRequest));
        }
        String featImg = article.getFeatImg();
        if (StringUtils.isNotBlank(featImg)){
            try {
                File file = ImageUtils.base642img(featImg);
                String s = qnsi.uploadFile(file, "/articleFeatIamg", article.getId(), ".png", true);
                article.setFeatImg(s);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        String newArticleId=null;
        synchronized (article.getArticleUrl().intern()) {
            articleService.validArticle(article, true);
            User loginUser = userService.getLoginUser(request);
            article.setUserId(loginUser.getId());
            //??????????????????
            Date publishTime = articleAddRequest.getPublishTime();
            if (ObjectUtil.isNotEmpty(publishTime)){
                String corn =null;
                try {
                    corn=expDateUtil.getCorn(publishTime);
                } catch (KaToolException e) {
                    throw new BusinessException(e);
                }
                String finalNewArticleId = newArticleId;
                String id = CronUtil.schedule(corn, new Task() {
                    @Override
                    public void execute() {
                        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", finalNewArticleId);
                        Article one = articleService.getOne(queryWrapper);
                        one.setIsPublic(1);
                        articleService.updateById(one);
                        log.info("????????????ID={}??????????????????,????????????{},??????????????????{}", finalNewArticleId, new Date(), publishTime);
                    }
                });
                article.setSchedId(id);
                redisTemplate.opsForHash().put(RedisKeysConstant.SchedDate,id,publishTime);
            }
            boolean result = articleService.save(article);
            if (!result) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR);
            }
            newArticleId = article.getId();
            //???????????????????????????
            Articlehistory articlehistory = new Articlehistory();
            articlehistory.setId(newArticleId);
            articlehistory.setArticleUrl(articleUrl);
            articlehistory.setIp(article.getIP());
            articlehistory.setVersion(1L);
            articlehistory.setUpdateTime(new Date());
            articlehistoryService.save(articlehistory);
        }
        return ResultUtils.success(newArticleId,"????????????");
    }

    /**
     * ??????
     *
     * @param articleUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @ApiOperationSupport(author = "Karos")
    @ApiOperation(value = "????????????")
    public BaseResponse<Boolean> updateArticle(@RequestBody ArticleUpdateRequest articleUpdateRequest,
                                            HttpServletRequest request) {
        if (articleUpdateRequest == null || articleUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Article article = new Article();
        BeanUtils.copyProperties(articleUpdateRequest, article);
        String articleText = articleUpdateRequest.getArticleText();
        // ????????????
        articleService.validArticle(article, false);
        User user = userService.getLoginUser(request);
        String id = articleUpdateRequest.getId();
        String featImg = article.getFeatImg();
        String schedId = article.getSchedId();
        // ??????????????????
        Article oldArticle = articleService.getById(id);
        String oldarticleUrl = oldArticle.getArticleUrl();
        if (oldArticle == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // ??????????????????????????????
        if (!oldArticle.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        String originName = qnsi.getOriginName(oldarticleUrl);
        String articleUrl=null;
        try {
            File tempFile = File.createTempFile("temp",".html");
            FileOutputStream fos= new FileOutputStream(tempFile);
            //???????????????
            OutputStreamWriter osw=new OutputStreamWriter(fos,"utf-8");
            osw.write(articleText);
            osw.flush();
            osw.close();
            articleUrl=qnsi.uploadFile(tempFile, "articleFile", originName, true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (StringUtils.isNotBlank(featImg)&&expBase64Util.isBase64(featImg)){
            try {
                File file = ImageUtils.base642img(featImg);
                String s = qnsi.uploadFile(file, "/articleFeatIamg", article.getId(), ".png", true);
                article.setFeatImg(s);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        Date publishTime = articleUpdateRequest.getPublishTime();
        if (ObjectUtil.isNotEmpty(publishTime)&&!redisTemplate.opsForHash().get(RedisKeysConstant.SchedDate,schedId).equals(publishTime)) {
            try {
                if (StringUtils.isNotBlank(schedId)){
                    CronUtil.updatePattern(schedId, CronPattern.of(expDateUtil.getCorn(publishTime)));
                    redisTemplate.opsForHash().put(RedisKeysConstant.SchedDate,schedId,publishTime);
                }
                else{
                    String corn =null;
                    try {
                        corn=expDateUtil.getCorn(publishTime);
                    } catch (KaToolException e) {
                        throw new BusinessException(e);
                    }
                    String finalNewArticleId = article.getId();
                    String SchedId = CronUtil.schedule(corn, new Task() {
                        @Override
                        public void execute() {
                            QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
                            queryWrapper.eq("id", finalNewArticleId);
                            Article one = articleService.getOne(queryWrapper);
                            one.setIsPublic(1);
                            articleService.updateById(one);
                            log.info("????????????ID={}??????????????????,????????????{},??????????????????{}", finalNewArticleId, new Date(), publishTime);
                        }
                    });
                    article.setSchedId(SchedId);
                    redisTemplate.opsForHash().put(RedisKeysConstant.SchedDate,SchedId,publishTime);
                }
            } catch (KaToolException e) {
                throw new RuntimeException(e);
            }
        }
        if(ObjectUtil.isEmpty(publishTime)){
            if (ObjectUtil.isNotEmpty(schedId)){
                CronUtil.remove(schedId);
                redisTemplate.opsForHash().delete(RedisKeysConstant.SchedDate,schedId);
            }
        }
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
        log.info("??????{} ??????{} ????????????ID{}-{}",user.getUserAccount(),user.getUserRole(),article.getId(),article.getArticleTitle());
        return ResultUtils.success(result,"????????????");
    }

    /**
     * ?????? id ??????
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    @ApiOperationSupport(author = "Karos")
    @ApiOperation(value = "????????????ID????????????")
    public BaseResponse<ArticleVo> getArticleById(String id) {
        if (StringUtils.isAnyBlank(id)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Article article = articleService.getById(id);
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article, articleVo);
        if(article.getIsPublic()==0){
            Date publishTime = (Date) redisTemplate.opsForHash().get(RedisKeysConstant.SchedDate, articleVo.getSchedId());
            articleVo.setPublishTime(publishTime);
        }
        return ResultUtils.success(articleVo,"????????????");
    }

    /**
     * ???????????????????????????????????????
     *
     * @param articleQueryRequest
     * @return
     */
    @Resource
    ArticleMapper articleMapper;
    @AuthCheck(anyRole ={"admin","user"})
    @GetMapping("/list")
    @ApiOperationSupport(author = "Karos")
    @ApiOperation(value = "????????????????????????")
    public BaseResponse<Page<ArticleVo>> listArticle(ArticleQueryRequest articleQueryRequest,HttpServletRequest request) {
        Page<ArticleVo> articlePage=new Page<>(articleQueryRequest.getCurrent(),articleQueryRequest.getPageSize());
        if (userService.isAdmin(request))
            articlePage = articleMapper.VoPage(articlePage);
        else articlePage=articleMapper.VoPagebyUser(String.valueOf(userService.getLoginUser(request).getId()),articlePage);
        return ResultUtils.success(articlePage,"????????????");
    }

    @GetMapping("/list/guest")
    @ApiOperationSupport(author = "Karos")
    @ApiOperation(value = "???????????????????????? - ??????")
    public BaseResponse<Page<ArticleVo>> listArticlebyGuest(ArticleQueryRequest articleQueryRequest,HttpServletRequest request) {
        Page<ArticleVo> articlePage=new Page<>(articleQueryRequest.getCurrent(),articleQueryRequest.getPageSize());
        articlePage = articleMapper.VoPageByGuest(articlePage);
        return ResultUtils.success(articlePage,"????????????");
    }

    @AuthCheck
    @GetMapping("/list/myfavorite")
    @ApiOperationSupport(author = "Karos")
    @ApiOperation(value = "???????????? ??????/?????? ??????")
    public BaseResponse<Page<ArticleVo>> listArticleByFavorite(HttpServletRequest request){
        User loginUser = userService.getLoginUser(request);
        Long id = loginUser.getId();
        HashOperations hashOperations = redisTemplate.opsForHash();
        List<Articlethumbrecords> list = (List) hashOperations.get(RedisKeysConstant.ThumbsHistoryHash, id.toString());
        //?????????????????????????????????????????????
        if (list==null||list.size()<=0){
            QueryWrapper<Articlethumbrecords> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("userId",id);
            list=articlethumbrecordsService.list(queryWrapper);
            //???list??????redis
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
        return ResultUtils.success(voList,"????????????");
    }
    @Resource
    ArticleTypeService articleTypeService;
    /**
     * ??????????????????
     *
     * @param articleQueryRequest
     * @param request
     * @return
     */
//    @AuthCheck
    @GetMapping("/list/page")
    @ApiOperationSupport(author = "Karos")
    @ApiOperation(value = "????????????????????????")
    public BaseResponse<Page<ArticleVo>> listArticleByPage(ArticleQueryRequest articleQueryRequest, HttpServletRequest request) {
        if (articleQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Article articleQuery = new Article();
        BeanUtils.copyProperties(articleQueryRequest, articleQuery);
        long current = articleQueryRequest.getCurrent();
        long size = articleQueryRequest.getPageSize();
        String sortField = articleQueryRequest.getSortField();
        String sortOrder = articleQueryRequest.getSortOrder();
        //??????????????????
        // content ?????????????????????
        Long userId = articleQuery.getUserId();
        String articleTitle = articleQuery.getArticleTitle();
        // ????????????
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //ToDo: ?????????????????????????????? ???????????????ElasticSearch
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(Article.class,info -> !"IP".equals(info.getColumn()));
        Long loginId = userService.getLoginUserNoThrow(request).getId();
        if (userService.isAdmin(request)!=null&& userService.isAdmin(request)){
            queryWrapper.like(StringUtils.isNotBlank(articleTitle),"articleTitle",articleTitle)
                    .eq(StringUtils.isNotBlank(articleTitle),"isPublic",1)
                    .or()
                    .like(StringUtils.isNotBlank(articleTitle),"articleTitle",articleTitle)
                    .eq(ObjectUtil.isEmpty(userId),"userId", loginId)
                    .eq(ObjectUtil.isNotEmpty(userId),"userId",userId)
                    .eq(StringUtils.isNotBlank(articleTitle),"isPublic",0);
        } else if (userService.isAdmin(request)==null) {
            queryWrapper.like(StringUtils.isNotBlank(articleTitle),"articleTitle",articleTitle)
                    .eq("isPublic",1);
        } else{
            queryWrapper.like(StringUtils.isNotBlank(articleTitle),"articleTitle",articleTitle)
            .eq("isPublic",1);
        }
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_DESC), sortField);
        if (articleQueryRequest.getType()!=0){
              queryWrapper.eq("type",articleQueryRequest.getType());
        }
        if (StringUtils.isNotEmpty(articleQueryRequest.getTypeName())){
            QueryWrapper<ArticleType> typeWrapper = new QueryWrapper<>();
            typeWrapper.eq("typeName",articleQueryRequest.getTypeName());

            queryWrapper.eq("type",articleTypeService.getOne(typeWrapper));
        }
        Page<Article> articlePage = articleService.page(new Page<>(current, size), queryWrapper);
        HashOperations hashOperations = redisTemplate.opsForHash();
        //?????????????????????
        ArrayList<Articlethumbrecords> list = (ArrayList<Articlethumbrecords>) hashOperations.get(RedisKeysConstant.ThumbsHistoryHash, String.valueOf(userId!=null?userId:loginId));
        Page<ArticleVo> articleVoPage= (Page<ArticleVo>) articlePage.convert(u->{
            ArticleVo v=new ArticleVo();
            BeanUtils.copyProperties(u,v);
            int type = u.getType();
            if (articleTypeService.getById(type)!=null){
                v.setTypeId(type);
                v.setType(articleTypeService.getById(type).getTypeName());
            }
            else{
                v.setType("?????????");
            }
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
        return ResultUtils.success(articleVoPage,"????????????");
    }

    @AuthCheck
    @GetMapping("/list/garbage")
    public BaseResponse<Page<ArticleVo>> getGarbage(ArticleQueryRequest articleQueryRequest){
        Page<ArticleVo> articlePage=new Page<>(articleQueryRequest.getCurrent(),articleQueryRequest.getPageSize());
        Page<ArticleVo> articleVoPage = articleMapper.GarbageVoPage(articlePage);
        return ResultUtils.success(articleVoPage,"????????????");
    }
    @AuthCheck
    @GetMapping("/getHistory")
    public BaseResponse<Page<ArticleHistoryVo>> getUpdateHistory(ArticleHistoryQueryRequest ArticleHistoryQueryRequest, HttpServletRequest request){
        String id = ArticleHistoryQueryRequest.getId();   //??????ID
        //??????
        //??????????????????ID
        Article oldArticle = articleService.getById(id);
        Long ArticleUserId= oldArticle.getUserId();
        //????????????
        User loginUser = userService.getLoginUser(request);
        //????????????????????????????????????
        if (!ArticleUserId.equals(loginUser)&&!userService.isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        QueryWrapper<Articlehistory> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("id",id)
                .orderByDesc("updateTime");
        List<Articlehistory> list = articlehistoryService.list(queryWrapper);
        Page<Articlehistory> ArticlehistoryPage=new Page<>();
        ArticlehistoryPage.setRecords(list);
        Page<ArticleHistoryVo> ArticleHistoryVoPage= (Page<ArticleHistoryVo>) ArticlehistoryPage.convert(dao->{
            ArticleHistoryVo vo=new ArticleHistoryVo();
            BeanUtils.copyProperties(dao,vo);
            return vo;
        });
        return ResultUtils.success(ArticleHistoryVoPage,"????????????");
    }

    /**
     * ??????
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AllLimitCheck(mustText = "????????????????????????",limitMaxNUM = 10000)
    @ApiOperationSupport(author = "Karos")
    @ApiOperation(value = "??????????????????")
    public BaseResponse<Boolean> deleteArticle(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        String obId = deleteRequest.getId();
        List<String> obIds=deleteRequest.getIds();
            if (deleteRequest == null || (StringUtils.isAnyBlank((String) obId)&&ObjectUtils.isEmpty(obIds))) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }

        if (ObjectUtils.isNotEmpty(obIds) && obIds.size()>0){
            List<String> obIdList = (List<String>) obIds;
            if (obIdList.size()<1){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"???????????????1?????????");
            }
            if ((obIdList).size()>20){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"??????????????????20?????????");
            }
            User user = userService.getLoginUser(request);
            boolean b=true;
            int errorNum=0;
            // ??????????????????
            for (String id:obIdList){
                Article byId = articleService.getById(id);
                if (!byId.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
                    b&=false;
                    errorNum++;
                    continue;
                }
                articleService.removeById(id);
            }
            if (b)return ResultUtils.success(b,obIdList.size()+"???????????????????????????");
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"???"+errorNum+"???????????????????????????????????????????????????????????????...");
        }
        User user = userService.getLoginUser(request);
        String id = (String) obId;
        // ??????????????????
        Article oldArticle = articleService.getById(id);
        if (oldArticle == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // ??????????????????????????????
        if (!oldArticle.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        //???????????????
        boolean b = articleService.removeById(id);
        if (b)return ResultUtils.success(b,"????????????");
        throw new BusinessException(ErrorCode.OPERATION_ERROR);
    }

    @AuthCheck
    @AllLimitCheck(mustText = "????????????????????????",limitMaxNUM = 10000)
    @PostMapping("/recovery")
    BaseResponse<Boolean> recovery(@RequestBody RecoveryRequest recoveryRequest,HttpServletRequest request){
        String obId = recoveryRequest.getId();
        List<String> obIds=recoveryRequest.getIds();
        if (recoveryRequest == null || (StringUtils.isAnyBlank((String) obId)&&ObjectUtils.isEmpty(obIds))) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        if (ObjectUtils.isNotEmpty(obIds) && obIds.size()>0){
            List<String> obIdList = (List) obIds;
            if (obIdList.size() < 1) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "???????????????1?????????");
            }
            if ((obIdList).size() > 20) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "??????????????????20?????????");
            }
            boolean b=true;
            if (userService.isAdmin(request)){
                b=articleMapper.recoveryByList(obIdList);
            }
            else{
                User user=userService.getLoginUser(request);
                int errorNum=0;
                for (String id:obIdList){
                    Article byId = articleService.getById(id);
                    if (!byId.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
                        b&=false;
                        errorNum++;
                        continue;
                    }
                    articleMapper.recoveryById(id);
                }
                if (b)return ResultUtils.success(b,obIdList.size()+"???????????????????????????");
                throw new BusinessException(ErrorCode.OPERATION_ERROR,"???"+errorNum+"???????????????????????????????????????????????????????????????...");
            }
            if (b)return ResultUtils.success(b,obIdList.size()+"???????????????????????????");
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        String id = (String) recoveryRequest.getId();
        Article article = articleMapper.ingoreGetOneByID(id);
        if (ObjectUtils.isEmpty(article)||article.getIsDelete()!=1){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"???????????????????????????????????????");
        }
        if(!userService.isAdmin(request)&& !Objects.equals(userService.getLoginUser(request).getId(), article.getUserId())){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = articleMapper.recoveryById(id);
        return ResultUtils.success(b,"????????????");
    }
    // endregion

}
