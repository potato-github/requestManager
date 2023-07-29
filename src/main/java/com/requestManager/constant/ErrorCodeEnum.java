package com.requestManager.constant;

import lombok.Getter;

/**
 * TODO
 *
 * @author chunxu.zhang
 * @since 2023/7/20
 **/
@Getter
public enum ErrorCodeEnum {
    /**
     *
     */
    NEW_OLD_PWD_NOT_SAME_ERROR(2,"新密码与旧密码一样，请修改"),
    USER_DUPLICATE_ERROR(3,"账号重复"),
    USER_PWD_ERROR(4,"账号/密码不正确"),
    TOKEN_MISS_ERROR(5,"请先登录"),
    LIMIT_ERROR(6,"用户请求次数已达上限"),
    CREATE_REST_CLIENT_ERROR(7,"创建client失败"),
    REQUEST_FAIL_ERROR(8,"请求发送失败，%s"),
    ;
    private int code;
    private String desc;

    ErrorCodeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
