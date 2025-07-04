<template>
  <div class="sidebar-container" :class="{ 'is-collapsed': isCollapsed }">
    <div class="logo">
      <span class="logo-icon">🌍</span>
      <span class="logo-text" v-show="!isCollapsed">广西智游指南</span>
    </div>
    <div class="menu-wrapper">
      <el-menu :default-active="activeMenu" :collapse="isCollapsed" :collapse-transition="false" mode="vertical" class="sidebar-menu"
        text-color="#bfcbd9" active-text-color="#409EFF" router>
        
        <!-- 固定菜单项 -->
        <el-menu-item index="/back/dashboard">
          <el-icon><HomeFilled /></el-icon>
          <template #title>首页</template>
        </el-menu-item>

        <el-menu-item index="/back/profile">
          <el-icon><UserFilled /></el-icon>
          <template #title>个人信息</template>
        </el-menu-item>

        <el-menu-item index="/back/user" v-if="userStore.isAdmin">
          <el-icon><User /></el-icon>
          <template #title>用户管理</template>
        </el-menu-item>

        <el-menu-item index="/back/ticket" v-if="userStore.isAdmin">
          <el-icon><Ticket /></el-icon>
          <template #title>门票管理</template>
        </el-menu-item>

        <el-menu-item index="/back/scenic" v-if="userStore.isAdmin">
          <el-icon><Location /></el-icon>
          <template #title>景点管理</template>
        </el-menu-item>
        
        <el-menu-item index="/back/category">
          <el-icon><Menu /></el-icon>
          <template #title>分类管理</template>
        </el-menu-item>

        <el-menu-item index="/back/food" v-if="userStore.isAdmin">
          <el-icon><Bowl /></el-icon>
          <template #title>美食管理</template>
        </el-menu-item>

        <el-menu-item index="/back/accommodation">
          <el-icon><House /></el-icon>
          <span>住宿管理</span>
        </el-menu-item>

        <el-menu-item index="/back/order" v-if="userStore.isAdmin">
          <el-icon><List /></el-icon>
          <template #title>订单管理</template>
        </el-menu-item>

        <el-menu-item index="/back/collection" v-if="userStore.isAdmin">
          <el-icon><Star /></el-icon>
          <template #title>收藏管理</template>
        </el-menu-item>

        <el-menu-item index="/back/comment" v-if="userStore.isAdmin">
          <el-icon><ChatDotRound /></el-icon>
          <template #title>评论管理</template>
        </el-menu-item>
        
        <el-menu-item index="/back/guide" v-if="userStore.isAdmin">
          <el-icon><Document /></el-icon>
          <template #title>攻略管理</template>
        </el-menu-item>

        <el-menu-item index="/back/carousel" v-if="userStore.isAdmin">
          <el-icon><Picture /></el-icon>
          <span>轮播图管理</span>
        </el-menu-item>
        
      </el-menu>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useAppStore } from '@/store/app'
import { useUserStore } from '@/store/user'
import { 
  HomeFilled, 
  User, 
  UserFilled,
  Location,
  Menu,
  ChatDotRound,
  Document,
  Star,
  Ticket,
  List,
  House,
  Picture,
  Bowl
} from '@element-plus/icons-vue'

const route = useRoute()
const appStore = useAppStore()
const userStore = useUserStore()

const isCollapsed = computed(() => appStore.sidebarCollapsed)

// 当前激活的菜单
const activeMenu = computed(() => {
  const { meta, path } = route
  if (meta.activeMenu) {
    return meta.activeMenu
  }
  return path
})
</script>

<style lang="scss" scoped>
.sidebar-container {
  height: 100%; 
  min-height: 100vh;
  background: linear-gradient(180deg, #1c1c1c 0%, #2d2d2d 100%);
  display: flex;
  flex-direction: column;
  width: 220px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  
  &.is-collapsed {
    width: 64px;
    
    .logo {
      padding: 0;
      justify-content: center;
      
      .logo-icon {
        margin: 0;
      }
    }

    :deep(.el-menu) {
      .el-sub-menu__title span,
      .el-menu-item span {
        opacity: 0;
        transition: opacity 0.2s;
      }
    }
  }
  
  .logo {
    height: 60px;
    flex-shrink: 0;
    line-height: 60px;
    text-align: center;
    background: rgba(255, 255, 255, 0.05);
    backdrop-filter: blur(10px);
    border-bottom: 1px solid rgba(255, 255, 255, 0.05);
    display: flex;
    align-items: center;
    padding: 0 16px;
    overflow: hidden;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    
    .logo-icon {
      font-size: 24px;
      margin-right: 8px;
      transition: margin 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    }
    
    .logo-text {
      color: #ffffff;
      font-size: 18px;
      font-weight: 600;
      white-space: nowrap;
      opacity: 1;
      transition: opacity 0.2s;
    }
  }

  .menu-wrapper {
    flex: 1;
    overflow-y: auto;
    overflow-x: hidden;

    &::-webkit-scrollbar {
      width: 6px;
    }

    &::-webkit-scrollbar-thumb {
      background: rgba(255, 255, 255, 0.2);
      border-radius: 3px;
    }

    &::-webkit-scrollbar-track {
      background: transparent;
    }
  }

  :deep(.sidebar-menu) {
    border: none;
    background: transparent;
    transition: width 0.3s cubic-bezier(0.4, 0, 0.2, 1);

    .el-menu-item, .el-sub-menu__title {
      height: 50px;
      line-height: 50px;
      color: rgba(255, 255, 255, 0.7);
      background: transparent;
      transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
      
      span {
        opacity: 1;
        transition: opacity 0.3s;
      }
      
      &:hover {
        background: rgba(255, 255, 255, 0.05) !important;
        color: #ffffff;
      }
    }

    .el-menu-item.is-active {
      background: #000000 !important;
      color: #ffffff !important;
      
      &::before {
        content: '';
        position: absolute;
        left: 0;
        top: 0;
        width: 3px;
        height: 100%;
        background: #1890ff;
      }
    }

    .el-sub-menu {
      &.is-opened {
        > .el-sub-menu__title {
          color: #ffffff;
          background: rgba(0, 0, 0, 0.2) !important;
        }
      }

      .el-menu {
        background: rgba(0, 0, 0, 0.3);
        
        .el-menu-item {
          background: transparent;
          
          &:hover {
            background: rgba(255, 255, 255, 0.05) !important;
          }
          
          &.is-active {
            background: #000000 !important;
          }
        }
      }
    }

    // 折叠状态下的弹出菜单样式
    &.el-menu--collapse {
      .el-sub-menu {
        &.is-opened {
          > .el-sub-menu__title {
            background: transparent !important;
          }
        }
      }
    }
  }

  .el-icon {
    vertical-align: middle;
    margin-right: 5px;
    width: 24px;
    text-align: center;
    color: inherit;
  }

  span {
    vertical-align: middle;
  }
}
</style> 