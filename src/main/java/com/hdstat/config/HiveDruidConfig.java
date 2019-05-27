package com.hdstat.config;

import javax.sql.DataSource;

import com.hdstat.config.prop.HiveConfigProp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
public class HiveDruidConfig {

    @Autowired
    private HiveConfigProp prop;

    private String url;
    private String user;
    private String password;
    private String driverClassName;
    private int initialSize;
    private int minIdle;
    private int maxActive;
    private int maxWait;
    private int timeBetweenEvictionRunsMillis;
    private int minEvictableIdleTimeMillis;
    private String validationQuery;
    private boolean testWhileIdle;
    private boolean testOnBorrow;
    private boolean testOnReturn;
    private boolean poolPreparedStatements;
    private int maxPoolPreparedStatementPerConnectionSize;

    @Bean(name = "hiveDruidDataSource")
    @Qualifier("hiveDruidDataSource")
    public DataSource dataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(prop.getUrl());
        datasource.setUsername(prop.getUser());
        datasource.setPassword(prop.getPassword());
        datasource.setDriverClassName(prop.getDriverClassName());

        // pool configuration
        datasource.setInitialSize(prop.getInitialSize());
        datasource.setMinIdle(prop.getMinIdle());
        datasource.setMaxActive(prop.getMaxActive());
        datasource.setMaxWait(prop.getMaxWait());
        datasource.setTimeBetweenEvictionRunsMillis(prop.getTimeBetweenEvictionRunsMillis());
        datasource.setMinEvictableIdleTimeMillis(prop.getMinEvictableIdleTimeMillis());
        datasource.setValidationQuery(prop.getValidationQuery());
        datasource.setTestWhileIdle(prop.isTestWhileIdle());
        datasource.setTestOnBorrow(prop.isTestOnBorrow());
        datasource.setTestOnReturn(prop.isTestOnReturn());
        datasource.setPoolPreparedStatements(prop.isPoolPreparedStatements());
        datasource.setMaxPoolPreparedStatementPerConnectionSize(prop.getMaxPoolPreparedStatementPerConnectionSize());
        return datasource;
    }

    // 此处省略各个属性的get和set方法

    @Bean(name = "hiveDruidTemplate")
    public JdbcTemplate hiveDruidTemplate(@Qualifier("hiveDruidDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}