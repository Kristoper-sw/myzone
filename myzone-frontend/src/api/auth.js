import axios from 'axios'
import router from '@/router'
import { ElMessage } from 'element-plus'
import { apiBaseUrl } from '@/config'

// 创建axios实例
const api = axios.create({
  baseURL: `${apiBaseUrl}/api`,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器 - 添加token
api.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器 - 处理错误
api.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    if (error.response) {
      // 服务器返回错误状态码
      const { status, data } = error.response
      if (status === 401) {
        // 未授权，清除token并跳转到登录页
        localStorage.removeItem('token')
        localStorage.removeItem('userInfo')
        
        // 显示提示信息
        ElMessage.error('登录已过期，请重新登录')
        
        // 使用Vue Router跳转到登录页
        router.push('/login')
      }
      return Promise.reject(data || error.message)
    } else if (error.request) {
      // 请求发送失败
      return Promise.reject('网络连接失败，请检查网络设置')
    } else {
      // 其他错误
      return Promise.reject(error.message)
    }
  }
)

// 认证相关API
export const authAPI = {
  // 用户登录
  login: (data) => {
    return api.post('/user/login', data)
  },

  // 用户注册
  register: (data) => {
    return api.post('/user/register', data)
  },

  // 获取用户信息
  getUserInfo: () => {
    return api.get('/user/info')
  },

  // 健康检查
  health: () => {
    return api.get('/user/health')
  }
}

export default api 