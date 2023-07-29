package com.requestManager.aspect;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
/**
 * @description: 参数加密设置
 * @Author: wts
 * @Date: 2022/11/7 11:10
 * @Version 1.0
 */
public @interface Encrypt {

    /**
     * 入参是否解密，默认解密
     */
    boolean in() default true;

    /**
     * 返回是否加密，默认加密
     */
    boolean out() default true;
}
