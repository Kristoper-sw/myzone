<template>
  <CommonLayout>
    <div class="post-detail-content">
      <PostDetail :post-id="postId" />
    </div>
  </CommonLayout>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import CommonLayout from '@/components/layout/CommonLayout.vue'
import PostDetail from '@/components/content/PostDetail.vue'

const route = useRoute()
const postId = computed(() => route.params.id)
</script>

<style scoped>
.post-detail-content {
  max-width: 1000px;
  margin: 0 auto;
}
</style> 