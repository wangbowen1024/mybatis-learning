package com.mybatis.ibatis.session.defaults;

import com.mybatis.ibatis.config.Configuration;
import com.mybatis.ibatis.session.SqlSession;
import com.mybatis.ibatis.session.proxy.MapperProxy;
import com.mybatis.ibatis.utils.DataSourceUtil;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DefaultSqlSession class
 *
 * @author BowenWang
 * @date 2019/07/08
 */
public class DefaultSqlSession implements SqlSession {

    private final Configuration configuration;
    private Connection connection;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
        // 获取到了配置信息，利用数据库连接池工具类获取连接
        connection = DataSourceUtil.getConnection(configuration);
    }

    public <T> T getMapper(Class<T> daoInterfaceClass) {
        // 将代理结果强制转换为需要的接口实现类返回
        return (T)Proxy.newProxyInstance(daoInterfaceClass.getClassLoader(),
                new Class[]{daoInterfaceClass},
                new MapperProxy(configuration.getMappers(), connection));
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
