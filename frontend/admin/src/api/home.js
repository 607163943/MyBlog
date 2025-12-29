import request from '@/utils/request'

// 获取首页统计数据
export const homeGetTotalService = () => {
  return request.get('/home/total')
}
