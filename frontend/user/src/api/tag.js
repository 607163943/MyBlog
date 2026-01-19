import request from '@/utils/request'

// 获取标签墙数据
export const tagWallService = () => {
  return request.get('/tag/wall')
}
