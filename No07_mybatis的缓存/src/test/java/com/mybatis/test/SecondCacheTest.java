package com.mybatis.test;

import com.mybatis.dao.StudentDao;
import com.mybatis.domain.Student;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * StudentTest class
 *
 * @author BowenWang
 * @date 2019/07/11
 */
public class SecondCacheTest {
    private StudentDao studentDao;
    private InputStream in;
    private SqlSession sqlSession;
    private SqlSessionFactory sqlSessionFactory;
    @Before
    public void init() throws IOException {
        in = Resources.getResourceAsStream("SqlMapConfig.xml");
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
        sqlSession = sqlSessionFactory.openSession(true);
        studentDao = sqlSession.getMapper(StudentDao.class);
    }

    @After
    public void destroy() {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 测试二级缓存
     */
    @Test
    public void testSecondLevelCache() {
        SqlSession sqlSession1 = sqlSessionFactory.openSession();
        StudentDao studentDao1 = sqlSession1.getMapper(StudentDao.class);
        Student students1 = studentDao1.findById(1);
        System.out.println(students1);
        sqlSession1.close();

        SqlSession sqlSession2 = sqlSessionFactory.openSession();
        StudentDao studentDao2 = sqlSession2.getMapper(StudentDao.class);
        Student students2 = studentDao2.findById(1);
        System.out.println(students2);

        // 这里的false很简单，因为二级缓存的是数据
        System.out.println(students1 == students2);
    }


    /**
     * 测试二级缓存清空
     */
    @Test
    public void testCleanFirstLevelCache() {
        SqlSession sqlSession1 = sqlSessionFactory.openSession();
        StudentDao studentDao1 = sqlSession1.getMapper(StudentDao.class);
        Student students1 = studentDao1.findById(1);
        System.out.println(students1);
        sqlSession1.close();

        SqlSession sqlSession2 = sqlSessionFactory.openSession();
        StudentDao studentDao2 = sqlSession2.getMapper(StudentDao.class);

        Student student = new Student();
        student.setId(1);
        studentDao2.updateStudent(student);

        Student students2 = studentDao2.findById(1);
        System.out.println(students2);

        // 查看日志可以发现，增删改操作后。不论是一级还是二级缓存都会清空
        System.out.println(students1 == students2);
    }
}
