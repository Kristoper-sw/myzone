import { createRouter, createWebHistory } from 'vue-router'
import { userStore } from '@/stores/user'
import HomeView from '../views/HomeView.vue'
import MosaicView from '../views/MosaicView.vue'
import PostDetailView from '../views/PostDetailView.vue'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import ProfileView from '../views/ProfileView.vue'
import UploadView from '../views/UploadView.vue'
import MyContentsView from '../views/MyContentsView.vue'

const routes = [
  { 
    path: '/', 
    name: 'Home', 
    component: HomeView,
    meta: { requiresAuth: false }
  },
  { 
    path: '/mosaic', 
    name: 'Mosaic', 
    component: MosaicView,
    meta: { requiresAuth: true }
  },
  { 
    path: '/post/:id', 
    name: 'PostDetail', 
    component: PostDetailView,
    meta: { requiresAuth: true }
  },
  { 
    path: '/login', 
    name: 'Login', 
    component: LoginView,
    meta: { requiresGuest: true }
  },
  { 
    path: '/register', 
    name: 'Register', 
    component: RegisterView,
    meta: { requiresGuest: true }
  },
  { 
    path: '/profile', 
    name: 'Profile', 
    component: ProfileView,
    meta: { requiresAuth: true }
  },
  { 
    path: '/upload', 
    name: 'Upload', 
    component: UploadView,
    meta: { requiresAuth: true }
  },
  { 
    path: '/my-contents', 
    name: 'MyContents', 
    component: MyContentsView,
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const isLoggedIn = userStore.isLoggedIn

  console.log('路由守卫检查:', {
    to: to.path,
    isLoggedIn,
    requiresAuth: to.meta.requiresAuth,
    requiresGuest: to.meta.requiresGuest
  })

  // 需要登录的页面
  if (to.meta.requiresAuth && !isLoggedIn) {
    console.log('需要登录，跳转到登录页')
    next('/login')
    return
  }

  // 已登录用户不能访问登录/注册页面
  if (to.meta.requiresGuest && isLoggedIn) {
    console.log('已登录用户，跳转到首页')
    next('/')
    return
  }

  console.log('允许访问:', to.path)
  next()
})

export default router 