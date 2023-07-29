package com.requestManager.constant;

import lombok.Getter;

/**
 * 捕获全局异常
 *
 * @author chunxu.zhang
 * @since 2023/7/20
 **/
@Getter
public class BusinessException extends RuntimeException {

    private Integer code;

    /**
     * 异常对应的描述信息
     */
    private String message;

    private Throwable throwable;

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
        this.message = message;
    }

    public BusinessException(ErrorCodeEnum codeEnum, String param) {
        super(codeEnum.getDesc());
        this.message = String.format(codeEnum.getDesc(), param);
        this.code = codeEnum.getCode();
    }

    public BusinessException(ErrorCodeEnum codeEnum) {
        super(codeEnum.getDesc());
        this.code = codeEnum.getCode();
        this.message = codeEnum.getDesc();
    }

}
