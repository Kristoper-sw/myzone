# 数据库修复说明

## 问题描述

用户反馈上传内容后文件上传成功，但数据库中没有记录。经过检查发现，数据库初始化脚本中缺少了 `content` 表的创建语句。

## 解决方案

### 方法一：执行数据库更新脚本

```bash
mysql -u root -p < myzone-backend/update_database.sql
```

### 方法二：手动执行SQL

```sql
USE myzone;

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
```

## 验证修复

```sql
USE myzone;

-- 查看表是否存在
SHOW TABLES LIKE 'content';

-- 查看表结构
DESCRIBE content;

-- 查看记录数
SELECT COUNT(*) as content_count FROM content;
```

## 功能测试

修复数据库后，可以测试以下功能：

1. **上传内容** - 上传视频、图片和日记
2. **查看我的内容** - 在"我的内容"页面查看上传的内容
3. **地图展示** - 在地图页面查看带有位置信息的内容
4. **内容管理** - 删除、编辑内容

## 注意事项

1. 确保MySQL服务正在运行
2. 确保数据库连接配置正确
3. 确保有足够的磁盘空间存储上传的文件
4. 确保 `uploads/` 目录存在且有写入权限

## 相关文件

- `myzone-backend/src/main/resources/sql/init.sql` - 完整的数据库初始化脚本
- `myzone-backend/update_database.sql` - 数据库更新脚本
- `myzone-backend/src/main/resources/application.yml` - 数据库连接配置 