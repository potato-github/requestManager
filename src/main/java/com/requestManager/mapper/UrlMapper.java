package com.requestManager.mapper;

import com.requestManager.data.url.Url;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * url访问的配置
 *
 * @author chunxu.zhang
 * @since 2023/7/20
 **/
@Mapper
@Repository
public interface UrlMapper {

    void add(Url url);

    void update(Url url);

    void delete(Url url);

    List<Url> select(Url url);
}
