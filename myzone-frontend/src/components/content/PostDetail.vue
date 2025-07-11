<template>
  <div v-if="post" class="post-detail">
    <el-card class="post-card">
      <template #header>
        <h2 class="post-title">{{ post.title }}</h2>
      </template>
      
      <div v-if="post.mediaUrl" class="post-media">
        <img 
          v-if="isImage(post.mediaUrl)" 
          :src="post.mediaUrl" 
          :alt="post.title"
          class="post-image"
        />
        <video 
          v-else 
          controls 
          :src="post.mediaUrl" 
          class="post-video"
        />
      </div>
      
      <div class="post-description">
        <p>{{ post.description }}</p>
      </div>
    </el-card>
  </div>
  
  <div v-else class="post-not-found">
    <el-empty description="内容未找到">
      <el-button type="primary" @click="$router.go(-1)">返回</el-button>
    </el-empty>
  </div>
</template>

<script setup>
// 定义props
defineProps({
  post: {
    type: Object,
    default: null
  }
})

// 判断是否为图片
const isImage = (url) => {
  return /\.(jpg|jpeg|png|gif|webp|svg)$/i.test(url)
}
</script>

<style scoped>
.post-detail {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.post-card {
  margin-bottom: 20px;
}

.post-title {
  margin: 0;
  color: #333;
  font-size: 24px;
}

.post-media {
  margin-bottom: 20px;
}

.post-image,
.post-video {
  max-width: 100%;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.post-description {
  line-height: 1.6;
  color: #666;
  font-size: 16px;
}

.post-not-found {
  padding: 40px 20px;
  text-align: center;
}

@media (max-width: 768px) {
  .post-detail {
    padding: 10px;
  }
  
  .post-title {
    font-size: 20px;
  }
}
</style> 