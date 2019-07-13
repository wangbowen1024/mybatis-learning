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
 * @date 2019/07/13
 */
public class StudentTest {

    private InputStream in;
    private SqlSessionFactory sqlSessionFactory;
    private SqlSession sqlSession;
    private StudentDao studentDao;

    @Before
    public void init() throws IOException {
        in = Resources.getResourceAsStream("SqlMapConfig.xml");
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
        sqlSession = sqlSessionFactory.openSession(true);
        studentDao = sqlSession.getMapper(StudentDao.class);
    }

    @After
    public void destroy() throws IOException {
        in.close();
        sqlSession.close();
    }

    /**
     * 测试根据ID查找学生
     */
    @Test
    public void testFindById() {
        Student student = studentDao.findById(1);
        System.out.println(student);
    }

    /**
     * 测试查询所有学生
     */
    @Test
    public void testFindAll() {
        List<Student> students = studentDao.findAll();
        for (Student student : students) {
            System.out.println(student);
        }
    }

    /**
     * 测试添加学生
     */
    @Test
    public void testSaveStudent() {
        Student student = new Student();
        student.setId(6);
        student.setAge(30);
        student.setName("管理员");
        int rows = studentDao.saveStudent(student);
        System.out.println(rows);
    }

    /**
     * 测试计数
     */
    @Test
    public void testCount() {
        int count = studentDao.count();
        System.out.println(count);
    }

    /**
     * 测试查找学生写的文章
     */
    @Test
    public void testFindStudentsInfo() {
        List<Student> students = studentDao.findStudentsInfo();
        for (Student student : students) {
            System.out.println("-----------------------");
            System.out.println(student);
            System.out.println(student.getArticles());
        }
    }
}
