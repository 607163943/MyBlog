import { request } from '@/util/request'

/** 分类返回实体 */
export interface CategoryVO {
  id: number
  name: string
  description: string | null
  sort: number
  createTime: string
  updateTime: string
}

/** 分类新增 / 修改参数 */
export interface CategoryDTO {
  id?: number
  name: string
  description?: string
  sort?: number
}

/** 分类分页查询参数 */
export interface CategoryPageQuery {
  name: string
  page: number
  pageSize: number
}

/** 通用分页返回结构 */
export interface PageVO<T> {
  records: T[]
  total: number
  current: number
  size: number
  pages: number
}

/** 分页查询分类 */
export function getCategoryPage(params: CategoryPageQuery) {
  return request<PageVO<CategoryVO>>({
    url: '/category/page',
    method: 'get',
    params,
  })
}

/** 根据 ID 查询分类 */
export function getCategoryById(id: number) {
  return request<CategoryVO>({
    url: `/category/${id}`,
    method: 'get',
  })
}

/** 新增分类 */
export function addCategory(data: CategoryDTO) {
  return request<void>({
    url: '/category',
    method: 'post',
    data,
  })
}

/** 修改分类 */
export function updateCategory(data: CategoryDTO) {
  return request<void>({
    url: '/category',
    method: 'put',
    data,
  })
}

/** 删除单个分类 */
export function deleteCategory(id: number) {
  return request<void>({
    url: `/category/${id}`,
    method: 'delete',
  })
}

/** 批量删除分类 */
export function deleteCategories(ids: number[]) {
  return request<void>({
    url: '/category/batch',
    method: 'delete',
    data: ids,
  })
}
