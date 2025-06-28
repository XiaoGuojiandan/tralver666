<!-- eslint-disable -->
<template>
  <div class="city-sidebar-container">
    <div class="sidebar-header">
      <h3 class="sidebar-title">
        <el-icon><Location /></el-icon>
        城市导航
      </h3>
    </div>
    <div class="city-list">
      <div
        v-for="city in cities"
        :key="city.code"
        class="city-item"
        :class="{ active: selectedCity === city.name }"
        @click="selectCity(city)"
      >
        <span class="city-name">{{ city.name }}</span>
        <span class="city-count" v-if="city.count">({{ city.count }})</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Location } from '@element-plus/icons-vue'

const emit = defineEmits(['citySelect'])
const selectedCity = ref('')

const cities = [
  { name: '南宁', code: '450100' },
  { name: '柳州', code: '450200' },
  { name: '桂林', code: '450300' },
  { name: '梧州', code: '450400' },
  { name: '北海', code: '450500' },
  { name: '防城港', code: '450600' },
  { name: '钦州', code: '450700' },
  { name: '贵港', code: '450800' },
  { name: '玉林', code: '450900' },
  { name: '百色', code: '451000' },
  { name: '贺州', code: '451100' },
  { name: '河池', code: '451200' },
  { name: '来宾', code: '451300' },
  { name: '崇左', code: '451400' }
]

const selectCity = (city) => {
  if (selectedCity.value === city.name) {
    selectedCity.value = ''
    emit('citySelect', { name: null })
  } else {
    selectedCity.value = city.name
    emit('citySelect', city)
  }
}
</script>

<style lang="scss" scoped>
.city-sidebar-container {
  width: 200px;
  background: white;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  border: 1px solid #e2e8f0;
}

.sidebar-header {
  margin-bottom: 20px;
}

.sidebar-title {
  font-size: 18px;
  font-weight: 600;
  color: #2d3748;
  margin: 0;
  display: flex;
  align-items: center;
  gap: 8px;

  .el-icon {
    color: #667eea;
  }
}

.city-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.city-item {
  padding: 10px 16px;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  color: #64748b;

  &:hover {
    background: #f8fafc;
    color: #667eea;
  }

  &.active {
    background: linear-gradient(45deg, #667eea, #764ba2);
    color: white;

    .city-count {
      color: rgba(255, 255, 255, 0.8);
    }
  }

  .city-count {
    font-size: 12px;
    color: #94a3b8;
  }
}
</style> 