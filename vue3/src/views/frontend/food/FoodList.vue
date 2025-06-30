<!-- 美食列表页面 -->
<template>
  <div class="food-list-container">
    <div class="content-layout">
      <!-- 左侧城市导航 -->
      <CitySidebar class="city-sidebar" @citySelect="handleCitySelect" />

      <!-- 右侧内容区域 -->
      <div class="main-content">
        <!-- 搜索和筛选区域 -->
        <div class="search-filter-section">
          <!-- 页面标题和统计 -->
          <div class="page-header">
            <div class="header-content">
              <h1 class="page-title">
                <span class="title-icon">🍜</span>
                美食发现
              </h1>
            </div>
          </div>

          <!-- 搜索栏 -->
          <div class="search-card">
            <div class="search-form">
              <div class="search-inputs">
                <div class="search-input-group main-search">
                  <el-input
                    v-model="searchForm.keyword"
                    placeholder="搜索美食名称、地区或描述..."
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
                美食分类
              </h3>
              <div class="category-tags">
                <div class="tag-group">
                  <el-tag
                    v-for="category in categories"
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

        <!-- 美食列表区域 -->
        <div class="food-list-section">
          <div class="food-grid" v-if="foodList.length > 0">
            <div
              v-for="(food, index) in foodList"
              :key="food.id"
              class="food-item"
              :class="`delay-${(index % 6 + 1) * 100}`"
              @click="goToDetail(food.id)"
            >
              <div class="food-card">
                <div class="food-image">
                  <img :src="getImageUrl(food.imageUrl)" :alt="food.name" />
                  <div class="image-overlay">
                    <div class="overlay-content">
                      <div class="food-rating" v-if="food.rating">
                        <el-icon><Star /></el-icon>
                        {{ getDisplayRating(food.rating) }}
                      </div>
                    </div>
                  </div>
                  <div class="card-badges">
                    <span v-if="food.categoryName" class="badge category">
                      <span class="badge-icon">{{ getCategoryEmoji(food.categoryName) }}</span>
                      {{ food.categoryName }}
                    </span>
                    <span v-if="food.city" class="badge city">
                      <el-icon><Location /></el-icon>
                      {{ food.city }}
                    </span>
                    <span v-if="food.priceRange" class="badge price">
                      <el-icon><Goods /></el-icon>
                      {{ food.priceRange }}
                    </span>
                  </div>
                </div>
                <div class="food-info">
                  <h3 class="food-name">{{ food.name }}</h3>
                  <div class="food-location">
                    <el-icon><Location /></el-icon>
                    {{ food.address || '地址待更新' }}
                  </div>
                  <p class="food-desc">{{ truncateText(food.description, 80) }}</p>
                  <div class="card-footer">
                    <div class="card-meta">
                      <div class="meta-stats">
                        <span class="review-count">{{ formatReviewCount(food.reviewCount) }}</span>
                      </div>
                    </div>
                    <el-button type="primary" size="small" class="detail-btn" @click.stop="goToDetail(food.id)" round>
                      查看详情
                      <el-icon class="el-icon--right"><ArrowRight /></el-icon>
                    </el-button>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div v-else class="empty-state">
            <div class="empty-icon">🍽️</div>
            <h3 class="empty-title">暂无美食信息</h3>
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
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Search, Location, Refresh, Star, Grid, Goods, ArrowRight, Bowl } from '@element-plus/icons-vue'
import CitySidebar from '@/components/frontend/CitySidebar.vue'
import { getFoodList } from '@/api/food'

const router = useRouter()
const baseAPI = process.env.VUE_APP_BASE_API || '/api'

// 数据状态
const foodList = ref([])
const currentPage = ref(1)
const pageSize = ref(9)
const total = ref(0)
const categories = ref([
  { id: 1, name: '美味小吃', icon: '🥘' },
  { id: 2, name: '特产水果', icon: '🍎' },
  { id: 3, name: '地方特色', icon: '🍜' },
  { id: 4, name: '甜品饮品', icon: '🧋' },
  { id: 5, name: '海鲜水产', icon: '🦐' },
  { id: 6, name: '农家美食', icon: '🥘' },
  { id: 7, name: '烧烤火锅', icon: '🍖' },
  { id: 8, name: '传统糕点', icon: '🥮' }
])

// 搜索表单
const searchForm = reactive({
  keyword: '',
  city: '',
  categoryId: null
})

// 获取美食列表
const fetchFoodList = async () => {
  try {
    const params = {
      pageNum: currentPage.value,
      pageSize: pageSize.value,
      keyword: searchForm.keyword,
      categoryId: searchForm.categoryId,
      city: searchForm.city
    }
    
    await getFoodList(params, {
      onSuccess: (data) => {
        foodList.value = data.records || []
        total.value = data.total || 0
      },
      onError: (error) => {
        console.error('获取美食列表失败:', error)
      }
    })
  } catch (error) {
    console.error('获取美食列表失败:', error)
  }
}

// 处理图片URL
const getImageUrl = (url) => {
  if (!url) return require('@/assets/images/no-image.png')
  return url.startsWith('http') ? url : baseAPI + url
}

// 搜索处理
const handleSearch = () => {
  currentPage.value = 1
  fetchFoodList()
}

// 重置搜索
const resetSearch = () => {
  searchForm.keyword = ''
  searchForm.categoryId = null
  searchForm.city = ''
  currentPage.value = 1
  fetchFoodList()
}

// 分页处理
const handleCurrentChange = (page) => {
  currentPage.value = page
  fetchFoodList()
}

// 分类选择
const handleCategorySelect = (category) => {
  if (searchForm.categoryId === category.id) {
    searchForm.categoryId = null
  } else {
    searchForm.categoryId = category.id
  }
  currentPage.value = 1
  fetchFoodList()
}

// 城市选择
const handleCitySelect = (city) => {
  searchForm.city = city.name
  currentPage.value = 1
  fetchFoodList()
}

// 跳转到详情页
const goToDetail = (id) => {
  router.push({
    name: 'FoodDetail',
    params: { id }
  })
}

// 截取文本
const truncateText = (text, length) => {
  if (!text) return ''
  return text.length > length ? text.substring(0, length) + '...' : text
}

// 格式化评价数量
const formatReviewCount = (count) => {
  if (!count || count === 0) return '暂无评价'
  return `${count}条评价`
}

// 获取评分显示
const getDisplayRating = (rating) => {
  if (!rating) return '4.5'
  return parseFloat(rating).toFixed(1)
}

// 获取分类对应的emoji
const getCategoryEmoji = (categoryName) => {
  const emojiMap = {
    '美味小吃': '🥘',
    '特产水果': '🍎',
    '地方特色': '🍜',
    '甜品饮品': '🧋',
    '海鲜水产': '🦐',
    '农家美食': '🥘',
    '烧烤火锅': '🍖',
    '传统糕点': '🥮'
  }
  return emojiMap[categoryName] || '🍽️'
}

// 搜索标签
const searchTags = computed(() => {
  const tags = []
  if (searchForm.keyword) {
    tags.push({
      type: 'info',
      label: `关键词: ${searchForm.keyword}`,
      clear: () => {
        searchForm.keyword = ''
        handleSearch()
      }
    })
  }
  if (searchForm.city) {
    tags.push({
      type: 'success',
      label: `城市: ${searchForm.city}`,
      clear: () => {
        searchForm.city = ''
        handleSearch()
      }
    })
  }
  if (searchForm.categoryId) {
    const category = categories.value.find(c => c.id === searchForm.categoryId)
    if (category) {
      tags.push({
        type: 'primary',
        label: `分类: ${category.name}`,
        clear: () => {
          searchForm.categoryId = null
          handleSearch()
        }
      })
    }
  }
  return tags
})

onMounted(() => {
  fetchFoodList()
})
</script>

<style lang="scss" scoped>
.food-list-container {
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

// 美食列表
.food-list-section {
  margin-top: 30px;
}

.food-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 24px;
  margin-bottom: 40px;
}

.food-item {
  transition: all 0.3s ease;
  
  &:hover {
    transform: translateY(-4px);
  }
}

.food-card {
  background: white;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 4px 30px rgba(0, 0, 0, 0.05);
  border: 1px solid rgba(231, 235, 238, 0.8);
  transition: all 0.3s ease;
  cursor: pointer;

  &:hover {
    box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);

    .food-image img {
      transform: scale(1.05);
    }
  }
}

.food-image {
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

  .food-card:hover & {
    opacity: 1;
  }
}

.card-badges {
  position: absolute;
  top: 12px;
  left: 12px;
  display: flex;
  flex-direction: column;
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

  &.price {
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

.food-info {
  padding: 20px;
}

.food-name {
  font-size: 18px;
  font-weight: 600;
  margin: 0 0 8px;
  color: #1a1a1a;
}

.food-location {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #64748b;
  font-size: 14px;
  margin-bottom: 8px;
}

.food-desc {
  color: #64748b;
  font-size: 14px;
  line-height: 1.5;
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
}

.meta-stats {
  display: flex;
  align-items: center;
  gap: 12px;
  color: #64748b;
  font-size: 14px;
}

.detail-btn {
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
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 16px;
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
  background: linear-gradient(45deg, #3b82f6, #2563eb);
  border: none;
  padding: 0 24px;
  
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
  :deep(.el-pagination__total),
  :deep(.el-pagination__jump) {
    color: #64748b;
  }

  :deep(.el-pager li) {
    border-radius: 8px;
    margin: 0 4px;
    
    &:hover {
      color: #3b82f6;
    }
    
    &.is-active {
      background: #3b82f6;
      color: white;
    }
  }

  :deep(.btn-prev),
  :deep(.btn-next) {
    border-radius: 8px;
    margin: 0 4px;
    
    &:hover {
      color: #3b82f6;
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .content-layout {
    grid-template-columns: 1fr;
  }

  .search-inputs {
    grid-template-columns: 1fr;
  }

  .food-grid {
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  }
}
</style> 