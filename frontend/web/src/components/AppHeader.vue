<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import { RouterLink, useRoute, useRouter } from 'vue-router'
import type { Component } from 'vue'
import { storeToRefs } from 'pinia'
import {
  CollectionTag,
  Files,
  HomeFilled,
  Menu as MenuIcon,
  PriceTag,
  Search,
} from '@element-plus/icons-vue'

import { useArticleStore } from '@/stores/article'
import { useSearchStore } from '@/stores/search'
import type { CategoryVO } from '@/types/category'
import CategoryPostDrawer from './CategoryPostDrawer.vue'
import TocIcon from './TocIcon.vue'

interface NavItem {
  path: string
  label: string
  icon: Component
}

const navItems: NavItem[] = [
  { path: '/', label: '首页', icon: HomeFilled },
  { path: '/category', label: '分类', icon: PriceTag },
  { path: '/tag', label: '标签', icon: CollectionTag },
  { path: '/archive', label: '归档', icon: Files },
]

const route = useRoute()
const router = useRouter()
const searchStore = useSearchStore()
const articleStore = useArticleStore()
const { keyword } = storeToRefs(searchStore)

const drawerVisible = ref(false)
const categoryDrawerVisible = ref(false)

const activeMenu = computed(() => {
  if (route.path.startsWith('/post/') || route.path === '/search') return '/'
  if (route.path.startsWith('/archive')) return '/archive'
  if (route.path.startsWith('/category')) return '/category'
  if (route.path.startsWith('/tag')) return '/tag'
  return route.path
})

const currentPost = computed(() => {
  if (route.name !== 'post') return undefined
  const id = Number(route.params.id)
  const detail = articleStore.detail
  return detail && detail.id === id ? detail : undefined
})

const categoryPosts = computed(() => {
  const post = currentPost.value
  if (!post || post.categoryId <= 0) return []
  return articleStore.categoryIndex[post.categoryId] ?? []
})

const currentCategory = computed<CategoryVO | undefined>(() => {
  const post = currentPost.value
  // categoryId = 0 表示无分类，不提供分类目录
  if (!post || post.categoryId <= 0) return undefined
  return {
    id: post.categoryId,
    name: post.categoryName || '未分类',
    description: null,
    articleCount: categoryPosts.value.length,
    hotArticles: [],
  }
})

const showCategoryNav = computed(() => currentCategory.value !== undefined)

const categoryIndexLoading = computed(() => {
  const category = currentCategory.value
  if (!category) return false
  return articleStore.categoryIndexLoading[category.id] ?? false
})

function openNavDrawer() {
  drawerVisible.value = true
}

function openCategoryDrawer() {
  const category = currentCategory.value
  if (!category) return
  void articleStore.ensureCategoryIndex(category.id)
  categoryDrawerVisible.value = true
}

watch(
  () => route.query.query,
  (query) => {
    searchStore.setKeyword(typeof query === 'string' ? query : '')
  },
  { immediate: true },
)

watch(
  () => route.fullPath,
  () => {
    drawerVisible.value = false
    categoryDrawerVisible.value = false
  },
)

// 左右两个抽屉互斥：同一时间只保留一个
watch(categoryDrawerVisible, (visible) => {
  if (visible) drawerVisible.value = false
})

watch(drawerVisible, (visible) => {
  if (visible) categoryDrawerVisible.value = false
})

watch(showCategoryNav, (show) => {
  if (!show) categoryDrawerVisible.value = false
})

function submitSearch() {
  const query = keyword.value.trim()
  searchStore.setKeyword(query)
  // 显式关闭移动端抽屉：重复搜索同一关键词时路由不变，不能依赖路由 watcher
  drawerVisible.value = false
  router.push(query ? { name: 'search', query: { query } } : { name: 'search' })
}
</script>

<template>
  <header class="app-header">
    <div class="header-inner">
      <el-button
        v-if="showCategoryNav"
        class="category-entry"
        text
        circle
        :aria-label="`打开本分类文章目录，共 ${categoryPosts.length} 篇`"
        aria-haspopup="dialog"
        :aria-expanded="categoryDrawerVisible"
        @click="openCategoryDrawer"
      >
        <el-icon :size="22"><TocIcon /></el-icon>
      </el-button>

      <RouterLink class="brand" to="/" aria-label="返回首页">
        MyBlog<span class="brand-dot">.</span>
      </RouterLink>

      <el-menu
        class="app-nav desktop-nav"
        mode="horizontal"
        router
        :default-active="activeMenu"
        :ellipsis="false"
      >
        <el-menu-item v-for="item in navItems" :key="item.path" :index="item.path">
          <el-icon><component :is="item.icon" /></el-icon>
          <span>{{ item.label }}</span>
        </el-menu-item>
      </el-menu>

      <div class="header-search">
        <el-input
          v-model="keyword"
          class="header-search-input"
          placeholder="搜索文章..."
          clearable
          aria-label="搜索文章"
          @keyup.enter="submitSearch"
        >
          <template #suffix>
            <el-icon
              class="search-action"
              role="button"
              tabindex="0"
              aria-label="搜索"
              @click="submitSearch"
              @keydown.enter="submitSearch"
            >
              <Search />
            </el-icon>
          </template>
        </el-input>
      </div>

      <el-button
        class="menu-toggle"
        text
        circle
        aria-label="打开站点导航"
        aria-haspopup="dialog"
        :aria-expanded="drawerVisible"
        @click="openNavDrawer"
      >
        <el-icon :size="22"><MenuIcon /></el-icon>
      </el-button>
    </div>

    <el-drawer v-model="drawerVisible" append-to-body direction="rtl" size="280px" title="站点导航">
      <div class="drawer-content">
        <el-input
          v-model="keyword"
          placeholder="搜索文章..."
          clearable
          aria-label="搜索文章"
          @keyup.enter="submitSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>

        <el-menu class="mobile-nav" router :default-active="activeMenu">
          <el-menu-item v-for="item in navItems" :key="item.path" :index="item.path">
            <el-icon><component :is="item.icon" /></el-icon>
            <span>{{ item.label }}</span>
          </el-menu-item>
        </el-menu>
      </div>
    </el-drawer>

    <Teleport to="body">
      <el-button
        v-if="showCategoryNav"
        class="category-fab"
        type="primary"
        aria-label="打开分类目录"
        aria-haspopup="dialog"
        :aria-expanded="categoryDrawerVisible"
        title="分类目录"
        @click="openCategoryDrawer"
      >
        <el-icon :size="20"><TocIcon /></el-icon>
      </el-button>
    </Teleport>

    <CategoryPostDrawer
      v-model="categoryDrawerVisible"
      :category="currentCategory"
      :posts="categoryPosts"
      :current-post-id="currentPost?.id"
      :loading="categoryIndexLoading"
    />
  </header>
</template>

<style scoped>
.app-header {
  position: sticky;
  top: 0;
  z-index: 100;
  border-bottom: 1px solid color-mix(in srgb, var(--el-border-color-lighter) 80%, transparent);
  background-color: color-mix(in srgb, var(--el-bg-color) 62%, transparent);
  backdrop-filter: blur(16px) saturate(160%);
  -webkit-backdrop-filter: blur(16px) saturate(160%);
}

.header-inner {
  display: flex;
  width: 100%;
  max-width: 1280px;
  height: var(--app-header-height);
  align-items: center;
  gap: 20px;
  margin: 0 auto;
  padding: 0 var(--app-gutter);
}

.brand {
  flex-shrink: 0;
  color: var(--el-color-primary);
  font-size: 28px;
  font-weight: 800;
  letter-spacing: -0.8px;
  line-height: 1;
}

.brand-dot {
  color: var(--el-color-primary-light-3);
}

.desktop-nav {
  margin-left: auto;
}

.header-search {
  width: 257px;
}

.header-search-input {
  --el-input-border-radius: var(--el-border-radius-round);
  --el-input-height: 36px;
}

.search-action {
  cursor: pointer;
  color: var(--el-text-color-secondary);
  transition: color var(--el-transition-duration-fast);
}

.search-action:hover {
  color: var(--el-color-primary);
}

.menu-toggle {
  display: none;
  margin-left: auto;
}

/* 移动端左侧分类入口：与右侧菜单按钮统一为透明底、中性灰图标，不再显示篇数角标 */
.category-entry.el-button {
  display: none;
  width: 44px;
  height: 44px;
  flex-shrink: 0;
  color: var(--el-text-color-regular);
}

/* 图标颜色过渡：比 Element Plus 默认的 .1s 稍长，悬浮变蓝更柔和 */
.category-entry.el-button,
.menu-toggle.el-button {
  transition:
    color var(--el-transition-duration-fast) ease,
    background-color var(--el-transition-duration-fast) ease;
}

/* 顶部两侧图标：悬浮 / 聚焦只变图标颜色，不出现圆形底色，与文本链接的 hover 态一致 */
.category-entry.el-button.is-text:hover,
.category-entry.el-button.is-text:focus-visible,
.menu-toggle.el-button.is-text:hover,
.menu-toggle.el-button.is-text:focus-visible {
  background-color: transparent;
  color: var(--el-color-primary);
}

.category-entry.el-button.is-text:active,
.menu-toggle.el-button.is-text:active {
  background-color: transparent;
  color: var(--el-color-primary-light-3);
}

/* 桌面端右侧悬浮分类目录入口：蓝色圆角方块，与返回顶部按钮同列排列，避开滚动条 */
.category-fab.el-button {
  position: fixed;
  right: var(--app-gutter);
  bottom: 88px;
  z-index: 90;
  display: none;
  width: 44px;
  height: 44px;
  padding: 0;
  border: none;
  border-radius: 12px;
  box-shadow: 0 8px 18px color-mix(in srgb, var(--el-color-primary) 28%, transparent);
  transition: box-shadow var(--el-transition-duration) ease;
}

.category-fab.el-button:hover {
  box-shadow: 0 10px 24px color-mix(in srgb, var(--el-color-primary) 38%, transparent);
}

.drawer-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.mobile-nav {
  border-top: 1px solid var(--el-border-color-lighter);
  padding-top: 8px;
}

@media (min-width: 901px) {
  .category-fab.el-button {
    display: inline-flex;
  }
}

@media (max-width: 900px) {
  .desktop-nav,
  .header-search {
    display: none;
  }

  .header-inner {
    display: grid;
    grid-template-columns: 48px minmax(0, 1fr) 48px;
    gap: 0;
  }

  .brand {
    grid-column: 2;
    justify-self: center;
    font-size: 24px;
  }

  .category-entry.el-button {
    display: inline-flex;
    grid-column: 1;
    justify-self: start;
  }

  .menu-toggle {
    display: inline-flex;
    grid-column: 3;
    justify-self: end;
    margin-left: 0;
  }

  .menu-toggle.el-button {
    width: 44px;
    height: 44px;
  }
}
</style>
