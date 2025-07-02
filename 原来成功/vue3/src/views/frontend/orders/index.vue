<template>
  <div class="orders-container">
    <!-- 现代化页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">
          <span class="title-icon">🎫</span>
          我的订单
        </h1>
        <p class="page-subtitle">
          查看和管理您的门票订单
        </p>
      </div>
    </div>

    <!-- 现代化标签页区域 -->
    <div class="orders-section">
      <div class="section-container">
        <div class="orders-tabs">
          <el-tabs
            v-model="activeTab"
            @tab-click="handleTabClick"
            class="modern-tabs"
          >
            <el-tab-pane label="全部" name="all">
              <template #label>
                <div class="tab-label">
                  <el-icon><List /></el-icon>
                  <span>全部</span>
                  <span class="tab-count">{{ orderStats.total || 0 }}</span>
                </div>
              </template>
            </el-tab-pane>
            <el-tab-pane label="待支付" name="0">
              <template #label>
                <div class="tab-label">
                  <el-icon><Clock /></el-icon>
                  <span>待支付</span>
                  <span class="tab-count">{{ orderStats.pending || 0 }}</span>
                </div>
              </template>
            </el-tab-pane>
            <el-tab-pane label="已支付" name="1">
              <template #label>
                <div class="tab-label">
                  <el-icon><Check /></el-icon>
                  <span>已支付</span>
                  <span class="tab-count">{{ orderStats.paid || 0 }}</span>
                </div>
              </template>
            </el-tab-pane>
            <el-tab-pane label="已取消" name="2">
              <template #label>
                <div class="tab-label">
                  <el-icon><Close /></el-icon>
                  <span>已取消</span>
                  <span class="tab-count">{{ orderStats.cancelled || 0 }}</span>
                </div>
              </template>
            </el-tab-pane>
            <el-tab-pane label="已退款" name="3">
              <template #label>
                <div class="tab-label">
                  <el-icon><RefreshLeft /></el-icon>
                  <span>已退款</span>
                  <span class="tab-count">{{ orderStats.refunded || 0 }}</span>
                </div>
              </template>
            </el-tab-pane>
            <el-tab-pane label="已完成" name="4">
              <template #label>
                <div class="tab-label">
                  <el-icon><CircleCheck /></el-icon>
                  <span>已完成</span>
                  <span class="tab-count">{{ orderStats.completed || 0 }}</span>
                </div>
              </template>
            </el-tab-pane>
          </el-tabs>

          <!-- 加载状态 -->
          <div v-if="loading" class="loading-state">
            <el-skeleton :rows="6" animated />
          </div>

          <!-- 空状态 -->
          <div v-else-if="orderList.length === 0" class="empty-state">
            <div class="empty-icon">📋</div>
            <h3 class="empty-title">暂无订单</h3>
            <p class="empty-desc">您还没有任何订单，快去预订门票吧</p>
            <el-button type="primary" @click="goToTicketList" class="empty-action">
              <el-icon><Ticket /></el-icon>
              浏览门票
            </el-button>
          </div>

          <!-- 订单列表 -->
          <div v-else class="orders-list">
            <div
              v-for="(order, index) in orderList"
              :key="order.id"
              class="order-card "
              :class="`delay-${(index % 4 + 1) * 100}`"
            >
              <div class="order-header">
                <div class="order-info">
                  <div class="order-no">
                    <el-icon><Document /></el-icon>
                    <span>{{ order.orderNo }}</span>
                  </div>
                  <div class="order-time">{{ formatTime(order.createTime) }}</div>
                </div>
                <div class="order-status-badge" :class="getStatusClass(order.status)">
                  {{ getOrderStatusText(order.status) }}
                </div>
              </div>

              <div class="order-content">
                <div class="ticket-section">
                  <div class="ticket-main">
                    <h3 class="ticket-name">{{ order.ticketName }}</h3>
                    <div class="scenic-info">
                      <el-icon><MapLocation /></el-icon>
                      <span>{{ order.scenicName }}</span>
                    </div>
                  </div>

                  <div class="order-details">
                    <div class="detail-row">
                      <div class="detail-item">
                        <el-icon><Calendar /></el-icon>
                        <span>游玩日期</span>
                        <strong>{{ formatDate(order.visitDate) }}</strong>
                      </div>
                      <div class="detail-item">
                        <el-icon><User /></el-icon>
                        <span>数量</span>
                        <strong>{{ order.quantity }} 张</strong>
                      </div>
                    </div>

                    <div class="detail-row">
                      <div class="detail-item">
                        <el-icon><UserFilled /></el-icon>
                        <span>游客</span>
                        <strong>{{ order.visitorName }}</strong>
                      </div>
                      <div class="detail-item">
                        <el-icon><Phone /></el-icon>
                        <span>联系电话</span>
                        <strong>{{ order.visitorPhone }}</strong>
                      </div>
                    </div>
                  </div>
                </div>

                <div class="price-section">
                  <div class="price-label">订单总额</div>
                  <div class="price-amount">¥{{ order.totalAmount }}</div>
                </div>
              </div>

              <div class="order-footer">
                <div class="payment-info" v-if="order.paymentTime">
                  <el-icon><CreditCard /></el-icon>
                  <span>支付时间: {{ formatTime(order.paymentTime) }}</span>
                </div>
                <div class="order-actions">
                  <el-button
                    v-if="order.status === 0"
                    type="primary"
                    @click="payOrder(order)"
                    class="action-btn pay-btn"
                  >
                    <el-icon><CreditCard /></el-icon>
                    去支付
                  </el-button>
                  <el-button
                    v-if="order.status === 0"
                    type="danger"
                    @click="cancelOrder(order.id)"
                    class="action-btn cancel-btn"
                  >
                    <el-icon><Close /></el-icon>
                    取消订单
                  </el-button>
                  <el-button
                    @click="viewOrderDetail(order.id)"
                    class="action-btn detail-btn"
                  >
                    <el-icon><View /></el-icon>
                    查看详情
                  </el-button>
                </div>
              </div>
            </div>

            <!-- 现代化分页 -->
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

    <!-- 支付对话框 -->
    <el-dialog
      title="订单支付"
      v-model="payDialogVisible"
      width="500px"
      :close-on-click-modal="false"
    >
      <div class="pay-dialog-content" v-if="currentOrder">
        <div class="order-info">
          <p><strong>订单号:</strong> {{ currentOrder.orderNo }}</p>
          <p><strong>门票名称:</strong> {{ currentOrder.ticketName }}</p>
          <p><strong>游玩日期:</strong> {{ formatDate(currentOrder.visitDate) }}</p>
          <p><strong>数量:</strong> {{ currentOrder.quantity }}</p>
          <p><strong>总金额:</strong> <span class="amount">¥{{ currentOrder.totalAmount }}</span></p>
        </div>
        <div class="payment-methods">
          <h4>请选择支付方式</h4>
          <el-radio-group v-model="paymentMethod">
            <el-radio label="WECHAT">微信支付</el-radio>
            <el-radio label="ALIPAY">支付宝</el-radio>
            <el-radio label="BANK_CARD">银行卡</el-radio>
          </el-radio-group>
        </div>
        <div class="payment-info" v-if="paymentMethod">
          <template v-if="paymentMethod === 'ALIPAY'">
            <p class="payment-tip">点击下方"去支付"按钮，跳转到支付宝支付页面</p>
          </template>
          <template v-else>
            <p class="qrcode-tip">请扫描二维码完成支付</p>
            <div class="qrcode-image">
              <!-- 这里放二维码图片，实际项目中可能需要后端生成 -->
              <el-image
                style="width: 200px; height: 200px"
                :src="require('@/assets/payment-qrcode.png')"
                fit="cover"
              ></el-image>
            </div>
          </template>
        </div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="payDialogVisible = false">取消</el-button>
          <template v-if="paymentMethod === 'ALIPAY'">
            <el-button type="primary" @click="goToAlipay">去支付</el-button>
          </template>
          <template v-else>
            <el-button type="primary" @click="confirmPayment">确认已支付</el-button>
          </template>
        </span>
      </template>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog
      title="订单详情"
      v-model="detailDialogVisible"
      width="600px"
    >
      <div class="order-detail" v-if="currentOrder">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="订单号">{{ currentOrder.orderNo }}</el-descriptions-item>
          <el-descriptions-item label="门票名称">{{ currentOrder.ticketName }}</el-descriptions-item>
          <el-descriptions-item label="景点名称">{{ currentOrder.scenicName }}</el-descriptions-item>
          <el-descriptions-item label="游玩日期">{{ formatDate(currentOrder.visitDate) }}</el-descriptions-item>
          <el-descriptions-item label="购买数量">{{ currentOrder.quantity }}</el-descriptions-item>
          <el-descriptions-item label="订单金额">¥{{ currentOrder.totalAmount }}</el-descriptions-item>
          <el-descriptions-item label="游客姓名">{{ currentOrder.visitorName }}</el-descriptions-item>
          <el-descriptions-item label="联系电话">{{ currentOrder.visitorPhone }}</el-descriptions-item>
          <el-descriptions-item label="身份证号">
            {{ currentOrder.idCard || '未提供' }}
          </el-descriptions-item>
          <el-descriptions-item label="订单状态">
            {{ getOrderStatusText(currentOrder.status) }}
          </el-descriptions-item>
          <el-descriptions-item label="下单时间">
            {{ formatTime(currentOrder.createTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="支付时间" v-if="currentOrder.paymentTime">
            {{ formatTime(currentOrder.paymentTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="支付方式" v-if="currentOrder.paymentMethod">
            {{ getPaymentMethodText(currentOrder.paymentMethod) }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'


const router = useRouter()

// 分页参数
const currentPage = ref(1)
const pageSize = ref(12)
const total = ref(0)

// 订单列表数据
const orderList = ref([])
const loading = ref(false)
const activeTab = ref('all')

// 对话框控制
const payDialogVisible = ref(false)
const detailDialogVisible = ref(false)
const currentOrder = ref(null)
const paymentMethod = ref('')

// 订单统计数据
const orderStats = computed(() => {
  const stats = {
    total: orderList.value.length,
    pending: 0,
    paid: 0,
    cancelled: 0,
    refunded: 0,
    completed: 0
  }

  orderList.value.forEach(order => {
    switch (order.status) {
      case 0:
        stats.pending++
        break
      case 1:
        stats.paid++
        break
      case 2:
        stats.cancelled++
        break
      case 3:
        stats.refunded++
        break
      case 4:
        stats.completed++
        break
    }
  })

  return stats
})

// 获取状态样式类
const getStatusClass = (status) => {
  const statusMap = {
    0: 'pending',
    1: 'paid',
    2: 'cancelled',
    3: 'refunded',
    4: 'completed'
  }
  return statusMap[status] || 'default'
}

// 跳转到门票列表
const goToTicketList = () => {
  router.push('/ticket')
}

// 获取订单列表
const fetchOrders = async () => {
  loading.value = true
  try {
    const status = activeTab.value === 'all' ? '' : activeTab.value
    
    await request.get('/order/my', {
      status,
      currentPage: currentPage.value,
      size: pageSize.value
    }, {
      showDefaultMsg: false,
      onSuccess: (res) => {
        orderList.value = res?.records || []
        total.value = res?.total || 0
      }
    })
  } catch (error) {
    console.error('获取订单列表失败:', error)
    orderList.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

// 分页变化事件
const handleCurrentChange = (page) => {
  currentPage.value = page
  fetchOrders()
}

// 标签页切换事件
const handleTabClick = () => {
  currentPage.value = 1
  fetchOrders()
}

// 获取订单状态文本
const getOrderStatusText = (status) => {
  const statusMap = {
    0: '待支付',
    1: '已支付',
    2: '已取消',
    3: '已退款',
    4: '已完成'
  }
  return statusMap[status] || '未知状态'
}

// 获取支付方式文本
const getPaymentMethodText = (method) => {
  const methodMap = {
    'WECHAT': '微信支付',
    'ALIPAY': '支付宝',
    'BANK_CARD': '银行卡'
  }
  return methodMap[method] || '未知方式'
}

// 格式化日期
const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
}

// 格式化时间
const formatTime = (timeStr) => {
  if (!timeStr) return ''
  const date = new Date(timeStr)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}:${String(date.getSeconds()).padStart(2, '0')}`
}

// 支付订单
const payOrder = (order) => {
  if (!order || !order.id) {
    ElMessage.error('订单数据无效')
    return
  }
  currentOrder.value = order
  paymentMethod.value = '' // 重置支付方式选择
  payDialogVisible.value = true
}

// 确认支付
const confirmPayment = async () => {
  if (!paymentMethod.value) {
    ElMessage.warning('请选择支付方式')
    return
  }
  
  if (!currentOrder.value || !currentOrder.value.id) {
    ElMessage.error('订单数据无效')
    payDialogVisible.value = false
    return
  }

  loading.value = true
  try {
    await request.post(`/order/${currentOrder.value.id}/pay`, null, {
      params: {
        paymentMethod: paymentMethod.value
      },
      successMsg: '支付成功',
      onSuccess: () => {
        payDialogVisible.value = false
        fetchOrders()  // 重新加载订单列表
      }
    })
  } catch (error) {
    console.error('支付订单失败:', error)
  } finally {
    loading.value = false
  }
}

// 跳转到支付宝支付页面
const goToAlipay = () => {
  if (currentOrder.value && currentOrder.value.id) {
    payDialogVisible.value = false;
    router.push(`/payment/alipay/${currentOrder.value.id}`)
  } else {
    ElMessage.error('订单信息错误')
  }
}

// 取消订单
const cancelOrder = async (orderId) => {
  ElMessageBox.confirm('确定要取消该订单吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    loading.value = true
    try {
      await request.post(`/order/${orderId}/cancel`, {}, {
        successMsg: '订单已取消',
        onSuccess: () => {
          fetchOrders()  // 重新加载订单列表
        }
      })
    } catch (error) {
      console.error('取消订单失败:', error)
    } finally {
      loading.value = false
    }
  }).catch(() => {})
}

// 查看订单详情
const viewOrderDetail = async (orderId) => {
  loading.value = true
  try {
    await request.get(`/order/${orderId}`, {}, {
      showDefaultMsg: false,
      onSuccess: (res) => {
        if (res) {
          currentOrder.value = res
          detailDialogVisible.value = true
        } else {
          ElMessage.error('获取订单详情失败')
        }
      }
    })
  } catch (error) {
    console.error('获取订单详情失败:', error)
    ElMessage.error('获取订单详情失败')
  } finally {
    loading.value = false
  }
}

// 页面加载时获取订单列表
onMounted(() => {
  fetchOrders()
})
</script>

<style lang="scss" scoped>
.orders-container {
  min-height: 100vh;
  background: #f8fafc;
  font-family: "思源黑体", "Source Han Sans", "Noto Sans CJK SC", sans-serif;
  color: #333;

  // 通用容器样式
  .section-container {
    max-width: 1300px;
    margin: 0 auto;
    padding: 40px 20px;
  }

  // 页面头部
  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 40px;
    padding: 40px 0 20px;
    border-bottom: 1px solid #e2e8f0;
  }

  .header-content {
    flex: 1;
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
    text-align: left;
    margin: 0;
  }




  // 订单区域
  .orders-section {
    background: white;
    margin: 0;
    padding-top: 20px;
  }

  .orders-tabs {
    background: white;
    border-radius: 16px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
    overflow: hidden;
    border: 1px solid #e2e8f0;
  }

  // 现代化标签页样式
  .modern-tabs {
    :deep(.el-tabs__header) {
      margin: 0;
      background: #f8fafc;
      border-bottom: 1px solid #e2e8f0;
    }

    :deep(.el-tabs__nav-wrap) {
      padding: 0 24px;
    }

    :deep(.el-tabs__item) {
      padding: 20px 0;
      font-size: 16px;
      font-weight: 600;
      color: #64748b;
      border: none;
      margin-right: 40px;

      &.is-active {
        color: #667eea;
      }

      &:hover {
        color: #667eea;
      }
    }

    :deep(.el-tabs__active-bar) {
      background: linear-gradient(45deg, #667eea, #764ba2);
      height: 3px;
    }

    :deep(.el-tabs__content) {
      padding: 40px 24px;
    }

    .tab-label {
      display: flex;
      align-items: center;
      gap: 8px;

      .tab-count {
        background: #667eea;
        color: white;
        padding: 2px 8px;
        border-radius: 12px;
        font-size: 12px;
        font-weight: 600;
      }
    }
  }

  // 加载状态
  .loading-state {
    padding: 40px 20px;
  }

  // 空状态
  .empty-state {
    text-align: center;
    padding: 80px 20px;

    .empty-icon {
      font-size: 64px;
      margin-bottom: 20px;
    }

    .empty-title {
      font-size: 24px;
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

  // 订单列表
  .orders-list {
    display: flex;
    flex-direction: column;
    gap: 20px;
  }

  .order-card {
    background: white;
    border-radius: 16px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
    border: 1px solid #e2e8f0;
    overflow: hidden;
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-4px);
      box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
    }
  }

  .order-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px 24px;
    background: #f8fafc;
    border-bottom: 1px solid #e2e8f0;

    .order-info {
      .order-no {
        display: flex;
        align-items: center;
        gap: 8px;
        font-size: 16px;
        font-weight: 600;
        color: #2d3748;
        margin-bottom: 4px;

        .el-icon {
          color: #667eea;
        }
      }

      .order-time {
        font-size: 12px;
        color: #64748b;
      }
    }

    .order-status-badge {
      padding: 6px 12px;
      border-radius: 12px;
      font-size: 12px;
      font-weight: 600;

      &.pending {
        background: linear-gradient(45deg, #f59e0b, #d97706);
        color: white;
      }

      &.paid {
        background: linear-gradient(45deg, #10b981, #059669);
        color: white;
      }

      &.cancelled {
        background: linear-gradient(45deg, #ef4444, #dc2626);
        color: white;
      }

      &.refunded {
        background: linear-gradient(45deg, #8b5cf6, #7c3aed);
        color: white;
      }

      &.completed {
        background: linear-gradient(45deg, #06b6d4, #0891b2);
        color: white;
      }
    }
  }

  .order-content {
    padding: 24px;
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    gap: 24px;
  }

  .ticket-section {
    flex: 1;

    .ticket-main {
      margin-bottom: 16px;

      .ticket-name {
        font-size: 20px;
        font-weight: 700;
        color: #2d3748;
        margin: 0 0 8px;
        line-height: 1.3;
      }

      .scenic-info {
        display: flex;
        align-items: center;
        gap: 6px;
        font-size: 14px;
        color: #64748b;

        .el-icon {
          color: #667eea;
        }
      }
    }

    .order-details {
      .detail-row {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 16px;
        margin-bottom: 12px;

        &:last-child {
          margin-bottom: 0;
        }
      }

      .detail-item {
        display: flex;
        align-items: center;
        gap: 8px;
        font-size: 14px;
        color: #64748b;

        .el-icon {
          color: #667eea;
          flex-shrink: 0;
        }

        strong {
          color: #2d3748;
          font-weight: 600;
          margin-left: auto;
        }
      }
    }
  }

  .price-section {
    text-align: right;
    flex-shrink: 0;

    .price-label {
      font-size: 14px;
      color: #64748b;
      margin-bottom: 4px;
    }

    .price-amount {
      font-size: 28px;
      font-weight: 700;
      color: #e53e3e;
    }
  }

  .order-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px 24px;
    background: #f8fafc;
    border-top: 1px solid #e2e8f0;

    .payment-info {
      display: flex;
      align-items: center;
      gap: 6px;
      font-size: 12px;
      color: #64748b;

      .el-icon {
        color: #667eea;
      }
    }

    .order-actions {
      display: flex;
      gap: 8px;

      .action-btn {
        border-radius: 8px;
        font-weight: 600;
        transition: all 0.3s ease;

        &.pay-btn {
          background: linear-gradient(45deg, #667eea, #764ba2);
          border: none;
          color: white;

          &:hover {
            transform: translateY(-1px);
            box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
          }
        }

        &.cancel-btn {
          background: #f56565;
          border: none;
          color: white;

          &:hover {
            background: #e53e3e;
            transform: translateY(-1px);
          }
        }

        &.detail-btn {
          background: white;
          border: 2px solid #e2e8f0;
          color: #64748b;

          &:hover {
            border-color: #667eea;
            color: #667eea;
            background: #f8fafc;
          }
        }
      }
    }
  }

  // 分页样式
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

        &:hover {
          background: #667eea;
          color: white;
        }

        &.is-active {
          background: linear-gradient(45deg, #667eea, #764ba2);
          color: white;
        }
      }

      .btn-prev,
      .btn-next {
        border-radius: 8px;
        transition: all 0.3s ease;

        &:hover {
          background: #667eea;
          color: white;
        }
      }
    }
  }

 
  .hover-lift {
    transition: transform 0.3s ease, box-shadow 0.3s ease;
  }

  // 延迟动画类
  .delay-100 { animation-delay: 0.1s; }
  .delay-200 { animation-delay: 0.2s; }
  .delay-300 { animation-delay: 0.3s; }
  .delay-400 { animation-delay: 0.4s; }

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

 

}

</style>