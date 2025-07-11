# MyZone 后端项目

## 项目简介

MyZone 是一个基于 Spring Boot + MyBatis + MySQL 的用户管理系统后端项目，提供用户注册、登录等功能。

## 技术栈

- **Spring Boot 3.4.8**: 主框架
- **MyBatis 3.0.4**: ORM框架
- **MySQL**: 数据库
- **JWT**: 身份认证
- **BCrypt**: 密码加密
- **Lombok**: 简化代码

## 项目结构

```
src/main/java/com/sxw/myzonebackend/
├── controller/          # 控制器层
│   └── UserController.java
├── service/            # 服务层
│   └── UserService.java
├── mapper/             # 数据访问层
│   └── UserMapper.java
├── entity/             # 实体类
│   └── User.java
├── dto/                # 数据传输对象
│   ├── LoginRequest.java
│   ├── LoginResponse.java
│   └── RegisterRequest.java
├── common/             # 公共类
│   └── Result.java
├── util/               # 工具类
│   └── JwtUtil.java
└── exception/          # 异常处理
    └── GlobalExceptionHandler.java
```

## 环境要求

- JDK 17+
- MySQL 8.0+
- Maven 3.6+

## 快速开始

### 1. 数据库配置

1. 创建数据库：
```sql
CREATE DATABASE myzone DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 执行初始化脚本：
```sql
-- 执行 src/main/resources/sql/init.sql 文件
```

### 2. 配置文件

修改 `src/main/resources/application.yml` 中的数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/myzone?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

### 3. 启动项目

```bash
# 编译项目
mvn clean compile

# 运行项目
mvn spring-boot:run
```

项目启动后，访问 `http://localhost:8080/api/user/health` 检查服务状态。

## API 接口

### 用户注册

**POST** `/api/user/register`

请求体：
```json
{
    "username": "testuser",
    "password": "123456",
    "confirmPassword": "123456",
    "email": "test@example.com",
    "nickname": "测试用户",
    "phone": "13800138000"
}
```

### 用户登录

**POST** `/api/user/login`

请求体：
```json
{
    "username": "testuser",
    "password": "123456"
}
```

响应体：
```json
{
    "code": 200,
    "message": "登录成功",
    "data": {
        "userId": 1,
        "username": "testuser",
        "nickname": "测试用户",
        "email": "test@example.com",
        "avatar": null,
        "token": "eyJhbGciOiJIUzUxMiJ9...",
        "tokenType": "Bearer",
        "expiresIn": 86400000
    },
    "timestamp": 1640995200000
}
```

### 获取用户信息

**GET** `/api/user/info`

请求头：
```
Authorization: Bearer your_jwt_token
```

### 健康检查

**GET** `/api/user/health`

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

## 功能特性

- ✅ 用户注册（用户名、邮箱唯一性验证）
- ✅ 用户登录（密码加密验证）
- ✅ JWT 令牌认证
- ✅ 参数验证
- ✅ 全局异常处理
- ✅ 统一响应格式
- ✅ 跨域支持

## 开发说明

### 密码加密

使用 BCrypt 算法对用户密码进行加密存储，确保安全性。

### JWT 认证

- 令牌有效期：24小时
- 算法：HS512
- 包含信息：用户名、用户ID

### 数据库设计

- 用户表包含基本信息字段
- 支持用户名和邮箱唯一性约束
- 包含用户状态管理
- 自动记录创建和更新时间

## 注意事项

1. 请确保 MySQL 服务已启动
2. 修改数据库连接配置为您的实际配置
3. JWT 密钥建议在生产环境中使用更复杂的密钥
4. 建议在生产环境中配置 HTTPS

## 许可证

MIT License 