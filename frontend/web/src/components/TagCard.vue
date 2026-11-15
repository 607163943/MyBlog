<script setup lang="ts">
import { RouterLink } from 'vue-router'
import { TopRight } from '@element-plus/icons-vue'

import type { TagVO } from '@/types/tag'

const { tag } = defineProps<{
  tag: TagVO
}>()
</script>

<template>
  <RouterLink
    class="tag-card"
    :to="{ name: 'tag-detail', params: { id: tag.id } }"
    :title="tag.name"
  >
    <span class="tag-card__head">
      <span class="tag-card__name">{{ tag.name }}</span>
      <el-icon class="tag-card__arrow" aria-hidden="true"><TopRight /></el-icon>
    </span>
    <span class="tag-card__count">{{ tag.articleCount }} 篇文章</span>
  </RouterLink>
</template>

<style scoped>
.tag-card {
  display: flex;
  min-width: 0;
  flex-direction: column;
  gap: 6px;
  padding: 14px 16px;
  border: 1px solid var(--el-border-color-light);
  border-radius: var(--app-card-radius);
  background-color: var(--el-bg-color);
  transition:
    border-color var(--el-transition-duration-fast),
    background-color var(--el-transition-duration-fast),
    box-shadow var(--el-transition-duration-fast),
    transform var(--el-transition-duration-fast);
}

.tag-card:hover {
  border-color: var(--el-color-primary-light-5);
  background-color: var(--el-color-primary-light-9);
  box-shadow: 0 8px 20px rgba(31, 45, 61, 0.07);
  transform: translateY(-2px);
}

.tag-card__head {
  display: flex;
  min-width: 0;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
}

.tag-card__name {
  overflow: hidden;
  color: var(--el-text-color-primary);
  font-size: 15px;
  font-weight: 600;
  line-height: 1.4;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.tag-card__arrow {
  flex-shrink: 0;
  color: var(--el-text-color-placeholder);
  font-size: 14px;
  transition: color var(--el-transition-duration-fast);
}

.tag-card__count {
  color: var(--el-text-color-secondary);
  font-size: 12px;
  line-height: 1.4;
}

@media (max-width: 640px) {
  .tag-card {
    padding: 12px 14px;
  }

  .tag-card__name {
    font-size: 14px;
  }
}
</style>
