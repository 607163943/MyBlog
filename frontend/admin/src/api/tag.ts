import { request } from '@/util/request'
import type { TagDTO, TagPageQuery, TagVO } from '@/types/tag'
import type { PageVO } from '@/types/common'

/** 分页查询标签 */
export function getTagPage(params: TagPageQuery) {
  return request<PageVO<TagVO>>({
    url: '/tag/page',
    method: 'get',
    params,
  })
}

/** 根据 ID 查询标签 */
export function getTagById(id: number) {
  return request<TagVO>({
    url: `/tag/${id}`,
    method: 'get',
  })
}

/** 新增标签 */
export function addTag(data: TagDTO) {
  return request<void>({
    url: '/tag',
    method: 'post',
    data,
  })
}

/** 修改标签 */
export function updateTag(data: TagDTO) {
  return request<void>({
    url: '/tag',
    method: 'put',
    data,
  })
}

/** 删除单个标签 */
export function deleteTag(id: number) {
  return request<void>({
    url: `/tag/${id}`,
    method: 'delete',
  })
}

/** 批量删除标签 */
export function deleteTags(ids: number[]) {
  return request<void>({
    url: '/tag/batch',
    method: 'delete',
    data: ids,
  })
}
