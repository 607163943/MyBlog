<script setup>
import { ref, shallowRef, onMounted } from 'vue'
import * as echarts from 'echarts'
import dayjs from 'dayjs'
import {
  chartKpiCardService,
  chartTrendCardService,
  chartArticleActiveCalendarService
} from '@/api/chart'

// KPI卡片
const kpiData = ref({
  addArticleCount: 0,
  publishArticleCount: 0,
  offlineArticleCount: 0,
  viewArticleCount: 0,
  totalArticleCount: 0,
  draftArticleCount: 0,
  categoryCount: 0,
  tagCount: 0
})

const handlerKpiCardData = async () => {
  const res = await chartKpiCardService()
  kpiData.value = res.data.data
}

// 趋势分析
const trendData = ref({
  addArticleTrendDate: [],
  addArticleTrendData: [],
  articleStatusRatioData: []
})

// 新增文章趋势图
const addArticleTrendChart = shallowRef(null)
// 初始化新增趋势图
const initAddArticleTrendChart = () => {
  addArticleTrendChart.value = echarts.init(document.getElementById('add-article-trend-chart'))
  addArticleTrendChart.value.setOption({
    grid: {
      left: 0,
      right: 0,
      top: 0,
      bottom: 0,
      containLabel: false // 注意：false 时轴文字可能会被裁掉
    },
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: []
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '新增文章趋势',
        data: [],
        type: 'line'
      }
    ]
  })
}

// 文章状态占比图
const articleStatusRatioChart = shallowRef(null)
// 初始化文章状态占比图
const initArticleStatusRatioChart = () => {
  articleStatusRatioChart.value = echarts.init(document.getElementById('article-status-ratio'))
  articleStatusRatioChart.value.setOption({
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '文章状态占比',
        type: 'pie',
        radius: '72%',
        data: [],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  })
}

// 处理图表数据获取
const handlerCardChart = async () => {
  const res = await chartTrendCardService()
  trendData.value.articleStatusRatioData = res.data.data.articleStatusRatioData

  articleStatusRatioChart.value.setOption({
    series: [
      {
        data: trendData.value.articleStatusRatioData
      }
    ]
  })

  // 处理趋势图原始数据，将日期、数据分离
  const tempTrendData = res.data.data.addArticleTrendData
  trendData.value.addArticleTrendDate = tempTrendData.map((item) => item.date)
  trendData.value.addArticleTrendData = tempTrendData.map((item) => item.count)

  addArticleTrendChart.value.setOption({
    xAxis: {
      data: trendData.value.addArticleTrendDate
    },
    series: [
      {
        data: trendData.value.addArticleTrendData
      }
    ]
  })
}

// 文章创作活跃图
const calendarChart = shallowRef(null)
// 初始化文章创作活跃图
const initCalendarChart = () => {
  calendarChart.value = echarts.init(document.getElementById('calendar-chart'))
  calendarChart.value.setOption({
    backgroundColor: '#fff',
    tooltip: {
      formatter: (p) => {
        // p.value: [date, count]
        const [date, count] = p.value || []
        return `${count || 0} contributions<br/>${date || ''}`
      }
    },
    visualMap: {
      type: 'piecewise',
      orient: 'horizontal',
      right: 0,
      bottom: 0,
      itemGap: 6,
      itemWidth: 12,
      itemHeight: 12,
      text: ['More', 'Less'],
      textGap: 8,
      pieces: [
        { min: 0, max: 0, color: '#ebedf0' }, // GitHub 0
        { min: 1, max: 1, color: '#9be9a8' },
        { min: 2, max: 2, color: '#40c463' },
        { min: 3, max: 3, color: '#30a14e' },
        { min: 4, color: '#216e39' }
      ]
    },
    calendar: {
      top: 0,
      left: 40,
      right: 20,
      cellSize: ['auto', 20], // 方格大小（GitHub 更小更紧凑）
      range: dayjs().format('YYYY'),
      itemStyle: {
        color: '#fff', // “间距”底色（和背景保持一致）
        borderWidth: 2, // 留白间距的核心：边框越大，间距越明显
        borderColor: '#fff'
      },
      splitLine: { show: false },
      yearLabel: { show: false },
      monthLabel: {
        position: 'top',
        color: '#57606a',
        margin: 10
      },
      dayLabel: {
        firstDay: 1,
        color: '#57606a',
        margin: 10,
        formatter: (d) => (d === 'Mon' || d === 'Wed' || d === 'Fri' ? d : '')
      }
    },
    series: {
      type: 'heatmap',
      coordinateSystem: 'calendar',
      data: [],
      itemStyle: {
        borderWidth: 2, // 与 calendar.itemStyle 保持一致，避免叠加发灰
        borderColor: '#fff',
        borderRadius: 2 // GitHub 方格有轻微圆角
      },
      emphasis: {
        itemStyle: {
          borderWidth: 2,
          borderColor: '#fff'
        }
      }
    }
  })
}

// 处理日历热力图数据获取
const handlerArticleActiveCalendarData = async () => {
  const res = await chartArticleActiveCalendarService()
  calendarChart.value.setOption({
    series: [
      {
        data: res.data.data
      }
    ]
  })
}

onMounted(() => {
  // 初始化图表
  initAddArticleTrendChart()
  initArticleStatusRatioChart()
  initCalendarChart()
  // 获取数据
  handlerCardChart()
  handlerArticleActiveCalendarData()
})

handlerKpiCardData()
</script>

<template>
  <div class="kpi-card">
    <a-card title="KPI概览 最近7天">
      <a-row>
        <a-col :span="4">
          <a-statistic title="新增文章" :value="kpiData.addArticleCount" />
        </a-col>
        <a-col :span="4" :offset="1">
          <a-statistic title="发布文章" :value="kpiData.publishArticleCount" />
        </a-col>
        <a-col :span="4" :offset="1">
          <a-statistic title="总文章" :value="kpiData.totalArticleCount" />
        </a-col>
        <a-col :span="4" :offset="1">
          <a-statistic title="分类数" :value="kpiData.categoryCount" />
        </a-col>
        <a-col :span="4" :offset="1">
          <a-statistic title="标签数" :value="kpiData.tagCount" />
        </a-col>
      </a-row>
    </a-card>
  </div>

  <div class="trend-chart">
    <div class="trend-chart-item">
      <a-card title="新增文章趋势-最近7天" style="width: 48%">
        <div id="add-article-trend-chart" style="width: 100%; height: 300px"></div>
      </a-card>
      <a-card title="文章状态占比" style="width: 48%">
        <div id="article-status-ratio" style="width: 100%; height: 300px"></div>
      </a-card>
    </div>
  </div>

  <div class="calendar-card">
    <a-card title="文章创作活跃度">
      <div id="calendar-chart" style="width: 100%; height: 200px"></div>
    </a-card>
  </div>
</template>

<style lang="less" scoped>
.trend-chart {
  margin-top: 20px;
}
.trend-chart-item {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

.calendar-card {
  margin-top: 20px;
}
</style>
