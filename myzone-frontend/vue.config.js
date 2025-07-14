const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  // 设置页面标题
  pages: {
    index: {
      entry: 'src/main.js',
      title: 'MyZone'
    }
  },
  devServer: {
    port: 3000,
    open: false
  },
  // 生产环境优化配置
  configureWebpack: {
    optimization: {
      splitChunks: {
        chunks: 'all',
        cacheGroups: {
          vendor: {
            test: /[\\/]node_modules[\\/]/,
            name: 'vendors',
            chunks: 'all',
            priority: 10
          },
          mapbox: {
            test: /[\\/]node_modules[\\/]mapbox-gl[\\/]/,
            name: 'mapbox',
            chunks: 'all',
            priority: 20
          },
          elementPlus: {
            test: /[\\/]node_modules[\\/]element-plus[\\/]/,
            name: 'element-plus',
            chunks: 'all',
            priority: 15
          }
        }
      }
    }
  },
  // 移除不支持的modern选项
  // modern: true,
  // 生产环境移除console
  productionSourceMap: false,
  // 启用gzip压缩
  chainWebpack: config => {
    if (process.env.NODE_ENV === 'production') {
      config.optimization.minimize(true)
      config.optimization.splitChunks({
        chunks: 'all',
        maxInitialRequests: Infinity,
        minSize: 20000,
        cacheGroups: {
          vendor: {
            test: /[\\/]node_modules[\\/]/,
            name(module) {
              const packageName = module.context.match(/[\\/]node_modules[\\/](.*?)([\\/]|$)/)[1]
              return `npm.${packageName.replace('@', '')}`
            }
          }
        }
      })
    }
  }
})
