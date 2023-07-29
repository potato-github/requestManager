package com.requestManager.data.request;

import com.requestManager.constant.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 请求入口类
 *
 * @author chunxu.zhang
 * @since 2023/7/21
 **/
@RestController
@RequestMapping("/request")
public class ReqController {

    @Autowired
    public ReqService reqService;

    @PostMapping("/req/invoke")
    public RestResponse<ReqResponse> exchange(@RequestBody ReqParam reqParam) {
        ReqResponse result = reqService.exchange(reqParam);
        return RestResponse.success(result);
    }

}
