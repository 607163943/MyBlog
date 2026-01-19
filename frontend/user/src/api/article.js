import request from '@/utils/request'

// 分页查询文章
export const articlePageQueryService = (params) => {
  return request.get('/article', { params })
}

// 获取最新发布文章Top5
export const articleNewTop5Service = () => {
  return request.get('/article/new/top5')
}
