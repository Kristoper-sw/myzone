<template>
  <CommonLayout>
    <div class="auth-content">
      <el-card class="auth-card">
        <template #header>
          <h2>用户注册</h2>
        </template>
        <AuthForm type="register" />
      </el-card>
    </div>
  </CommonLayout>
</template>

<script setup>
import CommonLayout from '@/components/layout/CommonLayout.vue'
import AuthForm from '@/components/auth/AuthForm.vue'
</script>

<style scoped>
.auth-content {
  max-width: 400px;
  margin: 0 auto;
  padding-top: 50px;
}

.auth-card {
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.auth-card :deep(.el-card__header) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 8px 8px 0 0;
  text-align: center;
}

.auth-card h2 {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
}
</style> 