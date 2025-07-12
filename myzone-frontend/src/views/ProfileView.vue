<template>
  <div class="profile-content">
    <el-card class="profile-card" shadow="hover">
      <template #header>
        <h2>个人资料</h2>
        <p class="subtitle">管理你的个人信息和账户设置</p>
      </template>
      <!-- 基本信息表单 -->
      <el-form :model="infoForm" :rules="infoRules" ref="infoFormRef" label-width="100px" class="profile-form">
        <div class="profile-section">
          <h3>基本信息</h3>
          <div class="section-content">
            <el-form-item label="头像">
              <AvatarUpload v-model="infoForm.avatar" :username="infoForm.username" :nickname="infoForm.nickname" />
            </el-form-item>
            <el-form-item label="用户名">
              <el-input v-model="infoForm.username" disabled />
            </el-form-item>
            <el-form-item label="昵称" prop="nickname">
              <el-input v-model="infoForm.nickname" maxlength="20" show-word-limit />
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="infoForm.email" maxlength="100" />
            </el-form-item>
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="infoForm.phone" maxlength="20" />
            </el-form-item>
            <el-form-item label="注册时间">
              <el-input :value="formatDate(infoForm.createTime)" disabled />
            </el-form-item>
          </div>
        </div>
        <el-form-item>
          <el-button type="primary" @click="onSaveInfo" :loading="savingInfo">保存基本信息</el-button>
        </el-form-item>
      </el-form>
      <el-divider></el-divider>
      <!-- 密码修改表单 -->
      <el-form :model="pwdForm" :rules="pwdRules" ref="pwdFormRef" label-width="100px" class="profile-form">
        <div class="profile-section">
          <h3>修改密码</h3>
          <div class="section-content">
            <el-form-item label="当前密码" prop="currentPassword">
              <el-input v-model="pwdForm.currentPassword" type="password" show-password autocomplete="off" />
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
              <el-input v-model="pwdForm.newPassword" type="password" show-password autocomplete="off" />
            </el-form-item>
            <el-form-item label="确认新密码" prop="confirmPassword">
              <el-input v-model="pwdForm.confirmPassword" type="password" show-password autocomplete="off" />
            </el-form-item>
          </div>
        </div>
        <el-form-item>
          <el-button type="primary" @click="onSavePwd" :loading="savingPwd">修改密码</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { userAPI } from '@/api/user'
import AvatarUpload from '@/components/profile/AvatarUpload.vue'

const userStore = useUserStore()
const { currentUser, refreshUserInfo } = userStore
const infoFormRef = ref()
const pwdFormRef = ref()
const savingInfo = ref(false)
const savingPwd = ref(false)

const infoForm = reactive({
  username: '',
  nickname: '',
  email: '',
  phone: '',
  avatar: '',
  createTime: ''
})

const pwdForm = reactive({
  currentPassword: '',
  newPassword: '',
  confirmPassword: ''
})

watch(
  () => currentUser.value,
  (user) => {
    if (user) {
      infoForm.username = user.username || ''
      infoForm.nickname = user.nickname || ''
      infoForm.email = user.email || ''
      infoForm.phone = user.phone || ''
      infoForm.avatar = user.avatar || ''
      infoForm.createTime = user.createTime || user.createdAt || ''
      pwdForm.currentPassword = ''
      pwdForm.newPassword = ''
      pwdForm.confirmPassword = ''
    }
  },
  { immediate: true }
)

const infoRules = {
  nickname: [
    { required: true, message: '昵称不能为空', trigger: 'blur' },
    { min: 2, max: 20, message: '昵称长度必须在2-20个字符之间', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
  ],
  phone: [
    { min: 0, max: 20, message: '手机号长度不能超过20个字符', trigger: 'blur' }
  ]
}

const pwdRules = {
  currentPassword: [
    { required: true, message: '请输入当前密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度必须在6-20个字符之间', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    { validator: (rule, value, callback) => {
      if (value !== pwdForm.newPassword) return callback(new Error('两次输入的新密码不一致'))
      callback()
    }, trigger: 'blur' }
  ]
}

function formatDate(dateString) {
  if (!dateString) return '未知'
  return new Date(dateString).toLocaleDateString('zh-CN')
}

const onSaveInfo = () => {
  infoFormRef.value.validate(async (valid) => {
    if (!valid) return
    savingInfo.value = true
    try {
      const payload = {
        nickname: infoForm.nickname,
        email: infoForm.email,
        phone: infoForm.phone,
        avatar: infoForm.avatar
      }
      const res = await userAPI.updateUserInfo(payload)
      if (res.code === 200) {
        ElMessage.success('资料已更新')
        await refreshUserInfo()
      } else {
        ElMessage.error(res.message || '更新失败')
      }
    } catch (e) {
      ElMessage.error(e.message || '更新失败')
    } finally {
      savingInfo.value = false
    }
  })
}

const onSavePwd = () => {
  pwdFormRef.value.validate(async (valid) => {
    if (!valid) return
    savingPwd.value = true
    try {
      const payload = {
        currentPassword: pwdForm.currentPassword,
        newPassword: pwdForm.newPassword,
        confirmPassword: pwdForm.confirmPassword
      }
      const res = await userAPI.updateUserInfo(payload)
      if (res.code === 200) {
        ElMessage.success('密码修改成功')
        await refreshUserInfo()
        pwdForm.currentPassword = ''
        pwdForm.newPassword = ''
        pwdForm.confirmPassword = ''
      } else {
        ElMessage.error(res.message || '修改失败')
      }
    } catch (e) {
      ElMessage.error(e.message || '修改失败')
    } finally {
      savingPwd.value = false
    }
  })
}
</script>

<style scoped>
.profile-content {
  min-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.profile-card {
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(55, 59, 68, 0.12);
  border: none;
}

.profile-card :deep(.el-card__header) {
  background: linear-gradient(to right, rgb(55, 59, 68), rgb(66, 134, 244));
  color: white;
  border-radius: 16px 16px 0 0;
  padding: 24px 20px;
}

.profile-card h2 {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
  letter-spacing: 1px;
}

.profile-card .subtitle {
  margin-top: 8px;
  font-size: 14px;
  color: rgba(255, 255, 255, 0.8);
}

.profile-section {
  padding: 20px 0;
}

.profile-section h3 {
  font-size: 18px;
  font-weight: 600;
  color: rgb(55, 59, 68);
  margin-bottom: 16px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.profile-section h3::before {
  content: '';
  width: 4px;
  height: 18px;
  background: linear-gradient(to bottom, rgb(55, 59, 68), rgb(66, 134, 244));
  border-radius: 2px;
}

.profile-form {
  max-width: 600px;
  margin: 0 auto;
}

.section-content {
  padding: 0 10px;
}

@media (max-width: 768px) {
  .profile-content {
    padding: 10px;
  }
  .profile-form {
    max-width: 100%;
    padding: 0 4px;
  }
}
</style>
