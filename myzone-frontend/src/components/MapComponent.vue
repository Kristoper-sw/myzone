<template>
  <div id="map" class="mapbox-absolute"></div>
  <button class="xh-reset-btn-fab" @click="handleResetClick" title="原始视图">
    <el-icon :size="28" color="rgb(66, 134, 244)">
      <HomeFilled />
    </el-icon>
  </button>
  <div v-if="showDialog" class="xh-popup-mask" @click.self="showDialog = false">
    <div class="xh-popup-card-xhs">
      <div class="xh-popup-img-col" style="height:100%">
        <el-carousel v-if="mediaList.length > 0" height="100%" indicator-position="outside">
          <el-carousel-item v-for="(media, idx) in mediaList" :key="idx">
            <video v-if="media.type === 'video'" :src="media.url" controls
              style="width:100%;height:100%;background:#000;object-fit:cover;" />
            <img v-else :src="media.url" style="width:100%;height:100%;object-fit:cover;" />
          </el-carousel-item>
        </el-carousel>
        <div v-else class="no-media">无媒体内容</div>
      </div>
      <div class="xh-popup-content-col">
        <div class="xh-popup-header-xhs">
          <img v-if="currentPost.user" :src="currentPost.user.avatar || defaultAvatar" class="xh-popup-avatar-xhs" />
          <span v-if="currentPost.user" class="xh-popup-username-xhs">{{ currentPost.user.nickname ||
            currentPost.user.username }}</span>
          <button class="xh-popup-close-xhs" @click="showDialog = false">×</button>
        </div>
        <div class="xh-popup-title-xhs">{{ currentPost.title }}</div>
        <div class="xh-popup-desc-xhs">{{ currentPost.description }}</div>
        <div class="xh-popup-location-xhs" v-if="currentPost.location">
          <el-icon>
            <Location />
          </el-icon>
          <span>{{ currentPost.location }}</span>
        </div>
        <div class="xh-popup-actions-xhs">
          <button :disabled="likeLoading" @click.stop="handleLike" :class="{ liked: currentPost._liked }" style="background:none;border:none;cursor:pointer;font-size:1.1em;">
            <span v-if="currentPost._liked">❤️</span>
            <span v-else>🤍</span>
            {{ currentPost.likes || 0 }}
          </button>
          <span>💬 {{ commentTotal || 0 }}</span>
        </div>
        <!-- 评论区 -->
        <div class="xh-comment-section">
          <div class="xh-comment-input">
            <el-input v-model="commentInput" placeholder="说点什么..." maxlength="200" show-word-limit @keyup.enter="handleAddComment" />
            <el-button type="primary" size="small" @click="handleAddComment" :loading="commentLoading" style="margin-left:8px;">评论</el-button>
          </div>
          <div class="xh-comment-list" ref="commentListRef" @scroll="onCommentScroll">
            <div v-for="comment in commentList" :key="comment.id" class="xh-comment-item">
              <div class="xh-comment-main">
                <img :src="comment.user?.avatar || defaultAvatar" class="xh-comment-avatar" />
                <span class="xh-comment-user">{{ comment.user?.nickname || comment.user?.username || '用户' }}</span>
                <span class="xh-comment-content">{{ comment.content }}</span>
                <span class="xh-comment-time">{{ formatTime(comment.createTime) }}</span>
                <el-button text size="small" @click="replyVisible[comment.id]=!replyVisible[comment.id]">{{ replyVisible[comment.id] ? '收起回复' : '回复' }}</el-button>
                <el-button
                  v-if="comment.user?.userId && currentUser?.userId === comment.user.userId && deleteConfirmId !== comment.id"
                  text size="small"
                  @click="deleteConfirmId = comment.id"
                >删除</el-button>
                <span v-if="deleteConfirmId === comment.id">
                  <el-button text size="small" @click="handleDeleteComment(comment.id, false)">确认删除</el-button>
                  <el-button text size="small" @click="deleteConfirmId = null">取消</el-button>
                </span>
              </div>
              <div v-if="replyVisible[comment.id]" class="xh-reply-box">
                <el-input v-model="replyInput[comment.id]" placeholder="回复..." size="small" maxlength="200" show-word-limit @keyup.enter="() => handleReply(comment.id)" />
                <el-button type="primary" size="small" @click="() => handleReply(comment.id)" :loading="repliesLoading[comment.id]" style="margin-left:8px;">发送</el-button>
              </div>
              <div v-if="comment.replyCount > 0">
                <div class="xh-reply-fold-bar" @click="toggleReplies(comment.id)">
                  <span>{{ repliesVisible[comment.id] ? '收起回复' : `展开${comment.replyCount}条回复` }}</span>
                </div>
                <div v-if="repliesVisible[comment.id]" class="xh-reply-list">
                  <div v-for="reply in replies[comment.id]" :key="reply.id" class="xh-reply-item">
                    <img :src="reply.user?.avatar || defaultAvatar" class="xh-comment-avatar" />
                    <span class="xh-comment-user">{{ reply.user?.nickname || reply.user?.username || '用户' }}</span>
                    <span class="xh-comment-content">{{ reply.content }}</span>
                    <span class="xh-comment-time">{{ formatTime(reply.createTime) }}</span>
                    <el-button
                      v-if="reply.user?.userId && currentUser?.userId === reply.user.userId && deleteConfirmId !== reply.id"
                      text size="small"
                      @click="deleteConfirmId = reply.id"
                    >删除</el-button>
                    <span v-if="deleteConfirmId === reply.id">
                      <el-button text size="small" type="danger" @click="handleDeleteComment(reply.id, true, comment.id)">确认删除</el-button>
                      <el-button text size="small" @click="deleteConfirmId = null">取消</el-button>
                    </span>
                  </div>
                </div>
              </div>
            </div>
            <div v-if="commentLoading" class="xh-comment-loading">加载中...</div>
            <div v-if="!hasMoreComments && commentList.length > 0" class="xh-comment-end">没有更多评论了</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, computed } from 'vue'
// 移除同步导入的mapbox-gl
// import mapboxgl from 'mapbox-gl'
import { ElIcon, ElMessage } from 'element-plus'
import { HomeFilled, Location } from '@element-plus/icons-vue'
import { contentApi } from '@/api/content'
import { commentApi } from '@/api/comment'
import { useUserStore } from '@/stores/user'
import { getFileUrlByEnv } from '@/config'

let map
let mapboxgl = null // 动态加载的mapbox-gl

const showDialog = ref(false)
const currentPost = ref({})
const mapData = ref([])

const initialCenter = [116.397389, 39.908722]
const initialZoom = 2

const likeLoading = ref(false)

// 动态加载Mapbox CSS
const loadMapboxCSS = () => {
  return new Promise((resolve) => {
    if (document.querySelector('link[href*="mapbox-gl.css"]')) {
      resolve()
      return
    }
    
    const link = document.createElement('link')
    link.rel = 'stylesheet'
    link.href = 'https://api.mapbox.com/mapbox-gl-js/v3.12.0/mapbox-gl.css'
    link.onload = resolve
    link.onerror = resolve // 即使失败也继续
    document.head.appendChild(link)
  })
}

// 动态加载Mapbox GL JS
const loadMapboxGL = async () => {
  if (mapboxgl) return mapboxgl
  
  try {
    // 先加载CSS
    await loadMapboxCSS()
    
    // 动态导入Mapbox GL JS
    const mapboxModule = await import('mapbox-gl')
    mapboxgl = mapboxModule.default
    return mapboxgl
  } catch (error) {
    console.error('加载Mapbox失败:', error)
    ElMessage.error('地图加载失败，请刷新页面重试')
    throw error
  }
}

// 点赞/取消点赞逻辑
const handleLike = async () => {
  if (!currentPost.value.id) return
  if (likeLoading.value) return
  likeLoading.value = true
  try {
    if (!currentPost.value._liked) {
      await contentApi.likeContent(currentPost.value.id)
      currentPost.value.likes = (currentPost.value.likes || 0) + 1
      currentPost.value._liked = true
      ElMessage.success('点赞成功')
    } else {
      await contentApi.unlikeContent(currentPost.value.id)
      currentPost.value.likes = (currentPost.value.likes || 1) - 1
      currentPost.value._liked = false
      ElMessage.success('已取消点赞')
    }
  } catch (e) {
    ElMessage.error(e.message || '操作失败')
  } finally {
    likeLoading.value = false
  }
}

// 弹窗打开时查询是否已点赞
const checkLiked = async (contentId) => {
  try {
    const res = await contentApi.hasLiked(contentId)
    if (res.code === 200) {
      currentPost.value._liked = !!res.data
    }
  } catch (e) {
    currentPost.value._liked = false
  }
}

function handleResetClick() {
  if (map) {
    map.flyTo({ center: initialCenter, zoom: initialZoom, speed: 2 })
  }
  showDialog.value = false
}

// 计算当前弹窗的媒体列表
const mediaList = computed(() => {
  const list = []
  // 处理视频
  if (currentPost.value.videoUrl) {
    list.push({ type: 'video', url: getFileUrlByEnv(currentPost.value.videoUrl) })
  }
  // 处理图片（支持数组或单个字符串）
  let imgs = currentPost.value.imageUrl
  if (imgs) {
    if (typeof imgs === 'string' && imgs.trim().startsWith('[')) {
      try {
        imgs = JSON.parse(imgs)
      } catch (error) {
        console.warn('解析图片JSON失败:', error)
      }
    }
    if (typeof imgs === 'string') imgs = [imgs]
    imgs.forEach(img => {
      if (img) list.push({ type: 'image', url: getFileUrlByEnv(img) })
    })
  }
  return list
})

// 获取地图数据
const loadMapData = async () => {
  try {
    const response = await contentApi.getAllMapContents()
    if (response.code === 200) {
      mapData.value = response.data || []
      addMarkersToMap()
    } else {
      console.error('获取地图数据失败:', response.message)
    }
  } catch (error) {
    console.error('获取地图数据失败:', error)
  }
}

// 添加标记到地图
const addMarkersToMap = () => {
  if (!map || !mapData.value.length) return

  mapData.value.forEach(item => {
    if (!item.latitude || !item.longitude) return

    const el = document.createElement('div')
    el.style.backgroundImage = `url(${getFileUrlByEnv(item.imageUrl)})`
    el.style.width = '80px'
    el.style.height = '80px'
    el.style.backgroundSize = 'cover'
    el.style.border = '2px solid #fff'
    el.style.boxShadow = '0 2px 8px #0003'
    el.style.cursor = 'pointer'
    el.style.borderRadius = '10px'
    el.title = item.title || '内容'

    el.addEventListener('click', async () => {
      map.flyTo({
        center: [item.longitude, item.latitude],
        zoom: 10,
        speed: 2
      })
      // 先清空，防止闪烁旧数据
      currentPost.value = {}
      // 切换内容时重置评论区
      commentList.value = []
      commentPage.value = 1
      hasMoreComments.value = true
      showDialog.value = true
      // 拉取最新详情
      try {
        const res = await contentApi.getContentById(item.id)
        if (res.code === 200) {
          // 将后端返回的Content对象转换为MapContentResponse格式
          const content = res.data
          currentPost.value = {
            id: content.id,
            latitude: content.latitude,
            longitude: content.longitude,
            location: content.location,
            title: content.title,
            description: content.diary,
            imageUrl: content.imagePaths,
            videoUrl: content.videoPath,
            likes: content.likeCount,
            comments: content.commentCount,
            createTime: content.createTime,
            user: {
              userId: content.userId,
              username: content.user?.username || `user${content.userId}`,
              nickname: content.user?.nickname || `用户${content.userId}`,
              avatar: content.user?.avatar || `https://placehold.co/40x40?text=U${content.userId}`
            }
          }
          await checkLiked(item.id)
          // 拉取评论
          await loadComments()
        } else {
          ElMessage.error(res.message || '获取内容详情失败')
          showDialog.value = false
        }
      } catch (error) {
        console.error('获取内容详情失败:', error)
        ElMessage.error('获取内容详情失败')
        showDialog.value = false
      }
    })

    new mapboxgl.Marker(el)
      .setLngLat([item.longitude, item.latitude])
      .addTo(map)
  })
}

// 评论区微博式加载
const commentList = ref([])
const commentPage = ref(1)
const commentPageSize = 10
const commentLoading = ref(false)
const hasMoreComments = ref(true)
const commentInput = ref('')
const replyInput = ref({})
const replyVisible = ref({})
const commentTotal = ref(0) // 新增
const deleteConfirmId = ref(null) // 当前待确认删除的评论或回复id

const loadComments = async () => {
  if (commentLoading.value || !hasMoreComments.value) return
  commentLoading.value = true
  try {
    const res = await commentApi.getComments(currentPost.value.id, commentPage.value, commentPageSize)
    if (res.code === 200) {
      const newComments = res.data.data || []
      // 支持后端返回总数
      if (typeof res.data.total === 'number') {
        commentTotal.value = res.data.total
      } else if (typeof currentPost.value.comments === 'number') {
        commentTotal.value = currentPost.value.comments
      } else {
        commentTotal.value = commentList.value.length + newComments.length
      }
      if (newComments.length < commentPageSize) hasMoreComments.value = false
      commentList.value.push(...newComments)
      commentPage.value++
    }
  } finally {
    commentLoading.value = false
  }
}

const onCommentScroll = (e) => {
  const el = e.target
  if (el.scrollTop + el.clientHeight >= el.scrollHeight - 10) {
    loadComments()
  }
}

// 发表评论
const handleAddComment = async () => {
  if (!commentInput.value.trim()) return
  await commentApi.addComment({ contentId: currentPost.value.id, content: commentInput.value })
  commentInput.value = ''
  // 重新加载全部评论
  commentList.value = []
  commentPage.value = 1
  hasMoreComments.value = true
  await loadComments()
  commentTotal.value++ // 新增
  currentPost.value.comments = (currentPost.value.comments || 0) + 1
}

// 回复评论
const handleReply = async (parentId) => {
  const content = replyInput.value[parentId]
  if (!content || !content.trim()) return
  await commentApi.addComment({ contentId: currentPost.value.id, content, parentId })
  replyInput.value[parentId] = ''
  replyVisible.value[parentId] = false
  await loadReplies(parentId)
  currentPost.value.comments = (currentPost.value.comments || 0) + 1
  commentTotal.value++
}

// 加载回复
const replies = ref({})
const repliesLoading = ref({})
// const hasMoreReplies = ref({})
// const replyPage = ref({})
// const replyPageSize = 10

const loadReplies = async (parentId) => {
  repliesLoading.value[parentId] = true
  try {
    // 只加载全部回复（如需分页可扩展）
    const res = await commentApi.getReplies(parentId)
    if (res.code === 200) {
      replies.value[parentId] = res.data || []
    }
  } finally {
    repliesLoading.value[parentId] = false
  }
}

const repliesVisible = ref({}) // { [commentId]: true/false }

const toggleReplies = async (commentId) => {
  repliesVisible.value[commentId] = !repliesVisible.value[commentId]
  if (repliesVisible.value[commentId] && !replies.value[commentId]) {
    await loadReplies(commentId)
  }
}

// 删除评论
const handleDeleteComment = async (commentId, isReply, parentId) => {
  try {
    await commentApi.deleteComment(commentId)
    ElMessage.success('删除成功')
    deleteConfirmId.value = null
    if (isReply && parentId) {
      await loadReplies(parentId)
    } else {
      commentList.value = []
      commentPage.value = 1
      hasMoreComments.value = true
      await loadComments()
    }
    commentTotal.value--
  } catch (e) {
    ElMessage.error(e.message || '删除失败')
  }
}

// 弹窗打开时重置评论区
// watch(showDialog, (val) => {
//   if (val) {
//     commentList.value = []
//     commentPage.value = 1
//     hasMoreComments.value = true
//     loadComments()
//   }
// })

// 时间格式化
const formatTime = (t) => {
  if (!t) return ''
  const d = new Date(t)
  return `${d.getFullYear()}-${String(d.getMonth()+1).padStart(2,'0')}-${String(d.getDate()).padStart(2,'0')} ${String(d.getHours()).padStart(2,'0')}:${String(d.getMinutes()).padStart(2,'0')}`
}

const userStore = useUserStore()
const currentUser = userStore.currentUser
const defaultAvatar = 'https://placehold.co/40x40?text=U'

onMounted(async () => {
  // 动态加载Mapbox GL JS
  await loadMapboxGL()

  mapboxgl.accessToken = 'pk.eyJ1Ijoia3Jpc3RvcGhlcnFxIiwiYSI6ImNtYnl2c2RhMDAyY3IybXNmZzY5cmFhYWQifQ.DgqaIt3pvVHrEOmOmTO5aQ'
  map = new mapboxgl.Map({
    container: 'map',
    style: 'mapbox://styles/mapbox/streets-v11',
    center: [116.397389, 39.908722],
    zoom: 2
  })

  map.on('load', () => {
    loadMapData()
  })
})

onBeforeUnmount(() => {
  if (map) map.remove()
})
</script>

<style scoped>
.mapbox-absolute {
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  width: 100%;
  height: 100%;
}

.xh-popup-mask {
  position: fixed;
  z-index: 3000;
  left: 0;
  top: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.35);
  display: flex;
  align-items: center;
  justify-content: center;
}

.xh-popup-card-xhs {
  background: #fff;
  border-radius: 18px;
  width: 980px;
  max-width: 99vw;
  min-height: 480px;
  height: 700px;      /* 关键：固定高度，保证内容不会无限撑开 */
  max-height: 85vh;
  box-shadow: 0 8px 32px #0003;
  display: flex;
  overflow: hidden;
  animation: popup-in 0.2s;
}

@keyframes popup-in {
  from {
    transform: scale(0.95);
    opacity: 0;
  }

  to {
    transform: scale(1);
    opacity: 1;
  }
}

.xh-popup-img-col {
  flex: 1 1 0;
  display: flex;
  align-items: stretch;
  justify-content: stretch;
  background: #000;
  padding: 0;
  height: 100%;
  min-width: 0;
  min-height: 0;
}

.el-carousel,
.el-carousel__container,
.el-carousel__item {
  width: 100% !important;
  height: 100% !important;
}

.xh-popup-img-xhs,
.xh-popup-img-xhs img,
.xh-popup-img-xhs video {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
  background: #000;
  border-radius: 0;
  margin: 0;
  box-shadow: none;
  max-width: none;
  max-height: none;
}

.xh-popup-content-col {
  flex: 1 1 0;
  min-width: 0;
  min-height: 0;
  display: flex;
  flex-direction: column;
  padding: 32px 32px 24px 32px;
}

.xh-popup-header-xhs {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.xh-popup-avatar-xhs {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 8px;
}

.xh-popup-username-xhs {
  font-weight: bold;
  margin-right: auto;
}

.xh-popup-close-xhs {
  background: none;
  border: none;
  font-size: 1.5em;
  color: #888;
  cursor: pointer;
}

.xh-popup-title-xhs {
  font-weight: bold;
  font-size: 1.1em;
  margin-bottom: 8px;
}

.xh-popup-desc-xhs {
  color: #555;
  font-size: 0.98em;
  margin-bottom: 12px;
  word-break: break-all;
}

.xh-popup-location-xhs {
  display: flex;
  align-items: center;
  gap: 5px;
  color: #666;
  font-size: 0.9em;
  margin-bottom: 12px;
}

.xh-popup-actions-xhs {
  display: flex;
  gap: 18px;
  color: #ff0050;
  font-size: 1.1em;
  margin-bottom: 16px;
}

.liked {
  color: #e74c3c;
}

.xh-reset-btn-fab {
  position: fixed;
  bottom: 24px;
  right: 32px;
  z-index: 10001;
  background: #fff;
  color: #333;
  border: 1px solid #eee;
  border-radius: 20px;
  padding: 8px 20px;
  box-shadow: 0 2px 8px #0002;
  font-size: 1em;
  cursor: pointer;
  transition: background 0.2s, box-shadow 0.2s;
}

.xh-reset-btn-fab:hover {
  background: #f5f5f5;
  box-shadow: 0 4px 16px #0002;
}

.xh-comment-section {
  display: flex;
  flex-direction: column;
  flex: 1 1 0;
  min-height: 0;
  overflow: hidden;
}
.xh-comment-input {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
  flex-shrink: 0;
  padding: 8px 0 0 0;
  background: #fafbfc;
}
.xh-comment-list {
  flex: 1 1 0;
  min-height: 0;
  overflow-y: auto;
  background: #f8fafc;
  border-radius: 8px;
  padding: 8px 8px 0 8px;
}
.xh-comment-item {
  margin-bottom: 16px;
  padding: 12px 0 10px 0;
  border-bottom: 1px solid #f0f0f0;
  background: #fafbfc;
  border-radius: 8px;
  transition: background 0.2s;
}
.xh-comment-item:hover {
  background: #f5f7fa;
}
.xh-comment-main {
  display: flex;
  align-items: center;
  gap: 5px;
}
.xh-comment-user {
  font-weight: bold;
  color: #409eff;
  font-size: 1em;
}
.xh-comment-content {
  margin-left: 8px;
  color: #333;
  font-size: 1em;
}
.xh-comment-fold-bar {
  color: #888;
  cursor: pointer;
  margin-bottom: 6px;
  font-size: 0.95em;
}
.xh-reply-box {
  margin: 6px 0 4px 0;
  display: flex;
  align-items: center;
  gap: 6px;
}
.xh-reply-list {
  margin-left: 32px;
  margin-top: 4px;
  background: #f6f8fa;
  border-radius: 6px;
  padding: 6px 8px;
}
.xh-reply-item {
  margin-bottom: 6px;
  color: #666;
  font-size: 0.98em;
}
.xh-reply-fold-bar {
  color: #aaa;
  cursor: pointer;
  font-size: 0.93em;
  margin: 2px 0 2px 0;
}
.xh-comment-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  margin-right: 10px;
  object-fit: cover;
  vertical-align: middle;
}
.xh-comment-time {
  color: #bbb;
  font-size: 0.95em;
  margin-left: 12px;
}
.xh-comment-loading {
  text-align: center;
  color: #888;
  padding: 8px 0;
}
.xh-comment-end {
  text-align: center;
  color: #bbb;
  padding: 8px 0;
}
</style>