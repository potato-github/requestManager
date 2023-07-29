package com.requestManager.data.url;

import com.requestManager.constant.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * url访问的配置
 *
 * @author chunxu.zhang
 * @since 2023/7/20
 **/
@RestController
@RequestMapping("/url/config")
public class UrlController {

    @Autowired
    public UrlService urlService;
    
    @PostMapping("/add")
    public RestResponse<Void> register(@RequestBody UrlReq req) {
        urlService.add(req);
        return RestResponse.success();
    }

    @PostMapping("/update")
    public RestResponse<Void> login(@RequestBody UrlReq req) {
        urlService.update(req);
        return RestResponse.success();
    }

    @PostMapping("/delete")
    public RestResponse<Void> loginOut(@RequestBody UrlReq req) {
        urlService.delete(req);
        return RestResponse.success();
    }

    @PostMapping("/select")
    public RestResponse<List<Url>> modifyPwd(@RequestBody UrlReq req) {
        List<Url> urlList = urlService.select(req);
        return RestResponse.success(urlList);
    }
}
