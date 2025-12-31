import request from '@/utils/request'

// 搜索文章
export const searchArticleService = (keyword) => {
  return request.get(`/search/${keyword}`)
}
