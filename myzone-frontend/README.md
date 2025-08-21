# MyZone Frontend Project | MyZone 前端项目

[English](#english) | [中文](#chinese)

---

## English

## Project Introduction

MyZone Frontend is a modern web application built with Vue 3, providing a user-friendly interface for the MyZone cultural sharing platform.

## Technology Stack

- **Vue 3**: Progressive JavaScript framework
- **Element Plus**: UI component library
- **Vue Router**: Official router for Vue.js
- **Axios**: HTTP client for API requests
- **Vue I18n**: Internationalization plugin
- **Mapbox GL**: Interactive maps

## Features

- ✅ User authentication and authorization
- ✅ Interactive map with cultural content
- ✅ Content upload and management
- ✅ Multi-language support (Chinese/English)
- ✅ Responsive design
- ✅ Real-time content sharing
- ✅ Comment and like system

## Project Setup

```bash
npm install
```

### Compiles and hot-reloads for development

```bash
npm run serve
```

### Compiles and minifies for production

```bash
npm run build
```

### Lints and fixes files

```bash
npm run lint
```

### Customize configuration

See [Configuration Reference](https://cli.vuejs.org/config/).

## Project Structure

```
src/
├── api/                 # API interfaces
├── assets/              # Static resources
├── components/          # Vue components
├── config/              # Configuration files
├── i18n/                # Internationalization
├── plugins/             # Vue plugins
├── router/              # Route configuration
├── stores/              # State management
├── views/               # Page components
└── main.js              # Application entry
```

## Environment Requirements

- Node.js 16+
- npm 8+ or yarn 1.22+

## Development Notes

### Internationalization

The project supports both Chinese and English languages using Vue I18n.

### Map Integration

Uses Mapbox GL for interactive map functionality with cultural content markers.

### API Integration

Communicates with the backend API for user management and content operations.

## Notes

1. Ensure the backend service is running on port 8080
2. Configure API endpoints in `src/config/index.js`
3. Set up Mapbox access token for map functionality

## License

MIT License

---

## Chinese

## 项目简介

MyZone 前端是一个基于 Vue 3 构建的现代化 Web 应用程序，为 MyZone 文化分享平台提供用户友好的界面。

## 技术栈

- **Vue 3**: 渐进式 JavaScript 框架
- **Element Plus**: UI 组件库
- **Vue Router**: Vue.js 官方路由
- **Axios**: HTTP 客户端
- **Vue I18n**: 国际化插件
- **Mapbox GL**: 交互式地图

## 功能特性

- ✅ 用户认证和授权
- ✅ 交互式文化内容地图
- ✅ 内容上传和管理
- ✅ 多语言支持（中文/英文）
- ✅ 响应式设计
- ✅ 实时内容分享
- ✅ 评论和点赞系统

## 项目设置

```bash
npm install
```

### 编译和热重载用于开发

```bash
npm run serve
```

### 编译和压缩用于生产

```bash
npm run build
```

### 代码检查和修复

```bash
npm run lint
```

### 自定义配置

查看 [配置参考](https://cli.vuejs.org/config/)。

## 项目结构

```
src/
├── api/                 # API 接口
├── assets/              # 静态资源
├── components/          # Vue 组件
├── config/              # 配置文件
├── i18n/                # 国际化
├── plugins/             # Vue 插件
├── router/              # 路由配置
├── stores/              # 状态管理
├── views/               # 页面组件
└── main.js              # 应用入口
```

## 环境要求

- Node.js 16+
- npm 8+ 或 yarn 1.22+

## 开发说明

### 国际化

项目使用 Vue I18n 支持中文和英文双语。

### 地图集成

使用 Mapbox GL 实现交互式地图功能，支持文化内容标记。

### API 集成

与后端 API 通信，实现用户管理和内容操作。

## 注意事项

1. 确保后端服务在 8080 端口运行
2. 在 `src/config/index.js` 中配置 API 端点
3. 设置 Mapbox 访问令牌以启用地图功能

## 许可证

MIT License
