package com.karos.project.annotation;

import com.karos.project.common.LimitTypeCommon;
import com.karos.project.config.FinalvarConfig;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AllLimitCheck {

    /**
     * 任意内容
     *
     * @return
     */
    String[] anyText() default "";

    /**
     * 指定内容
     *
     * @return
     */
    String mustText() default "";

    LimitTypeCommon type() default LimitTypeCommon.RequestLimit;
    long limitMaxNUM() default -1;

    long limitMaxExpTime() default -1;
}
