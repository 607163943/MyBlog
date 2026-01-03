import request from '@/utils/request'

// 分页查询字典内容
export const dictValuePageQueryService = (params) => {
  return request.get('/dict-value', { params })
}

// 根据id获取字典
export const dictValueByIdService = (id) => {
  return request.get(`/dict-value/${id}`)
}

// 获取所有字典
export const dictAllService = () => {
  return request.get('/dict-value/all')
}

// 新增字典
export const dictValueAddService = (data) => {
  return request.post('/dict-value', data)
}

// 更新字典
export const dictValueUpdateService = (data) => {
  return request.put('/dict-value', data)
}

// 修改字典状态
export const dictValueUpdateStatueService = (id) => {
  return request.patch(`/dict-value/${id}/status`)
}

// 删除字典
export const dictValueDeleteService = (id) => {
  return request.delete(`/dict-value/${id}`)
}

// 根据id集合批量删除字典
export const dictValueBatchDeleteService = (ids) => {
  return request.delete('/dict-value', { params: { ids } })
}
