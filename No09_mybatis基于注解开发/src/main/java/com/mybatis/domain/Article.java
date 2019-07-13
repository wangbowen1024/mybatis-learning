package com.mybatis.domain;

import java.io.Serializable;

/**
 * Article class
 *
 * @author BowenWang
 * @date 2019/07/13
 */
public class Article implements Serializable {

    private Integer id;
    private String title;
    private Integer sid;
    private Student student;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", sid=" + sid +
                '}';
    }
}
