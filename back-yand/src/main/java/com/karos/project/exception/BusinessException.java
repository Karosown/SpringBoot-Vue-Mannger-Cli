package com.karos.project.exception;

import cn.katool.Exception.KaToolException;
import com.karos.project.common.ErrorCode;
import springfox.documentation.spi.service.contexts.ResponseContext;

/**
 * 自定义异常类
 *
 * @author karos
 */
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }
    public BusinessException(KaToolException kaToolException) {
        super(kaToolException.getMessage());
        this.code = kaToolException.getCode();
    }
    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }
}
