import request from '@/utils/request'

// 分页查询字典
export const dictPageQueryService = (params) => {
  return request.get('/dict', { params })
}

// 根据id获取字典
export const dictByIdService = (id) => {
  return request.get(`/dict/${id}`)
}

// 获取所有字典
export const dictAllService = () => {
  return request.get('/dict/all')
}

// 新增字典
export const dictAddService = (data) => {
  return request.post('/dict', data)
}

// 更新字典
export const dictUpdateService = (data) => {
  return request.put('/dict', data)
}

// 修改字典状态
export const dictUpdateStatueService = (id) => {
  return request.patch(`/dict/${id}/status`)
}

// 删除字典
export const dictDeleteService = (id) => {
  return request.delete(`/dict/${id}`)
}

// 根据id集合批量删除字典
export const dictBatchDeleteService = (ids) => {
  return request.delete('/dict', { params: { ids } })
}
