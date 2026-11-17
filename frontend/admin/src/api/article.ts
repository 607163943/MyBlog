import { request } from '@/util/request'
import type { ArticleDTO, ArticlePageQuery, ArticleSortVO, ArticleVO } from '@/types/article'
import type { PageVO } from '@/types/common'

/** 分页查询文章 */
export function getArticlePage(params: ArticlePageQuery) {
  return request<PageVO<ArticleVO>>({
    url: '/article/page',
    method: 'get',
    params: {
      ...params,
      status: params.status === '' ? undefined : params.status,
      categoryId: params.categoryId === '' ? undefined : params.categoryId,
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

/** 查询分类内已发布文章排序列表 */
export function getArticleSortList(categoryId: number) {
  return request<ArticleSortVO[]>({
    url: `/article/category/${categoryId}/sort`,
    method: 'get',
  })
}

/** 批量保存分类内文章排序 */
export function updateArticleSort(categoryId: number, ids: number[]) {
  return request<void>({
    url: `/article/category/${categoryId}/sort`,
    method: 'put',
    data: ids,
  })
}
