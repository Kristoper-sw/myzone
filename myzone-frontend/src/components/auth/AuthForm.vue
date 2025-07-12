<template>
  <div class="auth-container">
    <div class="auth-card">
      <div class="auth-card-header">
        <h2>{{ isLogin ? '用户登录' : '用户注册' }}</h2>
        <p>{{ isLogin ? '欢迎回来！请输入账号和密码' : '欢迎加入！请填写注册信息' }}</p>
      </div>

      <el-form
        :model="form"
        :rules="rules"
        ref="formRef"
        @submit.prevent="handleSubmit"
        label-width="0"
        class="auth-form"
      >
        <el-form-item prop="username">
          <el-input
            v-model="form.username"
            placeholder="用户名"
            :prefix-icon="User"
            size="large"
          />
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="密码"
            :prefix-icon="Lock"
            size="large"
            show-password
          />
        </el-form-item>

        <el-form-item v-if="!isLogin" prop="confirmPassword">
          <el-input
            v-model="form.confirmPassword"
            type="password"
            placeholder="确认密码"
            :prefix-icon="Lock"
            size="large"
            show-password
          />
        </el-form-item>

        <el-form-item v-if="!isLogin" prop="email">
          <el-input
            v-model="form.email"
            placeholder="邮箱（可选）"
            :prefix-icon="Message"
            size="large"
          />
        </el-form-item>

        <el-form-item v-if="!isLogin" prop="nickname">
          <el-input
            v-model="form.nickname"
            placeholder="昵称（可选）"
            :prefix-icon="UserFilled"
            size="large"
          />
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            style="width: 100%;"
            size="large"
            :loading="loading"
            @click="handleSubmit"
          >
            {{ loading ? (isLogin ? '登录中...' : '注册中...') : (isLogin ? '登录' : '注册') }}
          </el-button>
        </el-form-item>

        <div class="auth-link">
          <span v-if="isLogin">还没有账号？</span>
          <span v-else>已有账号？</span>
          <router-link :to="isLogin ? '/register' : '/login'">
            {{ isLogin ? '注册' : '登录' }}
          </router-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock, Message, UserFilled } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

const props = defineProps({
  type: {
    type: String,
    default: 'login',
    validator: (value) => ['login', 'register'].includes(value)
  }
})

const router = useRouter()
const userStore = useUserStore()
const formRef = ref()
const loading = ref(false)

const isLogin = computed(() => props.type === 'login')

const form = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  email: '',
  nickname: ''
})

const validateConfirmPassword = (rule, value, callback) => {
  if (!isLogin.value) {
    if (value === '') {
      callback(new Error('请再次输入密码'))
    } else if (value !== form.password) {
      callback(new Error('两次输入密码不一致!'))
    } else {
      callback()
    }
  } else {
    callback()
  }
}

const validateEmail = (rule, value, callback) => {
  if (!isLogin.value && value !== '') {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    if (!emailRegex.test(value)) {
      callback(new Error('请输入正确的邮箱格式'))
    } else {
      callback()
    }
  } else {
    callback()
  }
}

const rules = computed(() => ({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: !isLogin.value, validator: validateConfirmPassword, trigger: 'blur' }
  ],
  email: [
    { validator: validateEmail, trigger: 'blur' }
  ]
}))

const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    loading.value = true

    if (isLogin.value) {
      const result = await userStore.login({
        username: form.username,
        password: form.password
      })

      if (result.success) {
        ElMessage.success('登录成功！')
        router.push('/')
      } else {
        ElMessage.error(result.message || '登录失败')
      }
    } else {
      const registerData = {
        username: form.username,
        password: form.password,
        confirmPassword: form.confirmPassword,
        email: form.email || undefined,
        nickname: form.nickname || undefined
      }

      const result = await userStore.register(registerData)

      if (result.success) {
        ElMessage.success('注册成功！请登录')
        router.push('/login')
      } else {
        ElMessage.error(result.message || '注册失败')
      }
    }
  } catch (error) {
    console.error(isLogin.value ? '登录错误:' : '注册错误:', error)
    ElMessage.error(error.message || (isLogin.value ? '登录失败，请重试' : '注册失败，请重试'))
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.auth-container {
  min-height: calc(100vh - 50px);
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, rgba(66, 134, 244, 0.1), rgba(55, 59, 68, 0.1));
}

.auth-card {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(55, 59, 68, 0.12);
  padding: 40px 32px 30px 32px;
  min-width: 320px;
  max-width: 90vw;
  border: 1px solid rgba(66, 134, 244, 0.1);
}

.auth-card-header {
  background: linear-gradient(to right, rgb(55, 59, 68), rgb(66, 134, 244));
  color: white;
  border-radius: 16px 16px 0 0;
  text-align: center;
  margin: -40px -32px 30px -32px;
  padding: 24px 20px;
}

.auth-card-header h2 {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
  letter-spacing: 1px;
}

.auth-card-header p {
  margin-top: 8px;
  font-size: 14px;
  color: rgba(255, 255, 255, 0.85);
}

:deep(.el-form-item) {
  margin-bottom: 20px;
}

.auth-link {
  margin-top: 16px;
  text-align: center;
  font-size: 14px;
  color: rgb(55, 59, 68);
}

.auth-link a {
  color: rgb(66, 134, 244);
  text-decoration: none;
  font-weight: 600;
  transition: all 0.3s ease;
}

.auth-link a:hover {
  color: rgb(55, 59, 68);
  text-decoration: underline;
}
</style>
