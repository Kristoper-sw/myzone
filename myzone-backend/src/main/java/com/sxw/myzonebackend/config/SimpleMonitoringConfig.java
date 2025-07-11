package com.sxw.myzonebackend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 简化的数据库监控配置类
 */
@Configuration
@EnableScheduling
public class SimpleMonitoringConfig {

    @Autowired
    private DataSource dataSource;

    /**
     * 定期监控数据库连接健康状态
     */
    @Scheduled(initialDelay = 30000, fixedRate = 120000) // 30秒后开始，每2分钟执行一次
    public void monitorDatabaseHealth() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(5)) {
                System.out.println("✅ 数据库连接状态: 正常");
            } else {
                System.out.println("❌ 数据库连接状态: 异常");
            }
        } catch (SQLException e) {
            System.out.println("❌ 数据库连接异常: " + e.getMessage());
        }
    }
} 