package com.requestManager.data.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * 入参
 *
 * @author chunxu.zhang
 * @since 2023/7/21
 **/
@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReqParam {
    private String url;
    private Object data;
    private int method;
    // 0-普通json，1-base64，2-json格式，3-表单格式
    private int type;
    private List<String> cookieList;
    private Boolean proxyFlag = false;
    private Boolean deleteFlag = false;
    private String header;
    private Boolean isRedirects;
    private MediaType mediaType;

    public HttpHeaders init() {
        HttpHeaders headers = new HttpHeaders();
        String header = this.getHeader();
        try {
            header = URLDecoder.decode(header, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("[入参转换] header编码异常，header：{}", header);
        }
        String[] split = header.split("\n");
        int i = 0;
        String key = Strings.EMPTY;
        for (String line : split) {
            if (i % 2 == 0) {
                key = line;
            } else {
                headers.add(key, line);
            }
            i++;
        }
        headers.setContentType(mediaType);
        return headers;
    }

}
