import request from '@/util/request'
import type { TagVO } from '@/types/tag'

/** 查询全部标签 */
export function getTagList() {
  return request<TagVO[]>({
    url: '/web/tag/list',
    method: 'get',
  })
}

/** 查询标签详情 */
export function getTagDetail(id: number) {
  return request<TagVO>({
    url: `/web/tag/${id}`,
    method: 'get',
  })
}

/** 按文章数查询热门标签 */
export function getTopTags(limit = 10) {
  return request<TagVO[]>({
    url: '/web/tag/top',
    method: 'get',
    params: { limit },
  })
}
