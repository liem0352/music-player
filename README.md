<p align="center">
  <img src="./assets/readme/hero.svg" width="100%" alt="music-player 音乐播放器,基于 SpringBoot + MyBatis,支持上传、播放、收藏与 API">
</p>

# 音乐播放器

一个基于 SpringBoot + MyBatis 的音乐播放器系统。用户可以注册登录、上传音乐、在线播放、收藏喜欢的曲目；同时对外暴露一套音乐 API 接口，供第三方调用。

## 核心功能

- **用户模块**：注册、登录、登录拦截器校验
- **音乐模块**：列表展示、音乐上传、在线播放、收藏管理
- **音乐 API**：`MusicApiController` 对外接口，使用指南见 `MUSIC_API_使用指南.md`

## 技术栈

| 技术 | 用途 |
|------|------|
| Spring Boot | 基础框架 |
| MyBatis | ORM |
| MySQL | 数据库 |
| Thymeleaf | 模板引擎 |
| Bootstrap | 前端 UI |
| jQuery | 前端交互 |
| TweenLite | 动画效果 |

## 项目结构

```
├── src/main/
│   ├── java/com/example/demo/
│   │   ├── DemoApplication.java
│   │   ├── controller/
│   │   │   ├── MusicController.java        # 音乐页面
│   │   │   ├── MusicApiController.java     # 对外 API
│   │   │   ├── LoveMusicController.java    # 收藏
│   │   │   └── UserController.java         # 用户
│   │   ├── mapper/                         # 数据访问
│   │   ├── model/                          # 实体
│   │   ├── service/                        # 业务
│   │   ├── interceptor/                    # 登录拦截器
│   │   ├── assist/                         # 辅助工具
│   │   └── tools/                          # 工具类
│   └── resources/
│       ├── mybatis/                        # Mapper XML
│       └── static/{css,js,fonts,images}
├── db.sql                                  # 数据库脚本
├── pom.xml
├── 音乐播放器文档.md
├── MUSIC_API_使用指南.md
└── 项目运行截图.md
```

## 快速开始

1. 准备数据库
   ```sql
   CREATE DATABASE music_player;
   USE music_player;
   -- 执行 db.sql 脚本
   ```
2. 修改 `src/main/resources/application.yml` 中的数据库连接
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/music_player
       username: root
       password: your_password
   ```
3. 启动
   ```bash
   mvn spring-boot:run
   ```
4. 访问
   - 登录页面：http://localhost:8080/login.html
   - 音乐列表：http://localhost:8080/list.html

---

<p align="center"><sub>作者 liem · 音乐播放器</sub></p>
