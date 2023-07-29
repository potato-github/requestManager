package com.requestManager.util;

import com.alibaba.fastjson.JSONObject;
import com.requestManager.constant.BusinessException;
import com.requestManager.constant.ErrorCodeEnum;
import com.requestManager.data.request.ReqParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 多线程异步执行请求类
 *
 * @author chunxu.zhang
 * @since 2023/7/20
 **/
@Slf4j
@Component
public class RestClient {

    @Value("${req.timeout}")
    private Long reqTimeOut;
    @Autowired
    private IPPool ipPool;
    @Autowired
    private RestTemplate restTemplate;

    private final static int PROCESSOR_COUNT = Runtime.getRuntime().availableProcessors();

    private static ExecutorService executor =
            new ThreadPoolExecutor(PROCESSOR_COUNT, PROCESSOR_COUNT * 5,
                    1, TimeUnit.MINUTES, new LinkedBlockingQueue<>(1000));

    public ResponseEntity doExchange(ReqParam param, RestTemplate restTemplate) {
        List<String> cookieList = param.getCookieList();
        HttpHeaders headers = new HttpHeaders();
        if (!CollectionUtils.isEmpty(cookieList)) {
            cookieList.forEach(cookie -> {
                headers.add(HttpHeaders.COOKIE, cookie);
            });
        }
        String jsonBody = param.getJsonBody();
        Object parse = JSONObject.parse(jsonBody);
        HttpEntity<Object> entity = new HttpEntity<>(parse, headers);
        try {
            HttpMethod method = HttpMethod.valueOf(param.getMethod().toUpperCase());
            return restTemplate.exchange(param.getUrl(), method, entity, String.class);
        } catch (Exception e) {
            log.error("请求发送失败，url:{}，error：{}", param.getUrl(), e.getMessage());
            throw new BusinessException(ErrorCodeEnum.REQUEST_FAIL_ERROR, e.getMessage());
        }
    }

    public RestTemplate getRestTemplate(ReqParam param) {
        if (!param.getProxyFlag()) {
            return restTemplate;
        }
        String proxy = ipPool.getProxy(param.getDeleteFlag());
        return proxyTemplate(proxy);
    }

    public <T> ResponseEntity<T> exchange(ReqParam param) {
        doExchange(param, getRestTemplate(param));
        Future<ResponseEntity<T>> future = executor.submit(
                () -> doExchange(param, getRestTemplate(param)));
        try {
            ResponseEntity<T> response = future.get(reqTimeOut, TimeUnit.SECONDS);
            return response;
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error("请求发送失败，url:{}，error：{}", param.getUrl(), e.getMessage());
            throw new BusinessException(ErrorCodeEnum.REQUEST_FAIL_ERROR, e.getMessage());
        }

    }

    public RestTemplate proxyTemplate(String proxy) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        String[] proxys = proxy.split(":");
        factory.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxys[0], Integer.parseInt(proxys[1]))));
        return new RestTemplate(factory);
    }

}
