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
  border: 2px solid #e4e7ed;
}

.avatar-actions {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.avatar-actions .el-button {
  width: 100px;
}

@media (max-width: 768px) {
  .avatar-upload {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style> 