package org.cent.dynamicdatasourcedemo.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源选择器类
 * @author Vincent
 * @version 1.0 2019/12/1
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    /**
     * 数据源持有者，线程副本保证线程安全，隔离不同线程上下文使用的动态数据源
     */
    private static final ThreadLocal<String> dataSourceContextHolder = new ThreadLocal<>();

    /**
     * 确定当前上下文设置使用的数据源
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return getDataSource();
    }

    /**
     * 设置当前线程要使用的数据源
     * @param type 数据源类型
     */
    public static void setDataSource(String type) {
        dataSourceContextHolder.set(type);
    }

    /**
     * 获取当前线程设置的数据源
     * @return 数据源类型
     */
    public static String getDataSource() {
        return dataSourceContextHolder.get();
    }

    /**
     * 清除当前线程上下文设置的数据源
     */
    public static void clear() {
        dataSourceContextHolder.remove();
    }
}
