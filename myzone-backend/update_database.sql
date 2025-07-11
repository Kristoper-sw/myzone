-- 数据库更新脚本 - 添加content表
-- 如果数据库不存在，先创建数据库
CREATE DATABASE IF NOT EXISTS myzone DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE myzone;

-- 检查content表是否存在，如果不存在则创建
CREATE TABLE IF NOT EXISTS `content` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '内容ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `content_type` INT NOT NULL COMMENT '内容类型：1-短视频，2-图片，3-混合',
    `diary` TEXT COMMENT '日记内容（可选）',
    `video_path` VARCHAR(500) COMMENT '视频文件路径',
    `image_paths` TEXT COMMENT '图片文件路径列表（JSON格式存储）',
    `latitude` DOUBLE COMMENT '纬度',
    `longitude` DOUBLE COMMENT '经度',
    `location` VARCHAR(255) COMMENT '位置描述',
    `status` INT DEFAULT 1 COMMENT '内容状态：0-草稿，1-已发布，2-已删除',
    `like_count` INT DEFAULT 0 COMMENT '点赞数',
    `comment_count` INT DEFAULT 0 COMMENT '评论数',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_content_type` (`content_type`),
    INDEX `idx_status` (`status`),
    INDEX `idx_create_time` (`create_time`),
    INDEX `idx_location` (`latitude`, `longitude`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='内容表';

-- 显示表结构
DESCRIBE content;

-- 显示表是否创建成功
SHOW TABLES LIKE 'content';

-- 显示表中的记录数
SELECT COUNT(*) as content_count FROM content; 