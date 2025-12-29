import request from '@/utils/request'

// 获取配置列表
export const configGetListService = () => {
  return request.get('/config/list')
}
