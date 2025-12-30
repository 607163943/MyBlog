import request from '@/utils/request'

// 获取博客首页数据
export const homeGetDataService = () => {
  return request.get('/home')
}
