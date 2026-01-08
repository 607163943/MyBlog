import request from '@/utils/request'

// 文章编辑器初始化
export const articleEditInitService = () => {
  return request.get('/article/edit')
}

// 分页查询文章
export const articlePageQueryService = (params) => {
  return request.get('/article', { params })
}

// 根据id获取文章
export const articleByIdService = (id) => {
  return request.get(`/article/${id}`)
}

// 获取所有文章
export const articleAllService = () => {
  return request.get('/article/all')
}

// 新增文章
export const articleAddService = (data) => {
  return request.post('/article', data)
}

// 更新文章
export const articleUpdateService = (data) => {
  return request.put('/article', data)
}

// 修改文章状态
export const articleUpdateStatueService = (id) => {
  return request.patch(`/article/${id}/status`)
}

// 删除文章
export const articleDeleteService = (id) => {
  return request.delete(`/article/${id}`)
}

// 根据id集合批量删除文章
export const articleBatchDeleteService = (ids) => {
  return request.delete('/article', { params: { ids } })
}
