<template>
  <div class="mosaic-content">
    <el-card class="mosaic-header">
      <template #header>
        <h2>文化拼图</h2>
      </template>
      <p>探索世界各地的文化，点击卡片查看详情</p>
    </el-card>

    <div class="mosaic-grid">
      <MosaicCard v-for="item in mosaicItems" :key="item.id" :item="item" @click="goToDetail" />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import MosaicCard from '@/components/content/MosaicCard.vue'

const router = useRouter()
const mosaicItems = ref([
  { id: 1, imageUrl: 'https://placehold.co/200x120', title: '京剧脸谱', location: '北京', cultureType: '戏剧' },
  { id: 2, imageUrl: 'https://placehold.co/200x120', title: '和服文化', location: '东京', cultureType: '服饰' },
  { id: 3, imageUrl: 'https://placehold.co/200x120', title: '美式涂鸦', location: '纽约', cultureType: '艺术' },
  { id: 4, imageUrl: 'https://placehold.co/200x120', title: '法式甜点', location: '巴黎', cultureType: '美食' },
  { id: 5, imageUrl: 'https://placehold.co/200x120', title: '非洲鼓', location: '拉各斯', cultureType: '音乐' },
  { id: 6, imageUrl: 'https://placehold.co/200x120', title: '印度节日', location: '孟买', cultureType: '节日' }
])

function goToDetail(item) {
  router.push(`/post/${item.id}`)
}
</script>

<style scoped>
.mosaic-content {
  max-width: 1200px;
  margin: 0 auto;
}

.mosaic-header {
  margin-bottom: 30px;
}

.mosaic-header :deep(.el-card__header) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 8px 8px 0 0;
}

.mosaic-header h2 {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
}

.mosaic-header p {
  margin: 10px 0 0 0;
  color: #666;
}

.mosaic-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 20px;
}

@media (max-width: 768px) {
  .mosaic-grid {
    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
    gap: 15px;
  }
}
</style>