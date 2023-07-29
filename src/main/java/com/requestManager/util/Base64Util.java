package com.requestManager.util;

import org.springframework.util.Base64Utils;

/**
 * TODO
 *
 * @author chunxu.zhang
 * @since 2023/7/26
 **/
public class Base64Util {

    public static String encode(String msg) {
        return Base64Utils.encodeToString(msg.getBytes());
    }

    public static String decode(String encode) {
        byte[] decode = Base64Utils.decodeFromString(encode);return new String(decode);
    }

}