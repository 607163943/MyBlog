import { request } from '@/util/request'
import type {
  ArticleRankQuery,
  ArticleRankVO,
  CategoryRankQuery,
  CategoryRankVO,
  DashboardStatisticsVO,
} from '@/types/dashboard'
import type { PageVO } from '@/types/common'

/** 查询统计卡片数据 */
export function getDashboardStatistics() {
  return request<DashboardStatisticsVO>({
    url: '/dashboard/statistics',
    method: 'get',
  })
}

/** 分页查询文章浏览量排行 */
export function getArticleRankPage(params: ArticleRankQuery) {
  return request<PageVO<ArticleRankVO>>({
    url: '/dashboard/article-rank/page',
    method: 'get',
    params,
  })
}

/** 分页查询分类文章数排行 */
export function getCategoryRankPage(params: CategoryRankQuery) {
  return request<PageVO<CategoryRankVO>>({
    url: '/dashboard/category-rank/page',
    method: 'get',
    params,
  })
}
