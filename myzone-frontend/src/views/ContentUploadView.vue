<template>
  <div class="upload-content">
    <el-card class="upload-header">
      <template #header>
        <h2>{{ $t('content.uploadContent') }}</h2>
      </template>
      <p>{{ $t('content.uploadContent') }}</p>
      <ContentUpload />
    </el-card>
  </div>
</template>

<script setup>
import { useI18n } from 'vue-i18n'
import ContentUpload from '@/components/ContentUpload.vue'

const { t: $t } = useI18n()
</script>

<style scoped>
.upload-content {
  min-width: 1200px;
  margin: 0 auto;
}

.upload-header {
  margin-bottom: 30px;
}

.upload-header :deep(.el-card__header) {
  background: linear-gradient(to right, rgb(55, 59, 68), rgb(66, 134, 244));
  color: #fff;
  border-radius: 12px 12px 0 0;
}

.upload-header h2 {
  font-size: 28px;
  font-weight: bold;
  color: #fff;
  margin: 0;
  letter-spacing: 2px;
}

.upload-header p {
  margin: 10px 0 0 0;
  color: #666;
}
</style>