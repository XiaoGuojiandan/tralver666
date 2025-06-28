<template>
  <div class="region-sidebar">
    <div class="region-title">广西地区</div>
    <el-menu
      :default-active="activeRegion"
      class="region-menu"
      @select="handleSelect"
    >
      <el-menu-item index="all">
        <span>全部地区</span>
      </el-menu-item>
      <el-menu-item v-for="region in regions" :key="region.id" :index="region.id.toString()">
        <span>{{ region.name }}</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script setup>
import { ref, defineEmits } from 'vue'

const emit = defineEmits(['regionChange'])

const activeRegion = ref('all')

const regions = ref([
  { id: 1, name: '南宁市' },
  { id: 2, name: '桂林市' },
  { id: 3, name: '柳州市' },
  { id: 4, name: '梧州市' },
  { id: 5, name: '北海市' },
  { id: 6, name: '防城港市' },
  { id: 7, name: '钦州市' },
  { id: 8, name: '贵港市' },
  { id: 9, name: '玉林市' },
  { id: 10, name: '百色市' },
  { id: 11, name: '贺州市' },
  { id: 12, name: '河池市' },
  { id: 13, name: '来宾市' },
  { id: 14, name: '崇左市' }
])

const handleSelect = (index) => {
  activeRegion.value = index
  emit('regionChange', index)
}
</script>

<style scoped>
.region-sidebar {
  width: 200px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 16px 0;
}

.region-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  padding: 0 20px;
  margin-bottom: 16px;
}

.region-menu {
  border-right: none;
}

.el-menu-item {
  height: 40px;
  line-height: 40px;
  padding: 0 20px !important;
}

.el-menu-item:hover {
  background-color: #f5f7fa;
}

.el-menu-item.is-active {
  background-color: #ecf5ff;
  color: #409EFF;
}
</style> 