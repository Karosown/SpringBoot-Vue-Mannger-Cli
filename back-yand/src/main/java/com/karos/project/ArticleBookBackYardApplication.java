package com.karos.project;

import cn.hutool.core.codec.Base64;
import cn.hutool.cron.CronUtil;
import com.karos.project.common.InitRedis;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableScheduling
@MapperScan("com.karos.project.mapper")
public class ArticleBookBackYardApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArticleBookBackYardApplication.class, args);
        //支持秒级别定时任务
        CronUtil.setMatchSecond(true);
        //定时服务启动
        CronUtil.start();
        //使用deamon模式
        //CronUtil.start(true);
    }

}
