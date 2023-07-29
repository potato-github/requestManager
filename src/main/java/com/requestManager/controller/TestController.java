package com.requestManager.controller;

import com.requestManager.aspect.Encrypt;
import com.requestManager.constant.RestResponse;
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

    @Encrypt
    @PostMapping("/test")
    public RestResponse<String> test(@RequestBody Object param) {
        return RestResponse.success("test");
    }
}
