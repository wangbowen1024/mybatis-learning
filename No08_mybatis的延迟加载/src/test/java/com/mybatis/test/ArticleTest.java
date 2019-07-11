package com.mybatis.test;

import com.mybatis.dao.ArticleDao;
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
public class ArticleTest {
    private ArticleDao articleDao;
    private InputStream in;
    private SqlSession sqlSession;
    @Before
    public void init() throws IOException {
        in = Resources.getResourceAsStream("SqlMapConfig.xml");
        sqlSession = new SqlSessionFactoryBuilder().build(in).openSession(true);
        articleDao = sqlSession.getMapper(ArticleDao.class);
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
     * 测试查询所有文章所属作者的信息
     */
    @Test
    public void testFindAll() {
        List<Article> articles = articleDao.getArticlesInfo();
/*        for (Article article : articles) {
            System.out.println("----------------");
            System.out.println(article);
            System.out.println(article.getStudent());
        }*/
    }

}
