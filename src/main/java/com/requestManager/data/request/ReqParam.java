package com.requestManager.data.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * TODO
 *
 * @author chunxu.zhang
 * @since 2023/7/21
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReqParam {
    private String url;
    private String jsonBody;
    private String method;
    private List<String> cookieList;
    private Boolean proxyFlag = false;
    private Boolean deleteFlag = false;


}
