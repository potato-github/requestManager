package com.requestManager.aspect;

import com.alibaba.fastjson.JSONObject;
import com.requestManager.constant.BusinessException;
import com.requestManager.constant.Constant;
import com.requestManager.constant.ErrorCodeEnum;
import com.requestManager.data.user.User;
import com.requestManager.redis.RedisService;
import com.requestManager.util.Base64Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Objects;

/**
 * 权限校验的切面
 *
 * @author chunxu.zhang
 * @since 2023/7/19
 **/
@Aspect
@Slf4j
@Component
@Order(1)
public class AuthAspect {
    @Autowired
    private RedisService redisService;


    @Pointcut("execution(* com.requestManager.data.request.*Controller.*(..))" +
            "|| execution(* com.requestManager.data.user.LoginController.loginOut(..))" +
            "|| execution(* com.requestManager.data.user.LoginController.modifyPwd(..))")
    public void authCheck() {
    }

    @Before("authCheck()")
    public void auth(JoinPoint point) throws Exception {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String authCookie = null;
        if (StringUtils.isEmpty(authCookie)) {

            Cookie[] cookies = request.getCookies();
            if (Objects.nonNull(cookies)) {
                for (Cookie cookie : cookies) {
                    if (StringUtils.equals(cookie.getName(), Constant.AUTH_TOKEN)) {
                        authCookie = cookie.getValue();
                    }
                }
            }
        }
        if (StringUtils.isBlank(authCookie)) {
            throw new BusinessException(ErrorCodeEnum.TOKEN_MISS_ERROR);
        }
        User user = checkAuth(authCookie);
        UserThreadLocal.setUser(user);
    }

    private User checkAuth(String token) throws Exception {
        if (StringUtils.isBlank(token)) {
            log.error("请求header中token为空");
            throw new BusinessException(ErrorCodeEnum.TOKEN_MISS_ERROR);
        }

        String userStr = Base64Util.decode(token);
        User user = JSONObject.parseObject(userStr, User.class);
        Long userId = user.getId();
        String realToken = redisService.get(userId.toString());
        if (StringUtils.isBlank(realToken)) {
            log.error("token已过期，请重新登录");
            throw new BusinessException(ErrorCodeEnum.TOKEN_MISS_ERROR);
        }
        if (!StringUtils.equals(realToken, token)) {
            log.error("token校验不通过，请重新登录");
            throw new BusinessException(ErrorCodeEnum.TOKEN_MISS_ERROR);
        }
        // 通过校验，token续期
        redisService.expire(userId.toString(), Duration.ofHours(1));
        return user;
    }

}
