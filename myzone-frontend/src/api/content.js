import api from './auth.js'
import { ElMessage } from 'element-plus'
import router from '@/router'

// 为文件上传创建特殊的axios实例，使用更长的超时时间
const uploadApi = api.create({
  timeout: 30000,
  withCredentials: true,
})

// 自动注入token
uploadApi.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => Promise.reject(error)
)

// 添加响应拦截器
uploadApi.interceptors.response.use(
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

/**
 * 内容相关API
 */
export const contentApi = {
  uploadContent(formData) {
    return uploadApi.post('/content/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },

  getUserContents(userId, page = 1, size = 10) {
    return api.get(`/content/user/${userId}`, {
      params: { page, size }
    })
  },

  getContentById(contentId) {
    return api.get(`/content/${contentId}`)
  },

  deleteContent(contentId) {
    return api.delete(`/content/${contentId}`)
  },

  updateContentStatus(contentId, status) {
    return api.put(`/content/${contentId}/status`, null, {
      params: { status }
    })
  },

  updateContent(contentId, formData) {
    return uploadApi.put(`/content/${contentId}`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },

  getAllMapContents() {
    return api.get('/content/map/all')
  },

  getContentsByArea(minLat, maxLat, minLng, maxLng) {
    return api.get('/content/map/area', {
      params: { minLat, maxLat, minLng, maxLng }
    })
  },

  likeContent(contentId) {
    return api.post(`/content/${contentId}/like`)
  },

  unlikeContent(contentId) {
    return api.post(`/content/${contentId}/unlike`)
  },

  hasLiked(contentId) {
    return api.get(`/content/${contentId}/liked`)
  }
}

export default contentApi
