@echo off
echo ========================================
echo           MyZone 开发环境启动脚本
echo ========================================
echo.

echo 正在启动后端服务...
start "MyZone Backend" cmd /k "cd myzone-backend && mvnw.cmd spring-boot:run"

echo 等待后端服务启动...
timeout /t 10 /nobreak > nul

echo 正在启动前端服务...
start "MyZone Frontend" cmd /k "cd myzone-frontend && npm run serve"

echo.
echo ========================================
echo 开发环境启动完成！
echo.
echo 后端服务: http://localhost:8080
echo 前端服务: http://localhost:3000
echo.
echo 测试账号:
echo - 管理员: admin / 123456
echo - 测试用户: test / 123456
echo ========================================
echo.
pause 