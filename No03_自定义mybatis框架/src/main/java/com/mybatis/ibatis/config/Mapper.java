package com.mybatis.ibatis.config;

/**
 * Mapper class
 *
 * 用于封装配置文件中的SQL和返回值类型
 *
 * @author BowenWang
 * @date 2019/07/08
 */
public class Mapper {
    private String queryString;
    private String resultType;

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }
}
