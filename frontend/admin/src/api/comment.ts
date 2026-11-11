import { request } from '@/util/request'
import type { CommentDTO, CommentPageQuery, CommentVO } from '@/types/comment'
import type { PageVO } from '@/types/common'

/** 分页查询评论 */
export function getCommentPage(params: CommentPageQuery) {
  return request<PageVO<CommentVO>>({
    url: '/comment/page',
    method: 'get',
    params: {
      ...params,
      status: params.status === '' ? undefined : params.status,
    },
  })
}

/** 根据 ID 查询评论 */
export function getCommentById(id: number) {
  return request<CommentVO>({
    url: `/comment/${id}`,
    method: 'get',
  })
}

/** 修改评论 */
export function updateComment(data: CommentDTO) {
  return request<void>({
    url: '/comment',
    method: 'put',
    data,
  })
}

/** 删除单个评论 */
export function deleteComment(id: number) {
  return request<void>({
    url: `/comment/${id}`,
    method: 'delete',
  })
}

/** 批量删除评论 */
export function deleteComments(ids: number[]) {
  return request<void>({
    url: '/comment/batch',
    method: 'delete',
    data: ids,
  })
}
