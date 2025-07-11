# 用户上传功能说明

## 功能概述

用户上传功能允许用户上传短视频、图片和日记，支持以下内容类型：

1. **短视频** - 上传单个视频文件
2. **图片** - 上传多张图片
3. **混合** - 同时上传视频和多张图片

## 功能特性

### 支持的文件格式

#### 视频格式
- MP4, AVI, MOV, WMV, FLV, MKV
- 最大文件大小：100MB

#### 图片格式
- JPG, JPEG, PNG, GIF, BMP, WebP
- 支持多选上传
- 最大文件大小：100MB

### 用户体验

- **拖拽上传** - 支持拖拽文件到上传区域
- **文件预览** - 上传前可预览视频和图片
- **进度显示** - 实时显示上传进度
- **文件管理** - 可删除已选择的文件
- **日记功能** - 可选的文字描述

## 技术实现

### 后端架构

#### 核心组件

1. **Content实体类** - 内容数据模型
2. **FileUploadUtil** - 文件上传工具类
3. **ContentService** - 内容业务逻辑
4. **ContentController** - API接口控制器

#### 文件存储

- 文件存储在 `uploads/` 目录下
- 按类型分类：`uploads/videos/` 和 `uploads/images/`
- 文件名格式：`时间戳_随机字符串.扩展名`

#### 安全措施

- 文件类型验证
- 文件大小限制
- 唯一文件名生成
- 用户权限验证

### 前端架构

#### 核心组件

1. **ContentUpload.vue** - 上传组件
2. **UploadView.vue** - 上传页面
3. **content.js** - API接口

#### 用户界面

- 响应式设计，支持移动端
- 现代化UI设计
- 直观的操作流程
- 实时反馈

## API接口

### 上传内容

```
POST /api/content/upload
```

**请求参数：**
- `contentType` (必填) - 内容类型：1-短视频，2-图片，3-混合
- `diary` (可选) - 日记内容
- `videoFile` (可选) - 视频文件
- `imageFiles` (可选) - 图片文件列表

**响应：**
```json
{
  "code": 200,
  "message": "内容上传成功",
  "data": {
    "id": 123,
    "userId": 1,
    "contentType": 1,
    "diary": "今天很开心",
    "videoPath": "uploads/videos/20231201_123456_abc123.mp4",
    "imagePaths": null,
    "status": 1,
    "createTime": "2023-12-01T12:34:56"
  }
}
```

### 获取用户内容列表

```
GET /api/content/user/{userId}?page=1&size=10
```

### 获取内容详情

```
GET /api/content/{contentId}
```

### 删除内容

```
DELETE /api/content/{contentId}
```

### 更新内容状态

```
PUT /api/content/{contentId}/status?status=1
```

## 使用流程

### 1. 访问上传页面

用户登录后，点击导航栏的"上传"按钮，进入上传页面。

### 2. 选择内容类型

- **短视频** - 只能上传视频文件
- **图片** - 只能上传图片文件
- **混合** - 可以同时上传视频和图片

### 3. 上传文件

- 点击上传区域选择文件
- 或直接拖拽文件到上传区域
- 支持预览已选择的文件
- 可以删除不需要的文件

### 4. 添加日记（可选）

在文本框中输入日记内容，记录想法和感受。

### 5. 发布内容

点击"发布内容"按钮，系统会：
- 验证文件格式和大小
- 上传文件到服务器
- 保存内容信息到数据库
- 显示上传成功提示

## 数据库设计

### 内容表 (content)

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 主键，自增 |
| user_id | BIGINT | 用户ID，外键 |
| content_type | INT | 内容类型：1-短视频，2-图片，3-混合 |
| diary | TEXT | 日记内容 |
| video_path | VARCHAR(500) | 视频文件路径 |
| image_paths | TEXT | 图片文件路径（JSON格式） |
| status | INT | 状态：0-草稿，1-已发布，2-已删除 |
| like_count | INT | 点赞数 |
| comment_count | INT | 评论数 |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |

## 部署说明

### 1. 创建上传目录

```bash
mkdir -p uploads/videos uploads/images
chmod 755 uploads uploads/videos uploads/images
```

### 2. 配置数据库

执行 `DATABASE_SCHEMA.md` 中的SQL语句创建内容表。

### 3. 配置应用

确保应用有写入 `uploads/` 目录的权限。

### 4. 启动服务

```bash
# 启动后端服务
cd myzone-backend
mvn spring-boot:run

# 启动前端服务
cd myzone-frontend
npm run serve
```

## 注意事项

1. **文件安全** - 定期清理临时文件，防止磁盘空间不足
2. **性能优化** - 大文件上传时考虑使用分片上传
3. **存储扩展** - 生产环境建议使用云存储服务
4. **备份策略** - 定期备份用户上传的文件
5. **监控告警** - 监控磁盘使用率和上传成功率

## 后续优化

1. **视频压缩** - 自动压缩视频文件，减少存储空间
2. **图片处理** - 生成缩略图，优化加载速度
3. **CDN加速** - 使用CDN分发静态文件
4. **断点续传** - 支持大文件断点续传
5. **批量上传** - 支持批量文件上传 