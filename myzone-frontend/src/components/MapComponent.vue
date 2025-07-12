<template>
  <div id="map" class="mapbox-absolute"></div>
  <button class="xh-reset-btn-fab" @click="handleResetClick" title="原始视图">
    <el-icon :size="28" color="rgb(66, 134, 244)">
      <HomeFilled />
    </el-icon>
  </button>
  <div v-if="showDialog" class="xh-popup-mask" @click.self="showDialog = false">
    <div class="xh-popup-card-xhs">
      <div class="xh-popup-img-col" style="height:100%">
        <el-carousel v-if="mediaList.length > 0" height="100%" indicator-position="outside">
          <el-carousel-item v-for="(media, idx) in mediaList" :key="idx">
            <video v-if="media.type === 'video'" :src="media.url" controls
              style="width:100%;height:100%;background:#000;object-fit:cover;" />
            <img v-else :src="media.url" style="width:100%;height:100%;object-fit:cover;" />
          </el-carousel-item>
        </el-carousel>
        <div v-else class="no-media">无媒体内容</div>
      </div>
      <div class="xh-popup-content-col">
        <div class="xh-popup-header-xhs">
          <img v-if="currentPost.user" :src="currentPost.user.avatar" class="xh-popup-avatar-xhs" />
          <span v-if="currentPost.user" class="xh-popup-username-xhs">{{ currentPost.user.nickname ||
            currentPost.user.username }}</span>
          <button class="xh-popup-follow-xhs">关注</button>
          <button class="xh-popup-close-xhs" @click="showDialog = false">×</button>
        </div>
        <div class="xh-popup-title-xhs">{{ currentPost.title }}</div>
        <div class="xh-popup-desc-xhs">{{ currentPost.description }}</div>
        <div class="xh-popup-location-xhs" v-if="currentPost.location">
          <el-icon>
            <Location />
          </el-icon>
          <span>{{ currentPost.location }}</span>
        </div>
        <div class="xh-popup-actions-xhs">
          <span>❤️ {{ currentPost.likes || 0 }}</span>
          <span>💬 {{ currentPost.comments || 0 }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, computed } from 'vue'
import mapboxgl from 'mapbox-gl'
import { ElIcon } from 'element-plus'
import { HomeFilled, Location } from '@element-plus/icons-vue'
import { contentApi } from '@/api/content'

let map

const showDialog = ref(false)
const currentPost = ref({})
const mapData = ref([])

const initialCenter = [116.397389, 39.908722]
const initialZoom = 2

function handleResetClick() {
  if (map) {
    map.flyTo({ center: initialCenter, zoom: initialZoom, speed: 2 })
  }
  showDialog.value = false
}

// 计算当前弹窗的媒体列表
const mediaList = computed(() => {
  const list = []
  // 处理视频
  if (currentPost.value.videoUrl) {
    list.push({ type: 'video', url: getFileUrl(currentPost.value.videoUrl) })
  }
  // 处理图片（支持数组或单个字符串）
  let imgs = currentPost.value.imageUrl
  if (imgs) {
    if (typeof imgs === 'string' && imgs.trim().startsWith('[')) {
      try {
        imgs = JSON.parse(imgs)
      } catch (error) {
        console.warn('解析图片JSON失败:', error)
      }
    }
    if (typeof imgs === 'string') imgs = [imgs]
    imgs.forEach(img => {
      if (img) list.push({ type: 'image', url: getFileUrl(img) })
    })
  }
  return list
})

// 获取地图数据
const loadMapData = async () => {
  try {
    const response = await contentApi.getAllMapContents()
    if (response.code === 200) {
      mapData.value = response.data || []
      addMarkersToMap()
    } else {
      console.error('获取地图数据失败:', response.message)
    }
  } catch (error) {
    console.error('获取地图数据失败:', error)
  }
}

// 添加标记到地图
const addMarkersToMap = () => {
  if (!map || !mapData.value.length) return

  mapData.value.forEach(item => {
    if (!item.latitude || !item.longitude) return

    const el = document.createElement('div')
    el.style.backgroundImage = `url(${getFileUrl(item.imageUrl)})`
    el.style.width = '80px'
    el.style.height = '80px'
    el.style.backgroundSize = 'cover'
    el.style.border = '2px solid #fff'
    el.style.boxShadow = '0 2px 8px #0003'
    el.style.cursor = 'pointer'
    el.style.borderRadius = '10px'
    el.title = item.title || '内容'

    el.addEventListener('click', () => {
      map.flyTo({
        center: [item.longitude, item.latitude],
        zoom: 10,
        speed: 2
      })
      currentPost.value = item
      showDialog.value = true
    })

    new mapboxgl.Marker(el)
      .setLngLat([item.longitude, item.latitude])
      .addTo(map)
  })
}

// 获取文件URL
const getFileUrl = (path) => {
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
  if (path.startsWith('/')) return `http://localhost:8080${path}`
  return `http://localhost:8080/uploads/${path}`
}

onMounted(async () => {
  mapboxgl.accessToken = 'pk.eyJ1Ijoia3Jpc3RvcGhlcnFxIiwiYSI6ImNtYnl2c2RhMDAyY3IybXNmZzY5cmFhYWQifQ.DgqaIt3pvVHrEOmOmTO5aQ'
  map = new mapboxgl.Map({
    container: 'map',
    style: 'mapbox://styles/mapbox/streets-v11',
    center: [116.397389, 39.908722],
    zoom: 2
  })

  map.on('load', () => {
    loadMapData()
  })
})

onBeforeUnmount(() => {
  if (map) map.remove()
})
</script>

<style scoped>
.mapbox-absolute {
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  width: 100%;
  height: 100%;
}

.xh-popup-mask {
  position: fixed;
  z-index: 9999;
  left: 0;
  top: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.35);
  display: flex;
  align-items: center;
  justify-content: center;
}

.xh-popup-card-xhs {
  background: #fff;
  border-radius: 18px;
  width: 980px;
  max-width: 99vw;
  min-height: 480px;
  height: 100%;
  max-height: 85vh;
  box-shadow: 0 8px 32px #0003;
  display: flex;
  overflow: hidden;
  animation: popup-in 0.2s;
}

@keyframes popup-in {
  from {
    transform: scale(0.95);
    opacity: 0;
  }

  to {
    transform: scale(1);
    opacity: 1;
  }
}

.xh-popup-img-col {
  flex: 1 1 0;
  display: flex;
  align-items: stretch;
  justify-content: stretch;
  background: #000;
  padding: 0;
  height: 100%;
  min-width: 0;
  min-height: 0;
}

.el-carousel,
.el-carousel__container,
.el-carousel__item {
  width: 100% !important;
  height: 100% !important;
}

.xh-popup-img-xhs,
.xh-popup-img-xhs img,
.xh-popup-img-xhs video {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
  background: #000;
  border-radius: 0;
  margin: 0;
  box-shadow: none;
  max-width: none;
  max-height: none;
}

.xh-popup-content-col {
  flex: 1 1 0;
  padding: 32px 32px 24px 32px;
  display: flex;
  flex-direction: column;
  min-width: 0;
}

.xh-popup-header-xhs {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.xh-popup-avatar-xhs {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 8px;
}

.xh-popup-username-xhs {
  font-weight: bold;
  margin-right: auto;
}

.xh-popup-follow-xhs {
  background: #ff0050;
  color: #fff;
  border: none;
  border-radius: 16px;
  padding: 4px 12px;
  margin-right: 8px;
  cursor: pointer;
}

.xh-popup-close-xhs {
  background: none;
  border: none;
  font-size: 1.5em;
  color: #888;
  cursor: pointer;
}

.xh-popup-title-xhs {
  font-weight: bold;
  font-size: 1.1em;
  margin-bottom: 8px;
}

.xh-popup-desc-xhs {
  color: #555;
  font-size: 0.98em;
  margin-bottom: 12px;
  word-break: break-all;
}

.xh-popup-location-xhs {
  display: flex;
  align-items: center;
  gap: 5px;
  color: #666;
  font-size: 0.9em;
  margin-bottom: 12px;
}

.xh-popup-actions-xhs {
  display: flex;
  gap: 18px;
  color: #ff0050;
  font-size: 1.1em;
  margin-bottom: 16px;
}

.xh-reset-btn-fab {
  position: fixed;
  bottom: 24px;
  right: 32px;
  z-index: 10001;
  background: #fff;
  color: #333;
  border: 1px solid #eee;
  border-radius: 20px;
  padding: 8px 20px;
  box-shadow: 0 2px 8px #0002;
  font-size: 1em;
  cursor: pointer;
  transition: background 0.2s, box-shadow 0.2s;
}

.xh-reset-btn-fab:hover {
  background: #f5f5f5;
  box-shadow: 0 4px 16px #0002;
}
</style>