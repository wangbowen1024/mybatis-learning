package com.mybatis.ibatis.session;

/**
 * SqlSessionFactory class
 *
 * @author BowenWang
 * @date 2019/07/08
 */
public interface SqlSessionFactory {
    SqlSession openSession();
}
