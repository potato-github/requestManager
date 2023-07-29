package com.requestManager.constant;

import lombok.Data;
import org.apache.logging.log4j.util.Strings;

/**
 * TODO
 *
 * @author chunxu.zhang
 * @since 2023/7/20
 **/
@Data
public class RestResponse<T> {
    private int code;
    private String msg;
    private T data;

    public static <T> RestResponse<T> success() {
        RestResponse<T> response = new RestResponse<>();
        response.code = 200;
        response.msg = Strings.EMPTY;
        response.data = null;
        return response;
    }
    public static <T> RestResponse<T> success(T data) {
        RestResponse<T> response = new RestResponse<>();
        response.code = 200;
        response.msg = Strings.EMPTY;
        response.data = data;
        return response;
    }

    public static <T> RestResponse<T> fail(ErrorCodeEnum codeEnum) {
        RestResponse<T> response = new RestResponse<>();
        response.code = codeEnum.getCode();
        response.msg = codeEnum.getDesc();
        response.data = null;
        return response;
    }

    public static <T> RestResponse<T> fail(BusinessException businessException) {
        RestResponse<T> response = new RestResponse<>();
        response.code = businessException.getCode();
        response.msg = businessException.getMessage();
        response.data = null;
        return response;
    }

    public static <T> RestResponse<T> fail(ErrorCodeEnum codeEnum, String msg) {
        RestResponse<T> response = new RestResponse<>();
        response.code = codeEnum.getCode();
        response.msg = String.format(codeEnum.getDesc(), msg);
        response.data = null;
        return response;
    }
}
