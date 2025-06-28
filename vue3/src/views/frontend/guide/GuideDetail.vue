<template>
  <div class="guide-detail-container" v-loading="loading">
    <div class="guide-detail" v-if="guide">
      <!-- 英雄区域 - 参考景点详情页面设计 -->
      <div class="detail-hero-section">
        <div class="hero-image-container">
          <div class="image-wrapper">
            <img :src="getImageUrl(guide.coverImage)" :alt="guide.title" class="hero-image" v-if="guide.coverImage" />
            <div class="default-hero-image" v-else>
              <div class="default-icon">📖</div>
              <div class="default-text">旅游攻略</div>
            </div>
            <div class="image-overlay">
              <div class="overlay-gradient"></div>
              <div class="hero-content">
                <div class="breadcrumb">
                  <el-breadcrumb separator="/">
                    <el-breadcrumb-item @click="$router.push('/')">首页</el-breadcrumb-item>
                    <el-breadcrumb-item @click="$router.push('/guide')">旅游攻略</el-breadcrumb-item>
                    <el-breadcrumb-item>{{ guide.title }}</el-breadcrumb-item>
                  </el-breadcrumb>
                </div>
                <h1 class="guide-title">{{ guide.title }}</h1>
                <div class="guide-meta">
                  <div class="meta-item author">
                    <el-avatar
                      :src="getImageUrl(guide.userAvatar)"
                      :size="40"
                      class="author-avatar"
                    >
                      <el-icon><User /></el-icon>
                    </el-avatar>
                    <span class="author-name">{{ guide.userNickname || '旅行者' + guide.userId }}</span>
                  </div>
                  <div class="meta-item">
                    <el-icon><Calendar /></el-icon>
                    <span>{{ formatDate(guide.createTime) }}</span>
                  </div>
                  <div class="meta-item">
                    <el-icon><View /></el-icon>
                    <span>{{ formatNumber(guide.views) }} 阅读</span>
                  </div>
                </div>
                <div class="action-buttons">
                  <el-button
                    :type="isCollected ? 'danger' : 'primary'"
                    size="large"
                    @click="handleCollectionToggle"
                    :disabled="!userStore.isLoggedIn"
                    class="collection-btn"
                  >
                    <el-icon>
                      <StarFilled v-if="isCollected" />
                      <Star v-else />
                    </el-icon>
                    {{ isCollected ? '已收藏' : '收藏攻略' }}
                  </el-button>
                  <el-button size="large" class="share-btn" @click="handleShare">
                    <el-icon><Share /></el-icon>
                    分享
                  </el-button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 攻略内容区域 - 简洁无卡片设计 -->
      <div class="guide-content-section">
        <div class="content-container">
          <div class="content-layout">
            <!-- 主要内容 -->
            <div class="main-content">
              <div v-html="guide.content" class="rich-content"></div>
            </div>

            <!-- 侧边栏 -->
            <div class="sidebar" v-if="relatedGuides.length > 0">
              <div class="sidebar-sticky">
                <h3 class="sidebar-title">
                  <el-icon><ArrowRight /></el-icon>
                  相关攻略
                </h3>
                <div class="related-guides">
                  <div
                    v-for="item in relatedGuides"
                    :key="item.id"
                    class="related-item"
                    @click="goToGuide(item.id)"
                  >
                    <h4 class="related-title">{{ item.title }}</h4>
                    <div class="related-meta">
                      <span class="views">
                        <el-icon><View /></el-icon>
                        {{ formatNumber(item.views) }} 阅读
                      </span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <div class="guide-not-found" v-else-if="!loading">
      <el-empty description="未找到该攻略" />
      <el-button type="primary" @click="goBack">返回</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import request from '@/utils/request'
import { formatDate } from '@/utils/dateUtils'
import { useUserStore } from '@/store/user'
import { ElMessage } from 'element-plus'
import { View, Calendar, Star, StarFilled, Share, User, ArrowRight } from '@element-plus/icons-vue'

const baseAPI = process.env.VUE_APP_BASE_API || '/api'
const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const guide = ref(null)
const isCollected = ref(false)
const loading = ref(true)
const relatedGuides = ref([])

// 获取图片完整URL
const getImageUrl = (url) => {
  if (!url) return ''
  return url.startsWith('http') ? url : baseAPI + url
}

// 格式化数字
const formatNumber = (num) => {
  if (!num) return '0'
  if (num >= 10000) {
    return (num / 10000).toFixed(1) + 'w'
  } else if (num >= 1000) {
    return (num / 1000).toFixed(1) + 'k'
  }
  return num.toString()
}

const fetchGuide = async () => {
  loading.value = true
  const id = route.params.id
  try {
    await request.get(`/guide/${id}`, {}, {
      showDefaultMsg: false,
      onSuccess: (res) => {
        guide.value = res
        document.title = res.title + ' - 旅游攻略'
        
        // 如果用户已登录，查询是否已收藏
        if (userStore.isLoggedIn) {
          checkIsCollected(id)
        }
        
        // 获取相关攻略
        fetchRelatedGuides()
      }
    })
  } catch (error) {
    console.error('获取攻略详情失败:', error)
  } finally {
    loading.value = false
  }
}

// 获取相关攻略
const fetchRelatedGuides = async () => {
  if (!guide.value) return
  
  try {
    await request.get('/guide/related', { 
      guideId: guide.value.id,
      limit: 3
    }, {
      showDefaultMsg: false,
      onSuccess: (data) => {
        relatedGuides.value = data || []
      }
    })
  } catch (error) {
    console.error('获取相关攻略失败:', error)
  }
}

// 查询是否已收藏
const checkIsCollected = async (guideId) => {
  if (!userStore.isLoggedIn) return
  
  try {
    await request.get('/collection/isCollected', { guideId }, {
      showDefaultMsg: false,
      onSuccess: (data) => {
        isCollected.value = data
      }
    })
  } catch (error) {
    console.error('查询收藏状态出错:', error)
  }
}

// 处理收藏/取消收藏
const handleCollectionToggle = async () => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    router.push('/login?redirect=' + route.fullPath)
    return
  }
  
  try {
    if (isCollected.value) {
      // 取消收藏
      await request.delete('/collection/cancel?guideId=' + guide.value.id, {
        successMsg: '取消收藏成功',
        onSuccess: () => {
          isCollected.value = false
        }
      })
    } else {
      // 添加收藏
      await request.post('/collection/add', { guideId: guide.value.id }, {
        successMsg: '收藏成功',
        onSuccess: () => {
          isCollected.value = true
        }
      })
    }
  } catch (error) {
    console.error('收藏操作失败:', error)
  }
}

// 分享功能
const handleShare = () => {
  // 复制链接到剪贴板
  const url = window.location.href
  navigator.clipboard.writeText(url).then(() => {
    ElMessage.success('链接已复制，快去分享吧！')
  }).catch(() => {
    ElMessage.error('复制失败，请手动复制地址栏链接')
  })
}

// 前往其他攻略
const goToGuide = (id) => {
  router.push(`/guide/detail/${id}`)
}

// 返回上一页
const goBack = () => {
  router.back()
}

onMounted(fetchGuide)
</script>

<style lang="scss" scoped>
.guide-detail-container {
  min-height: 100vh;
  background: #f8fafc;
  font-family: "思源黑体", "Source Han Sans", "Noto Sans CJK SC", sans-serif;
}

.guide-detail {
  // 英雄区域样式 - 参考景点详情页面
  .detail-hero-section {
    position: relative;
    height: 60vh;
    min-height: 500px;
    overflow: hidden;
  }

  .hero-image-container {
    position: relative;
    width: 100%;
    height: 100%;
  }

  .image-wrapper {
    position: relative;
    width: 100%;
    height: 100%;
  }

  .hero-image {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: transform 0.8s ease;
  }

  .default-hero-image {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;

    .default-icon {
      font-size: 80px;
      margin-bottom: 20px;
      opacity: 0.8;
    }

    .default-text {
      font-size: 24px;
      font-weight: 600;
      opacity: 0.9;
    }
  }

  .image-overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .overlay-gradient {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(
      to bottom,
      rgba(0, 0, 0, 0.3) 0%,
      rgba(0, 0, 0, 0.1) 50%,
      rgba(0, 0, 0, 0.6) 100%
    );
  }

  .hero-content {
    position: relative;
    z-index: 10;
    color: white;
    text-align: center;
    max-width: 1200px;
    width: 100%;
    padding: 0 40px;
  }

  .breadcrumb {
    margin-bottom: 30px;
    text-align: left;

    :deep(.el-breadcrumb) {
      .el-breadcrumb__item {
        .el-breadcrumb__inner {
          color: rgba(255, 255, 255, 0.8);
          font-weight: 500;
          cursor: pointer;

          &:hover {
            color: white;
          }
        }

        &:last-child .el-breadcrumb__inner {
          color: white;
        }
      }

      .el-breadcrumb__separator {
        color: rgba(255, 255, 255, 0.6);
      }
    }
  }

  .guide-title {
    font-size: 48px;
    font-weight: 700;
    margin: 0 0 30px;
    text-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
    line-height: 1.2;
  }

  .guide-meta {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 30px;
    margin-bottom: 30px;
    flex-wrap: wrap;
  }

  .meta-item {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 16px;
    font-weight: 500;
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);

    &.author {
      .author-avatar {
        border: 2px solid rgba(255, 255, 255, 0.3);
        transition: all 0.3s ease;

        &:hover {
          border-color: rgba(255, 255, 255, 0.6);
          transform: scale(1.1);
        }
      }

      .author-name {
        font-weight: 600;
      }
    }

    .el-icon {
      font-size: 18px;
    }
  }

  .action-buttons {
    display: flex;
    justify-content: center;
    gap: 16px;
    flex-wrap: wrap;
  }

  .collection-btn,
  .share-btn {
    border-radius: 50px;
    padding: 12px 24px;
    font-weight: 600;
    font-size: 16px;
    transition: all 0.3s ease;
    border: 2px solid rgba(255, 255, 255, 0.3);
    backdrop-filter: blur(10px);

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.2);
    }
  }

  .collection-btn {
    background: rgba(255, 255, 255, 0.9);
    color: #667eea;

    &:hover {
      background: white;
      color: #5a67d8;
      border-color: rgba(255, 255, 255, 0.6);
    }
  }

  .share-btn {
    background: rgba(255, 255, 255, 0.1);
    color: white;

    &:hover {
      background: rgba(255, 255, 255, 0.2);
      border-color: rgba(255, 255, 255, 0.5);
    }
  }
// 攻略内容区域 - 简洁无卡片设计
.guide-content-section {
  background: white;
  padding: 60px 0 80px;
}

.content-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.content-layout {
  display: grid;
  grid-template-columns: 1fr 280px;
  gap: 60px;
  align-items: start;
}

.main-content {
  min-width: 0; // 防止内容溢出
}

.sidebar {
  .sidebar-sticky {
    position: sticky;
    top: 100px;
    background: #f8fafc;
    border-radius: 12px;
    padding: 24px;
    border: 1px solid #e2e8f0;
  }

  .sidebar-title {
    font-size: 18px;
    font-weight: 700;
    color: #2d3748;
    margin: 0 0 20px;
    display: flex;
    align-items: center;
    gap: 8px;
    padding-bottom: 12px;
    border-bottom: 2px solid #667eea;

    .el-icon {
      color: #667eea;
      font-size: 18px;
    }
  }

  .related-guides {
    display: flex;
    flex-direction: column;
    gap: 16px;
  }

  .related-item {
    padding: 16px;
    background: white;
    border-radius: 8px;
    border: 1px solid #e2e8f0;
    cursor: pointer;
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      border-color: #667eea;

      .related-title {
        color: #667eea;
      }
    }
  }

  .related-title {
    font-size: 14px;
    font-weight: 600;
    color: #2d3748;
    margin-bottom: 8px;
    line-height: 1.4;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
    transition: color 0.3s ease;
  }

  .related-meta {
    .views {
      display: flex;
      align-items: center;
      gap: 4px;
      font-size: 12px;
      color: #64748b;

      .el-icon {
        font-size: 12px;
      }
    }
  }
}

// 富文本内容样式 - 优化阅读体验
.rich-content {
  font-size: 16px;
  line-height: 1.8;
  color: #2d3748;
  max-width: none;

  // 标题样式
  h1, h2, h3, h4, h5, h6 {
    color: #1a202c;
    font-weight: 700;
    margin: 32px 0 16px;
    line-height: 1.3;
  }

  h1 {
    font-size: 32px;
    border-bottom: 3px solid #667eea;
    padding-bottom: 12px;
    margin-bottom: 24px;
  }

  h2 {
    font-size: 28px;
    border-bottom: 2px solid #e2e8f0;
    padding-bottom: 10px;
    margin-bottom: 20px;
  }

  h3 {
    font-size: 24px;
    margin-bottom: 16px;
  }

  h4 {
    font-size: 20px;
    margin-bottom: 14px;
  }

  // 段落样式
  p {
    margin: 16px 0;
    text-align: justify;
    text-justify: inter-ideograph;
  }

  // 图片样式
  img {
    max-width: 100%;
    height: auto;
    border-radius: 12px;
    margin: 24px 0;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
    transition: transform 0.3s ease;

    &:hover {
      transform: scale(1.02);
    }
  }

  // 引用样式
  blockquote {
    border-left: 4px solid #667eea;
    padding: 16px 20px;
    margin: 24px 0;
    background: #f8fafc;
    border-radius: 0 8px 8px 0;
    font-style: italic;
    color: #4a5568;

    p {
      margin: 0;
    }
  }

  // 列表样式
  ul, ol {
    margin: 16px 0;
    padding-left: 24px;

    li {
      margin: 8px 0;
      line-height: 1.6;
    }
  }

  ul li {
    list-style-type: disc;
  }

  ol li {
    list-style-type: decimal;
  }

  // 代码样式
  code {
    background: linear-gradient(135deg, #f1f5f9 0%, #e2e8f0 100%);
    padding: 4px 8px;
    border-radius: 6px;
    color: #667eea;
    font-weight: 600;
    font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
    font-size: 14px;
  }

  pre {
    background: #1a202c;
    padding: 20px;
    border-radius: 12px;
    margin: 24px 0;
    overflow-x: auto;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);

    code {
      background: transparent;
      color: #e2e8f0;
      font-weight: normal;
      padding: 0;
    }
  }

  // 表格样式
  table {
    width: 100%;
    border-collapse: collapse;
    margin: 24px 0;
    background: white;
    border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);

    th, td {
      padding: 12px 16px;
      text-align: left;
      border-bottom: 1px solid #e2e8f0;
    }

    th {
      background: #f8fafc;
      font-weight: 600;
      color: #2d3748;
    }

    tr:hover {
      background: #f8fafc;
    }

    tr:last-child td {
      border-bottom: none;
    }
  }

  // 链接样式
  a {
    color: #667eea;
    text-decoration: none;
    border-bottom: 1px solid transparent;
    transition: all 0.3s ease;

    &:hover {
      color: #5a67d8;
      border-bottom-color: #5a67d8;
    }
  }

  // 分割线样式
  hr {
    border: none;
    height: 2px;
    background: linear-gradient(90deg, transparent, #e2e8f0, transparent);
    margin: 32px 0;
  }

  // 强调样式
  strong, b {
    font-weight: 700;
    color: #1a202c;
  }

  em, i {
    font-style: italic;
    color: #4a5568;
  }

  // 删除线
  del, s {
    text-decoration: line-through;
    color: #a0aec0;
  }

  // 下划线
  u {
    text-decoration: underline;
    text-decoration-color: #667eea;
  }
  line-height: 1.8;
  color: #2d3748;
  text-align: left;

  // Markdown样式优化
  h1, h2, h3, h4, h5, h6 {
    font-weight: 700;
    margin-top: 2em;
    margin-bottom: 0.8em;
    color: #1a202c;
    text-align: left;
    line-height: 1.3;
  }

  h1 {
    font-size: 32px;
    border-bottom: 3px solid #667eea;
    padding-bottom: 12px;
    margin-top: 1.5em;
  }

  h2 {
    font-size: 28px;
    border-bottom: 2px solid #e2e8f0;
    padding-bottom: 10px;
    color: #2d3748;
  }

  h3 {
    font-size: 24px;
    color: #4a5568;
  }

  h4 {
    font-size: 20px;
    color: #4a5568;
  }

  p {
    margin: 1.2em 0;
    text-align: left;
    line-height: 1.8;
  }

  img {
    max-width: 100%;
    border-radius: 12px;
    margin: 20px 0;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
    transition: transform 0.3s ease;

    &:hover {
      transform: scale(1.02);
    }
  }

  ul, ol {
    padding-left: 24px;
    margin: 1.2em 0;

    li {
      margin: 0.5em 0;
      line-height: 1.6;
    }
  }

  blockquote {
    border-left: 4px solid #667eea;
    padding: 16px 20px;
    color: #4a5568;
    margin: 1.5em 0;
    background: #f8fafc;
    border-radius: 0 8px 8px 0;
    font-style: italic;
  }

  code {
    background: linear-gradient(135deg, #f1f5f9 0%, #e2e8f0 100%);
    padding: 4px 8px;
    border-radius: 6px;
    font-family: 'Fira Code', 'Monaco', 'Consolas', monospace;
    font-size: 14px;
    color: #667eea;
    font-weight: 600;
  }

  pre {
    background: #1a202c;
    padding: 20px;
    border-radius: 12px;
    overflow-x: auto;
    margin: 1.5em 0;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);

    code {
      background: transparent;
      padding: 0;
      color: #e2e8f0;
      font-weight: normal;
    }
  }

  table {
    width: 100%;
    border-collapse: collapse;
    margin: 1.5em 0;
    background: white;
    border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);

    th, td {
      padding: 12px 16px;
      text-align: left;
      border-bottom: 1px solid #e2e8f0;
    }

    th {
      background: #f8fafc;
      font-weight: 600;
      color: #2d3748;
    }

    tr:hover {
      background: #f8fafc;
    }
  }
}

// 侧边栏样式
.sidebar {
  width: 320px;
  flex-shrink: 0;
}

.sidebar-card {
  background: white;
  border-radius: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  border: 1px solid #e2e8f0;
  overflow: hidden;
}

.sidebar-header {
  padding: 24px 24px 0;
  border-bottom: 1px solid #f1f5f9;
}

.sidebar-title {
  font-size: 20px;
  font-weight: 700;
  color: #2d3748;
  margin: 0 0 20px;
  display: flex;
  align-items: center;
  gap: 8px;

  .title-icon {
    font-size: 18px;
    filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.1));
  }
}

.sidebar-content {
  padding: 24px;
}

.related-guides {
  .related-guide-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 16px 0;
    border-bottom: 1px solid #f1f5f9;
    cursor: pointer;
    transition: all 0.3s ease;

    &:last-child {
      border-bottom: none;
    }

    &:hover {
      transform: translateX(8px);

      .related-guide-title {
        color: #667eea;
      }

      .related-guide-arrow {
        color: #667eea;
        transform: translateX(4px);
      }
    }
  }

  .related-guide-content {
    flex: 1;
    min-width: 0;
  }

  .related-guide-title {
    font-size: 16px;
    font-weight: 600;
    color: #2d3748;
    margin-bottom: 6px;
    line-height: 1.4;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
    transition: color 0.3s ease;
  }

  .related-guide-meta {
    display: flex;
    align-items: center;
    gap: 12px;

    .views {
      display: flex;
      align-items: center;
      gap: 4px;
      font-size: 13px;
      color: #64748b;

      .el-icon {
        font-size: 12px;
      }
    }
  }

  .related-guide-arrow {
    color: #cbd5e0;
    transition: all 0.3s ease;
    margin-left: 12px;

    .el-icon {
      font-size: 16px;
    }
  }
}

.guide-not-found {
  text-align: center;
  padding: 80px 20px;
  background: white;
  border-radius: 20px;
  margin: 40px 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);

  .el-button {
    margin-top: 24px;
    border-radius: 50px;
    padding: 12px 32px;
    font-weight: 600;
  }
}

// 动画效果
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.animate-fade-in-up {
  animation: fadeInUp 0.8s ease-out forwards;
  opacity: 0;
}

.delay-200 {
  animation-delay: 0.2s;
}

.delay-300 {
  animation-delay: 0.3s;
}

// 响应式设计
@media (max-width: 1024px) {
  .content-layout {
    grid-template-columns: 1fr;
    gap: 40px;
  }

  .sidebar {
    .sidebar-sticky {
      position: static;
      top: auto;
    }
  }
}

@media (max-width: 768px) {
  .detail-hero-section {
    height: 50vh;
    min-height: 400px;
  }

  .hero-content {
    padding: 0 20px;
  }

  .guide-title {
    font-size: 32px;
    margin-bottom: 20px;
  }

  .guide-meta {
    flex-direction: column;
    gap: 16px;
    align-items: stretch;
  }

  .action-buttons {
    flex-direction: column;
    gap: 12px;
  }

  .collection-btn,
  .share-btn {
    width: 100%;
    max-width: 280px;
  }

  .guide-content-section {
    padding: 40px 0 60px;
  }

  .content-container {
    padding: 0 16px;
  }

  .content-layout {
    gap: 30px;
  }

  .sidebar {
    .sidebar-sticky {
      padding: 20px;
    }

    .sidebar-title {
      font-size: 16px;
    }

    .related-item {
      padding: 12px;
    }

    .related-title {
      font-size: 13px;
    }
  }

  .rich-content {
    font-size: 15px;

    h1 {
      font-size: 26px;
    }

    h2 {
      font-size: 22px;
    }

    h3 {
      font-size: 20px;
    }

    h4 {
      font-size: 18px;
    }

    img {
      margin: 16px 0;
    }

    blockquote {
      padding: 12px 16px;
      margin: 16px 0;
    }

    pre {
      padding: 16px;
      margin: 16px 0;
    }

    table {
      font-size: 14px;

      th, td {
        padding: 8px 12px;
      }
    }
  }
}

@media (max-width: 480px) {
  .guide-title {
    font-size: 24px;
  }

  .meta-item {
    font-size: 14px;

    .el-icon {
      font-size: 16px;
    }
  }

  .rich-content {
    font-size: 14px;

    h1 {
      font-size: 22px;
    }

    h2 {
      font-size: 20px;
    }

    h3 {
      font-size: 18px;
    }

    h4 {
      font-size: 16px;
    }
  }
}
}
</style>