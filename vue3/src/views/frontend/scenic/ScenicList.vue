<!-- eslint-disable -->
<template>
  <div class="scenic-frontend-container">
    <div class="content-layout">
      <!-- 添加城市侧边栏 -->
      <CitySidebar class="city-sidebar" @citySelect="handleCitySelect" />

      <div class="main-content">
        <!-- 搜索和筛选区域 -->
        <div class="search-filter-section">
          <!-- 页面标题和统计 -->
          <div class="page-header">
            <div class="header-content">
              <h1 class="page-title">
                <span class="title-icon">🏞️</span>
                探索精彩景点
                <span class="total-count" v-if="total > 0">(共{{ total }}个景点)</span>
              </h1>
              <p class="page-subtitle">
                发现广西各地的美丽风景和文化遗产
              </p>
            </div>
          </div>

          <!-- 搜索栏 -->
          <div class="search-card">
            <div class="search-form">
              <div class="search-inputs">
                <div class="search-input-group main-search">
                  <el-input
                    v-model="searchForm.name"
                    placeholder="搜索景点名称、地区或描述..."
                    clearable
                    size="large"
                    class="main-search-input"
                    @keyup.enter="handleSearch"
                  >
                    <template #prefix>
                      <el-icon><Search /></el-icon>
                    </template>
                  </el-input>
                </div>
                <div class="search-actions">
                  <el-button type="primary" @click="handleSearch" class="search-btn" size="large">
                    <el-icon><Search /></el-icon>
                    搜索
                  </el-button>
                  <el-button @click="resetSearch" class="reset-btn" size="large">
                    <el-icon><Refresh /></el-icon>
                    重置
                  </el-button>
                </div>
              </div>

              <!-- 搜索结果提示 -->
              <div v-if="searchTags.length > 0" class="search-tags">
                <el-tag
                  v-for="tag in searchTags"
                  :key="tag.label"
                  :type="tag.type"
                  closable
                  @close="tag.clear"
                  effect="light"
                  class="search-tag"
                  round
                >
                  {{ tag.label }}
                </el-tag>
              </div>
            </div>

            <!-- 分类筛选 -->
            <div class="category-filter">
              <h3 class="filter-title">
                <el-icon><Grid /></el-icon>
                景点分类
              </h3>
              <div class="category-tags">
                <div class="tag-group">
                  <el-tag
                    v-for="category in categoryList"
                    :key="category.id"
                    :class="{ active: searchForm.categoryId === category.id }"
                    @click="handleCategorySelect(category)"
                    :type="searchForm.categoryId === category.id ? 'primary' : 'info'"
                    effect="light"
                    size="large"
                    class="category-tag"
                    round
                  >
                    <span class="tag-icon">{{ category.icon }}</span>
                    {{ category.name }}
                  </el-tag>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 景点列表区域 -->
        <div class="scenic-list-section">
          <div class="scenic-grid" v-if="tableData && tableData.length > 0">
            <div
              v-for="(item, index) in tableData"
              :key="item.id"
              class="scenic-card"
              :class="`delay-${(index % 6 + 1) * 100}`"
              @click="goDetail(item.id)"
            >
              <div class="card-image">
                <img :src="getImageUrl(item.imageUrl)" :alt="item.name" />
                <div class="image-overlay">
                  <div class="overlay-content">
                    <div class="scenic-rating" v-if="item.rating">
                      <el-icon><Star /></el-icon>
                      {{ getDisplayRating(item.rating) }}
                    </div>
                  </div>
                </div>
                <div class="card-badges">
                  <span v-if="item.categoryInfo" class="badge category">
                    <span class="badge-icon">{{ getCategoryEmoji(item.categoryInfo.name) }}</span>
                    {{ item.categoryInfo.name }}
                  </span>
                  <span v-if="item.city" class="badge city">
                    <el-icon><Location /></el-icon>
                    {{ item.city }}
                  </span>
                  <span v-if="item.price === 0" class="badge free">
                    <el-icon><Ticket /></el-icon>
                    免费
                  </span>
                  <span v-else-if="item.price > 0" class="badge price">
                    <el-icon><Ticket /></el-icon>
                    ¥{{ item.price }}
                  </span>
                </div>
              </div>
              <div class="card-content">
                <h3 class="scenic-name">{{ item.name }}</h3>
                <div class="scenic-location">
                  <el-icon><Location /></el-icon>
                  {{ item.location || '未知地区' }}
                </div>
                <p class="scenic-desc">{{ truncateText(item.description, 80) }}</p>
                <div class="card-footer">
                  <div class="card-meta">
                    <div class="meta-stats">
                      <span class="review-count">{{ formatReviewCount(item.reviewCount) }}</span>
                      <span class="collection-status" v-if="collectionStatus[item.id]">
                        <el-icon><Star /></el-icon>
                        已收藏
                      </span>
                    </div>
                  </div>
                  <el-button type="primary" size="small" class="detail-btn" @click.stop="goDetail(item.id)" round>
                    查看详情
                    <el-icon class="el-icon--right"><ArrowRight /></el-icon>
                  </el-button>
                </div>
              </div>
            </div>
          </div>

          <div v-else class="empty-state">
            <div class="empty-icon">🔍</div>
            <h3 class="empty-title">暂无景点信息</h3>
            <p class="empty-desc">试试调整搜索条件或浏览其他分类</p>
            <el-button type="primary" @click="resetSearch" class="empty-action" round>
              重新搜索
            </el-button>
          </div>

          <!-- 分页 -->
          <div class="pagination-wrapper" v-if="total > 0">
            <el-pagination
              background
              layout="total, prev, pager, next, jumper"
              :current-page="currentPage"
              :page-size="pageSize"
              :total="total"
              @current-change="handleCurrentChange"
              class="modern-pagination"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import request from '@/utils/request'
import { useUserStore } from '@/store/user'
import { Search, Location, Refresh, Star, Grid, Ticket, ArrowRight } from '@element-plus/icons-vue'
import CitySidebar from '@/components/frontend/CitySidebar.vue'

const baseAPI = process.env.VUE_APP_BASE_API || '/api'
const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const tableData = ref([])
const categoryList = ref([])
const currentPage = ref(1)
const pageSize = ref(8)
const total = ref(0)
const searchForm = reactive({
  name: '',
  categoryId: null,
  city: ''
})
const collectionStatus = ref({})

// 检查是否登录
const isLoggedIn = computed(() => userStore.isLoggedIn)

const fetchCategories = async () => {
  try {
    await request.get('/scenic-category/tree', {}, {
      onSuccess: (res) => {
        categoryList.value = res || [];
      }
    });
  } catch (error) {
    console.error('获取分类列表失败:', error);
    categoryList.value = [];
  }
}

const fetchScenicSpots = async () => {
  try {
    const params = {
      name: searchForm.name || '',
      categoryId: searchForm.categoryId || null,
      city: searchForm.city || '',
      currentPage: currentPage.value,
      size: pageSize.value
    }
    console.log('请求参数:', params)
    const response = await request.get('/scenic/page', params)
    console.log('API响应:', response)
    
    if (response && response.data) {
      tableData.value = response.data.records || []
      total.value = response.data.total || 0
      console.log('设置数据:', tableData.value)
      
      // 如果用户已登录，检查收藏状态
      if (isLoggedIn.value && tableData.value.length > 0) {
        await checkCollectionStatus()
      }
    } else {
      console.warn('API响应数据格式不正确:', response)
      tableData.value = []
      total.value = 0
    }
  } catch (error) {
    console.error('获取景点列表失败:', error)
    tableData.value = []
    total.value = 0
  }
}

// 检查景点收藏状态
const checkCollectionStatus = async () => {
  const scenicIds = tableData.value.map(item => item.id)
  if (scenicIds.length === 0) return
  
  try {
    const response = await request.post('/scenic-collection/batch-is-collected', scenicIds)
    if (response.code === '200') {
      collectionStatus.value = response.data || {}
      }
  } catch (error) {
    console.error('获取收藏状态失败:', error)
  }
}

// 处理URL搜索参数
const handleUrlParams = () => {
  const searchParam = route.query.search
  const categoryParam = route.params.categoryId

  if (searchParam) {
    searchForm.name = decodeURIComponent(searchParam)
  }

  if (categoryParam) {
    searchForm.categoryId = parseInt(categoryParam)
  }
}

// 监听路由变化
watch(() => route.query, (newQuery) => {
  if (newQuery.search !== undefined) {
    searchForm.name = newQuery.search ? decodeURIComponent(newQuery.search) : ''
    currentPage.value = 1
    fetchScenicSpots()
  }
}, { immediate: false })

watch(() => route.params.categoryId, (newCategoryId) => {
  if (newCategoryId !== undefined) {
    searchForm.categoryId = newCategoryId ? parseInt(newCategoryId) : null
    currentPage.value = 1
    fetchScenicSpots()
  }
}, { immediate: false })

onMounted(() => {
  fetchCategories();
  handleUrlParams();
  fetchScenicSpots();
})

const handleSearch = () => {
  currentPage.value = 1
  fetchScenicSpots()
}

const resetSearch = () => {
  searchForm.name = ''
  searchForm.categoryId = null
  searchForm.city = ''
  currentPage.value = 1
  fetchScenicSpots()
}

const handleCurrentChange = (page) => {
  currentPage.value = page
  fetchScenicSpots()
}

const handleCategorySelect = (category) => {
  if (searchForm.categoryId === category.id) {
    searchForm.categoryId = null
  } else {
    searchForm.categoryId = category.id
  }
  currentPage.value = 1
  fetchScenicSpots()
}

const goDetail = (id) => {
  router.push(`/scenic/${id}`)
}

// 清除搜索条件的方法
const clearSearchName = () => {
  searchForm.name = ''
  handleSearch()
}

const clearSearchLocation = () => {
  searchForm.location = ''
  handleSearch()
}

const clearSearchCategory = () => {
  searchForm.categoryId = null
  handleSearch()
}

// 获取当前选中分类的名称
const getCurrentCategoryName = () => {
  if (!searchForm.categoryId) return ''
  const category = categoryList.value.find(cat => cat.id === searchForm.categoryId)
  return category ? category.name : ''
}

// 获取图片完整URL
const getImageUrl = (url) => {
  if (!url) return '/default-scenic.jpg'
  return url.startsWith('http') ? url : baseAPI + url
}

// 截取文本
const truncateText = (text, length) => {
  if (!text) return ''
  return text.length > length ? text.substring(0, length) + '...' : text
}

// 格式化评价数量
const formatReviewCount = (count) => {
  if (!count || count === 0) return '暂无评价'
  if (count === 1) return '1条评价'
  return `${count}条评价`
}

// 获取评分显示
const getDisplayRating = (rating) => {
  if (!rating) return '4.5'
  return parseFloat(rating).toFixed(1)
}

// 批量获取评论统计信息
const fetchBatchCommentStats = async () => {
  for (const item of tableData.value) {
    try {
      const response = await request.get('/comment/page', {
        scenicId: item.id,
        currentPage: 1,
        size: 1
      })
      if (response.code === '200') {
        item.reviewCount = response.data.total || 0
        }
    } catch (error) {
      console.error(`获取景点${item.id}评论统计失败:`, error)
      item.reviewCount = 0
    }
  }
}

// 处理城市选择
const handleCitySelect = (city) => {
  console.log('选择城市:', city)
  searchForm.city = city.name || ''
  currentPage.value = 1
  fetchScenicSpots()
}

// 清除城市筛选
const clearSearchCity = () => {
  console.log('清除城市筛选')
  searchForm.city = ''
  currentPage.value = 1
  fetchScenicSpots()
}

// 添加城市标签显示
const searchTags = computed(() => {
  const tags = []
  if (searchForm.name) {
    tags.push({
      type: 'info',
      label: `关键词: ${searchForm.name}`,
      clear: clearSearchName
    })
  }
  if (searchForm.location) {
    tags.push({
      type: 'warning',
      label: `地区: ${searchForm.location}`,
      clear: clearSearchLocation
    })
  }
  if (searchForm.city) {
    tags.push({
      type: 'success',
      label: `城市: ${searchForm.city}`,
      clear: clearSearchCity
    })
  }
  if (searchForm.categoryId && getCurrentCategoryName()) {
    tags.push({
      type: 'primary',
      label: `分类: ${getCurrentCategoryName()}`,
      clear: clearSearchCategory
    })
  }
  return tags
})

// 获取分类对应的emoji
const getCategoryEmoji = (categoryName) => {
  const emojiMap = {
    '自然风光': '🏞️',
    '历史古迹': '🏛️',
    '文化场所': '🏺',
    '主题公园': '🎡',
    '商业街区': '🏪',
    '宗教场所': '⛩️',
    '乡村旅游': '🌾',
    '工业旅游': '🏭'
  }
  return emojiMap[categoryName] || '🏞️'
}
</script>

<style lang="scss" scoped>
.scenic-frontend-container {
  min-height: 100vh;
  background: #f8fafc;
  font-family: "思源黑体", "Source Han Sans", "Noto Sans CJK SC", sans-serif;
  color: #1a1a1a;
  padding: 40px 0;
}

.content-layout {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 20px;
  display: grid;
  grid-template-columns: auto 1fr;
  gap: 30px;
}

.main-content {
  flex: 1;
}

// 页面头部
.page-header {
  margin-bottom: 30px;
}

.page-title {
  font-size: 32px;
  font-weight: 700;
  margin: 0 0 12px;
  display: flex;
  align-items: center;
  gap: 12px;
  color: #1a1a1a;

  .title-icon {
    font-size: 36px;
  }

  .total-count {
    font-size: 16px;
    color: #64748b;
    font-weight: normal;
    margin-left: 12px;
  }
}

.page-subtitle {
  font-size: 16px;
  color: #64748b;
  margin: 0;
}

// 搜索卡片
.search-card {
  background: white;
  border-radius: 20px;
  padding: 24px;
  box-shadow: 0 4px 30px rgba(0, 0, 0, 0.05);
  border: 1px solid rgba(231, 235, 238, 0.8);
  margin-bottom: 30px;
}

.search-form {
  .search-inputs {
    display: grid;
    grid-template-columns: 1fr auto;
    gap: 16px;
    margin-bottom: 20px;
  }

  .main-search {
    flex: 1;
  }

  .main-search-input {
    :deep(.el-input__wrapper) {
      border-radius: 12px;
      padding: 8px 16px;
      background: #f8fafc;
      box-shadow: none;
      border: 1px solid #e2e8f0;
      transition: all 0.3s ease;

      &:hover, &.is-focus {
        border-color: #3b82f6;
        box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.1);
      }
    }
  }

  .search-actions {
    display: flex;
    gap: 12px;
  }

  .search-btn {
    background: linear-gradient(45deg, #3b82f6, #2563eb);
    border: none;
    padding: 0 24px;
    
    &:hover {
      background: linear-gradient(45deg, #2563eb, #1d4ed8);
      transform: translateY(-1px);
    }
  }

  .reset-btn {
    border-color: #e2e8f0;
    color: #64748b;
    
    &:hover {
      border-color: #3b82f6;
      color: #3b82f6;
    }
  }
}

// 搜索标签
.search-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 16px;
}

.search-tag {
  border-radius: 20px;
  padding: 6px 12px;
  font-size: 13px;
  
  &.el-tag--info {
    background-color: #f1f5f9;
    border-color: #e2e8f0;
    color: #64748b;
  }
}

// 分类筛选
.category-filter {
  margin-top: 24px;
  padding-top: 20px;
  border-top: 1px solid #e2e8f0;
}

.filter-title {
  font-size: 16px;
  font-weight: 600;
  color: #1a1a1a;
  margin: 0 0 16px;
  display: flex;
  align-items: center;
  gap: 8px;

  .el-icon {
    color: #3b82f6;
  }
}

.category-tags {
  .tag-group {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
  }
}

.category-tag {
  cursor: pointer;
  padding: 8px 16px;
  font-size: 14px;
  border-radius: 20px;
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-1px);
  }

  &.active {
    background: #eff6ff;
    border-color: #3b82f6;
    color: #1d4ed8;
  }

  .tag-icon {
    margin-right: 6px;
  }
}

// 景点列表
.scenic-list-section {
  margin-top: 30px;
}

.scenic-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 24px;
  margin-bottom: 40px;
}

.scenic-card {
  background: white;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 4px 30px rgba(0, 0, 0, 0.05);
  border: 1px solid rgba(231, 235, 238, 0.8);
  transition: all 0.3s ease;
  cursor: pointer;

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);

    .card-image img {
      transform: scale(1.05);
    }
  }
}

.card-image {
  position: relative;
  height: 200px;
  overflow: hidden;

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
  right: 0;
  bottom: 0;
  background: linear-gradient(to bottom, rgba(0,0,0,0.1), rgba(0,0,0,0.4));
  opacity: 0;
  transition: opacity 0.3s ease;

  .scenic-card:hover & {
    opacity: 1;
  }
}

.card-badges {
  position: absolute;
  top: 12px;
  left: 12px;
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  z-index: 2;
}

.badge {
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 4px;
  backdrop-filter: blur(8px);

  &.category {
    background: rgba(59, 130, 246, 0.9);
    color: white;
  }

  &.city {
    background: rgba(255, 255, 255, 0.9);
    color: #1a1a1a;
  }

  &.price, &.free {
    background: rgba(34, 197, 94, 0.9);
    color: white;
  }

  .badge-icon {
    font-size: 14px;
  }

  .el-icon {
    font-size: 14px;
  }
}

.card-content {
  padding: 20px;
}

.scenic-name {
  font-size: 18px;
  font-weight: 600;
  margin: 0 0 8px;
  color: #1a1a1a;
  line-height: 1.4;
}

.scenic-location {
  color: #64748b;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 4px;
  margin-bottom: 12px;
}

.scenic-desc {
  color: #64748b;
  font-size: 14px;
  line-height: 1.6;
  margin: 0 0 16px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid #e2e8f0;
}

.meta-stats {
  display: flex;
  align-items: center;
  gap: 12px;
  color: #64748b;
  font-size: 13px;
}

.review-count {
  display: flex;
  align-items: center;
  gap: 4px;
}

.collection-status {
  color: #f59e0b;
  display: flex;
  align-items: center;
  gap: 4px;
}

.detail-btn {
  padding: 8px 16px;
  font-size: 13px;
  border-radius: 20px;
  background: linear-gradient(45deg, #3b82f6, #2563eb);
  border: none;
  
  &:hover {
    background: linear-gradient(45deg, #2563eb, #1d4ed8);
    transform: translateY(-1px);
  }
}

// 空状态
.empty-state {
  text-align: center;
  padding: 60px 20px;
  background: white;
  border-radius: 20px;
  box-shadow: 0 4px 30px rgba(0, 0, 0, 0.05);
  border: 1px solid rgba(231, 235, 238, 0.8);
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 20px;
}

.empty-title {
  font-size: 20px;
  font-weight: 600;
  color: #1a1a1a;
  margin: 0 0 8px;
}

.empty-desc {
  color: #64748b;
  margin: 0 0 24px;
}

.empty-action {
  padding: 12px 24px;
  font-size: 14px;
  background: linear-gradient(45deg, #3b82f6, #2563eb);
  border: none;
  
  &:hover {
    background: linear-gradient(45deg, #2563eb, #1d4ed8);
    transform: translateY(-1px);
  }
}

// 分页
.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 40px;
}

.modern-pagination {
  :deep(.el-pagination.is-background) {
    .btn-prev,
    .btn-next,
    .el-pager li {
      background-color: white;
      border: 1px solid #e2e8f0;
      margin: 0 4px;
      color: #64748b;
      
      &:hover {
        color: #3b82f6;
        border-color: #3b82f6;
      }
      
      &.active {
        background: #3b82f6;
        color: white;
        border-color: #3b82f6;
      }
    }
  }
}

// 动画
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.scenic-card {
  animation: fadeInUp 0.6s ease-out;
  animation-fill-mode: both;
}

@for $i from 1 through 6 {
  .delay-#{$i * 100} {
    animation-delay: #{$i * 0.1}s;
  }
}
</style> 