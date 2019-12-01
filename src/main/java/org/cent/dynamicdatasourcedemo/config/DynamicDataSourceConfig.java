package org.cent.dynamicdatasourcedemo.config;

import com.alibaba.druid.pool.DruidAbstractDataSource;
import com.alibaba.druid.pool.DruidDataSource;
import com.sun.xml.internal.messaging.saaj.packaging.mime.MultipartDataSource;
import javafx.scene.chart.PieChart;
import org.apache.ibatis.session.SqlSessionFactory;
import org.cent.dynamicdatasourcedemo.constant.DataSourceType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据源配置
 * @author Vincent
 * @version 1.0 2019/11/23
 */
@Configuration
public class DynamicDataSourceConfig {

    // 防止循环依赖，但不知道为什么dynamicDataSource不能设置
    @Primary
    @Bean(name = "masterDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource masterDataSource() {
//        return DataSourceBuilder.create().build();
        return new DruidDataSource();
    }

    @Bean(name = "slaveDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.slave")
    public DataSource slaveDataSource() {
//        return DataSourceBuilder.create().build();
        return new DruidDataSource();
    }

//    @Primary // 实现AbstractRoutingDataSource加此注解会报错循环依赖，暂时不知道原因
    @Bean(name = "dynamicDataSource")
    public DataSource dataSource(@Qualifier("masterDataSource") DataSource masterDataSource,
                                 @Qualifier("slaveDataSource") DataSource slaveDataSource) {

        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        // 配置可用数据源
        Map<Object, Object> targetDataSource = new HashMap<>(2);
        targetDataSource.put(DataSourceType.MASTER.getType(), masterDataSource);
        targetDataSource.put(DataSourceType.SLAVE.getType(), slaveDataSource);
        dynamicDataSource.setTargetDataSources(targetDataSource);
        // 设置默认数据源
        dynamicDataSource.setDefaultTargetDataSource(masterDataSource);
//        dynamicDataSource.setDefaultTargetDataSource(slaveDataSource);
        dynamicDataSource.afterPropertiesSet();
        return dynamicDataSource;
    }

//    @Primary
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(
            @Qualifier("dynamicDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver()
                        .getResources("classpath:mapper/*.xml"));
        return factoryBean.getObject();
    }

//    @Primary
    @Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(
            @Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

//    @Primary
    @Bean(name = "transactionManager")
    public DataSourceTransactionManager transactionManager(
            @Qualifier("dynamicDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
