import request from '@/utils/request'

// 查询标签所属文章
export const tagGetArticleService = (tagName) => {
  return request.get(`/tag/${tagName}`)
}
