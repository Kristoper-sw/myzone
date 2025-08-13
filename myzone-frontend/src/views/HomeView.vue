<template>
  <div class="home-map-fullscreen">
    <Suspense>
      <template #default>
        <MapComponent />
      </template>
      <template #fallback>
        <div class="map-loading">
          <div class="loading-spinner"></div>
          <p>{{ $t('map.loading') }}</p>
        </div>
      </template>
    </Suspense>
  </div>
</template>

<script setup>
import { defineAsyncComponent } from 'vue'
import { useI18n } from 'vue-i18n'
// 使用动态导入实现懒加载
const MapComponent = defineAsyncComponent(() => import('@/components/MapComponent.vue'))
const { t: $t } = useI18n()
</script>

<style scoped>
.home-map-fullscreen {
  width: 100%;
  flex: 1 1 auto;
  height: 100%;
  position: relative;
}

.map-loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  background: #f5f5f5;
  color: #666;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #e3e3e3;
  border-top: 4px solid #409eff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 16px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style>