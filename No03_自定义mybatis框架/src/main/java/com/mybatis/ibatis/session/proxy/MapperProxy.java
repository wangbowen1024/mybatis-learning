package com.mybatis.ibatis.session.proxy;

import com.mybatis.ibatis.config.Mapper;
import com.mybatis.ibatis.utils.Executor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Map;

/**
 * MapperProxy class
 *
 * @author BowenWang
 * @date 2019/07/08
 */
public class MapperProxy implements InvocationHandler {

    private Map<String, Mapper> mappers;
    private Connection connection;

    public MapperProxy(Map<String, Mapper> mappers, Connection connection) {
        this.mappers = mappers;
        this.connection = connection;
    }

    /**
     * 用于对方法进行增强的，我们的增强其实就是调用selectList方法
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 获取方法名：因为所有方法被调用时候都会被这拦截
        String methodName = method.getName();
        // 获取定义该方法的类名
        String className = method.getDeclaringClass().getName();
        // 组合，作为key
        String key = className + "." + methodName;
        // 查询
        Mapper mapper = this.mappers.get(key);
        if (mapper == null) {
            throw new IllegalArgumentException("传入参数有误");
        }
        /**
         * 这里仅仅以selectList为例，实际上还需要很多参数，查询类型等，如select还是insert等
         * 通过调用Execute工具类中方法，并判断查询类型，执行相对应的方法，返回不同的结果集
         */
        // 调用工具类执行查询所有
        return new Executor().selectList(mapper, connection);
    }
}
