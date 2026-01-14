<script setup>
import dayjs from 'dayjs'
import { ref, onMounted, onUnmounted, shallowRef } from 'vue'
import * as echarts from 'echarts'
import { homeCardDataService, homeChartCardDataService } from '@/api/home'

const time = ref(dayjs().format('HH:mm:ss'))

let timer = null

const startTimeUpdate = () => {
  // 防止重复启动（保险起见）
  if (timer) return
  timer = setInterval(() => {
    time.value = dayjs().format('HH:mm:ss')
  }, 1000)
}

const stopTimeUpdate = () => {
  if (timer) {
    clearInterval(timer)
    timer = null
  }
}

// 统计卡片
const cardData = ref({
  articleCount: 0,
  publishArticleCount: 0,
  draftArticleCount: 0,
  categoryCount: 0,
  tagCount: 0
})

const handlerCardData = async () => {
  const res = await homeCardDataService()
  cardData.value = res.data.data
}

// 图表数据
const chartData = ref({
  trendDate: [],
  trendData: [],
  ratioData: []
})

// 初始化趋势图
const trendChart = shallowRef(null)
const initTrendChart = () => {
  trendChart.value = echarts.init(document.getElementById('trend-chart'))
  trendChart.value.setOption({
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

// 初始化占比图
const ratioChart = shallowRef(null)
const initRatioChart = () => {
  ratioChart.value = echarts.init(document.getElementById('ratio-chart'))
  ratioChart.value.setOption({
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '文章占比',
        type: 'pie',
        radius: '72%',
        data: chartData.value.ratioData,
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
  const res = await homeChartCardDataService()
  chartData.value.ratioData = res.data.data.ratioData
  ratioChart.value.setOption({
    series: [
      {
        data: chartData.value.ratioData
      }
    ]
  })

  // 处理趋势图原始数据，将日期、数据分离
  const tempTrendData = res.data.data.trendData
  chartData.value.trendDate = tempTrendData.map((item) => item.date)
  chartData.value.trendData = tempTrendData.map((item) => item.count)

  trendChart.value.setOption({
    xAxis: {
      data: chartData.value.trendDate
    },
    series: [
      {
        data: chartData.value.trendData
      }
    ]
  })
}

onMounted(() => {
  startTimeUpdate()
  // 图表初始化
  initTrendChart()
  initRatioChart()

  // 获取初始化数据
  handlerCardData()
  handlerCardChart()
})

onUnmounted(() => {
  stopTimeUpdate()
})
</script>

<template>
  <div class="welcome">
    <a-card title="欢迎卡片">
      <a-row>
        <a-col :span="6">
          <span>你好，管理员，欢迎使用博客管理系统</span>
        </a-col>
        <a-col :span="6">
          <span>今天：{{ dayjs().format('YYYY-MM-DD') }} {{ dayjs().format('dddd') }}</span>
        </a-col>
        <a-col :span="6">
          <span>当前时间：{{ time }}</span>
        </a-col>
      </a-row>
      <div class="quick-enter">
        <span>快捷入口：</span>
        <a-space>
          <a-button @click="$router.push('/admin/article/edit')">写文章</a-button>
          <a-button @click="$router.push('/admin/article')">文章管理</a-button>
          <a-button @click="$router.push('/admin/category')">分类管理</a-button>
          <a-button @click="$router.push('/admin/tag')">标签管理</a-button>
          <a-button @click="$router.push('/admin/profile')">个人中心</a-button>
        </a-space>
      </div>
    </a-card>
  </div>
  <div class="stat-cards">
    <a-card title="统计卡片">
      <a-row>
        <a-col :span="4">
          <a-statistic title="文章总数" :value="cardData.articleCount" />
        </a-col>
        <a-col :span="4" :offset="1">
          <a-statistic title="已发布" :value="cardData.publishArticleCount" />
        </a-col>
        <a-col :span="4" :offset="1">
          <a-statistic title="草稿" :value="cardData.draftArticleCount" />
        </a-col>
        <a-col :span="4" :offset="1">
          <a-statistic title="分类总数" :value="cardData.categoryCount" />
        </a-col>
        <a-col :span="4" :offset="1">
          <a-statistic title="标签总数" :value="cardData.tagCount" />
        </a-col>
      </a-row>
    </a-card>
  </div>
  <div class="chart-cards">
    <a-card title="趋势图-最近7天" style="width: 48%">
      <div id="trend-chart" style="width: 100%; height: 300px"></div>
    </a-card>
    <a-card title="分类统计Top10占比图" style="width: 48%">
      <div id="ratio-chart" style="width: 100%; height: 300px"></div>
    </a-card>
  </div>
</template>

<style lang="less" scoped>
.quick-enter {
  margin-top: 20px;
}

.stat-cards {
  margin-top: 20px;
}

.chart-cards {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}
</style>
