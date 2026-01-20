import request from '@/utils/request'

// 获取热门分类
export const categoryHotTop5Service = () => {
  return request.get('/category/hot/top5')
}

// 分页查询分类
export const categoryPageQueryService = (params) => {
  return request.get('/category', { params })
}
