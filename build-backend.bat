@echo off
echo 正在构建 MyZone 后端项目...
echo.

cd myzone-backend

echo 清理项目...
call mvnw.cmd clean

echo 构建项目...
call mvnw.cmd package -DskipTests

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ✅ 构建成功！
    echo 📦 JAR 文件位置: myzone-backend\target\myzone-backend-*.jar
    echo.
    echo 下一步：将 JAR 文件上传到服务器
) else (
    echo.
    echo ❌ 构建失败，请检查错误信息
)

pause 