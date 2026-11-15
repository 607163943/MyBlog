<script setup lang="ts">
import { computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'

import BlogSidebar from '@/components/BlogSidebar.vue'
import ListPagination from '@/components/ListPagination.vue'
import PostListItem from '@/components/PostListItem.vue'
import { useTagStore } from '@/stores/tag'
import { parsePositiveInt } from '@/util/route'
import { setPageTitle } from '@/util/title'

const route = useRoute()
const router = useRouter()
const tagStore = useTagStore()

const tagId = computed(() => parsePositiveInt(route.params.id))

function loadTag(page = 1) {
  const id = tagId.value
  if (!id) {
    tagStore.current = null
    tagStore.resetArticles()
    return
  }
  tagStore.fetchDetail(id)
  tagStore.fetchArticles(id, page)
}

function changePage(page: number) {
  const id = tagId.value
  if (id) {
    tagStore.fetchArticles(id, page)
  }
}

function goBack() {
  // 没有站内历史（直接打开详情页）时回到标签总览
  if (window.history.state?.back) {
    router.back()
    return
  }
  router.push({ name: 'tag' })
}

onMounted(() => loadTag(1))

watch(
  () => route.params.id,
  () => {
    tagStore.resetArticles()
    loadTag(1)
  },
)

watch(
  () => tagStore.current,
  (tag) => {
    if (tag) {
      setPageTitle(tag.name)
    }
  },
)
</script>

<template>
  <div class="page-container page-container--wide">
    <div class="layout-with-aside">
      <div v-loading="tagStore.currentLoading || tagStore.articlesLoading" class="layout-main">
        <template v-if="tagStore.current">
          <header class="detail-head">
            <el-button class="detail-back" text :icon="ArrowLeft" @click="goBack">返回</el-button>
            <h1 class="detail-title">{{ tagStore.current.name }}</h1>
          </header>

          <div v-if="tagStore.articles.length" class="post-list">
            <PostListItem
              v-for="post in tagStore.articles"
              :key="post.id"
              :post="post"
              :active-tag-id="tagId ?? undefined"
            />
          </div>
          <el-card v-else-if="!tagStore.articlesLoading" shadow="never">
            <el-empty description="该标签下暂无文章" />
          </el-card>

          <ListPagination
            v-if="tagStore.total > 0"
            :total="tagStore.total"
            :page="tagStore.page"
            :page-size="tagStore.pageSize"
            @update:page="changePage"
          />
        </template>

        <el-card v-else-if="!tagStore.currentLoading" shadow="never">
          <el-empty :description="tagStore.currentError || '标签不存在'" />
        </el-card>
      </div>

      <aside class="home-aside">
        <BlogSidebar />
      </aside>
    </div>
  </div>
</template>

<style scoped>
/* 页面标题区：无卡片背景，标题居中，返回按钮固定在左侧 */
.detail-head {
  position: relative;
  display: flex;
  min-height: 52px;
  align-items: center;
  justify-content: center;
  padding: 0 96px;
}

.detail-back {
  position: absolute;
  top: 50%;
  left: 0;
  transform: translateY(-50%);
}

/* 悬浮 / 按下只改变文字颜色，不出现背景块 */
.detail-back.el-button.is-text:hover,
.detail-back.el-button.is-text:active {
  background-color: transparent;
  color: var(--el-color-primary);
}

.detail-title {
  overflow-wrap: anywhere;
  color: var(--el-text-color-primary);
  font-size: 30px;
  font-weight: 700;
  letter-spacing: 0.08em;
  line-height: 1.3;
  text-align: center;
  text-transform: uppercase;
}

@media (max-width: 768px) {
  .detail-head {
    min-height: 44px;
    padding: 0 76px;
  }

  .detail-title {
    font-size: 22px;
  }
}
</style>
