package com.mybatis.ibatis.session;

/**
 * SqlSession class 数据库操作接口
 *
 * @author BowenWang
 * @date 2019/07/08
 */
public interface SqlSession {
    /**
     * 根据参数创建一个代理对象
     * @param daoInterfaceClass
     * @param <T>
     * @return
     */
    <T> T getMapper(Class<T> daoInterfaceClass);

    /**
     * 释放资源
     */
    void close();
}
