<script setup>
import { chartKpiCardService } from '@/api/chart'
import { ref, shallowRef, onMounted } from 'vue'
import * as echarts from 'echarts'
import { chartTrendCardService } from '@/api/chart'

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
  articleStatusRatioData: [],
  categoryStatusRatioData: [],
  tagStatusRatioData: []
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

// 分类状态占比图
const categoryStatusRatioChart = shallowRef(null)
// 初始化分类状态占比图
const initCategoryStatusRatioChart = () => {
  categoryStatusRatioChart.value = echarts.init(document.getElementById('category-status-ratio'))
  categoryStatusRatioChart.value.setOption({
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '分类状态占比',
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

// 标签状态占比图
const tagStatusRatioChart = shallowRef(null)
// 初始化标签状态占比图
const initTagStatusRatioChart = () => {
  tagStatusRatioChart.value = echarts.init(document.getElementById('tag-status-ratio'))
  tagStatusRatioChart.value.setOption({
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '标签状态占比',
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
  trendData.value.categoryStatusRatioData = res.data.data.categoryStatusRatioData
  trendData.value.tagStatusRatioData = res.data.data.tagStatusRatioData

  articleStatusRatioChart.value.setOption({
    series: [
      {
        data: trendData.value.articleStatusRatioData
      }
    ]
  })
  categoryStatusRatioChart.value.setOption({
    series: [
      {
        data: trendData.value.categoryStatusRatioData
      }
    ]
  })
  tagStatusRatioChart.value.setOption({
    series: [
      {
        data: trendData.value.tagStatusRatioData
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

onMounted(() => {
  // 初始化图表
  initAddArticleTrendChart()
  initArticleStatusRatioChart()
  initCategoryStatusRatioChart()
  initTagStatusRatioChart()
  // 获取数据
  handlerCardChart()
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
    <div class="trend-chart-item">
      <a-card title="分类状态占比" style="width: 48%">
        <div id="category-status-ratio" style="width: 100%; height: 300px"></div>
      </a-card>
      <a-card title="标签状态占比" style="width: 48%">
        <div id="tag-status-ratio" style="width: 100%; height: 300px"></div>
      </a-card>
    </div>
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
</style>
