import request from '@/util/request'
import type { ArticleBriefVO, ArticleDetailVO, ArticlePageQuery, ArticleVO } from '@/types/article'
import type { PageVO } from '@/types/common'

/** 分页查询已发布文章（首页 / 分类 / 标签 / 搜索共用） */
export function getArticlePage(params: ArticlePageQuery) {
  return request<PageVO<ArticleVO>>({
    url: '/web/article/page',
    method: 'get',
    params,
  })
}

/** 查询最新发布的文章 */
export function getLatestArticles(limit = 8) {
  return request<ArticleBriefVO[]>({
    url: '/web/article/latest',
    method: 'get',
    params: { limit },
  })
}

/** 查询文章详情 */
export function getArticleDetail(id: number) {
  return request<ArticleDetailVO>({
    url: `/web/article/${id}`,
    method: 'get',
  })
}

/** 查询分类下的全部文章（文章详情页分类目录） */
export function getCategoryArticleIndex(categoryId: number) {
  return request<ArticleBriefVO[]>({
    url: `/web/category/${categoryId}/articles`,
    method: 'get',
  })
}
