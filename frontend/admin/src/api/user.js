import request from '@/utils/request'

// 获取用户信息
export const userGetInfo = () => {
  return request.get('/user/info')
}
