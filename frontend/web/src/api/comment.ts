import request from '@/util/request'
import type { CommentSubmitDTO, CommentVO } from '@/types/comment'

/** 查询文章下已通过的评论 */
export function getArticleComments(articleId: number) {
  return request<CommentVO[]>({
    url: `/web/article/${articleId}/comments`,
    method: 'get',
  })
}

/** 提交评论（默认待审核） */
export function submitArticleComment(articleId: number, data: CommentSubmitDTO) {
  return request<void>({
    url: `/web/article/${articleId}/comments`,
    method: 'post',
    data,
  })
}
