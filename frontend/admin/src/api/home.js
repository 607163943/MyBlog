import request from '@/utils/request'

// 获取首页统计数据
export const homeGetTotal = () => {
  return request.get('/home/total')
}
