<template>
  <div class="mosaic-card" @click="$emit('click', item)">
    <img :src="item.imageUrl" :alt="item.title" class="mosaic-img" />
    <div class="mosaic-title">{{ item.title }}</div>
    <div class="mosaic-location">{{ item.location }}</div>
  </div>
</template>

<script setup>
// 定义props
defineProps({
  item: {
    type: Object,
    required: true
  }
})

// 定义emits
defineEmits(['click'])
</script>

<style scoped>
.mosaic-card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px #0001;
  cursor: pointer;
  overflow: hidden;
  transition: box-shadow 0.2s;
}

.mosaic-card:hover {
  box-shadow: 0 4px 16px #0002;
}

.mosaic-img {
  width: 100%;
  height: 120px;
  object-fit: cover;
}

.mosaic-title {
  font-weight: bold;
  margin: 8px 0 4px 0;
  padding: 0 8px;
}

.mosaic-location {
  color: #888;
  font-size: 0.9em;
  padding: 0 8px 8px 8px;
}
</style> 