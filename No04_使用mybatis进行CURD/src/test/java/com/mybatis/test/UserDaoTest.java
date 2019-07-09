package com.mybatis.test;

import com.mybatis.dao.UserDao;
import com.mybatis.domain.Card;
import com.mybatis.domain.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * UserDaoTest class
 *
 * @author BowenWang
 * @date 2019/07/09
 */
public class UserDaoTest {

    private UserDao userDao;
    private InputStream in;
    private SqlSession sqlSession;
    @Before
    public void init() throws IOException {
        in = Resources.getResourceAsStream("SqlMapConfig.xml");
        sqlSession = new SqlSessionFactoryBuilder().build(in).openSession();
        userDao = sqlSession.getMapper(UserDao.class);
    }

    @After
    public void destroy() {
        // 提交事务
        sqlSession.commit();

        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 测试添加用户
     */
    @Test
    public void testSaveUser() {
        User user = new User();
        user.setUsername("张三");
        user.setSex("男");
        user.setAddress("北京");
        user.setBirthday(new Date());

        System.out.println("保存前：" + user);
        userDao.saveUser(user);
        System.out.println("保存后：" + user);

    }

    /**
     * 测试删除用户
     */
    @Test
    public void testDeleteUser() {
        userDao.deleteUser(49);
    }

    /**
     * 测试更新用户
     */
    @Test
    public void testUpdateUser() {
        User user = new User();
        user.setUsername("张三");
        user.setAddress("上海");

        userDao.updateUser(user);
    }

    /**
     * 测试根据Id查找用户
     */
    @Test
    public void testFindUserById() {
        User user = userDao.findUserById(41);
        System.out.println(user);
    }

    /**
     * 测试根据name模糊查找用户
     */
    @Test
    public void testFindUserByName() {
        /*List<User> userList = userDao.findUserByName("王");*/
        List<User> userList = userDao.findUserByName("%王%");
        for (User user : userList) {
            System.out.println(user);
        }
    }

    /**
     * 测试获取总记录条数
     */
    @Test
    public void testGetTotol() {
        System.out.println(userDao.getTotol());
    }

    /**
     * 测试通过User包装类作为参数查询用户
     */
    @Test
    public void testFindUserByCard() {
        Card card = new Card();
        User user = new User();
        user.setUsername("%王%");
        card.setUser(user);

        List<User> userList = userDao.findUserByCard(card);
        for (User u : userList) {
            System.out.println(u);
        }

    }
}
