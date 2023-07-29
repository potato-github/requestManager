package com.requestManager.data.user;

import lombok.Data;

/**
 * TODO
 *
 * @author chunxu.zhang
 * @since 2023/7/20
 **/
@Data
public class LoginReq {
    private String password;
    private String name;
}
