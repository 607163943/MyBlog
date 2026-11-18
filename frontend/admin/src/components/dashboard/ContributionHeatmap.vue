<script setup lang="ts">
import { computed } from 'vue'
import { CalendarHeatmap } from 'vue3-calendar-heatmap'
import type { CalendarItem, Locale, Value } from 'vue3-calendar-heatmap'
import 'vue3-calendar-heatmap/dist/style.css'
import { useDashboardStore } from '@/stores/dashboard'

/** GitHub 贡献图色阶：无贡献 + 四级绿色 */
const GITHUB_COLORS = ['#ebedf0', '#9be9a8', '#40c463', '#30a14e', '#216e39']

/**
 * 组件按 0-5 计算颜色索引：0 为无数据，1 对应 count <= 0（不会出现），
 * 2-5 依次对应 GitHub 的四级绿色，因此这里在索引 1 处补一个不会被用到的颜色
 */
const HEATMAP_COLORS = ['#ebedf0', '#9be9a8', '#9be9a8', '#40c463', '#30a14e', '#216e39']

/** 与 GitHub 贡献图一致的月份 / 星期标签与图例文案 */
const GITHUB_LOCALE: Partial<Locale> = {
  months: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
  days: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
  less: 'Less',
  more: 'More',
}

const store = useDashboardStore()

const endDate = new Date()

/** 热力图数据：无记录的日期由组件按 0 处理；日期按本地时间解析，避免时区偏移 */
const values = computed<Value[]>(() =>
  store.contributions
    .filter((item) => item.contributionCount > 0)
    .map((item) => ({
      date: new Date(`${item.contributionDate}T00:00:00`),
      count: item.contributionCount,
    })),
)

/**
 * 手动指定颜色上限，避免只有 1 次贡献的日期占满最深层级；
 * 下限取 4，使 1-4 次贡献正好落在四级绿色上
 */
const maxCount = computed(() =>
  Math.max(4, ...store.contributions.map((item) => item.contributionCount)),
)

/** 中文日期，用于 tooltip */
function formatDate(date: Date) {
  return `${date.getFullYear()}年${date.getMonth() + 1}月${date.getDate()}日`
}

/** 日历格悬浮提示 */
function formatTooltip(item: CalendarItem) {
  if (!item.count) {
    return `${formatDate(item.date)}<br/>无贡献`
  }
  return `${formatDate(item.date)}<br/><b>${item.count} 次贡献</b>`
}
</script>

<template>
  <el-card shadow="never" class="contribution-card">
    <div class="contribution-header">
      <span class="contribution-title">文章创作贡献</span>
      <span class="contribution-sub">近一年</span>
    </div>
    <div v-loading="store.contributionLoading" class="heatmap-wrapper">
      <div class="heatmap-scroll">
        <CalendarHeatmap
          :values="values"
          :end-date="endDate"
          :max="maxCount"
          :range-color="HEATMAP_COLORS"
          :locale="GITHUB_LOCALE"
          :round="2"
          no-data-text="当天无贡献"
          :tooltip-formatter="formatTooltip"
        >
          <template #vch__legend-left>
            <span class="legend-note">新增或编辑保存文章均计 1 次贡献</span>
          </template>
          <template #legend-right>
            <div class="heatmap-legend">
              <span>Less</span>
              <span
                v-for="color in GITHUB_COLORS"
                :key="color"
                class="legend-block"
                :style="{ backgroundColor: color }"
              />
              <span>More</span>
            </div>
          </template>
        </CalendarHeatmap>
      </div>
    </div>
  </el-card>
</template>

<style scoped lang="scss">
.contribution-card {
  :deep(.el-card__body) {
    padding: 20px;
  }
}

.contribution-header {
  display: flex;
  align-items: baseline;
  gap: 8px;
}

.contribution-title {
  font-size: 15px;
  font-weight: 600;
  color: var(--el-text-color-primary);
}

.contribution-sub {
  font-size: 13px;
  color: var(--el-text-color-secondary);
}

.heatmap-wrapper {
  margin-top: 12px;
}

.heatmap-scroll {
  overflow-x: auto;

  :deep(.vch__container) {
    min-width: 680px;
  }
}

.legend-note {
  font-size: 12px;
  color: var(--el-text-color-secondary);
}

.heatmap-legend {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  color: var(--el-text-color-secondary);

  .legend-block {
    width: 10px;
    height: 10px;
    border-radius: 2px;
  }
}
</style>
