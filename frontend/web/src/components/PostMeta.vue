<script setup lang="ts">
import { computed } from 'vue'
import { RouterLink } from 'vue-router'
import { Calendar, Folder, PriceTag, View } from '@element-plus/icons-vue'

import type { ArticleVO } from '@/types/article'
import { formatDate, formatNumber } from '@/util/format'

const { post, activeTagId } = defineProps<{
  post: ArticleVO
  activeTagId?: number
}>()

const categoryLink = computed(() => {
  return post.categoryId
    ? { name: 'category-detail', params: { id: post.categoryId } }
    : { name: 'category' }
})

function tagLink(tagId: number) {
  return tagId ? { name: 'tag-detail', params: { id: tagId } } : { name: 'tag' }
}
</script>

<template>
  <div class="post-meta">
    <span class="meta-item">
      <el-icon><Calendar /></el-icon>
      {{ formatDate(post.createTime) }}
    </span>
    <span class="meta-item">
      <el-icon><View /></el-icon>
      {{ formatNumber(post.viewCount) }}
    </span>

    <RouterLink class="meta-item meta-link post-category" :to="categoryLink">
      <el-icon><Folder /></el-icon>
      {{ post.categoryName || '未分类' }}
    </RouterLink>

    <span class="meta-item post-tags">
      <el-icon><PriceTag /></el-icon>
      <span class="post-tags-links">
        <RouterLink
          v-for="tag in post.tags"
          :key="tag.id"
          class="post-tag"
          :class="{ 'is-active': tag.id === activeTagId }"
          :to="tagLink(tag.id)"
        >
          {{ tag.name }}
        </RouterLink>
      </span>
    </span>
  </div>
</template>

<style scoped>
.post-meta {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 8px 12px;
  color: var(--el-text-color-secondary);
  font-size: 13px;
}

.meta-item {
  display: inline-flex;
  align-items: center;
  gap: 4px;
}

.meta-item .el-icon {
  color: var(--el-text-color-placeholder);
  font-size: 14px;
}

/* 分组之间的 | 分隔线 */
.post-meta > * + * {
  position: relative;
}

.post-meta > * + *::before {
  position: absolute;
  top: 50%;
  left: -6px;
  width: 1px;
  height: 12px;
  background-color: var(--el-border-color);
  transform: translateY(-50%);
  content: '';
}

.meta-link,
.meta-link .el-icon {
  transition: color var(--el-transition-duration-fast);
}

.meta-link:hover,
.meta-link:hover .el-icon {
  color: var(--el-color-primary);
}

.post-tags-links {
  display: inline-flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 2px 10px;
}

.post-tag {
  cursor: pointer;
  transition: color var(--el-transition-duration-fast);
}

.post-tag:hover {
  color: var(--el-color-primary);
}

/* 标签页中与当前选中标签匹配的标签：常亮主色并加粗，与悬浮态区分 */
.post-tag.is-active {
  color: var(--el-color-primary);
  font-weight: 500;
}

/* 中等宽度下元信息会换行，分隔线会悬在行首，改为仅用间距分隔 */
@media (max-width: 1199px) {
  .post-meta {
    gap: 8px 14px;
  }

  .post-meta > * + *::before {
    display: none;
  }
}
</style>
