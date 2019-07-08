package com.mybatis.ibatis.utils;

import com.mybatis.ibatis.config.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DataSourceUtil class 数据源工具类
 * （这里应该使用数据库连接池技术，这里为方便操作直接JDBC连接）
 *
 * @author BowenWang
 * @date 2019/07/08
 */
public class DataSourceUtil {
    /**
     * 根据参数获取数据源
     * @return
     */
    public static Connection getConnection(Configuration configuration) {
        Connection connection = null;
        try {
            Class.forName(configuration.getDriver());
            connection = DriverManager.getConnection(configuration.getUrl(),
                    configuration.getUsername(),
                    configuration.getPassword());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
