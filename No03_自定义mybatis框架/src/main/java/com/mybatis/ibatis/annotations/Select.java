package com.mybatis.ibatis.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Select class
 *
 * @author BowenWang
 * @date 2019/07/08
 */

// 保留时间：运行期
@Retention(RetentionPolicy.RUNTIME)
// 作用于方法上
@Target(ElementType.METHOD)
public @interface Select {
    /**
     * 配置的SQL语句
     * @return
     */
    String value();
}
