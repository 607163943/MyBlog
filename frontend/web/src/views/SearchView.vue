<script setup lang="ts">
import { computed, watch } from 'vue'
import { RouterLink, useRoute, useRouter } from 'vue-router'
import { Search } from '@element-plus/icons-vue'

import BlogSidebar from '@/components/BlogSidebar.vue'
import ListPagination from '@/components/ListPagination.vue'
import PostListItem from '@/components/PostListItem.vue'
import { useSearchStore } from '@/stores/search'
import { useSiteStore } from '@/stores/site'

const route = useRoute()
const router = useRouter()
const searchStore = useSearchStore()
const siteStore = useSiteStore()

const keyword = computed(() => {
  const query = route.query.query
  return typeof query === 'string' ? query.trim() : ''
})

const hotTags = computed(() => siteStore.topTags.slice(0, 6))

function submitMobileSearch() {
  const query = searchStore.keyword.trim()
  // 页内重搜用 replace：连续改关键词不堆历史记录，返回键直接回到搜索前的页面
  router.replace(query ? { name: 'search', query: { query } } : { name: 'search' })
}

function changePage(page: number) {
  searchStore.search(keyword.value, page)
}

watch(
  () => route.query.query,
  () => {
    searchStore.setKeyword(keyword.value)
    searchStore.search(keyword.value, 1)
  },
  { immediate: true },
)
</script>

<template>
  <div class="page-container page-container--wide">
    <div class="layout-with-aside">
      <div v-loading="searchStore.loading" class="layout-main">
        <!-- 移动端专属页内搜索：桌面端由头部搜索框承担，避免出现两个输入框 -->
        <div class="mobile-search">
          <el-input
            v-model="searchStore.keyword"
            class="mobile-search-input"
            placeholder="搜索文章..."
            clearable
            aria-label="搜索文章"
            @keyup.enter="submitMobileSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
          <el-button type="primary" class="mobile-search-button" @click="submitMobileSearch">
            搜索
          </el-button>
        </div>

        <el-card v-if="keyword" class="head-only-card" shadow="never" body-style="display: none">
          <template #header>
            <div class="card-heading search-heading">
              <div class="card-heading-title">
                <span class="heading-accent" />
                <h2 class="search-heading-text">“{{ keyword }}” 的搜索结果</h2>
              </div>
              <span class="card-heading-meta">
                共
                <strong>{{ searchStore.total }}</strong>
                篇
              </span>
            </div>
          </template>
        </el-card>

        <el-card v-else shadow="never">
          <template #header>
            <div class="card-heading">
              <div class="card-heading-title">
                <span class="heading-accent" />
                <h2>搜索</h2>
              </div>
            </div>
          </template>

          <el-empty description="输入关键词，搜索博客文章">
            <div v-if="hotTags.length" class="hot-search">
              <span class="hot-search-label">热门搜索</span>
              <RouterLink
                v-for="tag in hotTags"
                :key="tag.id"
                :to="{ name: 'tag-detail', params: { id: tag.id } }"
              >
                <el-tag effect="plain">{{ tag.name }}</el-tag>
              </RouterLink>
            </div>
          </el-empty>
        </el-card>

        <div v-if="searchStore.results.length" class="post-list">
          <PostListItem
            v-for="post in searchStore.results"
            :key="post.id"
            :post="post"
            :keyword="keyword"
          />
        </div>

        <el-card v-else-if="keyword && searchStore.searched && !searchStore.loading" shadow="never">
          <el-empty
            :description="searchStore.error || `没有找到与 “${keyword}” 相关的文章，换个关键词试试`"
          />
        </el-card>

        <ListPagination
          v-if="searchStore.total > 0"
          :total="searchStore.total"
          :page="searchStore.page"
          :page-size="searchStore.pageSize"
          @update:page="changePage"
        />
      </div>

      <aside class="home-aside">
        <BlogSidebar />
      </aside>
    </div>
  </div>
</template>

<style scoped>
/* 移动端页内搜索：与头部搜索框同款圆角，触摸目标加高到 44px */
.mobile-search {
  display: none;
}

.mobile-search-input {
  --el-input-height: 44px;
  --el-input-border-radius: var(--el-border-radius-round);
}

.mobile-search-button {
  height: 44px;
  flex-shrink: 0;
  padding: 0 20px;
  border-radius: var(--el-border-radius-round);
}

/* 结果标题回显关键词：长关键词允许换行，计数保持不换行 */
.search-heading .card-heading-title {
  min-width: 0;
}

.search-heading-text {
  overflow-wrap: anywhere;
}

:deep(.el-empty__description p) {
  overflow-wrap: anywhere;
}

.hot-search {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.hot-search-label {
  color: var(--el-text-color-secondary);
  font-size: 13px;
}

/* 头部搜索框在 ≤900px 隐藏，页内搜索条在同一断点出现 */
@media (max-width: 900px) {
  .mobile-search {
    display: flex;
    align-items: center;
    gap: 10px;
  }

  .mobile-search-input {
    flex: 1;
    min-width: 0;
  }
}
</style>
