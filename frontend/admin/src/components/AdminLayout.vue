<script setup lang="ts">
import { computed } from 'vue'
import { RouterView, useRoute } from 'vue-router'
import { Document, FolderOpened, PriceTag } from '@element-plus/icons-vue'

const route = useRoute()
const activeMenu = computed(() => route.path)
</script>

<template>
  <div class="admin-layout">
    <header class="admin-header">
      <div class="brand">MyBlog 管理后台</div>
      <div class="admin-user">管理员</div>
    </header>

    <div class="admin-body">
      <aside class="admin-aside">
        <el-menu :default-active="activeMenu" router class="side-menu">
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
  font-size: 14px;
  color: var(--el-text-color-regular);
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
