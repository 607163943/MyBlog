import { request } from '@/util/request'
import type { ArticleDTO, ArticlePageQuery, ArticleVO } from '@/types/article'
import type { PageVO } from '@/types/common'

/** 分页查询文章 */
export function getArticlePage(params: ArticlePageQuery) {
  return request<PageVO<ArticleVO>>({
    url: '/article/page',
    method: 'get',
    params: {
      ...params,
      status: params.status === '' ? undefined : params.status,
    },
  })
}

/** 根据 ID 查询文章 */
export function getArticleById(id: number) {
  return request<ArticleVO>({
    url: `/article/${id}`,
    method: 'get',
  })
}

/** 新增文章 */
export function addArticle(data: ArticleDTO) {
  return request<void>({
    url: '/article',
    method: 'post',
    data,
  })
}

/** 修改文章 */
export function updateArticle(data: ArticleDTO) {
  return request<void>({
    url: '/article',
    method: 'put',
    data,
  })
}

/** 删除单个文章 */
export function deleteArticle(id: number) {
  return request<void>({
    url: `/article/${id}`,
    method: 'delete',
  })
}

/** 批量删除文章 */
export function deleteArticles(ids: number[]) {
  return request<void>({
    url: '/article/batch',
    method: 'delete',
    data: ids,
  })
}
