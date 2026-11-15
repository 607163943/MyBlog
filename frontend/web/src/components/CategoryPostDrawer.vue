<script setup lang="ts">
import { computed, nextTick, onUnmounted, ref, watch } from 'vue'
import { RouterLink } from 'vue-router'
import { ArrowLeft, ArrowRight, Close, Search } from '@element-plus/icons-vue'

import type { ArticleBriefVO } from '@/types/article'
import type { CategoryVO } from '@/types/category'
import HighlightText from './HighlightText.vue'

const props = defineProps<{
  category?: CategoryVO
  posts: ArticleBriefVO[]
  currentPostId?: number
  loading?: boolean
}>()

const visible = defineModel<boolean>({ required: true })

// 桌面端入口在视口右侧，抽屉也从右侧打开；移动端保持从左侧打开
const desktopMedia = window.matchMedia('(min-width: 901px)')
const isDesktop = ref(desktopMedia.matches)

function syncDrawerSide() {
  isDesktop.value = desktopMedia.matches
}

desktopMedia.addEventListener('change', syncDrawerSide)
onUnmounted(() => desktopMedia.removeEventListener('change', syncDrawerSide))

const drawerDirection = computed(() => (isDesktop.value ? 'rtl' : 'ltr'))

// 移动端收窄到 80vw 并封顶 360px，给右侧遮罩留出可点击的关闭区域
const drawerSize = computed(() => (isDesktop.value ? '420px' : 'min(80vw, 360px)'))

const listRef = ref<HTMLElement>()

function formatIndex(index: number) {
  return String(index + 1).padStart(2, '0')
}

/** 抽屉展开后把当前文章滚到列表中间，避免长列表里看不到自己 */
async function scrollActiveIntoView() {
  await nextTick()
  const list = listRef.value
  const active = list?.querySelector<HTMLElement>('[aria-current="page"]')
  if (!list || !active) return

  const listRect = list.getBoundingClientRect()
  const activeRect = active.getBoundingClientRect()
  list.scrollTop += activeRect.top - listRect.top - (list.clientHeight - activeRect.height) / 2
}

/** 抽屉内搜索：只筛当前分类的文章，标题不区分大小写匹配 */
const keyword = ref('')

interface IndexedPost {
  post: ArticleBriefVO
  index: number
}

const filteredPosts = computed<IndexedPost[]>(() => {
  const query = keyword.value.trim().toLowerCase()
  return props.posts
    .map((post, index) => ({ post, index }))
    .filter(({ post }) => !query || post.title.toLowerCase().includes(query))
})

// 关键词变化后列表回到顶部，避免停在上一轮结果的滚动位置
watch(keyword, () => {
  if (listRef.value) listRef.value.scrollTop = 0
})

// 抽屉收起时清空搜索，下次打开始终展示完整目录
watch(visible, (open) => {
  if (!open) keyword.value = ''
})
</script>

<template>
  <el-drawer
    v-model="visible"
    class="category-drawer"
    :direction="drawerDirection"
    :with-header="false"
    :size="drawerSize"
    append-to-body
    :aria-label="category ? `本分类文章：${category.name}` : '本分类文章'"
    @opened="scrollActiveIntoView"
  >
    <div v-if="category" class="category-drawer-inner">
      <header class="category-drawer-head">
        <div class="category-drawer-head-top">
          <div class="category-drawer-heading">
            <span class="category-drawer-eyebrow">分类目录</span>
            <div class="category-drawer-title-row">
              <h2 class="category-drawer-title">{{ category.name }}</h2>
              <span class="category-drawer-count">共 {{ posts.length }} 篇</span>
            </div>
          </div>

          <button
            class="category-drawer-close"
            type="button"
            aria-label="关闭分类目录"
            @click="visible = false"
          >
            <el-icon :size="20"><Close /></el-icon>
          </button>
        </div>

        <div class="category-drawer-search">
          <el-input
            v-model="keyword"
            class="category-drawer-search-input"
            placeholder="搜索文章..."
            clearable
            aria-label="在本分类中搜索文章"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
        </div>
      </header>

      <div ref="listRef" v-loading="loading" class="category-drawer-scroll">
        <nav aria-label="本分类文章">
          <ul v-if="filteredPosts.length" class="category-drawer-list">
            <li v-for="{ post, index } in filteredPosts" :key="post.id">
              <RouterLink
                class="category-drawer-item"
                :class="{ 'is-active': post.id === currentPostId }"
                :to="`/post/${post.id}`"
                :aria-current="post.id === currentPostId ? 'page' : undefined"
              >
                <span class="category-drawer-index">{{ formatIndex(index) }}</span>
                <span class="category-drawer-item-title">
                  <HighlightText :text="post.title" :keyword="keyword" />
                </span>
              </RouterLink>
            </li>
          </ul>

          <div v-else class="category-drawer-empty" role="status">
            <el-icon class="category-drawer-empty-icon" :size="26"><Search /></el-icon>
            <p class="category-drawer-empty-text">没有找到相关文章</p>
            <el-button text type="primary" size="small" @click="keyword = ''">
              清除关键词
            </el-button>
          </div>
        </nav>
      </div>

      <footer class="category-drawer-foot">
        <RouterLink
          class="category-drawer-more"
          :to="{ name: 'category-detail', params: { id: category.id } }"
        >
          <span>查看「{{ category.name }}」分类页</span>
          <el-icon><ArrowRight /></el-icon>
        </RouterLink>

        <el-button class="category-drawer-collapse" text @click="visible = false">
          <el-icon><ArrowLeft /></el-icon>
          <span>收起目录</span>
        </el-button>
      </footer>
    </div>
  </el-drawer>
</template>

<style scoped>
.category-drawer-inner {
  display: flex;
  flex: 1;
  min-height: 0;
  flex-direction: column;
  background-color: var(--el-bg-color);
}

.category-drawer-head {
  flex-shrink: 0;
  padding: 20px 20px 16px;
  border-bottom: 1px solid var(--el-border-color-lighter);
}

.category-drawer-head-top {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 12px;
}

.category-drawer-heading {
  min-width: 0;
}

.category-drawer-close {
  display: inline-flex;
  width: 44px;
  height: 44px;
  flex-shrink: 0;
  align-items: center;
  justify-content: center;
  padding: 0;
  border: none;
  border-radius: 50%;
  background-color: var(--el-fill-color-light);
  color: var(--el-text-color-primary);
  cursor: pointer;
  transition:
    background-color var(--el-transition-duration-fast),
    color var(--el-transition-duration-fast);
}

.category-drawer-close:hover {
  background-color: var(--el-color-primary-light-9);
  color: var(--el-color-primary);
}

.category-drawer-close:focus-visible {
  outline: 2px solid var(--el-color-primary);
  outline-offset: 2px;
}

.category-drawer-eyebrow {
  color: var(--el-text-color-secondary);
  font-size: 12px;
  letter-spacing: 0.08em;
}

.category-drawer-title-row {
  display: flex;
  flex-wrap: wrap;
  align-items: baseline;
  gap: 8px;
  margin-top: 4px;
}

.category-drawer-title {
  color: var(--el-text-color-primary);
  font-size: 22px;
  font-weight: 600;
  line-height: 1.3;
}

.category-drawer-count {
  color: var(--el-text-color-secondary);
  font-size: 12px;
}

/* 搜索条：位于目录标题下方、文章列表上方，跟随头部固定不参与列表滚动 */
.category-drawer-search {
  margin-top: 16px;
}

.category-drawer-search-input {
  --el-input-height: 38px;
  --el-input-border-radius: 10px;
  --el-input-bg-color: var(--el-fill-color-blank);
  --el-input-icon-color: var(--el-text-color-placeholder);
}

.category-drawer-scroll {
  overflow-y: auto;
  min-height: 0;
  flex: 1;
  padding: 10px 12px;
  overscroll-behavior: contain;
  scrollbar-width: thin;
}

.category-drawer-list {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.category-drawer-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 11px 12px;
  border-radius: 10px;
  color: var(--el-text-color-regular);
  font-size: 14px;
  line-height: 1.5;
  transition:
    color var(--el-transition-duration-fast),
    background-color var(--el-transition-duration-fast);
}

/* 悬浮：中性浅底 + 正文色，不使用主色高亮（参考图 2 的 hover 态） */
.category-drawer-item:hover {
  background-color: var(--el-fill-color-light);
  color: var(--el-text-color-primary);
}

/* 激活：Element Plus 主色浅底 + 主色文字，圆角块状，无左侧竖条 */
.category-drawer-item.is-active {
  background-color: var(--el-color-primary-light-9);
  color: var(--el-color-primary);
}

.category-drawer-index {
  width: 24px;
  flex-shrink: 0;
  color: var(--el-text-color-placeholder);
  font-size: 12px;
  font-variant-numeric: tabular-nums;
}

.category-drawer-item:hover .category-drawer-index {
  color: var(--el-text-color-secondary);
}

.category-drawer-item.is-active .category-drawer-index {
  color: var(--el-color-primary);
}

.category-drawer-item-title {
  overflow: hidden;
  min-width: 0;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* 搜索无结果：紧凑占位，避免撑开抽屉的滚动区 */
.category-drawer-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 48px 12px;
  color: var(--el-text-color-secondary);
  text-align: center;
}

.category-drawer-empty-icon {
  color: var(--el-text-color-placeholder);
}

.category-drawer-empty-text {
  font-size: 13px;
  line-height: 1.6;
}

.category-drawer-foot {
  display: flex;
  flex-shrink: 0;
  flex-direction: column;
  gap: 10px;
  padding: 14px 20px calc(14px + env(safe-area-inset-bottom));
  border-top: 1px solid var(--el-border-color-lighter);
}

/* 底栏收起按钮：全宽文字按钮，照顾单手拇指够不到顶部关闭按钮的场景 */
.category-drawer-collapse.el-button {
  width: 100%;
  height: 44px;
  margin: 0;
  border-radius: 10px;
  color: var(--el-text-color-regular);
}

.category-drawer-collapse.el-button:hover {
  background-color: var(--el-fill-color-light);
  color: var(--el-color-primary);
}

.category-drawer-more {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  color: var(--el-color-primary);
  font-size: 14px;
  transition: color var(--el-transition-duration-fast);
}

.category-drawer-more:hover {
  color: var(--el-color-primary-light-3);
}
</style>
