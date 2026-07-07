@echo off
chcp 65001 >nul
title 音乐播放器后端服务 - 微信小程序专用

echo ========================================
echo   🎵 音乐播放器后端服务启动器
echo   端口: 8081
echo ========================================
echo.

:: 检查Java环境
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ 错误：未检测到Java环境！
    echo 请先安装JDK 1.8或更高版本
    pause
    exit /b 1
)

echo ✅ Java环境检测通过
echo.

:: 检查Maven环境
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ⚠️ 警告：未检测到Maven，尝试直接运行jar包...
    
    :: 检查是否有已编译的jar包
    if exist "target\demo-0.0.1-SNAPSHOT.jar" (
        echo 📦 找到已编译的jar包，正在启动...
        java -jar target\demo-0.0.1-SNAPSHOT.jar
    ) else (
        echo ❌ 错误：未找到jar文件，请先使用IDE或Maven编译项目
        echo.
        echo 推荐方式：
        echo   1. 使用IntelliJ IDEA打开项目
        echo   2. 运行 DemoApplication.java 主类
        echo.
        pause
    )
) else (
    echo ✅ Maven环境检测通过
    echo.
    echo 🔨 正在编译项目（首次可能需要几分钟）...
    echo.
    
    mvn clean package -DskipTests
    
    if %errorlevel% equ 0 (
        echo.
        echo ✅ 编译成功！正在启动服务...
        echo.
        
        :: 启动服务
        java -jar target\demo-0.0.1-SNAPSHOT.jar
        
    ) else (
        echo.
        echo ❌ 编译失败！请检查代码或依赖问题
        pause
    )
)

pause
