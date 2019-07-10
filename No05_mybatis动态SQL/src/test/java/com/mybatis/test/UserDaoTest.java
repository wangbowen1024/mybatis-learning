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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * UserDaoTest class
 *
 * @author BowenWang
 * @date 2019/07/10
 */
public class UserDaoTest {

    private UserDao userDao;
    private InputStream in;
    private SqlSession sqlSession;
    @Before
    public void init() throws IOException {
        in = Resources.getResourceAsStream("SqlMapConfig.xml");
        sqlSession = new SqlSessionFactoryBuilder().build(in).openSession(true);
        userDao = sqlSession.getMapper(UserDao.class);
    }

    @After
    public void destroy() {
        // 提交事务
        //sqlSession.commit();

        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 测试查询用户集合
     */
    @Test
    public void testFindUserByIds() {
        Card card = new Card();
        List<Integer> list = new ArrayList<Integer>(3);
        list.add(48);
        list.add(49);
        list.add(50);
        card.setIds(list);

        List<User> userList = userDao.findUserByIds(card);
        for (User u : userList) {
            System.out.println(u);
        }

    }

    @Test
    public void testFindAll() {
        User user = new User();
        user.setUsername("%王%");
        user.setSex("女");

        List<User> all = userDao.findAll(user);
        for (User user1 : all) {
            System.out.println(user1);
        }

    }
}
