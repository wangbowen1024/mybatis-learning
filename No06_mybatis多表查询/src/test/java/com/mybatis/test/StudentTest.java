package com.mybatis.test;

import com.mybatis.dao.StudentDao;
import com.mybatis.domain.Article;
import com.mybatis.domain.Student;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
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
 * @date 2019/07/10
 */
public class StudentTest {
    private StudentDao studentDao;
    private InputStream in;
    private SqlSession sqlSession;
    @Before
    public void init() throws IOException {
        in = Resources.getResourceAsStream("SqlMapConfig.xml");
        sqlSession = new SqlSessionFactoryBuilder().build(in).openSession(true);
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
     * 测试查询学生所有文章
     */
    @Test
    public void testFindAll() {
        List<Student> students = studentDao.getArticlesByStudents();
        for (Student student : students) {
            System.out.println("----------------------------");
            System.out.println(student);
            for (Article article : student.getArticles()) {
                System.out.println(article);
            }
        }

    }

}
