<script setup lang="ts">
import { RouterLink } from 'vue-router'

import type { ArticleVO } from '@/types/article'

defineProps<{
  posts: ArticleVO[]
}>()

function formatArchiveDate(date: string) {
  return `${Number(date.slice(5, 7))}月`
}
</script>

<template>
  <ul class="archive-list">
    <li v-for="post in posts" :key="post.id" class="archive-row">
      <span class="archive-dot" aria-hidden="true" />

      <RouterLink class="archive-title" :to="`/post/${post.id}`">
        {{ post.title }}
      </RouterLink>

      <el-tag class="archive-date" size="small" effect="plain">
        {{ formatArchiveDate(post.publishTime) }}
      </el-tag>
    </li>
  </ul>
</template>

<style scoped>
.archive-row {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px 20px;
  border-bottom: 1px solid var(--el-border-color-lighter);
  transition: background-color var(--el-transition-duration-fast);
}

.archive-row:last-child {
  border-bottom: none;
}

.archive-row:hover {
  background-color: var(--el-fill-color-lighter);
}

.archive-dot {
  width: 7px;
  height: 7px;
  flex-shrink: 0;
  border-radius: 50%;
  background-color: var(--el-color-primary);
  box-shadow: 0 0 0 4px var(--el-color-primary-light-9);
}

.archive-title {
  overflow: hidden;
  min-width: 0;
  color: var(--el-text-color-regular);
  font-size: 15px;
  text-overflow: ellipsis;
  white-space: nowrap;
  transition: color var(--el-transition-duration-fast);
}

.archive-title:hover {
  color: var(--el-color-primary);
}

.archive-date {
  flex-shrink: 0;
  margin-left: auto;
}

@media (max-width: 768px) {
  .archive-row {
    flex-wrap: wrap;
    gap: 8px 10px;
    padding: 12px 16px;
  }

  .archive-title {
    white-space: normal;
  }

  .archive-original {
    margin-left: 0;
  }
}
</style>
