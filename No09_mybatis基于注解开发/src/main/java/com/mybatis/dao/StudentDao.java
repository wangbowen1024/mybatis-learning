package com.mybatis.dao;

import com.mybatis.domain.Student;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

/**
 * StudentDao class
 *
 * @author BowenWang
 * @date 2019/07/13
 */
public interface StudentDao {

    /**
     * 根据ID查找学生
     * @param id
     * @return
     */
    @Select("select * from student where sid = #{id}")
    @Results(id="studentMap", value = {
            @Result(id = true, property = "id", column = "sid"),
            @Result(property = "name", column = "sname"),
            @Result(property = "age", column = "age"),
            @Result(property = "articles", column = "sid",
                    many = @Many(select = "com.mybatis.dao.ArticleDao.findBySid", fetchType = FetchType.LAZY))
    })
    Student findById(Integer id);

    /**
     * 查询所有学生
     * @return
     */
    @Select("select * from student")
    @ResultMap("studentMap")
    List<Student> findAll();

    /**
     * 添加学生
     * @param student
     * @return
     */
    @Insert("insert into student values(#{id},#{name},#{age})")
    int saveStudent(Student student);

    /**
     * 计数
     * @return
     */
    @Select("select count(*) from student")
    int count();

    /**
     * 查询学生文章信息
     * @return
     */
    @Select("select * from student")
    @ResultMap("studentMap")
    List<Student> findStudentsInfo();
}
