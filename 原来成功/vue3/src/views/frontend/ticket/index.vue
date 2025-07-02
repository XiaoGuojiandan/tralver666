<template>
  <div class="ticket-list-container">
    <div class="section-container">
      <!-- 左侧城市侧边栏 -->
      <div class="sidebar">
        <CitySidebar @citySelect="handleCitySelect" />
      </div>

      <!-- 右侧主内容区 -->
      <div class="main-content">
        <!-- 页面头部 -->
        <div class="page-header">
          <div class="header-content">
            <h1 class="page-title">
              <span class="title-icon">🎫</span>
              精选门票预订
            </h1>
            <p class="page-subtitle">
              探索各地热门景点，预订优惠门票，开启美好旅程
            </p>
          </div>
        </div>

        <!-- 搜索区域 -->
        <div class="search-card">
          <div class="search-header">
            <h3 class="search-title">
              <el-icon><Search /></el-icon>
              智能筛选
            </h3>
          </div>
          
          <div class="search-form">
            <div class="search-inputs">
              <div class="search-input-group">
                <el-input
                  v-model="searchForm.ticketName"
                  placeholder="搜索门票名称或景点..."
                  clearable
                  size="large"
                  class="main-search-input"
                >
                  <template #prefix>
                    <el-icon><Search /></el-icon>
                  </template>
                </el-input>
              </div>
              <div class="search-actions">
                <el-button
                  type="primary"
                  size="large"
                  @click="handleSearch"
                  class="search-btn"
                >
                  <el-icon><Search /></el-icon>
                  搜索门票
                </el-button>
                <el-button
                  size="large"
                  @click="handleReset"
                  class="reset-btn"
                >
                  <el-icon><Refresh /></el-icon>
                  重置
                </el-button>
              </div>
            </div>

            <!-- 高级筛选选项 -->
            <div class="advanced-filters">
              <div class="filter-row">
                <div class="filter-group">
                  <label class="filter-label">门票类型</label>
                  <el-select
                    v-model="searchForm.ticketType"
                    placeholder="选择类型"
                    clearable
                    class="filter-select"
                  >
                    <el-option label="成人票" value="成人票" />
                    <el-option label="儿童票" value="儿童票" />
                    <el-option label="学生票" value="学生票" />
                    <el-option label="老人票" value="老人票" />
                  </el-select>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 门票列表 -->
        <div class="ticket-list-section">
          <!-- 加载状态 -->
          <div v-if="loading" class="loading-state">
            <el-skeleton :rows="8" animated />
          </div>

          <!-- 空状态 -->
          <div v-else-if="ticketList.length === 0" class="empty-state">
            <div class="empty-icon">🎫</div>
            <h3 class="empty-title">暂无符合条件的门票</h3>
            <p class="empty-desc">试试调整搜索条件或浏览其他门票</p>
            <el-button type="primary" @click="handleReset" class="empty-action">
              <el-icon><Refresh /></el-icon>
              重新搜索
            </el-button>
          </div>

          <!-- 门票网格 -->
          <div v-else class="ticket-grid">
            <div
              v-for="ticket in ticketList"
              :key="ticket.id"
              class="ticket-card"
              @click="goToBooking(ticket.id)"
            >
              <div class="card-header">
                <div class="ticket-type-badge">{{ ticket.ticketType }}</div>
                <div class="card-actions">
                  <el-button
                    type="primary"
                    size="small"
                    @click.stop="goToBooking(ticket.id)"
                    class="quick-book-btn"
                  >
                    <el-icon><Ticket /></el-icon>
                  </el-button>
                </div>
              </div>

              <div class="card-content">
                <h3 class="ticket-name">{{ ticket.ticketName }}</h3>
                
                <div class="ticket-price-section">
                  <div class="price-info">
                    <template v-if="ticket.discountPrice">
                      <span class="discount-price">¥{{ ticket.discountPrice }}</span>
                      <span class="original-price">¥{{ ticket.originalPrice }}</span>
                      <span class="discount-badge">特惠</span>
                    </template>
                    <template v-else>
                      <span class="normal-price">¥{{ ticket.originalPrice }}</span>
                    </template>
                  </div>
                  <div class="valid-period">
                    <el-icon><Calendar /></el-icon>
                    <span>{{ ticket.validPeriod }}</span>
                  </div>
                </div>

                <div class="card-footer">
                  <div class="ticket-meta">
                    <div class="stock-info">
                      <el-icon><Goods /></el-icon>
                      <span>余票 {{ ticket.stock }} 张</span>
                    </div>
                    <div class="status-info" v-if="ticket.status === 0">
                      <el-tag type="info" size="small">暂不可预订</el-tag>
                    </div>
                  </div>
                  <el-button
                    type="primary"
                    class="booking-btn"
                    :disabled="ticket.status === 0"
                  >
                    {{ ticket.status === 1 ? '立即预订' : '暂不可预订' }}
                  </el-button>
                </div>
              </div>
            </div>
          </div>

          <!-- 分页 -->
          <div class="pagination-wrapper" v-if="total > 0">
            <el-pagination
              background
              layout="total, prev, pager, next"
              :total="total"
              :page-size="pageSize"
              :current-page="currentPage"
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
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import request from '@/utils/request'
import { Search, Refresh, Ticket, Calendar, Goods } from '@element-plus/icons-vue'
import CitySidebar from '@/components/frontend/CitySidebar.vue'
import { ElMessage } from 'element-plus'

const router = useRouter()

// 数据列表
const ticketList = ref([])
const total = ref(0)
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(12)

// 搜索表单
const searchForm = ref({
  ticketName: '',
  ticketType: '',
  scenicId: null
})

// 城市选择处理
const selectedCity = ref(null)

const handleCitySelect = (city) => {
  selectedCity.value = city
  currentPage.value = 1
  fetchTickets()
}

// 获取门票列表
const fetchTickets = async () => {
  loading.value = true
  try {
    const params = {
      ticketName: searchForm.value.ticketName,
      ticketType: searchForm.value.ticketType,
      scenicId: searchForm.value.scenicId,
      cityCode: selectedCity.value?.name,
      currentPage: currentPage.value,
      size: pageSize.value
    }
    console.log('请求参数:', params)
    const res = await request.get('/ticket/page', params, {
      showDefaultMsg: false,
      onSuccess: (data) => {
        console.log('成功回调数据:', data)
        ticketList.value = data.records || []
        total.value = data.total || 0
      }
    })
    console.log('完整响应:', res)
  } catch (error) {
    console.error('获取门票列表失败:', error)
    ElMessage.error('获取门票列表失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 监听分页变化
watch([currentPage, pageSize], () => {
  fetchTickets()
})

// 监听搜索条件变化
watch([() => searchForm.value.ticketName, () => searchForm.value.ticketType], () => {
  currentPage.value = 1
  fetchTickets()
})

// 搜索按钮点击事件
const handleSearch = () => {
  currentPage.value = 1
  fetchTickets()
}

// 重置搜索条件
const handleReset = () => {
  searchForm.value.ticketName = ''
  searchForm.value.ticketType = ''
  searchForm.value.scenicId = null
  selectedCity.value = null
  currentPage.value = 1
  fetchTickets()
}

// 前往预订页面
const goToBooking = (ticketId) => {
  router.push(`/ticket/booking/${ticketId}`)
}

// 页面加载时获取数据
onMounted(() => {
  fetchTickets()
})
</script>

<style lang="scss" scoped>
.ticket-list-container {
  min-height: 100vh;
  background: #f8fafc;
  font-family: "思源黑体", "Source Han Sans", "Noto Sans CJK SC", sans-serif;
  color: #333;
  padding: 40px 0;
}

.section-container {
  max-width: 1440px;
  margin: 0 auto;
  padding: 0 32px;
  display: flex;
  gap: 32px;
}

// 侧边栏
.sidebar {
  flex: 0 0 280px;
  position: sticky;
  top: 24px;
  height: fit-content;
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  border: 1px solid #e2e8f0;
  overflow: hidden;
}

// 主内容区
.main-content {
  flex: 1;
  min-width: 0;
}

// 页面头部
.page-header {
  margin-bottom: 40px;
}

.page-title {
  font-size: 36px;
  font-weight: 700;
  margin: 0 0 8px;
  color: #2d3748;
  display: flex;
  align-items: center;
  gap: 12px;

  .title-icon {
    font-size: 32px;
  }
}

.page-subtitle {
  font-size: 16px;
  color: #64748b;
  margin: 0;
  text-align: left;
}

// 搜索卡片
.search-card {
  background: white;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  margin-bottom: 30px;
  border: 1px solid #e2e8f0;
}

.search-header {
  margin-bottom: 20px;
}

.search-title {
  font-size: 20px;
  font-weight: 700;
  color: #2d3748;
  margin: 0;
  display: flex;
  align-items: center;
  gap: 8px;

  .el-icon {
    color: #667eea;
  }
}


.search-form {
  .search-inputs {
    display: grid;
    grid-template-columns: 2fr auto;
    gap: 16px;
    align-items: end;
    margin-bottom: 20px;
  }

  .main-search-input {
    :deep(.el-input__wrapper) {
      border-radius: 12px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
      border: 2px solid #e2e8f0;
      transition: all 0.3s ease;

      &:hover {
        border-color: #667eea;
      }

      &.is-focus {
        border-color: #667eea;
        box-shadow: 0 4px 12px rgba(102, 126, 234, 0.2);
      }
    }
  }

  .search-actions {
    display: flex;
    gap: 12px;
  }

  .search-btn {
    background: linear-gradient(45deg, #667eea, #764ba2);
    border: none;
    border-radius: 12px;
    font-weight: 600;
    box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 6px 20px rgba(102, 126, 234, 0.6);
    }
  }

  .reset-btn {
    border-radius: 12px;
    border: 2px solid #e2e8f0;
    color: #64748b;
    background: white;

    &:hover {
      border-color: #667eea;
      color: #667eea;
      background: #f8fafc;
    }
  }
}

// 高级筛选
.advanced-filters {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #e2e8f0;
}

.filter-row {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
}

.filter-group {
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 12px;
}

.filter-label {
  font-size: 14px;
  font-weight: 600;
  color: #2d3748;
  white-space: nowrap;
}

.filter-select {
  :deep(.el-input__wrapper) {
    border-radius: 8px;
    border: 1px solid #e2e8f0;
    transition: all 0.3s ease;

    &:hover {
      border-color: #667eea;
    }

    &.is-focus {
      border-color: #667eea;
    }
  }
}

// 门票列表
.ticket-list-section {
  margin-top: 40px;
}

// 门票网格
.ticket-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 24px;
  margin-bottom: 40px;
}

.ticket-card {
  background: white;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  cursor: pointer;
  border: 1px solid #e2e8f0;

  &:hover {
    transform: translateY(-8px);
    box-shadow: 0 12px 40px rgba(0, 0, 0, 0.12);
  }
}

.card-header {
  padding: 16px;
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.ticket-type-badge {
  padding: 6px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
  background: linear-gradient(45deg, #667eea, #764ba2);
  color: white;
}

.quick-book-btn {
  border-radius: 50%;
  width: 36px;
  height: 36px;
  padding: 0;
  background: linear-gradient(45deg, #667eea, #764ba2);
  border: none;
  transition: all 0.3s ease;

  &:hover {
    transform: scale(1.1);
    box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
  }
}

.card-content {
  padding: 20px;
}

.ticket-name {
  font-size: 18px;
  font-weight: 700;
  color: #2d3748;
  margin: 0 0 16px;
  line-height: 1.4;
}

.ticket-price-section {
  margin-bottom: 16px;
}

.price-info {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;

  .discount-price {
    font-size: 24px;
    font-weight: 700;
    color: #e53e3e;
  }

  .original-price {
    font-size: 14px;
    color: #94a3b8;
    text-decoration: line-through;
  }

  .normal-price {
    font-size: 24px;
    font-weight: 700;
    color: #2d3748;
  }

  .discount-badge {
    padding: 2px 6px;
    border-radius: 8px;
    font-size: 12px;
    background: #e53e3e;
    color: white;
  }
}

.valid-period {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 14px;
  color: #64748b;

  .el-icon {
    color: #667eea;
  }
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid #e2e8f0;
}

.ticket-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  
  .stock-info {
    display: flex;
    align-items: center;
    gap: 4px;
    color: #64748b;
    font-size: 14px;
  }
  
  .status-info {
    display: flex;
    align-items: center;
  }
}

.booking-btn {
  &:disabled {
    background-color: #e2e8f0;
    border-color: #e2e8f0;
    color: #94a3b8;
    cursor: not-allowed;
    
    &:hover {
      background-color: #e2e8f0;
      border-color: #e2e8f0;
      color: #94a3b8;
    }
  }
}

// 空状态
.empty-state {
  text-align: center;
  padding: 60px 20px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);

  .empty-icon {
    font-size: 48px;
    margin-bottom: 20px;
  }

  .empty-title {
    font-size: 20px;
    font-weight: 600;
    color: #2d3748;
    margin: 0 0 8px;
  }

  .empty-desc {
    font-size: 16px;
    color: #64748b;
    margin: 0 0 24px;
  }

  .empty-action {
    background: linear-gradient(45deg, #667eea, #764ba2);
    border: none;
    border-radius: 20px;
    padding: 12px 24px;
    font-weight: 600;
  }
}

// 分页
.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 40px;
}

.modern-pagination {
  :deep(.el-pagination) {
    .el-pager li {
      border-radius: 8px;
      margin: 0 4px;
      transition: all 0.3s ease;

      &.is-active {
        background: linear-gradient(45deg, #667eea, #764ba2);
        color: white;
      }
    }

    .btn-prev,
    .btn-next {
      border-radius: 8px;
    }
  }
}

// 响应式设计
@media (max-width: 1200px) {
  .section-container {
    padding: 0 24px;
  }

  .ticket-grid {
    grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  }
}

@media (max-width: 992px) {
  .section-container {
    flex-direction: column;
  }

  .sidebar {
    width: 100%;
    position: static;
    margin-bottom: 24px;
  }
}

@media (max-width: 768px) {
  .section-container {
    flex-direction: column;
    padding: 20px;
  }

  .sidebar {
    width: 100%;
    position: static;
    margin-bottom: 20px;
  }

  .search-form {
    .search-inputs {
      grid-template-columns: 1fr;
    }
  }

  .ticket-grid {
    grid-template-columns: 1fr;
    gap: 16px;
  }
}
</style>