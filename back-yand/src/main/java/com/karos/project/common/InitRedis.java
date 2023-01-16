/**
 * Title
 *
 * @ClassName: Init
 * @Description:
 * @author: Karos
 * @date: 2022/12/17 22:25
 * @Blog: https://www.wzl1.top/
 */

package com.karos.project.common;

import com.karos.project.constant.RedisKeysConstant;
import com.karos.project.model.entity.Article;
import com.karos.project.service.ArticleService;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@Component
public class InitRedis{
    @Resource
    RedisTemplate redisTemplate;
    @Resource
    ArticleService articleService;
    public InitRedis(){
            }

           public void init(){
               HashOperations hashOperations = redisTemplate.opsForHash();
               redisTemplate.expire("checkcode_img",1800000, TimeUnit.MILLISECONDS);
                redisTemplate.expire("checkcode_sms",1800000, TimeUnit.MILLISECONDS);
               ArrayList<Article> list = (ArrayList<Article>) articleService.list();
               for (Article k:list){
                   hashOperations.put(RedisKeysConstant.ThumbsNum,k.getId(),k.getThumbNum());
               }
            }
}