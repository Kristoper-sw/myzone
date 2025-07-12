<template>
  <div class="avatar-upload">
    <el-avatar 
      :size="100" 
      :src="avatarUrl"
      class="avatar-preview"
    >
      {{ avatarText }}
    </el-avatar>
    <div class="avatar-actions">
      <el-button type="primary" @click="handleAvatarUpload">
        更换头像
      </el-button>
      <el-button @click="removeAvatar" v-if="modelValue && modelValue.trim()">
        移除头像
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { ElMessageBox } from 'element-plus'

// 定义props
const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  },
  username: {
    type: String,
    default: ''
  },
  nickname: {
    type: String,
    default: ''
  }
})

// 定义emits
const emit = defineEmits(['update:modelValue'])

// 头像文字
const avatarText = computed(() => {
  const name = props.nickname || props.username || ''
  return name.charAt(0).toUpperCase()
})

// 头像URL
const avatarUrl = computed(() => {
  return props.modelValue || ''
})

// 头像上传
const handleAvatarUpload = () => {
  ElMessageBox.prompt('请输入头像URL', '上传头像', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputValue: props.modelValue || ''
  }).then(({ value }) => {
    if (value) {
      emit('update:modelValue', value)
    }
  }).catch(() => {
    // 用户取消
  })
}

// 移除头像
const removeAvatar = () => {
  emit('update:modelValue', '')
}
</script>

<style scoped>
.avatar-upload {
  display: flex;
  align-items: center;
  gap: 20px;
}

.avatar-preview {
  border: 3px solid rgba(66, 134, 244, 0.3);
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(55, 59, 68, 0.1);
}

.avatar-preview:hover {
  border-color: rgb(66, 134, 244);
  box-shadow: 0 6px 20px rgba(66, 134, 244, 0.2);
  transform: scale(1.02);
}

.avatar-actions {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.avatar-actions .el-button {
  width: 100px;
  border-radius: 8px;
  font-weight: 600;
  transition: all 0.3s ease;
}

.avatar-actions .el-button--primary {
  background: linear-gradient(to right, rgb(55, 59, 68), rgb(66, 134, 244));
  border: none;
}

.avatar-actions .el-button--primary:hover {
  box-shadow: 0 4px 12px rgba(66, 134, 244, 0.3);
  transform: translateY(-1px);
}

.avatar-actions .el-button--default {
  border-color: rgb(66, 134, 244);
  color: rgb(66, 134, 244);
}

.avatar-actions .el-button--default:hover {
  background: rgba(66, 134, 244, 0.1);
  border-color: rgb(55, 59, 68);
  color: rgb(55, 59, 68);
}

@media (max-width: 768px) {
  .avatar-upload {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style> 