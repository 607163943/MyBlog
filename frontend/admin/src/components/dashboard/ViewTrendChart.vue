<script setup lang="ts">
import { computed, onBeforeUnmount, onMounted, ref, watch } from 'vue'
import { LineChart } from 'echarts/charts'
import { GridComponent, TooltipComponent } from 'echarts/components'
import * as echarts from 'echarts/core'
import type { EChartsCoreOption, EChartsType } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { useDashboardStore } from '@/stores/dashboard'
import { formatNumber } from '@/util/format'

echarts.use([LineChart, GridComponent, TooltipComponent, CanvasRenderer])

/** 图表主色与 Element Plus 默认主题保持一致 */
const PRIMARY_COLOR = '#409EFF'

const store = useDashboardStore()
const chartRef = ref<HTMLDivElement>()

let chart: EChartsType | null = null
let resizeObserver: ResizeObserver | null = null

/** 过滤掉首个没有前一日快照的数据点，生成 [日期, 浏览量] 序列 */
const trendPoints = computed<[string, number][]>(() =>
  store.viewTrend
    .filter((item): item is { snapshotDate: string; dailyViewCount: number } => {
      return item.dailyViewCount !== null
    })
    .map((item) => [item.snapshotDate, item.dailyViewCount]),
)

/** 构建折线图配置：平滑曲线 + 曲线下方渐变覆盖 */
function buildOption(): EChartsCoreOption {
  return {
    grid: { top: 16, right: 16, bottom: 8, left: 8, containLabel: true },
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'line', lineStyle: { color: '#C0C4CC' } },
      formatter: (params: unknown) => {
        const items = Array.isArray(params) ? params : [params]
        const item = items[0] as
          | { marker?: string; seriesName?: string; value?: [string, number] }
          | undefined
        if (!item?.value) {
          return ''
        }
        const [date, value] = item.value
        return `${date}<br/>${item.marker ?? ''}${item.seriesName ?? ''} ${formatNumber(value)}`
      },
    },
    xAxis: {
      type: 'time',
      boundaryGap: false,
      axisLine: { lineStyle: { color: '#DCDFE6' } },
      axisTick: { show: false },
      axisLabel: {
        color: '#909399',
        hideOverlap: true,
        margin: 12,
        formatter: (value: number) => {
          const date = new Date(value)
          return `${date.getMonth() + 1}/${date.getDate()}`
        },
      },
    },
    yAxis: {
      type: 'value',
      splitNumber: 2,
      axisLine: { show: false },
      axisTick: { show: false },
      axisLabel: {
        color: '#909399',
        margin: 14,
        formatter: (value: number) => formatNumber(value),
      },
      splitLine: { lineStyle: { color: '#EBEEF5' } },
    },
    series: [
      {
        name: '浏览量',
        type: 'line',
        smooth: true,
        symbol: 'none',
        data: trendPoints.value,
        lineStyle: { width: 2, color: PRIMARY_COLOR },
        itemStyle: { color: PRIMARY_COLOR },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(64, 158, 255, 0.35)' },
            { offset: 1, color: 'rgba(64, 158, 255, 0.2)' },
          ]),
        },
      },
    ],
  }
}

/** 渲染图表 */
function renderChart() {
  chart?.setOption(buildOption(), { notMerge: true })
}

onMounted(() => {
  if (!chartRef.value) {
    return
  }
  chart = echarts.init(chartRef.value)
  renderChart()
  resizeObserver = new ResizeObserver(() => chart?.resize())
  resizeObserver.observe(chartRef.value)
})

watch(trendPoints, renderChart)

onBeforeUnmount(() => {
  resizeObserver?.disconnect()
  resizeObserver = null
  chart?.dispose()
  chart = null
})
</script>

<template>
  <el-card shadow="never" class="trend-card">
    <div class="trend-header">
      <span class="trend-title">文章每日浏览量</span>
      <span class="trend-sub">近 30 天</span>
    </div>
    <div v-loading="store.viewTrendLoading" class="chart-wrapper">
      <div ref="chartRef" class="trend-chart"></div>
      <span v-if="!store.viewTrendLoading && trendPoints.length === 0" class="chart-empty">
        暂无数据
      </span>
    </div>
  </el-card>
</template>

<style scoped lang="scss">
.trend-card {
  :deep(.el-card__body) {
    padding: 20px;
  }
}

.trend-header {
  display: flex;
  align-items: baseline;
  gap: 8px;
}

.trend-title {
  font-size: 15px;
  font-weight: 600;
  color: var(--el-text-color-primary);
}

.trend-sub {
  font-size: 13px;
  color: var(--el-text-color-secondary);
}

.chart-wrapper {
  position: relative;
  margin-top: 12px;
}

.trend-chart {
  height: 320px;
}

.chart-empty {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  color: var(--el-text-color-secondary);
}
</style>
