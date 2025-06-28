<!-- eslint-disable -->
<template>
  <div class="city-sidebar-container">
    <div class="sidebar-header">
      <h3 class="sidebar-title">
        <el-icon><Location /></el-icon>
        城市导航
      </h3>
      <p class="sidebar-subtitle">探索广西各地精彩景点</p>
    </div>
    
    <div class="search-box">
      <el-input
        v-model="searchText"
        placeholder="搜索城市..."
        clearable
        prefix-icon="Search"
        @input="filterCities"
      />
    </div>

    <div class="city-list" :class="{ 'has-search': searchText }">
      <div
        v-for="city in filteredCities"
        :key="city.code"
        class="city-item"
        :class="{ active: selectedCity === city.name }"
        @click="selectCity(city)"
      >
        <div class="city-info">
          <span class="city-icon">{{ getCityEmoji(city.name) }}</span>
          <span class="city-name">{{ city.name }}</span>
        </div>
        <div class="city-meta">
          <span class="city-count" v-if="city.count">({{ city.count }})</span>
          <el-icon v-if="selectedCity === city.name"><Check /></el-icon>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { Location, Search, Check } from '@element-plus/icons-vue'

const emit = defineEmits(['citySelect'])
const selectedCity = ref('')
const searchText = ref('')

const cities = [
  { name: '南宁', code: '450100', emoji: '🌺' },
  { name: '柳州', code: '450200', emoji: '🌉' },
  { name: '桂林', code: '450300', emoji: '🗻' },
  { name: '梧州', code: '450400', emoji: '🍃' },
  { name: '北海', code: '450500', emoji: '🌊' },
  { name: '防城港', code: '450600', emoji: '🚢' },
  { name: '钦州', code: '450700', emoji: '🌴' },
  { name: '贵港', code: '450800', emoji: '🌿' },
  { name: '玉林', code: '450900', emoji: '🌳' },
  { name: '百色', code: '451000', emoji: '⛰️' },
  { name: '贺州', code: '451100', emoji: '🍵' },
  { name: '河池', code: '451200', emoji: '💧' },
  { name: '来宾', code: '451300', emoji: '🌸' },
  { name: '崇左', code: '451400', emoji: '🌺' }
]

const filteredCities = computed(() => {
  if (!searchText.value) return cities
  return cities.filter(city => 
    city.name.toLowerCase().includes(searchText.value.toLowerCase())
  )
})

const selectCity = (city) => {
  if (selectedCity.value === city.name) {
    selectedCity.value = ''
    emit('citySelect', { name: null })
  } else {
    selectedCity.value = city.name
    emit('citySelect', city)
  }
}

const getCityEmoji = (cityName) => {
  const city = cities.find(c => c.name === cityName)
  return city ? city.emoji : '🏙️'
}
</script>

<style lang="scss" scoped>
.city-sidebar-container {
  width: 280px;
  background: white;
  border-radius: 20px;
  padding: 24px;
  box-shadow: 0 4px 30px rgba(0, 0, 0, 0.05);
  border: 1px solid rgba(231, 235, 238, 0.8);
  position: sticky;
  top: 20px;
}

.sidebar-header {
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid rgba(231, 235, 238, 0.6);
}

.sidebar-title {
  font-size: 20px;
  font-weight: 600;
  color: #1a1a1a;
  margin: 0 0 8px;
  display: flex;
  align-items: center;
  gap: 8px;

  .el-icon {
    color: #3b82f6;
    font-size: 22px;
  }
}

.sidebar-subtitle {
  font-size: 14px;
  color: #64748b;
  margin: 0;
}

.search-box {
  margin-bottom: 20px;

  :deep(.el-input__wrapper) {
    border-radius: 12px;
    padding: 8px 12px;
    background: #f8fafc;
    box-shadow: none;
    border: 1px solid #e2e8f0;

    &:hover, &.is-focus {
      border-color: #3b82f6;
      box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.1);
    }
  }

  :deep(.el-input__inner) {
    font-size: 14px;
    color: #1a1a1a;
    
    &::placeholder {
      color: #94a3b8;
    }
  }
}

.city-list {
  margin-top: 20px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.city-item {
  padding: 12px 16px;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 15px;
  color: #64748b;

  &:hover {
    background: #f8fafc;
    transform: translateX(4px);
  }

  &.active {
    background: #eff6ff;
    color: #3b82f6;
    font-weight: 500;
  }

  .city-icon {
    font-size: 18px;
  }

  .city-name {
    flex: 1;
  }

  .city-count {
    font-size: 13px;
    color: #94a3b8;
  }
}

.city-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.city-meta {
  display: flex;
  align-items: center;
  gap: 8px;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateX(-10px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

.city-item {
  animation: slideIn 0.3s ease-out;
  animation-fill-mode: both;
}

@for $i from 1 through 14 {
  .city-item:nth-child(#{$i}) {
    animation-delay: #{$i * 0.05}s;
  }
}
</style> 