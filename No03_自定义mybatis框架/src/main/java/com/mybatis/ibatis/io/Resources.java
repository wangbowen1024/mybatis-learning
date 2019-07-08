package com.mybatis.ibatis.io;

import java.io.InputStream;

/**
 * Resources class 加载资源类
 *
 * @author BowenWang
 * @date 2019/07/08
 *
 */
public class Resources {
    public static InputStream getResourceAsStream(String configPath) {
        return Resources.class.getClassLoader().getResourceAsStream(configPath);
    }
}
