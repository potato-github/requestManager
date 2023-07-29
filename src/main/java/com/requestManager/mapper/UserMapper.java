package com.requestManager.mapper;

import com.requestManager.data.user.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * TODO
 *
 * @author chunxu.zhang
 * @since 2023/7/20
 **/
@Mapper
@Repository
public interface UserMapper {

    User queryByName(User user);

    void insert(User user);

    void update(User user);
}
