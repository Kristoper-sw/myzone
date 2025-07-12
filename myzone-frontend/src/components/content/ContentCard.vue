<template>
  <div class="content-card">
    <!-- 内容预览 -->
    <div class="content-preview">
      <video 
        v-if="content.videoPath" 
        :src="getFileUrl(content.videoPath)" 
        class="preview-media" 
        controls
      />
      <img 
        v-else-if="getFirstImage(content.imagePaths)" 
        :src="getFileUrl(getFirstImage(content.imagePaths))" 
        class="preview-media" 
      />
      <div v-else class="no-media">
        <el-icon :size="48"><Picture /></el-icon>
        <p>无媒体文件</p>
      </div>
    </div>
    
    <!-- 内容信息 -->
    <div class="content-info">
      <div class="content-header">
        <el-tag :type="getContentTypeTag(content.contentType)">
          {{ getContentTypeText(content.contentType) }}
        </el-tag>
        <el-tag :type="getStatusTag(content.status)">
          {{ getStatusText(content.status) }}
        </el-tag>
      </div>
      
      <div class="content-diary" v-if="content.diary">
        <p>{{ content.diary }}</p>
      </div>
      
      <div class="content-location" v-if="content.location">
        <el-icon><Location /></el-icon>
        <span>{{ content.location }}</span>
      </div>
      
      <div class="content-stats">
        <span>❤️ {{ content.likeCount || 0 }}</span>
        <span>💬 {{ content.commentCount || 0 }}</span>
      </div>
      
      <div class="content-time">
        {{ formatTime(content.createTime) }}
      </div>
    </div>
    
    <!-- 操作按钮 -->
    <div class="content-actions">
      <el-button size="small" @click="$emit('view', content)">查看</el-button>
      <el-button size="small" type="primary" @click="$emit('edit', content)">编辑</el-button>
      <el-button size="small" type="danger" @click="$emit('delete', content)">删除</el-button>
    </div>
  </div>
</template>

<script setup>
import { Picture, Location } from '@element-plus/icons-vue'

// 定义props
defineProps({
  content: {
    type: Object,
    required: true
  }
})

// 定义emits
defineEmits(['view', 'edit', 'delete'])

// 获取文件URL
const getFileUrl = (path) => {
  if (!path) return ''
  // 如果路径已经以http开头，直接返回
  if (path.startsWith('http')) {
    return path
  }
  // 如果路径以/开头，直接拼接baseURL
  if (path.startsWith('/')) {
    return `http://localhost:8080${path}`
  }
  // 否则添加/uploads/前缀
  return `http://localhost:8080/uploads/${path}`
}

// 获取第一张图片
const getFirstImage = (imagePaths) => {
  if (!imagePaths) return null
  try {
    const paths = JSON.parse(imagePaths)
    return paths.length > 0 ? paths[0] : null
  } catch {
    return null
  }
}

// 获取内容类型标签
const getContentTypeTag = (type) => {
  switch (type) {
    case 1: return 'success'
    case 2: return 'warning'
    case 3: return 'info'
    default: return ''
  }
}

// 获取内容类型文本
const getContentTypeText = (type) => {
  switch (type) {
    case 1: return '短视频'
    case 2: return '图片'
    case 3: return '混合'
    default: return '未知'
  }
}

// 获取状态标签
const getStatusTag = (status) => {
  switch (status) {
    case 0: return 'info'
    case 1: return 'success'
    case 2: return 'danger'
    default: return ''
  }
}

// 获取状态文本
const getStatusText = (status) => {
  switch (status) {
    case 0: return '草稿'
    case 1: return '已发布'
    case 2: return '已删除'
    default: return '未知'
  }
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleString('zh-CN')
}
</script>

<style scoped>
.content-card {
  background: white;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(55, 59, 68, 0.08);
  transition: all 0.3s ease;
  border: 1px solid rgba(66, 134, 244, 0.1);
}

.content-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 40px rgba(55, 59, 68, 0.15);
  border-color: rgba(66, 134, 244, 0.3);
}

.content-preview {
  height: 200px;
  background: linear-gradient(135deg, rgba(66, 134, 244, 0.1), rgba(55, 59, 68, 0.1));
  position: relative;
  overflow: hidden;
}

.preview-media {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.content-card:hover .preview-media {
  transform: scale(1.05);
}

.no-media {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: rgb(55, 59, 68);
  background: linear-gradient(135deg, rgba(66, 134, 244, 0.05), rgba(55, 59, 68, 0.05));
}

.no-media p {
  margin-top: 10px;
  font-size: 14px;
  color: rgb(66, 134, 244);
}

.content-info {
  padding: 20px;
}

.content-header {
  display: flex;
  gap: 10px;
  margin-bottom: 15px;
  flex-wrap: wrap;
}

.content-header .el-tag {
  border-radius: 6px;
  font-weight: 600;
  border: none;
}

.content-diary {
  margin-bottom: 15px;
}

.content-diary p {
  color: rgb(55, 59, 68);
  line-height: 1.6;
  margin: 0;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.content-location {
  display: flex;
  align-items: center;
  gap: 5px;
  color: rgb(66, 134, 244);
  font-size: 14px;
  margin-bottom: 15px;
  font-weight: 500;
}

.content-stats {
  display: flex;
  gap: 15px;
  color: rgb(55, 59, 68);
  font-size: 14px;
  margin-bottom: 10px;
  font-weight: 500;
}

.content-time {
  color: rgba(55, 59, 68, 0.6);
  font-size: 12px;
}

.content-actions {
  padding: 0 20px 20px;
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.content-actions .el-button {
  border-radius: 8px;
  font-weight: 600;
  transition: all 0.3s ease;
}

.content-actions .el-button--primary {
  background: linear-gradient(to right, rgb(55, 59, 68), rgb(66, 134, 244));
  border: none;
}

.content-actions .el-button--primary:hover {
  box-shadow: 0 4px 12px rgba(66, 134, 244, 0.3);
  transform: translateY(-1px);
}

.content-actions .el-button--danger {
  background: linear-gradient(to right, #ff6b6b, #ee5a52);
  border: none;
}

.content-actions .el-button--danger:hover {
  box-shadow: 0 4px 12px rgba(255, 107, 107, 0.3);
  transform: translateY(-1px);
}

.content-actions .el-button--default {
  border-color: rgb(66, 134, 244);
  color: rgb(66, 134, 244);
}

.content-actions .el-button--default:hover {
  background: rgba(66, 134, 244, 0.1);
  border-color: rgb(55, 59, 68);
  color: rgb(55, 59, 68);
}
</style> 