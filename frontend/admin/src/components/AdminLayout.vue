<script setup lang="ts">
import { computed } from 'vue'
import { RouterView, useRoute, useRouter } from 'vue-router'
import {
  ArrowDown,
  ChatDotRound,
  Document,
  FolderOpened,
  Odometer,
  PriceTag,
  SwitchButton,
} from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const activeMenu = computed(() => {
  const menu = route.meta.activeMenu
  return typeof menu === 'string' ? menu : route.path
})

const displayName = computed(
  () => userStore.userInfo?.nickname || userStore.userInfo?.username || '管理员',
)
const avatarText = computed(() => displayName.value.charAt(0).toUpperCase())

/** 顶部用户菜单命令 */
async function handleCommand(command: string) {
  if (command !== 'logout') {
    return
  }
  try {
    await userStore.logout()
  } catch {
    // 退出接口失败时仍清理本地状态并返回登录页
  }
  await router.replace('/login')
}
</script>

<template>
  <div class="admin-layout">
    <header class="admin-header">
      <div class="brand">MyBlog 管理后台</div>
      <el-dropdown trigger="click" @command="handleCommand">
        <div class="admin-user">
          <el-avatar :size="32" :src="userStore.userInfo?.avatar ?? undefined">
            {{ avatarText }}
          </el-avatar>
          <span class="admin-user-name">{{ displayName }}</span>
          <el-icon class="admin-user-arrow"><ArrowDown /></el-icon>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="logout">
              <el-icon><SwitchButton /></el-icon>
              退出登录
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </header>

    <div class="admin-body">
      <aside class="admin-aside">
        <el-menu :default-active="activeMenu" router class="side-menu">
          <el-menu-item index="/dashboard">
            <el-icon><Odometer /></el-icon>
            <span>数据总览</span>
          </el-menu-item>
          <el-menu-item index="/article">
            <el-icon><Document /></el-icon>
            <span>文章管理</span>
          </el-menu-item>
          <el-menu-item index="/category">
            <el-icon><FolderOpened /></el-icon>
            <span>分类管理</span>
          </el-menu-item>
          <el-menu-item index="/tag">
            <el-icon><PriceTag /></el-icon>
            <span>标签管理</span>
          </el-menu-item>
          <el-menu-item index="/comment">
            <el-icon><ChatDotRound /></el-icon>
            <span>评论管理</span>
          </el-menu-item>
        </el-menu>
      </aside>

      <div class="admin-main">
        <main class="admin-content">
          <RouterView />
        </main>
        <footer class="admin-footer">© 2026 MyBlog. All rights reserved.</footer>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.admin-layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.admin-header {
  position: sticky;
  top: 0;
  z-index: 10;
  display: flex;
  flex: none;
  align-items: center;
  justify-content: space-between;
  height: 60px;
  padding: 0 24px;
  background-color: var(--el-bg-color);
  border-bottom: 1px solid var(--el-border-color-light);
  box-shadow: 0 1px 4px rgb(0 0 0 / 4%);
}

.brand {
  font-size: 18px;
  font-weight: 600;
  color: var(--el-color-primary);
}

.admin-user {
  display: flex;
  align-items: center;
  gap: 8px;
  color: var(--el-text-color-regular);
  cursor: pointer;

  &:hover .admin-user-name {
    color: var(--el-color-primary);
  }
}

.admin-user-name {
  font-size: 14px;
}

.admin-user-arrow {
  font-size: 12px;
}

.admin-body {
  display: flex;
  flex: 1;
  min-height: 0;
}

.admin-aside {
  flex: none;
  width: 200px;
  background-color: var(--el-bg-color);
  border-right: 1px solid var(--el-border-color-light);
}

.side-menu {
  padding-top: 8px;
}

.admin-main {
  display: flex;
  flex: 1;
  flex-direction: column;
  min-width: 0;
}

.admin-content {
  flex: 1;
  padding: 16px;
}

.admin-footer {
  flex: none;
  padding: 16px;
  font-size: 13px;
  color: var(--el-text-color-secondary);
  text-align: center;
}
</style>
