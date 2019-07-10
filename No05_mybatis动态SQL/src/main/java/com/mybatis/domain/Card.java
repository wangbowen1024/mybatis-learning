package com.mybatis.domain;

import java.util.List;

/**
 * Card class 用于包装User类
 *
 * @author BowenWang
 * @date 2019/07/10
 */
public class Card {
    private User user;
    private List<Integer> ids;

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
