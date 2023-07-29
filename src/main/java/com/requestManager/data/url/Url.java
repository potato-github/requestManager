package com.requestManager.data.url;

import lombok.Builder;
import lombok.Data;

/**
 * url访问的配置
 *
 * @author chunxu.zhang
 * @since 2023/7/20
 **/
@Data
@Builder
public class Url {
    private Long id;
    private String url;
    private int count;
    private int enable;
}
