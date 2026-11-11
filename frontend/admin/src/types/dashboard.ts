/** 数据总览统计卡片 */
export interface DashboardStatisticsVO {
  /** 文章总数 */
  articleTotal: number
  /** 草稿数 */
  draftCount: number
  /** 已发布数 */
  publishedCount: number
  /** 分类总数 */
  categoryCount: number
  /** 标签总数 */
  tagCount: number
}

/** 文章浏览量排行查询参数 */
export interface ArticleRankQuery {
  /** 文章标题，模糊搜索 */
  title: string
  /** 分类 ID，空表示全部 */
  categoryId?: number
  /** 标签 ID，空表示全部 */
  tagId?: number
  page: number
  pageSize: number
}

/** 文章浏览量排行 */
export interface ArticleRankVO {
  id: number
  title: string
  categoryId: number
  categoryName: string | null
  viewCount: number
  /** 标签名称列表 */
  tagNames: string[]
}

/** 分类文章数排行查询参数 */
export interface CategoryRankQuery {
  /** 分类名称，模糊搜索 */
  name: string
  /** 最小文章数（大于等于） */
  minCount?: number
  /** 最大文章数（小于等于） */
  maxCount?: number
  page: number
  pageSize: number
}

/** 分类文章数排行 */
export interface CategoryRankVO {
  id: number
  name: string
  articleCount: number
}
