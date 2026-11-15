import request from '@/util/request'
import type { SiteStatisticsVO } from '@/types/site'

/** 查询站点统计信息 */
export function getSiteStatistics() {
  return request<SiteStatisticsVO>({
    url: '/web/site/statistics',
    method: 'get',
  })
}
