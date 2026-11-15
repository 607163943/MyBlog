<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { RouterLink } from 'vue-router'

import BlogSidebar from '@/components/BlogSidebar.vue'
import { useArchiveStore } from '@/stores/archive'
import type { ArchiveMonthVO, ArchiveYearVO } from '@/types/archive'

/** 月份网格每行的列数 */
const MONTH_COLUMNS = 4

const archiveStore = useArchiveStore()

const totalPosts = computed(() =>
  archiveStore.years.reduce((sum, group) => sum + yearTotal(group), 0),
)

const yearRange = computed(() => {
  const latest = archiveStore.years[0]?.year
  const earliest = archiveStore.years[archiveStore.years.length - 1]?.year
  if (!latest || !earliest) return ''
  return latest === earliest ? String(latest) : `${earliest}—${latest}`
})

function yearTotal(group: ArchiveYearVO) {
  return group.months.reduce((sum, month) => sum + month.articleCount, 0)
}

/** 按固定列数把月份拆成行，行与行之间才能画出通栏分割线 */
function monthRows(months: ArchiveMonthVO[]) {
  const rows: ArchiveMonthVO[][] = []
  for (let index = 0; index < months.length; index += MONTH_COLUMNS) {
    rows.push(months.slice(index, index + MONTH_COLUMNS))
  }
  return rows
}

onMounted(() => {
  archiveStore.fetchOverview()
})
</script>

<template>
  <div class="page-container page-container--wide">
    <div class="layout-with-aside">
      <div v-loading="archiveStore.loading" class="layout-main">
        <el-card class="archive-view-card" shadow="never">
          <header class="archive-head">
            <div class="archive-title-row">
              <h1 class="archive-title">归档</h1>
              <p class="archive-hint">点击月份进入对应文章列表</p>
            </div>
            <p class="archive-subtitle">共 {{ totalPosts }} 篇文章 · {{ yearRange }}</p>
          </header>

          <section v-for="group in archiveStore.years" :key="group.year" class="archive-group">
            <div class="year-meta">
              <span class="year-label">{{ group.year }} 年</span>
              <span class="year-count">{{ yearTotal(group) }} 篇文章</span>
            </div>

            <div class="month-grid">
              <div
                v-for="(row, rowIndex) in monthRows(group.months)"
                :key="rowIndex"
                class="month-row"
              >
                <RouterLink
                  v-for="month in row"
                  :key="month.month"
                  class="month-item"
                  :to="`/archive/${group.year}/${month.month}`"
                >
                  <span class="month-label">{{ month.month }}月</span>
                  <span class="month-count">{{ month.articleCount }} 篇</span>
                </RouterLink>
              </div>
            </div>
          </section>

          <el-empty
            v-if="!archiveStore.loading && !archiveStore.years.length"
            description="暂无归档文章"
          />
        </el-card>
      </div>

      <aside class="home-aside">
        <BlogSidebar />
      </aside>
    </div>
  </div>
</template>

<style scoped>
.archive-view-card :deep(.el-card__body) {
  padding: 28px 32px;
}

.archive-head {
  padding-bottom: 22px;
  border-bottom: 1px solid var(--el-border-color-lighter);
}

.archive-title-row {
  display: flex;
  flex-wrap: wrap;
  align-items: baseline;
  gap: 4px 14px;
}

.archive-title {
  color: var(--el-text-color-primary);
  font-size: 28px;
  font-weight: 700;
  line-height: 1.2;
}

.archive-subtitle {
  margin-top: 10px;
  color: var(--el-text-color-secondary);
  font-size: 14px;
}

/* 每个年份分组：左侧月份网格 + 右侧年份栏，年份栏垂直居中 */
.archive-group {
  display: grid;
  grid-template-areas: 'months year';
  grid-template-columns: minmax(0, 1fr) 128px;
  align-items: center;
  gap: 0 24px;
  padding: 22px 0;
}

.archive-group + .archive-group {
  border-top: 1px solid var(--el-border-color-lighter);
}

/* 右侧年份栏：固定宽度右对齐，垂直居中于该年月份网格 */
.year-meta {
  display: flex;
  grid-area: year;
  flex-direction: column;
  align-items: flex-end;
  gap: 2px;
  text-align: right;
}

.year-label {
  color: var(--el-text-color-primary);
  font-size: 20px;
  font-weight: 600;
  letter-spacing: 0.5px;
  line-height: 1.3;
}

.year-count {
  color: var(--el-text-color-secondary);
  font-size: 13px;
  line-height: 1.4;
}

.month-grid {
  display: block;
  grid-area: months;
  min-width: 0;
}

.month-row {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
}

/* 行与行之间的通栏分割线 */
.month-row + .month-row {
  border-top: 1px solid var(--el-border-color-light);
}

.month-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2px;
  padding: 9px 6px;
  border-radius: var(--el-border-radius-base);
  text-align: center;
  transition: background-color var(--el-transition-duration-fast);
}

/* 列与列之间的分割线，避免末行空列出现多余竖线 */
.month-row .month-item:not(:first-child) {
  border-left: 1px solid var(--el-border-color-light);
}

.month-item:hover,
.month-item:focus-visible {
  background-color: var(--el-color-primary-light-9);
}

.month-label {
  color: var(--el-text-color-primary);
  font-size: 16px;
  font-weight: 600;
  line-height: 1.3;
  transition: color var(--el-transition-duration-fast);
}

.month-item:hover .month-label,
.month-item:focus-visible .month-label {
  color: var(--el-color-primary);
}

.month-count {
  color: var(--el-text-color-secondary);
  font-size: 12px;
}

.archive-hint {
  color: var(--el-text-color-secondary);
  font-size: 13px;
  line-height: 1.4;
}

@media (max-width: 768px) {
  .archive-view-card :deep(.el-card__body) {
    padding: 20px 16px;
  }

  .archive-title {
    font-size: 24px;
  }

  /* 窄屏：年份栏降级为右对齐的组头，月份网格排在其下 */
  .archive-group {
    grid-template-areas:
      'year'
      'months';
    grid-template-columns: minmax(0, 1fr);
    gap: 12px;
    padding: 18px 0;
  }

  .year-meta {
    flex-direction: row;
    align-items: baseline;
    justify-content: flex-end;
    gap: 8px;
  }

  .year-count::before {
    content: '·';
    margin-right: 8px;
  }

  /* 两列时行容器不参与布局，让月份直接排进两列网格 */
  .month-grid {
    display: grid;
    grid-template-columns: repeat(2, minmax(0, 1fr));
    gap: 0;
  }

  .month-row {
    display: contents;
  }

  /* 两列时只在每对月份的中间保留竖线 */
  .month-row .month-item:nth-child(odd) {
    border-left: 0;
  }
}
</style>
