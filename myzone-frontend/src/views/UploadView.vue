<template>
  <div class="upload-view">
    <el-card class="upload-card-page" shadow="hover">
      <template #header>
        <h2>{{ $t('content.uploadContent') }}</h2>
        <p class="subtitle">{{ $t('content.uploadDescription') }}</p>
      </template>
      <ContentUpload ref="contentUpload" @upload-success="handleUploadSuccess" />
      <el-divider />
      <!-- 上传成功提示 -->
      <SuccessMessage
        :visible="showSuccessMessage"
        :title="$t('content.uploadSuccessTitle')"
        :message="$t('content.uploadSuccessMessage')"
        :primary-action-text="$t('content.viewMyContents')"
        :secondary-action-text="$t('content.continueUpload')"
        @primary-action="goToProfile"
        @secondary-action="continueUpload"
      />
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n'
import ContentUpload from '@/components/ContentUpload.vue'
import SuccessMessage from '@/components/common/SuccessMessage.vue'

const { t: $t } = useI18n()
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
  router.push('/my-contents')
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
  min-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.upload-card-page {
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(55, 59, 68, 0.12);
  border: none;
}

.upload-card-page :deep(.el-card__header) {
  background: linear-gradient(to right, rgb(55, 59, 68), rgb(66, 134, 244));
  color: white;
  border-radius: 16px 16px 0 0;
  padding: 24px 20px;
}

.upload-card-page h2 {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
  letter-spacing: 1px;
}

.upload-card-page .subtitle {
  margin-top: 8px;
  font-size: 14px;
  color: rgba(255,255,255,0.8);
}

@media (max-width: 768px) {
  .upload-view {
    min-width: auto;
    padding: 10px;
  }
  .upload-card-page {
    margin-bottom: 100px;
  }
}
</style>