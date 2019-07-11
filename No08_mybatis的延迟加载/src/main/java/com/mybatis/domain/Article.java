package com.mybatis.domain;

import java.io.Serializable;

/**
 * Artcle class
 *
 * @author BowenWang
 * @date 2019/07/10
 */
public class Article implements Serializable {
    private int id;
    private String title;
    private int sid;
    /**
     * 一个文章的作者只能一个学生(即，一对一。多对一在Mybatis中也是看成一对一)
     */
    private Student student;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
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
