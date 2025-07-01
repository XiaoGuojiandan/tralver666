<template>
  <div class="food-management">
    <div class="page-header">
      <h1 class="page-title">美食管理</h1>
      <p class="page-subtitle">Food Management</p>
    </div>

    <!-- 搜索区域 -->
    <div class="filter-container">
      <el-card shadow="never" class="search-card">
        <el-form :inline="true" :model="searchForm" class="search-form">
          <el-form-item label="名称">
            <el-input v-model="searchForm.name" placeholder="美食名称" clearable @keyup.enter="handleSearch" />
          </el-form-item>
          <el-form-item label="分类">
            <el-select v-model="searchForm.categoryId" placeholder="选择分类" clearable>
              <el-option
                v-for="item in categories"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="城市">
            <el-input v-model="searchForm.city" placeholder="所在城市" clearable />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch" :icon="Search">搜索</el-button>
            <el-button @click="resetSearch" :icon="Refresh">重置</el-button>
            <el-button type="success" @click="handleCreate" :icon="Plus">新增美食</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>

    <!-- 表格区域 -->
    <el-card shadow="never" class="table-card">
      <el-table 
        :data="tableData" 
        v-loading="loading" 
        border 
        stripe
        style="width: 100%"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="名称" min-width="100" show-overflow-tooltip />
        <el-table-column label="图片" width="120">
          <template #default="scope">
            <el-image 
              v-if="scope.row.imageUrl" 
              :src="scope.row.imageUrl.startsWith('/') ? `/api${scope.row.imageUrl}` : scope.row.imageUrl"
              fit="cover"
              style="width: 80px; height: 60px; border-radius: 4px;"
              :preview-src-list="[scope.row.imageUrl.startsWith('/') ? `/api${scope.row.imageUrl}` : scope.row.imageUrl]"
            >
              <template #error>
                <div class="image-error">
                  <el-icon><Picture /></el-icon>
                </div>
              </template>
            </el-image>
            <div v-else class="no-image">
              <el-icon><Picture /></el-icon>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="categoryInfo.name" label="分类" width="120" />
        <el-table-column prop="priceRange" label="价格区间" width="120" />
        <el-table-column prop="city" label="城市" width="120" />
        <el-table-column prop="location" label="地址" min-width="150" show-overflow-tooltip />
        <el-table-column prop="businessHours" label="营业时间" width="150" show-overflow-tooltip />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <div class="action-buttons">
              <el-button 
                size="small" 
                type="primary" 
                :icon="Edit"
                plain
                @click="handleEdit(scope.row)"
              >
                编辑
              </el-button>
              <el-button 
                size="small" 
                type="danger" 
                :icon="Delete"
                plain
                @click="handleDelete(scope.row)"
              >
                删除
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 编辑/新增对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑美食' : '新增美食'"
      width="60%"
      :close-on-click-modal="false"
    >
      <el-form
        ref="foodFormRef"
        :model="foodForm"
        :rules="rules"
        label-width="100px"
        label-position="right"
      >
        <el-form-item label="名称" prop="name">
          <el-input v-model="foodForm.name" placeholder="请输入美食名称" />
        </el-form-item>
        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="foodForm.categoryId" placeholder="请选择分类">
            <el-option
              v-for="item in categories"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="图片" prop="imageUrl">
          <el-upload
            class="food-image-upload"
            action="#"
            :auto-upload="true"
            :show-file-list="false"
            :http-request="customUploadImage"
            :before-upload="beforeImageUpload"
          >
            <img v-if="foodForm.imageUrl" :src="foodForm.imageUrl.startsWith('/') ? `/api${foodForm.imageUrl}` : foodForm.imageUrl" class="preview-image">
            <el-icon v-else class="upload-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="价格区间" prop="priceRange">
          <el-input v-model="foodForm.priceRange" placeholder="例如：¥50-100/人" />
        </el-form-item>
        <el-form-item label="城市" prop="city">
          <el-input v-model="foodForm.city" placeholder="请输入所在城市" />
        </el-form-item>
        <el-form-item label="地址" prop="location" >
          <el-input v-model="foodForm.location" placeholder="请输入详细地址" />
        </el-form-item>
        <el-form-item label="营业时间" prop="businessHours">
          <el-input v-model="foodForm.businessHours" placeholder="例如：10:00-22:00" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="foodForm.description"
            type="textarea"
            :rows="4"
            placeholder="请输入美食描述"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm" :loading="submitLoading">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { Search, Refresh, Plus, Edit, Delete, View, Picture } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAllFoods, createFood, updateFood, deleteFood } from '@/api/food'
import { getCategoryList } from '@/api/food'
import request from '@/utils/request'

// 搜索表单
const searchForm = reactive({
  name: '',
  categoryId: '',
  city: ''
})

// 表格数据
const tableData = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const categories = ref([])

// 表单数据
const dialogVisible = ref(false)
const isEdit = ref(false)
const submitLoading = ref(false)
const foodFormRef = ref(null)
const foodForm = reactive({
  id: undefined,
  name: '',
  categoryId: '',
  imageUrl: '',
  priceRange: '',
  city: '',
  location: '',
  businessHours: '',
  description: ''
})

// 表单验证规则
const rules = {
  name: [{ required: true, message: '请输入美食名称', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
  city: [{ required: true, message: '请输入所在城市', trigger: 'blur' }],
  location: [{ required: true, message: '请输入详细地址', trigger: 'blur' }]
}

// 获取分类列表
const fetchCategories = async () => {
  try {
    const res = await getCategoryList()
    if (res.code === '200') {
      categories.value = res.data
    }
  } catch (error) {
    console.error('获取分类列表失败:', error)
  }
}

// 获取美食列表
const fetchFoodList = async () => {
  try {
    loading.value = true
    const params = {
      pageNum: currentPage.value,
      pageSize: pageSize.value,
      name: searchForm.name,
      categoryId: searchForm.categoryId,
      city: searchForm.city
    }
    const res = await getAllFoods(params)
    if (res.code === '200') {
      tableData.value = res.data.records
      total.value = res.data.total
    }
  } catch (error) {
    console.error('获取美食列表失败:', error)
    ElMessage.error('获取美食列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  fetchFoodList()
}

// 重置搜索
const resetSearch = () => {
  searchForm.name = ''
  searchForm.categoryId = null
  searchForm.city = ''
  currentPage.value = 1
  fetchFoodList()
}

// 新增
const handleCreate = () => {
  isEdit.value = false
  Object.keys(foodForm).forEach(key => {
    foodForm[key] = ''
  })
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  isEdit.value = true
  Object.keys(foodForm).forEach(key => {
    foodForm[key] = row[key]
  })
  foodForm.id = row.id
  dialogVisible.value = true
}


// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm(
    '确定要删除这条美食信息吗？',
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      const res = await deleteFood(row.id)
      if (res.code === '200') {
        ElMessage.success('删除成功')
        fetchFoodList()
      }
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  })
}

// 提交表单
const submitForm = async () => {
  if (!foodFormRef.value) return
  
  await foodFormRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        const submitFunc = isEdit.value ? updateFood : createFood
        const res = await submitFunc(foodForm.id, foodForm)
        if (res.code === '200') {
          ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
          dialogVisible.value = false
          fetchFoodList()
        }
      } catch (error) {
        console.error(isEdit.value ? '更新失败:' : '创建失败:', error)
        ElMessage.error(isEdit.value ? '更新失败' : '创建失败')
      } finally {
        submitLoading.value = false
      }
    }
  })
}

// 自定义上传方法
const customUploadImage = async (options) => {
  try {
    const { file } = options
    const formData = new FormData()
    formData.append('file', file)
    
    const res = await request.post('/file/upload/img', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
        token: localStorage.getItem('token') || ''
      },
      transformRequest: [(data) => data],
      successMsg: '图片上传成功',
      errorMsg: '图片上传失败'
    })

    if (res.code === '200') {
      foodForm.imageUrl = res.data
      options.onSuccess(res)
    } else {
      options.onError(new Error(res.message || '上传失败'))
    }
  } catch (error) {
    console.error('图片上传过程发生错误:', error)
    options.onError(error)
  }
}

// 图片上传相关
const handleImageSuccess = (res) => {
  if (res.code === '200') {
    foodForm.imageUrl = res.data
    ElMessage.success('图片上传成功')
  } else {
    ElMessage.error('图片上传失败')
  }
}

const handleImageError = (error) => {
  console.error('图片上传失败:', error)
  ElMessage.error('图片上传失败，请检查网络连接或联系管理员')
}

const beforeImageUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB!')
    return false
  }
  return true
}

// 分页相关
const handleSizeChange = (val) => {
  pageSize.value = val
  fetchFoodList()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchFoodList()
}

onMounted(() => {
  fetchCategories()
  fetchFoodList()
})
</script>

<style lang="scss" scoped>
.food-management {
  padding: 20px;

  .page-header {
    margin-bottom: 24px;
    text-align: left;

    .page-title {
      font-size: 24px;
      font-weight: 600;
      margin: 0;
      color: #2c3e50;
    }

    .page-subtitle {
      margin: 8px 0 0;
      color: #909399;
      font-size: 14px;
    }
  }

  .filter-container {
    margin-bottom: 24px;

    .search-card {
      .search-form {
        display: flex;
        flex-wrap: wrap;
        gap: 16px;
      }
    }
  }

  .table-card {
    .action-buttons {
      display: flex;
      gap: 8px;
      justify-content: flex-start;
      flex-wrap: nowrap;
    }

    .no-image {
      width: 80px;
      height: 60px;
      display: flex;
      align-items: center;
      justify-content: center;
      background: #f5f7fa;
      border-radius: 4px;
      color: #909399;
    }

    .image-error {
      width: 80px;
      height: 60px;
      display: flex;
      align-items: center;
      justify-content: center;
      background: #fef0f0;
      border-radius: 4px;
      color: #f56c6c;
    }
  }

  .pagination-container {
    margin-top: 20px;
    display: flex;
    justify-content: center;
  }

  .food-image-upload {
    :deep(.el-upload) {
      border: 1px dashed #d9d9d9;
      border-radius: 6px;
      cursor: pointer;
      position: relative;
      overflow: hidden;
      transition: border-color 0.3s;

      &:hover {
        border-color: #409EFF;
      }
    }

    .preview-image {
      width: 150px;
      height: 150px;
      object-fit: cover;
    }

    .upload-icon {
      font-size: 28px;
      color: #8c939d;
      width: 150px;
      height: 150px;
      line-height: 150px;
      text-align: center;
    }
  }
}
</style> 