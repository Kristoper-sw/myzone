<template>
  <div class="content-upload">
    <!-- 内容类型选择 -->
    <el-form-item :label="$t('content.category')" class="form-item">
      <el-radio-group v-model="contentType" @change="handleTypeChange">
        <el-radio :label="1">{{ $t('content.video') }}</el-radio>
        <el-radio :label="2">{{ $t('content.image') }}</el-radio>
        <el-radio :label="3">{{ $t('content.mixed') }}</el-radio>
      </el-radio-group>
    </el-form-item>
    <!-- 标题 -->
    <el-form-item :label="$t('content.title')" class="form-item" required>
      <el-input v-model="title" :placeholder="$t('content.titlePlaceholder')" maxlength="50" show-word-limit />
    </el-form-item>
    <!-- 位置信息 -->
    <el-form-item :label="$t('content.location')" class="form-item">
      <div class="location-section">
        <el-input v-model="location" :placeholder="$t('content.locationPlaceholder')" readonly class="location-input" />
        <el-button type="primary" @click="getCurrentLocation" :icon="Location"></el-button>
      </div>
      <el-alert v-if="locationError" :title="locationError" type="error" show-icon :closable="false" />
    </el-form-item>
    <!-- 视频上传区域 -->
    <div v-if="showVideoUpload">
      <el-form-item :label="$t('content.uploadVideo')" class="form-item">
        <el-upload
          ref="videoUpload"
          :auto-upload="false"
          :show-file-list="false"
          accept="video/*"
          :on-change="handleVideoSelect"
          :on-remove="removeVideo"
          drag
          class="upload-area"
        >
          <el-icon class="el-icon--upload"><upload-filled /></el-icon>
          <div class="el-upload__text">
            {{ $t('content.dragDrop') }}
          </div>
          <template #tip>
            <div class="el-upload__tip">
              {{ $t('content.videoFileTypeSupport') }}
            </div>
          </template>
        </el-upload>
        <!-- 视频预览 -->
        <div v-if="selectedVideo" class="video-preview">
          <video :src="videoPreviewUrl" controls class="preview-video"></video>
          <div class="file-info">
            <p>{{ selectedVideo.name }}</p>
            <p>{{ formatFileSize(selectedVideo.size) }}</p>
          </div>
          <el-button @click="removeVideo" type="danger" size="small" :icon="Delete"></el-button>
        </div>
      </el-form-item>
    </div>
    <!-- 图片上传区域 -->
    <div v-if="showImageUpload">
      <el-form-item :label="$t('content.uploadImage')" class="form-item">
        <el-upload
          ref="imageUpload"
          :auto-upload="false"
          :show-file-list="false"
          accept="image/*"
          multiple
          :on-change="handleImageSelect"
          :on-remove="removeImage"
          drag
          class="upload-area"
        >
          <el-icon class="el-icon--upload"><upload-filled /></el-icon>
          <div class="el-upload__text">
            {{ $t('content.dragDrop') }}
          </div>
          <template #tip>
            <div class="el-upload__tip">
              {{ $t('content.fileTypeSupport') }}
            </div>
          </template>
        </el-upload>
        <!-- 图片预览 -->
        <div v-if="selectedImages.length > 0" class="images-preview">
          <div v-for="(image, index) in selectedImages" :key="index" class="image-item">
            <el-image
              :src="image.preview"
              :alt="image.name"
              class="preview-image"
              fit="cover"
            />
            <div class="image-info">
              <p>{{ image.name }}</p>
              <p>{{ formatFileSize(image.size) }}</p>
            </div>
            <el-button
              @click="removeImage(index)"
              type="danger"
              size="small"
              :icon="Delete"
              circle
            ></el-button>
          </div>
        </div>
      </el-form-item>
    </div>
    <!-- 日记输入区域 -->
    <el-form-item :label="$t('content.writeDiary')" class="form-item">
      <el-input
        v-model="diary"
        type="textarea"
        :placeholder="$t('content.diaryPlaceholder')"
        :rows="6"
        maxlength="1000"
        show-word-limit
      />
    </el-form-item>
    <!-- 上传按钮和进度条 -->
    <div class="upload-actions">
      <el-button
        @click="uploadContent"
        :disabled="!canUpload || uploading"
        type="primary"
        size="large"
        :loading="uploading"
      >
        <el-icon v-if="!uploading"><UploadFilled /></el-icon>
        {{ uploading ? $t('content.uploading') : $t('content.publishContent') }}
      </el-button>
      <el-progress
        v-if="uploading"
        :percentage="uploadProgress"
        :stroke-width="8"
        status="success"
        class="upload-progress"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useI18n } from 'vue-i18n'
import { ElMessage } from 'element-plus'
import { UploadFilled, Delete, Location } from '@element-plus/icons-vue'
import { contentApi } from '@/api/content.js'

const { t: $t } = useI18n()

// 响应式数据
const contentType = ref(1)
const selectedVideo = ref(null)
const selectedImages = ref([])
const diary = ref('')
const uploading = ref(false)
const uploadProgress = ref(0)
const videoPreviewUrl = ref('')
const latitude = ref(null)
const longitude = ref(null)
const location = ref('')
const gettingLocation = ref(false)
const locationError = ref('')
const title = ref('')

// 计算属性
const showVideoUpload = computed(() => {
  return contentType.value === 1 || contentType.value === 3
})

const showImageUpload = computed(() => {
  return contentType.value === 2 || contentType.value === 3
})

const canUpload = computed(() => {
  if (!title.value || !title.value.trim()) return false
  if (contentType.value === 1) {
    return selectedVideo.value !== null
  } else if (contentType.value === 2) {
    return selectedImages.value.length > 0
  } else if (contentType.value === 3) {
    return selectedVideo.value !== null || selectedImages.value.length > 0
  }
  return false
})

// 方法
const handleTypeChange = () => {
  selectedVideo.value = null
  selectedImages.value = []
  videoPreviewUrl.value = ''
}

const getCurrentLocation = async () => {
  gettingLocation.value = true
  locationError.value = ''
  try {
    if (!navigator.geolocation) {
      throw new Error($t('comment.browserNotSupportLocation'))
    }
    
    const position = await new Promise((resolve, reject) => {
      navigator.geolocation.getCurrentPosition(resolve, reject, {
        enableHighAccuracy: true,
        timeout: 10000,
        maximumAge: 60000
      })
    })
    
    latitude.value = position.coords.latitude
    longitude.value = position.coords.longitude
    location.value = `${latitude.value.toFixed(6)}, ${longitude.value.toFixed(6)}`
  } catch (error) {
    console.error('获取位置失败:', error)
    locationError.value = $t('comment.getLocationFailed')
  } finally {
    gettingLocation.value = false
  }
}

const handleVideoSelect = (file) => {
  if (file.raw) {
    selectedVideo.value = file.raw
    videoPreviewUrl.value = URL.createObjectURL(file.raw)
  }
}

const handleImageSelect = (file) => {
  if (file.raw) {
    const preview = URL.createObjectURL(file.raw)
    selectedImages.value.push({
      file: file.raw,
      name: file.raw.name,
      size: file.raw.size,
      preview
    })
  }
}

const removeVideo = () => {
  selectedVideo.value = null
  videoPreviewUrl.value = ''
}

const removeImage = (index) => {
  const removedImage = selectedImages.value.splice(index, 1)[0]
  URL.revokeObjectURL(removedImage.preview)
}

const formatFileSize = (bytes) => {
  if (bytes === 0) return '0 Bytes'
  const k = 1024
  const sizes = ['Bytes', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

const uploadContent = async () => {
  if (!canUpload.value) {
    ElMessage.warning($t('comment.pleaseCompleteRequired'))
    return
  }
  
  uploading.value = true
  uploadProgress.value = 0
  
  try {
    // 创建FormData对象
    const formData = new FormData()
    
    // 添加基本信息
    formData.append('title', title.value.trim())
    formData.append('contentType', contentType.value)
    formData.append('diary', diary.value.trim())
    
    // 添加地理位置信息
    if (latitude.value && longitude.value) {
      formData.append('latitude', latitude.value)
      formData.append('longitude', longitude.value)
    }
    
    // 添加视频文件
    if (selectedVideo.value) {
      formData.append('videoFile', selectedVideo.value)
    }
    
    // 添加图片文件
    selectedImages.value.forEach((image, index) => {
      formData.append(`imageFiles`, image.file)
    })
    
    // 调用后端API
    const response = await contentApi.uploadContent(formData)
    
    console.log('上传响应:', response) // 添加调试日志
    
    // 检查响应
    if (response && response.code === 200) {
      // 不在这里显示ElMessage，让父组件处理成功消息
      // ElMessage.success($t('content.uploadSuccess'))
      
      // 重置表单
      title.value = ''
      diary.value = ''
      selectedVideo.value = null
      selectedImages.value = []
      videoPreviewUrl.value = ''
      location.value = ''
      latitude.value = null
      longitude.value = null
      uploadProgress.value = 0
      
      // 触发上传成功事件
      emit('upload-success')
    } else {
      ElMessage.error(response?.message || $t('content.uploadFailed'))
    }
  } catch (error) {
    console.error('上传失败:', error)
    ElMessage.error(error?.message || $t('content.uploadFailed'))
  } finally {
    uploading.value = false
  }
}

// 添加emit定义
const emit = defineEmits(['upload-success'])

// 添加重置表单方法
const resetForm = () => {
  title.value = ''
  diary.value = ''
  selectedVideo.value = null
  selectedImages.value = []
  videoPreviewUrl.value = ''
  location.value = ''
  latitude.value = null
  longitude.value = null
  uploadProgress.value = 0
}

// 暴露resetForm方法给父组件
defineExpose({
  resetForm
})
</script>

<style scoped>
.content-upload {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.form-item {
  margin-bottom: 24px;
}

.location-section {
  display: flex;
  gap: 12px;
  align-items: center;
}

.location-input {
  flex: 1;
}

.upload-area {
  border: 2px dashed #d9d9d9;
  border-radius: 8px;
  padding: 40px;
  text-align: center;
  transition: border-color 0.3s;
}

.upload-area:hover {
  border-color: #409eff;
}

.el-icon--upload {
  font-size: 48px;
  color: #c0c4cc;
  margin-bottom: 16px;
}

.el-upload__text {
  color: #606266;
  font-size: 14px;
}

.el-upload__text em {
  color: #409eff;
  font-style: normal;
}

.el-upload__tip {
  font-size: 12px;
  color: #909399;
  margin-top: 8px;
}

.video-preview,
.images-preview {
  margin-top: 16px;
}

.preview-video {
  width: 100%;
  max-width: 400px;
  border-radius: 8px;
  margin-bottom: 8px;
}

.images-preview {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 16px;
}

.image-item {
  position: relative;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.preview-image {
  width: 100%;
  height: 150px;
  object-fit: cover;
}

.image-info {
  padding: 8px;
  background: rgba(0, 0, 0, 0.7);
  color: white;
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
}

.image-info p {
  margin: 0;
  font-size: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.file-info {
  margin-top: 8px;
  text-align: center;
}

.file-info p {
  margin: 4px 0;
  font-size: 12px;
  color: #666;
}

.upload-actions {
  margin-top: 32px;
  text-align: center;
}

.upload-progress {
  margin-top: 16px;
  max-width: 400px;
  margin-left: auto;
  margin-right: auto;
}
</style>
