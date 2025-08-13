<template>
  <el-dropdown @command="handleLanguageChange" trigger="click">
    <div class="language-switcher">
      <el-icon class="language-icon">
        <Location />
      </el-icon>
      <span class="language-text">{{ currentLanguageText }}</span>
      <el-icon class="arrow-icon">
        <ArrowDown />
      </el-icon>
    </div>
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item 
          command="zh-CN" 
          :class="{ 'is-active': currentLocale === 'zh-CN' }"
        >
          <span class="flag">🇨🇳</span>
          中文
        </el-dropdown-item>
        <el-dropdown-item 
          command="en-US" 
          :class="{ 'is-active': currentLocale === 'en-US' }"
        >
          <span class="flag">🇺🇸</span>
          English
        </el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>

<script setup>
import { computed } from 'vue'
import { useI18n } from 'vue-i18n'
import { Location, ArrowDown } from '@element-plus/icons-vue'

const { locale } = useI18n()

const currentLocale = computed(() => locale.value)

const currentLanguageText = computed(() => {
  return currentLocale.value === 'zh-CN' ? '中文' : 'English'
})

const handleLanguageChange = (lang) => {
  locale.value = lang
  localStorage.setItem('locale', lang)
}
</script>

<style scoped>
.language-switcher {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 8px 12px;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s ease;
  color: #fff;
}

.language-switcher:hover {
  background: rgba(255, 255, 255, 0.1);
}

.language-icon {
  font-size: 16px;
}

.language-text {
  font-size: 14px;
  font-weight: 500;
}

.arrow-icon {
  font-size: 12px;
  margin-left: 4px;
}

.flag {
  margin-right: 8px;
  font-size: 16px;
}

:deep(.el-dropdown-menu__item.is-active) {
  color: var(--el-color-primary);
  background-color: var(--el-color-primary-light-9);
}

:deep(.el-dropdown-menu__item:hover) {
  background-color: var(--el-color-primary-light-9);
}
</style>
