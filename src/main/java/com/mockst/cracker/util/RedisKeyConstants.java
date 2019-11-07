package com.mockst.cracker.util;

/**
 * @Auther: zhiwei
 * @Date: 2019/8/8 23:05
 * @Description:
 */
public class RedisKeyConstants {

    public static final String USER_SESSION_KEY = "token:%s";

    public static String getRedisKey(String key, String value) {
        return String.format(key, value);
    }
}
