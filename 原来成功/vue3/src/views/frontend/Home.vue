<template>
  <div class="home-container">
    <!-- Hero区域 - 轮播图 + 搜索 -->
    <div class="hero-section">
      <div class="hero-background">
        <home-carousel />
        <div class="hero-particles"></div>
      </div>
      <div class="hero-content">
        <div class="hero-text">
          <h1 class="hero-title">发现世界之美</h1>
          <p class="hero-subtitle">探索精彩旅程，创造美好回忆</p>
        </div>
 
      </div>
    </div>
  <div class="search-section" style="width:100%;text-align:center;margin:30px 0;">
       <div class="hero-search">
          <smart-search
            placeholder="搜索目的地、景点、攻略..."
            @search="handleSearch"
          />
        </div>
   </div> 
    <!-- 快速导航区域 -->
    <div class="quick-nav-section">
      <div class="section-container">
        <div class="section-header">
          <h2 class="section-title">
            <span class="title-icon">🎯</span>
            快速导航
          </h2>
        </div>
        <div class="category-grid">
          <div v-for="(category, index) in categories" :key="category.id"
               class="category-card animate-scale-in hover-lift"
               :class="`category-card-${index + 1} delay-${(index + 1) * 100}`"
               @click="goToCategory(category.id)">
            <div class="category-bg"></div>
            <div class="category-content">
              <div class="category-icon">
                <el-icon :size="28"><component :is="category.icon" /></el-icon>
              </div>
              <div class="category-info">
                <h3 class="category-name">{{ category.name }}</h3>
                <p class="category-desc">{{ category.description || '探索更多精彩' }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 精选推荐区域 -->
    <div class="featured-section">
      <div class="section-container">
        <div class="section-header">
          <h2 class="section-title">
            <span class="title-icon">✨</span>
            精选景点
          </h2>
          <router-link to="/scenic" class="view-more-btn">
            查看全部 <el-icon><ArrowRight /></el-icon>
          </router-link>
        </div>

        <el-skeleton :loading="scenicLoading" animated :count="6" :throttle="500">
          <template #template>
            <div class="scenic-grid">
              <div v-for="i in 6" :key="i" class="skeleton-item">
                <el-skeleton-item variant="image" style="width: 100%; height: 200px" />
                <div style="padding: 16px;">
                  <el-skeleton-item variant="h3" style="width: 80%; margin-bottom: 8px" />
                  <el-skeleton-item variant="text" style="width: 60%; margin-bottom: 8px" />
                  <el-skeleton-item variant="text" style="width: 40%" />
                </div>
              </div>
            </div>
          </template>
          <template #default>
            <div class="scenic-grid">
              <div v-for="(item, index) in scenicList" :key="item.id"
                   class="scenic-card animate-fade-in-up hover-lift"
                   :class="`delay-${(index % 6 + 1) * 100}`"
                   @click="goToScenicDetail(item.id)">
                <div class="card-image">
                  <img :src="getImageUrl(item.imageUrl)" :alt="item.name" />
                  <div class="image-overlay">
                    <div class="overlay-content">
                      <span class="scenic-rating">
                        <el-icon><Star /></el-icon>
                        {{ item.rating || '4.5' }}
                      </span>
                    </div>
                  </div>
                  <div class="card-badges">
                    <span v-if="item.price === 0" class="badge free">免费</span>
                    <span v-else-if="item.price > 0" class="badge price">¥{{ item.price }}</span>
                  </div>
                </div>
                <div class="card-content">
                  <h3 class="scenic-name">{{ item.name }}</h3>
                  <div class="scenic-location">
                    <el-icon><Location /></el-icon>
                    {{ item.location || '未知地区' }}
                  </div>
                  <p class="scenic-desc">{{ truncateText(item.description, 60) }}</p>
                  <div class="card-footer">
                    <el-button type="primary" size="small" class="book-btn">查看详情</el-button>
                  </div>
                </div>
              </div>
            </div>
          </template>
        </el-skeleton>
      </div>
    </div>

    <!-- 旅游灵感区域 -->
    <div class="inspiration-section">
      <div class="section-container">
        <div class="section-header">
          <h2 class="section-title">
            <span class="title-icon">💡</span>
            旅游灵感
          </h2>
        </div>

        <div class="guides-section">
          <div class="column-header">
            <h3>精选攻略</h3>
            <router-link to="/guide" class="more-link">更多攻略</router-link>
          </div>

          <el-skeleton :loading="guideLoading" animated :count="3" :throttle="500">
            <template #template>
              <div class="guide-grid">
                <div v-for="i in 3" :key="i" class="guide-skeleton">
                  <el-skeleton-item variant="image" style="width: 100%; height: 160px; border-radius: 8px" />
                  <div style="padding: 16px;">
                    <el-skeleton-item variant="h3" style="width: 80%; margin-bottom: 8px" />
                    <el-skeleton-item variant="text" style="width: 60%; margin-bottom: 8px" />
                    <el-skeleton-item variant="text" style="width: 40%" />
                  </div>
                </div>
              </div>
            </template>
            <template #default>
              <div class="guide-grid">
                <div v-for="item in guideList" :key="item.id"
                     class="guide-card animate-fade-in-up hover-lift"
                     @click="goToGuideDetail(item.id)">
                  <div class="guide-image">
                    <img :src="getImageUrl(item.coverImage)" :alt="item.title" />
                    <div class="guide-views">
                      <el-icon><View /></el-icon>
                      {{ item.views || 0 }}
                    </div>
                  </div>
                  <div class="guide-content">
                    <h4 class="guide-title">{{ item.title }}</h4>
                    <div class="guide-meta">
                      <span class="author">{{ item.userNickname || '旅游达人' }}</span>
                      <span class="date">{{ formatDate(item.createTime) }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </template>
          </el-skeleton>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import request from '@/utils/request'
import HomeCarousel from '@/components/frontend/HomeCarousel.vue'
import SmartSearch from '@/components/common/SmartSearch.vue'
import {
  ArrowRight,
  Location,
  View,
  Calendar,
  Sunny,
  House,
  TakeawayBox,
  OfficeBuilding,
  Ship,
  Star,
  Medal,
  Present,
  Food,
  Place,
  Flag,
  HomeFilled
} from '@element-plus/icons-vue'

const router = useRouter()
const baseAPI = process.env.VUE_APP_BASE_API || '/api'

// 景点分类
const categories = ref([
  { id: 1, name: '自然风光', icon: 'Sunny', description: '山水秀丽，风景如画' },
  { id: 2, name: '文化古迹', icon: 'House', description: '历史遗迹，文化传承' },
  { id: 3, name: '民族风情', icon: 'Medal', description: '多彩民族，特色文化' },
  { id: 4, name: '主题公园', icon: 'Present', description: '欢乐体验，休闲娱乐' },
  { id: 5, name: '美食街区', icon: 'Food', description: '特色美食，舌尖风味' },
  { id: 6, name: '宗教圣地', icon: 'Place', description: '宗教文化，心灵净地' },
  { id: 7, name: '红色旅游', icon: 'Flag', description: '革命圣地，红色记忆' },
  { id: 8, name: '乡村旅游', icon: 'HomeFilled', description: '田园风光，乡村生活' },
  { id: 9, name: '滨海度假', icon: 'Ship', description: '碧海银滩，休闲度假' },
  { id: 10, name: '工业遗产', icon: 'OfficeBuilding', description: '工业文明，历史印记' }
])

// 景点数据
const scenicList = ref([])
const scenicLoading = ref(true)

// 攻略数据
const guideList = ref([])
const guideLoading = ref(true)

// 获取图片完整URL
const getImageUrl = (url) => {
  if (!url) return ''
  return url.startsWith('http') ? url : baseAPI + url
}

// 截断文本
const truncateText = (text, length) => {
  if (!text) return ''
  return text.length > length ? text.substring(0, length) + '...' : text
}

// 格式化开放时间
const formatOpeningHours = (hours) => {
  if (!hours) return '暂无信息'
  return hours.split('-')[0].trim()
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
}

// 跳转到分类页面
const goToCategory = (categoryId) => {
  router.push(`/scenic/category/${categoryId}`)
}

// 跳转到景点详情
const goToScenicDetail = (scenicId) => {
  router.push(`/scenic/${scenicId}`)
}

// 跳转到攻略详情
const goToGuideDetail = (guideId) => {
  router.push(`/guide/detail/${guideId}`)
}

// 搜索功能
const handleSearch = (keyword) => {
  if (keyword && keyword.trim()) {
    router.push(`/scenic?search=${encodeURIComponent(keyword.trim())}`)
  }
}

// 获取热门景点
const fetchHotScenics = async () => {
  scenicLoading.value = true
  try {
    await request.get('/scenic/hot', {
      limit: 4
    }, {
      showDefaultMsg: false,
      onSuccess: (data) => {
        scenicList.value = data || []
      }
    })
  } catch (error) {
    console.error('获取热门景点失败:', error)
  } finally {
    scenicLoading.value = false
  }
}

// 获取精选攻略
const fetchHotGuides = async () => {
  guideLoading.value = true
  try {
    await request.get('/guide/hot', {
      limit: 3
    }, {
      showDefaultMsg: false,
      onSuccess: (data) => {
        guideList.value = data || []
      }
    })
  } catch (error) {
    console.error('获取精选攻略失败:', error)
  } finally {
    guideLoading.value = false
  }
}

// 滚动动画观察器
const observeElements = () => {
  const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        entry.target.classList.add('animate-fade-in-up')
      }
    })
  }, {
    threshold: 0.1,
    rootMargin: '0px 0px -50px 0px'
  })

  // 观察需要动画的元素
  const animateElements = document.querySelectorAll('.animate-on-scroll')
  animateElements.forEach(el => observer.observe(el))
}

// 添加粒子效果
const createParticles = () => {
  const particlesContainer = document.querySelector('.hero-particles')
  if (!particlesContainer) return

  for (let i = 0; i < 20; i++) {
    const particle = document.createElement('div')
    particle.className = 'particle'
    particle.style.cssText = `
      position: absolute;
      width: ${Math.random() * 4 + 2}px;
      height: ${Math.random() * 4 + 2}px;
      background: rgba(255, 255, 255, ${Math.random() * 0.5 + 0.2});
      border-radius: 50%;
      left: ${Math.random() * 100}%;
      top: ${Math.random() * 100}%;
      animation: particleFloat ${Math.random() * 3 + 4}s ease-in-out infinite;
      animation-delay: ${Math.random() * 2}s;
    `
    particlesContainer.appendChild(particle)
  }
}

onMounted(() => {
  fetchHotScenics()
  fetchHotGuides()

  // 延迟执行动画相关功能，确保DOM已渲染
  nextTick(() => {
    observeElements()
    createParticles()
  })
})
</script>

<style lang="scss" scoped>
.home-container {
  width: 100%;
  font-family: "思源黑体", "Source Han Sans", "Noto Sans CJK SC", sans-serif;
  color: #333;
  min-height: 100vh;
}

// Hero区域样式
.hero-section {
  position: relative;
  height: 450px;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 0 0 40px 40px;
  box-shadow: 0 10px 30px rgba(76, 175, 80, 0.1);
}

.hero-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;

  :deep(.home-carousel) {
    height: 100%;
    z-index: 1;

    .el-carousel {
      height: 100%;
      z-index: 1;
    }

    .el-carousel__item {
      height: 100%;
      z-index: 1;
    }
  }
}

.hero-particles {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
  z-index: 2;
  pointer-events: none;
}

@keyframes float {
  0%, 100% { transform: translateY(0px); }
  50% { transform: translateY(-10px); }
}

.hero-content {
  position: relative;
  z-index: 10;
  text-align: center;
  color: white;
  max-width: 600px;
  padding: 0 20px;
}

.hero-text {
  margin-bottom: 30px;
}

.hero-title {
  font-size: 48px;
  font-weight: 700;
  margin: 0 0 16px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
  background: linear-gradient(45deg, #fff, #f0f8ff);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.hero-subtitle {
  font-size: 18px;
  margin: 0;
  opacity: 0.9;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
}

.hero-search {
  width: 100%;
  max-width: 600px;
  margin: 0 auto;
  display: flex;
  justify-content: center;
}

// 通用容器样式
.section-container {
  max-width: 1300px;
  margin: 0 auto;
  padding: 30px 20px;
}

// 区域样式
.quick-nav-section,
.featured-section,
.inspiration-section {
  background: white;
  margin: 0;
  position: relative;

  &:nth-child(even) {
    background: #f8fafc;
  }
}

// 标题样式
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 40px;
  flex-wrap: wrap;
  gap: 20px;
}

.section-title {
  font-size: 32px;
  font-weight: 700;
  margin: 0;
  color: #2d3748;
  display: flex;
  align-items: center;
  gap: 12px;

  .title-icon {
    font-size: 28px;
  }
}

.view-more-btn,
.more-link {
  color: #667eea;
  font-size: 14px;
  display: flex;
  align-items: center;
  text-decoration: none;
  font-weight: 600;
  transition: all 0.3s ease;

  .el-icon {
    margin-left: 4px;
    transition: transform 0.3s ease;
  }

  &:hover {
    color: #764ba2;

    .el-icon {
      transform: translateX(3px);
    }
  }
}

// 快速导航样式
.category-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 20px;
  padding: 20px 0;
}

.category-card {
  position: relative;
  border-radius: 20px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.4s ease;
  height: 120px;
  background: white;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.03);

  &:hover {
    transform: translateY(-8px);
    box-shadow: 0 8px 25px rgba(0, 0, 0, 0.06);

    .category-bg {
      transform: scale(1.1);
    }

    .category-icon {
      transform: scale(1.1) rotate(5deg);
    }
  }

  // 第一行：粉色到黄色
  &-1 .category-bg {
    background: linear-gradient(135deg, #FFB7D1 0%, #FFE6EB 100%);
  }

  &-2 .category-bg {
    background: linear-gradient(135deg, #FFD6E0 0%, #FFF0F3 100%);
  }

  &-3 .category-bg {
    background: linear-gradient(135deg, #FFE5D6 0%, #FFF5EE 100%);
  }

  &-4 .category-bg {
    background: linear-gradient(135deg, #FFF0C8 0%, #FFF8E7 100%);
  }

  &-5 .category-bg {
    background: linear-gradient(135deg, #FFECB3 0%, #FFF8E1 100%);
  }

  // 第二行：绿色到蓝色
  &-6 .category-bg {
    background: linear-gradient(135deg, #E0F5D7 0%, #F1FAE9 100%);
  }

  &-7 .category-bg {
    background: linear-gradient(135deg, #D4F2E7 0%, #E8F8F1 100%);
  }

  &-8 .category-bg {
    background: linear-gradient(135deg, #D6F3F3 0%, #EAF9F9 100%);
  }

  &-9 .category-bg {
    background: linear-gradient(135deg, #D4E5FF 0%, #EAF1FF 100%);
  }

  &-10 .category-bg {
    background: linear-gradient(135deg, #E5D4FF 0%, #F1EAFF 100%);
  }
}

.category-bg {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  transition: transform 0.4s ease;
  opacity: 0.95;
}

.category-content {
  position: relative;
  z-index: 2;
  padding: 20px;
  height: 100%;
  display: flex;
  align-items: center;
  color: #5D6975;
}

.category-icon {
  width: 50px;
  height: 50px;
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.9);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
  transition: all 0.4s ease;
  backdrop-filter: blur(10px);
  position: relative;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.03);

  .el-icon {
    font-size: 24px;
    color: #5D6975;
    opacity: 0.8;
  }
}

.category-info {
  flex: 1;
}

.category-name {
  font-size: 18px;
  font-weight: 600;
  margin: 0 0 4px;
  color: #3C4858;
}

.category-desc {
  font-size: 13px;
  margin: 0;
  color: #5D6975;
  opacity: 0.9;
}

// 景点网格布局
.scenic-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 24px;
}

.scenic-card {
  border-radius: 20px;
  overflow: hidden;
  background: #fff;
  box-shadow: 0 4px 20px rgba(76, 175, 80, 0.1);
  transition: all 0.4s ease;
  cursor: pointer;
  position: relative;

  &:hover {
    transform: translateY(-8px);
    box-shadow: 0 12px 40px rgba(76, 175, 80, 0.2);
  }
}

.card-image {
  height: 200px;
  overflow: hidden;
  position: relative;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: transform 0.6s ease;
  }
}

.image-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(to bottom, transparent 0%, rgba(0, 0, 0, 0.7) 100%);
  opacity: 0;
  transition: opacity 0.3s ease;
  display: flex;
  align-items: flex-end;
  padding: 20px;
}

.overlay-content {
  color: white;

  .scenic-rating {
    display: flex;
    align-items: center;
    font-size: 14px;
    font-weight: 600;

    .el-icon {
      margin-right: 4px;
      color: #ffd700;
    }
  }
}

.card-badges {
  position: absolute;
  top: 12px;
  left: 12px;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.badge {
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
  backdrop-filter: blur(10px);

  &.hot {
    background: linear-gradient(45deg, #ff6b6b, #ee5a24);
    color: white;
  }

  &.free {
    background: linear-gradient(45deg, #00d2d3, #54a0ff);
    color: white;
  }

  &.price {
    background: rgba(255, 255, 255, 0.9);
    color: #333;
  }
}

.card-content {
  padding: 20px;
}

.scenic-name {
  margin: 0 0 8px;
  font-size: 18px;
  font-weight: 700;
  color: #2d3748;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  line-height: 1.3;
}

.scenic-location {
  display: flex;
  align-items: center;
  font-size: 14px;
  color: #64748b;
  margin-bottom: 12px;

  .el-icon {
    margin-right: 4px;
    color: #667eea;
  }
}

.scenic-desc {
  font-size: 14px;
  color: #64748b;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  line-height: 1.5;
  margin-bottom: 16px;
}

.card-footer {
  display: flex;
  justify-content: flex-end;
  align-items: center;

  .book-btn {
    border-radius: 20px;
    background: linear-gradient(45deg, #667eea, #764ba2);
    border: none;
    font-weight: 600;
    padding: 8px 20px;
    color: white;
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-1px);
      box-shadow: 0 4px 12px rgba(76, 175, 80, 0.4);
      background: linear-gradient(45deg, #388E3C, #66BB6A);
    }
  }
}

// 攻略区域
.guides-section {
  background: white;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.column-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;

  h3 {
    margin: 0;
    font-size: 20px;
    font-weight: 700;
    color: #2d3748;
  }
}

.guide-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.guide-card {
  border-radius: 12px;
  overflow: hidden;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  cursor: pointer;

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 8px 20px rgba(0, 0, 0, 0.12);

    .guide-image img {
      transform: scale(1.05);
    }
  }
}

.guide-image {
  height: 160px;
  overflow: hidden;
  position: relative;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: transform 0.5s ease;
  }

  .guide-views {
    position: absolute;
    top: 10px;
    right: 10px;
    background: rgba(0, 0, 0, 0.6);
    color: white;
    padding: 4px 8px;
    border-radius: 4px;
    font-size: 12px;
    display: flex;
    align-items: center;
    backdrop-filter: blur(4px);

    .el-icon {
      margin-right: 4px;
    }
  }
}

.guide-content {
  padding: 16px;
}

.guide-title {
  margin: 0 0 8px;
  font-size: 16px;
  font-weight: 600;
  color: #2d3748;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  line-height: 1.3;
}

.guide-meta {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #64748b;

  .author {
    font-weight: 500;
  }

  .date {
    color: #94a3b8;
  }
}

// 骨架屏样式
.guide-skeleton {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

// 骨架屏样式
.skeleton-item {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

// 响应式样式
@media (max-width: 1400px) {
  .category-grid {
    grid-template-columns: repeat(4, 1fr);
  }
}

@media (max-width: 1200px) {
  .category-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 768px) {
  .hero-section {
    height: 350px;
  }

  .hero-title {
    font-size: 32px;
  }

  .hero-subtitle {
    font-size: 16px;
  }

  .hero-search {
    max-width: 90%;
    padding: 0 10px;
  }

  .section-title {
    font-size: 24px;
  }

  .section-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }

  .section-tabs {
    order: 2;
  }

  .view-more-btn {
    order: 3;
  }

  .category-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 15px;
  }

  .category-card {
    height: 100px;
  }

  .category-icon {
    width: 40px;
    height: 40px;

    .el-icon {
      font-size: 20px;
    }
  }

  .category-name {
    font-size: 16px;
  }

  .category-desc {
    font-size: 12px;
  }

  .scenic-grid,
  .guide-grid {
    grid-template-columns: 1fr;
  }

  .section-container {
    padding: 30px 15px;
  }
}

@media (max-width: 480px) {
  .hero-title {
    font-size: 24px;
  }

  .hero-subtitle {
    font-size: 14px;
  }

  .hero-search {
    max-width: 95%;
    padding: 0 5px;
  }

  .category-grid {
    grid-template-columns: 1fr;
  }

  .category-content {
    padding: 15px;
  }

  .category-name {
    font-size: 16px;
  }

  .category-desc {
    font-size: 12px;
  }
}
</style>


