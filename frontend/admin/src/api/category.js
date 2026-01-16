import request from '@/utils/request'

// 分页查询分类
export const categoryPageQueryService = (params) => {
  return request.get('/category', { params })
}

// 根据id获取分类
export const categoryByIdService = (id) => {
  return request.get(`/category/${id}`)
}

// 获取所有激活中分类
export const categoryAllActiveService = () => {
  return request.get('/category/all/active')
}

// 新增分类
export const categoryAddService = (data) => {
  return request.post('/category', data)
}

// 更新分类
export const categoryUpdateService = (data) => {
  return request.put('/category', data)
}

// 修改分类状态
export const categoryUpdateStatueService = (id) => {
  return request.patch(`/category/${id}/status`)
}

// 删除分类
export const categoryDeleteService = (id) => {
  return request.delete(`/category/${id}`)
}

// 根据id集合批量删除分类
export const categoryBatchDeleteService = (ids) => {
  return request.delete('/category', { params: { ids } })
}
