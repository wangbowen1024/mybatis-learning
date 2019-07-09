package com.mybatis.dao;

import com.mybatis.domain.Card;
import com.mybatis.domain.User;

import java.util.List;

/**
 * UserDao class
 *
 * @author BowenWang
 * @date 2019/07/09
 */
public interface UserDao {
    /**
     * 添加用户
     * @param user
     */
    void saveUser(User user);

    /**
     * 删除用户
     * @param id
     */
    void deleteUser(Integer id);

    /**
     * 更新用户
     * @param user
     */
    void updateUser(User user);

    /**
     * 根据Id查询用户
     * @param id
     * @return
     */
    User findUserById(Integer id);

    /**
     * 根据用户名字查询
     *
     * @param username
     * @return
     */
    List<User> findUserByName(String username);

    /**
     * 获取总记录条数
     * @return
     */
    int getTotol();

    /**
     * 根据包装类查询用户
     * @param card
     * @return
     */
    List<User> findUserByCard(Card card);
}
