<template>
  <div class="my-contents-content">
    <el-card class="contents-card" shadow="hover">
      <template #header>
        <h2>我的内容</h2>
        <p class="subtitle">管理你上传的所有内容</p>
      </template>

      <ContentFilter @filter-change="handleFilterChange" />

      <el-divider />

      <!-- 加载状态 -->
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="3" animated />
        <el-skeleton :rows="3" animated />
        <el-skeleton :rows="3" animated />
      </div>

      <!-- 内容列表 -->
      <div v-else-if="filteredContents.length > 0" class="contents-grid">
        <ContentCard
          v-for="content in filteredContents"
          :key="content.id"
          :content="content"
          @view="goToDetail"
          @edit="goToEdit"
          @delete="handleDelete"
        />
      </div>

      <!-- 空状态 -->
      <el-empty v-else description="暂无内容" />

      <!-- 分页 -->
      <div v-if="!loading && total > 0" class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import ContentCard from '@/components/content/ContentCard.vue'
import ContentFilter from '@/components/content/ContentFilter.vue'
import { contentApi } from '@/api/content'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const { currentUser } = useUserStore()

// 响应式数据
const contents = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const filterCriteria = ref({
  search: '',
  category: 'all',
  sortBy: 'newest'
})

// 计算属性 - 过滤后的内容
const filteredContents = computed(() => {
  let filtered = [...contents.value]

  // 搜索过滤
  if (filterCriteria.value.search) {
    filtered = filtered.filter(content =>
      (content.diary && content.diary.toLowerCase().includes(filterCriteria.value.search.toLowerCase())) ||
      (content.location && content.location.toLowerCase().includes(filterCriteria.value.search.toLowerCase()))
    )
  }

  // 类型过滤
  if (filterCriteria.value.category !== 'all') {
    const categoryMap = {
      'video': 1,
      'image': 2,
      'mixed': 3
    }
    const targetType = categoryMap[filterCriteria.value.category]
    if (targetType) {
      filtered = filtered.filter(content => content.contentType === targetType)
    }
  }

  // 排序
  if (filterCriteria.value.sortBy === 'newest') {
    filtered.sort((a, b) => new Date(b.createTime) - new Date(a.createTime))
  } else if (filterCriteria.value.sortBy === 'oldest') {
    filtered.sort((a, b) => new Date(a.createTime) - new Date(b.createTime))
  }

  return filtered
})

// 获取用户内容
const fetchUserContents = async () => {
  if (!currentUser.value?.userId) {
    console.log(currentUser.value)
    ElMessage.error('用户信息获取失败')
    return
  }

  loading.value = true
  try {
    const response = await contentApi.getUserContents(
      currentUser.value.userId,
      currentPage.value,
      pageSize.value
    )
    
    if (response.code === 200) {
      // 修正：正确解析两层 data
      contents.value = response.data.data || []
      total.value = response.data.total || 0
    } else {
      ElMessage.error(response.message || '获取内容失败')
    }
  } catch (error) {
    console.error('获取用户内容失败:', error)
    ElMessage.error(error.message || '获取内容失败')
  } finally {
    loading.value = false
  }
}

// 处理筛选变化
function handleFilterChange(criteria) {
  filterCriteria.value = { ...filterCriteria.value, ...criteria }
}

// 处理分页大小变化
const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
  fetchUserContents()
}

// 处理页码变化
const handleCurrentChange = (page) => {
  currentPage.value = page
  fetchUserContents()
}

// 跳转到详情页
function goToDetail(content) {
  router.push(`/post/${content.id}`)
}

// 跳转到编辑页
function goToEdit(content) {
  router.push(`/edit/${content.id}`)
}

// 处理删除
const handleDelete = async (content) => {
  try {
    await ElMessageBox.confirm(
      '确定要删除这个内容吗？删除后无法恢复。',
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const response = await contentApi.deleteContent(content.id)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      // 重新获取数据
      await fetchUserContents()
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除内容失败:', error)
      ElMessage.error(error.message || '删除失败')
    }
  }
}

// 组件挂载时获取数据
onMounted(() => {
  fetchUserContents()
})
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

.loading-container {
  padding: 20px;
}

.loading-container :deep(.el-skeleton) {
  margin-bottom: 20px;
}

.contents-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 30px;
  padding: 20px 0;
}

.pagination-container :deep(.el-pagination) {
  --el-pagination-bg-color: white;
  --el-pagination-border-radius: 8px;
  --el-pagination-button-color: rgb(66, 134, 244);
  --el-pagination-button-bg-color: white;
  --el-pagination-button-border-color: rgb(66, 134, 244);
  --el-pagination-button-hover-color: white;
  --el-pagination-button-hover-bg-color: rgb(66, 134, 244);
  --el-pagination-button-disabled-color: rgba(66, 134, 244, 0.3);
  --el-pagination-button-disabled-bg-color: rgba(66, 134, 244, 0.1);
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
