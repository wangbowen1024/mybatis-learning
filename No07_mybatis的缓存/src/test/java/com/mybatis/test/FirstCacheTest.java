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
import java.util.List;

/**
 * StudentTest class
 *
 * @author BowenWang
 * @date 2019/07/11
 */
public class FirstCacheTest {
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
     * 测试一级缓存
     */
    @Test
    public void testFirstLevelCache() {
        SqlSession sqlSession1 = sqlSessionFactory.openSession();
        StudentDao studentDao1 = sqlSession1.getMapper(StudentDao.class);
        Student students1 = studentDao1.findById(1);
        System.out.println(students1);

        SqlSession sqlSession2 = sqlSessionFactory.openSession();
        StudentDao studentDao2 = sqlSession2.getMapper(StudentDao.class);
        Student students2 = studentDao2.findById(1);
        System.out.println(students2);

        System.out.println(students1 == students2);
    }

    /**
     * 测试一级缓存2
     */
    @Test
    public void testFirstLevelCache2() {

        Student students1 = studentDao.findById(1);
        System.out.println(students1);

        Student students2 = studentDao.findById(1);
        System.out.println(students2);

        System.out.println(students1 == students2);
    }

    /**
     * 测试一级缓存清空
     */
    @Test
    public void testCleanFirstLevelCache() {
        Student students1 = studentDao.findById(1);
        System.out.println(students1);

        Student student = new Student();
        student.setId(1);
        studentDao.updateStudent(student);

        Student students2 = studentDao.findById(1);
        System.out.println(students2);

        System.out.println(students1 == students2);
    }

    /**
     * 测试一级缓存清空2
     */
    @Test
    public void testCleanFirstLevelCache2() {
        Student students1 = studentDao.findById(1);
        System.out.println(students1);

        sqlSession.clearCache();

        Student students2 = studentDao.findById(1);
        System.out.println(students2);

        System.out.println(students1 == students2);
    }

}
