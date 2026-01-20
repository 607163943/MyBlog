import request from '@/utils/request'

// 分页查询文章
export const articlePageQueryService = (params) => {
  return request.get('/article', { params })
}

// 获取最新发布文章Top5
export const articleNewTop5Service = () => {
  return request.get('/article/new/top5')
}

// 根据id查询文章详情
export const articleByIdService = (id) => {
  return request.get(`/article/view/${id}`)
}
