import request from '@/util/request'
import type { CategoryVO } from '@/types/category'

/** 查询全部分类及每类热门文章 */
export function getCategoryList() {
  return request<CategoryVO[]>({
    url: '/web/category/list',
    method: 'get',
  })
}

/** 查询分类详情 */
export function getCategoryDetail(id: number) {
  return request<CategoryVO>({
    url: `/web/category/${id}`,
    method: 'get',
  })
}

/** 按文章数查询热门分类 */
export function getTopCategories(limit = 5) {
  return request<CategoryVO[]>({
    url: '/web/category/top',
    method: 'get',
    params: { limit },
  })
}
