<template>
  <div class="common-layout">
    <el-container direction="vertical" style="min-height: 100vh;">
      <el-header class="header">
        <div class="header-content">
          <div class="header-left">
            <div class="logo" @click="$router.push('/')">MyZone</div>
            <el-menu
              :default-active="activeIndex"
              mode="horizontal"
              background-color="transparent"
              text-color="#FFFFFF"
              active-text-color="#CCCCFF"
              @select="handleSelect"
              class="nav-menu"
              :ellipsis="false"
            >
              <el-menu-item index="/">首页</el-menu-item>
              <!-- <el-menu-item index="/mosaic">拼图</el-menu-item> -->
              <el-menu-item index="/upload">上传内容</el-menu-item>
              <!-- <el-menu-item index="/my-contents">我的内容</el-menu-item> -->
            </el-menu>
          </div>
          <div class="header-right">
            <template v-if="isLoggedIn">
              <el-dropdown @command="handleCommand" trigger="click">
                <div class="user-info">
                  <span class="username">{{ displayName }}</span>
                  <el-avatar :size="32" :src="currentUser?.avatar" class="user-avatar">
                    {{ avatarText }}
                  </el-avatar>
                </div>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="profile">
                      <el-icon><User /></el-icon>个人资料
                    </el-dropdown-item>
                    <el-dropdown-item command="my-contents">
                      <el-icon><Document /></el-icon>我的内容
                    </el-dropdown-item>
                    <!-- <el-dropdown-item command="upload">
                      <el-icon><Upload /></el-icon>上传内容
                    </el-dropdown-item> -->
                    <el-dropdown-item divided command="logout">
                      <el-icon><SwitchButton /></el-icon>退出登录
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </template>
            <template v-else>
              <el-button type="primary" @click="$router.push('/login')">登录</el-button>
              <el-button type="danger" @click="$router.push('/register')">注册</el-button>
            </template>
          </div>
        </div>
      </el-header>
      <el-main class="main">
        <router-view></router-view>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useUserStore } from '@/stores/user'
import { useRouter, useRoute } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import { User, Document, SwitchButton } from '@element-plus/icons-vue'

const userStore = useUserStore()
const router = useRouter()
const route = useRoute()

// 正确解构用户状态
const isLoggedIn = userStore.isLoggedIn
const currentUser = userStore.currentUser

const activeIndex = computed(() => route.path)

const displayName = computed(() => {
  if (!currentUser.value) return ''
  return currentUser.value.nickname || currentUser.value.username || ''
})

const avatarText = computed(() => {
  if (!currentUser.value) return ''
  const name = currentUser.value.nickname || currentUser.value.username || ''
  return name.charAt(0).toUpperCase()
})

const handleSelect = (key) => {
  router.push(key)
}

const handleCommand = async (command) => {
  switch (command) {
    case 'profile':
      router.push('/profile')
      break
    case 'my-contents':
      router.push('/my-contents')
      break
    case 'upload':
      router.push('/upload')
      break
    case 'logout':
      try {
        await ElMessageBox.confirm(
          '确定要退出登录吗？',
          '提示',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
          }
        )
        userStore.logout()
        router.push('/login')
        ElMessage.success('已退出登录')
      } catch {
        // 用户取消
      }
      break
  }
}
</script>

<style scoped>
.common-layout {
  min-height: 100vh;
  background: linear-gradient(135deg, rgba(66, 134, 244, 0.02), rgba(55, 59, 68, 0.02));
}

.header {
  background: linear-gradient(to right, rgb(55, 59, 68), rgb(66, 134, 244));
  color: white;
  box-shadow: 0 4px 20px rgba(55, 59, 68, 0.15);
  padding: 0;
  height: 50px;
  display: flex;
  align-items: center;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
  width: 100%;
}

.header-left {
  display: flex;
  align-items: center;
  flex: 1;
  min-width: 0;
}

.logo {
  background: linear-gradient(270deg, #ff4ecf, #66ccff, #4effd7, #fffb4e, #ff4ecf);
  background-size: 1000% 1000%;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  animation: rainbow 6s ease infinite;
  text-shadow: 0 0 10px rgba(255, 255, 255, 0.5);
  font-size: 22px;
  font-weight: bold;
  margin-right: 10px;
  margin-left: 30px;
  letter-spacing: 2px;
}

@keyframes rainbow {
  0% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0% 50%;
  }
}


.logo:hover {
  text-shadow: 0 0 10px rgba(255, 255, 255, 0.5);
}

.nav-menu {
  background: transparent;
  border:0!important;
  height: 50px;
  flex: 1;
  min-width: 0;
  overflow: hidden;
  margin-left: 10px;
}

.el-menu--horizontal > .el-sub-menu.is-overflow {
  display: none !important;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-right: 10px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  color: white;
  margin-right: 10px;
  transition: all 0.3s ease;
}

.user-info:hover {
  opacity: 0.8;
}

.username {
  color: white;
  font-size: 14px;
  max-width: 120px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.user-avatar {
  border: 2px solid transparent;
  transition: border-color 0.3s ease;
}

.user-avatar:hover {
  border-color: #ffffff;
}

.main {
  background: transparent;
  padding: 0 0 0 0;
  min-height: calc(100vh - 50px);
  display: flex;
  flex-direction: column;
}

@media (max-width: 768px) {
  .header-content {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  .header-left {
    gap: 10px;
  }
  .main {
    padding: 10px 0 0 0;
  }
}
</style> 