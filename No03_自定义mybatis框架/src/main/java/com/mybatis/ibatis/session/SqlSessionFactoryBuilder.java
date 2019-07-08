package com.mybatis.ibatis.session;

import com.mybatis.ibatis.config.Configuration;
import com.mybatis.ibatis.session.defaults.DefaultSqlSessionFactory;
import com.mybatis.ibatis.utils.XMLConfigBuilder;

import java.io.InputStream;

/**
 * SqlSessionFactoryBuilder class 工厂构建者类
 *
 * @author BowenWang
 * @date 2019/07/08
 *
 */
public class SqlSessionFactoryBuilder {
    public SqlSessionFactory build(InputStream config) {
        /**
         * 对SqlMapperConfig.xml主配置文件进行解析，将结果封装到Configuration
         * 其中配置文件中主要包括数据库连接四大参数，和对应的映射文件
         * 根据映射文件继续解析xml文件，获取到命名空间+select标签的id组成Key
         * 其余的SQL语句和返回值类型封装成Mapper对象
         * 并用Map存储
         */
        Configuration configuration = XMLConfigBuilder.loadConfiguration(config);
        /**
         * 这个构建者的作用只是用来读取配置文件中的信息并封装到一个对象中
         * 最后将获取到的配置对象作为实际工厂类的构造参数,返回工厂实现类
         */
        return new DefaultSqlSessionFactory(configuration);
    }
}
