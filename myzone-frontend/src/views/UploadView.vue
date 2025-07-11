<template>
  <div class="upload-view">
    <ContentUpload ref="contentUpload" @upload-success="handleUploadSuccess" />
    
    <!-- 上传成功提示 -->
    <SuccessMessage
      :visible="showSuccessMessage"
      title="上传成功！"
      message="你的内容已经成功发布，快去查看吧！"
      primary-action-text="查看我的内容"
      secondary-action-text="继续上传"
      @primary-action="goToProfile"
      @secondary-action="continueUpload"
    />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import ContentUpload from '@/components/ContentUpload.vue'
import SuccessMessage from '@/components/common/SuccessMessage.vue'

const router = useRouter()
const contentUpload = ref()
const showSuccessMessage = ref(false)

const handleUploadSuccess = () => {
  showSuccessMessage.value = true
  // 3秒后自动隐藏成功消息
  setTimeout(() => {
    showSuccessMessage.value = false
  }, 3000)
}

const goToProfile = () => {
  router.push('/profile')
}

const continueUpload = () => {
  showSuccessMessage.value = false
  // 触发子组件重置表单
  if (contentUpload.value) {
    contentUpload.value.resetForm()
  }
}
</script>

<style scoped>
.upload-view {
  flex: 1;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
  display: flex;
  flex-direction: column;
  min-height: calc(100vh - 56px); /* 减去导航栏的高度 */
}

@media (max-width: 768px) {
  .upload-view {
    padding: 10px;
    min-height: calc(100vh - 56px); /* 确保在移动端也减去导航栏高度 */
  }
}
</style> 