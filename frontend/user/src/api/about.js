import request from '@/utils/request'

// 获取关于文章
export const aboutArticleService = () => {
  return request.get('/about')
}
