/**
 * Title
 *
 * @ClassName: FileUploadIntercepotor
 * @Description:
 * @author: Karos
 * @date: 2022/12/15 10:35
 * @Blog: https://www.wzl1.top/
 */

package com.karos.project.aop;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.katool.iputils.IpUtils;
import com.karos.project.common.*;
import com.karos.project.config.FinalvarConfig;
import com.karos.project.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.xmlbeans.impl.xb.xsdschema.All;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.karos.project.annotation.AllLimitCheck;
@Aspect
@Component
@Slf4j
public class AllLimitIntercepotor {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    FinalvarConfig fcf;


    @Around("@annotation(AllLimitCheck)")
    public Object doInterceptor(ProceedingJoinPoint point, AllLimitCheck AllLimitCheck) throws Throwable {
        if (AllLimitCheck.limitMaxNUM()==-1){
            AnnotationUtil.setValue(AllLimitCheck,"limitMaxNUM",fcf.MAX_FERQUENCY);
        }
        if (AllLimitCheck.limitMaxExpTime()==-1){
            AnnotationUtil.setValue(AllLimitCheck,"limitMaxExpTime",fcf.EXP_TIME);
        }
        // ??????
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // ??????????????????
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        //??????IP????????????
            //??????ip??????
        String ip = IpUtils.getIpAddr(httpServletRequest);
        HashOperations hashOperations = redisTemplate.opsForHash();
        FileLimit fileLimit=null;
        //???????????????????????????
        String type = AllLimitCheck.type().toString();
        if (!hashOperations.hasKey(type,AllLimitCheck.mustText())) {
            ConcurrentHashMap<String, IPFileDownloadNum> ipFileDownloadNums = new ConcurrentHashMap<>();
            ipFileDownloadNums.put(ip,new IPFileDownloadNum(1L,new Date(new Date().getTime()+AllLimitCheck.limitMaxNUM())));
            fileLimit=new FileLimit(AllLimitCheck.mustText(), ipFileDownloadNums);
           hashOperations.put(type, AllLimitCheck.mustText(),fileLimit);
        }
       else{
            fileLimit = (FileLimit) hashOperations.get(type, AllLimitCheck.mustText());
            ConcurrentHashMap<String, IPFileDownloadNum> ipFileDownloadNums = fileLimit.getIpFileDownloadNums();
                IPFileDownloadNum ipFileDownloadNum = ipFileDownloadNums.get(ip);
                if (ipFileDownloadNum!=null){
                    if (ipFileDownloadNum.getExpTime().getTime()>=new Date().getTime()){
                        hashOperations.delete(type, AllLimitCheck.mustText());
                        ipFileDownloadNum.setFreQuency(0L);
                        ipFileDownloadNum.setExpTime(new Date(new Date().getTime()+ AllLimitCheck.limitMaxExpTime()));
                        ipFileDownloadNums.remove(ip);
                        ipFileDownloadNums.put(ip,ipFileDownloadNum.inc());
                        hashOperations.put(type, AllLimitCheck.mustText(), fileLimit);
                    }else {
                        if (ipFileDownloadNum.getFreQuency() >= AllLimitCheck.limitMaxNUM()){
                            log.error("{}>={}",ipFileDownloadNum.getFreQuency() , AllLimitCheck.limitMaxNUM());
                            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, AllLimitCheck.mustText()+"????????????");
                        }

                        hashOperations.delete(type,AllLimitCheck.mustText());
                        ipFileDownloadNums.remove(ip);
                        ipFileDownloadNums.put(ip,ipFileDownloadNum.inc());
                        hashOperations.put(
                                type, AllLimitCheck.mustText(),
                                fileLimit
                        );
                    }
//                    log.info("{}",ipFileDownloadNums==fileLimit.getIpFileDownloadNums());
                }
        }
        // ?????????????????? id
        String requestId = UUID.randomUUID().toString();
        String url = httpServletRequest.getRequestURI();
        // ??????????????????
        Object[] args = point.getArgs();
        String reqParam = "[" + StringUtils.join(args, ", ") + "]";
        // ??????????????????
        log.info("request start???id: {}, path: {}, ip: {}, params: {}", requestId, url, ip, reqParam);
        // ???????????????
        Object result = point.proceed();
        // ??????????????????
        stopWatch.stop();
        long totalTimeMillis = stopWatch.getTotalTimeMillis();
        log.info("request end, id: {}, cost: {}ms", requestId, totalTimeMillis);
        return result;
    }
}
