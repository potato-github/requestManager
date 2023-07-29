package com.requestManager.data.token;

import com.alibaba.fastjson.JSONObject;
import com.requestManager.data.user.User;
import com.requestManager.redis.RedisService;
import com.requestManager.util.Base64Util;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.Duration;

/**
 * token生成、校验、销毁的类
 *
 * @author chunxu.zhang
 * @since 2023/7/20
 **/
@Service
public class TokeService {

    @Autowired
    private RedisService redisService;

    public String buildToken(User user) {
        Field[] fields = User.class.getDeclaredFields();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("now", System.currentTimeMillis());
        for (Field field : fields) {
            if (StringUtils.equalsAny(field.getName(), "password")) {
                continue;
            }
            try {
                field.setAccessible(true);
                jsonObject.put(field.getName(), field.get(user));
            } catch (IllegalAccessException ignore) {
            }
        }
        String userStr = JSONObject.toJSONString(jsonObject);
        String token = Strings.EMPTY;
        try {
            token = Base64Util.encode(userStr);
        } catch (Exception ignore) {
        }
        redisService.set(user.getId().toString(), token, Duration.ofHours(1L));
        return token;
    }
}
