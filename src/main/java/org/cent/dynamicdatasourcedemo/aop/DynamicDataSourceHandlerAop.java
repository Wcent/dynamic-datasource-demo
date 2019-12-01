package org.cent.dynamicdatasourcedemo.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.cent.dynamicdatasourcedemo.annotation.UseDataSource;
import org.cent.dynamicdatasourcedemo.config.DynamicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 数据源处理器，使用前设置，使用后清除
 * @author Vincent
 * @version 1.0 2019/12/1
 */
@Aspect
@Component
@Order(-1) // 保证在开启事务前先切数据源
public class DynamicDataSourceHandlerAop {

    private static final Logger log = LoggerFactory.getLogger(DynamicDataSourceHandlerAop.class);

    // 拦截注解，@within用于拦截类标注注解，但接口或类好像无效
    @Pointcut("@within(org.cent.dynamicdatasourcedemo.annotation.UseDataSource) ||" +
              "@annotation(org.cent.dynamicdatasourcedemo.annotation.UseDataSource)")
    public void pointcut() {
    }

    @Before("pointcut() && @annotation(useDataSource)")
    public void setDataSource(JoinPoint joinPoint, UseDataSource useDataSource) {

        String methodName = joinPoint.getSignature().getName();
        String clsName = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String type = useDataSource.type().getType();
        if (!StringUtils.isEmpty(type)) {
            DynamicDataSource.setDataSource(type);
            log.info("动态切换数据源===>>>类：{}， 方法：{}，数据源：{}", clsName, methodName, type);
        }
    }

    @After("pointcut() && @annotation(useDataSource)")
    public void removeDataSource(JoinPoint joinPoint, UseDataSource useDataSource) {
        DynamicDataSource.clear();
        String methodName = joinPoint.getSignature().getName();
        String clsName = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String type = useDataSource.type().getType();
        log.info("清除数据源===>>>类：{}， 方法：{}，数据源：{}", clsName, methodName, type);
    }
}
