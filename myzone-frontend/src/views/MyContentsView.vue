<template>
  <CommonLayout>
    <div class="my-contents-content">
      <el-card class="contents-header">
        <template #header>
          <h2>我的内容</h2>
        </template>
        <p>管理你上传的所有内容</p>
      </el-card>
      
      <ContentFilter @filter-change="handleFilterChange" />
      
      <div class="contents-grid">
        <ContentCard
          v-for="content in filteredContents"
          :key="content.id"
          :content="content"
          @click="goToDetail"
        />
      </div>
      
      <el-empty v-if="filteredContents.length === 0" description="暂无内容" />
    </div>
  </CommonLayout>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import CommonLayout from '@/components/layout/CommonLayout.vue'
import ContentCard from '@/components/content/ContentCard.vue'
import ContentFilter from '@/components/content/ContentFilter.vue'

const router = useRouter()
const contents = ref([
  { id: 1, title: '我的第一个内容', description: '这是一个测试内容', imageUrl: 'https://placehold.co/300x200', createdAt: '2024-01-15' },
  { id: 2, title: '另一个内容', description: '这是另一个测试内容', imageUrl: 'https://placehold.co/300x200', createdAt: '2024-01-10' }
])

const filterCriteria = ref({
  search: '',
  category: 'all',
  sortBy: 'newest'
})

const filteredContents = computed(() => {
  let filtered = [...contents.value]
  
  if (filterCriteria.value.search) {
    filtered = filtered.filter(content => 
      content.title.toLowerCase().includes(filterCriteria.value.search.toLowerCase()) ||
      content.description.toLowerCase().includes(filterCriteria.value.search.toLowerCase())
    )
  }
  
  if (filterCriteria.value.category !== 'all') {
    filtered = filtered.filter(content => content.category === filterCriteria.value.category)
  }
  
  if (filterCriteria.value.sortBy === 'newest') {
    filtered.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
  } else if (filterCriteria.value.sortBy === 'oldest') {
    filtered.sort((a, b) => new Date(a.createdAt) - new Date(b.createdAt))
  }
  
  return filtered
})

function handleFilterChange(criteria) {
  filterCriteria.value = { ...filterCriteria.value, ...criteria }
}

function goToDetail(content) {
  router.push(`/post/${content.id}`)
}
</script>

<style scoped>
.my-contents-content {
  max-width: 1200px;
  margin: 0 auto;
}

.contents-header {
  margin-bottom: 30px;
}

.contents-header :deep(.el-card__header) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 8px 8px 0 0;
}

.contents-header h2 {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
}

.contents-header p {
  margin: 10px 0 0 0;
  color: #666;
}

.contents-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

@media (max-width: 768px) {
  .contents-grid {
    grid-template-columns: 1fr;
  }
}
</style> 