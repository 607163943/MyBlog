<script setup lang="ts">
import { computed, watch } from 'vue'
import { RouterLink, useRoute, useRouter } from 'vue-router'
import { ArrowLeft, ArrowRight } from '@element-plus/icons-vue'
import { MdPreview } from 'md-editor-v3'
import 'md-editor-v3/lib/preview.css'

import BlogSidebar from '@/components/BlogSidebar.vue'
import CommentSection from '@/components/CommentSection.vue'
import PostMeta from '@/components/PostMeta.vue'
import { useArticleStore } from '@/stores/article'
import { useCommentStore } from '@/stores/comment'
import { parsePositiveInt } from '@/util/route'
import { setPageTitle } from '@/util/title'

const route = useRoute()
const router = useRouter()
const articleStore = useArticleStore()
const commentStore = useCommentStore()

const postId = computed(() => parsePositiveInt(route.params.id))
const post = computed(() => articleStore.detail)
const previewId = computed(() => `post-detail-preview-${postId.value ?? 0}`)

/** 无分类文章（categoryId = 0）不展示任何分类入口 */
const showCategory = computed(() => {
  const current = post.value
  return !!current && current.categoryId > 0 && !!current.categoryName
})

const categoryLink = computed(() => {
  const current = post.value
  return current
    ? { name: 'category-detail', params: { id: current.categoryId } }
    : { name: 'category' }
})

function tagLink(tagId: number) {
  return tagId ? { name: 'tag-detail', params: { id: tagId } } : { name: 'tag' }
}

// 从任意入口（首页、分类 / 标签详情、搜索、归档、站外）进入时都按站内历史返回；
// 直接打开详情页（没有站内历史）时回到首页
function goBack() {
  if (window.history.state?.back) {
    router.back()
    return
  }
  router.push({ name: 'home' })
}

function load() {
  const id = postId.value
  if (!id) {
    articleStore.resetDetail()
    articleStore.detailError = '文章不存在或已被删除'
    return
  }
  articleStore.loadDetail(id)
}

watch(postId, load, { immediate: true })

watch(post, (current) => {
  if (current) {
    setPageTitle(current.title)
  }
})
</script>

<template>
  <div class="page-container page-container--wide">
    <div v-if="post" class="layout-with-aside">
      <div class="layout-main">
        <el-card class="article-card" shadow="never">
          <div class="article-head">
            <el-button class="article-back" text :icon="ArrowLeft" @click="goBack">返回</el-button>
            <el-breadcrumb class="article-breadcrumb" separator="/">
              <el-breadcrumb-item :to="{ name: 'home' }">首页</el-breadcrumb-item>
              <el-breadcrumb-item v-if="showCategory" :to="categoryLink">
                {{ post.categoryName }}
              </el-breadcrumb-item>
              <el-breadcrumb-item>正文</el-breadcrumb-item>
            </el-breadcrumb>
          </div>

          <h1 class="article-title">{{ post.title }}</h1>

          <div class="article-meta">
            <PostMeta class="article-meta-items" :post="post" />
          </div>

          <el-divider />

          <div class="article-body">
            <MdPreview
              :id="previewId"
              :model-value="post.content"
              preview-theme="default"
              code-theme="github"
              language="zh-CN"
              :show-code-row-number="false"
              :code-style-reverse="false"
              :no-katex="true"
              :no-mermaid="true"
              :no-echarts="true"
            />
          </div>

          <el-divider />

          <div class="article-taxonomies">
            <div v-if="showCategory" class="article-taxonomy-row">
              <span class="article-taxonomy-label">分类</span>
              <RouterLink :to="categoryLink">
                <el-tag type="warning" effect="plain" size="small">
                  {{ post.categoryName }}
                </el-tag>
              </RouterLink>
            </div>

            <div class="article-taxonomy-row">
              <span class="article-taxonomy-label">标签</span>
              <RouterLink v-for="tag in post.tags" :key="tag.id" :to="tagLink(tag.id)">
                <el-tag effect="plain" size="small">{{ tag.name }}</el-tag>
              </RouterLink>
            </div>
          </div>

          <div class="article-nav">
            <RouterLink
              v-if="post.prevArticle"
              class="article-nav-item"
              :to="`/post/${post.prevArticle.id}`"
            >
              <span class="article-nav-label">
                <el-icon><ArrowLeft /></el-icon>
                上一篇
              </span>
              <span class="article-nav-title">{{ post.prevArticle.title }}</span>
            </RouterLink>

            <RouterLink
              v-if="post.nextArticle"
              class="article-nav-item article-nav-item--next"
              :to="`/post/${post.nextArticle.id}`"
            >
              <span class="article-nav-label">
                下一篇
                <el-icon><ArrowRight /></el-icon>
              </span>
              <span class="article-nav-title">{{ post.nextArticle.title }}</span>
            </RouterLink>
          </div>
        </el-card>

        <el-card class="comment-card" shadow="never">
          <template #header>
            <div class="card-heading">
              <div class="card-heading-title">
                <span class="heading-accent" />
                <h2>评论</h2>
              </div>
              <span class="card-heading-meta">
                共
                <strong>{{ commentStore.totalCount }}</strong>
                条
              </span>
            </div>
          </template>

          <CommentSection :article-id="post.id" />
        </el-card>
      </div>

      <aside class="detail-aside">
        <BlogSidebar />
      </aside>
    </div>

    <el-card v-else-if="articleStore.detailLoading" v-loading="true" shadow="never">
      <div class="detail-loading-placeholder" />
    </el-card>

    <el-card v-else shadow="never">
      <el-empty :description="articleStore.detailError || '文章不存在或已被删除'" />
    </el-card>
  </div>
</template>

<style scoped>
.article-card :deep(.el-card__body) {
  /* 顶部收紧：返回按钮贴近卡片上沿，与下方面包屑留出间距 */
  padding: 16px 24px 24px;
}

.detail-loading-placeholder {
  min-height: 260px;
}

/* 头部导航区：返回按钮单独一行，面包屑在下方独立一行 */
.article-head {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 10px;
  margin-bottom: 18px;
}

.article-back {
  padding: 0;
}

/* 悬浮 / 按下只改变文字颜色，不出现背景块（与分类 / 标签详情页一致） */
.article-back.el-button.is-text:hover,
.article-back.el-button.is-text:active {
  background-color: transparent;
  color: var(--el-color-primary);
}

.article-breadcrumb {
  margin-bottom: 0;
}

.article-title {
  color: var(--el-text-color-primary);
  font-size: 30px;
  font-weight: 700;
  letter-spacing: -0.3px;
  line-height: 1.35;
}

.article-meta {
  display: flex;
  align-items: flex-start;
  gap: 16px;
  margin-top: 16px;
}

.article-meta-items {
  min-width: 0;
  flex: 1;
}

.article-original {
  flex-shrink: 0;
}

/* 正文 Markdown 预览（md-editor-v3 MdPreview）：排版对齐设计稿的正文样式 */
.article-body :deep(.md-editor) {
  width: 100%;
  height: auto;
  border: none;
  overflow: visible;
  background-color: transparent;
  color: var(--el-text-color-regular);
  font-family: inherit;
  font-size: 15px;
}

.article-body :deep(.md-editor-content),
.article-body :deep(.md-editor-preview-wrapper) {
  height: auto;
  overflow: visible;
}

.article-body :deep(.md-editor-preview) {
  --md-theme-color: var(--el-text-color-regular);
  --md-theme-link-color: var(--el-color-primary);
  --md-theme-link-hover-color: var(--el-color-primary-light-3);
  --md-theme-border-color: var(--el-border-color-lighter);
  --md-theme-bg-color: transparent;
  --md-theme-bg-color-inset: var(--el-color-primary-light-9);
  --md-theme-heading-color: var(--el-text-color-primary);
  --md-theme-quote-color: var(--el-text-color-regular);
  --md-theme-quote-border: 3px solid var(--el-color-primary);
  --md-theme-quote-bg-color: var(--el-color-primary-light-9);
  --md-theme-code-block-bg-color: var(--el-fill-color-light);
  --md-theme-code-before-bg-color: var(--el-fill-color-light);
  --md-theme-code-block-color: var(--el-text-color-primary);
  --md-theme-code-inline-color: var(--el-color-primary);
  --md-theme-code-inline-bg-color: var(--el-color-primary-light-9);
  --md-theme-code-block-radius: var(--el-border-radius-base);
  padding: 0;
  color: var(--el-text-color-regular);
  font-size: 15px;
  line-height: 1.9;
  overflow-wrap: break-word;
  word-break: normal;
}

.article-body :deep(.md-editor-preview p) {
  margin: 14px 0;
  line-height: inherit;
}

.article-body :deep(.md-editor-preview > p:first-child) {
  font-size: 16px;
}

.article-body :deep(.md-editor-preview h2) {
  margin: 30px 0 10px;
  color: var(--el-text-color-primary);
  font-size: 20px;
  font-weight: 600;
}

.article-body :deep(.md-editor-preview ol) {
  margin: 12px 0;
  padding-inline-start: 22px;
  list-style: decimal;
}

.article-body :deep(.md-editor-preview ul) {
  margin: 12px 0;
  padding-inline-start: 22px;
  list-style: disc;
}

.article-body :deep(.md-editor-preview li) {
  margin: 6px 0;
  line-height: 1.9;
}

.article-body :deep(.md-editor-preview blockquote) {
  margin: 20px 0;
  padding: 14px 18px;
  border-radius: 0 var(--el-border-radius-base) var(--el-border-radius-base) 0;
  line-height: 1.9;
}

.article-body :deep(.md-editor-preview blockquote p) {
  margin: 0;
}

.article-body :deep(.md-editor-preview .md-editor-code pre code) {
  padding: 16px 18px;
  font-size: 13px;
  line-height: 1.7;
}

/* 代码块头部在 md-editor-v3 中默认 sticky(top: 0) 且 z-index: 10000。
   正文预览容器改为 overflow: visible 后，它会相对视口吸顶，层级高过顶部导航（z-index: 100）而将其盖住；
   这里保留吸顶效果，但把吸附位置下移到导航栏下方，并降低层级确保永远处于导航之下 */
.article-body :deep(.md-editor-preview .md-editor-code .md-editor-code-head) {
  top: var(--app-header-height);
  z-index: 1;
}

.article-taxonomies {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.article-taxonomy-row {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 8px;
}

.article-taxonomy-label {
  color: var(--el-text-color-secondary);
  font-size: 13px;
}

.article-nav {
  display: flex;
  gap: 16px;
  margin-top: 24px;
}

.article-nav-item {
  display: flex;
  flex: 1;
  flex-direction: column;
  gap: 6px;
  min-width: 0;
  padding: 14px 16px;
  border: 1px solid var(--el-border-color-lighter);
  border-radius: var(--el-border-radius-base);
  transition:
    border-color var(--el-transition-duration-fast),
    background-color var(--el-transition-duration-fast);
}

.article-nav-item:hover {
  border-color: var(--el-color-primary-light-5);
  background-color: var(--el-color-primary-light-9);
}

.article-nav-item--next {
  text-align: right;
}

.article-nav-label {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  color: var(--el-text-color-secondary);
  font-size: 12px;
}

.article-nav-item--next .article-nav-label {
  justify-content: flex-end;
}

.article-nav-title {
  overflow: hidden;
  color: var(--el-text-color-regular);
  font-size: 14px;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.comment-editor {
  display: flex;
  gap: 14px;
}

.comment-editor-main {
  min-width: 0;
  flex: 1;
}

.comment-editor-actions {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-top: 10px;
}

.comment-hint {
  color: var(--el-text-color-placeholder);
  font-size: 12px;
}

.comment-list {
  display: flex;
  flex-direction: column;
}

.comment-item {
  display: flex;
  gap: 14px;
  padding: 16px 0;
  border-top: 1px solid var(--el-border-color-lighter);
}

.comment-item:first-child {
  padding-top: 0;
  border-top: none;
}

.comment-avatar {
  flex-shrink: 0;
  color: var(--el-color-white);
  font-size: 14px;
}

.comment-main {
  min-width: 0;
  flex: 1;
}

.comment-meta {
  display: flex;
  align-items: center;
  gap: 10px;
}

.comment-meta strong {
  font-size: 14px;
  font-weight: 600;
}

.comment-meta span {
  color: var(--el-text-color-secondary);
  font-size: 12px;
}

.comment-content {
  margin: 8px 0 4px;
  color: var(--el-text-color-regular);
  font-size: 14px;
  line-height: 1.8;
}

@media (max-width: 768px) {
  .article-card :deep(.el-card__body) {
    padding: 12px 16px 18px;
  }

  .article-title {
    font-size: 24px;
  }

  .article-nav {
    flex-direction: column;
  }

  .comment-editor > :first-child {
    display: none;
  }

  .comment-editor-actions {
    flex-wrap: wrap;
  }
}
</style>
