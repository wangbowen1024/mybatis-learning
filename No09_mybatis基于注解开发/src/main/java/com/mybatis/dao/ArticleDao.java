package com.mybatis.dao;

import com.mybatis.domain.Article;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

/**
 * ArticleDao class
 *
 * @author BowenWang
 * @date 2019/07/13
 */
@CacheNamespace(blocking = true)
public interface ArticleDao {
    /**
     * 根据ID查找文章
     * @param id
     * @return
     */
    @Select("select * from article where aid = #{id}")
    @Results(id = "ArticleMap",value = {
            @Result(id = true, property = "id", column = "aid"),
            @Result(property = "title", column = "title"),
            @Result(property = "sid", column = "sid"),
            @Result(property = "student", column = "sid",
                    one = @One(select = "com.mybatis.dao.StudentDao.findById", fetchType = FetchType.EAGER))
    })
    Article findById(Integer id);

    /**
     * 根据SID查询
     * @param sid
     * @return
     */
    @Select("select * from article where sid = #{sid}")
    @ResultMap("ArticleMap")
    List<Article> findBySid(Integer sid);

    @Select("select * from article")
    @ResultMap("ArticleMap")
    List<Article> findArticleInfo();
}
