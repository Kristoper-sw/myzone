<template>
  <div class="auth-container">
    <div class="auth-card">
      <div class="auth-card-header">
        <h2>{{ isLogin ? $t('auth.login') : $t('auth.register') }}</h2>
        <p>{{ isLogin ? $t('auth.loginWelcome') : $t('auth.registerWelcome') }}</p>
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
            :placeholder="$t('auth.username')"
            :prefix-icon="User"
            size="large"
          />
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            type="password"
            :placeholder="$t('auth.password')"
            :prefix-icon="Lock"
            size="large"
            show-password
          />
        </el-form-item>

        <el-form-item v-if="!isLogin" prop="confirmPassword">
          <el-input
            v-model="form.confirmPassword"
            type="password"
            :placeholder="$t('auth.confirmPassword')"
            :prefix-icon="Lock"
            size="large"
            show-password
          />
        </el-form-item>

        <el-form-item v-if="!isLogin" prop="email">
          <el-input
            v-model="form.email"
            :placeholder="$t('auth.email')"
            :prefix-icon="Message"
            size="large"
          />
        </el-form-item>

        <el-form-item v-if="!isLogin" prop="nickname">
          <el-input
            v-model="form.nickname"
            :placeholder="$t('auth.nickname')"
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
            {{ loading ? (isLogin ? $t('auth.loggingIn') : $t('auth.registering')) : (isLogin ? $t('auth.loginButton') : $t('auth.registerButton')) }}
          </el-button>
        </el-form-item>

        <div class="auth-link">
          <span v-if="isLogin">{{ $t('auth.noAccount') }}</span>
          <span v-else>{{ $t('auth.hasAccount') }}</span>
          <router-link :to="isLogin ? '/register' : '/login'">
            {{ isLogin ? $t('nav.register') : $t('nav.login') }}
          </router-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { useI18n } from 'vue-i18n'
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

const { t: $t } = useI18n()
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
      callback(new Error($t('validation.confirmPasswordRequired')))
    } else if (value !== form.password) {
      callback(new Error($t('validation.passwordMismatch')))
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
      callback(new Error($t('validation.emailFormat')))
    } else {
      callback()
    }
  } else {
    callback()
  }
}

const rules = computed(() => ({
  username: [
    { required: true, message: $t('validation.required', { field: $t('auth.username') }), trigger: 'blur' },
    { min: 3, max: 20, message: $t('validation.minLength', { field: $t('auth.username'), min: 3, max: 20 }), trigger: 'blur' }
  ],
  password: [
    { required: true, message: $t('validation.required', { field: $t('auth.password') }), trigger: 'blur' },
    { min: 6, max: 20, message: $t('validation.minLength', { field: $t('auth.password'), min: 6, max: 20 }), trigger: 'blur' }
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
        ElMessage.success($t('auth.loginSuccess'))
        router.push('/')
      } else {
        ElMessage.error(result.message || $t('auth.loginFailed'))
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
        ElMessage.success($t('auth.registerSuccess'))
        router.push('/login')
      } else {
        ElMessage.error(result.message || $t('auth.registerFailed'))
      }
    }
  } catch (error) {
    console.error(isLogin.value ? '登录错误:' : '注册错误:', error)
    ElMessage.error(error.message || (isLogin.value ? $t('auth.loginFailed') : $t('auth.registerFailed')))
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
