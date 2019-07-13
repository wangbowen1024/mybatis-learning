package com.mybatis.test;

import com.mybatis.dao.ArticleDao;
import com.mybatis.dao.StudentDao;
import com.mybatis.domain.Article;
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
 * ArticleTest class
 *
 * @author BowenWang
 * @date 2019/07/13
 */
public class ArticleTest {
    private InputStream in;
    private SqlSessionFactory sqlSessionFactory;
    private SqlSession sqlSession;
    private ArticleDao articleDao;

    @Before
    public void init() throws IOException {
        in = Resources.getResourceAsStream("SqlMapConfig.xml");
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
        sqlSession = sqlSessionFactory.openSession(true);
        articleDao = sqlSession.getMapper(ArticleDao.class);
    }

    @After
    public void destroy() throws IOException {
        in.close();
        sqlSession.close();
    }

    /**
     * 测试根据SID查找文章
     */
    @Test
    public void testFindById() {
        List<Article> articles = articleDao.findBySid(1);
        for (Article article : articles) {
            System.out.println(article);
        }
    }

    /**
     * 测试查询所有文章的作者信息
     */
    @Test
    public void testFindArticleInfo() {
        List<Article> articles = articleDao.findArticleInfo();
        for (Article article : articles) {
            System.out.println("-------------");
            System.out.println(article);
            System.out.println(article.getStudent());
        }
    }


}
