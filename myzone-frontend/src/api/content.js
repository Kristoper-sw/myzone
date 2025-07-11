import axios from 'axios'

const API_BASE_URL = 'http://localhost:8080/api'

// 创建axios实例
const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 30000, // 30秒超时，文件上传需要更长时间
})

// 请求拦截器，添加token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  (response) => {
    return response.data
  },
  (error) => {
    console.error('API请求错误:', error)
    return Promise.reject(error)
  }
)

/**
 * 内容相关API
 */
export const contentApi = {
  /**
   * 上传内容
   * @param {Object} formData - 包含文件和数据的FormData对象
   * @returns {Promise}
   */
  uploadContent(formData) {
    return api.post('/content/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },

  /**
   * 获取用户内容列表
   * @param {number} userId - 用户ID
   * @param {number} page - 页码
   * @param {number} size - 每页大小
   * @returns {Promise}
   */
  getUserContents(userId, page = 1, size = 10) {
    return api.get(`/content/user/${userId}`, {
      params: { page, size }
    })
  },

  /**
   * 获取内容详情
   * @param {number} contentId - 内容ID
   * @returns {Promise}
   */
  getContentById(contentId) {
    return api.get(`/content/${contentId}`)
  },

  /**
   * 删除内容
   * @param {number} contentId - 内容ID
   * @returns {Promise}
   */
  deleteContent(contentId) {
    return api.delete(`/content/${contentId}`)
  },

  /**
   * 更新内容状态
   * @param {number} contentId - 内容ID
   * @param {number} status - 状态
   * @returns {Promise}
   */
  updateContentStatus(contentId, status) {
    return api.put(`/content/${contentId}/status`, null, {
      params: { status }
    })
  },

  /**
   * 获取所有地图内容
   * @returns {Promise}
   */
  getAllMapContents() {
    return api.get('/content/map/all')
  },

  /**
   * 获取指定区域的内容
   * @param {number} minLat - 最小纬度
   * @param {number} maxLat - 最大纬度
   * @param {number} minLng - 最小经度
   * @param {number} maxLng - 最大经度
   * @returns {Promise}
   */
  getContentsByArea(minLat, maxLat, minLng, maxLng) {
    return api.get('/content/map/area', {
      params: { minLat, maxLat, minLng, maxLng }
    })
  }
}

export default contentApi 