package com.sxw.myzonebackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * 数据库配置类
 */
@Configuration
@EnableTransactionManagement
public class DatabaseConfig {

    /**
     * 配置事务管理器
     */
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        // 设置事务超时时间（秒）
        transactionManager.setDefaultTimeout(30);
        // 设置回滚策略
        transactionManager.setRollbackOnCommitFailure(true);
        return transactionManager;
    }
} 