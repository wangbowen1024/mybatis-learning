package com.mybatis.dao;

import com.mybatis.domain.Card;
import com.mybatis.domain.User;

import java.util.List;

/**
 * UserDao class
 *
 * @author BowenWang
 * @date 2019/07/10
 */
public interface UserDao {

    /**
     * 根据IDS查询
     * @param card
     * @return
     */
    List<User> findUserByIds(Card card);
    /**
     * 查询所有
     * @param user
     * @return
     */
    List<User> findAll(User user);
}
