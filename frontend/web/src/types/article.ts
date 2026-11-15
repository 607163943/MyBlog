import type { PageQuery } from './common'

/** 文章标签简要信息 */
export interface ArticleTagVO {
  id: number
  name: string
}

/** 文章简要信息 */
export interface ArticleBriefVO {
  id: number
  title: string
  viewCount: number
}

/** 文章列表项 */
export interface ArticleVO {
  id: number
  title: string
  description: string | null
  cover: string | null
  categoryId: number
  categoryName: string | null
  tags: ArticleTagVO[]
  viewCount: number
  publishTime: string
}

/** 文章详情 */
export interface ArticleDetailVO extends ArticleVO {
  content: string
  /** 分类内排序值，null 表示未编号 */
  categorySort: number | null
  prevArticle: ArticleBriefVO | null
  nextArticle: ArticleBriefVO | null
}

/** 文章分页查询参数（首页 / 分类 / 标签 / 搜索共用） */
export interface ArticlePageQuery extends PageQuery {
  categoryId?: number
  tagId?: number
  keyword?: string
}
