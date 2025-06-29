.nav-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 20px;
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo-container {
  display: flex;
  align-items: center;
  text-decoration: none;
  color: #333;
  font-size: 20px;
  font-weight: bold;

  img {
    height: 40px;
    margin-right: 10px;
  }
}

.main-menu {
  display: flex;
  align-items: center;
  gap: 20px;

  .menu-item {
    text-decoration: none;
    color: #666;
    font-size: 15px;
    padding: 8px 12px;
    border-radius: 6px;
    transition: all 0.3s ease;

    &:hover, &.active {
      color: #42b983;
      background: rgba(66, 185, 131, 0.1);
    }
  }
}

.user-actions {
  display: flex;
  align-items: center;
  gap: 15px;

  .action-btn {
    padding: 8px 16px;
    border-radius: 6px;
    font-size: 14px;
    transition: all 0.3s ease;
    
    &.login {
      color: #42b983;
      background: rgba(66, 185, 131, 0.1);
      
      &:hover {
        background: rgba(66, 185, 131, 0.2);
      }
    }
    
    &.register {
      color: white;
      background: #42b983;
      
      &:hover {
        background: #3aa876;
      }
    }
  }

  .user-avatar {
    width: 36px;
    height: 36px;
    border-radius: 50%;
    cursor: pointer;
    transition: transform 0.3s ease;

    &:hover {
      transform: scale(1.05);
    }
  }
}

// 响应式样式
@media (max-width: 768px) {
  .nav-container {
    height: 56px;
    padding: 0 15px;
  }

  .logo-container {
    font-size: 18px;
    
    img {
      height: 32px;
    }
  }

  .main-menu {
    display: none; // 在移动端可以改为汉堡菜单
  }

  .user-actions {
    gap: 10px;

    .action-btn {
      padding: 6px 12px;
      font-size: 13px;
    }

    .user-avatar {
      width: 32px;
      height: 32px;
    }
  }
} 