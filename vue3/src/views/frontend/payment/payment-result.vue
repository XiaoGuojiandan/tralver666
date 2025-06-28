<template>
  <div class="payment-result-container">
    <el-result
      :icon="resultInfo.icon"
      :title="resultInfo.title"
      :sub-title="resultInfo.subTitle"
    >
      <template #extra>
        <el-button type="primary" @click="goToOrders">查看我的订单</el-button>
        <el-button @click="goToTickets">继续浏览门票</el-button>
      </template>
    </el-result>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const router = useRouter()
const route = useRoute()

const paymentStatus = ref('processing') // processing, success, failed
const paymentInfo = ref({})

// 计算结果信息
const resultInfo = computed(() => {
  switch (paymentStatus.value) {
    case 'success':
      return {
        icon: 'success',
        title: '支付成功',
        subTitle: '您的订单已支付成功，感谢您的购买！'
      }
    case 'failed':
      return {
        icon: 'error',
        title: '支付失败',
        subTitle: '抱歉，您的支付未能完成，请稍后再试。'
      }
    default:
      return {
        icon: 'info',
        title: '处理中',
        subTitle: '正在处理您的支付结果，请稍候...'
      }
  }
})

// 查询支付结果
const checkPaymentResult = async () => {
  try {
    // 从URL获取支付宝回调参数
    const outTradeNo = route.query.out_trade_no
    
    if (!outTradeNo) {
      paymentStatus.value = 'failed'
      return
    }
    
    await request.get('/order/status', {
      orderNo: outTradeNo
    }, {
      showDefaultMsg: false,
      onSuccess: (res) => {
        if (res && res.status === 1) { // 1表示已支付
          paymentStatus.value = 'success'
          paymentInfo.value = res
        } else {
          paymentStatus.value = 'failed'
        }
      },
      onError: () => {
        paymentStatus.value = 'failed'
      }
    })
  } catch (error) {
    console.error('查询支付结果失败:', error)
    paymentStatus.value = 'failed'
  }
}

const goToOrders = () => {
  router.push('/orders')
}

const goToTickets = () => {
  router.push('/tickets')
}

onMounted(() => {
  // 如果URL中有支付宝回调参数，则查询支付结果
  if (route.query.out_trade_no) {
    checkPaymentResult()
  } else {
    paymentStatus.value = 'failed'
  }
})
</script>

<style scoped>
.payment-result-container {
  width: 100%;
  height: 60vh;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
}
</style> 