<script setup lang="ts">
import { RouterLink } from 'vue-router'

import type { ArticleBriefVO } from '@/types/article'
import type { CategoryVO } from '@/types/category'
import { formatNumber } from '@/util/format'

const { category, hotArticles } = defineProps<{
  category: CategoryVO
  hotArticles: ArticleBriefVO[]
}>()

function formatViews(views: number) {
  return formatNumber(views)
}
</script>

<template>
  <article class="category-card">
    <RouterLink
      class="category-card__top"
      :to="{ name: 'category-detail', params: { id: category.id } }"
    >
      <h2 class="category-card__name">{{ category.name }}</h2>
      <span class="category-card__total">共 {{ category.articleCount }} 篇</span>
    </RouterLink>

    <div class="category-card__body">
      <ol v-if="hotArticles.length" class="category-card__hot-list">
        <li v-for="(article, hotIndex) in hotArticles" :key="article.id">
          <RouterLink
            class="category-card__hot-item"
            :to="`/post/${article.id}`"
            :title="article.title"
          >
            <span class="category-card__hot-index">{{ hotIndex + 1 }}.</span>
            <span class="category-card__hot-title">{{ article.title }}</span>
            <span class="category-card__hot-views">{{ formatViews(article.viewCount) }}</span>
          </RouterLink>
        </li>
      </ol>

      <div v-else class="category-card__empty">
        <el-empty description="该分类下暂无文章" :image-size="52" />
      </div>
    </div>
  </article>
</template>

<style scoped>
.category-card {
  display: flex;
  overflow: hidden;
  min-width: 0;
  flex-direction: column;
  border: 1px solid var(--el-border-color-light);
  border-radius: var(--app-card-radius);
  background-color: var(--el-bg-color);
  transition:
    border-color var(--el-transition-duration-fast),
    box-shadow var(--app-card-hover-duration),
    transform var(--el-transition-duration-fast);
}

.category-card:hover {
  border-color: var(--el-color-primary-light-7);
  box-shadow: var(--app-card-hover-shadow);
  transform: translateY(-2px);
}

/* 分类名区域：整行可点击跳转到分类，左标题 + 右文章总数 */
.category-card__top {
  display: flex;
  min-height: 56px;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  padding: 12px 16px;
  border-bottom: 1px solid var(--el-border-color-lighter);
}

.category-card__name {
  color: var(--el-text-color-primary);
  font-size: 18px;
  font-weight: 700;
  line-height: 1.3;
  transition: color var(--el-transition-duration-fast);
}

.category-card__top:hover .category-card__name {
  color: var(--el-color-primary);
}

.category-card__body {
  display: flex;
  flex: 1;
  flex-direction: column;
  padding: 12px 16px 14px;
}

.category-card__total {
  color: var(--el-text-color-secondary);
  font-size: 12px;
  white-space: nowrap;
}

.category-card__hot-list {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.category-card__hot-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 3px 0;
  color: var(--el-text-color-regular);
  font-size: 14px;
  line-height: 1.5;
}

.category-card__hot-index {
  width: 16px;
  flex-shrink: 0;
  color: var(--el-text-color-secondary);
  font-variant-numeric: tabular-nums;
}

.category-card__hot-title {
  overflow: hidden;
  min-width: 0;
  flex: 1;
  text-overflow: ellipsis;
  white-space: nowrap;
  transition: color var(--el-transition-duration-fast);
}

.category-card__hot-item:hover .category-card__hot-title {
  color: var(--el-color-primary);
}

.category-card__hot-views {
  flex-shrink: 0;
  color: var(--el-text-color-secondary);
  font-size: 12px;
  font-variant-numeric: tabular-nums;
}

.category-card__empty {
  display: flex;
  flex: 1;
  align-items: center;
  justify-content: center;
}

.category-card__empty :deep(.el-empty) {
  padding: 0;
}

.category-card__empty :deep(.el-empty__description) {
  margin-top: 0;
}

.category-card__empty :deep(.el-empty__description p) {
  color: var(--el-text-color-secondary);
  font-size: 13px;
}
</style>
