<template>
  <div class="smart-match-container">
    <!-- 顶部搜索区域 -->
    <div class="search-section" :class="{ 'search-active': showResults }">
      <div class="search-content">
        <h1 class="title">
          <span class="emoji">🎯</span>
          智能旅行推荐
          <span class="emoji">✨</span>
        </h1>
        <p class="subtitle">告诉我你的偏好，为你推荐最适合的旅行体验</p>
        
        <div class="search-box">
          <el-input
            v-model="searchQuery"
            placeholder="想吃水果？想看山水？还是体验文化？告诉我吧~"
            class="search-input"
            :prefix-icon="Search"
            @keyup.enter="handleSearch"
            clearable
          >
            <template #append>
              <el-button type="primary" @click="handleSearch">
                <el-icon><Magic /></el-icon>
                <span>智能匹配</span>
              </el-button>
            </template>
          </el-input>
        </div>

        <div class="tags-container">
          <div class="tags-title">
            <el-icon><Collection /></el-icon>
            <span>热门偏好</span>
          </div>
          <div class="tags-list">
            <el-tag
              v-for="tag in suggestedTags"
              :key="tag.name"
              :type="tag.type"
              class="tag"
              @click="handleTagClick(tag.name)"
              effect="light"
            >
              <span class="tag-emoji">{{ tag.emoji }}</span>
              {{ tag.name }}
            </el-tag>
          </div>
        </div>
      </div>
    </div>

    <!-- 推荐结果区域 -->
    <div v-if="showResults" class="results-section">
      <el-row :gutter="20">
        <!-- 景点推荐卡片 -->
        <el-col :lg="12" :md="24" class="mb-20">
          <div class="result-card scenic-card" v-loading="loading.scenic">
            <div class="card-header">
              <div class="header-title">
                <span class="emoji">🏞️</span>
                <h2>推荐景点</h2>
              </div>
              <el-tag type="success" effect="plain" round>{{ results.scenicSpots.length }}个景点</el-tag>
            </div>
            <div class="card-content">
              <div v-for="spot in results.scenicSpots" :key="spot.id" class="result-item">
                <el-image 
                  :src="getImageUrl(spot.imageUrl)" 
                  class="item-image"
                  fit="cover"
                  loading="lazy"
                >
                  <template #error>
                    <div class="image-placeholder">
                      <el-icon><Picture /></el-icon>
                    </div>
                  </template>
                </el-image>
                <div class="item-info">
                  <h3>{{ spot.name }}</h3>
                  <p class="description">{{ spot.description }}</p>
                  <div class="item-footer">
                    <div class="location">
                      <el-tag size="small" effect="plain">{{ spot.city }}</el-tag>
                    </div>
                    <el-button type="primary" link @click="goToDetail('scenic', spot.id)">
                      查看详情 <el-icon><ArrowRight /></el-icon>
                    </el-button>
                  </div>
                </div>
              </div>
              <div v-if="results.scenicSpots.length === 0" class="empty-state">
                <el-empty description="暂无匹配的景点" />
              </div>
            </div>
          </div>
        </el-col>

        <!-- 美食推荐卡片 -->
        <el-col :lg="12" :md="24" class="mb-20">
          <div class="result-card food-card" v-loading="loading.food">
            <div class="card-header">
              <div class="header-title">
                <span class="emoji">🍜</span>
                <h2>推荐美食</h2>
              </div>
              <el-tag type="danger" effect="plain" round>{{ results.foods.length }}道美食</el-tag>
            </div>
            <div class="card-content">
              <div v-for="food in results.foods" :key="food.id" class="result-item">
                <el-image 
                  :src="getImageUrl(food.imageUrl)" 
                  class="item-image"
                  fit="cover"
                  loading="lazy"
                >
                  <template #error>
                    <div class="image-placeholder">
                      <el-icon><Picture /></el-icon>
                    </div>
                  </template>
                </el-image>
                <div class="item-info">
                  <h3>{{ food.name }}</h3>
                  <p class="description">{{ food.description }}</p>
                  <div class="item-footer">
                    <div class="meta-info">
                      <el-tag size="small" effect="plain">{{ food.location }}</el-tag>
                      <span class="price">{{ food.priceRange }}</span>
                    </div>
                    <el-button type="primary" link @click="goToDetail('food', food.id)">
                      查看详情 <el-icon><ArrowRight /></el-icon>
                    </el-button>
                  </div>
                </div>
              </div>
              <div v-if="results.foods.length === 0" class="empty-state">
                <el-empty description="暂无匹配的美食" />
              </div>
            </div>
          </div>
        </el-col>

        <!-- 住宿推荐卡片 -->
        <el-col :lg="12" :md="24" class="mb-20">
          <div class="result-card accommodation-card" v-loading="loading.accommodation">
            <div class="card-header">
              <div class="header-title">
                <span class="emoji">🏡</span>
                <h2>推荐住宿</h2>
              </div>
              <el-tag type="warning" effect="plain" round>{{ results.accommodations.length }}个住宿</el-tag>
            </div>
            <div class="card-content">
              <div v-for="hotel in results.accommodations" :key="hotel.id" class="result-item">
                <el-image 
                  :src="getImageUrl(hotel.imageUrl)" 
                  class="item-image"
                  fit="cover"
                  loading="lazy"
                >
                  <template #error>
                    <div class="image-placeholder">
                      <el-icon><Picture /></el-icon>
                    </div>
                  </template>
                </el-image>
                <div class="item-info">
                  <h3>{{ hotel.name }}</h3>
                  <p class="description">{{ hotel.description }}</p>
                  <div class="item-footer">
                    <div class="meta-info">
                      <el-tag size="small" effect="plain">{{ hotel.type }}</el-tag>
                      <div class="rating">
                        <el-rate
                          v-model="hotel.starLevel"
                          disabled
                          text-color="#ff9900"
                          score-template="{value}"
                        />
                      </div>
                      <span class="price">{{ hotel.priceRange }}</span>
                    </div>
                    <el-button type="primary" link @click="goToDetail('accommodation', hotel.id)">
                      查看详情 <el-icon><ArrowRight /></el-icon>
                    </el-button>
                  </div>
                </div>
              </div>
              <div v-if="results.accommodations.length === 0" class="empty-state">
                <el-empty description="暂无匹配的住宿" />
              </div>
            </div>
          </div>
        </el-col>

        <!-- 攻略推荐卡片 -->
        <el-col :lg="12" :md="24" class="mb-20">
          <div class="result-card guide-card" v-loading="loading.guides">
            <div class="card-header">
              <div class="header-title">
                <span class="emoji">📖</span>
                <h2>相关攻略</h2>
              </div>
              <el-tag type="info" effect="plain" round>{{ results.guides.length }}篇攻略</el-tag>
            </div>
            <div class="card-content">
              <div v-for="guide in results.guides" :key="guide.id" class="result-item">
                <el-image 
                  :src="getImageUrl(guide.coverImage)" 
                  class="item-image"
                  fit="cover"
                  loading="lazy"
                >
                  <template #error>
                    <div class="image-placeholder">
                      <el-icon><Picture /></el-icon>
                    </div>
                  </template>
                </el-image>
                <div class="item-info">
                  <h3>{{ guide.title }}</h3>
                  <p class="description">{{ stripHtml(guide.content).slice(0, 100) }}...</p>
                  <div class="item-footer">
                    <div class="author">
                      <el-avatar :size="24" :src="guide.userAvatar">
                        {{ guide.userNickname?.[0] || '游' }}
                      </el-avatar>
                      <div class="author-info">
                        <span class="author-name">{{ guide.userNickname || '游客' }}</span>
                      </div>
                    </div>
                    <el-button type="primary" link @click="goToDetail('guide', guide.id)">
                      查看详情 <el-icon><ArrowRight /></el-icon>
                    </el-button>
                  </div>
                </div>
              </div>
              <div v-if="results.guides.length === 0" class="empty-state">
                <el-empty description="暂无匹配的攻略" />
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Search, Magic, Collection, Picture, ArrowRight } from '@element-plus/icons-vue'
import { getSmartRecommendations } from '@/api/smart-match'
import { formatTime } from '@/utils/dateUtils'

const API_BASE_URL = 'http://localhost:1236'

const STORAGE_KEY = 'smart_match_state'

const router = useRouter()
const searchQuery = ref('')
const showResults = ref(false)

const suggestedTags = [
  { name: '山水风光', emoji: '🏞️', type: 'success' },
  { name: '特色美食', emoji: '🍜', type: 'danger' },
  { name: '文化古迹', emoji: '🏛️', type: 'warning' },
  { name: '民族风情', emoji: '👘', type: 'primary' },
  { name: '水果之旅', emoji: '🍎', type: 'danger' },
  { name: '红色旅游', emoji: '🏛️', type: 'info' },
  { name: '户外探险', emoji: '🏃', type: 'success' },
  { name: '温泉度假', emoji: '♨️', type: 'warning' }
]

const loading = reactive({
  scenic: false,
  food: false,
  accommodation: false,
  guides: false
})

const results = reactive({
  scenicSpots: [],
  foods: [],
  accommodations: [],
  guides: []
})

// 添加图片URL处理函数
const getImageUrl = (path) => {
  if (!path) return require('@/assets/images/no-image.png')
  if (path.startsWith('http')) return path
  // 移除开头的斜杠（如果存在）
  return path.startsWith('/') ? `/api${path}` : `/api/${path}`
}

// 处理HTML内容
const stripHtml = (html) => {
  if (!html) return ''
  return html.replace(/<[^>]*>/g, '')
}

const handleSearch = async () => {
  if (!searchQuery.value.trim()) {
    ElMessage.warning('请输入搜索内容')
    return
  }

  // 设置所有loading状态
  Object.keys(loading).forEach(key => loading[key] = true)
  showResults.value = true

  try {
    console.log('开始智能匹配搜索:', searchQuery.value)
    const response = await getSmartRecommendations(searchQuery.value)
    console.log('智能匹配响应:', response)
    
    if (!response || !response.data) {
      throw new Error('没有返回数据')
    }

    // 直接使用response.data，因为拦截器已经处理了一层响应
    results.scenicSpots = response.data.scenicSpots || []
    results.foods = response.data.foods || []
    results.accommodations = response.data.accommodations || []
    results.guides = response.data.guides || []

    console.log('处理后的结果:', results)
  } catch (error) {
    console.error('智能匹配错误:', error)
    ElMessage.error(error.message || '获取推荐失败，请稍后重试')
  } finally {
    Object.keys(loading).forEach(key => loading[key] = false)
  }
}

const handleTagClick = (tag) => {
  searchQuery.value = tag
  handleSearch()
}

// 保存状态到localStorage
const saveState = () => {
  const state = {
    searchQuery: searchQuery.value,
    showResults: showResults.value,
    results: {
      scenicSpots: results.scenicSpots,
      foods: results.foods,
      accommodations: results.accommodations,
      guides: results.guides
    }
  }
  localStorage.setItem(STORAGE_KEY, JSON.stringify(state))
}

// 从localStorage恢复状态
const restoreState = () => {
  const savedState = localStorage.getItem(STORAGE_KEY)
  if (savedState) {
    const state = JSON.parse(savedState)
    searchQuery.value = state.searchQuery
    showResults.value = state.showResults
    results.scenicSpots = state.results.scenicSpots
    results.foods = state.results.foods
    results.accommodations = state.results.accommodations
    results.guides = state.results.guides
  }
}

// 在组件挂载时恢复状态
onMounted(() => {
  restoreState()
})

const goToDetail = (type, id) => {
  if (!id) {
    ElMessage.warning('无效的ID')
    return
  }

  // 在跳转前保存状态
  saveState()

  const routes = {
    scenic: `/scenic/${id}`,
    food: `/food/detail/${id}`,
    accommodation: `/accommodation/${id}`,
    guide: `/guide/detail/${id}`
  }

  const route = routes[type]
  if (!route) {
    ElMessage.warning('无效的跳转类型')
    return
  }

  router.push(route)
}

// 在组件即将卸载时清除状态
// 但如果是跳转到详情页，我们已经保存了状态
// 所以这里我们需要检查路由
const clearStateIfNeeded = () => {
  const currentPath = router.currentRoute.value.path
  // 如果不是跳转到详情页，则清除状态
  if (!currentPath.includes('/scenic/') && 
      !currentPath.includes('/food/detail/') && 
      !currentPath.includes('/accommodation/') && 
      !currentPath.includes('/guide/detail/')) {
    localStorage.removeItem(STORAGE_KEY)
  }
}

onBeforeUnmount(() => {
  clearStateIfNeeded()
})
</script>

<style scoped lang="scss">
.smart-match-container {
  padding: 20px;
  min-height: 100vh;
  background: #f8f9fa;
}

.search-section {
  text-align: center;
  padding: 60px 20px;
  background: linear-gradient(135deg, #e8f5e9 0%, #e3f2fd 100%);
  border-radius: 20px;
  margin-bottom: 40px;
  transition: all 0.3s ease;

  &.search-active {
    padding: 40px 20px;
  }

  .search-content {
    max-width: 800px;
    margin: 0 auto;
  }

  .title {
    font-size: 2.5rem;
    color: #2c3e50;
    margin-bottom: 15px;
    font-weight: 600;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 10px;

    .emoji {
      font-size: 2rem;
    }
  }

  .subtitle {
    color: #666;
    font-size: 1.1rem;
    margin-bottom: 30px;
  }

  .search-box {
    max-width: 800px;
    margin: 0 auto 20px;
    position: relative;
    display: flex;
    align-items: center;

    .search-input {
      :deep(.el-input__wrapper) {
        padding-right: 0;
        border-radius: 25px;
        background: rgba(255, 255, 255, 0.9);
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
        transition: all 0.3s ease;
        border: 2px solid transparent;
        margin-right: 15px;

        &:hover {
          background: #fff;
          box-shadow: 0 6px 20px rgba(0, 0, 0, 0.08);
        }

        &.is-focus {
          background: #fff;
          border-color: #8e9aff;
          box-shadow: 0 6px 20px rgba(142, 154, 255, 0.15);
        }

        .el-input__prefix-inner {
          color: #8e9aff;
          font-size: 18px;
        }

        .el-input__inner {
          font-size: 16px;
          padding: 12px 5px;
          &::placeholder {
            color: #909399;
            font-size: 15px;
          }
        }
      }

      :deep(.el-input-group__append) {
        position: absolute;
        right: -15px;
        top: 50%;
        transform: translateY(-50%);
        border-radius: 50px;
        padding: 0;
        border: none;
        background: transparent;
        
        button {
          min-width: 120px;
          height: 45px;
          color: white;
          border: none;
          font-size: 15px;
          font-weight: 500;
          border-radius: 50px;
          background: linear-gradient(135deg, #8e9aff 0%, #6c63ff 100%);
          box-shadow: 0 4px 15px rgba(110, 99, 255, 0.3);
          transition: all 0.3s ease;
          display: flex;
          align-items: center;
          justify-content: center;
          padding: 0 25px;
          position: relative;

          .el-icon {
            font-size: 18px;
            position: absolute;
            left: 20px;
          }

          span {
            flex: 1;
            text-align: center;
            margin-left: 20px;
          }

          &:hover {
            transform: translateY(-2px);
            background: linear-gradient(135deg, #9ea8ff 0%, #7b73ff 100%);
            box-shadow: 0 6px 20px rgba(110, 99, 255, 0.4);
          }

          &:active {
            transform: translateY(0);
            background: linear-gradient(135deg, #7d88ff 0%, #5c54ff 100%);
            box-shadow: 0 2px 10px rgba(110, 99, 255, 0.3);
          }
        }
      }
    }
  }

  .tags-container {
    margin-top: 35px;

    .tags-title {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 8px;
      margin-bottom: 20px;
      color: #666;
      font-size: 1rem;
      opacity: 0.85;

      .el-icon {
        font-size: 1.2rem;
        color: #42b983;
      }
    }

    .tags-list {
      display: flex;
      flex-wrap: wrap;
      gap: 12px;
      justify-content: center;
      padding: 0 20px;

      .tag {
        cursor: pointer;
        transition: all 0.3s;
        padding: 8px 18px;
        border-radius: 20px;
        font-size: 0.95rem;
        border: none;
        background: var(--tag-bg);
        color: var(--tag-color);
        box-shadow: 0 3px 10px var(--tag-shadow);

        &.el-tag--info {
          --tag-bg: #fce4ec;
          --tag-color: #e91e63;
          --tag-shadow: rgba(233, 30, 99, 0.1);
        }

        &.el-tag--success {
          --tag-bg: #e8f5e9;
          --tag-color: #43a047;
          --tag-shadow: rgba(67, 160, 71, 0.1);
        }

        &.el-tag--warning {
          --tag-bg: #fff3e0;
          --tag-color: #fb8c00;
          --tag-shadow: rgba(251, 140, 0, 0.1);
        }

        &.el-tag--danger {
          --tag-bg: #f3e5f5;
          --tag-color: #9c27b0;
          --tag-shadow: rgba(156, 39, 176, 0.1);
        }

        &.el-tag--primary {
          --tag-bg: #e3f2fd;
          --tag-color: #1976d2;
          --tag-shadow: rgba(25, 118, 210, 0.1);
        }

        .tag-emoji {
          margin-right: 6px;
          font-size: 1.1rem;
        }

        &:hover {
          transform: translateY(-2px) scale(1.02);
          box-shadow: 0 5px 15px var(--tag-shadow);
          filter: brightness(1.05);
        }
      }
    }
  }
}

.results-section {
  max-width: 1400px;
  margin: 0 auto;

  .result-card {
    background: white;
    border-radius: 16px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
    overflow: hidden;
    height: 100%;
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-5px);
      box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
    }

    .card-header {
      padding: 20px;
      border-bottom: 1px solid #f0f0f0;
      display: flex;
      justify-content: space-between;
      align-items: center;

      .header-title {
        display: flex;
        align-items: center;
        gap: 10px;

        .emoji {
          font-size: 1.5rem;
        }

        h2 {
          margin: 0;
          font-size: 1.2rem;
          color: #2c3e50;
        }
      }
    }

    .card-content {
      padding: 20px;
      max-height: 500px;
      overflow-y: auto;

      &::-webkit-scrollbar {
        width: 6px;
      }

      &::-webkit-scrollbar-thumb {
        background-color: #ddd;
        border-radius: 3px;
      }

      .result-item {
        display: flex;
        gap: 15px;
        padding: 15px 0;
        border-bottom: 1px solid #f5f5f5;

        &:last-child {
          border-bottom: none;
        }

        .item-image {
          width: 120px;
          height: 80px;
          border-radius: 8px;
          overflow: hidden;
        }

        .image-placeholder {
          width: 100%;
          height: 100%;
          display: flex;
          align-items: center;
          justify-content: center;
          background: #f5f5f5;
          color: #999;
          font-size: 24px;
        }

        .item-info {
          flex: 1;
          display: flex;
          flex-direction: column;

          h3 {
            margin: 0 0 8px 0;
            font-size: 1.1rem;
            color: #2c3e50;
          }

          .description {
            margin: 0 0 10px 0;
            color: #666;
            font-size: 0.9rem;
            line-height: 1.4;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            overflow: hidden;
          }

          .item-footer {
            margin-top: auto;
            display: flex;
            justify-content: space-between;
            align-items: center;

            .meta-info {
              display: flex;
              align-items: center;
              gap: 10px;
              flex-wrap: wrap;

              .price {
                color: #ff6b6b;
                font-weight: 500;
              }

              .rating {
                display: flex;
                align-items: center;
              }
            }

            .location {
              display: flex;
              align-items: center;
              gap: 5px;
            }
          }
        }
      }

      .empty-state {
        padding: 40px 0;
      }
    }
  }
}

.mb-20 {
  margin-bottom: 20px;
}

@media (max-width: 768px) {
  .search-section {
    padding: 40px 15px;
    
    .title {
      font-size: 2rem;
    }
    
    .subtitle {
      font-size: 1rem;
    }
  }

  .result-card {
    .card-content {
      .result-item {
        .item-image {
          width: 100px;
          height: 70px;
        }
      }
    }
  }
}

.author {
  display: flex;
  align-items: center;
  gap: 8px;

  .author-info {
    display: flex;
    flex-direction: column;
    gap: 2px;

    .author-name {
      font-size: 14px;
      color: #333;
      font-weight: 500;
    }

    .publish-time {
      font-size: 12px;
      color: #999;
    }
  }
}
</style> 