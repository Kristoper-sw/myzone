<template>
  <CommonLayout>
    <div class="upload-content">
      <el-card class="upload-header">
        <template #header>
          <h2>上传内容</h2>
        </template>
        <p>分享你的精彩时刻，上传图片和视频内容</p>
      </el-card>
      
      <ContentUpload />
    </div>
  </CommonLayout>
</template>

<script setup>
import CommonLayout from '@/components/layout/CommonLayout.vue'
import ContentUpload from '@/components/ContentUpload.vue'
</script>

<style scoped>
.upload-content {
  max-width: 800px;
  margin: 0 auto;
}

.upload-header {
  margin-bottom: 30px;
}

.upload-header :deep(.el-card__header) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 8px 8px 0 0;
}

.upload-header h2 {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
}

.upload-header p {
  margin: 10px 0 0 0;
  color: #666;
}
</style> 