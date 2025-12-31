import request from '@/utils/request'

// 查询分类所属文章
export const categoryArticleService = (categoryName) => {
  return request.get(`/category/${categoryName}`)
}
