package com.mybatis.dao;

import com.mybatis.domain.Article;

import java.util.List;

/**
 * ArticleDao class
 *
 * @author BowenWang
 * @date 2019/07/11
 */
public interface ArticleDao {

    /**
     * 获取每篇文章的作者信息
     * @return
     */
    List<Article> getArticlesInfo();

    /**
     * 根据ID查询文章
     * @param id
     * @return
     */
    Article findById(Integer id);
}
