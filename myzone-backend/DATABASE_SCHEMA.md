# 数据库表结构设计

## 内容表 (content)

用于存储用户上传的内容信息，包括短视频、图片、日记和位置信息。

### 表结构

```sql
CREATE TABLE content (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '内容ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    content_type INT NOT NULL COMMENT '内容类型：1-短视频，2-图片，3-混合',
    diary TEXT COMMENT '日记内容（可选）',
    video_path VARCHAR(500) COMMENT '视频文件路径',
    image_paths TEXT COMMENT '图片文件路径列表（JSON格式存储）',
    latitude DOUBLE COMMENT '纬度',
    longitude DOUBLE COMMENT '经度',
    location VARCHAR(255) COMMENT '位置描述',
    status INT DEFAULT 1 COMMENT '内容状态：0-草稿，1-已发布，2-已删除',
    like_count INT DEFAULT 0 COMMENT '点赞数',
    comment_count INT DEFAULT 0 COMMENT '评论数',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    INDEX idx_user_id (user_id),
    INDEX idx_content_type (content_type),
    INDEX idx_status (status),
    INDEX idx_create_time (create_time),
    INDEX idx_location (latitude, longitude),
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='内容表';
```

### 字段说明

- `id`: 主键，自增
- `user_id`: 外键，关联用户表
- `content_type`: 内容类型
  - 1: 短视频
  - 2: 图片
  - 3: 混合（视频+图片）
- `diary`: 日记内容，可选
- `video_path`: 视频文件在服务器上的存储路径
- `image_paths`: 图片文件路径的JSON数组，例如：`["uploads/images/20231201_123456_abc123.jpg", "uploads/images/20231201_123457_def456.png"]`
- `latitude`: 纬度，用于地图定位
- `longitude`: 经度，用于地图定位
- `location`: 位置描述，如"北京市朝阳区"
- `status`: 内容状态
  - 0: 草稿
  - 1: 已发布
  - 2: 已删除
- `like_count`: 点赞数量
- `comment_count`: 评论数量
- `create_time`: 创建时间
- `update_time`: 更新时间

### 索引设计

- `idx_user_id`: 用于快速查询用户的内容
- `idx_content_type`: 用于按内容类型筛选
- `idx_status`: 用于筛选已发布的内容
- `idx_create_time`: 用于按时间排序
- `idx_location`: 用于地理位置查询和地图展示

### 文件存储结构

```
uploads/
├── videos/
│   ├── 20231201_123456_abc123.mp4
│   └── 20231201_123457_def456.avi
└── images/
    ├── 20231201_123456_abc123.jpg
    ├── 20231201_123457_def456.png
    └── 20231201_123458_ghi789.gif
```

### 注意事项

1. 文件路径使用相对路径，便于迁移
2. 图片路径使用JSON格式存储，支持多张图片
3. 添加了外键约束，确保数据完整性
4. 使用utf8mb4字符集，支持emoji等特殊字符
5. 添加了适当的索引，提高查询性能
6. 位置信息支持地图展示功能
7. 地理位置索引支持范围查询 