import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
// 移除同步加载的Mapbox CSS
// import 'mapbox-gl/dist/mapbox-gl.css'
// 导入Element Plus插件
import ElementPlus from './plugins/element-plus'
import './assets/styles/global.css'

const app = createApp(App)
app.use(router)
app.use(ElementPlus)
app.mount('#app')
