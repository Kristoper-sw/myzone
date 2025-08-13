<template>
  <div class="mosaic-content">
    <el-card class="mosaic-header">
      <template #header>
        <h2>{{ $t('mosaic.culturalMosaic') }}</h2>
      </template>
      <p>{{ $t('mosaic.exploreCultures') }}</p>
    </el-card>

    <div class="mosaic-grid">
      <MosaicCard v-for="item in mosaicItems" :key="item.id" :item="item" @click="goToDetail" />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n'
import MosaicCard from '@/components/content/MosaicCard.vue'

const { t: $t } = useI18n()
const router = useRouter()
const mosaicItems = ref([
  { id: 1, imageUrl: 'https://placehold.co/200x120', title: $t('mosaic.pekingOpera'), location: $t('mosaic.beijing'), cultureType: $t('mosaic.drama') },
  { id: 2, imageUrl: 'https://placehold.co/200x120', title: $t('mosaic.kimonoCulture'), location: $t('mosaic.tokyo'), cultureType: $t('mosaic.clothing') },
  { id: 3, imageUrl: 'https://placehold.co/200x120', title: $t('mosaic.americanGraffiti'), location: $t('mosaic.newYork'), cultureType: $t('mosaic.art') },
  { id: 4, imageUrl: 'https://placehold.co/200x120', title: $t('mosaic.frenchDesserts'), location: $t('mosaic.paris'), cultureType: $t('mosaic.food') },
  { id: 5, imageUrl: 'https://placehold.co/200x120', title: $t('mosaic.africanDrums'), location: $t('mosaic.lagos'), cultureType: $t('mosaic.music') },
  { id: 6, imageUrl: 'https://placehold.co/200x120', title: $t('mosaic.indianFestivals'), location: $t('mosaic.mumbai'), cultureType: $t('mosaic.festival') }
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