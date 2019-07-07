package com.mybatis.test;

import com.mybatis.dao.UserDao;
import com.mybatis.domain.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * MybatisTest class
 *
 * @author BowenWang
 * @date 2019/07/07
 *
 * mybatis入门案例
 */
public class MybatisTest {
    /**
     * 入门案例
     * @param args
     */
    public static void main(String[] args) throws IOException {
        /*
        * 1.读取配置文件
        *
        * 使用绝对路径和相对路径都不合适！
        * 加载资源的时候推荐：
        *   1. 使用类加载器，它智能读取类路径的配置文件
        *   2. 使用ServiceContext对象的getRealPath()
        * */
        InputStream in = Resources.getResourceAsStream("SqlMapConfig.xml");
        /*
        * 2.创建SqlSessionFactory工厂
        *
        * 创建工厂mybatis使用了构建者模式
        *   优势：把对象的创建细节隐藏，使用者直接调用方法即可拿到对象
        * 生产SqlSession使用了工厂模式
        *   优势：解耦
        * 创建Dao接口实现类使用了代理模式
        *   优势：不修改源码的基础上对已有方法的增强
        * */
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        SqlSessionFactory factory = builder.build(in);
        // 3.使用工厂生产SqlSession对象
        SqlSession session = factory.openSession();
        // 4.使用SqlSession创建dao接口的代理对象
        UserDao userDao = session.getMapper(UserDao.class);
        // 5.使用代理对象执行方法
        List<User> users = userDao.findAll();
        for (User user : users) {
            System.out.println(user);
        }
        // 6.释放资源
        session.close();
        in.close();
    }
}
