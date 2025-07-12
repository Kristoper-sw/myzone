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
  background: rgba(55, 59, 68, 0.8);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  animation: fadeIn 0.3s ease;
  backdrop-filter: blur(4px);
}

.success-content {
  background: white;
  padding: 40px;
  border-radius: 16px;
  text-align: center;
  max-width: 400px;
  max-height: 80vh;
  overflow-y: auto;
  box-shadow: 0 16px 48px rgba(55, 59, 68, 0.2);
  animation: slideUp 0.3s ease;
  border: 1px solid rgba(66, 134, 244, 0.1);
}

.success-icon {
  margin-bottom: 20px;
  display: block;
  color: #67C23A;
}

.success-content h3 {
  color: rgb(55, 59, 68);
  margin-bottom: 10px;
  font-size: 24px;
  font-weight: 600;
  letter-spacing: 1px;
}

.success-content p {
  color: rgb(66, 134, 244);
  margin-bottom: 30px;
  font-size: 16px;
  line-height: 1.6;
}

.success-actions {
  display: flex;
  gap: 15px;
  justify-content: center;
}

.success-actions .el-button {
  border-radius: 8px;
  font-weight: 600;
  transition: all 0.3s ease;
  padding: 12px 24px;
}

.success-actions .el-button--primary {
  background: linear-gradient(to right, rgb(55, 59, 68), rgb(66, 134, 244));
  border: none;
}

.success-actions .el-button--primary:hover {
  box-shadow: 0 4px 12px rgba(66, 134, 244, 0.3);
  transform: translateY(-1px);
}

.success-actions .el-button--default {
  border-color: rgb(66, 134, 244);
  color: rgb(66, 134, 244);
}

.success-actions .el-button--default:hover {
  background: rgba(66, 134, 244, 0.1);
  border-color: rgb(55, 59, 68);
  color: rgb(55, 59, 68);
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
  
  .success-actions {
    flex-direction: column;
    gap: 10px;
  }
}
</style> 