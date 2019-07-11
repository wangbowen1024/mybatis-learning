package com.mybatis.dao;

import com.mybatis.domain.Student;

import java.util.List;

/**
 * StudentDao class
 *
 * @author BowenWang
 * @date 2019/07/10
 */
public interface StudentDao {
    /**
     * 获取所有学生写的所有文章
     * @return
     */
    List<Student> getArticlesByStudents();

    /**
     * 根据ID查找学生
     * @param id
     * @return
     */
    Student findById(Integer id);
}
