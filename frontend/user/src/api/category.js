import request from '@/utils/request'

// 获取热门分类
export const categoryHotTop5Service = () => {
  return request.get('/category/hot/top5')
}
