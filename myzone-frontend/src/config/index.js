// 环境配置
const config = {
  // 开发环境
  development: {
    apiBaseUrl: 'http://localhost:8080',
    uploadBaseUrl: 'http://localhost:8080'
  },
  // 生产环境
  production: {
    apiBaseUrl: 'https://www.culturezone.space',
    uploadBaseUrl: 'https://www.culturezone.space'
  }
}

// 根据当前环境获取配置
const env = process.env.NODE_ENV || 'development'
const currentConfig = config[env]

// 导出配置
export const apiBaseUrl = currentConfig.apiBaseUrl
export const uploadBaseUrl = currentConfig.uploadBaseUrl

// 获取完整的文件URL（统一使用完整URL，确保从正确的服务器获取）
export const getFileUrl = (path) => {
  if (!path) return 'https://placehold.co/80x80?text=No+Image'
  
  // 如果是JSON数组字符串，取第一项
  if (path.trim().startsWith('[')) {
    try {
      const arr = JSON.parse(path)
      if (arr.length > 0) path = arr[0]
    } catch (error) {
      console.warn('解析路径JSON失败:', error)
    }
  }
  
  if (path.startsWith('http')) return path
  if (path.startsWith('/')) return `${uploadBaseUrl}${path}`
  const fullUrl = `${uploadBaseUrl}/uploads/${path}`
  
  // 开发环境下输出调试信息
  if (env === 'development') {
    console.log(`[DEBUG] 文件路径: ${path} -> ${fullUrl}`)
    console.log(`[DEBUG] 当前环境: ${env}, uploadBaseUrl: ${uploadBaseUrl}`)
  }
  
  return fullUrl
}

// 根据环境选择文件URL函数（统一使用getFileUrl）
export const getFileUrlByEnv = getFileUrl

export default currentConfig 