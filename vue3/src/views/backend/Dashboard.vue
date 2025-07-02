<template>
  <div class="dashboard-container">
    <!-- 顶部数据卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="4" v-for="(item, index) in statsCards" :key="index">
        <div class="stats-card" :class="`gradient-${index + 1}`">
          <div class="stats-icon">
            <el-icon><component :is="item.icon" /></el-icon>
          </div>
          <div class="stats-info">
            <div class="stats-title">{{ item.title }}</div>
            <div class="stats-value">{{ item.value }}</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="chart-row">
      <!-- 订单趋势图 -->
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>近7天订单趋势</span>
            </div>
          </template>
          <div class="chart-container" ref="orderTrendChart"></div>
        </el-card>
      </el-col>

      <!-- 收藏排行榜 -->
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>热门收藏榜</span>
            </div>
          </template>
          <div class="chart-container" ref="collectionRankChart"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 评论统计和景点排行榜 -->
    <el-row :gutter="20" class="chart-row">
      <!-- 评论分析 -->
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>评论分析</span>
            </div>
          </template>
          <div class="chart-container" ref="commentChart"></div>
        </el-card>
      </el-col>

      <!-- 热门景点排行 -->
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>热门景点排行</span>
            </div>
          </template>
          <div v-if="loading" class="loading-container">
            <el-skeleton :rows="5" animated />
          </div>
          <div v-else-if="topScenics.length" class="scenic-list">
            <div v-for="(scenic, index) in topScenics" :key="index" class="scenic-item">
              <div class="rank-number" :class="{'top-three': index < 3}">{{ index + 1 }}</div>
              <el-image :src="getImageUrl(scenic.imageUrl)" fit="cover" class="scenic-image">
                <template #error>
                  <div class="image-slot">
                    <el-icon><Picture /></el-icon>
                  </div>
                </template>
              </el-image>
              <div class="scenic-info">
                <div class="scenic-name">{{ scenic.name }}</div>
                <div class="scenic-stats">
                  <el-tag size="small" type="success">收藏: {{ scenic.collectionCount }}</el-tag>
                  <el-tag size="small" type="warning">订单: {{ scenic.orderCount }}</el-tag>
                  <el-tag size="small" type="info">总热度: {{ scenic.totalCount }}</el-tag>
                </div>
              </div>
            </div>
          </div>
          <div v-else class="empty-container">
            <el-empty description="暂无数据" />
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { ref, onMounted, onUnmounted } from 'vue'
import { User, Ticket, Location, Collection, ChatDotRound, ShoppingCart, Picture } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'

const baseAPI = process.env.VUE_APP_BASE_API || '/api'

// 获取图片完整URL
const getImageUrl = (url) => {
  if (!url) return require('@/assets/images/no-image.png')
  return url.startsWith('http') ? url : baseAPI + url
}

export default {
  name: 'Dashboard',
  components: {
    Picture
  },
  setup() {
    // 统计卡片数据
    const statsCards = ref([
      { title: '总用户数', value: 0, icon: 'User', color: '#409EFF' },
      { title: '景点数量', value: 0, icon: 'Location', color: '#67C23A' },
      { title: '美食数量', value: 0, icon: 'Food', color: '#E6A23C' },
      { title: '收藏数', value: 0, icon: 'Collection', color: '#F56C6C' },
      { title: '评论数', value: 0, icon: 'ChatDotRound', color: '#909399' },
      { title: '订单数', value: 0, icon: 'ShoppingCart', color: '#9B59B6' }
    ])

    const loading = ref(true)
    const topScenics = ref([])

    // 图表实例
    const charts = {
      orderTrend: null,
      collectionRank: null,
      comment: null
    }

    // 初始化图表
    const initCharts = () => {
      const containers = document.querySelectorAll('.chart-container')
      charts.orderTrend = echarts.init(containers[0])
      charts.collectionRank = echarts.init(containers[1])
      charts.comment = echarts.init(containers[2])
    }

    // 更新订单趋势图表
    const updateOrderTrendChart = (data) => {
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: data.map(item => item.date)
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          data: data.map(item => item.count),
          type: 'line',
          smooth: true,
          areaStyle: {}
        }]
      }
      charts.orderTrend.setOption(option)
    }

    // 更新收藏排行榜图表
    const updateCollectionRankChart = (data) => {
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        xAxis: {
          type: 'value'
        },
        yAxis: {
          type: 'category',
          data: data.map(item => item.name)
        },
        series: [{
          type: 'bar',
          data: data.map(item => item.count)
        }]
      }
      charts.collectionRank.setOption(option)
    }

    // 更新评论分析图表
    const updateCommentChart = (data) => {
      const option = {
        tooltip: {
          trigger: 'item'
        },
        series: [{
          type: 'pie',
          radius: ['40%', '70%'],
          data: [
            { value: data.scenic, name: '景点评论' },
            { value: data.food, name: '美食评论' },
            { value: data.accommodation, name: '住宿评论' }
          ]
        }]
      }
      charts.comment.setOption(option)
    }

    // 监听窗口大小变化
    const handleResize = () => {
      Object.values(charts).forEach(chart => chart?.resize())
    }

    // 加载数据
    const fetchData = async () => {
      try {
        loading.value = true
        const response = await request.get('/api/monitor/dashboard')
        if (response.data) {
          // 更新统计卡片
          statsCards.value[0].value = response.data.totalUsers
          statsCards.value[1].value = response.data.totalScenic
          statsCards.value[2].value = response.data.totalFoods
          statsCards.value[3].value = response.data.totalCollections
          statsCards.value[4].value = response.data.totalComments
          statsCards.value[5].value = response.data.totalOrders

          // 更新图表
          updateOrderTrendChart(response.data.recentOrders || [])
          updateCollectionRankChart(response.data.topCollections || [])
          updateCommentChart(response.data.commentStats || {})

          // 更新热门景点排行
          topScenics.value = response.data.commentStats?.topScenics || []
        }
      } catch (error) {
        console.error('获取数据失败:', error)
        ElMessage.error('获取数据失败')
      } finally {
        loading.value = false
      }
    }

    onMounted(() => {
      initCharts()
      fetchData()
      window.addEventListener('resize', handleResize)
    })

    onUnmounted(() => {
      window.removeEventListener('resize', handleResize)
      Object.values(charts).forEach(chart => chart?.dispose())
    })

    return {
      loading,
      statsCards,
      topScenics,
      getImageUrl
    }
  }
}
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
}

.stats-row {
  margin-bottom: 20px;
}

.stats-card {
  height: 100px;
  display: flex;
  align-items: center;
  padding: 20px;
  border-radius: 8px;
  background: white;
  transition: all 0.3s ease;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.stats-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px 0 rgba(0, 0, 0, 0.1);
}

.stats-icon {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
}

.stats-icon :deep(.el-icon) {
  font-size: 20px;
  color: white;
}

.stats-info {
  flex: 1;
}

.stats-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 6px;
}

.stats-value {
  font-size: 22px;
  font-weight: 600;
  color: #303133;
}

/* 简约渐变色系 */
.gradient-1 {
  .stats-icon {
    background: linear-gradient(135deg, #409EFF, #53a8ff);
  }
}

.gradient-2 {
  .stats-icon {
    background: linear-gradient(135deg, #67C23A, #85ce61);
  }
}

.gradient-3 {
  .stats-icon {
    background: linear-gradient(135deg, #E6A23C, #ebb563);
  }
}

.gradient-4 {
  .stats-icon {
    background: linear-gradient(135deg, #F56C6C, #f78989);
  }
}

.gradient-5 {
  .stats-icon {
    background: linear-gradient(135deg, #909399, #a6a9ad);
  }
}

.gradient-6 {
  .stats-icon {
    background: linear-gradient(135deg, #9B59B6, #b07cc6);
  }
}

.chart-row {
  margin-top: 20px;
}

.chart-card {
  margin-bottom: 20px;
}

.chart-container {
  height: 300px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.scenic-list {
  max-height: 300px;
  overflow-y: auto;
  padding: 0 10px;
}

.scenic-item {
  display: flex;
  align-items: center;
  padding: 10px;
  border-bottom: 1px solid #eee;
}

.rank-number {
  width: 24px;
  height: 24px;
  line-height: 24px;
  text-align: center;
  border-radius: 50%;
  background-color: #f0f0f0;
  margin-right: 10px;
  font-weight: bold;
}

.rank-number.top-three {
  color: white;
}

.rank-number:nth-child(1) {
  background-color: #FFD700;
}

.rank-number:nth-child(2) {
  background-color: #C0C0C0;
}

.rank-number:nth-child(3) {
  background-color: #CD7F32;
}

.scenic-image {
  width: 80px;
  height: 60px;
  border-radius: 4px;
  margin-right: 10px;
}

.scenic-info {
  flex: 1;
}

.scenic-name {
  font-weight: bold;
  margin-bottom: 5px;
}

.scenic-stats {
  display: flex;
  gap: 8px;
}

.el-tag {
  margin-right: 5px;
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
}

.loading-container {
  padding: 20px;
}

.empty-container {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 40px 0;
}
</style> 