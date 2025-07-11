import { ref, computed } from 'vue'
import { authAPI } from '@/api/auth'
import { userAPI } from '@/api/user'

// 创建单例状态
const userInfo = ref(null)
const token = ref(localStorage.getItem('token') || null)

// 计算属性 - 确保响应性
const isLoggedIn = computed(() => {
  return !!(token.value && userInfo.value)
})

const currentUser = computed(() => userInfo.value)

// 初始化用户信息
const initUserInfo = () => {
  const storedUserInfo = localStorage.getItem('userInfo')
  
  if (storedUserInfo) {
    try {
      userInfo.value = JSON.parse(storedUserInfo)
    } catch (error) {
      console.error('解析用户信息失败:', error)
      clearUserInfo()
    }
  }
}

// 设置用户信息
const setUserInfo = (user) => {
  userInfo.value = user
  if (user) {
    localStorage.setItem('userInfo', JSON.stringify(user))
  } else {
    localStorage.removeItem('userInfo')
  }
}

// 设置token
const setToken = (newToken) => {
  token.value = newToken
  if (newToken) {
    localStorage.setItem('token', newToken)
  } else {
    localStorage.removeItem('token')
  }
}

// 清除用户信息
const clearUserInfo = () => {
  userInfo.value = null
  token.value = null
  localStorage.removeItem('token')
  localStorage.removeItem('userInfo')
}

// 登录
const login = async (credentials) => {
  try {
    const response = await authAPI.login(credentials)
    if (response.code === 200) {
      setToken(response.data.token)
      setUserInfo({
        userId: response.data.userId,
        username: response.data.username,
        nickname: response.data.nickname,
        email: response.data.email,
        avatar: response.data.avatar
      })
      return { success: true, data: response.data }
    } else {
      return { success: false, message: response.message }
    }
  } catch (error) {
    return { success: false, message: error.message || '登录失败' }
  }
}

// 注册
const register = async (userData) => {
  try {
    const response = await authAPI.register(userData)
    if (response.code === 200) {
      return { success: true, message: response.message }
    } else {
      return { success: false, message: response.message }
    }
  } catch (error) {
    return { success: false, message: error.message || '注册失败' }
  }
}

// 登出
const logout = () => {
  clearUserInfo()
}

// 获取用户信息
const fetchUserInfo = async () => {
  if (!token.value) return null
  
  try {
    const response = await authAPI.getUserInfo()
    if (response.code === 200) {
      setUserInfo(response.data)
      return response.data
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
    clearUserInfo()
  }
  return null
}

// 从后端获取最新用户信息
const refreshUserInfo = async () => {
  try {
    const response = await userAPI.getUserInfo()
    if (response.code === 200) {
      setUserInfo(response.data)
      return response.data
    }
  } catch (error) {
    console.error('刷新用户信息失败:', error)
  }
  return null
}

// 用户状态管理
export const useUserStore = () => {
  // 初始化
  initUserInfo()

  return {
    // 状态
    userInfo,
    token,
    isLoggedIn,
    currentUser,
    
    // 方法
    login,
    register,
    logout,
    fetchUserInfo,
    refreshUserInfo,
    setUserInfo,
    setToken,
    clearUserInfo
  }
}

// 导出单例状态，供路由守卫使用
export const userStore = {
  get isLoggedIn() { return isLoggedIn.value },
  get currentUser() { return currentUser.value },
  login,
  register,
  logout,
  fetchUserInfo,
  refreshUserInfo,
  setUserInfo,
  setToken,
  clearUserInfo
}

// 初始化
initUserInfo() 