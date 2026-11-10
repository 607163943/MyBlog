/** 文章返回实体 */
export interface ArticleVO {
  id: number
  title: string
  description: string | null
  content: string | null
  cover: string | null
  categoryId: number
  categoryName: string | null
  authorId: number
  authorName: string | null
  status: number
  viewCount: number
  createTime: string
  updateTime: string
}

/** 文章新增 / 修改参数 */
export interface ArticleDTO {
  id?: number
  title: string
  description?: string
  content: string
  cover?: string
  categoryId: number
  status: number
}

/** 文章分页查询参数 */
export interface ArticlePageQuery {
  title: string
  /** 发布状态：0-草稿，1-已发布，空串表示全部 */
  status: number | ''
  page: number
  pageSize: number
}
