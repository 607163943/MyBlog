import request from '@/util/request'
import type { ArchiveYearVO } from '@/types/archive'
import type { ArticleVO } from '@/types/article'

/** 查询归档概览 */
export function getArchiveOverview() {
  return request<ArchiveYearVO[]>({
    url: '/web/archive/overview',
    method: 'get',
  })
}

/** 查询指定年月的文章 */
export function getArchiveMonthArticles(year: number, month: number) {
  return request<ArticleVO[]>({
    url: `/web/archive/${year}/${month}`,
    method: 'get',
  })
}
