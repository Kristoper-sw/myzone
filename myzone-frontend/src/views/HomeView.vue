<template>
  <div class="home-map-fullscreen">
    <MapComponent />
  </div>
</template>

<script setup>
import MapComponent from '@/components/MapComponent.vue'
</script>

<style scoped>
.home-map-fullscreen {
  width: 100%;
  flex: 1 1 auto;
  height: 100%;
  position: relative;
}
</style>