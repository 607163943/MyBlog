import request from '@/utils/request'

// 获取文章详情
export const articleGetDetailService = (articleId) => {
  return request.get(`/article/${articleId}`)
}
