package com.requestManager.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.web.client.RestTemplate;

/**
 * 解析url到ip的类
 *
 * @author chunxu.zhang
 * @since 2023/7/27
 **/
public class IPAnalysis implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    RestTemplate restTemplate;

    public String url = "http://zltiqu.pyhttp.taolop.com/getip?count=1&neek=7288&type=1&yys=0&port=1&sb=&mr=1&sep=1";

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        analysisIpUrl();
    }

    public void analysisIpUrl() {
        Object forObject = restTemplate.getForObject(url, Object.class);
    }
}
