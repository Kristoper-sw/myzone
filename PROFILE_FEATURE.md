# 个人资料功能说明

## 功能概述

个人资料功能允许用户查看和编辑自己的个人信息，包括昵称、邮箱、头像和密码等。

## 后端API

### 1. 获取用户信息
- **接口**: `GET /api/user/info`
- **认证**: 需要Bearer Token
- **响应**: 返回用户信息（不包含密码）
- **响应格式**: `Result<User>`

### 2. 更新用户信息
- **接口**: `PUT /api/user/update`
- **认证**: 需要Bearer Token
- **请求体**: UpdateUserRequest
- **功能**: 更新用户基本信息，可选择更新密码
- **响应格式**: `Result<String>`

## 前端页面

### 个人资料页面 (`/profile`)
- **访问权限**: 需要登录
- **功能**:
  - 显示当前用户信息
  - 编辑昵称、邮箱
  - 更换头像（URL方式）
  - 修改密码（可选）
  - 表单验证
  - 实时预览

## 数据模型

### UpdateUserRequest
```java
public class UpdateUserRequest {
    private String nickname;        // 昵称（必填）
    private String email;           // 邮箱（可选）
    private String avatar;          // 头像URL（可选）
    private String newPassword;     // 新密码（可选）
    private String confirmPassword; // 确认密码（可选）
    private String currentPassword; // 当前密码（必填）
}
```

### Result<T> (通用响应类)
```java
public class Result<T> {
    private Integer code;      // 响应码
    private String message;    // 响应消息
    private T data;           // 响应数据
    private Long timestamp;    // 时间戳
}
```

## 安全特性

1. **密码验证**: 修改信息需要验证当前密码
2. **邮箱唯一性**: 检查邮箱是否被其他用户使用
3. **密码加密**: 新密码使用BCrypt加密存储
4. **Token验证**: 所有操作都需要有效的JWT Token

## 使用流程

1. 用户登录后，点击导航栏头像下拉菜单
2. 选择"个人资料"进入编辑页面
3. 修改需要更新的信息
4. 输入当前密码验证身份
5. 如需修改密码，输入新密码和确认密码
6. 点击"保存修改"提交更新

## 错误处理

- 当前密码错误
- 邮箱已被其他用户使用
- 新密码与确认密码不一致
- 表单验证失败
- 网络连接错误

## 技术实现

### 后端
- Spring Boot + MyBatis
- JWT认证
- 事务管理
- 参数验证
- 统一响应格式 (Result<T>)

### 前端
- Vue 3 + Element Plus
- 响应式表单
- 实时验证
- 状态管理

## 测试

运行后端测试：
```bash
cd myzone-backend
mvn test
```

## 部署说明

1. 确保数据库连接正常
2. 后端服务运行在8080端口
3. 前端服务运行在3000端口
4. 检查CORS配置是否正确

## 后续优化

1. 文件上传功能（头像）
2. 邮箱验证功能
3. 手机号绑定
4. 第三方登录集成
5. 用户偏好设置 