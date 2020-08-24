package com.jt.anno;

import com.jt.enu.KEY_ENUM;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//定义一个查询的注解
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Cache_Find {
    String key()   default "";//接受用户的key值
    KEY_ENUM keyType() default KEY_ENUM.AUTO;
    int secondes() default 0;//用户失效
}
