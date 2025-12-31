import request from '@/utils/request'

// 获取友链
export const linkFavoriteLinksService = () => {
  return request.get('/link')
}
