# 音乐播放器

> 作者：liem

## 项目简介

基于 SpringBoot + MyBatis 的音乐播放器系统，支持音乐上传、在线播放、收藏管理、用户注册登录等功能，同时提供音乐 API 接口供外部调用。

## 技术栈

| 技术 | 说明 |
|------|------|
| Spring Boot | 基础框架 |
| MyBatis | ORM 框架 |
| MySQL | 数据库 |
| Thymeleaf | 模板引擎 |
| Bootstrap | 前端 UI 框架 |
| jQuery | 前端交互 |
| TweenLite | 动画效果 |

## 功能模块

### 用户模块
- 用户注册（`register.html`）
- 用户登录（`login.html`）
- 登录拦截器校验

### 音乐模块
- 音乐列表展示（`list.html`）
- 音乐上传（`upload.html`）
- 在线播放
- 音乐收藏（`loveMusic.html`）

### 音乐 API
- 外部 API 接口（`MusicApiController`）
- API 使用指南见 `MUSIC_API_使用指南.md`

## 项目结构

```
├── src/main/
│   ├── java/com/example/demo/
│   │   ├── DemoApplication.java          # 主启动类
│   │   ├── controller/                   # 控制器
│   │   │   ├── MusicController.java      # 音乐页面控制器
│   │   │   ├── MusicApiController.java   # 音乐 API 控制器
│   │   │   ├── LoveMusicController.java  # 收藏控制器
│   │   │   └── UserController.java       # 用户控制器
│   │   ├── mapper/                       # 数据访问层
│   │   ├── model/                        # 实体类
│   │   ├── service/                      # 业务逻辑层
│   │   ├── interceptor/                  # 拦截器
│   │   ├── assist/                       # 辅助工具
│   │   └── tools/                        # 工具类
│   └── resources/
│       ├── mybatis/                      # Mapper XML
│       ├── static/                       # 静态资源
│       │   ├── css/                      # 样式文件
│       │   ├── js/                       # 脚本文件
│       │   ├── fonts/                    # 字体图标
│       │   └── images/                   # 图片资源
│       └── application.yml               # 配置文件
├── db.sql                                # 数据库脚本
├── pom.xml                               # Maven 配置
├── 音乐播放器文档.md                      # 项目文档
├── MUSIC_API_使用指南.md                  # API 使用指南
└── 项目运行截图.md                        # 运行截图
```

## 快速开始

### 1. 数据库准备

```sql
CREATE DATABASE music_player;
USE music_player;
-- 执行 db.sql 脚本
```

### 2. 修改配置

编辑 `src/main/resources/application.yml`，修改数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/music_player
    username: root
    password: your_password
```

### 3. 启动项目

```bash
mvn spring-boot:run
```

或运行 `DemoApplication.java` 主类。

### 4. 访问地址

- 登录页面：http://localhost:8080/login.html
- 音乐列表：http://localhost:8080/list.html
