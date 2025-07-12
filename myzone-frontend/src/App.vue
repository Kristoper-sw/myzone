<template>
  <router-view />
</template>

<script setup>
// App.vue 现在只作为路由容器
</script>

<style>
html, body {
  margin: 0;
  padding: 0;
  background: #fff;
}

body {
  box-sizing: border-box;
}

/* Element Plus 样式覆盖 */
.el-button--danger {
  --el-button-bg-color: #e9ebec;
  --el-button-border-color: #e9ebec;
  --el-button-hover-bg-color: #e9ebec;
  --el-button-hover-border-color: #e9ebec;
}
</style>
