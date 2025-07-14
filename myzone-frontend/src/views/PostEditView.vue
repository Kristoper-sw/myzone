<template>
  <div class="post-edit-content">
    <ContentUpload :edit-id="postId" mode="edit" />
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import ContentUpload from '@/components/ContentUpload.vue'

const route = useRoute()
const postId = computed(() => route.params.id)
</script>

<style scoped>
.post-edit-content {
  max-width: 1000px;
  margin: 0 auto;
}
</style> 