package com.requestManager.data.user;

import com.requestManager.constant.BusinessException;
import com.requestManager.constant.Constant;
import com.requestManager.constant.ErrorCodeEnum;
import com.requestManager.data.token.TokeService;
import com.requestManager.mapper.UserMapper;
import com.requestManager.redis.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * TODO
 *
 * @author chunxu.zhang
 * @since 2023/7/20
 **/
@Service
public class LoginService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TokeService tokeService;
    @Autowired
    private RedisService redisService;

    public void register(LoginReq req) {
        User userReq = User.builder().name(req.getName()).password(req.getPassword()).build();
        User user = userMapper.queryByName(userReq);
        if (!Objects.isNull(user)) {
            throw new BusinessException(ErrorCodeEnum.USER_DUPLICATE_ERROR);
        }
        userMapper.insert(userReq);
    }

    public void login(LoginReq req) {
        User userReq = User.builder().name(req.getName()).password(req.getPassword()).build();
        User user = userMapper.queryByName(userReq);
        if (Objects.isNull(user)) {
            throw new BusinessException(ErrorCodeEnum.USER_PWD_ERROR);
        }
        // 生产token
        String token = tokeService.buildToken(user);
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = ((ServletRequestAttributes) requestAttributes).getResponse();
        Cookie cookie = new Cookie(Constant.AUTH_TOKEN, token);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public void loginOut(LoginReq req) {
        User userReq = User.builder().name(req.getName()).build();
        User user = userMapper.queryByName(userReq);
        // 销毁token
        redisService.del(user.getId().toString());
    }

    public void modifyPwd(LoginReq req) {
        User userReq = User.builder().name(req.getName()).build();
        User user = userMapper.queryByName(userReq);
        if (StringUtils.equals(req.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCodeEnum.NEW_OLD_PWD_NOT_SAME_ERROR);
        }
        userReq.setPassword(req.getPassword());
        userMapper.update(userReq);
        // 销毁token
        redisService.del(user.getId().toString());
    }

}
