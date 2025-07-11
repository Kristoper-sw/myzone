<template>
    <div class="content-upload">
    <el-card class="upload-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <h2 class="upload-title">分享你的精彩时刻</h2>
        </div>
      </template>

      <!-- 内容类型选择 -->
      <div class="content-type-selector">
        <el-form-item label="选择内容类型：">
          <el-radio-group v-model="contentType" @change="handleTypeChange">
            <el-radio :label="1">短视频</el-radio>
            <el-radio :label="2">图片</el-radio>
            <el-radio :label="3">混合</el-radio>
          </el-radio-group>
        </el-form-item>
      </div>

      <!-- 位置信息 -->
      <el-form-item label="位置信息">
        <div class="location-section">
          <el-input
            v-model="location"
            placeholder="点击获取当前位置"
            readonly
            class="location-input"
          >
          </el-input>
          <el-button type="primary" @click="getCurrentLocation" :icon="Location"></el-button>
        </div>
        <el-alert
          v-if="locationError"
          :title="locationError"
          type="error"
          show-icon
          :closable="false"
        />
      </el-form-item>

      <!-- 视频上传区域 -->
      <div v-if="showVideoUpload">
        <el-form-item label="上传视频">
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
              将视频文件拖到此处，或<em>点击上传</em>
            </div>
            <template #tip>
              <div class="el-upload__tip">
                支持 MP4, AVI, MOV 等格式，最大 100MB
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
            <el-button @click="removeVideo" type="danger" size="small" :icon="Delete">
              删除视频
            </el-button>
          </div>
        </el-form-item>
      </div>

      <!-- 图片上传区域 -->
      <div v-if="showImageUpload">
        <el-form-item label="上传图片">
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
              将图片文件拖到此处，或<em>点击上传</em>
            </div>
            <template #tip>
              <div class="el-upload__tip">
                支持 JPG, PNG, GIF 等格式，可多选
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
              />
            </div>
          </div>
        </el-form-item>
      </div>

      <!-- 日记输入区域 -->
      <el-form-item label="写日记（可选）">
        <el-input
          v-model="diary"
          type="textarea"
          placeholder="分享你的想法和感受..."
          :rows="6"
          maxlength="1000"
          show-word-limit
        />
      </el-form-item>
    </el-card>

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
        {{ uploading ? '上传中...' : '发布内容' }}
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

<script>
import { contentApi } from '@/api/content'
import { ElMessage } from 'element-plus'
import {  UploadFilled } from '@element-plus/icons-vue'

export default {
  name: 'ContentUpload',
  components: {
    UploadFilled
  },
  data() {
    return {
      contentType: 1, // 1-短视频，2-图片，3-混合
      selectedVideo: null,
      selectedImages: [],
      diary: '',
      uploading: false,
      uploadProgress: 0,
      videoPreviewUrl: '',
      imagePreviewUrls: [],
      // 位置信息
      latitude: null,
      longitude: null,
      location: '',
      gettingLocation: false,
      locationError: ''
    }
  },
  computed: {
    showVideoUpload() {
      return this.contentType === 1 || this.contentType === 3
    },
    showImageUpload() {
      return this.contentType === 2 || this.contentType === 3
    },
    canUpload() {
      if (this.contentType === 1) {
        return this.selectedVideo !== null
      } else if (this.contentType === 2) {
        return this.selectedImages.length > 0
      } else if (this.contentType === 3) {
        return this.selectedVideo !== null || this.selectedImages.length > 0
      }
      return false
    }
  },
  methods: {
    handleTypeChange() {
      // 切换类型时清空已选择的文件
      this.selectedVideo = null
      this.selectedImages = []
      this.videoPreviewUrl = ''
      this.imagePreviewUrls = []
    },

    // 获取当前位置
    async getCurrentLocation() {
      this.gettingLocation = true
      this.locationError = ''

      try {
        const position = await this.getGeolocation()
        this.latitude = position.coords.latitude
        this.longitude = position.coords.longitude

        // 获取位置描述
        await this.getLocationDescription()

        ElMessage.success('位置获取成功')
      } catch (error) {
        console.error('获取位置失败:', error)
        this.locationError = '获取位置失败，请检查浏览器权限设置'
        ElMessage.error('获取位置失败')
      } finally {
        this.gettingLocation = false
      }
    },

    // 获取地理位置
    getGeolocation() {
      return new Promise((resolve, reject) => {
        if (!navigator.geolocation) {
          reject(new Error('浏览器不支持地理位置'))
          return
        }

        navigator.geolocation.getCurrentPosition(
          resolve,
          reject,
          {
            enableHighAccuracy: true,
            timeout: 10000,
            maximumAge: 60000
          }
        )
      })
    },

    // 获取位置描述
    async getLocationDescription() {
      try {
        const response = await fetch(
          `https://api.mapbox.com/geocoding/v5/mapbox.places/${this.longitude},${this.latitude}.json?access_token=pk.eyJ1Ijoia3Jpc3RvcGhlcnFxIiwiYSI6ImNtYnl2c2RhMDAyY3IybXNmZzY5cmFhYWQifQ.DgqaIt3pvVHrEOmOmTO5aQ`
        )
        const data = await response.json()

        if (data.features && data.features.length > 0) {
          const place = data.features[0]
          this.location = place.place_name || place.text
        } else {
          this.location = `${this.latitude.toFixed(6)}, ${this.longitude.toFixed(6)}`
        }
      } catch (error) {
        console.error('获取位置描述失败:', error)
        this.location = `${this.latitude.toFixed(6)}, ${this.longitude.toFixed(6)}`
      }
    },

    handleVideoSelect(file) {
      if (file.raw) {
        this.selectedVideo = file.raw
        this.videoPreviewUrl = URL.createObjectURL(file.raw)
      }
    },

    handleImageSelect(file) {
      if (file.raw) {
        const preview = URL.createObjectURL(file.raw)
        this.selectedImages.push({
          file: file.raw,
          name: file.raw.name,
          size: file.raw.size,
          preview
        })
      }
    },

    removeVideo() {
      this.selectedVideo = null
      this.videoPreviewUrl = ''
      if (this.$refs.videoUpload) {
        this.$refs.videoUpload.clearFiles()
      }
    },

    removeImage(index) {
      const removedImage = this.selectedImages.splice(index, 1)[0]
      URL.revokeObjectURL(removedImage.preview)
    },

    formatFileSize(bytes) {
      if (bytes === 0) return '0 Bytes'
      const k = 1024
      const sizes = ['Bytes', 'KB', 'MB', 'GB']
      const i = Math.floor(Math.log(bytes) / Math.log(k))
      return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
    },

    async uploadContent() {
      if (!this.canUpload) {
        ElMessage.error('请选择要上传的文件')
        return
      }

      this.uploading = true
      this.uploadProgress = 0

      try {
        const formData = new FormData()
        formData.append('contentType', this.contentType)

        if (this.diary) {
          formData.append('diary', this.diary)
        }

        if (this.selectedVideo) {
          formData.append('videoFile', this.selectedVideo)
        }

        if (this.selectedImages.length > 0) {
          this.selectedImages.forEach(image => {
            formData.append('imageFiles', image.file)
          })
        }

        // 添加位置信息
        if (this.latitude && this.longitude) {
          formData.append('latitude', this.latitude)
          formData.append('longitude', this.longitude)
        }

        if (this.location) {
          formData.append('location', this.location)
        }

        // 模拟上传进度
        const progressInterval = setInterval(() => {
          if (this.uploadProgress < 90) {
            this.uploadProgress += Math.random() * 10
          }
        }, 200)

        const response = await contentApi.uploadContent(formData)

        clearInterval(progressInterval)
        this.uploadProgress = 100

        if (response.code === 200) {
          ElMessage.success('内容上传成功！')
          this.resetForm()
          this.$emit('upload-success', response.data)
        } else {
          ElMessage.error(response.message || '上传失败')
        }
      } catch (error) {
        console.error('上传失败:', error)
        ElMessage.error('上传失败，请重试')
      } finally {
        this.uploading = false
        this.uploadProgress = 0
      }
    },

    resetForm() {
      this.contentType = 1
      this.selectedVideo = null
      this.selectedImages = []
      this.diary = ''
      this.videoPreviewUrl = ''
      this.imagePreviewUrls = []
      this.latitude = null
      this.longitude = null
      this.location = ''
      this.locationError = ''

      // 清空文件输入
      if (this.$refs.videoUpload) {
        this.$refs.videoUpload.clearFiles()
      }
      if (this.$refs.imageUpload) {
        this.$refs.imageUpload.clearFiles()
      }
    }
  },
  beforeUnmount() {
    // 清理预览URL
    if (this.videoPreviewUrl) {
      URL.revokeObjectURL(this.videoPreviewUrl)
    }
    this.selectedImages.forEach(image => {
      URL.revokeObjectURL(image.preview)
    })
  }
}
</script>

<style scoped>
.content-upload {
  height: 100vh;
  overflow-y: auto;
  padding: 16px;
  box-sizing: border-box;
}

.upload-card {
  max-width: 800px;
  margin: 0 auto;
  margin-bottom: 80px;
}

.card-header {
  text-align: center;
}

.upload-title {
  font-size: 24px;
  color: #333;
  margin: 0;
}

.content-type-selector {
  margin-bottom: 30px;
}

.location-section {
  display: flex;
  align-items: center;
  gap: 15px;
}

.location-input {
  flex: 1;
}

.upload-area {
  width: 100%;
}

.video-preview {
  margin-top: 20px;
  text-align: center;
}

.preview-video {
  max-width: 100%;
  border-radius: 8px;
  margin-bottom: 15px;
}

.images-preview {
  margin-top: 20px;
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
}

.image-item {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.preview-image {
  width: 200px;
  height: 200px;
  border-radius: 8px;
  margin-bottom: 10px;
}

.image-info {
  text-align: center;
  margin-bottom: 10px;
}

.image-info p {
  margin: 5px 0;
  font-size: 14px;
  color: #666;
}

.file-info {
  margin: 10px 0;
  font-size: 14px;
  color: #666;
}

.upload-actions {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background: white;
  padding: 20px;
  border-top: 1px solid #eee;
  text-align: center;
  z-index: 1000;
}

.upload-progress {
  margin-top: 15px;
  max-width: 400px;
  margin-left: auto;
  margin-right: auto;
}

@media (max-width: 768px) {
  .content-upload {
    padding: 10px;
  }

  .upload-card {
    margin-bottom: 100px;
  }

  .location-section {
    flex-direction: column;
    align-items: stretch;
  }

  .preview-image {
    width: 150px;
    height: 150px;
  }

  .upload-actions {
    padding: 15px;
  }
}
</style>