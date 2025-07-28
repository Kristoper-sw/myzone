-- 添加评论表和点赞表的SQL脚本
-- 如果数据库不存在，先创建数据库
CREATE DATABASE IF NOT EXISTS myzone DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE myzone;

-- 创建评论表
CREATE TABLE IF NOT EXISTS `comment` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '评论ID',
    `content_id` BIGINT NOT NULL COMMENT '内容ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `parent_id` BIGINT DEFAULT NULL COMMENT '父评论ID（为NULL表示一级评论）',
    `content` TEXT NOT NULL COMMENT '评论内容',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX `idx_content_id` (`content_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_parent_id` (`parent_id`),
    FOREIGN KEY (`content_id`) REFERENCES `content`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- 创建点赞表
CREATE TABLE IF NOT EXISTS `content_like` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '点赞ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `content_id` BIGINT NOT NULL COMMENT '内容ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
    UNIQUE KEY `uk_user_content` (`user_id`, `content_id`),
    INDEX `idx_content_id` (`content_id`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`content_id`) REFERENCES `content`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='内容点赞表';

-- 显示表结构
DESCRIBE comment;
DESCRIBE content_like;

-- 显示表是否创建成功
SHOW TABLES LIKE 'comment';
SHOW TABLES LIKE 'content_like'; 