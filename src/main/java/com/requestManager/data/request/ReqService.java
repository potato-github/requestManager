package com.requestManager.data.request;

import com.requestManager.aspect.UserThreadLocal;
import com.requestManager.config.UserLevelConfig;
import com.requestManager.constant.BusinessException;
import com.requestManager.constant.ErrorCodeEnum;
import com.requestManager.data.user.User;
import com.requestManager.util.RestClient;
import com.requestManager.util.SlideWindow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author chunxu.zhang
 * @since 2023/7/21
 **/
@Service
public class ReqService {

    @Value("${window.limit.count}")
    private int limitCount;
    @Value("${window.limit.time}")
    private int limitTimeSecond;
    @Autowired
    private RestClient client;
    @Autowired
    private UserLevelConfig levelConfig;


    // 获取body
    public ReqResponse exchange(ReqParam reqParam) {
        User user = UserThreadLocal.getUser();
        SlideWindow slideWindow = new SlideWindow(user, reqParam.getUrl(), levelConfig.getLimitCount(user.getLevel()), limitTimeSecond);
        if (!slideWindow.canOpr()) {
            throw new BusinessException(ErrorCodeEnum.LIMIT_ERROR, user.getName());
        }
        // 执行
        ResponseEntity<Object> result = client.exchange(reqParam);

        Object data = result.getBody();
        List<String> cookie = result.getHeaders().get(HttpHeaders.SET_COOKIE);
        ReqResponse response = ReqResponse.builder().data(data).cookie(cookie).build();
        return response;
    }
}
