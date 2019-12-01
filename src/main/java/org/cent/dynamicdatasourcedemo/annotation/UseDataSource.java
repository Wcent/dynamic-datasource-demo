package org.cent.dynamicdatasourcedemo.annotation;

import org.cent.dynamicdatasourcedemo.constant.DataSourceType;

import java.lang.annotation.*;

/**
 * 指定数据源注解
 */
@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UseDataSource {

    /**
     * 数据源类型
     * @return
     */
    DataSourceType type() default DataSourceType.MASTER;
}
