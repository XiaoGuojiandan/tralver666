<template>
  <div class="alipay-form-container">
    <div class="alipay-form" v-html="formHtml"></div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import request from '@/utils/request'

const route = useRoute()
const formHtml = ref('')

// 获取支付表单
const fetchAlipayForm = async () => {
  try {
    await request.get(`/alipay/pay/${route.params.id}`, {}, {
      showDefaultMsg: false,
      onSuccess: (res) => {
        formHtml.value = res
        // 自动提交表单
        setTimeout(() => {
          document.forms[0].submit()
        }, 100)
      }
    })
  } catch (error) {
    console.error('获取支付表单失败:', error)
  }
}

onMounted(() => {
  fetchAlipayForm()
})
</script>

<style scoped>
.alipay-form-container {
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f7fa;
}

.alipay-form {
  width: 100%;
  max-width: 600px;
}
</style> 