<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { RouterLink } from 'vue-router'
import { CollectionTag, FolderOpened, Star, View } from '@element-plus/icons-vue'

import { siteConfig } from '@/config/site'
import { useSiteStore } from '@/stores/site'
import { formatNumber } from '@/util/format'
import SidebarCard from './SidebarCard.vue'

const siteStore = useSiteStore()

const sidebarCategories = computed(() => siteStore.topCategories)
const sidebarTags = computed(() => siteStore.topTags)
const recommendedPosts = computed(() => siteStore.latestArticles)

const stats = computed(() => ({
  article: siteStore.statistics ? formatNumber(siteStore.statistics.articleCount) : '--',
  tag: siteStore.statistics ? formatNumber(siteStore.statistics.tagCount) : '--',
  category: siteStore.statistics ? formatNumber(siteStore.statistics.categoryCount) : '--',
}))

onMounted(() => {
  siteStore.load()
})
</script>

<template>
  <div v-loading="siteStore.loading" class="blog-sidebar">
    <el-card class="profile-card" shadow="never">
      <div class="profile">
        <el-avatar class="profile-avatar" :size="100" :src="siteConfig.avatar" alt="博主头像" />
        <h2 class="profile-name">{{ siteConfig.nickname }}</h2>
        <p class="profile-bio">{{ siteConfig.bio }}</p>

        <div class="profile-stats">
          <RouterLink class="profile-stat" :to="{ name: 'archive' }">
            <span class="profile-stat-label">文章</span>
            <span class="profile-stat-value">{{ stats.article }}</span>
          </RouterLink>
          <RouterLink class="profile-stat" :to="{ name: 'tag' }">
            <span class="profile-stat-label">标签</span>
            <span class="profile-stat-value">{{ stats.tag }}</span>
          </RouterLink>
          <RouterLink class="profile-stat" :to="{ name: 'category' }">
            <span class="profile-stat-label">分类</span>
            <span class="profile-stat-value">{{ stats.category }}</span>
          </RouterLink>
        </div>

        <div class="profile-links">
          <a class="profile-link" :href="siteConfig.github" aria-label="GitHub 主页">
            <i class="ri-github-fill" aria-hidden="true" />
          </a>
          <a class="profile-link" :href="siteConfig.gitee" aria-label="Gitee 主页">
            <i class="ri-gitee-fill" aria-hidden="true" />
          </a>
          <a class="profile-link" :href="`mailto:${siteConfig.email}`" aria-label="邮箱联系">
            <i class="ri-mail-fill" aria-hidden="true" />
          </a>
        </div>
      </div>
    </el-card>

    <SidebarCard title="最新推荐" :icon="Star" hoverable>
      <ul v-if="recommendedPosts.length" class="text-link-list recommend-list">
        <li v-for="post in recommendedPosts" :key="post.id">
          <RouterLink :to="`/post/${post.id}`">
            <span class="recommend-title">{{ post.title }}</span>
            <span class="recommend-views">
              <el-icon><View /></el-icon>
              {{ formatNumber(post.viewCount) }}
            </span>
          </RouterLink>
        </li>
      </ul>
      <el-empty v-else description="暂无文章" :image-size="48" />
    </SidebarCard>

    <SidebarCard title="分类" :icon="FolderOpened" hoverable>
      <ul v-if="sidebarCategories.length" class="category-list">
        <li v-for="category in sidebarCategories" :key="category.id">
          <RouterLink
            class="category-item"
            :to="{ name: 'category-detail', params: { id: category.id } }"
          >
            <span class="category-name">{{ category.name }}</span>
            <span class="category-count">{{ category.articleCount }}</span>
          </RouterLink>
        </li>
      </ul>
      <el-empty v-else description="暂无分类" :image-size="48" />
    </SidebarCard>

    <SidebarCard title="标签" :icon="CollectionTag" hoverable>
      <div v-if="sidebarTags.length" class="tag-cloud">
        <RouterLink
          v-for="tag in sidebarTags"
          :key="tag.id"
          :to="{ name: 'tag-detail', params: { id: tag.id } }"
        >
          <el-tag effect="plain" size="large">
            {{ tag.name }}
            <span class="tag-count">{{ tag.articleCount }}</span>
          </el-tag>
        </RouterLink>
      </div>
      <el-empty v-else description="暂无标签" :image-size="48" />
    </SidebarCard>
  </div>
</template>

<style scoped>
.blog-sidebar {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

/* 博主信息：头像、昵称、简介、社交图标自上而下居中排列 */
.profile-card {
  transition: box-shadow var(--app-card-hover-duration);
}

.profile-card:hover {
  box-shadow: var(--app-card-hover-shadow);
}

.profile-card :deep(.el-card__body) {
  padding: 26px 20px 24px;
}

.profile {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.profile-avatar {
  border: 1px solid var(--el-border-color-lighter);
  background-color: var(--el-color-primary-light-9);
}

.profile-name {
  margin-top: 14px;
  color: var(--el-text-color-primary);
  font-size: 18px;
  font-weight: 600;
  line-height: 1.4;
}

.profile-bio {
  margin-top: 8px;
  color: var(--app-text-color-body);
  font-size: 13px;
  line-height: 1.7;
}

/* 数据统计：文章 / 标签 / 分类三等分，标签在上、数值在下，整块可点击跳转 */
.profile-stats {
  display: grid;
  width: 100%;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  margin-top: 16px;
}

.profile-stat {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0;
  padding: 8px 4px 6px;
}

.profile-stat-label {
  color: var(--app-text-color-body);
  font-size: 15px;
  line-height: 1.2;
  transition: color var(--el-transition-duration-fast);
}

.profile-stat-value {
  color: var(--el-text-color-primary);
  font-size: 15px;
  font-variant-numeric: tabular-nums;
  font-weight: 400;
  line-height: 1.2;
  transition: color var(--el-transition-duration-fast);
}

/* 悬浮态：仅标签与数值文字变主色，不加底色 */
.profile-stat:hover .profile-stat-label,
.profile-stat:hover .profile-stat-value {
  color: var(--el-color-primary);
}

.profile-links {
  display: flex;
  align-items: center;
  gap: 26px;
  margin-top: 14px;
}

.profile-link {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  color: #5e5b5a;
  font-size: 27px;
  line-height: 1;
  transition: color var(--el-transition-duration-fast);
}

.profile-link:hover {
  color: var(--el-color-primary);
}

/* 最新推荐：标题与浏览量左右分布，标题超长省略 */
.recommend-list a {
  display: flex;
  align-items: center;
  gap: 12px;
}

.recommend-title {
  overflow: hidden;
  min-width: 0;
  flex: 1;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* 固定宽度并左对齐：各行浏览量的眼睛图标与数字从同一位置开始，形成整齐的一列 */
.recommend-views {
  display: inline-flex;
  width: 52px;
  flex-shrink: 0;
  align-items: center;
  justify-content: flex-start;
  gap: 3px;
  color: var(--el-text-color-secondary);
  font-size: 12px;
  font-variant-numeric: tabular-nums;
}

.recommend-views .el-icon {
  color: var(--el-text-color-placeholder);
  font-size: 13px;
}

/* 分类列表：分类名与文章数左右两列，悬浮效果加在单行列表项上 */
.category-list {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.category-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-inline: -10px;
  padding: 8px 10px;
  border-radius: 8px;
  color: var(--el-text-color-regular);
  font-size: 14px;
  line-height: 1.4;
  transition:
    color var(--el-transition-duration-fast),
    background-color var(--el-transition-duration-fast);
}

.category-item:hover {
  background-color: var(--el-color-primary-light-9);
  color: var(--el-color-primary);
}

.category-name {
  overflow: hidden;
  min-width: 0;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.category-count {
  flex-shrink: 0;
  color: var(--el-text-color-secondary);
  font-size: 12px;
  font-variant-numeric: tabular-nums;
  transition: color var(--el-transition-duration-fast);
}

.category-item:hover .category-count {
  color: var(--el-color-primary);
}

.tag-cloud {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

/* large 尺寸只放大了高度，字号仍为 12px，这里一并放大 */
.tag-cloud :deep(.el-tag) {
  --el-tag-text-color: var(--el-text-color-regular);
  --el-tag-border-color: var(--el-border-color);

  font-size: 14px;
  transition:
    color var(--el-transition-duration-fast),
    border-color var(--el-transition-duration-fast);
}

/* 悬浮后文字与描边一起变蓝 */
.tag-cloud :deep(.el-tag:hover) {
  --el-tag-text-color: var(--el-color-primary);
  --el-tag-border-color: var(--el-color-primary);
}

.tag-count {
  margin-left: 6px;
  font-size: 12px;
  opacity: 0.65;
}

@media (max-width: 1024px) and (min-width: 641px) {
  .blog-sidebar {
    display: grid;
    grid-template-columns: repeat(2, minmax(0, 1fr));
    align-items: start;
  }
}
</style>
