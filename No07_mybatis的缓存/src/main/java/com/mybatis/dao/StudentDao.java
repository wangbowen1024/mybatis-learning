package com.mybatis.dao;

import com.mybatis.domain.Student;

import java.util.List;

/**
 * StudentDao class
 *
 * @author BowenWang
 * @date 2019/07/11
 */
public interface StudentDao {


    /**
     * 根据ID查询学生
     * @param id
     * @return
     */
    Student findById(Integer id);

    /**
     * 更新用户
     * @param student
     */
    void updateStudent(Student student);
}
