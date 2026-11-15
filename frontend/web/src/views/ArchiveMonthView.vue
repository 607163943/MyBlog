<script setup lang="ts">
import { computed, onMounted, watch } from 'vue'
import { RouterLink, useRoute } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'

import ArchivePostList from '@/components/ArchivePostList.vue'
import BlogSidebar from '@/components/BlogSidebar.vue'
import { useArchiveStore } from '@/stores/archive'
import { parseMonth, parsePositiveInt } from '@/util/route'
import { setPageTitle } from '@/util/title'

const route = useRoute()
const archiveStore = useArchiveStore()

const year = computed(() => parsePositiveInt(route.params.year))
const month = computed(() => parseMonth(route.params.month))

const monthPosts = computed(() => archiveStore.monthArticles)

const monthTitle = computed(() =>
  year.value && month.value ? `${year.value}年${month.value}月` : '归档',
)

function load() {
  if (year.value && month.value) {
    archiveStore.fetchMonth(year.value, month.value)
    return
  }
  archiveStore.monthArticles = []
  archiveStore.monthError = '归档月份不存在'
}

onMounted(load)

watch(() => route.fullPath, load)

watch(
  monthTitle,
  (title) => {
    setPageTitle(title)
  },
  { immediate: true },
)
</script>

<template>
  <div class="page-container page-container--wide">
    <div class="layout-with-aside">
      <div v-loading="archiveStore.monthLoading" class="layout-main">
        <el-card class="head-only-card" shadow="never" body-style="display: none">
          <template #header>
            <div class="card-heading">
              <div class="card-heading-title">
                <span class="heading-accent" />
                <h2>归档</h2>
              </div>
              <div class="heading-actions">
                <span class="card-heading-meta">
                  共
                  <strong>{{ monthPosts.length }}</strong>
                  篇博客
                </span>
                <RouterLink class="back-link" to="/archive">
                  <el-icon><ArrowLeft /></el-icon>
                  返回归档
                </RouterLink>
              </div>
            </div>
          </template>
        </el-card>

        <section class="archive-group">
          <h2 class="archive-year">{{ monthTitle }}</h2>

          <el-card v-if="monthPosts.length" class="archive-card" shadow="never">
            <ArchivePostList :posts="monthPosts" />
          </el-card>

          <el-card v-else-if="!archiveStore.monthLoading" shadow="never">
            <el-empty :description="archiveStore.monthError || '该月份暂无归档文章'" />
          </el-card>
        </section>
      </div>

      <aside class="home-aside">
        <BlogSidebar />
      </aside>
    </div>
  </div>
</template>

<style scoped>
.archive-group {
  display: flex;
  flex-direction: column;
}

.archive-year {
  margin: 6px 0 14px;
  color: var(--el-text-color-primary);
  font-size: 22px;
  font-weight: 600;
  letter-spacing: 1px;
  text-align: center;
}

.heading-actions {
  display: flex;
  align-items: center;
  gap: 16px;
}

.back-link {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  color: var(--el-color-primary);
  font-size: 13px;
  transition: color var(--el-transition-duration-fast);
}

.back-link:hover {
  color: var(--el-color-primary-light-3);
}
</style>
