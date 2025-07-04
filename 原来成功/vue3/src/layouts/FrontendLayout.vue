<template>
    <div class="frontend-layout">
      <!-- 顶部导航栏 -->
      <header class="header">
        <div class="nav-container">
          <div class="logo-container">
            <el-icon class="logo-icon"><Place /></el-icon>
            <h1 class="logo-text">广西智游指南</h1>
          </div>
          
          <el-menu
            mode="horizontal"
            :ellipsis="false"
            class="main-menu"
            :router="true"
          >
            <el-menu-item index="/">
              <el-icon><HomeFilled /></el-icon>
              <span>首页</span>
            </el-menu-item>
            
            <el-menu-item index="/scenic">
              <el-icon><Bicycle /></el-icon>
              <span>景点</span>
            </el-menu-item>

            <el-menu-item index="/food">
              <el-icon><Bowl /></el-icon>
              <span>美食</span>
            </el-menu-item>

            <el-menu-item index="/tickets">
              <el-icon><Ticket /></el-icon>
              <span>门票预订</span>
            </el-menu-item>

            <el-menu-item index="/accommodation">
              <el-icon><House /></el-icon>
              <span>周边住宿</span>
            </el-menu-item>

            <el-menu-item index="/guide">
            <el-icon><Reading /></el-icon>
            <span>攻略</span>
          </el-menu-item>

          </el-menu>
          
          <div class="user-actions">
            <template v-if="isLoggedIn">
              <el-dropdown trigger="click" @command="handleCommand">
                <div class="user-info">
                  <el-avatar :size="32" :src="userAvatar">{{ userInitial }}</el-avatar>
                  <span class="user-name">{{ userName || '用户' }}</span>
                  <el-icon><ArrowDown /></el-icon>
                </div>
                
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="profile" icon="User">个人中心</el-dropdown-item>
                    <el-dropdown-item command="guide" icon="Notebook">我的攻略</el-dropdown-item>
                    <el-dropdown-item command="collection" icon="Star">我的收藏</el-dropdown-item>
                    <el-dropdown-item command="orders" icon="Ticket">我的订单</el-dropdown-item>
                    <el-dropdown-item divided command="logout" icon="SwitchButton">退出登录</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </template>
            
            <template v-else>
              <el-button type="primary" @click="goToLogin" class="login-btn">
                <el-icon><Key /></el-icon>
                <span>登录 / 注册</span>
              </el-button>
            </template>
          </div>
        </div>
      </header>
  
      <!-- 主要内容区域 -->
      <el-main class="main-content">
        <router-view />
      </el-main>
  
      <!-- 页脚 -->
      <footer class="footer">
        <div class="footer-content">
          <p class="copyright">&copy; 2025 广西智游指南 | 版权所有</p>
        </div>
      </footer>
    </div>
  </template>
  
  <script setup>
  import { computed } from 'vue'
  import { useUserStore } from '@/store/user'
  import { useRouter } from 'vue-router'
  import { 
    HomeFilled, 
    User, 
    Bicycle, 
    Reading, 
    Notebook, 
    Star, 
    SwitchButton, 
    Key, 
    Ticket,
    House,
    Place,
    ArrowDown,
    Bowl
  } from '@element-plus/icons-vue'
  
  const userStore = useUserStore()
  const router = useRouter()
  
  const isLoggedIn = computed(() => !!userStore.token)
  const userName = computed(() => userStore.userInfo?.name || userStore.userInfo?.username || '')
  const userInitial = computed(() => {
    const name = userName.value
    return name ? name.charAt(0).toUpperCase() : '游'
  })
  
  const userAvatar = computed(() => {
    const baseAPI = process.env.VUE_APP_BASE_API || '/api'
    return userStore.userInfo?.avatar ? baseAPI + userStore.userInfo.avatar : ''
  })
  
  const handleCommand = (command) => {
    switch (command) {
      case 'profile':
        router.push('/profile')
        break
      case 'guide':
        router.push('/my-guide')
        break
      case 'collection':
        router.push('/collection')
        break
      case 'orders':
        router.push('/orders')
        break
      case 'logout':
        handleLogout()
        break
    }
  }
  
  const goToLogin = () => {
    router.push('/login')
  }
  
  const handleLogout = () => {
    userStore.clearUserInfo()
    router.push('/login')
  }
  </script>
  
  <style lang="scss" scoped>
  .frontend-layout {
    min-height: 100vh;
    display: flex;
    flex-direction: column;
  }
  
  .header {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    z-index: 1000;
    background: rgba(255, 255, 255, 0.95);
    backdrop-filter: blur(10px);
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
    transition: all 0.3s ease;
  }
  
  .nav-container {
    max-width: 1400px;
    margin: 0 auto;
    display: flex;
    align-items: center;
    padding: 0 20px;
    height: 64px;
  }
  
  .logo-container {
    display: flex;
    align-items: center;
    margin-right: 30px;
    
    .logo-icon {
      font-size: 24px;
      color: #3490dc;
      margin-right: 10px;
    }
    
    .logo-text {
      font-size: 20px;
      font-weight: 600;
      color: #3490dc;
      margin: 0;
    }
  }
  
  .main-menu {
    flex: 1;
    border-bottom: none;
    background-color: transparent;
    
    :deep(.el-menu-item) {
      color: #606266;
      font-size: 16px;
      
      &:hover, &.is-active {
        color: #3490dc;
        background-color: transparent;
      }
      
      .el-icon {
        color: #606266;
        margin-right: 5px;
      }
      
      &.is-active .el-icon,
      &:hover .el-icon {
        color: #3490dc;
      }
    }
  }
  
  .user-actions {
    margin-left: auto;
    display: flex;
    align-items: center;
    
    .user-info {
      display: flex;
      align-items: center;
      cursor: pointer;
      padding: 5px 10px;
      border-radius: 20px;
      transition: background-color 0.3s;
      
      &:hover {
        background-color: #f5f7fa;
      }
      
      .user-name {
        margin: 0 8px;
        color: #606266;
        font-size: 14px;
      }
      
      .el-icon {
        font-size: 12px;
        color: #909399;
      }
    }
    
    .login-btn {
      border-radius: 4px;
      background-color: #3490dc;
      border: none;
      color: #fff;
      padding: 8px 16px;
      
      &:hover {
        background-color: #2779bd;
      }
      
      .el-icon {
        margin-right: 5px;
      }
    }
  }
  
  .main-content {
    flex: 1;
    margin-top: 64px; // 导航栏的高度
    min-height: calc(100vh - 64px);
  }
  
  .footer {
    background: #f8f9fa;
    padding: 20px 0;
    margin-top: auto;
  }
  
  .footer-content {
    max-width: 1400px;
    margin: 0 auto;
    text-align: center;
  }
  
  .copyright {
    margin: 0;
    font-size: 14px;
    color: #909399;
  }
  
  // 响应式调整
  @media (max-width: 768px) {
    .main-content {
      margin-top: 56px; // 移动端导航栏高度稍小
      min-height: calc(100vh - 56px);
    }
  }
  </style>