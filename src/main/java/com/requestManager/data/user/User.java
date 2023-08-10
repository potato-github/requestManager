package com.requestManager.data.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO
 *
 * @author chunxu.zhang
 * @since 2023/7/20
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String password;
    private String name;
    // 0-无次数限制，默认是1，1小时可访问100次
    private int level = 1;
}
