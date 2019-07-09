package com.mybatis.domain;

/**
 * Card class 用于包装User类
 *
 * @author BowenWang
 * @date 2019/07/09
 */
public class Card {
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
