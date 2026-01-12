import request from '@/utils/request'

// 获取KPI卡片图表
export const chartKpiCardService = () => {
  return request.get('/chart/card/kpi')
}

// 获取趋势卡片数据
export const chartTrendCardService = () => {
  return request.get('/chart/card/trend')
}
