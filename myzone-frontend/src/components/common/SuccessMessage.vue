<template>
  <div v-if="visible" class="success-message">
    <div class="success-content">
      <el-icon class="success-icon" :size="48" color="#67C23A">
        <CircleCheckFilled />
      </el-icon>
      <h3>{{ title }}</h3>
      <p>{{ message }}</p>
      <div class="success-actions">
        <el-button type="primary" @click="$emit('primary-action')">
          {{ primaryActionText }}
        </el-button>
        <el-button @click="$emit('secondary-action')">
          {{ secondaryActionText }}
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { CircleCheckFilled } from '@element-plus/icons-vue'

// 定义props
defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  title: {
    type: String,
    default: '操作成功！'
  },
  message: {
    type: String,
    default: '操作已完成'
  },
  primaryActionText: {
    type: String,
    default: '确定'
  },
  secondaryActionText: {
    type: String,
    default: '取消'
  }
})

// 定义emits
defineEmits(['primary-action', 'secondary-action'])
</script>

<style scoped>
.success-message {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.8);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  animation: fadeIn 0.3s ease;
}

.success-content {
  background: white;
  padding: 40px;
  border-radius: 16px;
  text-align: center;
  max-width: 400px;
  max-height: 80vh;
  overflow-y: auto;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
  animation: slideUp 0.3s ease;
}

.success-icon {
  margin-bottom: 20px;
  display: block;
}

.success-content h3 {
  color: #333;
  margin-bottom: 10px;
  font-size: 24px;
}

.success-content p {
  color: #666;
  margin-bottom: 30px;
  font-size: 16px;
}

.success-actions {
  display: flex;
  gap: 15px;
  justify-content: center;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes slideUp {
  from {
    transform: translateY(50px);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

@media (max-width: 768px) {
  .success-content {
    margin: 20px;
    padding: 30px;
  }
}
</style> 