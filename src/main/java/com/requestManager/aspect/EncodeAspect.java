package com.requestManager.aspect;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.util.Base64Utils;

import java.nio.charset.StandardCharsets;

/**
 * 加解密的切面
 *
 * @author chunxu.zhang
 * @since 2023/7/19
 **/
@Aspect
@Slf4j
//@Component
@Order(0)
public class EncodeAspect {

    @Pointcut("execution(* com.requestManager.controller.*Controller.*(..))")
    public void encode() {
    }

    @Around("encode()")
    public Object aroundq(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            String paramEncode = JSONObject.toJSONString(arg);
            byte[] decode = Base64Utils.decode(paramEncode.getBytes());
            String paramDecode = new String(decode, StandardCharsets.UTF_8);
            JSONObject jsonObject = JSONObject.parseObject(paramDecode);
            log.info("参数：{}", jsonObject);
        }

        Object proceed = joinPoint.proceed();
        log.info("返回值：{}", proceed);
        String result = JSONObject.toJSONString(proceed);
        byte[] resultEncode = Base64Utils.decode(result.getBytes());
        proceed = new String(resultEncode, StandardCharsets.UTF_8);
        return proceed;
    }

}
