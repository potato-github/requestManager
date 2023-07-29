package com.requestManager.constant;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * TODO
 *
 * @author chunxu.zhang
 * @since 2023/7/20
 **/
@ControllerAdvice
public class CatchExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public @ResponseBody RestResponse<Void> runtimeExceptionHandler(BusinessException businessException) {
        return RestResponse.fail(businessException);
    }

}
