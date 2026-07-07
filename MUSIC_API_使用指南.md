# 🎵 音乐API完整部署指南

## ✅ 已完成的工作

我已经帮你完成了**所有配置和代码集成**！以下是创建/修改的文件：

### **后端代码（music-player项目）**

| 文件 | 位置 | 说明 |
|------|------|------|
| [MusicApiController.java](src/main/java/com/example/demo/controller/MusicApiController.java) | `controller/` | API控制器，提供4个接口 |
| [IMusicApiService.java](src/main/java/com/example/demo/service/IMusicApiService.java) | `service/` | 服务接口定义 |
| [MusicApiServiceImpl.java](src/main/java/com/example/demo/service/impl/MusicApiServiceImpl.java) | `service/impl/` | ⭐核心实现，多源音源匹配 |
| [启动音乐API.bat](启动音乐API.bat) | 项目根目录 | 一键启动脚本 |
| [music-api-test.html](music-api-test.html) | 项目根目录 | API测试工具（浏览器打开） |

### **小程序端代码**

| 文件 | 修改内容 |
|------|---------|
| [musicApi.js](../../../微信小程序/miniprogram-1/utils/musicApi.js) | baseUrl已改为 `http://localhost:8081` |

---

## 🚀 快速开始（3步搞定）

### **第1步：启动后端服务**

#### 方式A：使用IDE运行（推荐）

1. 用 **IntelliJ IDEA** 打开项目：
   ```
   d:\文件\工作 作业\music-player--master
   ```

2. 找到主类并运行：
   ```
   src/main/java/com/example/demo/DemoApplication.java
   ```

3. 右键 → **Run 'DemoApplication'**
   
4. 看到以下日志说明启动成功：
   ```
   Started DemoApplication in x.x seconds
   Tomcat started on port(s): 8081 (http)
   ```

#### 方式B：使用启动脚本

双击运行：
```
d:\文件\工作 作业\music-player--master\启动音乐API.bat
```

脚本会自动：
- ✅ 检查Java环境
- ✅ Maven编译项目
- ✅ 启动SpringBoot服务

#### 方式C：命令行手动启动

```bash
cd "d:\文件\工作 作业\music-player--master"

# 编译
mvn clean package -DskipTests

# 运行
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

---

### **第2步：验证API是否正常**

#### 方法1：使用测试页面（推荐）

用浏览器打开：
```
d:\文件\工作 作业\music-player--master\music-api-test.html
```

功能：
- ✅ 自动检测连接状态
- ✅ 点击歌曲即可获取URL并试听
- ✅ 手动测试各种API接口
- ✅ 显示详细的请求和响应信息

#### 方法2：直接访问API

在浏览器或Postman中访问：

```bash
# 健康检查
GET http://localhost:8081/api/music/health

# 获取晴天URL（周杰伦）
GET http://localhost:8081/api/music/url/186016

# 批量获取
GET http://localhost:8081/api/music/urls?ids=186016,185868,287709
```

**成功响应示例：**
```json
{
    "code": 200,
    "msg": "获取成功",
    "data": "https://xxx.xxx.com/xxx.mp3"
}
```

---

### **第3步：在小程序中播放**

#### 配置微信开发者工具

1. 打开微信开发者工具

2. 勾选不校验域名：
   ```
   详情 → 本地设置 → ☑️ 不校验合法域名、web-view...
   ```

3. 确认小程序已加载最新代码（应该自动热重载）

4. 点击音乐页面的任意一首歌！

**控制台日志应显示：**
```
🎵 正在获取: 晴天 - 周杰伦
✨ [真实音乐] 将通过后端API获取
🔄 请求后端API: http://localhost:8081/api/music/url/186016
📡 后端响应: 200 {code: 200, ...}
✅ 后端返回URL成功
📦 使用缓存的URL
```

---

## 🎵 内置17首中文歌

所有歌曲都已配置好，点击即可播放真正的原声！

### **🎤 周杰伦精选（4首）**
1. ☀️ 晴天 - ID: 186016
2. 🌾 稻香 - ID: 185868
3. 🏺 青花瓷 - ID: 185868
4. 🍃 七里香 - ID: 186017

### **🎸 民谣治愈（4首）**
5. 🛣️ 平凡之路 - 朴树 - ID: 287709
6. 🏠 成都 - 赵雷 - ID: 444809640
7. ⛰️ 南山南 - 马頔 - ID: 27599897
8. 💃 董小姐 - 宋冬野 - ID: 22437204

### **🔥 热门金曲（4首）**
9. ✨ 光年之外 - 邓紫棋 - ID: 528365698
10. 🔥 孤勇者 - 陈奕迅 - ID: 1892528906
11. 🍃 起风了 - 买辣椒也用券 - ID: 1330348068
12. ❄️ 漠河舞厅 - 柳爽 - ID: 1800720666

### **🏮 国风古韵（3首）**
13. ❄️ 凉凉 - 张碧晨/杨宗纬 - ID: 459189067
14. 🐋 大鱼 - 周深 - ID: 418592218
15. 🏮 卷珠帘 - 霍尊 - ID: 29777968

### **✊ 摇滚经典（2首）**
16. ✊ 光辉岁月 - Beyond - ID: 109545
17. 🌊 海阔天空 - Beyond - ID: 109497

---

## 🔧 技术架构

### **系统流程**

```
┌─────────────┐     ┌──────────────────┐     ┌─────────────────┐
│  微信小程序   │────▶│  SpringBoot后端   │────▶│ 外部音乐平台      │
│             │     │ (localhost:8081) │     │                 │
└─────────────┘     └──────────────────┘     └─────────────────┘
      │                      │                        │
      │  1. 用户点击播放       │                        │
      │─────────────────────▶│                        │
      │                      │  2. 调用UNM API         │
      │                      │───────────────────────▶│
      │                      │                        │
      │                      │  3. 返回音乐URL          │
      │                      │◀───────────────────────│
      │                      │                        │
      │  4. 返回URL给小程序    │                        │
      │◀──────────────────────│                        │
      │                      │                        │
      │  5. 小程序播放音频     │                        │
      └─────────────────────>│                        │
```

### **多源降级策略**

```
优先级1：ToolKal API (api.injahow.cn)
    ↓ 失败
优先级2：Vercel增强版 (neteasecloudmusicapi.vercel.app)
    ↓ 失败
优先级3：UNM Utils (unblockneteasemusic-utils.vercel.app)
    ↓ 全部失败
备用方案：网易云音乐外链 (music.163.com)
```

---

## ❓ 常见问题排查

### **Q1: 后端启动失败？**

**可能原因及解决：**

1. **端口被占用**
   ```bash
   # 查看占用8081端口的进程
   netstat -ano | findstr :8081
   
   # 结束进程（PID替换为实际值）
   taskkill /PID <PID> /F
   ```

2. **Java版本不对**
   - 需要JDK 1.8或更高
   - 检查命令：`java -version`
   - 如果是JDK 11+，需要在pom.xml添加javax.servlet依赖

3. **数据库连接失败**
   - 项目需要MySQL数据库
   - 检查 `application-dev.yml` 中的数据库配置
   - 确保MySQL已启动且数据库已创建

4. **Maven依赖下载慢**
   - 配置阿里云镜像加速
   - 在settings.xml中添加：
   ```xml
   <mirror>
       <id>aliyunmaven</id>
       <mirrorOf>*</mirrorOf>
       <name>阿里云公共仓库</name>
       <url>https://maven.aliyun.com/repository/public</url>
   </mirror>
   ```

---

### **Q2: 小程序请求失败？**

**错误现象：** `request:fail` 或 `net::ERR_CONNECTION_REFUSED`

**解决方法：**

1. **确认后端已启动**
   - 浏览器访问：`http://localhost:8081/api/music/health`
   - 应该返回JSON数据

2. **勾选"不校验合法域名"**
   - 微信开发者工具 → 详情 → 本地设置
   - ☑️ 不校验合法域名、web-view...

3. **检查baseUrl是否正确**
   - 文件：`miniprogram-1/utils/musicApi.js`
   - 第16行：`this.baseUrl = 'http://localhost:8081';`

---

### **Q3: 歌曲无法播放？**

**可能原因：**

1. **版权限制（部分歌曲变灰）**
   - 这是正常现象
   - 后端会尝试多个音源
   - 如果都失败会返回网易云备用链接

2. **音频格式不支持**
   - 后端返回的URL可能是.m4a/.mp3格式
   - 微信小程序原生支持这两种格式

3. **网络问题**
   - 检查网络连接
   - 尝试切换WiFi/移动网络

---

### **Q4: 如何添加新歌曲？**

编辑小程序端的 `musicApi.js`：

```javascript
// 在 initBuiltInSongs() 方法中添加
{
    id: 'cn-018',
    neteaseId: '新歌曲的网易云ID',  // 必填！
    title: '歌曲名',
    artist: '歌手名',
    album: '专辑名',
    duration: 时长秒数,
    cover: '/images/封面.jpg',
    genre: '流派',
    mood: '情绪',
    hot: true,
    isRealMusic: true,  // 标记为真实音乐
    lyrics: [
        { time: 0, text: '歌词...' }
    ]
}
```

**如何获取网易云ID？**
1. 打开网易云音乐网页版
2. 搜索歌曲
3. 点击分享 → 复制链接
4. URL中的数字就是ID，例如：`music?id=186016`

---

## 🌐 生产环境部署

### **部署到服务器**

1. **打包项目**
   ```bash
   mvn clean package -DskipTests
   ```

2. **上传jar包到服务器**
   ```bash
   scp target/demo-0.0.1-SNAPSHOT.jar user@server:/opt/music-api/
   ```

3. **SSH登录服务器**
   ```bash
   ssh user@server
   cd /opt/music-api
   ```

4. **运行服务**
   ```bash
   # 前台运行（测试用）
   java -jar demo-0.0.1-SNAPSHOT.jar
   
   # 后台运行（生产用）
   nohup java -jar demo-0.0.1-SNAPSHOT.jar > app.log 2>&1 &
   
   # 使用screen/tmux保持会话
   screen -S music-api
   java -jar demo-0.0.1-SNAPSHOT.jar
   # Ctrl+A+D 退出会话
   ```

5. **配置Nginx反向代理（推荐）**
   ```nginx
   server {
       listen 80;
       server_name your-domain.com;
       
       location / {
           proxy_pass http://127.0.0.1:8081;
           proxy_set_header Host $host;
           proxy_set_header X-Real-IP $remote_addr;
           proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
       }
   }
   ```

6. **更新小程序配置**
   ```javascript
   // musicApi.js 第16行
   this.baseUrl = 'https://your-domain.com';
   ```

7. **微信公众平台配置域名**
   - 登录 [mp.weixin.qq.com](https://mp.weixin.qq.com)
   - 开发管理 → 开发设置 → 服务器域名
   - request合法域名添加：`https://your-domain.com`

---

## 📊 性能优化建议

### **1. 启用缓存（推荐）**

后端已经实现了30分钟URL缓存。如需更长时间：

在 `MusicApiServiceImpl.java` 中修改：
```java
// 当前：30分钟
// 可以改为：1小时、2小时等
private static final long CACHE_DURATION = 30 * 60 * 1000;  // 30分钟
```

### **2. 使用Redis缓存（生产环境）**

添加Redis依赖：
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

### **3. CDN加速音频**

如果用户量大，可以将音频URL缓存到CDN。

---

## 🔒 安全建议

⚠️ **重要提示：**

1. **仅限个人学习使用**
   - 本项目仅供学习交流
   - 请支持正版音乐平台
   - 商用请联系相关版权方

2. **生产环境务必启用HTTPS**
   - 申请SSL证书
   - 配置Nginx HTTPS

3. **添加API鉴权（可选）**
   - 可以添加Token认证
   - 限制调用频率
   - 防止滥用

---

## 📝 技术栈总结

| 组件 | 技术 |
|------|------|
| **后端框架** | Spring Boot 2.6.12 |
| **Java版本** | JDK 1.8 |
| **ORM框架** | MyBatis |
| **工具库** | Hutool 5.8.8 |
| **HTTP客户端** | Hutool HttpUtil |
| **JSON处理** | Hutool JSONUtil |
| **前端框架** | 微信小程序原生 |
| **音乐源** | UnblockNeteaseMusic + 多平台 |

---

## 💡 下一步可以做什么？

1. ✅ **立即体验** - 启动后端，在小程序中播放音乐！
2. 🎨 **美化界面** - 自定义UI样式
3. 📱 **添加更多歌曲** - 编辑musicApi.js
4. 🚀 **部署上线** - 部署到服务器供他人使用
5. 🔍 **搜索功能** - 实现歌曲搜索（预留接口）
6. ❤️ **收藏功能** - 实现喜欢/收藏列表
7. 📊 **统计功能** - 记录播放历史

---

## 👍 支持

如果这个项目对你有帮助：

- ⭐ Star 支持一下
- 🐛 发现Bug请提Issue
- 💬 有问题欢迎讨论

---

## 🎉 开始使用吧！

**现在就按照上面的步骤操作：**

1. ✅ 启动后端服务（IDE或bat脚本）
2. ✅ 测试API是否正常（打开test.html）
3. ✅ 在小程序中播放音乐

**享受真正的中文歌吧！** 🎧🎵✨
