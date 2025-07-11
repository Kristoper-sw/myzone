@echo off
echo 创建上传目录...

if not exist "uploads" mkdir uploads
if not exist "uploads\videos" mkdir uploads\videos
if not exist "uploads\images" mkdir uploads\images

echo 上传目录创建完成！
echo.
echo 目录结构：
echo uploads\
echo ├── videos\
echo └── images\
echo.
pause 