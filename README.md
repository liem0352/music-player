# 

> liem

## SpringBoot + MyBatis  API 

## |  |  |
|------|------|
| Spring Boot |  |
| MyBatis | ORM  |
| MySQL |  |
| Thymeleaf |  |
| Bootstrap |  UI  |
| jQuery |  |
| TweenLite |  |

## ### - `register.html`
- `login.html`
- 

### - `list.html`
- `upload.html`
- 
- `loveMusic.html`

### API
-  API `MusicApiController`
- API  `MUSIC_API_.md`

## ```
 src/main/
    java/com/example/demo/
       DemoApplication.java          # 
       controller/                   # 
          MusicController.java      # 
          MusicApiController.java   #  API 
          LoveMusicController.java  # 
          UserController.java       # 
       mapper/                       # 
       model/                        # 
       service/                      # 
       interceptor/                  # 
       assist/                       # 
       tools/                        # 
    resources/
        mybatis/                      # Mapper XML
        static/                       # 
           css/                      # 
           js/                       # 
           fonts/                    # 
           images/                   # 
        application.yml               # 
 db.sql                                # 
 pom.xml                               # Maven 
 .md                      # 
 MUSIC_API_.md                  # API 
 .md                        # 
```

## ### 1. 

```sql
CREATE DATABASE music_player;
USE music_player;
--  db.sql 
```

### 2. 

 `src/main/resources/application.yml`

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/music_player
    username: root
    password: your_password
```

### 3. 

```bash
mvn spring-boot:run
```

 `DemoApplication.java` 

### 4. 

- http://localhost:8080/login.html
- http://localhost:8080/list.html
