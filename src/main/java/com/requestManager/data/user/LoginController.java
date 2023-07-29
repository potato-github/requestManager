package com.requestManager.data.user;

import com.requestManager.constant.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO
 *
 * @author chunxu.zhang
 * @since 2023/7/20
 **/
@RestController
@RequestMapping("/user/login")
public class LoginController {

    @Autowired
    public LoginService loginService;
    
    @PostMapping("/register")
    public RestResponse<Void> register(@RequestBody LoginReq req) {
        loginService.register(req);
        return RestResponse.success();
    }

    @PostMapping("/login")
    public RestResponse<Void> login(@RequestBody LoginReq req) {
        loginService.login(req);
        return RestResponse.success();
    }

    @PostMapping("/loginout")
    public RestResponse<Void> loginOut(@RequestBody LoginReq req) {
        loginService.loginOut(req);
        return RestResponse.success();
    }

    @PostMapping("/modifypwd")
    public RestResponse<Void> modifyPwd(@RequestBody LoginReq req) {
        loginService.modifyPwd(req);
        return RestResponse.success();
    }
}
