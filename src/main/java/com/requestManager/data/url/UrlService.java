package com.requestManager.data.url;

import com.requestManager.data.token.TokeService;
import com.requestManager.mapper.UrlMapper;
import com.requestManager.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * url访问的配置
 *
 * @author chunxu.zhang
 * @since 2023/7/20
 **/
@Service
public class UrlService {

    @Autowired
    private UrlMapper urlMapper;
    @Autowired
    private TokeService tokeService;
    @Autowired
    private RedisService redisService;

    public void add(UrlReq req) {
        Url url = Url.builder().url(req.getUrl()).enable(req.getEnable()).count(req.getCount()).build();
        urlMapper.add(url);
    }

    public void update(UrlReq req) {
        Url url = Url.builder().url(req.getUrl()).enable(req.getEnable()).count(req.getCount()).build();
        urlMapper.update(url);
    }

    public void delete(UrlReq req) {
        Url url = Url.builder().id(req.getId()).build();
        urlMapper.delete(url);
    }

    public List<Url> select(UrlReq req) {
        Url url = Url.builder().url(req.getUrl()).enable(req.getEnable()).count(req.getCount()).build();
        List<Url> urlList = urlMapper.select(url);
        return urlList;
    }

}
