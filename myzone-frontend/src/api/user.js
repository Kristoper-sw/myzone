import axios from 'axios'

const API_BASE_URL = 'http://localhost:8080/api'

// 创建axios实例
const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000
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
      return Promise.reject(error.response.data)
    } else {
      return Promise.reject({ message: '网络错误' })
    }
  }
)

// 用户API
export const userAPI = {
  // 获取用户信息
  getUserInfo() {
    return api.get('/user/info')
  },

  // 更新用户信息
  updateUserInfo(userData) {
    return api.put('/user/update', userData)
  }
} 