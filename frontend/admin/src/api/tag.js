import request from '@/utils/request'

// 分页查询标签
export const tagPageQueryService = (params) => {
  return request.get('/tag', { params })
}

// 根据id获取标签
export const tagByIdService = (id) => {
  return request.get(`/tag/${id}`)
}

// 获取所有标签
export const tagAllService = () => {
  return request.get('/tag/all')
}

// 新增标签
export const tagAddService = (data) => {
  return request.post('/tag', data)
}

// 更新标签
export const tagUpdateService = (data) => {
  return request.put('/tag', data)
}

// 修改标签状态
export const tagUpdateStatueService = (id) => {
  return request.patch(`/tag/${id}/status`)
}

// 删除标签
export const tagDeleteService = (id) => {
  return request.delete(`/tag/${id}`)
}

// 根据id集合批量删除标签
export const tagBatchDeleteService = (ids) => {
  return request.delete('/tag', { params: { ids } })
}
