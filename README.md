# MyZone 项目

一个基于 Spring Boot + Vue.js 的现代化Web应用项目。

## 项目结构

```
final_project/
├── myzone-backend/          # 后端项目 (Spring Boot)
├── myzone-frontend/         # 前端项目 (Vue.js)
├── start.bat               # Windows启动脚本
└── README.md               # 项目说明
```

## 技术栈

### 后端
- **Spring Boot 3.4.8**: 主框架
- **MyBatis 3.0.4**: ORM框架
- **MySQL**: 数据库
- **JWT**: 身份认证
- **BCrypt**: 密码加密

### 前端
- **Vue 3**: 前端框架
- **Element Plus**: UI组件库
- **Vue Router**: 路由管理
- **Axios**: HTTP客户端

## 快速开始

### 方法一：使用启动脚本（推荐）

1. 确保已安装必要的环境：
   - JDK 17+
   - Node.js 16+
   - MySQL 8.0+
   - Maven 3.6+

2. 配置数据库：
   ```sql
   -- 创建数据库
   CREATE DATABASE myzone DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   
   -- 执行初始化脚本
   -- 运行 myzone-backend/src/main/resources/sql/init.sql
   ```

3. 修改数据库配置：
   - 编辑 `myzone-backend/src/main/resources/application.yml`
   - 修改数据库连接信息

4. 双击运行 `start.bat`

### 方法二：手动启动

#### 启动后端
```bash
cd myzone-backend
mvn spring-boot:run
```

#### 启动前端
```bash
cd myzone-frontend
npm install
npm run serve
```

## 访问地址

- **后端API**: http://localhost:8080
- **前端页面**: http://localhost:3000

## 测试账号

系统预置了两个测试账号：

1. **管理员账号**
   - 用户名：admin
   - 密码：123456
   - 邮箱：admin@myzone.com

2. **测试账号**
   - 用户名：test
   - 密码：123456
   - 邮箱：test@myzone.com

## API接口

### 用户认证
- `POST /api/user/register` - 用户注册
- `POST /api/user/login` - 用户登录
- `GET /api/user/info` - 获取用户信息
- `GET /api/user/health` - 健康检查

## 功能特性

- ✅ 用户注册与登录
- ✅ JWT身份认证
- ✅ 密码加密存储
- ✅ 表单验证
- ✅ 路由守卫
- ✅ 响应式设计
- ✅ 用户状态管理

## 开发说明

### 端口配置
- 后端：8080
- 前端：3000

### 数据库配置
- 数据库名：myzone
- 字符集：utf8mb4
- 排序规则：utf8mb4_unicode_ci

### 环境要求
- JDK 17+
- Node.js 16+
- MySQL 8.0+
- Maven 3.6+

## 注意事项

1. 确保MySQL服务已启动
2. 确保数据库连接配置正确
3. 首次运行需要执行数据库初始化脚本
4. 前端会自动打开浏览器访问 http://localhost:3000

## 许可证

MIT License 

## Todo
4.增加视频第一帧作为封面功能