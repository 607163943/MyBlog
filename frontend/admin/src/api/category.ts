import { request } from '@/util/request'
import type { CategoryDTO, CategoryPageQuery, CategoryVO } from '@/types/category'
import type { PageVO } from '@/types/common'

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
