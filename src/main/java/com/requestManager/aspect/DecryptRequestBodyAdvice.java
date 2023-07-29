package com.requestManager.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.requestManager.constant.BusinessException;
import com.requestManager.util.AesCBC;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * @description: post请求的加密参数进行解密，返回一个JSONObject对象
 * @Author: wts
 * @Date: 2022/11/7 11:10
 * @Version 1.0
 */
@Slf4j
@ControllerAdvice(basePackages = "com.requestManager.controller")
public class DecryptRequestBodyAdvice implements RequestBodyAdvice {

    private final HttpSession session;

    public DecryptRequestBodyAdvice(HttpSession session) {
        this.session = session;
    }

    /**
     * 该方法用于判断当前请求，是否要执行beforeBodyRead方法
     * methodParameter: 方法的参数对象
     * type: 方法的参数类型
     * aClass: 将会使用到的Http消息转换器类类型
     * 注意：此判断方法，会在beforeBodyRead 和 afterBodyRead方法前都触发一次。
     *
     * @return 返回true则会执行beforeBodyRead
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    /**
     * 在Http消息转换器执转换，之前执行
     * inputMessage: 客户端的请求数据
     * methodParameter: 方法的参数对象
     * type: 方法的参数类型
     * aClass: 将会使用到的Http消息转换器类类型
     *
     * @return 返回 一个自定义的HttpInputMessage
     */
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) throws IOException {
        try {
            boolean encode = false;
            // 判断该方法是否含有@Encrypt注解
            if (methodParameter.getMethod().isAnnotationPresent(Encrypt.class)) {
                //获取注解配置的包含和去除字段
                Encrypt serializedField = methodParameter.getMethodAnnotation(Encrypt.class);
                //入参是否需要解密
                encode = serializedField.in();
            }
            if (encode) {
                // 解密-使用解密后的数据，构造新的读取流
                return new MyHttpInputMessage(inputMessage, type);
            } else {
                return inputMessage;
            }
        } catch (Exception e) {
            log.error("请求参数错误：{}", e.getMessage(), e);
            throw new BusinessException("请求参数错误！");
        }
    }

    /**
     * 在Http消息转换器执转换，之后执行
     * o: 转换后的对象
     * httpInputMessage: 客户端的请求数据
     * methodParameter: handler方法的参数类型
     * type: handler方法的参数类型
     * aClass: 使用的Http消息转换器类类型
     *
     * @return 返回一个新的对象
     */
    @Override
    public Object afterBodyRead(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return o;
    }

    /**
     * 参数与afterBodyRead相同，不过这个方法处理的是，body为空的情况
     */
    @Override
    public Object handleEmptyBody(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return o;
    }

    /**
     * 解密-使用解密后的数据，构造新的读取流
     */
    class MyHttpInputMessage implements HttpInputMessage {
        private HttpHeaders headers;

        private InputStream body;

        public MyHttpInputMessage(HttpInputMessage inputMessage, Type type) throws Exception {
            this.headers = inputMessage.getHeaders();
            String bodyStr = StringUtils.defaultString(IOUtils.toString(inputMessage.getBody(), "UTF-8"));
            try {
                // SM4解密
                String decodeParameters = AesCBC.decrypt(bodyStr);
                // Feature.OrderedField：解析时增加参数不调整顺序
                JSONObject decodeParaJson = JSON.parseObject(decodeParameters, Feature.OrderedField);
                if (decodeParaJson != null) {
                    this.body = IOUtils.toInputStream(decodeParameters, "UTF-8");
                    return;
                }
                this.body = null;
            } catch (Exception e) {
                log.error("加密参数【{}】解密失败：{}", bodyStr, e.getMessage(), e);
                throw new BusinessException(e.getMessage());
            }
        }

        @Override
        public InputStream getBody() {
            return body;
        }

        @Override
        public HttpHeaders getHeaders() {
            return headers;
        }

    }
}
