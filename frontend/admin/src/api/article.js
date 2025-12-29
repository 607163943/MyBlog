import request from '@/utils/request'

// 文章编辑器初始化
export const articleEditInitService = () => {
  return request.get('/article/edit')
}
