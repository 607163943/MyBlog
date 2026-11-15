<script setup lang="ts">
import { ref, watch } from 'vue'
import { RouterLink } from 'vue-router'
import { Picture } from '@element-plus/icons-vue'

import type { ArticleVO } from '@/types/article'
import HighlightText from './HighlightText.vue'
import PostMeta from './PostMeta.vue'

const {
  post,
  keyword = '',
  activeTagId,
} = defineProps<{
  post: ArticleVO
  keyword?: string
  activeTagId?: number
}>()

const coverFailed = ref(false)

watch(
  () => post.cover,
  () => {
    coverFailed.value = false
  },
)
</script>

<template>
  <article class="post-item" :class="{ 'has-cover': post.cover }">
    <div class="post-main">
      <h2 class="post-title">
        <RouterLink :to="`/post/${post.id}`">
          <HighlightText :text="post.title" :keyword="keyword" />
        </RouterLink>
      </h2>

      <PostMeta class="post-card-meta" :post="post" :active-tag-id="activeTagId" />

      <p class="post-excerpt">
        <HighlightText :text="post.description ?? ''" :keyword="keyword" />
      </p>
    </div>

    <RouterLink
      v-if="post.cover"
      class="post-cover"
      :to="`/post/${post.id}`"
      tabindex="-1"
      aria-hidden="true"
    >
      <img
        v-if="!coverFailed"
        class="post-cover-image"
        :src="post.cover"
        alt=""
        @error="coverFailed = true"
      />
      <el-icon v-else class="post-cover-icon" :size="30"><Picture /></el-icon>
    </RouterLink>
  </article>
</template>

<style scoped>
/* 独立文章卡片：与其他卡片共用统一圆角 + 轻微阴影 */
.post-item {
  display: grid;
  grid-template-columns: minmax(0, 1fr);
  padding: 26px 28px;
  border-radius: var(--app-card-radius);
  background-color: var(--el-bg-color);
  box-shadow:
    0 1px 2px rgba(0, 0, 0, 0.06),
    0 2px 6px rgba(0, 0, 0, 0.04);
  transition: box-shadow var(--app-card-hover-duration);
}

.post-item:hover {
  box-shadow: var(--app-card-hover-shadow);
}

/* 有封面时才分两列；无封面时简介占满整个卡片内区域 */
.post-item.has-cover {
  --post-cover-gap: 32px;
  grid-template-columns: minmax(0, 1fr) 210px;
  column-gap: var(--post-cover-gap);
}

.post-main {
  min-width: 0;
}

.post-title {
  color: var(--el-text-color-primary);
  font-size: 20px;
  font-weight: 600;
  line-height: 1.45;
}

.post-title a {
  transition: color var(--el-transition-duration-fast);
}

.post-title a:hover {
  color: var(--el-color-primary);
}

.post-card-meta {
  margin-top: 12px;
}

/* 固定两行简介区域，超出时才由 line-clamp 显示省略号 */
.post-excerpt {
  display: -webkit-box;
  overflow: hidden;
  min-height: 3.5em;
  margin: 10px 0 0;
  color: var(--app-text-color-body);
  line-height: 1.75;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
}

/* 简介右边缘延伸到距封面 10px：抵消列间距后进入间距区域 */
.post-item.has-cover .post-excerpt {
  margin-right: calc(10px - var(--post-cover-gap));
}

.post-cover {
  position: relative;
  display: flex;
  overflow: hidden;
  aspect-ratio: 16 / 10;
  align-self: start;
  align-items: center;
  justify-content: center;
  border-radius: 16px;
  background: linear-gradient(
    135deg,
    var(--el-color-primary-light-9),
    var(--el-color-primary-light-8)
  );
}

.post-cover::before {
  position: absolute;
  inset: 0;
  background-image: radial-gradient(var(--el-color-primary-light-7) 1.2px, transparent 1.2px);
  background-size: 18px 18px;
  content: '';
  opacity: 0.75;
}

.post-cover::after {
  position: absolute;
  top: -26px;
  right: -18px;
  width: 96px;
  height: 96px;
  border: 1px solid var(--el-color-primary-light-6);
  border-radius: 50%;
  content: '';
  opacity: 0.7;
}

.post-cover-image {
  position: absolute;
  z-index: 1;
  inset: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.post-cover-icon {
  position: relative;
  z-index: 1;
  color: var(--el-color-primary-light-5);
  transition:
    color var(--el-transition-duration-fast),
    transform var(--el-transition-duration-fast);
}

.post-cover:hover .post-cover-icon {
  color: var(--el-color-primary);
  transform: scale(1.08);
}

@media (max-width: 768px) {
  .post-item {
    padding: 20px 18px;
  }

  .post-item.has-cover {
    --post-cover-gap: 16px;
    grid-template-columns: minmax(0, 1fr) 122px;
  }

  .post-title {
    font-size: 18px;
  }

  .post-card-meta {
    margin-top: 10px;
  }

  .post-excerpt {
    margin: 8px 0 0;
    font-size: 13px;
  }

  .post-cover {
    aspect-ratio: 1 / 1;
  }

  .post-cover-icon {
    font-size: 22px;
  }
}

@media (max-width: 560px) {
  .post-item.has-cover {
    grid-template-columns: minmax(0, 1fr);
  }

  .post-cover {
    display: none;
  }

  .post-item.has-cover .post-excerpt {
    margin-right: 0;
  }
}
</style>
