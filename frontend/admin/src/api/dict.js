import request from '@/utils/request'

// 分页查询字典
export const dictPageQueryService = (params) => {
  return request.get('/dict', { params })
}

// 根据id获取字典
export const dictByIdService = (id) => {
  return request.get(`/dict/${id}`)
}

// 新增字典
export const dictAddService = (data) => {
  return request.post('/dict', data)
}

// 更新字典
export const dictUpdateService = (data) => {
  return request.put('/dict', data)
}
