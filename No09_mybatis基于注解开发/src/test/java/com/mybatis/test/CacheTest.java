package com.mybatis.test;

import com.mybatis.dao.ArticleDao;
import com.mybatis.domain.Article;
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
 * CacheTest class
 *
 * @author BowenWang
 * @date 2019/07/13
 */
public class CacheTest {
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
     * 测试二级缓存
     */
    @Test
    public void testFindById() {
        SqlSession sqlSession1 = sqlSessionFactory.openSession();
        ArticleDao articleDao1 = sqlSession1.getMapper(ArticleDao.class);
        Article article1 = articleDao1.findById(1);
        System.out.println(article1);
        sqlSession1.close();

        Article article = articleDao.findById(1);
        System.out.println(article);
    }



}
