<template>
  <CommonLayout>
    <div class="profile-content">
      <el-card class="profile-header">
        <template #header>
          <h2>个人资料</h2>
        </template>
        <p>管理你的个人信息和账户设置</p>
      </el-card>
      
      <div class="profile-sections">
        <el-card class="profile-section">
          <template #header>
            <h3>基本信息</h3>
          </template>
          <AvatarUpload />
        </el-card>
        
        <el-card class="profile-section">
          <template #header>
            <h3>账户信息</h3>
          </template>
          <div class="account-info">
            <p><strong>用户名：</strong>{{ currentUser?.username || '未设置' }}</p>
            <p><strong>昵称：</strong>{{ currentUser?.nickname || '未设置' }}</p>
            <p><strong>邮箱：</strong>{{ currentUser?.email || '未设置' }}</p>
            <p><strong>注册时间：</strong>{{ formatDate(currentUser?.createdAt) }}</p>
          </div>
        </el-card>
      </div>
    </div>
  </CommonLayout>
</template>

<script setup>
import { computed } from 'vue'
import { useUserStore } from '@/stores/user'
import CommonLayout from '@/components/layout/CommonLayout.vue'
import AvatarUpload from '@/components/profile/AvatarUpload.vue'

const userStore = useUserStore()
const { currentUser } = userStore

function formatDate(dateString) {
  if (!dateString) return '未知'
  return new Date(dateString).toLocaleDateString('zh-CN')
}
</script>

<style scoped>
.profile-content {
  max-width: 800px;
  margin: 0 auto;
}

.profile-header {
  margin-bottom: 30px;
}

.profile-header :deep(.el-card__header) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 8px 8px 0 0;
}

.profile-header h2 {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
}

.profile-header p {
  margin: 10px 0 0 0;
  color: #666;
}

.profile-sections {
  display: grid;
  gap: 20px;
}

.profile-section {
  margin-bottom: 20px;
}

.profile-section :deep(.el-card__header) {
  background: #f8f9fa;
  border-bottom: 1px solid #e9ecef;
}

.profile-section h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.account-info p {
  margin: 10px 0;
  color: #666;
  line-height: 1.6;
}

.account-info strong {
  color: #333;
  margin-right: 10px;
}

@media (max-width: 768px) {
  .profile-sections {
    grid-template-columns: 1fr;
  }
}
</style> 