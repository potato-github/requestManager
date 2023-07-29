package com.requestManager.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.requestManager.util.AesCBC;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;

/**
 * @Description 返回数据加密
 * 实现ResponseBodyAdvice接口，其实是对加了@RestController(也就是@Controller+@ResponseBody)注解的处理器将要返回的值进行增强处理。其实也就是采用了AOP的思想，对返回值进行一次修改。
 * @Author: wts
 * @Date: 2022/11/7 11:10
 * @Version 1.0
 */
@Slf4j
@ControllerAdvice(basePackages = "com.requestManager.controller")
public class EncryptResponseBodyAdvice implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    /**
     * 原controller要返回的内容
     *
     * @param body
     * @param methodParameter
     * @param mediaType
     * @param aClass
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @return
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        Method method = methodParameter.getMethod();
        if (method.isAnnotationPresent(Encrypt.class)) {
            //获取注解配置的包含和去除字段
            Encrypt serializedField = methodParameter.getMethodAnnotation(Encrypt.class);
            //出参是否需要加密
            if (serializedField != null && !serializedField.out()) {
                return body;
            }
        }
        try {
            String result = JSON.toJSONString(body, SerializerFeature.DisableCircularReferenceDetect);
            // 加密
            String encrypt = AesCBC.encrypt(result);
            return encrypt;
        } catch (Exception e) {
            log.error("对方法method：{}返回数据进行解密出现异常：{}", methodParameter.getMethod().getName(), e.getMessage(), e);
        }
        return body;
    }
}
