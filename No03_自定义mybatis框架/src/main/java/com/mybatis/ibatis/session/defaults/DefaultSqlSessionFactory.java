package com.mybatis.ibatis.session.defaults;

import com.mybatis.ibatis.config.Configuration;
import com.mybatis.ibatis.session.SqlSession;
import com.mybatis.ibatis.session.SqlSessionFactory;

/**
 * DefaultSqlSessionFactory class 默认工厂实现类
 *
 * @author BowenWang
 * @date 2019/07/08
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * 创建一个新的数据库操作对象
     * @return
     */
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}
