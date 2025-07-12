<template>
  <div class="my-contents-content">
    <el-card class="contents-card" shadow="hover">
      <template #header>
        <h2>我的内容</h2>
        <p class="subtitle">管理你上传的所有内容</p>
      </template>

      <ContentFilter @filter-change="handleFilterChange" />

      <el-divider />

      <div v-if="filteredContents.length > 0" class="contents-grid">
        <ContentCard
          v-for="content in filteredContents"
          :key="content.id"
          :content="content"
          @click="goToDetail"
        />
      </div>

      <el-empty v-else description="暂无内容" />
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
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
  min-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.contents-card {
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(55, 59, 68, 0.12);
  border: none;
}

.contents-card :deep(.el-card__header) {
  background: linear-gradient(to right, rgb(55, 59, 68), rgb(66, 134, 244));
  color: white;
  border-radius: 16px 16px 0 0;
  padding: 24px 20px;
}

.contents-card h2 {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
  letter-spacing: 1px;
}

.contents-card .subtitle {
  margin-top: 8px;
  font-size: 14px;
  color: rgba(255,255,255,0.8);
}

.contents-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

/* 美化 ContentFilter */
:deep(.content-filter) {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(55, 59, 68, 0.08);
  padding: 20px;
  margin-bottom: 20px;
  border: 1px solid rgba(66, 134, 244, 0.1);
}

:deep(.content-filter .el-input__wrapper) {
  border-radius: 8px;
  border-color: rgb(66, 134, 244);
  transition: all 0.3s ease;
}

:deep(.content-filter .el-input__wrapper:hover) {
  border-color: rgb(55, 59, 68);
  box-shadow: 0 0 0 2px rgba(66, 134, 244, 0.1);
}

:deep(.content-filter .el-select .el-input__wrapper) {
  border-radius: 8px;
  border-color: rgb(66, 134, 244);
}

:deep(.content-filter .el-button--primary) {
  background: linear-gradient(to right, rgb(55, 59, 68), rgb(66, 134, 244));
  border: none;
  border-radius: 8px;
  font-weight: 600;
  transition: all 0.3s ease;
}

:deep(.content-filter .el-button--primary:hover) {
  box-shadow: 0 4px 12px rgba(66, 134, 244, 0.3);
  transform: translateY(-1px);
}

/* 美化 el-empty */
:deep(.el-empty) {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(55, 59, 68, 0.08);
  padding: 40px;
  margin-top: 20px;
}

@media (max-width: 768px) {
  .contents-grid {
    grid-template-columns: 1fr;
  }

  .my-contents-content {
    padding: 10px;
  }
}
</style>
