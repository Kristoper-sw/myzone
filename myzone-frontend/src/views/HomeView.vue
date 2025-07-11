<template>
  <CommonLayout>
    <div class="home-map-fullscreen">
      <MapComponent />
    </div>
  </CommonLayout>
</template>

<script setup>
import CommonLayout from '@/components/layout/CommonLayout.vue'
import MapComponent from '@/components/MapComponent.vue'
</script>

<style scoped>
.home-map-fullscreen {
  width: 100%;
  height: 50%;
  min-width: 0;
  /* overflow: hidden;
  display: flex;
  align-items: stretch;
  justify-content: stretch; */
}

/* .home-map-fullscreen > * {
  flex: 1 1 100%;
} */
</style> 