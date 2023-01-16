package com.karos.project.service.impl;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.karos.KaTool.lock.LockUtil;
import com.karos.project.common.ErrorCode;
import com.karos.project.constant.RedisKeysConstant;
import com.karos.project.exception.BusinessException;
import com.karos.project.model.entity.Articlethumbrecords;
import com.karos.project.model.entity.User;
import com.karos.project.service.ArticlethumbrecordsService;
import com.karos.project.mapper.ArticlethumbrecordsMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
* @author 30398
* @description 针对表【articlethumbrecords(文章点赞记录表)】的数据库操作Service实现
* @createDate 2023-01-05 04:22:14
*/
@Service
public class ArticlethumbrecordsServiceImpl extends ServiceImpl<ArticlethumbrecordsMapper, Articlethumbrecords>
    implements ArticlethumbrecordsService{
    @Resource
    RedisTemplate redisTemplate;
    @Resource
    UserServiceImpl userService;
    @Resource
    LockUtil lockUtil;
    @Override
    public Boolean thumb(Articlethumbrecords entity, HttpServletRequest request) {
        if (StringUtils.isAnyBlank(entity.getArticleId())){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long userId = entity.getUserId();
        if (ObjectUtils.isEmpty(userId)){
            if (ObjectUtils.isEmpty(request))
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"无法获取登录用户");
            //获取当前登录用户
            User loginUser = userService.getLoginUser(request);
            entity.setUserId(loginUser.getId());
            userId = entity.getUserId();
        }
        HashOperations hashOperations = redisTemplate.opsForHash();
        SetOperations setOperations = redisTemplate.opsForSet();
        //分布式锁校验，如果在这个时候在进行点赞数量持久化，那就等待
        lockUtil.DistributedAssert(RedisKeysConstant.ThumbsHistoryHash.intern());
        String userAccount=userService.getLoginUser(request).getUserAccount();
        synchronized (userAccount.intern()) {
            List list = (List) hashOperations.get(RedisKeysConstant.ThumbsHistoryHash, String.valueOf(userId));
            if (ObjectUtils.isEmpty(list)) list = new ArrayList<Articlethumbrecords>();
            //如果点过赞那么取消，并且返回true
            Integer o = (Integer) hashOperations.get(RedisKeysConstant.ThumbsNum, entity.getArticleId());
            boolean contains = list.contains(entity);
            if (BooleanUtil.isTrue(contains)) {
                list.remove(entity);
                Long delete = hashOperations.delete(RedisKeysConstant.ThumbsHistoryHash, String.valueOf(userId));
                hashOperations.put(RedisKeysConstant.ThumbsHistoryHash, String.valueOf(userId), list);
                hashOperations.increment(RedisKeysConstant.ThumbsNum, entity.getArticleId(), -1);
                return !(delete == 1L);
            }
            //把实体存入Redis缓存中
            list.add(entity);
            setOperations.add(RedisKeysConstant.ThumbsUserSet, entity.getUserId());
            hashOperations.put(RedisKeysConstant.ThumbsHistoryHash, String.valueOf(userId), list);
            hashOperations.increment(RedisKeysConstant.ThumbsNum, entity.getArticleId(), 1);
            return true;
        }
    }
}




