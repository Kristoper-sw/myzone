@echo off
echo 正在构建 MyZone 前端项目...
echo.

cd myzone-frontend

echo 安装依赖...
call npm install

if %ERRORLEVEL% NEQ 0 (
    echo ❌ 依赖安装失败
    pause
    exit /b 1
)

echo 构建项目...
call npm run build

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ✅ 构建成功！
    echo 📦 静态文件位置: myzone-frontend\dist\
    echo.
    echo 下一步：将 dist 文件夹上传到服务器
) else (
    echo.
    echo ❌ 构建失败，请检查错误信息
)

pause 