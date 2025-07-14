import api from './auth.js'

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