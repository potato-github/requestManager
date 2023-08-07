package com.requestManager.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.requestManager.constant.RestResponse;
import com.requestManager.util.AesCBC;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试类
 *
 * @author chunxu.zhang
 * @since 2023/7/19
 **/
@RestController
@RequestMapping("/test")
public class TestController {

    @PostMapping("/test")
    public RestResponse<String> test(@RequestBody Object param) {
        return RestResponse.success("test");
    }

    @PostMapping("/encode")
    public RestResponse<String> encode(@RequestBody Object param) {
        String result = JSON.toJSONString(param, SerializerFeature.DisableCircularReferenceDetect);
        // 加密
        String body = AesCBC.encrypt(result);
        return RestResponse.success(body);
    }
}
