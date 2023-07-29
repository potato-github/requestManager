package com.requestManager.aspect;

import com.requestManager.data.user.User;

/**
 * TODO
 *
 * @author chunxu.zhang
 * @since 2023/7/21
 **/
public class UserThreadLocal {

    private static ThreadLocal<User> usertl = new ThreadLocal<>();

    public static void setUser(User user) {
        usertl.set(user);
    }

    public static User getUser() {
        return usertl.get();
    }

}
