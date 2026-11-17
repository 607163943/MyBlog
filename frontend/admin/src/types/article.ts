/** 文章返回实体 */
export interface ArticleVO {
  id: number
  title: string
  description: string | null
  content: string | null
  cover: string | null
  /** 所属分类 ID，0 表示无分类 */
  categoryId: number
  categoryName: string | null
  /** 分类内排序值，null 表示未编号 */
  categorySort: number | null
  /** 标签 ID 列表，列表查询不返回该字段 */
  tagIds: number[] | null
  authorId: number
  authorName: string | null
  status: number
  viewCount: number
  /** 首次发布时间，草稿为 null */
  publishTime: string | null
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
  /** 所属分类 ID，传 0 表示无分类 */
  categoryId: number
  /** 分类内排序值，不传表示由服务端按现有规则处理；无分类时不生效 */
  categorySort?: number
  tagIds?: number[]
  status: number
}

/** 文章分页查询参数 */
export interface ArticlePageQuery {
  title: string
  /** 发布状态：0-草稿，1-已发布，空串表示全部 */
  status: number | ''
  /** 所属分类 ID，空串表示全部分类 */
  categoryId: number | ''
  page: number
  pageSize: number
}
