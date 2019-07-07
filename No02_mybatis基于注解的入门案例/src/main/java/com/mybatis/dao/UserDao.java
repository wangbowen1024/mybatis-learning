package com.mybatis.dao;

import com.mybatis.domain.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * UserDao 用户持久层接口
 *
 * @author BowenWang
 * @date 2019/07/07
 */
public interface UserDao {
    /**
     * 查询所有操作
     * @return
     */
    @Select("select * from user")
    List<User> findAll();
}
