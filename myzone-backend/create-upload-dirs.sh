#!/bin/bash

echo "创建上传目录..."

mkdir -p uploads/videos uploads/images

echo "设置目录权限..."
chmod 755 uploads uploads/videos uploads/images

echo "上传目录创建完成！"
echo ""
echo "目录结构："
echo "uploads/"
echo "├── videos/"
echo "└── images/"
echo "" 