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
  --el-button-bg-color: #ff0050;
  --el-button-border-color: #ff0050;
  --el-button-hover-bg-color: #d10042;
  --el-button-hover-border-color: #d10042;
}
</style>
