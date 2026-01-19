import request from '@/utils/request'

// 获取用户信息
export const userInfoService = () => {
  return request.get('/user/info')
}

// 统计用户创作数据卡片
export const userCountCardService = () => {
  return request.get('/user/chart/count')
}
