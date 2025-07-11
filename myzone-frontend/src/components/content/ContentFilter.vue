<template>
  <div class="filter-section">
    <el-select 
      v-model="filterType" 
      placeholder="内容类型" 
      @change="handleTypeChange"
      clearable
    >
      <el-option label="全部" value="" />
      <el-option label="短视频" value="1" />
      <el-option label="图片" value="2" />
      <el-option label="混合" value="3" />
    </el-select>
    
    <el-select 
      v-model="filterStatus" 
      placeholder="状态" 
      @change="handleStatusChange"
      clearable
    >
      <el-option label="全部" value="" />
      <el-option label="已发布" value="1" />
      <el-option label="草稿" value="0" />
      <el-option label="已删除" value="2" />
    </el-select>
    
    <el-button type="primary" @click="$emit('upload')">
      <el-icon><Plus /></el-icon>
      上传新内容
    </el-button>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { Plus } from '@element-plus/icons-vue'

// 定义props
const props = defineProps({
  type: {
    type: String,
    default: ''
  },
  status: {
    type: String,
    default: ''
  }
})

// 定义emits
const emit = defineEmits(['filter-change', 'upload'])

// 响应式数据
const filterType = ref(props.type)
const filterStatus = ref(props.status)

// 监听props变化
watch(() => props.type, (newVal) => {
  filterType.value = newVal
})

watch(() => props.status, (newVal) => {
  filterStatus.value = newVal
})

// 处理类型变化
const handleTypeChange = () => {
  emit('filter-change', {
    type: filterType.value,
    status: filterStatus.value
  })
}

// 处理状态变化
const handleStatusChange = () => {
  emit('filter-change', {
    type: filterType.value,
    status: filterStatus.value
  })
}
</script>

<style scoped>
.filter-section {
  display: flex;
  gap: 15px;
  margin-bottom: 30px;
  align-items: center;
}

@media (max-width: 768px) {
  .filter-section {
    flex-direction: column;
    align-items: stretch;
  }
}
</style> 