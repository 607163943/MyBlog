import request from '@/utils/request'

// 获取首页统计卡片数据
export const homeCardDataService = () => {
  return request.get('/home/card/data')
}

// 获取首页图表卡片数据
export const homeChartCardDataService = () => {
  return request.get('/home/card/chart')
}
