# 用户上传功能测试指南

## 功能概述

我已经成功实现了用户上传功能，包括：

1. **短视频上传** - 支持单个视频文件上传
2. **图片上传** - 支持多张图片上传
3. **混合上传** - 同时上传视频和图片
4. **日记功能** - 可选的文字描述

## 已实现的功能

### 后端功能
- ✅ Content实体类
- ✅ FileUploadUtil文件上传工具类
- ✅ ContentService内容服务
- ✅ ContentController API控制器
- ✅ 文件类型验证
- ✅ 文件大小限制（100MB）
- ✅ 唯一文件名生成
- ✅ 用户权限验证
- ✅ 文件存储管理

### 前端功能
- ✅ ContentUpload.vue上传组件
- ✅ UploadView.vue上传页面
- ✅ content.js API接口
- ✅ 拖拽上传支持
- ✅ 文件预览功能
- ✅ 上传进度显示
- ✅ 响应式设计

### 配置和文档
- ✅ 数据库表结构设计
- ✅ 应用配置文件
- ✅ 测试页面
- ✅ 使用说明文档

## 测试步骤

### 1. 启动后端服务

```bash
cd myzone-backend

# 创建上传目录
# Windows:
create-upload-dirs.bat

# Linux/Mac:
chmod +x create-upload-dirs.sh
./create-upload-dirs.sh

# 启动服务
mvn spring-boot:run
```

### 2. 启动前端服务

```bash
cd myzone-frontend
npm run serve
```

### 3. 测试上传功能

#### 方法一：使用前端页面
1. 访问 `http://localhost:8080`
2. 登录或注册用户
3. 点击导航栏的"上传"按钮
4. 选择内容类型（短视频/图片/混合）
5. 上传文件并添加日记
6. 点击"发布内容"

#### 方法二：使用测试页面
1. 访问 `http://localhost:8080/upload-test.html`
2. 选择内容类型
3. 上传文件
4. 查看上传结果

## API测试

### 上传内容
```bash
curl -X POST http://localhost:8080/api/content/upload \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -F "contentType=1" \
  -F "diary=今天很开心" \
  -F "videoFile=@/path/to/video.mp4"
```

### 获取用户内容
```bash
curl -X GET http://localhost:8080/api/content/user/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 文件存储结构

```
myzone-backend/
├── uploads/
│   ├── videos/
│   │   ├── 20231201_123456_abc123.mp4
│   │   └── 20231201_123457_def456.avi
│   └── images/
│       ├── 20231201_123456_abc123.jpg
│       ├── 20231201_123457_def456.png
│       └── 20231201_123458_ghi789.gif
```

## 支持的文件格式

### 视频格式
- MP4, AVI, MOV, WMV, FLV, MKV
- 最大文件大小：100MB

### 图片格式
- JPG, JPEG, PNG, GIF, BMP, WebP
- 最大文件大小：100MB

## 数据库表结构

需要创建内容表：

```sql
CREATE TABLE content (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '内容ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    content_type INT NOT NULL COMMENT '内容类型：1-短视频，2-图片，3-混合',
    diary TEXT COMMENT '日记内容（可选）',
    video_path VARCHAR(500) COMMENT '视频文件路径',
    image_paths TEXT COMMENT '图片文件路径列表（JSON格式存储）',
    status INT DEFAULT 1 COMMENT '内容状态：0-草稿，1-已发布，2-已删除',
    like_count INT DEFAULT 0 COMMENT '点赞数',
    comment_count INT DEFAULT 0 COMMENT '评论数',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    INDEX idx_user_id (user_id),
    INDEX idx_content_type (content_type),
    INDEX idx_status (status),
    INDEX idx_create_time (create_time),
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='内容表';
```

## 注意事项

1. **文件权限** - 确保应用有写入 `uploads/` 目录的权限
2. **磁盘空间** - 监控磁盘使用情况，定期清理临时文件
3. **数据库连接** - 确保MySQL服务正常运行
4. **JWT令牌** - 上传时需要有效的JWT认证令牌

## 常见问题

### Q: 上传失败，提示"文件类型不支持"
A: 检查文件扩展名是否在允许列表中，确保文件格式正确。

### Q: 上传失败，提示"文件大小超过限制"
A: 检查文件大小是否超过100MB限制。

### Q: 上传失败，提示"未提供认证令牌"
A: 确保用户已登录，JWT令牌有效。

### Q: 文件上传成功但无法访问
A: 检查文件存储路径是否正确，文件权限是否设置正确。

## 后续优化建议

1. **视频压缩** - 自动压缩视频文件，减少存储空间
2. **图片处理** - 生成缩略图，优化加载速度
3. **CDN加速** - 使用CDN分发静态文件
4. **断点续传** - 支持大文件断点续传
5. **批量上传** - 支持批量文件上传
6. **云存储** - 集成云存储服务（如阿里云OSS、腾讯云COS）

## 联系支持

如果在测试过程中遇到问题，请检查：
1. 后端服务是否正常启动
2. 数据库连接是否正常
3. 文件权限是否正确
4. 网络连接是否正常

功能已经完全实现并可以正常使用！ 