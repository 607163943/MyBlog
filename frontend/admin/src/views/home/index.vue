<script setup>
import dayjs from 'dayjs'
import { ref, onMounted, onUnmounted } from 'vue'
import * as echarts from 'echarts'

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

// 初始化趋势图
const initTrendChart = () => {
  const chart = echarts.init(document.getElementById('trend-chart'))
  chart.setOption({
    grid: {
      left: 0,
      right: 0,
      top: 0,
      bottom: 0,
      containLabel: false // 注意：false 时轴文字可能会被裁掉
    },
    xAxis: {
      type: 'category',
      data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
    },
    yAxis: {
      type: 'value'
    },
    tooltip: {
      trigger: 'axis'
    },
    series: [
      {
        data: [150, 230, 224, 218, 135, 147, 260],
        type: 'line'
      }
    ]
  })
}

// 初始化占比图
const initRatioChart = () => {
  const chart = echarts.init(document.getElementById('ratio-chart'))
  chart.setOption({
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: 'Access From',
        type: 'pie',
        radius: '72%',
        data: [
          { value: 1048, name: 'Search Engine' },
          { value: 735, name: 'Direct' },
          { value: 580, name: 'Email' },
          { value: 484, name: 'Union Ads' },
          { value: 300, name: 'Video Ads' }
        ],
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

onMounted(() => {
  startTimeUpdate()
  initTrendChart()
  initRatioChart()
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
          <a-statistic title="文章总数" :value="112893" />
        </a-col>
        <a-col :span="4" :offset="1">
          <a-statistic title="已发布" :value="112893" />
        </a-col>
        <a-col :span="4" :offset="1">
          <a-statistic title="草稿" :value="112893" />
        </a-col>
        <a-col :span="4" :offset="1">
          <a-statistic title="分类总数" :value="112893" />
        </a-col>
        <a-col :span="4" :offset="1">
          <a-statistic title="标签总数" :value="112893" />
        </a-col>
      </a-row>
    </a-card>
  </div>

  <div class="chart-cards">
    <a-card title="趋势图" style="width: 48%">
      <div id="trend-chart" style="width: 100%; height: 300px"></div>
    </a-card>
    <a-card title="占比图" style="width: 48%">
      <div id="ratio-chart" style="width: 100%; height: 300px"></div>
    </a-card>
  </div>

  <div class="system-cards">
    <a-card title="系统卡片">
      <a-row>
        <a-col :span="2"> 运行指标： </a-col>
        <a-col :span="4"> CPU 12% </a-col>
        <a-col :span="4"> Memory 43% </a-col>
      </a-row>
      <a-row style="margin-top: 12px">
        <a-col :span="2"> 服务状态： </a-col>
        <a-col :span="4">
          MySQL
          <a-tag color="green"> 正常 </a-tag>
        </a-col>
        <a-col :span="4">
          Redis
          <a-tag color="red"> 异常 </a-tag>
        </a-col>
      </a-row>
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

.system-cards {
  margin-top: 20px;
}
</style>
