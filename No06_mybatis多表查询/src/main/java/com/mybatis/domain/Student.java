package com.mybatis.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Student class
 *
 * @author BowenWang
 * @date 2019/07/10
 */
public class Student implements Serializable {
    private int id;
    private String name;
    private int age;
    /**
     * 一个学生可以写多篇文章（即，一对多）
     */
    private List<Article> articles;

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
