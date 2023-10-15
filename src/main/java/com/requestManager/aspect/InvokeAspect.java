//package com.requestManager.aspect;
//
//import com.alibaba.fastjson.JSONObject;
//import com.requestManager.constant.BusinessException;
//import com.requestManager.constant.Constant;
//import com.requestManager.constant.ErrorCodeEnum;
//import com.requestManager.data.request.ReqParam;
//import com.requestManager.data.user.User;
//import com.requestManager.redis.RedisService;
//import com.requestManager.util.Base64Util;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestAttributes;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import java.net.URLDecoder;
//import java.time.Duration;
//import java.util.Arrays;
//import java.util.Base64;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Objects;
//
///**
// * 只对invoke接口有效的切面
// *
// * @author chunxu.zhang
// * @since 2023/7/19
// **/
//@Aspect
//@Slf4j
//@Component
//@Order(2)
//public class InvokeAspect {
//    @Autowired
//    private RedisService redisService;
//
//
//    @Pointcut("execution(* com.requestManager.data.request.ReqController.exchange(..))")
//    public void paramInit() {
//    }
//
//    @Before("paramInit()")
//    public void auth(JoinPoint point) throws Exception {
//        ReqParam reqParam = (ReqParam) point.getArgs()[0];
//        int type = reqParam.getType();
//        String jsonBody = (String) reqParam.getData();
//        if (StringUtils.isEmpty(jsonBody)) {
//            return;
//        }
//        switch (type) {
//            case 0:
//                jsonBody = URLDecoder.decode(jsonBody, "UTF-8");
//                String[] split = jsonBody.split("&");
//                Map<String, String> map = new HashMap<>();
//                Arrays.stream(split).forEach(req -> {
//                    String[] split1 = req.split("=");
//                    if (split1.length == 0) {
//                        return;
//                    }
//                    map.put(split1[0], split1[1]);
//                });
//                jsonBody = JSONObject.toJSONString(map);
//                reqParam.setMediaType(MediaType.TEXT_PLAIN);
//                reqParam.setData(jsonBody);
//                break;
//            case 1:
//                byte[] decodedBytes = Base64.getDecoder().decode(jsonBody);
//                reqParam.setMediaType(MediaType.APPLICATION_OCTET_STREAM);
//                reqParam.setData(decodedBytes);
//                break;
//            case 2:
//                reqParam.setMediaType(MediaType.APPLICATION_JSON);
//                reqParam.setData(jsonBody);
//                break;
//            case 3:
//                split = jsonBody.split("&");
//                map = new HashMap<>();
//                Arrays.stream(split).forEach(req -> {
//                    String[] split1 = req.split("=");
//                    if (split1.length == 0) {
//                        return;
//                    }
//                    map.put(split1[0], split1[1]);
//                });
//                jsonBody = JSONObject.toJSONString(map);
//                reqParam.setMediaType(MediaType.APPLICATION_FORM_URLENCODED);
//                reqParam.setData(jsonBody);
//                break;
//        }
//    }
//
//}
