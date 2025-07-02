<!-- 美食详情页面 -->
<template>
  <div class="food-detail">
    <el-card v-loading="loading" class="main-card">
      <div class="food-header">
        <div class="food-title">
          <h1>{{ food.name }}</h1>
          <el-tag type="success" effect="light" round>{{ food.categoryInfo?.name }}</el-tag>
        </div>
        <div class="food-meta">
          <el-rate v-model="food.rating" disabled show-score text-color="#ff9900" />
          <span class="review-count">{{ food.reviewCount }}条评价</span>
        </div>
      </div>

      <div class="food-content">
        <div class="food-image-container">
          <el-image 
            :src="food.imageUrl?.startsWith('/') ? `/api${food.imageUrl}` : food.imageUrl" 
            fit="cover"
            :preview-src-list="[food.imageUrl?.startsWith('/') ? `/api${food.imageUrl}` : food.imageUrl]"
            class="main-image"
          >
            <template #error>
              <img src="/src/assets/images/no-image.png" alt="默认图片" class="fallback-image" />
            </template>
          </el-image>
        </div>

        <div class="food-info">
          <div class="info-card">
            <div class="price-tag">
              <span class="label">价格区间</span>
              <el-tag type="danger" effect="light" round size="large">{{ food.priceRange }}</el-tag>
            </div>
            
            <div class="info-item">
              <el-icon><Timer /></el-icon>
              <span class="label">营业时间</span>
              <span class="value">{{ food.businessHours }}</span>
            </div>

            <div class="info-item">
              <el-icon><Location /></el-icon>
              <span class="label">地址</span>
              <span class="value">{{ food.location }}</span>
            </div>

            <div class="info-item">
              <el-icon><Location /></el-icon>
              <span class="label">所在城市</span>
              <span class="value">{{ food.city }}</span>
            </div>
          </div>

          <div class="food-description">
            <h3>美食介绍</h3>
            <p>{{ food.description }}</p>
          </div>
        </div>
      </div>

      <!-- 评论区域 -->
      <div class="food-reviews" v-if="food.id">
        <h3>用户评价</h3>
        <CommentList 
          :target-id="food.id" 
          target-type="food"
          @refresh="fetchFoodDetail"
        />
      </div>
    </el-card>

    <!-- 相关推荐 -->
    <el-card class="similar-foods" v-if="similarFoods.length > 0">
      <template #header>
        <div class="card-header">
          <h3>相关推荐</h3>
        </div>
      </template>
      <el-row :gutter="24">
        <el-col :span="8" v-for="item in similarFoods" :key="item.id">
          <div class="food-card" @click="goToDetail(item.id)">
            <el-image 
              :src="item.imageUrl?.startsWith('/') ? `/api${item.imageUrl}` : item.imageUrl" 
              fit="cover"
              class="card-image"
            >
              <template #error>
                <img src="/src/assets/images/no-image.png" alt="默认图片" />
              </template>
            </el-image>
            <div class="card-content">
              <h4>{{ item.name }}</h4>
              <div class="card-footer">
                <el-rate v-model="item.rating" disabled size="small" />
                <el-tag type="danger" effect="light" round>{{ item.priceRange }}</el-tag>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Location, Timer } from '@element-plus/icons-vue'
import { getFoodDetail, getFoodByCategory } from '@/api/food'
import CommentList from '@/components/common/CommentList.vue'

const route = useRoute()
const router = useRouter()

const food = ref({})
const loading = ref(true)
const similarFoods = ref([])

// 获取美食详情
const fetchFoodDetail = async () => {
  loading.value = true
  try {
    const res = await getFoodDetail(route.params.id)
    if (res.code === '200') {
      food.value = res.data
      if (food.value.categoryId) {
        fetchSimilarFoods()
      }
    } else {
      ElMessage.error(res.msg || '获取美食详情失败')
    }
  } catch (error) {
    console.error('获取美食详情失败:', error)
    ElMessage.error(error.message || '获取美食详情失败')
  } finally {
    loading.value = false
  }
}

// 获取相关推荐
const fetchSimilarFoods = async () => {
  try {
    const res = await getFoodByCategory(food.value.categoryId, 3)
    if (res.code === '200') {
      similarFoods.value = res.data.filter(f => f.id !== parseInt(route.params.id))
    }
  } catch (error) {
    console.error('获取相关推荐失败:', error)
  }
}

// 跳转到详情页
const goToDetail = (id) => {
  router.push(`/food/detail/${id}`)
}

onMounted(() => {
  fetchFoodDetail()
})
</script>

<style lang="scss" scoped>
.food-detail {
  max-width: 1200px;
  margin: 20px auto;
  padding: 0 20px;
  
  .main-card {
    border-radius: 16px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
    background: #fff;
    
    .food-header {
      margin-bottom: 30px;
      
      .food-title {
        display: flex;
        align-items: center;
        gap: 16px;
        
        h1 {
          margin: 0;
          font-size: 32px;
          color: #2c3e50;
          font-weight: 600;
        }
      }
      
      .food-meta {
        margin-top: 16px;
        display: flex;
        align-items: center;
        gap: 16px;
        
        .review-count {
          color: #666;
          font-size: 14px;
        }
      }
    }
    
    .food-content {
      display: flex;
      gap: 40px;
      margin-bottom: 40px;
      
      .food-image-container {
        flex: 0 0 500px;
        
        .main-image {
          width: 100%;
          height: 400px;
          border-radius: 16px;
          overflow: hidden;
          box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
          transition: transform 0.3s ease;
          
          &:hover {
            transform: scale(1.02);
          }
        }
        
        .fallback-image {
          width: 100%;
          height: 100%;
          object-fit: cover;
          border-radius: 16px;
        }
      }
      
      .food-info {
        flex: 1;
        
        .info-card {
          background: #f8f9fa;
          border-radius: 16px;
          padding: 24px;
          margin-bottom: 24px;
          
          .price-tag {
            display: flex;
            align-items: center;
            gap: 12px;
            margin-bottom: 20px;
            
            .label {
              font-size: 16px;
              color: #666;
            }
          }
          
          .info-item {
            display: flex;
            align-items: center;
            gap: 12px;
            margin-bottom: 16px;
            
            .el-icon {
              color: #409EFF;
            }
            
            .label {
              color: #666;
              min-width: 80px;
            }
            
            .value {
              color: #2c3e50;
              flex: 1;
            }
          }
        }
        
        .food-description {
          h3 {
            font-size: 20px;
            color: #2c3e50;
            margin-bottom: 16px;
          }
          
          p {
            line-height: 1.8;
            color: #666;
            font-size: 16px;
          }
        }
      }
    }
  }
  
  .food-reviews {
    margin-top: 40px;
    
    h3 {
      font-size: 24px;
      color: #2c3e50;
      margin-bottom: 24px;
    }
  }
  
  .similar-foods {
    margin-top: 40px;
    border-radius: 16px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
    
    .card-header {
      h3 {
        font-size: 24px;
        color: #2c3e50;
        margin: 0;
      }
    }
    
    .food-card {
      background: #fff;
      border-radius: 12px;
      overflow: hidden;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
      cursor: pointer;
      transition: transform 0.3s ease;
      
      &:hover {
        transform: translateY(-4px);
      }
      
      .card-image {
        height: 200px;
        width: 100%;
      }
      
      .card-content {
        padding: 16px;
        
        h4 {
          margin: 0 0 12px;
          font-size: 18px;
          color: #2c3e50;
        }
        
        .card-footer {
          display: flex;
          justify-content: space-between;
          align-items: center;
        }
      }
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .food-detail {
    .food-content {
      flex-direction: column;
      
      .food-image-container {
        flex: none;
        width: 100%;
      }
    }
  }
}
</style> 